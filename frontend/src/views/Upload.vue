<template>
  <div class="upload-container">
    <el-upload
        action="http://localhost:9090/files/upload"
        :headers="headers"
        :with-credentials="true"
        multiple
        :on-success="onSuccess"
        :on-error="onError"
    >
      <el-button type="primary">选择文件</el-button>
    </el-upload>
  </div>
</template>

<script>
export default {
  name: 'Upload',
  data() {
    return {
      headers: {
        // 如果需要添加请求头，可以在这里添加
      },
    }
  },
  methods: {
    onSuccess(response, file) {
      this.$message.success(`${file.name} 上传成功`)
    },
    onError(error, file) {
      this.$message.error(`${file.name} 上传失败`)
    },
  },
}
</script>

<style scoped>
.upload-container {
  margin: 20px;
}
</style>
