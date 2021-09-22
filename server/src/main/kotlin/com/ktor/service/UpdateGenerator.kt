package com.ktor.service

import com.ktor.utils.Regex.Companion.DATE_REGEX
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream

class UpdateGenerator {
    /**
     * excel формат: первая колонка первая строка - ключ, по которому будут применяться изменения, вертикальные ячейки первой колонки - значения ключа
     * остальные колонки - первая строка: название колонки в базе, данные ячейки: новое значение
     */
    fun generateUpdate(inputStream: InputStream, tableName: String, schemaName: String): String {
        println("Generate update for $tableName, $schemaName")
        val result = StringBuilder()
        OPCPackage.open(inputStream).use {
            val sheet = XSSFWorkbook(it).getSheetAt(0)
            val rowsRange = sheet.firstRowNum + 1..sheet.lastRowNum
            val columnsRange = sheet.getRow(0).firstCellNum+1 until sheet.getRow(0).lastCellNum

            for (row in rowsRange) {
                result.appendLine(
                    """
                            <update tableName="$tableName" schemaName="$schemaName">
                        """.trimIndent()
                )
                val columns = mutableListOf<Pair<XSSFCell, XSSFCell>>()
                for (cell in columnsRange) {
                    sheet.getCellInRow(row, cell)?.let { cellContent ->
                        columns.add(Pair(sheet.getCellInRow(0, cell), cellContent))
                    }
                }
                val where = Pair(sheet.getCellInRow(0, 0), sheet.getCellInRow(row, 0))

                columns.forEach {
                    result.appendLine("\t<column name=\"${getValueFromCell(it.first)}\" value=\"${getValueFromCell(it.second)}\"/>")
                }
                result.appendLine("\t<where>${getValueFromCell(where.first)}='${getValueFromCell(where.second)}'</where>")

                result.appendLine("</update>")
            }
        }
        return result.toString()
    }

    private fun getValueFromCell(cellValue: XSSFCell): String {
        var value = ""

        when (cellValue.cellType) {
            CellType.STRING -> {
                value = cellValue.stringCellValue.trim()
                if (DATE_REGEX.matches(value)) {
                    val dateParts = value.replace("/", ".").split('.')
                    val month = if (dateParts[0].length == 1)
                        "0${dateParts[0]}"
                    else dateParts[0]
                    value = "${dateParts[2]}-$month-${dateParts[1]}"
                }
            }
            CellType.NUMERIC -> value = cellValue.numericCellValue.toString()
            else -> println("Unknown cell type")
        }
        return value
    }

    private fun XSSFSheet.getCellInRow(row: Int, cell: Int) = this.getRow(row).getCell(cell)
}
