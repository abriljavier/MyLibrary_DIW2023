package com.example.mylibrary

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import java.util.Calendar

class EditFragment : Fragment() {

    private var selectedTitle: String? = null
    private lateinit var sqlHelper: SqlHelper
    private lateinit var db: SQLiteDatabase
    private var idToEdit = 0
    lateinit var editTitle: EditText
    lateinit var editAuthor: EditText
    private var selectedDate: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño para este fragmento (se hará después de obtener el título)
        val view = inflater.inflate(R.layout.edit_row_layout, container, false)

        editTitle = view.findViewById(R.id.editTitle)
        editAuthor = view.findViewById(R.id.editAuthor)

        // Inicializar sqlHelper después de inflar el diseño
        sqlHelper = SqlHelper.getInstance(requireActivity().applicationContext)
        db = sqlHelper.writableDatabase

        showInputDialog(view)

        //BOTÓN PARA AGREGAR UNA NUEVA FECHA
        val addDateButton = view.findViewById<Button>(R.id.addDateButton)
        addDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        //BOTÓN PARA MANDAR LA MODIFICACIÓN DE LA ROW
        val modifyButton = view.findViewById<Button>(R.id.sendRowButton)
        modifyButton.setOnClickListener {
            //NO DEJA MANDAR UNA ROW VACIA
            if (editTitle.text!=null && editAuthor!=null){
                editRow()
            }else {
                Toast.makeText(context, "Please do not leave empty fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun showInputDialog(view: View) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val input = EditText(requireContext())
        builder.setTitle("Enter Book Title")
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val title = input.text.toString()
                if (title.isNotEmpty()) {
                    selectedTitle = title
                    // Llamamos a getOneBookByTitle cuando tenemos el título
                    val book = getOneBookByTitle(selectedTitle)
                    // Ahora puedes hacer lo que quieras con el objeto book, por ejemplo, mostrarlo en la UI
                    if (book != null) {
                        // Hacer algo con el libro
                        // Por ejemplo, mostrarlo en los campos de edición
                        idToEdit = book.id!!
                        view.findViewById<EditText>(R.id.editTitle)?.setText(book.title)
                        view.findViewById<EditText>(R.id.editAuthor)?.setText(book.author)
                        selectedDate = book.dateRead!! // Guardar la fecha actual
                        // Mostrar la fecha en un TextView o hacer lo que necesites
                    } else {
                        // Manejar el caso en que no se encuentra el libro
                        // Puedes mostrar un mensaje, etc.
                    }
                } else {
                    // El título está vacío, manejar según tus necesidades
                    // Puedes mostrar un mensaje, etc.
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
                // Puedes cerrar el fragmento o manejar según tus necesidades
            }
            .show()
    }

    private fun getOneBookByTitle(title: String?): Book? {
        return title?.let { sqlHelper.getOneBookByTitle(it) }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentDate = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Actualizar la fecha seleccionada
                calendar.set(year, monthOfYear, dayOfMonth)
                selectedDate = calendar.timeInMillis
                // Puedes mostrar la nueva fecha en un TextView si lo deseas
            },
            currentYear,
            currentMonth,
            currentDay
        )
        datePickerDialog.datePicker.maxDate = currentDate.timeInMillis
        datePickerDialog.show()
    }

    private fun editRow() {
        if (editTitle.text != null && editAuthor.text != null) {
            // Utilizar selectedDate para la actualización de la fecha en tu base de datos
            val result = sqlHelper.updateRowById(idToEdit, editTitle.text.toString(), editAuthor.text.toString(), selectedDate)
            if (result > 0) {
                Toast.makeText(context, "The row has been modified!", Toast.LENGTH_SHORT).show()
                val navController = view?.let { Navigation.findNavController(it) }
                // Navega a la acción que muestra todas las filas
                navController?.navigate(R.id.action_showAllRows)
                // La actualización fue exitosa
                // Puedes mostrar un mensaje al usuario o realizar otras acciones
            } else {
                Toast.makeText(context, "This row cannot be modified!", Toast.LENGTH_SHORT).show()
                // La actualización no tuvo éxito
                // Puedes mostrar un mensaje de error o realizar otras acciones
            }
        }
    }
}
