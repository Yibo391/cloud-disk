<template>
  <div class="file-list">
    <el-table :data="files" style="width: 100%">
      <el-table-column prop="fileName" label="File name" />
      <el-table-column prop="fileSize" label="File size" />
      <el-table-column prop="uploadTime" label="Upload date" />
      <el-table-column label="Operate">
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
            this.$message.error('fail to load the list')
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
            this.$message.error('fail to download file')
          })
    },
    deleteFile(fileId) {
      this.$confirm('Do you want to delete it?', 'tips', {
        type: 'warning',
      })
          .then(() => {
            axios
                .delete(`/files/delete/${fileId}`)
                .then(() => {
                  this.$message.success('deleted')
                  this.fetchFiles()
                })
                .catch(() => {
                  this.$message.error('fail to delete')
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
/*  */
</style>
