<!-- src/views/Login.vue -->
<template>
  <div class="auth-container">
    <el-form
        :model="form"
        @submit.prevent="onSubmit"
        ref="loginForm"
        status-icon
        class="auth-form"
    >
      <h2 class="auth-title">Login</h2>
      <el-form-item
          prop="username"
          :rules="[{ required: true, message: 'Please enter username', trigger: 'blur' }]"
      >
        <el-input
            v-model="form.username"
            placeholder="Username"
            autocomplete="off"
            prefix-icon="UserFilled"
        />
      </el-form-item>
      <el-form-item
          prop="password"
          :rules="[{ required: true, message: 'Please enter password', trigger: 'blur' }]"
      >
        <el-input
            v-model="form.password"
            type="password"
            placeholder="Password"
            autocomplete="off"
            prefix-icon="Lock"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit" class="auth-button">
          Login
        </el-button>
      </el-form-item>
      <div class="switch-link">
        Don't have an account? <router-link to="/register">Register now</router-link>
      </div>
    </el-form>
  </div>
</template>

<script>
import axios from '../utils/axios'
import { UserFilled, Lock } from '@element-plus/icons-vue'

export default {
  name: 'Login',
  components: {
    UserFilled,
    Lock,
  },
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
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          axios
              .post(
                  '/login',
                  new URLSearchParams({
                    username: this.form.username,
                    password: this.form.password,
                  })
              )
              .then((response) => {
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
                  this.$message.error('Login failed, incorrect username or password')
                } else {
                  this.$message.error('Login failed')
                }
              })
        }
      })
    },
  },
}
</script>
