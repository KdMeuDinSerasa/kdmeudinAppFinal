package com.example.kdmeudinheiro.bottomSheet

import android.view.View
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.TipBillLayoutBinding
import com.example.kdmeudinheiro.enums.TipType
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheetTips(val parentView: View, val typeTip: TipType) {

    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog /* Dismiss method needs to be implemented aways here*/

    fun loadTip() {

        if (typeTip == TipType.TIP_BILL_CATEGORY) {
            bottomSheetView = View.inflate(parentView.context, R.layout.tip_bill_layout, null)
            bottomSheetDialog = BottomSheetDialog(parentView.context)
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
            val bottomSheetBinding = TipBillLayoutBinding.bind(bottomSheetView)
            bottomSheetBinding.backButton.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
    }
}