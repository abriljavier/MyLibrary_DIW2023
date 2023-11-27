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

    private var selectedDate: Calendar? = null
    lateinit var sqlHelper: SqlHelper
    lateinit var db: SQLiteDatabase
    private lateinit var dateOutput: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_row_layout, container, false)
        sqlHelper = SqlHelper.getInstance(requireActivity().applicationContext)
        db = sqlHelper.writableDatabase

        val buttonDatePicker: Button = view.findViewById(R.id.addDateButton)
        buttonDatePicker.setOnClickListener {
            showDatePicker()
        }
        Log.d("SqlHelperInstance", "SqlHelper instance: $sqlHelper")
        return view
    }

    private fun showDatePicker() {
        if (selectedDate == null) {
            selectedDate = Calendar.getInstance()
        }

        val currentDate = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireActivity(),
            this,
            selectedDate!!.get(Calendar.YEAR),
            selectedDate!!.get(Calendar.MONTH),
            selectedDate!!.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.maxDate = currentDate.timeInMillis
        datePicker.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        selectedDate?.set(Calendar.YEAR, year)
        selectedDate?.set(Calendar.MONTH, month)
        selectedDate?.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        // Formatear la fecha y mostrarla en el TextView
        val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate?.time)

        dateOutput.text = "Date selected : ${formattedDate}"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<Button>(R.id.sendRowButton)
        val titleEdit = view.findViewById<EditText>(R.id.editTitle)
        val authorEdit = view.findViewById<EditText>(R.id.editAuthor)
        dateOutput = view.findViewById(R.id.outputDate)

        // Lógica para insertar datos en la base de datos cuando se presiona el botón
        button.setOnClickListener {
            val title = titleEdit.text.toString()
            val author = authorEdit.text.toString()

            if (title.isNotEmpty() && author.isNotEmpty()) {
                val dateInMillis = selectedDate?.timeInMillis
                if (dateInMillis !=null) {

                    val dateOutput = view.findViewById<TextView>(R.id.outputDate)
                    dateOutput.text = dateOutput.toString()

                    val pairOfValues = ContentValues().apply {
                        put("title", title)
                        put("author", author)
                        put("date", dateInMillis)
                    }

                    // Llamar a un método en DatabaseHelper para realizar la inserción
                    val newRow = db.insert("library", null, pairOfValues)
                    println("Books: ${sqlHelper.getAllBooks()}")
                    if (newRow != -1L) {
                        Toast.makeText(context, "Row inserted successfully", Toast.LENGTH_SHORT)
                            .show()
                        // Obtén el NavController usando la vista del fragmento
                        val navController = view?.let { Navigation.findNavController(it) }
                        // Navega a la acción que muestra todas las filas
                        navController?.navigate(R.id.action_showAllRows)
                    } else {
                        Toast.makeText(
                            context,
                            "Error inserting row, please try again.Please check that you do not repeat the book or leave an empty field.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(context, "Please select a valid date", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }



}

