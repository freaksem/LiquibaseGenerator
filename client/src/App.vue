<template>
  <div id="app">
<!--    <Todos />-->
    <GenerateLiquibase />
  </div>
</template>

<script>
// import Todos from './components/Todos'
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
