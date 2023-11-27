package com.example.mylibrary

import android.app.AlertDialog
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

class DeleteFragment : Fragment() {

    private lateinit var sqlHelper: SqlHelper
    private lateinit var db: SQLiteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.delete_row_layout, container, false)

        //SE RECOGE LA INSTANCIA DE LA BBDD
        sqlHelper = SqlHelper.getInstance(requireContext())
        db = sqlHelper.writableDatabase

        //EL BOTÓN DE BORRAR CON SU MÉTODO
        val deleteButton: Button = view.findViewById(R.id.sendRowButton)
        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        return view
    }

    //EL MÉTODO PARA MOSTRAR UN DIALOG CON EL BORRAR
    private fun showDeleteConfirmationDialog() {

        //RECOJO LAS VARIABLES DE LA VISTA
        val titleEditText: EditText? = view?.findViewById(R.id.editTitle)
        val title = titleEditText?.text.toString()

        //SI NO HA DEJADO EL CAMPO DE INSERTAR VACIO
        if (title.isNotEmpty()) {

            //CREA UN DIALOG CON ESTE TÍTULO Y MENSAJE
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("Delete Confirmation")
            alertDialogBuilder.setMessage("Are you sure you want to delete the row with title '$title'?")

            //SI DICE SÍ LLAMA A LA FUNCIÓN PARA BORRAR LIBRO POR TÍTULO DEL CRUD
            alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
                deleteRow(title)
                dialog.dismiss()
            }

            //SI DICE QUE NO CIERRA EL DIALOG
            alertDialogBuilder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            //MUESTRA EL DIALOGO
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

        } else {
            //SI NO SETEA EL NOMBRE A BORRAR
            Toast.makeText(context, "Please enter a title to delete", Toast.LENGTH_SHORT).show()
        }
    }

    //LA FUNCIÓN DE BORRAR SI LA VALIDACIÓN ES OKEY
    private fun deleteRow(title: String) {

        //LLAMA AL MÉTODO DEL CRUD
        val deletedRows = sqlHelper.deleteBookByTitle(title)

        //SI LA QUERY ES CORRECTA, INFORMA Y VA AL MAIN
        if (deletedRows > 0) {

            Toast.makeText(context, "Row deleted successfully", Toast.LENGTH_SHORT).show()

            val navController = view?.let { Navigation.findNavController(it) }
            navController?.navigate(R.id.action_showAllRows)

        } else {
            //SI NO ES PORQUE NO HAY LIBRO ENCONTRADO
            Toast.makeText(context, "No matching row found to delete", Toast.LENGTH_SHORT).show()
        }
    }

}

