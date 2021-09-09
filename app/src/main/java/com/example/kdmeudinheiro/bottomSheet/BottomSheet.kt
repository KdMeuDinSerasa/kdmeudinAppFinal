package com.example.kdmeudinheiro.bottomSheet

import android.app.DatePickerDialog
import android.view.View
import android.widget.ArrayAdapter
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.InputBillLayoutBinding
import com.example.kdmeudinheiro.databinding.TipBillLayoutBinding
import com.example.kdmeudinheiro.enums.TipType
import com.example.kdmeudinheiro.enums.TypesOfBills
import com.example.kdmeudinheiro.model.BillsModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

class BottomSheet(
    val parentView: View,/* TODO mudar quando tiver injecao de dependencias */
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

        bottomSheetBinding.editTextInputBillExpireDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH + 1)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                parentView.context,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    var mes = month + 1
                    bottomSheetBinding.editTextInputBillExpireDate.setText(" $dayOfMonth/$mes/$year")
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        if (bill == null) {
            bottomSheetBinding.editBillButton.visibility = View.GONE
            bottomSheetBinding.deleteBillButton.visibility = View.GONE
            bottomSheetBinding.payBillButton.visibility = View.GONE

            bottomSheetBinding.saveBillButtom.setOnClickListener {
                if (!bottomSheetBinding.editTextInputBillName.text.isNullOrEmpty() &&
                    !bottomSheetBinding.editTextInputBillPrice.text.isNullOrEmpty() &&
                    !bottomSheetBinding.editTextInputBillExpireDate.text.isNullOrEmpty()
                ) {

                    val selectedType = bottomSheetBinding.spinnerBillType.selectedItem.toString()
                    val billName = bottomSheetBinding.editTextInputBillName.text.toString()
                    val billPrice = bottomSheetBinding.editTextInputBillPrice.text.toString()
                    val billExpireDate =
                        bottomSheetBinding.editTextInputBillExpireDate.text.toString()

                    val billObject =
                        BillsModel(null, null, billPrice, selectedType, billName, billExpireDate)
                    callback(billObject, 0)
                    bottomSheetDialog.dismiss()

                    true
                } else
                    false
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
                if (!bottomSheetBinding.editTextInputBillName.text.isNullOrEmpty() &&
                    !bottomSheetBinding.editTextInputBillPrice.text.isNullOrEmpty() &&
                    !bottomSheetBinding.editTextInputBillExpireDate.text.isNullOrEmpty()
                ) {
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
                } else{
                    callback(bill, 3)
                    bottomSheetDialog.dismiss()
                }
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

class BottomSheetTips(val parentView: View, val typeTip: TipType,) {

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
        } else if (typeTip == TipType.TIP_INCOME) { /* TODO */
        } else if (typeTip == TipType.TIP_CHART) {/* TODO */
        }
    }
}
