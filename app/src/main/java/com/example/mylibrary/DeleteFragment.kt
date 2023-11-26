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

    lateinit var sqlHelper: SqlHelper
    lateinit var db: SQLiteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.delete_row_layout, container, false)

        sqlHelper = SqlHelper.getInstance(requireContext())
        db = sqlHelper.writableDatabase

        val deleteButton: Button = view.findViewById(R.id.sendRowButton)
        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        return view
    }

    private fun showDeleteConfirmationDialog() {
        val titleEditText: EditText = view?.findViewById(R.id.editTitle) ?: return
        val title = titleEditText.text.toString()

        if (title.isNotEmpty()) {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("Delete Confirmation")
            alertDialogBuilder.setMessage("Are you sure you want to delete the row with title '$title'?")

            alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
                deleteRow(title)
                dialog.dismiss()
            }

            alertDialogBuilder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        } else {
            Toast.makeText(context, "Please enter a title to delete", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteRow(title: String) {
        // No necesitas crear una nueva instancia de SqlHelper y SQLiteDatabase aquí
        // Ya tienes la instancia compartida desde el Singleton

        // Aquí asumimos que tienes una columna "title" en tu base de datos
        val whereClause = "title = ?"
        val whereArgs = arrayOf(title)

        // Llamar al método delete en SQLiteDatabase para borrar la fila
        val deletedRows = db.delete("library", whereClause, whereArgs)

        if (deletedRows > 0) {
            Toast.makeText(context, "Row deleted successfully", Toast.LENGTH_SHORT).show()
            //NAVEGAR A LA VIEW PRINCIPAL
            val navController = view?.let { Navigation.findNavController(it) }
            // Navega a la acción que muestra todas las filas
            navController?.navigate(R.id.action_showAllRows)
        } else {
            Toast.makeText(context, "No matching row found to delete", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Cierra la base de datos solo si está abierta
        sqlHelper.db?.let {
            if (it.isOpen) {
                it.close()
            }
        }
    }
}

