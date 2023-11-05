package de.denktmit.webapp.common

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration

class DataTableTest {


    @Test
    fun createTable() {
        val meta = DataTable.Meta.create("project", "user", "23/01", "23/02", "23/03")
        val rows = listOf(
            meta.createRow("süwag", "marc", Duration.ofHours(1), Duration.ofMinutes(600), Duration.ofDays(1)),
            meta.createRow("süwag", "marc", Duration.ofHours(1), Duration.ofMinutes(600), Duration.ofDays(1)),
            meta.createRow("süwag", "marc", Duration.ofHours(1), Duration.ofMinutes(600), Duration.ofDays(1)),
        )
        val dataTable = DataTable.create(meta, rows)

        println("<tr>")
        dataTable.meta.columnNames.forEach { cell ->
            println("\t<th>$cell</th>")
        }
        println("</tr>")

        dataTable.takeRows().forEach { row ->
            println("<tr>")
            row.iterator().forEach { cell ->
                println("\t<td>$cell</td>")
            }
            println("</tr>")
        }

        assertThat(dataTable).isNotNull
    }

    @Test
    fun createRow() {
        val result = DataTable.Meta.create("project").createRow("süwag")
        assertThat(result.data.values).contains("süwag")
    }

    @Test
    fun getColumnNames() {
        val result = DataTable.Meta.create("project")
        assertThat(result.columnNames).contains("project")
    }
}