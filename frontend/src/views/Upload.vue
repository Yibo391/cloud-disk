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
      <el-button type="primary">choose file</el-button>
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
      this.$message.success(`${file.name} Successfully uploaded`)
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
</style>
