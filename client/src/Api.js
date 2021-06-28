import axios from 'axios'

const SERVER_URL = 'http://localhost:9000';

const instance = axios.create({
  baseURL: SERVER_URL,
  timeout: 1000
});

export default {
  // (C)reate
  createNew: (text, completed) => instance.post('todos', {title: text, completed: completed}),
  // (R)ead
  getAll: () => instance.get('todos', {
    transformResponse: [function (data) {
      return data ? JSON.parse(data)._embedded.todos : data;
    }]
  }),
  // (U)pdate
  updateForId: (id, text, completed) => instance.put('todos/' + id, {title: text, completed: completed}),
  // (D)elete
  removeForId: (id) => instance.delete('todos/' + id),

  upload(file, tableName, schemaName) {
    let formData = new FormData();

    formData.append("file", file);
    formData.append("tableName", tableName);
    formData.append("schemaName", schemaName);

    return instance.post("/generateLiquibase", formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    }).then(response => {
      let blob = new Blob([response.data], { type: "text/plain; charset=utf-8" })

      let link = document.createElement('a')
      link.href = window.URL.createObjectURL(blob)
      link.download = 'result.txt'
      link.click()
    });
  }
}
