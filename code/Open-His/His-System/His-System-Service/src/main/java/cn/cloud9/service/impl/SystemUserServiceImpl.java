package cn.cloud9.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cloud9.mapper.SystemUserMapper;
import cn.cloud9.domain.SystemUser;
import cn.cloud9.service.SystemUserService;
@Service
public class SystemUserServiceImpl
    extends ServiceImpl<SystemUserMapper, SystemUser>
    implements SystemUserService {


    @Override
    public SystemUser queryUserByPhone(String phone) {
        return this.baseMapper.selectOne(
            new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getPhone, phone)
        );
    }

    @Override
    public SystemUser getOne(Long userId) {
        return this.baseMapper.selectById(userId);
    }
}
