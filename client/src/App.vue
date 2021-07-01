<template>
  <div id="app">
    <GenerateLiquibase />
  </div>
</template>

<script>
import GenerateLiquibase from './components/GenerateLiquibase'
// app Vue instance
const app = {
  name: 'app',
  components: {
    GenerateLiquibase
  },
  // app initial state
  data: () => {
    return {
    }
  }
}

export default app
</script>

<style>
[v-cloak] { display: none; }
</style>
