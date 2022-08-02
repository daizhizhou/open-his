<template>
  <div>
    <el-form ref="form" :model="userInfo" label-width="80px">

      <el-form-item label="电子邮箱">
        <el-input v-model="userInfo.email" />
      </el-form-item>

      <el-form-item label="注册手机">
        <el-input v-model="userInfo.phone" />
      </el-form-item>

      <el-form-item label="用户名称">
        <el-input v-model="userInfo.userName" />
      </el-form-item>

      <el-form-item label="所属部门">
        <el-input v-model="userInfo.deptId" />
      </el-form-item>

      <el-form-item label="登录时间">
        <el-input v-model="userInfo.lastLoginTime" />
      </el-form-item>

      <el-form-item label="登录IP">
        <el-input v-model="userInfo.lastLoginIp" />
      </el-form-item>

      <el-form-item label="学历背景">
        <el-input v-model="userInfo.background" />
      </el-form-item>

      <el-form-item label="账号状态">
        <el-input v-model="userInfo.status" />
      </el-form-item>

      <el-form-item label="账号类型">
        <el-input v-model="userInfo.userType" />
      </el-form-item>

      <el-form-item label="用户性别">
        <el-input v-model="userInfo.sex" />
      </el-form-item>

      <el-form-item label="用户年龄">
        <el-input v-model="userInfo.age" />
      </el-form-item>

      <el-form-item label="医术擅长">
        <el-input v-model="userInfo.strong" />
      </el-form-item>

      <el-form-item label="自我简介">
        <el-input v-model="userInfo.introduction" />
      </el-form-item>

      <el-form-item label="医师级别">
        <el-input v-model="userInfo.userRank" />
      </el-form-item>

      <el-form-item label="OpenId">
        <el-input v-model="userInfo.openId" />
      </el-form-item>

      <el-form-item label="医术荣誉">
        <el-input v-model="userInfo.honor" />
      </el-form-item>

      <el-form-item label="参与排班">
        <el-input v-model="userInfo.schedulingFlag" />
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
export default {
  name: 'UserInfo',
  data() {
    return {
      userInfo: null
    }
  },
  async created() {
    const { data: res } = await getCurrentUserInfo()
    console.log(JSON.stringify(res))
    this.userInfo = res
  },
  methods: {
    submit() {

    },
    cancel() {
      this.$parent.$parent.userInfoVisible = false
    }
  }

}
</script>

