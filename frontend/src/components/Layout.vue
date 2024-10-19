<template>
  <el-container>
    <el-header>
      <div class="logo">云盘</div>
      <el-menu mode="horizontal" :default-active="$route.path" @select="onSelect">
        <el-menu-item index="/files">我的文件</el-menu-item>
        <el-menu-item index="/upload">上传文件</el-menu-item>
        <el-menu-item index="/logout">退出登录</el-menu-item>
      </el-menu>
    </el-header>
    <el-main>
      <router-view></router-view>
    </el-main>
  </el-container>
</template>

<script>
import axios from '../utils/axios'

export default {
  name: 'Layout',
  methods: {
    onSelect(index) {
      if (index === '/logout') {
        this.$confirm('确定要退出登录吗？', '提示', {
          type: 'warning',
        })
            .then(() => {
              axios.post('/logout').finally(() => {
                localStorage.removeItem('user')
                this.$router.push('/login')
              })
            })
            .catch(() => {})
      } else {
        this.$router.push(index)
      }
    },
  },
}
</script>

<style scoped>
.logo {
  color: #fff;
  font-size: 20px;
  line-height: 60px;
  float: left;
  margin-right: 20px;
}
</style>
