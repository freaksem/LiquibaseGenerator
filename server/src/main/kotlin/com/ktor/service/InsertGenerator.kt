package com.ktor.service

import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream
import java.lang.StringBuilder

class InsertGenerator {
    fun generateInsertScript(inputStream: InputStream, tableName: String, schemaName: String): String {
        println("Generate for $tableName, $schemaName")
        val result = StringBuilder()
        OPCPackage.open(inputStream).use {
            val sheet = XSSFWorkbook(it).getSheetAt(0)
            val columnNames = sheet.getRow(0)
            val rowsRange = sheet.firstRowNum + 1..sheet.lastRowNum
            val cellsRange = columnNames.firstCellNum until columnNames.lastCellNum
            val dateRegex = """^(\d{1,2}\.\d{1,2}\.\d{4})|(\d{1,2}\/\d{1,2}\/\d{4})$""".toRegex()

            for (row in rowsRange) {
                result.appendLine(
                        """
                            <insert tableName="$tableName" schemaName="$schemaName">
                        """.trimIndent()
                )

                for (cell in cellsRange) {
                    result.append("\t<column name=\"${columnNames.getCell(cell)}\">")
                    when (sheet.getCellInRow(row, cell).cellType) {
                        CellType.STRING -> {
                            var value = sheet.getCellInRow(row, cell).stringCellValue.trim()
                            if(dateRegex.matches(value)) {
                                val dateParts = value.replace("/", ".").split('.')
                                val month = if (dateParts[0].length == 1)
                                    "0${dateParts[0]}"
                                else dateParts[0]
                                value = "${dateParts[2]}-$month-${dateParts[1]}"
                            }
                            result.append(value)
                        }
                        CellType.NUMERIC -> result.append(sheet.getCellInRow(row, cell).numericCellValue)
                        else -> println("Unknown cell type")
                    }
                    result.appendLine("</column>")
                }
                result.appendLine("</insert>")
            }
        }
        return result.toString()
    }

    private fun XSSFSheet.getCellInRow(row: Int, cell: Int) = this.getRow(row).getCell(cell)
}
