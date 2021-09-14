package com.example.kdmeudinheiro.pieChart


import android.graphics.Color
import android.view.View
import android.widget.SeekBar
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.MainFragmentBinding
import com.example.kdmeudinheiro.enums.TypesOfBills
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.model.IncomeModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
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
    val restValue: Float,
    val outComes: Float
) :
    SeekBar.OnSeekBarChangeListener,
    OnChartValueSelectedListener {

    private lateinit var binding: MainFragmentBinding


    fun loadChart() {

        binding = MainFragmentBinding.bind(parentView)

        val category = ArrayList<String>()
        category.add("Emergencial")
        category.add("Lazer")
        category.add("Fixas")
        category.add("Mensais")
        category.add("sobras")
        /* Aways create the same quantity */


        /* values colors*/
        val colors = java.util.ArrayList<Int>()
        colors.add(Color.RED)
        colors.add(Color.GREEN)
        colors.add(Color.YELLOW)
        colors.add(Color.CYAN)
        colors.add(Color.GRAY)

        val pieChartEntry = ArrayList<Entry>()
        val arrayDoubles = arrayListOf<Float>(0f, 0f, 0f, 0f)
        val priceArray = arrayListOf<Float>(0f, 0f, 0f, 0f)
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

        for (price in priceArray.withIndex()) {
            val income = incomes.income.toFloat()
            val final = 100 - (((income - price.value) / income) * 100)
            arrayDoubles[price.index] = +final

        }

        for (categories in arrayDoubles.withIndex()) {
            pieChartEntry.add(Entry(categories.value, categories.index))
        }


        val income = incomes.income.toFloat()
        val final = (((income - outComes) / income) * 100)
        pieChartEntry.add(Entry(final, 5))



        setData(category, pieChartEntry, colors)
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


        val legend: Legend = binding.chartIncluded.pieChart.getLegend()
        legend.position = Legend.LegendPosition.ABOVE_CHART_CENTER
        legend.textSize = 16f

    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {


    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        //  TODO("Not yet implemented")
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        // TODO("Not yet implemented")
    }

    override fun onValueSelected(e: Entry?, dataSetIndex: Int, h: Highlight?) {
        // TODO("Not yet implemented")
    }

    override fun onNothingSelected() {
        //   TODO("Not yet implemented")
    }
}

