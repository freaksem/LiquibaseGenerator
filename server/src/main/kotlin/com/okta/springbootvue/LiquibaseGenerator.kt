package com.okta.springbootvue

import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import java.io.InputStream
import java.lang.StringBuilder

@Service
class LiquibaseGenerator {
    fun generateInsertScript(inputStream: InputStream, tableName: String, schemaName: String): String {
        val result = StringBuilder()
        OPCPackage.open(inputStream).use {
            val sheet = XSSFWorkbook(it).getSheetAt(0)
            val columnNames = sheet.getRow(0)
            val rowsRange = sheet.firstRowNum + 1..sheet.lastRowNum
            val cellsRange = columnNames.firstCellNum until columnNames.lastCellNum

            for (row in rowsRange) {
                result.appendLine(
                        """
                            <insert tableName="$tableName" schemaName="$schemaName">
                        """.trimIndent()
                )

                for (cell in cellsRange) {
                    result.append("\t<column name=\"${columnNames.getCell(cell)}>\"")
                    when (sheet.getCellInRow(row, cell).cellType) {
                        CellType.STRING -> result.append(sheet.getCellInRow(row, cell).stringCellValue)
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