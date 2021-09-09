package com.example.kdmeudinheiro.bottomSheet


import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider

import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.InputBillLayoutBinding
import com.example.kdmeudinheiro.databinding.TipBillLayoutBinding
import com.example.kdmeudinheiro.enums.TipType
import com.example.kdmeudinheiro.enums.TypesOfBills
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.viewModel.BillsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheet(
    val parentView: View,
    val bill: BillsModel?,

    ) {
    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog /* Dismiss method needs to be implemented aways here*/
    private lateinit var bottomSheetBinding: InputBillLayoutBinding


    fun loadBottomBill(callback: (BillsModel, Int?) -> Unit) {

        bottomSheetView = View.inflate(parentView.context, R.layout.input_bill_layout, null)
        bottomSheetDialog = BottomSheetDialog(parentView.context)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
        bottomSheetBinding = InputBillLayoutBinding.bind(bottomSheetView)
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


        if (bill == null) {
            bottomSheetBinding.editBillButton.visibility = View.GONE
            bottomSheetBinding.deleteBillButton.visibility = View.GONE
            bottomSheetBinding.payBillButton.visibility = View.GONE
            bottomSheetBinding.saveBillButtom.setOnClickListener {

                val selectedType = bottomSheetBinding.spinnerBillType.selectedItem.toString()


                val billName = bottomSheetBinding.editTextInputBillName.text.toString()
                val billPrice = bottomSheetBinding.editTextInputBillPrice.text.toString()
                val billExpireDate =
                    bottomSheetBinding.editTextInputBillExpireDate.text.toString()
                val billObject =
                    BillsModel(null, null, billPrice, selectedType, billName, billExpireDate)
                callback(billObject, 0)
                bottomSheetDialog.dismiss()

            }


        } else {
            bottomSheetBinding.editTextInputBillName.setText(bill.name_bill)
            bottomSheetBinding.editTextInputBillPrice.setText(bill.price)
            bottomSheetBinding.editTextInputBillExpireDate.setText(bill.expire_date)
            bottomSheetBinding.saveBillButtom.visibility = View.GONE
            bottomSheetBinding.editTextInputBillExpireDate.visibility = View.VISIBLE
            bottomSheetBinding.editBillButton.visibility = View.VISIBLE
            bottomSheetBinding.deleteBillButton.visibility = View.VISIBLE
            bottomSheetBinding.payBillButton.visibility = View.VISIBLE
            bottomSheetBinding.editBillButton.setOnClickListener {
                val selectedType = bottomSheetBinding.spinnerBillType.selectedItem.toString()
                val billName = bottomSheetBinding.editTextInputBillName.text.toString()
                val billPrice = bottomSheetBinding.editTextInputBillPrice.text.toString()
                val billDate = bottomSheetBinding.editTextInputBillExpireDate.text.toString()
                val billObject =
                    BillsModel(
                        bill.id_bill,
                        bill.id_user,
                        billPrice,
                        selectedType,
                        billName,
                        billDate
                    )
                callback(billObject, 1)
                bottomSheetDialog.dismiss()
            }
            bottomSheetBinding.deleteBillButton.setOnClickListener {
                callback(bill, 2)
                bottomSheetDialog.dismiss()
            }
            bottomSheetBinding.payBillButton.setOnClickListener {
                //TODO
                bottomSheetDialog.dismiss()
            }


        }


    }
}

class BottomSheetTips(val parentView: View, val typeTip: TipType, @LayoutRes val layout: Int) {

    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog /* Dismiss method needs to be implemented aways here*/


    fun loadTip() {

        bottomSheetView = View.inflate(parentView.context, layout, null)
        val bottomSheetBinding = TipBillLayoutBinding.bind(bottomSheetView)
        bottomSheetDialog = BottomSheetDialog(parentView.context)
        bottomSheetDialog.setContentView(bottomSheetView)

        if (typeTip == TipType.TIP_BILL_CATEGORY) {
//               bottomSheetView = View.inflate(parentView.context, R.layout.tip_bill_layout, null)
//               bottomSheetDialog = BottomSheetDialog(parentView.context)
//               bottomSheetDialog.setContentView(bottomSheetView)
//               bottomSheetDialog.show()
//               val bottomSheetBinding = TipBillLayoutBinding.bind(bottomSheetView)
//               bottomSheetBinding.backButton.setOnClickListener {
//                   bottomSheetDialog.dismiss()
//               }

        } else if (typeTip == TipType.TIP_INCOME) { /* TODO */
        } else if (typeTip == TipType.TIP_CHART) {/* TODO */
        }

        bottomSheetBinding.backButton.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()


    }
}
