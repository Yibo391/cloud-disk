<!-- src/views/Register.vue -->
<template>
  <div class="auth-container">
    <el-form
        :model="form"
        @submit.prevent="onSubmit"
        ref="registerForm"
        status-icon
        class="auth-form"
    >
      <h2 class="auth-title">Register</h2>
      <el-form-item
          prop="username"
          :rules="[{ required: true, message: 'Please enter username', trigger: 'blur' }]"
      >
        <el-input
            v-model="form.username"
            placeholder="Username"
            autocomplete="off"
        >
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>
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
        >
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item
          prop="confirmPassword"
          :rules="[
          { required: true, message: 'Please confirm your password', trigger: 'blur' },
          { validator: validatePassword, trigger: 'blur' },
        ]"
      >
        <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="Confirm Password"
            autocomplete="off"
        >
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit" class="auth-button">
          Register
        </el-button>
      </el-form-item>
      <div class="switch-link">
        Already have an account? <router-link to="/login">Login here</router-link>
      </div>
    </el-form>
  </div>
</template>

<script>
import axios from '../utils/axios'
import { User, Lock } from '@element-plus/icons-vue'

export default {
  name: 'Register',
  components: {
    User,
    Lock,
  },
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
              .then(() => {
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
.auth-container {
  max-width: 400px;
  margin: 80px auto;
  padding: 40px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.auth-title {
  text-align: center;
  margin-bottom: 30px;
}

.auth-form {
  width: 100%;
}

.el-input {
  border-radius: 8px;
}

.auth-button {
  width: 100%;
  border-radius: 8px;
}

.switch-link {
  text-align: center;
  margin-top: 20px;
}
</style>
