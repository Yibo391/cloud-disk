<template>
  <div class="home">
    <div class="hero">
      <h1 class="logo">Cloud Disk</h1>
      <p class="slogan">Store, search, and share your files instantly.</p>
      <div class="search-container">
        <input type="text" v-model="query" @keyup.enter="onSearch" placeholder="Search for files..." />
        <button @click="onSearch">Search</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return { query: '' }
  },
  methods: {
    onSearch() {
      // Extend search functionality or navigate to file list
      this.$router.push('/files')
    }
  }
}
</script>

<style src="../styles/Home.css"></style>
