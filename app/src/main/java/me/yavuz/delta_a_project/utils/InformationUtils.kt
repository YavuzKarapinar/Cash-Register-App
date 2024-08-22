package me.yavuz.delta_a_project.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import me.yavuz.delta_a_project.databinding.InformationDialogBinding

object InformationUtils {

    /**
     * Showing information within a alert dialog message with its custom layout
     *
     * @param context context for custom layout and alert dialog builder
     * @param message message to be showed on the alert dialog
     *
     */
    fun showInfo(context: Context, message: String) {
        val customLayout = LayoutInflater.from(context)
        val informationBinding = InformationDialogBinding.inflate(customLayout)
        val dialogBuilder = AlertDialog.Builder(context)

        informationBinding.infoTextView.text = message

        dialogBuilder.setView(informationBinding.root)
            .setPositiveButton("OKAY") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}