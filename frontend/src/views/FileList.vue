<template>
  <div class="file-list">
    <el-table :data="files" style="width: 100%">
      <el-table-column prop="fileName" label="文件名" />
      <el-table-column prop="fileSize" label="大小" />
      <el-table-column prop="uploadTime" label="上传时间" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="small" @click="downloadFile(scope.row.id)">下载</el-button>
          <el-button size="small" type="danger" @click="deleteFile(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import axios from '../utils/axios'

export default {
  name: 'FileList',
  data() {
    return {
      files: [],
    }
  },
  created() {
    this.fetchFiles()
  },
  methods: {
    fetchFiles() {
      axios
          .get('/files/list')
          .then((response) => {
            this.files = response.data
          })
          .catch(() => {
            this.$message.error('加载文件列表失败')
          })
    },
    downloadFile(fileId) {
      axios
          .get(`/files/download/${fileId}`, { responseType: 'blob' })
          .then((response) => {
            const url = window.URL.createObjectURL(new Blob([response.data]))
            const link = document.createElement('a')
            link.href = url
            link.setAttribute('download', this.getFileName(response))
            document.body.appendChild(link)
            link.click()
          })
          .catch(() => {
            this.$message.error('下载失败')
          })
    },
    deleteFile(fileId) {
      this.$confirm('确定要删除该文件吗？', '提示', {
        type: 'warning',
      })
          .then(() => {
            axios
                .delete(`/files/delete/${fileId}`)
                .then(() => {
                  this.$message.success('文件已删除')
                  this.fetchFiles()
                })
                .catch(() => {
                  this.$message.error('删除失败')
                })
          })
          .catch(() => {})
    },
    getFileName(response) {
      const disposition = response.headers['content-disposition']
      const filename = decodeURIComponent(
          disposition.split('filename=')[1].replace(/"/g, '')
      )
      return filename
    },
  },
}
</script>

<style scoped>
/* 添加样式 */
</style>
