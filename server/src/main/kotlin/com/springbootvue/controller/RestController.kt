package com.springbootvue.controller

import com.springbootvue.service.LiquibaseGenerator
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class RestController(private val liquibaseGenerator: LiquibaseGenerator) {

    @PostMapping("/generateLiquibase")
    fun generateLiquibase(
        @RequestParam("file") multipartFile: MultipartFile,
        @RequestParam("tableName") tableName: String,
        @RequestParam("schemaName") schemaName: String,
    ) = liquibaseGenerator.generateInsertScript(multipartFile.inputStream, tableName, schemaName)

}