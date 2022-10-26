package com.geeklabs.myscanner.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.core.content.ContextCompat
import com.geeklabs.myscanner.R
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {

    private var dateFormat1 = SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault())

    fun dateFormat1(timeInMillis: Long): String {
        return dateFormat1.format(Date(timeInMillis))
    }

    fun showAlertWithActions(
        context: Context,
        title: String,
        message: String,
        listener: AlertButtonClickListener,
        positiveButtonName: String = "OK",
        negativeButtonName: String = "Cancel",
    ): AlertDialog? {
        val dialogBuilder = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonName) { _, _ ->
                listener.onPositiveClicked()
            }.setNegativeButton(negativeButtonName) { _, _ ->
                listener.onNegativeClicked()
            }
        val dialog = dialogBuilder.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val buttonPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        val buttonNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        val textColor = ContextCompat.getColor(context, R.color.blue)
        buttonPositive.setTextColor(textColor)
        buttonNegative.setTextColor(textColor)
        return dialog
    }
}