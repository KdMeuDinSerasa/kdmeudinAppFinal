package com.example.kdmeudinheiro.pieChart

import android.graphics.Color
import android.view.View
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.MainFragmentBinding
import com.example.kdmeudinheiro.enums.TipType
import com.example.kdmeudinheiro.enums.TypesOfBills
import com.example.kdmeudinheiro.interfaces.ChartClickInterceptor
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.model.IncomeModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class PieChartClass(
    val parentView: View,
    var listBills: List<BillsModel>,
    val incomes: IncomeModel,
    val outComes: Float,
    val clickInterceptor: ChartClickInterceptor,
) :
    OnChartValueSelectedListener {

    private lateinit var binding: MainFragmentBinding

    fun loadChart() {

        binding = MainFragmentBinding.bind(parentView)

        val category = ArrayList<String>()
        category.add("")
        category.add("")
        category.add("")
        category.add("")
        category.add("")
        /* Aways create the same quantity */

        /* values colors*/
        val colors = java.util.ArrayList<Int>()
        colors.add(Color.RED)
        colors.add(Color.GREEN)
        colors.add(Color.YELLOW)
        colors.add(Color.CYAN)
        colors.add(Color.GRAY)

        /* create the arrays */
        val pieChartEntry = ArrayList<Entry>()
        val arrayPercentages = arrayListOf(0f, 0f, 0f, 0f)
        val priceArray = arrayListOf(0f, 0f, 0f, 0f)

        /* take the bill prices sum all and make a new array*/
        listBills.forEach {
            if (it.type_bill == TypesOfBills.EMERGENCY_BILL.catName) {
                priceArray[0] += it.price.toFloat()
            }
            if (it.type_bill == TypesOfBills.LEISURE_BILLS.catName) {
                priceArray[1] += it.price.toFloat()
            }
            if (it.type_bill == TypesOfBills.FIX_BILLS.catName) {
                priceArray[2] += it.price.toFloat()
            }
            if (it.type_bill == TypesOfBills.MONTHLY_BILLS.catName) {
                priceArray[3] += it.price.toFloat()
            }
        }

        /*the the array with all prices sum, and transform to percentages */
        for (price in priceArray.withIndex()) {
            val income = incomes.income.toFloat()
            val final = 100 - (((income - price.value) / income) * 100)
            arrayPercentages[price.index] = +final

        }

        /* make the dynamic colors to no show empty percentages */
        val colorsFix = java.util.ArrayList<Int>()
        for (categories in arrayPercentages.withIndex()) {
            if (categories.value != 0F) {
                pieChartEntry.add(Entry(categories.value, categories.index))
                colorsFix.add(colors[categories.index])
            }
        }


        val income = incomes.income.toFloat()
        val final = (((income - outComes) / income) * 100)
        pieChartEntry.add(Entry(final, pieChartEntry.size))
        colorsFix.add(colors[4])


        setData(category, pieChartEntry, colorsFix)
    }

    private fun setData(cat: ArrayList<String>, pieEntries: ArrayList<Entry>?, colors: List<Int>) {

        //mpie data set related.
        val mpieDataset = PieDataSet(pieEntries, null)
        mpieDataset.colors = colors
        mpieDataset.valueTextSize = 16f
        mpieDataset.setValueFormatter(PercentFormatter())


        val dataSet = PieData(cat, mpieDataset)

        //bindings
        binding.chartIncluded.pieChart.data = dataSet
        binding.chartIncluded.pieChart.holeRadius = 2f
        binding.chartIncluded.pieChart.setHoleColor(R.color.PinkForbg)
        binding.chartIncluded.pieChart.setCenterTextSizePixels(150f)
        binding.chartIncluded.pieChart.setDescription(null)
        binding.chartIncluded.pieChart.animateXY(3000, 3000)
        binding.chartIncluded.pieChart.elevation = 50f
        binding.chartIncluded.pieChart.legend.isEnabled = false
        binding.chartIncluded.pieChart.setOnChartValueSelectedListener(this)
        binding.chartIncluded.pieChart.getEntriesAtIndex(0)
        binding.chartIncluded.pieChart.getEntriesAtIndex(1)
        binding.chartIncluded.pieChart.getEntriesAtIndex(2)


    }


    /*intercept the click at the chart, call a interface to pass the selected index
    to call the correct information at the bottom dialog*/
    override fun onValueSelected(e: Entry?, dataSetIndex: Int, h: Highlight?) {
        when (e?.xIndex) {
            0 -> clickInterceptor.interceptClick(TipType.CHART_EMERGENCY.type)
            1 -> clickInterceptor.interceptClick(TipType.CHART_LEISURE.type)
            2 -> clickInterceptor.interceptClick(TipType.CHART_FIX.type)
            3 -> clickInterceptor.interceptClick(TipType.CHART_MONTHLY.type)
        }
    }

    override fun onNothingSelected() {

    }
}