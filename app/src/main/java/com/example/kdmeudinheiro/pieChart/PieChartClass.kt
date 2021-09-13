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
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class PieChartClass(val parentView: View, var listBills: List<BillsModel>, val income: IncomeModel) :
    SeekBar.OnSeekBarChangeListener,
    OnChartValueSelectedListener {

    private lateinit var binding: MainFragmentBinding


    fun loadChart() {

        binding = MainFragmentBinding.bind(parentView)
        /* values x */
        val category = ArrayList<String>()
        category.add("teste1")
        category.add("teste2")
        category.add("teste3")
        category.add("teste4")
        /* Aways create the same quantity */

        val listaTeste = arrayListOf<Entry>()
        listaTeste.add(Entry(22f, 0))
        listaTeste.add(Entry(22f, 1))
        listaTeste.add(Entry(22f, 2))
        listaTeste.add(Entry(22f, 3))

        /* values colors*/
        val colors = java.util.ArrayList<Int>()
        colors.add(Color.GREEN)
        colors.add(Color.RED)
        colors.add(Color.GRAY)
        colors.add(Color.CYAN)


//        val pieChartEntry = ArrayList<Entry>()
//        val arrayDoubles = arrayOf<Double>(0.0, 0.0, 0.0, 0.0)
//        val mIncome = income?.let { income -> income.income.toDouble() / 100 }
//        listBills?.map { bills ->
//            when (bills.type_bill) {
//                TypesOfBills.FIX_BILLS.catName -> arrayDoubles[0] += (((bills.price.toDouble() / 100) - mIncome!!) * 100)
//                TypesOfBills.LEISURE_BILLS.catName -> arrayDoubles[1] += (((bills.price.toDouble() / 100) - mIncome!!) * 100)
//                TypesOfBills.MONTHLY_BILLS.catName -> arrayDoubles[2] += (((bills.price.toDouble() / 100) - mIncome!!) * 100)
//                TypesOfBills.EMERGENCY_BILL.catName -> arrayDoubles[3] += (((bills.price.toDouble() / 100) - mIncome!!) * 100)
//                else -> null
//            }
//        }
//        for (categories in arrayDoubles.withIndex()) {
//            pieChartEntry.add(Entry(categories.value.toFloat(), categories.index))
//        }
        print("yuhu")

        /*Load Chart*/
        setData(category, listaTeste, colors)
    }

    private fun setData(cat: ArrayList<String>, pieEntries: ArrayList<Entry>?, colors: List<Int>) {




        /* mPie dataSet related */
        val mpieDataset = PieDataSet(pieEntries, "dados")
        mpieDataset.colors = colors



        //  mpieDataset.setColors(colors);
        mpieDataset.valueTextSize = 16f

        val dataSet = PieData(cat, mpieDataset)

        //bindings
        binding.chartIncluded.pieChart.data = dataSet
        binding.chartIncluded.pieChart.holeRadius = 2f
//        binding.chartIncluded.pieChart.setHoleColor(R.color.PinkForbg)
//        binding.chartIncluded.pieChart.setCenterTextSizePixels(150f)
        binding.chartIncluded.pieChart.setDescription(null)
        binding.chartIncluded.pieChart.animateXY(3000, 3000)
//        binding.chartIncluded.pieChart.elevation = 50f


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
