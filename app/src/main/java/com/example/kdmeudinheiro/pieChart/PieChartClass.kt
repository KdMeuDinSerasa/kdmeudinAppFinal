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

class PieChartClass(val parentView: View, var listBills: List<BillsModel>, val incomes: IncomeModel, val restValue: Float, val outComes: Float) :
    SeekBar.OnSeekBarChangeListener,
    OnChartValueSelectedListener {

    private lateinit var binding: MainFragmentBinding


    fun loadChart() {

        binding = MainFragmentBinding.bind(parentView)

        val category = ArrayList<String>()
        category.add("Emergenciais")
        category.add("Lazer")
        category.add("Fixas")
        category.add("Lazer")
        category.add("sobras")
        /* Aways create the same quantity */



        /* values colors*/
        val colors = java.util.ArrayList<Int>()
        colors.add(Color.GREEN)
        colors.add(Color.RED)
        colors.add(Color.GRAY)
        colors.add(Color.CYAN)
        colors.add(Color.BLUE)

        val pieChartEntry = ArrayList<Entry>()
        val arrayDoubles = arrayListOf<Float>(0f, 0f, 0f, 0f)

        listBills.forEach {
            if (it.type_bill == TypesOfBills.EMERGENCY_BILL.catName){
                val price = it.price.toFloat()* 100
                val income = incomes.income.toFloat()* 100
                val subtraction = income / price * 100
                val multiply = subtraction / 10
                arrayDoubles[0] =+ multiply * 0.1F
            }
            if (it.type_bill == TypesOfBills.LEISURE_BILLS.catName){
                val price = it.price.toFloat()* 100
                val income = incomes.income.toFloat() * 100
                val subtraction = income / price * 100
                val multiply = subtraction / 10
                arrayDoubles[1] =+ multiply * 0.1F
            }
            if (it.type_bill == TypesOfBills.FIX_BILLS.catName){
                val price = it.price.toFloat()* 100
                val income = incomes.income.toFloat() * 100
                val subtraction = income / price * 100
                val multiply = subtraction / 10
                arrayDoubles[2] =+ multiply * 0.1F
            }
            if (it.type_bill == TypesOfBills.MONTHLY_BILLS.catName){
                val price = it.price.toFloat()* 100
                val income = incomes.income.toFloat() * 100
                val subtraction = income / price * 100
                val multiply = subtraction / 10
                arrayDoubles[3] =+ multiply * 0.1F
            }
        }
        for (categories in arrayDoubles.withIndex()) {
            pieChartEntry.add(Entry(categories.value, categories.index))
        }

        val income = incomes.income.toFloat()
        val rest = restValue * 100 / income
        pieChartEntry.add(Entry(rest, 5))


        val mpieDataset = PieDataSet(pieChartEntry, "dados")
        mpieDataset.colors = colors

        //  mpieDataset.setColors(colors);
        mpieDataset.valueTextSize = 16f
        mpieDataset.setValueFormatter(PercentFormatter())
        val dataSet = PieData(category, mpieDataset)

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

    private fun setData(cat: ArrayList<String>, pieEntries: ArrayList<Entry>?, colors: List<Int>) {


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
