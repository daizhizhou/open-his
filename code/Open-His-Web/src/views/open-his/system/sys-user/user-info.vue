<template>
  <div class="form-container">
    <el-form ref="form" :rules="formRule" :inline="true" :model="userInfo" label-position="right" label-width="80px">

      <el-form-item label="电子邮箱" prop="email">
        <el-input v-model.trim="userInfo.email" clearable :style="commonWidth" />
      </el-form-item>

      <el-form-item label="注册手机" prop="phone">
        <el-input v-model="userInfo.phone" clearable :style="commonWidth" />
      </el-form-item>

      <el-form-item label="用户名称" prop="userName">
        <el-input v-model="userInfo.userName" clearable :style="commonWidth" />
      </el-form-item>

      <el-form-item label="所属部门" prop="deptId">
        <el-select v-model="userInfo.deptId" placeholder="请选择" :style="commonWidth">
          <el-option
            v-for="item in deptList"
            :key="item.deptId"
            :label="item.deptName"
            :value="item.deptId"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="学历背景" prop="background">
        <el-select v-model="userInfo.background" placeholder="请选择" :style="commonWidth">
          <el-option
            v-for="item in backgroundTypeList"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="用户性别" prop="sex">
        <el-select v-model="userInfo.sex" placeholder="请选择" :style="commonWidth">
          <el-option
            v-for="item in genderTypeList"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="用户年龄" prop="age">
        <el-input v-model="userInfo.age" clearable type="number" :style="commonWidth" />
      </el-form-item>

      <el-form-item label="医师级别" prop="userRank">
        <el-select v-model="userInfo.userRank" placeholder="请选择" :style="commonWidth">
          <el-option
            v-for="item in userLevelList"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="参与排班" prop="schedulingFlag">
        <el-select v-model="userInfo.schedulingFlag" placeholder="请选择" :style="commonWidth">
          <el-option
            v-for="item in statusTypeList"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="账号状态">
        <el-select v-model="userInfo.status" placeholder="请选择" disabled :style="commonWidth">
          <el-option
            v-for="item in accountStatusTypeList"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="账号类型">
        <el-select v-model="userInfo.userType" placeholder="请选择" disabled :style="commonWidth">
          <el-option
            v-for="item in accountTypeList"
            :key="item.dictValue"
            :label="item.dictLabel"
            :value="item.dictValue"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="登录时间">
        <el-input v-model="userInfo.lastLoginTime" disabled :style="commonWidth" />
      </el-form-item>

      <el-form-item label="登录IP">
        <el-input v-model="userInfo.lastLoginIp" disabled :style="commonWidth" />
      </el-form-item>

      <el-form-item label="OpenId">
        <el-input v-model="userInfo.openId" disabled :style="commonWidth" />
      </el-form-item>

      <el-form-item label="医术荣誉" prop="honor">
        <el-input v-model="userInfo.honor" type="textarea" clearable :style="commonWidth" />
      </el-form-item>

      <el-form-item label="医术擅长" prop="strong">
        <el-input v-model="userInfo.strong" type="textarea" clearable :style="commonWidth" />
      </el-form-item>

      <el-form-item label="自我简介" prop="introduction">
        <el-input v-model="userInfo.introduction" type="textarea" clearable :style="commonWidth" />
      </el-form-item>

      <div align="center">
        <el-button type="primary" @click="submit">更新</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>

import {
  getCurrentUserInfo
} from '@/api/user'
import {
  updateUser
} from '@/api/system/sys-user'
import {
  selectAllDept
} from '@/api/system/department'

import {
  getDataByType
} from '@/api/system/dict/data'

export default {
  name: 'UserInfo',
  data() {
    return {
      userInfo: {
        email: ''
      },
      deptList: [],
      genderTypeList: [],
      backgroundTypeList: [],
      statusTypeList: [],
      userLevelList: [],
      accountTypeList: [
        { dictLabel: '超级管理员', dictValue: '0' },
        { dictLabel: '系统用户', dictValue: '1' }
      ],
      accountStatusTypeList: [
        { dictLabel: '正常', dictValue: '0' },
        { dictLabel: '停用', dictValue: '1' }
      ],
      commonWidth: { width: '220px' },
      formRule: {
        email: [
          { required: true, message: '请输入电子邮箱', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入注册手机', trigger: 'blur' }
        ],
        userName: [
          { required: true, message: '请输入用户名称', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.initialDictData()
    this.initialDepartmentData()
    this.initialUserInfoData()
  },
  methods: {
    async initialUserInfoData() {
      const { data: res } = await getCurrentUserInfo()
      console.log(JSON.stringify(res))
      this.userInfo = res
    },
    async initialDepartmentData() {
      const { data: resList } = await selectAllDept()
      this.deptList = resList
    },
    // 加载数据字典选项
    async initialDictData() {
      const { data: genderType } = await getDataByType('sys_user_sex')
      this.genderTypeList = genderType

      const { data: backgroundType } = await getDataByType('sys_user_background')
      this.backgroundTypeList = backgroundType

      const { data: statusType } = await getDataByType('sys_yes_no')
      this.statusTypeList = statusType

      const { data: userLevel } = await getDataByType('sys_user_level')
      this.userLevelList = userLevel
    },
    submit() {
      this.$refs['form'].validate(async valid => {
        if (!valid) return

        const { code } = await updateUser(this.userInfo)
        if (code === 200) {
          this.$message.success('更新成功！')
          this.$parent.$parent.userInfoVisible = false
        }
      })
    },
    cancel() {
      this.$parent.$parent.userInfoVisible = false
    }
  }

}
</script>

<style scoped>
.form-container {
  margin: 0 15px;
}
</style>

