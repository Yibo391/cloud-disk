<!-- src/components/Layout.vue -->
<template>
  <el-container>
    <el-header>
      <div class="logo">Cloud Disk</div>
      <el-menu mode="horizontal" :default-active="$route.path" @select="onSelect">
        <el-menu-item index="/files">My Files</el-menu-item>
        <el-menu-item index="/upload">Upload Files</el-menu-item>
        <el-menu-item index="/logout">Logout</el-menu-item>
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
        this.$confirm('Are you sure you want to logout?', 'Confirm', {
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
