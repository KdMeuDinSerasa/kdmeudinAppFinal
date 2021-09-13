package com.example.kdmeudinheiro.pieChart


import android.graphics.Color
import android.view.View
import android.widget.SeekBar
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.MainFragmentBinding
import com.example.kdmeudinheiro.enums.TypesOfBills
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class PieChartClass(val parentView: View, var pieChartEntry: ArrayList<Entry>) :
    SeekBar.OnSeekBarChangeListener,
    OnChartValueSelectedListener {

    private lateinit var binding: MainFragmentBinding


    fun loadChart() {

        binding = MainFragmentBinding.bind(parentView)
        /* values x */
        val category = ArrayList<String>()
        category.add(TypesOfBills.FIX_BILLS.catName)
        category.add(TypesOfBills.LEISURE_BILLS.catName)
        category.add(TypesOfBills.MONTHLY_BILLS.catName)
        category.add(TypesOfBills.EMERGENCY_BILL.catName)
        /* Aways create the same quantity */


        /*Load Chart*/

        setData(category, pieChartEntry)
    }

    private fun setData(cat: ArrayList<String>, pieEntries: ArrayList<Entry>?) {

        /* mPie DATA SET related */
        val mpieDataset = PieDataSet(pieEntries, null)
        var dataSet = PieData(cat, mpieDataset)
        val colors = java.util.ArrayList<Int>()
        colors.add(Color.GREEN)
        colors.add(Color.RED)
        colors.add(Color.GRAY)
        colors.add(Color.BLUE)
        mpieDataset.colors = colors


        //  mpieDataset.setColors( IntArray(colors));
        mpieDataset.valueTextSize = 16f

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
