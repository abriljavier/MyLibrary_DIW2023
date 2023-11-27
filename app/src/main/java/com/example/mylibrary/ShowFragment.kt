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
        val view = inflater.inflate(R.layout.show_layout, container, false)

        //SE OBTIENE LA INSTANCIA DE LA BBDD
        sqlHelper = SqlHelper.getInstance(requireActivity().applicationContext)
        Log.d("SqlHelperInstance", "SqlHelper instance: $sqlHelper")
        sqlHelper.db = sqlHelper.db ?: sqlHelper.writableDatabase

        //SE TRAEN TODOS LOS LIBROS DE LA BBDD
        val books = sqlHelper.getAllBooks()

        //SI LA BASE DE DATOS TIENE ROWS MUESTRALAS, SI NO, MUESTRA UN TEXTO DE AYUDA
        if (books.isNotEmpty()){
            //CONFIGURA EL RECYCLEVIEW Y LE ASIGNA LOS DATOS
            val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = BookAdapter(books)
            view.findViewById<TextView>(R.id.infoTextView).visibility = View.GONE
        } else {
            view.findViewById<TextView>(R.id.infoTextView).visibility = View.VISIBLE
        }
        return view
    }
}
