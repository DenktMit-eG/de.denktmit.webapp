package de.denktmit.webapp.common


class DataTable private constructor(
    val meta: Meta,
    private val rows: MutableList<Row>
) {

    companion object {
        fun create(vararg columnNames: String): DataTable {
            return DataTable(Meta.create(*columnNames), mutableListOf())
        }

        fun create(meta: Meta, data: Iterable<Row>): DataTable {
            return DataTable(meta, data.toMutableList())
        }
    }

    fun takeRows(range: IntRange? = null): List<Row> {
        return rows.toList()
    }

    class Meta private constructor(
        val columnNames: LinkedHashSet<String>,
    ) {

        companion object {
            fun create(vararg columnNames: String): Meta {
                val columnNameSet = LinkedHashSet<String>(columnNames.size)
                columnNames.forEach { columnNameSet.add(it) }
                return Meta(columnNameSet)
            }
        }

        fun createRow(vararg data: Any): Row {
            if (columnNames.size != data.size) {
                throw IllegalArgumentException("column size do not fit data size")
            }
            val dataMap = columnNames.mapIndexed { index, columnName ->
                columnName to data[index]
            }.toMap()
            return createRow(dataMap)
        }

        private fun createRow(data: Map<String, Any>): Row {
            if (data.keys != columnNames) {
                throw IllegalArgumentException("Keys are not the same")
            }
            return Row(this, data)
        }
    }

    class Row(private val meta: Meta, val data: Map<String, Any>) : Iterable<Any> {

        override fun iterator(): Iterator<Any> = cells(meta.columnNames).iterator()

        fun cells(vararg columnNames: String): Iterable<Any> {
            val columnNameSet = LinkedHashSet<String>(columnNames.size)
            columnNames.forEach { columnNameSet.add(it) }
            return cells(columnNameSet)
        }

        fun cells(columnNames: LinkedHashSet<String>): Iterable<Any> {
            return columnNames.mapNotNull { columnName ->
                data[columnName]
            }.toList()
        }
    }
}
