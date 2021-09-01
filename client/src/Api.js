import axios from 'axios'

const SERVER_URL = 'http://127.0.0.1:9000';

const instance = axios.create({
  baseURL: SERVER_URL,
  timeout: 100000
});

export default {
  upload(file, tableName, schemaName, url) {
    let formData = new FormData();
    formData.append("file", file);

    return instance.post(url+"?tableName="+tableName+"&schemaName="+schemaName, formData, {
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
