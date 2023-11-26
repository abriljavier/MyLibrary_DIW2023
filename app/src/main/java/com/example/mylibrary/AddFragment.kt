package com.example.mylibrary

import android.app.DatePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.Calendar


class AddFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var selectedDate: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_row_layout, container, false)

        val buttonDatePicker: Button = view.findViewById(R.id.addDateButton)
        buttonDatePicker.setOnClickListener {
            showDatePicker()
        }

        return view
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            requireActivity(),
            this,
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        selectedDate.set(Calendar.YEAR, year)
        selectedDate.set(Calendar.MONTH, month)
        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sqlHelper = SqlHelper(requireContext())
        val db = sqlHelper.writableDatabase

        val button = view.findViewById<Button>(R.id.sendRowButton)
        val titleEdit = view.findViewById<EditText>(R.id.editTitle)
        val authorEdit = view.findViewById<EditText>(R.id.editAuthor)

        // Lógica para insertar datos en la base de datos cuando se presiona el botón
        button.setOnClickListener {
            val title = titleEdit.text.toString()
            val author = authorEdit.text.toString()

            if (title.isNotEmpty() && author.isNotEmpty()) {
                val dateInMillis = selectedDate.timeInMillis

                val pairOfValues = ContentValues().apply {
                    put("title", title)
                    put("author", author)
                    put("date", dateInMillis)
                }

                // Llamar a un método en DatabaseHelper para realizar la inserción
                val newRow = db.insert("library", null, pairOfValues)

                if (newRow != -1L) {
                    Toast.makeText(context, "Row inserted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error inserting row, please try again", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
