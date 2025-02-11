<!-- src/components/Layout.vue -->
<template>
  <el-container>
    <el-header>
      <div class="logo">Cloud Disk</div>
      <el-menu mode="horizontal" :default-active="$route.path" @select="onSelect">
        <el-menu-item index="/files">
          <el-icon><Folder /></el-icon>
          My Files
        </el-menu-item>
        <el-menu-item index="/upload">
          <el-icon><UploadFilled /></el-icon>
          Upload Files
        </el-menu-item>
        <el-menu-item index="/logout">
          <el-icon><CloseBold /></el-icon>
          Logout
        </el-menu-item>
      </el-menu>
    </el-header>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>

<script>
import axios from '../utils/axios'
import { Folder, UploadFilled, CloseBold } from '@element-plus/icons-vue'
import "../styles/Layout.css"
export default {
  name: 'Layout',
  components: { Folder, UploadFilled, CloseBold },
  methods: {
    onSelect(index) {
      if (index === '/logout') {
        this.$confirm('Are you sure you want to logout?', 'Confirm', { type: 'warning' })
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
