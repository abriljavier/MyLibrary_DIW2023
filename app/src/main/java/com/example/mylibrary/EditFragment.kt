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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditFragment : Fragment() {

    private var selectedTitle: String? = null
    private lateinit var sqlHelper: SqlHelper
    private lateinit var db: SQLiteDatabase
    private var idToEdit = 0
    private lateinit var editTitle: EditText
    private lateinit var editAuthor: EditText
    private lateinit var dateOutput: TextView
    private var selectedDate: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.edit_row_layout, container, false)

        //DECLARACIÓN DE LAS VISTAS DEL LAYOUT
        editTitle = view.findViewById(R.id.editTitle)
        editAuthor = view.findViewById(R.id.editAuthor)
        dateOutput = view.findViewById(R.id.outputDate)

        //RECOGE LA BASE DE DATOS DEL CRUD
        sqlHelper = SqlHelper.getInstance(requireActivity().applicationContext)
        db = sqlHelper.writableDatabase

        //MUESTRA EL DIALOGO PARA INTRODUCIR EL NOMBRE A EDITAR
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
            if (editTitle.text!=null && editAuthor.text!=null){
                editRow()
            }else {
                Toast.makeText(context, "Please do not leave empty fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    //EL DIALOGO PARA INTRODUCIR EL NOMBRE
    private fun showInputDialog(view: View) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val input = EditText(requireContext())

        //CREA EL DIALOGO Y LE ASIGNA EL TITULO, QUE NO SEA CANCELABLE Y LOS BOTONES
        builder.setTitle("Enter Book Title")
            .setView(input)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
                val title = input.text.toString()

                //EL USUARIO INTRODUCE UN TÍTULO EN EL DIALOG Y SI NO ESTÁ VACIO
                if (title.isNotEmpty()) {
                    selectedTitle = title

                    //SE TRAE EL BOOK DE LA BASE DE DATOS CON UN MÉTODO DEL CRUD
                    val book = sqlHelper.getOneBookByTitle(selectedTitle!!)

                    //SI EL BOOK EXISTE
                    if (book != null) {

                        //SETEAS EL ID, EL TITULO Y EL AUTOR
                        idToEdit = book.id!!
                        view.findViewById<EditText>(R.id.editTitle)?.setText(book.title)
                        view.findViewById<EditText>(R.id.editAuthor)?.setText(book.author)

                        //SETEAS TAMBIÉN LA FECHA EN EL TEXTVIEW DE FECHA FORMATEADA
                        val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                        view.findViewById<TextView>(R.id.outputDate)?.text = "Date selected: ${dateFormat.format(book.dateRead)}"

                        //DE MOMENTO SETEO LA FECHA A LA MISMA QUE VIENE DE LA BBDD
                        selectedDate = book.dateRead!!

                    } else {
                        //SI EL LIBRO NO EXISTE ES QUE NO ESTÁ BIEN INTRODUCIDO EL TÍTULO
                        showInputDialog(view)
                        Toast.makeText(context, "There is no book with this title!", Toast.LENGTH_SHORT).show()

                    }
                } else {
                    //SI NO INTRODUCES TÍTULO DEBES VOLVER A ESTE DIALOG
                    showInputDialog(view)
                    Toast.makeText(context, "Please insert a title", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                //SI CANCELAS EL DIALOGO VUELVES AL FRAGMENT PRINCIPAL
                dialog.cancel()

                val navController = view?.let { Navigation.findNavController(it) }
                navController?.navigate(R.id.action_showAllRows)
            }
            .show()
    }

    //MOSTRAR EL DATEPICKER DE NUEVO CON LAS FUNCIONALIDADES DEL ADDFRAGMENT
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentDate = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar.set(year, monthOfYear, dayOfMonth)

                //SI EL USUARIO HA ABIERTO EL DATEPICKER ME SETEAS LA FECHA A LA NUEVA QUE COJA
                //SI NO TE QUEDAS CON LA ANTIGUA
                selectedDate = calendar.timeInMillis

                //MOSTRAR LA NUEVA FECHA EN EL TEXTVIEW
                val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
                view?.findViewById<TextView>(R.id.outputDate)?.text = "Date selected: ${dateFormat.format(calendar.time)}"
            },
            currentYear,
            currentMonth,
            currentDay
        )
        datePickerDialog.datePicker.maxDate = currentDate.timeInMillis
        datePickerDialog.show()
    }

    // EL MÉTODO PARA EDITAR EL LIBRO
    private fun editRow() {
        //SI EL TEXTO INTRODUCIDO NO ES VACIO
        if (editTitle.text.toString() != "" && editAuthor.text.toString() != "") {

            //HACES LA QUERY CON EL MÉTODO DE LA BBDD
            val result = sqlHelper.updateRowById(idToEdit, editTitle.text.toString(), editAuthor.text.toString(), selectedDate)

            //SI HA IDO BIEN, TOAST Y PARA EL SHOWFRAGMENT
            if (result > 0) {

                Toast.makeText(context, "The row has been modified!", Toast.LENGTH_SHORT).show()

                val navController = view?.let { Navigation.findNavController(it) }
                navController?.navigate(R.id.action_showAllRows)

            } else {
                //HA HABIDO UN PROBLEMA CON LA QUERY
                Toast.makeText(context, "This row cannot be modified!", Toast.LENGTH_SHORT).show()
            }
        } else{
            //HAY ALGUN CAMPO VACÍO
            Toast.makeText(context, "Remember to fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
