<!-- src/views/Upload.vue -->
<template>
  <div class="upload-container">
    <el-upload
      class="upload-demo"
      drag
      action="http://localhost:9090/files/upload"
      :headers="headers"
      :with-credentials="true"
      multiple
      :on-success="onSuccess"
      :on-error="onError"
    >
      <el-icon><UploadFilled /></el-icon>
      <div class="el-upload__text">
        Drag files here or <em>click to upload</em>
      </div>
      <div class="el-upload__tip">Supports multiple file uploads.</div>
    </el-upload>
  </div>
</template>

<script>
import { UploadFilled } from '@element-plus/icons-vue'
import "../styles/Upload.css"
export default {
  name: 'Upload',
  components: { UploadFilled },
  data() {
    return {
      headers: {
        // ...existing headers if needed...
      }
    }
  },
  methods: {
    onSuccess(response, file) {
      this.$message.success(`${file.name} successfully uploaded`)
    },
    onError(error, file) {
      this.$message.error(`${file.name} failed to upload`)
    },
  },
}
</script>
