package com.example.kdmeudinheiro.bottomSheet


import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider

import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.InputBillLayoutBinding
import com.example.kdmeudinheiro.enums.TypesOfBills
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.viewModel.BillsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheet(
    val parentView: View/* TODO mudar quando tiver injecao de dependencias */,
    val bill: BillsModel?,
    callback : (BillsModel) -> Unit,
) {
    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetBinding: InputBillLayoutBinding


    fun loadBottomBill() {

        bottomSheetView = View.inflate(parentView.context, R.layout.input_bill_layout, null)
        bottomSheetDialog = BottomSheetDialog(parentView.context)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
        bottomSheetBinding = InputBillLayoutBinding.bind(bottomSheetView)


        if (bill == null) {
            loadBottomSheetAddBillsComponents(bottomSheetView)
            bottomSheetBinding.editBillButton.visibility = View.GONE
            bottomSheetBinding.deleteBillButton.visibility = View.GONE
            bottomSheetBinding.payBillButton.visibility = View.GONE

            val listType = listOf<String>(
                TypesOfBills.EMERGENCY_BILL.catName,
                TypesOfBills.LEISURE_BILLS.catName,
                TypesOfBills.FIX_BILLS.catName,
                TypesOfBills.MONTHLY_BILLS.catName,
            )

            bottomSheetBinding.spinnerBillType.adapter = ArrayAdapter(
                parentView.context, android.R.layout.simple_spinner_item,
                listType
            )
            bottomSheetBinding.saveBillButtom.setOnClickListener {

                val selectedType = bottomSheetBinding.spinnerBillType.selectedItem.toString()


                val billName = bottomSheetBinding.editTextInputBillName.text.toString()
                val billPrice = bottomSheetBinding.editTextInputBillPrice.text.toString()
                val billExpireDate =
                    bottomSheetBinding.editTextInputBillExpireDate.text.toString()
                val billObject =
                    BillsModel(null, null, billPrice, selectedType, billName, billExpireDate)

            }

        } else {
            loadBottomSheetCrudBillsComponents(bottomSheetView, bill)
            bottomSheetBinding.saveBillButtom.visibility = View.GONE
            bottomSheetBinding.editTextInputBillExpireDate.visibility = View.GONE
            bottomSheetBinding.editBillButton.visibility = View.VISIBLE
            bottomSheetBinding.deleteBillButton.visibility = View.VISIBLE
            bottomSheetBinding.payBillButton.visibility = View.VISIBLE

        }

        val listType = listOf<String>(
            TypesOfBills.EMERGENCY_BILL.catName,
            TypesOfBills.LEISURE_BILLS.catName,
            TypesOfBills.FIX_BILLS.catName,
            TypesOfBills.MONTHLY_BILLS.catName,
        )

    }

    private fun loadBottomSheetCrudBillsComponents(bottomSheetView: View?, bill: BillsModel) {

    }

    private fun loadBottomSheetAddBillsComponents(bottomSheetView: View?) {

    }
}

