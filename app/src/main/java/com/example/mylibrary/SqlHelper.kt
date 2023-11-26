package com.example.mylibrary

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqlHelper(context: Context): SQLiteOpenHelper(context, "library.db", null, 1) {
    override fun  onCreate(db: SQLiteDatabase?){
        db?.execSQL("CREATE TABLE library (id INTEGER PRIMARY KEY, title TEXT, author TEXT, date INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int){
        TODO("Its not necessary")
    }

}