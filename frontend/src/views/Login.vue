<template>
  <div class="login-container">
    <el-form :model="form" @submit.prevent="onSubmit">
      <el-form-item label="用户名">
        <el-input v-model="form.username" autocomplete="off" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="form.password" type="password" autocomplete="off" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">登录</el-button>
        <router-link to="/register">没有账号？注册</router-link>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from '../utils/axios'

export default {
  name: 'Login',
  data() {
    return {
      form: {
        username: '',
        password: '',
      },
    }
  },
  methods: {
    onSubmit() {
      axios
          .post(
              '/login',
              new URLSearchParams({
                username: this.form.username,
                password: this.form.password,
              })
          )
          .then((response) => {
            console.log(response)

            if (response.data.status === 200) {
              localStorage.setItem('user', this.form.username)
              this.$message.success(response.data.message)
              this.$router.push('/files')
            } else {
              this.$message.error(response.data.message)
            }
          })
          .catch((error) => {
            if (error.response && error.response.status === 401) {
              this.$message.error('登录失败，用户名或密码错误')
            } else {
              this.$message.error('登录失败')
            }
          })
    },
  },
}
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 100px auto;
}
</style>
