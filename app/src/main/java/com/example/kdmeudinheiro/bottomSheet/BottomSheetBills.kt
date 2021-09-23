package com.example.kdmeudinheiro.bottomSheet

import android.app.DatePickerDialog
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.adapter.AdapterChartTips
import com.example.kdmeudinheiro.databinding.IncomeLayoutBinding
import com.example.kdmeudinheiro.databinding.InputBillLayoutBinding
import com.example.kdmeudinheiro.databinding.TipBillLayoutBinding
import com.example.kdmeudinheiro.databinding.TipChartBinding
import com.example.kdmeudinheiro.enums.StatusBills
import com.example.kdmeudinheiro.enums.TipType
import com.example.kdmeudinheiro.enums.TypesOfBills
import com.example.kdmeudinheiro.interfaces.ChartClickInterceptor
import com.example.kdmeudinheiro.model.Articles
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.utils.feedback
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*


class BottomSheetBills(
  private  val parentView: View,
   private val bill: BillsModel?,
) {
    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog /* Dismiss method needs to be implemented aways here*/
    private lateinit var bottomSheetBinding: InputBillLayoutBinding
    private lateinit var date: DatePicker

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

        bottomSheetBinding.spinnerExposed.setText(listType[0])

        bottomSheetBinding.spinnerExposed.setAdapter(
            ArrayAdapter(
                parentView.context, android.R.layout.simple_spinner_item,
                listType
            )
        )

        bottomSheetBinding.editTextInputBillExpireDate.setOnClickListener {
            val c = Calendar.getInstance(Locale.US)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                parentView.context,
                DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
                    date = datePicker
                    bottomSheetBinding.editTextInputBillExpireDate.setText("$dayOfMonth/0${month + 1}/$year")
                }, c.get(Calendar.YEAR), month, day
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
                    !bottomSheetBinding.editTextInputBillExpireDate.text.isNullOrEmpty() &&
                    !bottomSheetBinding.spinnerExposed.text.isNullOrEmpty()
                ) {

                    val selectedType = bottomSheetBinding.spinnerExposed.text.toString()
                    val billName = bottomSheetBinding.editTextInputBillName.text.toString()
                    val billPrice = bottomSheetBinding.editTextInputBillPrice.text.toString()

                    /**
                     * Create a variable time of the type
                     * Calendar that receive the type that the
                     * user selected. using time.time to save
                     * a type Date
                     */
                    val time = Calendar.getInstance().apply {
                        set(date.year, date.month, date.dayOfMonth)
                    }

                    val billObject =
                        BillsModel(
                            null,
                            null,
                            billPrice,
                            selectedType,
                            billName,
                            time.time,
                            StatusBills.NOTPAID.status
                        )
                    callback(billObject, 0)
                    bottomSheetDialog.dismiss()
                } else
                    feedback(parentView, R.string.validation_registration_failure, R.color.failure)
                    bottomSheetDialog.dismiss()
            }

        } else {
            bottomSheetBinding.editTextInputBillName.setText(bill.name_bill)
            bottomSheetBinding.editTextInputBillPrice.setText(bill.price)
            bottomSheetBinding.saveBillButtom.visibility = View.GONE
            bottomSheetBinding.editTextInputBillExpireDate.visibility = View.VISIBLE
            bottomSheetBinding.editBillButton.visibility = View.VISIBLE
            bottomSheetBinding.deleteBillButton.visibility = View.VISIBLE
            bottomSheetBinding.payBillButton.visibility = View.VISIBLE

            bottomSheetBinding.editBillButton.setOnClickListener {
                if (!bottomSheetBinding.editTextInputBillName.text.isNullOrEmpty() &&
                    !bottomSheetBinding.editTextInputBillPrice.text.isNullOrEmpty() &&
                    !bottomSheetBinding.editTextInputBillExpireDate.text.isNullOrEmpty() &&
                    !bottomSheetBinding.spinnerExposed.text.isNullOrEmpty()
                ) {
                    val selectedType = bottomSheetBinding.spinnerExposed.text.toString()
                    val billName = bottomSheetBinding.editTextInputBillName.text.toString()
                    val billPrice = bottomSheetBinding.editTextInputBillPrice.text.toString()

                    val time = Calendar.getInstance().apply {
                        set(date.year, date.month, date.dayOfMonth)
                    }
                    val billObject =
                        BillsModel(
                            bill.id_bill,
                            bill.id_user,
                            billPrice,
                            selectedType,
                            billName,
                            time.time,
                            StatusBills.NOTPAID.status
                        )
                    callback(billObject, 1)
                    bottomSheetDialog.dismiss()
                } else {
                    callback(bill, 3)
                    bottomSheetDialog.dismiss()
                }
            }

            bottomSheetBinding.deleteBillButton.setOnClickListener {
                callback(bill, 2)
                bottomSheetDialog.dismiss()
            }
            bottomSheetBinding.payBillButton.setOnClickListener {
                bill.status = StatusBills.PAID.status
                callback(bill, 4)
                bottomSheetDialog.dismiss()
            }
        }
    }
}
















