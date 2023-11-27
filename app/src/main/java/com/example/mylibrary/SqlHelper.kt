package com.example.mylibrary

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SqlHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    var db: SQLiteDatabase? = null

    //CREAS UN CONJUNTO DE VARIABLES ESTÁTICAS Y LAS ASIGNAS A INSTANCE
    companion object {
        private const val DATABASE_NAME = "library.db"
        private const val DATABASE_VERSION = 1

        private var instance: SqlHelper? = null

        //CUANDO UN FRAGMENT SOLICITE LA BASE DE DATOS SI NO EXISTE LA CREARÁ
        // Y SI NO LE DEVUELVE LA BBDD QUE EXISTE
        @Synchronized
        fun getInstance(context: Context): SqlHelper {
            if (instance == null) {
                instance = SqlHelper(context.applicationContext)
                Log.d("SqlHelper", "New instance created: $instance")
            }
            return instance!!
        }
    }

    //CREAR LA BBDD AL INICIAR LA APP Y LA ASIGNA A LA INSTANCIA GENERAL DE SQLITEDATABASE
    override fun onCreate(db: SQLiteDatabase) {
        this.db = db
        db.execSQL("CREATE TABLE library (id INTEGER PRIMARY KEY, title TEXT UNIQUE, author TEXT, date INTEGER)")
    }

    //HACE FALTA IMPLEMENTAR PERO DE MOMENTO NO HACE NADA (NO HAY ACTUALIZACIÓN DE VERSIONES)
    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // No es necesario implementar esto por ahora
    }

    //INSERTAR UNA ROW
    fun addBook(title: String, author: String, date: Long): Long {
        val values = ContentValues().apply {
            put("title", title)
            put("author", author)
            put("date", date)
        }

        return db?.insert("library", null, values) ?: -1
    }

    //DEVOLVER TODOS LOS LIBROS COMO UNA COLECCIÓN
    fun getAllBooks(): List<Book> {
        val books = mutableListOf<Book>()
        this.db?.let { safeDb ->
            val cursor = safeDb.query(
                "library",
                arrayOf("id", "title", "author", "date"),
                null,
                null,
                null,
                null,
                "id ASC"
            )
            with(cursor) {
                while (moveToNext()) {
                    val id = getInt(0)
                    val itemName = getString(1)
                    val itemAuthor = getString(2)
                    val itemDate = getLong(3)
                    var oneBook = Book(id, itemName, itemAuthor, itemDate)
                    books.add(oneBook)
                }
            }
            cursor.close()
        }
        return books
    }

    //DEVOLVER UN LIBRO DE TIPO BOOK DADO UN TITULO
    fun getOneBookByTitle(title: String): Book? {
        var oneBook: Book? = null
        if (db != null) {
            val cursor = db!!.query(
                "library",
                arrayOf("id", "title", "author", "date"),
                "title = ?",
                arrayOf(title),
                null,
                null,
                null
            )
            with(cursor) {
                if (moveToFirst()) {
                    val itemId = getInt(0)
                    val itemName = getString(1)
                    val itemAuthor = getString(2)
                    val itemDate = getLong(3)
                    oneBook = Book(itemId, itemName, itemAuthor, itemDate)
                }
            }
            cursor.close()
        }
        return oneBook
    }

    //MODIFICAR UN LIBRO POR ID
    fun updateRowById(id: Int, title: String, author: String, date: Long): Int {
        val values = ContentValues().apply {
            put("title", title)
            put("author", author)
            put("date", date)
        }

        val whereClause = "id = ?"
        val whereArgs = arrayOf(id.toString())

        return db?.update("library", values, whereClause, whereArgs) ?: 0
    }

    //BORRAR UN LIBRO POR TÍTULO
    fun deleteBookByTitle(title: String): Int {
        val whereClause = "title = ?"
        val whereArgs = arrayOf(title)

        return db?.delete("library", whereClause, whereArgs) ?: 0
    }
}

