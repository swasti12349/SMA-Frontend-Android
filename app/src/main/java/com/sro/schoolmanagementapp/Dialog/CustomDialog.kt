package com.sro.schoolmanagementapp.Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import com.sro.schoolmanagementapp.R


class CustomDialog {

    interface CustomDialogInterface {
        fun onPositiveButtonClick(dialog: Dialog?, text: String?, subName: String?)
        fun onCancelButtonClick(dialog: Dialog?)
    }

    companion object {
        fun show(
            context: Context?,
            listener: CustomDialogInterface,
            title: String,
            edvis: Boolean,

            ) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogView: View = inflater.inflate(R.layout.dialog_edit_text, null)
            builder.setView(dialogView)
            val editText = dialogView.findViewById<EditText>(R.id.fileNameTxt)
            val titletxt = dialogView.findViewById<TextView>(R.id.fileName)
            titletxt.text = title
            if (edvis) {
                dialogView.findViewById<EditText>(R.id.subNameTxt).isVisible = true
            }
            builder.setPositiveButton("OK") { dialog: DialogInterface?, which: Int ->
                val text = editText.text.toString()
                val subName = dialogView.findViewById<EditText>(R.id.subNameTxt).text.toString()
                listener.onPositiveButtonClick(dialog as Dialog?, text, subName)
            }

            builder.setNegativeButton("Cancel") { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                listener.onCancelButtonClick(dialog as Dialog)
            }

            builder.create().show()
        }
    }
}