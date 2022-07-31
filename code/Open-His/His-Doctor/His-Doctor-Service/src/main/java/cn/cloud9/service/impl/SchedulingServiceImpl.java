package cn.cloud9.service.impl;

import cn.cloud9.domain.Scheduling;
import cn.cloud9.domain.form.SchedulingForm;
import cn.cloud9.mapper.SchedulingMapper;
import cn.cloud9.service.SchedulingService;
import cn.cloud9.utils.CheckUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
@Service
public class SchedulingServiceImpl extends ServiceImpl<SchedulingMapper, Scheduling> implements SchedulingService {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    @Override
    public List<Scheduling> queryScheduling(Scheduling schedulingDto) {
        QueryWrapper<Scheduling> qw = new QueryWrapper<>();
        qw.eq(null != schedulingDto.getUserId(), Scheduling.COL_USER_ID, schedulingDto.getUserId());
        qw.eq(null != schedulingDto.getDeptId(), Scheduling.COL_DEPT_ID, schedulingDto.getDeptId());
        qw.ge(StringUtils.isNotBlank(schedulingDto.getBeginDate()), Scheduling.COL_SCHEDULING_DAY, schedulingDto.getBeginDate());
        qw.le(StringUtils.isNotBlank(schedulingDto.getEndDate()), Scheduling.COL_SCHEDULING_DAY, schedulingDto.getEndDate());
        return this.baseMapper.selectList(qw);
    }

    @Override
    public int saveScheduling(SchedulingForm schedulingFormDto) {
        // 1、校验数据
        final List<SchedulingForm.SchedulingData> schedulingData = schedulingFormDto.getData();
        if (CollectionUtil.isEmpty(schedulingData)) return 0;
        final SchedulingForm.SchedulingData schedulingData2 = schedulingData.get(0);
        if (CheckUtil.isEmpty(schedulingData2.getUserId())) return 0;

        // 2、获取参照日期所在周的开始日期和结束日期
        DateTime refDateTime = DateUtil.parse(schedulingFormDto.getBeginDate(), YYYY_MM_DD);
        String formatBegin = DateUtil.format(DateUtil.beginOfWeek(refDateTime), YYYY_MM_DD);
        String formatEnd = DateUtil.format(DateUtil.endOfDay(refDateTime), YYYY_MM_DD);

        // 3、获取首个用户信息
        Long userId2 = schedulingData2.getUserId();
        Long deptId2 = schedulingData2.getDeptId();

        // 4、根据用户信息删除对应排班
        QueryWrapper<Scheduling> qw = new QueryWrapper<>();
        qw.eq(Scheduling.COL_USER_ID, userId2);
        qw.eq(Scheduling.COL_DEPT_ID, deptId2);
        qw.between(Scheduling.COL_SCHEDULING_DAY, formatBegin, formatEnd);
        final int delete = this.baseMapper.delete(qw);

        // 5、添加新的排版，取那周的开始日期
        List<String> schedulingWeeks = initSchedulingDay(DateUtil.beginOfWeek(refDateTime));
        schedulingData.forEach(sd -> {
            final Collection<String> sdSchedulingType = sd.getSchedulingTypeList();
            int iterater = 0;
            for (String schedulingType : sdSchedulingType) {
                if (StringUtils.isNotBlank(schedulingType)) {
                    this.baseMapper.insert(new Scheduling(
                            userId2,
                            deptId2,
                            schedulingWeeks.get(iterater),
                            sd.getSubsectionType(),
                            schedulingType,
                            new Date(),
                            schedulingFormDto.getSimpleUser().getUserName()
                    ));
                }
                iterater++;
            }
        });
        return 1;

//        if (null != schedulingFormDto.getData() && schedulingFormDto.getData().size() > 0) {
//            DateTime dateTime = DateUtil.parse(schedulingFormDto.getBeginDate(), YYYY_MM_DD);
//            DateTime date = DateUtil.beginOfWeek(dateTime);
//            String beginDate = DateUtil.format(date, YYYY_MM_DD);
//            String endDate = DateUtil.format(DateUtil.endOfWeek(dateTime), YYYY_MM_DD);
//            SchedulingForm.SchedulingData schedulingData = schedulingFormDto.getData().get(0);
//            Long userId = schedulingData.getUserId();
//            Long deptId = schedulingData.getDeptId();
//            if (null != userId) {
//                //删除原来的排班
//                QueryWrapper<Scheduling> qw = new QueryWrapper<>();
//                qw.eq(Scheduling.COL_USER_ID, userId);
//                qw.eq(Scheduling.COL_DEPT_ID, deptId);
//                qw.between(Scheduling.COL_SCHEDULING_DAY, beginDate, endDate);
//                this.baseMapper.delete(qw);
//                //添加新的排班
//                List<String> schedulingDays = initSchedulingDay(date);
//                for (SchedulingForm.SchedulingData d : schedulingFormDto.getData()) {
//                    Scheduling scheduling;
//                    int i = 0;
//                    for (String s : d.getSchedulingType()) {
//                        if (StringUtils.isNotBlank(s)) {
//                            scheduling = new Scheduling(userId, deptId, schedulingDays.get(i), d.getSubsectionType(), s, new Date(), schedulingFormDto.getSimpleUser().getUserName());
//                            this.baseMapper.insert(scheduling);
//                        }
//                        i++;
//                    }
//                }
//                return 1;//受影响的行数
//            } else {
//                return 0;
//            }
//        } else {
//            return 0;
//        }
    }

    /**
     * 初始化值班记录
     *
     * @param startDate 当周第一天
     * @return
     */
    private static List<String> initSchedulingDay(Date startDate) {
        final int WEEK_LENGTH = 7;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < WEEK_LENGTH; i++) {
            DateTime d = DateUtil.offsetDay(startDate, i);
            String key = DateUtil.format(d, YYYY_MM_DD);
            list.add(key);
        }
        return list;
    }

    @Override
    public List<Long> queryHasSchedulingDeptIds(Long deptId, String schedulingDay, String schedulingType, String subsectionType) {
        return this.baseMapper.queryHasSchedulingDeptIds(deptId,schedulingDay,schedulingType,subsectionType);
    }
}
