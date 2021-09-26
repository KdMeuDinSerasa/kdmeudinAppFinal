package com.example.kdmeudinheiro.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogToFilter {

    private var selectedItem: Map<Int, String>? = null

    fun dialogToFilter(context: Context, callback: (Map<Int, String>?) -> Unit) {
        val builder = MaterialAlertDialogBuilder(context)

        builder.setTitle("Selecione o filtro desejado")

        val options = hashMapOf(
            0 to "A Vencer",
            1 to "Vencidas",
            2 to "Pagas",
            3 to "Todas"
        )

        builder.setSingleChoiceItems(options.values.toTypedArray(), -1) { dialog, index ->
            selectedItem = options.filterKeys { it == index }
        }

        builder.setPositiveButton("Filtrar") { dialog, which ->
            if (selectedItem != null) {
                callback(selectedItem)
            }
        }

        builder.setNeutralButton("Cancelar", null)

        builder.setCancelable(false)

        val dialog = builder.create()

        dialog.show()
    }
}
