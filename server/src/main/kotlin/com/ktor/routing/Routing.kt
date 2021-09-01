package com.ktor.routing

import com.ktor.service.InsertGenerator
import com.ktor.service.UpdateGenerator
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting() {
    val insertGenerator = InsertGenerator()
    val updateGenerator = UpdateGenerator()
    routing {

        post("/generateInsert") {
            val tableName = call.parameters["tableName"]
            val schemaName = call.parameters["schemaName"]
            require(tableName != null && tableName.isNotEmpty()) {
                "Table name must not be empty"
            }
            require(schemaName != null && schemaName.isNotEmpty()) {
                "Schema name must not be empty"
            }
            call.receiveMultipart().forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        val result = insertGenerator.generateInsertScript(
                            part.streamProvider(),
                            tableName,
                            schemaName
                        )
                        call.respondText(result)
                    }
                    else -> {}
                }
            }
        }

        post("/generateUpdate") {
            val tableName = call.parameters["tableName"]
            val schemaName = call.parameters["schemaName"]
            require(tableName != null && tableName.isNotEmpty()) {
                "Table name must not be empty"
            }
            require(schemaName != null && schemaName.isNotEmpty()) {
                "Schema name must not be empty"
            }
            call.receiveMultipart().forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        val result = updateGenerator.generateUpdate(
                            part.streamProvider(),
                            tableName,
                            schemaName
                        )
                        call.respondText(result)
                    }
                    else -> {}
                }
            }
        }
    }
}