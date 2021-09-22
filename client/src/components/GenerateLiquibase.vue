<template>
  <div>
    <h1 class="title">Generate liquibase</h1>
    <section class="GenerateApp">
      <div>
        <div>
          <div><h2 class="title">Заполните поля и прикрепите файл с данными:</h2></div>
          <div class="form-group">
            <label class="form__label">Что будем делать?</label>
            <div>
              <input type="radio" value="/generateInsert" name="Insert" @change="selectAction($event.target.value)" v-model="action" id="insert"/>
              <label for="insert">Insert</label>
            </div>
            <div>
              <input type="radio" value="/generateUpdate" name="Update" @change="selectAction($event.target.value)" v-model="action" id="update"/>
              <label for="update">Update</label>
            </div>
          </div>
          <div v-if="action !== ''" class="form-group" :class="{ 'form-group--error': $v.tableName.$error }">
            <label class="form__label">Название таблицы</label>
            <div>
              <input class="form__input" v-model.trim="tableName" @input="setTableName($event.target.value)"/>
            </div>
            <div class="error2" v-if="$v.tableName.$dirty">Заполни поле</div>
          </div>
          <div v-if="action !== ''" class="form-group" :class="{ 'form-group--error': $v.schemaName.$error }">
            <label class="form__label">Название схемы</label>
            <div>
              <input class="form__input" v-model.trim="schemaName" @input="setSchemaName($event.target.value)"/>
            </div>
            <div class="error2" v-if="$v.schemaName.$dirty">Заполни поле</div>
          </div>
        </div>
        <br />
        <div>
          <label>
            <input type="file" ref="file" @change="selectFile"/>
          </label>
          <br/>
          <br/>
          <button @click="GenerateLiquibase">
            Загрузить
          </button>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import api from '../Api';
import {required} from 'vuelidate/lib/validators'
import Vue from "vue";


// app Vue instance
const GenerateLiquibase = {
  name: 'GenerateLiquibase',
  props: {},

  // app initial state
  data: function () {
    return {
      action: '',
      selectedFiles: null,
      currentFile: undefined,
      tableName: '',
      schemaName: '',
      submitStatus: 'ERROR',
    }
  },
  validations: {
    tableName: {
      required
    },
    schemaName: {
      required
    }
  },

  methods: {
    GenerateLiquibase: async function () {
      Vue.$log.debug("Selected action: " + this.action)
      Vue.$log.debug("Table name: " + this.tableName)
      Vue.$log.debug("Schema name: " + this.schemaName)

      this.currentFile = this.selectedFiles.item(0);
      let result = await api.upload(this.currentFile, this.tableName, this.schemaName, this.action);
      let _this = this
      this.$v.$reset();
      if (result instanceof Array) {
        result.forEach(function(item) {
          if(item[1].fieldName === "tableName") {
            _this.$v.tableName.$touch()
          }
          if(item[1].fieldName === "schemaName") {
            _this.$v.schemaName.$touch()
          }
        });
      }
    },
    selectFile() {
      this.selectedFiles = this.$refs.file.files;
    },
    selectAction(value) {
      this.action = value;
    },

    setTableName(value) {
      this.tableName = value
      this.$v.tableName.$touch()
      if (this.tableName === '' || this.schemaName === '')
        this.submitStatus = 'ERROR'
      else this.submitStatus = 'OK'
    },
    setSchemaName(value) {
      this.schemaName = value
      this.$v.schemaName.$touch()
      if (this.tableName === '' || this.schemaName === '')
        this.submitStatus = 'ERROR'
      else this.submitStatus = 'OK'
    }
  },

  mounted() {},

  // computed properties
  // http://vuejs.org/guide/computed.html
  computed: {},

  filters: {},

  // a custom directive to wait for the DOM to be updated
  // before focusing on the input field.
  // http://vuejs.org/guide/custom-directive.html
  directives: {}
}

export default GenerateLiquibase
</script>

<style>
[v-cloak] {
  display: none;
}
</style>
