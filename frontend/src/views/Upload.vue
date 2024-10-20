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

export default {
  name: 'Upload',
  components: {
    UploadFilled,
  },
  data() {
    return {
      headers: {
        // Add any required headers here
      },
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

<style scoped>
.upload-container {
  margin: 20px;
}

.upload-demo {
  border: 2px dashed #d9d9d9;
  border-radius: 12px;
  padding: 40px;
  text-align: center;
  cursor: pointer;
  background-color: #fafafa;
}

.upload-demo:hover {
  border-color: #409eff;
}

.el-icon {
  font-size: 50px;
  color: #409eff;
}

.el-upload__text {
  margin-top: 16px;
  font-size: 16px;
  color: #606266;
}

.el-upload__tip {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
}
</style>
