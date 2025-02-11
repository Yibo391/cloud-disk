<template>
  <div class="file-list">
    <el-table :data="files" style="width: 100%">
      <el-table-column prop="fileName" label="File Name" />
      <el-table-column prop="fileSize" label="File Size" />
      <el-table-column prop="uploadTime" label="Upload Date" />
      <el-table-column label="Actions" align="center">
        <template #default="scope">
          <el-button icon circle size="small" @click="previewFile(scope.row)" v-if="isPreviewable(scope.row.fileName)">
            <el-icon><View /></el-icon>
          </el-button>
          <el-button icon circle size="small" @click="downloadFile(scope.row.id)">
            <el-icon><Download /></el-icon>
          </el-button>
          <el-button icon circle size="small" type="danger" @click="deleteFile(scope.row.id)">
            <el-icon><Delete /></el-icon>
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Preview Dialog -->
    <el-dialog v-model="previewVisible" :title="previewFileData.fileName" width="80%">
      <div v-if="isImage(previewFileData.fileName)">
        <img :src="previewUrl" style="max-width: 100%; max-height: 80vh;" />
      </div>
      <div v-else-if="isPdf(previewFileData.fileName)" style="height: 80vh;">
        <iframe
          :src="previewUrl"
          style="width: 100%; height: 100%; border: none;"
          :key="previewUrl"
        ></iframe>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from '../utils/axios'
import { Download, Delete, View } from '@element-plus/icons-vue'
import "../styles/FileList.css"
export default {
  name: 'FileList',
  components: { Download, Delete, View },
  data() {
    return { 
      files: [],
      previewVisible: false,
      previewFileData: {},
      previewUrl: '',
      isChrome: /Chrome/.test(navigator.userAgent) && !/Edge/.test(navigator.userAgent)
    }
  },
  created() {
    this.fetchFiles()
  },
  methods: {
    fetchFiles() {
      axios.get('/files/list')
      .then((response) => {
        this.files = response.data
      })
      .catch(() => {
        this.$message.error('Failed to load the list')
      })
    },
    downloadFile(fileId) {
      axios.get(`/files/download/${fileId}`, { responseType: 'blob' })
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
      this.$confirm('Do you want to delete it?', 'Confirm', { type: 'warning' })
      .then(() => {
        axios.delete(`/files/delete/${fileId}`)
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
      const filename = decodeURIComponent(disposition.split('filename=')[1].replace(/"/g, ''))
      return filename
    },
    isPreviewable(fileName) {
      return this.isImage(fileName) || this.isPdf(fileName)
    },
    isImage(fileName) {
      const ext = fileName.toLowerCase().split('.').pop()
      return ['jpg', 'jpeg', 'png', 'gif'].includes(ext)
    },
    isPdf(fileName) {
      return fileName.toLowerCase().endsWith('.pdf')
    },
    previewFile(file) {
      this.previewFileData = file;
      // Add timestamp to prevent caching
      const timestamp = new Date().getTime();
      this.previewUrl = `${axios.defaults.baseURL}/files/preview/${file.id}?t=${timestamp}`;
      this.previewVisible = true;
    }
  }
}
</script>

<style>
.el-dialog__body {
  padding: 10px 20px;
  overflow: hidden; /* Prevent scrollbars from iframe */
}

.pdf-fallback {
  text-align: center;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
}
</style>
