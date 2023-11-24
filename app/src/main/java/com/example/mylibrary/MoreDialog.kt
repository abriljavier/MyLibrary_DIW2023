package com.example.mylibrary

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment

class MoreDialog: DialogFragment() {
override fun onCreateDialog(savedInstance: Bundle?):Dialog{
    val builder = AlertDialog.Builder(requireActivity())
    val inflater =requireActivity().layoutInflater
    val dialogView =inflater.inflate(R.layout.more_layout, null)
    builder.setView(dialogView)

    val closeButton = dialogView.findViewById<Button>(R.id.clsButton)
    closeButton.setOnClickListener{
        println("Gola Chicos")
        dismiss()
    }

    return builder.create()
}
}