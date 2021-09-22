package com.ktor.routing

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.core.valid
import com.ktor.service.InsertGenerator
import com.ktor.service.UpdateGenerator
import com.ktor.validation.RequestParam
import com.ktor.validation.Rules
import com.ktor.validation.Strategy
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray

fun Application.configureRouting() {
    val insertGenerator = InsertGenerator()
    val updateGenerator = UpdateGenerator()
    routing {

        post("/generateInsert") {
            val tableName = call.parameters["tableName"]
            val schemaName = call.parameters["schemaName"]
//            require(tableName != null && tableName.isNotEmpty()) {
//                "Table name must not be empty"
//            }
//            require(schemaName != null && schemaName.isNotEmpty()) {
//                "Schema name must not be empty"
//            }
            call.receiveMultipart().forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        val result = insertGenerator.generateInsertScript(
                            part.streamProvider(),
                            tableName!!,
                            schemaName!!
                        )
                        call.respondText(result)
                    }
                    else -> {}
                }
            }
        }

        post("/generateUpdate") {
            val tableName = RequestParam("tableName", call.parameters["tableName"])
            val schemaName = RequestParam("schemaName", call.parameters["schemaName"])
            val validationResult = Rules(
                Strategy.ErrorAccumulation,
                listOf(
                    tableName,
                    schemaName
                )
            )
            if(validationResult.isRight()) {
                call.receiveMultipart().forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            val result = updateGenerator.generateUpdate(
                                part.streamProvider(),
                                tableName.value!!,
                                schemaName.value!!
                            )
                            call.respondText(result)
                        }
                        else -> {}
                    }
                }
            } else {
                val result = (validationResult as Either.Left).value.all
                call.respond(result)
            }
        }
    }
}