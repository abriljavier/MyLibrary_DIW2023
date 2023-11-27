package com.example.mylibrary

import android.app.DatePickerDialog
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var selectedDate: Calendar = Calendar.getInstance()
    private lateinit var sqlHelper: SqlHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var dateOutput: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_row_layout, container, false)

        //LLAMO A LA INSTANCIA DE LA BBDD
        sqlHelper = SqlHelper.getInstance(requireActivity().applicationContext)
        db = sqlHelper.writableDatabase

        //LOS ELEMENTOS DE LA VISTA DEL ADDFRAGMENT
        val button = view.findViewById<Button>(R.id.sendRowButton)
        val titleEdit = view.findViewById<EditText>(R.id.editTitle)
        val authorEdit = view.findViewById<EditText>(R.id.editAuthor)
        dateOutput = view.findViewById(R.id.outputDate)

        //LA FUNCIÓN DEL BOTÓN DE SELECCIONAR FECHA
        val buttonDatePicker: Button = view.findViewById(R.id.addDateButton)
        buttonDatePicker.setOnClickListener { showDatePicker() }

        //CUANDO SE LE DA AL BOTÓN DE GUARDAR
        button.setOnClickListener {
            val title = titleEdit.text.toString()
            val author = authorEdit.text.toString()

            //SI LOS CAMPOS TITLE Y AUTOR NO ESTÁN VACIOS
            if (title.isNotEmpty() && author.isNotEmpty()) {
                val dateInMillis = selectedDate.timeInMillis

                //SI EL TEXTO DE LA FECHA ESTÁ RELLENO Y POR LO TANTO ESTA ESTÁ SETEADA
                if (dateOutput.text != "") {

                    //HACES LA QUERY
                    val newRowId = sqlHelper.addBook(title, author, dateInMillis)

                    //SI NO DA PROBLEMAS, QUERY HECHA Y REDIRECCIÓN
                    if (newRowId != -1L) {
                        Toast.makeText(context, "Row inserted successfully", Toast.LENGTH_SHORT)
                            .show()

                        val navController = view?.let { Navigation.findNavController(it) }
                        navController?.navigate(R.id.action_showAllRows)

                    } else {
                        //EN CASO CONTRARIO AVISO
                        Toast.makeText(
                            context,
                            "Error inserting row, there is a book with this name",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    //SI NO SE SETEA FECHA
                    Toast.makeText(context, "Please select a valid date", Toast.LENGTH_SHORT).show()
                }
            } else {
                //SI NO SE SETEA NOMBRE O TÍTULO
                Toast.makeText(context, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    //MOSTRAR EL DATEPICKER
    private fun showDatePicker() {
        val currentDate = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireActivity(),
            this,
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.maxDate = currentDate.timeInMillis
        datePicker.show()
    }

    //SE SETEA A FECHA EN EL DATEPICKER
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        selectedDate.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }

        val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate.time)

        dateOutput.text = "Date selected: $formattedDate"
    }
}


