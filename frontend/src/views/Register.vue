<!-- src/views/Register.vue -->
<template>
  <div class="register-container">
    <el-form :model="form" @submit.prevent="onSubmit" ref="registerForm" status-icon>
      <h2 class="register-title">Register</h2>
      <el-form-item prop="username" :rules="[{ required: true, message: 'Please enter username', trigger: 'blur' }]">
        <el-input v-model="form.username" placeholder="Username" autocomplete="off" />
      </el-form-item>
      <el-form-item prop="password" :rules="[{ required: true, message: 'Please enter password', trigger: 'blur' }]">
        <el-input v-model="form.password" type="password" placeholder="Password" autocomplete="off" />
      </el-form-item>
      <el-form-item
          prop="confirmPassword"
          :rules="[
          { required: true, message: 'Please confirm your password', trigger: 'blur' },
          { validator: validatePassword, trigger: 'blur' },
        ]"
      >
        <el-input v-model="form.confirmPassword" type="password" placeholder="Confirm Password" autocomplete="off" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit" style="width: 100%;">Register</el-button>
      </el-form-item>
      <div class="login-link">
        Already have an account? <router-link to="/login">Login here</router-link>
      </div>
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
    validatePassword(rule, value, callback) {
      if (value !== this.form.password) {
        callback(new Error('Passwords do not match'))
      } else {
        callback()
      }
    },
    onSubmit() {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          axios
              .post(
                  '/register',
                  new URLSearchParams({
                    username: this.form.username,
                    password: this.form.password,
                  })
              )
              .then((response) => {
                this.$message.success('Registration successful')
                this.$router.push('/login')
              })
              .catch(() => {
                this.$message.error('Registration failed')
              })
        }
      })
    },
  },
}
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: 100px auto;
  padding: 40px;
  background-color: #fff;
  border-radius: 8px;
}
.register-title {
  text-align: center;
  margin-bottom: 30px;
}
.login-link {
  text-align: center;
  margin-top: 20px;
}
</style>
