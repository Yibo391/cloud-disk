<template>
  <div class="register-container">
    <el-form :model="form" @submit.prevent="onSubmit">
      <el-form-item label="用户名">
        <el-input v-model="form.username" autocomplete="off" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="form.password" type="password" autocomplete="off" />
      </el-form-item>
      <el-form-item label="确认密码">
        <el-input v-model="form.confirmPassword" type="password" autocomplete="off" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">注册</el-button>
        <router-link to="/login">已有账号？登录</router-link>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from '../utils/axios'

export default {
  name: 'Register',
  data() {
    return {
      form: {
        username: '',
        password: '',
        confirmPassword: '',
      },
    }
  },
  methods: {
    onSubmit() {
      if (this.form.password !== this.form.confirmPassword) {
        this.$message.error('两次输入的密码不一致')
        return
      }
      axios
          .post(
              '/register',
              new URLSearchParams({
                username: this.form.username,
                password: this.form.password,
              })
          )
          .then(() => {
            this.$message.success('注册成功')
            this.$router.push('/login')
          })
          .catch(() => {
            this.$message.error('注册失败')
          })
    },
  },
}
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: 100px auto;
}
</style>
