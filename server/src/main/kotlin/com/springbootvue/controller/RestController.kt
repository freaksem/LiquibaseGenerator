package com.springbootvue.controller

import com.springbootvue.service.InsertGenerator
import com.springbootvue.service.UpdateGenerator
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class RestController(
    private val insertGenerator: InsertGenerator,
    private val updateGenerator: UpdateGenerator
) {

    @PostMapping("/generateInsert")
    fun generateInsert(
        @RequestParam("file") multipartFile: MultipartFile,
        @RequestParam("tableName") tableName: String,
        @RequestParam("schemaName") schemaName: String,
    ) = insertGenerator.generateInsertScript(multipartFile.inputStream, tableName, schemaName)

    @PostMapping("/generateUpdate")
    fun generateUpdate(
        @RequestParam("file") multipartFile: MultipartFile,
        @RequestParam("tableName") tableName: String,
        @RequestParam("schemaName") schemaName: String,
    ) = updateGenerator.generateUpdate(multipartFile.inputStream, tableName, schemaName)
}
