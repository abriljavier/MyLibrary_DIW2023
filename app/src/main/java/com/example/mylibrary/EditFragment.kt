package com.example.mylibrary

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment

class EditFragment : Fragment() {

    private var selectedTitle: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showInputDialog()
        // Inflar el diseño para este fragmento (se hará después de obtener el título)
        return inflater.inflate(R.layout.edit_row_layout, container, false)
    }

    private fun showInputDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val input = EditText(requireContext())
        builder.setTitle("Enter Book Title")
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val title = input.text.toString()
                if (title.isNotEmpty()) {
                    selectedTitle = title
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
}
