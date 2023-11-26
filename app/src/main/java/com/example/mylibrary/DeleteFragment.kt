package com.example.mylibrary

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class DeleteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.delete_row_layout, container, false)

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
        val sqlHelper = SqlHelper(requireContext())
        val db = sqlHelper.writableDatabase

        // Aquí asumimos que tienes una columna "title" en tu base de datos
        val whereClause = "title = ?"
        val whereArgs = arrayOf(title)

        // Llamar al método delete en SQLiteDatabase para borrar la fila
        val deletedRows = db.delete("library", whereClause, whereArgs)

        if (deletedRows > 0) {
            Toast.makeText(context, "Row deleted successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No matching row found to delete", Toast.LENGTH_SHORT).show()
        }
    }
}