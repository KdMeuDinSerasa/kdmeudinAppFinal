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
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*


class BottomSheet(
    val parentView: View,
    val bill: BillsModel?,
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
                     * Calendar that receive the tipe that the
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

                    true
                } else
                    false
            }

        } else {
            bottomSheetBinding.editTextInputBillName.setText(bill.name_bill)
            bottomSheetBinding.editTextInputBillPrice.setText(bill.price)
//            bottomSheetBinding.editTextInputBillExpireDate.setText(bill.expire_date)

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
                    val billDate = bottomSheetBinding.editTextInputBillExpireDate.text.toString()

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

class bottomSheetIncome(val parentView: View) {
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

class BottomSheetChart(
    val parentView: View,
    val typeClicked: Int,
    val clickInterceptor: ChartClickInterceptor,
    val articleList: List<Articles>
) {
    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetBinding: TipChartBinding

    private lateinit var recyclerView: RecyclerView
    private var adapter = AdapterChartTips() {
        clickInterceptor.interceptSelectedArticle(it)
    }

    fun loadBottomSheet() {
        /* setup bottom sheet */
        bottomSheetView = View.inflate(parentView.context, R.layout.tip_chart, null)
        bottomSheetDialog = BottomSheetDialog(parentView.context)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
        bottomSheetBinding = TipChartBinding.bind(bottomSheetView)
        /* setup recyclerview */

        recyclerView = bottomSheetBinding.recyclerViewIdTipChart
        recyclerView.layoutManager = LinearLayoutManager(bottomSheetView.context)
        recyclerView.adapter = adapter


        /* filter to show based at parameter */
        if (typeClicked == 4 /* fix */) {
            bottomSheetBinding.textViewTipChart.text =
                bottomSheetView.context.getString(R.string.text_tip_chart_fix)
            bottomSheetBinding.recyclerViewIdTipChart.visibility = View.GONE
            bottomSheetBinding.materialCardForChartTips.visibility = View.VISIBLE
            bottomSheetBinding.webViewList.loadUrl("https://www.serasa.com.br/ensina/como-ganhar-dinheiro/")
        } else if (typeClicked == 5 /* leisure */) {
            bottomSheetBinding.textViewTipChart.text = "Lazer"
            bottomSheetBinding.materialCardForChartTips.visibility = View.GONE
            var list = articleList.filter { it.typeArticle.toInt() == 1 }
            adapter.update(list)
        } else if (typeClicked == 6 /* emergency */) {
            bottomSheetBinding.textViewTipChart.text = "Emergenciais"
            bottomSheetBinding.materialCardForChartTips.visibility = View.GONE
            var list = articleList.filter { it.typeArticle.toInt() == 2 }
            adapter.update(list)
        } else if (typeClicked == 7 /* monthly */) {
            bottomSheetBinding.textViewTipChart.text =
                bottomSheetView.context.getString(R.string.text_tip_chart_monthly)
            bottomSheetBinding.recyclerViewIdTipChart.visibility = View.GONE
            bottomSheetBinding.materialCardForChartTips.visibility = View.VISIBLE
            bottomSheetBinding.webViewList.loadUrl("https://www.serasa.com.br/ensina/suas-economias/")
        }
    }
}












