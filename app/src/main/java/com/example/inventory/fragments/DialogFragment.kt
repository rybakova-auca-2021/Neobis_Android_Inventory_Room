package com.example.inventory.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class StartDialogFragment(private val productName: String) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder
            .setMessage("Архивировать $productName из каталога?")
            .setPositiveButton("Да") { dialog, id ->
                // Handle "Yes" button click
            }
            .setNegativeButton("Нет") { dialog, id ->
                // Handle "No" button click
            }
        return builder.create()
    }
}