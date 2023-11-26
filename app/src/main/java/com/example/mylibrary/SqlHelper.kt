package com.example.mylibrary

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SqlHelper private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    var db: SQLiteDatabase? = null // Cambiado a Nullable
    companion object {
        private const val DATABASE_NAME = "library.db"
        private const val DATABASE_VERSION = 1

        private var instance: SqlHelper? = null

        @Synchronized
        fun getInstance(context: Context): SqlHelper {
            if (instance == null) {
                instance = SqlHelper(context.applicationContext)
                Log.d("SqlHelper", "New instance created: $instance")
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        this.db = db // Agrega esta línea
        db.execSQL("CREATE TABLE library (id INTEGER PRIMARY KEY, title TEXT, author TEXT, date INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // No es necesario implementar esto por ahora
    }

//    fun getAllBooks(): List<Book> {
//        val books = mutableListOf<Book>()
//        this.db?.let { safeDb ->
//            val cursor = safeDb.query("library", arrayOf("id", "title", "author", "date"), null, null, null, null, "id ASC")
//            with(cursor) {
//                while (moveToNext()) {
//                    val id = getInt(0)
//                    val itemName = getString(1)
//                    val itemAuthor = getString(2)
//                    val itemDate = getLong(3)
//                    var oneBook = Book(id, itemName, itemAuthor, itemDate)
//                    books.add(oneBook)
//                }
//            }
//            cursor.close()
//        }
//        return books
//    }
fun getAllBooks(): List<Book> {
    val books = mutableListOf<Book>()
    this.db?.let { safeDb ->
        val cursor = safeDb.query("library", arrayOf("id", "title", "author", "date"), null, null, null, null, "id ASC")
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
}

