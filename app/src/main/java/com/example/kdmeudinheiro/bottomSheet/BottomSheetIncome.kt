package com.example.kdmeudinheiro.bottomSheet

import android.view.View
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.IncomeLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheetIncome(val parentView: View) {
    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetBinding: IncomeLayoutBinding

    fun loadIncome(callback: (Double?) -> Unit) {
        bottomSheetView = View.inflate(parentView.context, R.layout.income_layout, null)
        bottomSheetDialog = BottomSheetDialog(parentView.context)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
        bottomSheetBinding = IncomeLayoutBinding.bind(bottomSheetView)

        bottomSheetBinding.buttonAddIncome.setOnClickListener {
            if (!bottomSheetBinding.editTextIncome.text.toString().isNullOrBlank()) {
                val income = bottomSheetBinding.editTextIncome.text.toString().toDouble()
                callback(income)
                bottomSheetDialog.dismiss()
            } else {
                bottomSheetDialog.dismiss()
            }
        }
    }

}