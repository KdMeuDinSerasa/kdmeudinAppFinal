package com.example.kdmeudinheiro.bottomSheet

import android.app.DatePickerDialog
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
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
import com.example.kdmeudinheiro.model.Articles
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
            val c = Calendar.getInstance(Locale.US)
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                parentView.context,
                { _, year, month, dayOfMonth ->
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
                        BillsModel(
                            null,
                            null,
                            billPrice,
                            selectedType,
                            billName,
                            billExpireDate,
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
                            billDate,
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

class BottomSheetIncome(val parentView: View,  val typeClicked: Int){
    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetBinding: TipChartBinding

    private lateinit var recyclerView: RecyclerView
    private var adapter = AdapterChartTips(){

    }

    fun loadBottomSheet(){
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

        val listOfFixBills = mutableListOf<Articles>()
        listOfFixBills.add(Articles("poupa din", "https://clubedovalor.com.br/wp-content/uploads/2015/11/Como-Economizar-Dinheiro.jpg", "https://clubedovalor.com.br/economizar-dinheiro/"))
        listOfFixBills.add(Articles("Creditas", "https://api.creditas.io/exponencial/_next/image/?url=https%3A%2F%2Fexponencial-assets.creditas.com%2Fexponencial%2Fwp-content%2Fuploads%2F2018%2F04%2FComo-economizar-dinheiro-1.jpg&w=1200&q=90", "https://www.creditas.com/exponencial/como-economizar-dinheiro/"))
        val listOfLeisure = mutableListOf<Articles>()


        /* filter to show based at parameter */
        if(typeClicked ==  4 /* fix */){
            bottomSheetBinding.textViewTipChart.text = TODO()
            adapter.update(listOfFixBills)
        }
        else if (typeClicked == 5 /* leisure */){
            bottomSheetBinding.textViewTipChart.text = TODO()
            adapter.update(listOfFixBills)
        }
        else if (typeClicked == 6 /* emergency */){
            bottomSheetBinding.textViewTipChart.text = TODO()
        }
        else if (typeClicked == 7 /* monthly */){
            bottomSheetBinding.textViewTipChart.text = TODO()
            adapter.update(listOfFixBills)
        }
        else {}
    }
}











