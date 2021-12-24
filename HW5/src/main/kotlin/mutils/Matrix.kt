package mutils

class Matrix(rows: List<List<Double>>) {

    companion object {
        fun identity(size: Int): Matrix {
            val zeroRow = (0 until size).map { 0.0 }
            val finalList = zeroRow.indices.map {
                val currRow = zeroRow.toMutableList()
                currRow[it] = 1.0
                return@map currRow
            }
            return Matrix(finalList)
        }
    }

    private val listRows: List<List<Double>> = rows
    private val listCols: List<List<Double>> by lazy {
        cols.map { it.toList() }
    }
    private var privateTransposed: Matrix? = null

    val transpose: Matrix
        get() {
            if (privateTransposed == null) {
                val transposedMatrix = Matrix(
                    listRows[0].mapIndexed { index, _ ->
                        listRows.map { it[index] }
                    }
                )
                transposedMatrix.privateTransposed = this
                this.privateTransposed = transposedMatrix
            }
            return this.privateTransposed!!
        }

    val rows: List<Vector> by lazy { listRows.map { Vector(it) } }

    val cols: List<Vector>
        get() = transpose.rows

    val shape: Pair<Int, Int>

    init {
        val rowCnt = rows.size
        val colCnt = rows[0].size
        for (row in rows) {
            if (row.size != colCnt)
                error("Input matrix is not rectangular; \nFirst column count: $colCnt, irregular row: $row")
        }
        shape = Pair(rowCnt, colCnt)
    }

    fun copy(): Matrix = Matrix(listRows)

    fun toRowList(): List<List<Double>> = listRows

    fun flatRowVec(): Vector = Vector(listRows.flatten())

    fun flatRowList(): List<Double> = listRows.flatten()

    fun flatRowListFloat(): List<Float> = flatRowList().map { it.toFloat() }

    fun toColList(): List<List<Double>> = listCols

    fun flatColVec(): Vector = Vector(listCols.flatten())

    fun flatColList(): List<Double> = listCols.flatten()

    fun flatColListFloat(): List<Float> = flatColList().map { it.toFloat() }

    override fun equals(other: Any?): Boolean {
        if (other is Matrix)
            return listRows == other.listRows
        return false
    }

    override fun hashCode(): Int {
        return listRows.hashCode()
    }

    override fun toString(): String {
        var st = "Matrix:\n"
        for (row in listRows)
            st += "\t$row\n"
        return st
    }

    operator fun plus(other: Matrix): Matrix {
        if (shape != other.shape)
            error("Trying to add two matrices $this,\n $other with different shapes")
        return Matrix(rows.mapIndexed { index, vector -> (vector + other.rows[index]).toList() })
    }

    operator fun minus(other: Matrix): Matrix {
        if (shape != other.shape)
            error("Trying to add two matrices $this,\n $other with different shapes")
        return Matrix(rows.mapIndexed { index, vector -> (vector - other.rows[index]).toList() })
    }

    operator fun times(other: Matrix): Matrix {
        if (shape.second != other.shape.first)
            error("Trying to multiply two matrices $this,\n $other with not conforming shapes")
        return Matrix(rows.map { row -> other.cols.map { col -> (row * col).sum() } })
    }

    operator fun plus(vector: Vector): Matrix {
        if (shape.second != vector.size)
            error("Trying to add $vector\n to $this\n with not conforming shapes")
        return Matrix(rows.map { (it + vector).toList() })
    }

    operator fun minus(vector: Vector): Matrix {
        if (shape.second != vector.size)
            error("Trying to subtract $vector\n to $this\n with not conforming shapes")
        return Matrix(rows.map { (it - vector).toList() })
    }

    operator fun times(vector: Vector): Vector {
        if (shape.second != vector.size)
            error("Trying to right-hand multiply $this,\n $vector with not conforming shapes")
        return Vector(rows.map { (it * vector).sum() })
    }

    operator fun plus(number: Number): Matrix {
        val dNumber = number.toDouble()
        return Matrix(listRows.map { it.map { num -> num + dNumber } })
    }

    operator fun minus(number: Number): Matrix {
        val dNumber = number.toDouble()
        return Matrix(listRows.map { it.map { num -> num - dNumber } })
    }

    operator fun times(number: Number): Matrix {
        val dNumber = number.toDouble()
        return Matrix(listRows.map { it.map { num -> num * dNumber } })
    }

    operator fun div(number: Number): Matrix {
        val dNumber = number.toDouble()
        return Matrix(listRows.map { it.map { num -> num / dNumber } })
    }

    operator fun unaryMinus(): Matrix = Matrix(listRows.map { row -> row.map { -it } })

    operator fun unaryPlus(): Matrix = copy()

    operator fun get(row: Int): Vector = rows[row]

    operator fun get(rowRange: IntRange): Matrix = Matrix(listRows.slice(rowRange))

    operator fun get(row: Int, col: Int): Double = listRows[row][col]

    operator fun get(row: Int, colRange: IntRange): Vector = rows[row][colRange]

    operator fun get(rowRange: IntRange, col: Int): Vector = cols[col][rowRange]

    operator fun get(rowRange: IntRange, colRange: IntRange): Matrix =
        Matrix(listRows.slice(rowRange).map { it.slice(colRange) })
}

operator fun Number.plus(matrix: Matrix): Matrix = matrix + this

operator fun Number.minus(matrix: Matrix): Matrix = matrix - this

operator fun Number.times(matrix: Matrix): Matrix = matrix * this
