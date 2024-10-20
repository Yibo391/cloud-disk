<!-- src/views/FileList.vue -->
<template>
  <div class="file-list">
    <el-table :data="files" style="width: 100%">
      <el-table-column prop="fileName" label="File Name" />
      <el-table-column prop="fileSize" label="File Size" />
      <el-table-column prop="uploadTime" label="Upload Date" />
      <el-table-column label="Actions" align="center">
        <template #default="scope">
          <el-button
              icon
              circle
              size="small"
              @click="downloadFile(scope.row.id)"
          >
            <el-icon><Download /></el-icon>
          </el-button>
          <el-button
              icon
              circle
              size="small"
              type="danger"
              @click="deleteFile(scope.row.id)"
          >
            <el-icon><Delete /></el-icon>
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import axios from '../utils/axios'
import { Download, Delete } from '@element-plus/icons-vue'

export default {
  name: 'FileList',
  components: {
    Download,
    Delete,
  },
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
            this.$message.error('Failed to load the list')
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
            this.$message.error('Failed to download file')
          })
    },
    deleteFile(fileId) {
      this.$confirm('Do you want to delete it?', 'Confirm', {
        type: 'warning',
      })
          .then(() => {
            axios
                .delete(`/files/delete/${fileId}`)
                .then(() => {
                  this.$message.success('File deleted')
                  this.fetchFiles()
                })
                .catch(() => {
                  this.$message.error('Failed to delete file')
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
.file-list {
  padding: 20px;
}

.el-button {
  margin: 0 5px;
  border-radius: 50%;
}

.el-button--danger {
  background-color: #f56c6c;
  border-color: #f56c6c;
  color: #fff;
}

.el-icon {
  font-size: 16px;
}
</style>
