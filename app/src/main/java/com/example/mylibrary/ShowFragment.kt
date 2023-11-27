package com.example.mylibrary

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylibrary.Book
import com.example.mylibrary.BookAdapter
import com.example.mylibrary.R
import com.example.mylibrary.SqlHelper

class ShowFragment : Fragment() {

    private lateinit var sqlHelper: SqlHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.see_layout, container, false)

        // Obtén la instancia del SqlHelper
        sqlHelper = SqlHelper.getInstance(requireActivity().applicationContext)
        Log.d("SqlHelperInstance", "SqlHelper instance: $sqlHelper")

        if (sqlHelper.db == null) {
            println("la base de datos no es nula")
            sqlHelper.db = sqlHelper.writableDatabase
        } else {
            println("La base de datos es nula")
        }

        // Obtén la lista de libros desde la base de datos
        val books = sqlHelper.getAllBooks()

        if (books.isNotEmpty()){
            // Configura el RecyclerView y asigna el adaptador
            val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = BookAdapter(books)
        } else {
            val infoTextView = TextView(view.context)
            infoTextView.text = "Please start by introducing an element in the bbdd"
            infoTextView.textSize =20F
            val father = view.findViewById<LinearLayout>(R.id.seeLayout)
            father.addView(infoTextView)
        }



        return view
    }
}
