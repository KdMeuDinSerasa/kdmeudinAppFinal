package com.example.kdmeudinheiro.pieChart


import android.R.attr
import android.graphics.Color
import android.os.Build
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
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import android.R.attr.entries
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class PieChartClass(
    val parentView: View,
    var listBills: List<BillsModel>,
    val income: IncomeModel
) :
    SeekBar.OnSeekBarChangeListener,
    OnChartValueSelectedListener {

    private lateinit var binding: MainFragmentBinding
    private val chart: PieChart? = null

    fun loadChart() {

        binding = MainFragmentBinding.bind(parentView)
        /* values x */
        val category = ArrayList<String>()
        category.add(TypesOfBills.FIX_BILLS.catName)
        category.add(TypesOfBills.LEISURE_BILLS.catName)
        category.add(TypesOfBills.MONTHLY_BILLS.catName)
        category.add(TypesOfBills.EMERGENCY_BILL.catName)
        /* Aways create the same quantity */

        /* values colors*/
//        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            binding.chartIncluded.pieChart.setHardwareAccelerationEnabled(false); }
        val colors: ArrayList<Int> = ArrayList()

        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())


        var pieChartEntry = mutableListOf<PieEntry>()
        val arrayDoubles = arrayOf<Float>(0f, 0f, 0f, 0f)
        val mIncome = income?.let { income -> income.income.toDouble() / 100 }
        listBills?.map { bills ->
            when (bills.type_bill) {
                TypesOfBills.FIX_BILLS.catName -> arrayDoubles[0] += (((bills.price.toDouble() / 100) - mIncome!!) * 100).toFloat()
                TypesOfBills.LEISURE_BILLS.catName -> arrayDoubles[1] += (((bills.price.toDouble() / 100) - mIncome!!) * 100).toFloat()
                TypesOfBills.MONTHLY_BILLS.catName -> arrayDoubles[2] += (((bills.price.toDouble() / 100) - mIncome!!) * 100).toFloat()
                TypesOfBills.EMERGENCY_BILL.catName -> arrayDoubles[3] += (((bills.price.toDouble() / 100) - mIncome!!) * 100).toFloat()
                else -> null
            }
        }
        for (categories in arrayDoubles.withIndex()) {
            if (categories.index == 0) {
                val value = categories.value.toFloat()
                pieChartEntry.add(PieEntry(value, 0))
            } else if (categories.index == 1) {
                val value = categories.value.toFloat()
                pieChartEntry.add(PieEntry(value, 1))
            } else if (categories.index == 2) {
                val value = categories.value.toFloat()
                pieChartEntry.add(PieEntry(value, 2))
            } else if (categories.index == 3) {
                val value = categories.value.toFloat()
                pieChartEntry.add(PieEntry(value, 3))
            }
        }
        var pieChartEntry1 = mutableListOf<PieEntry>()
        pieChartEntry1.add(PieEntry(20f, 1))
        pieChartEntry1.add(PieEntry(30f, 2))
        pieChartEntry1.add(PieEntry(20f, 3))
        pieChartEntry1.add(PieEntry(20f, 4))
        pieChartEntry1.add(PieEntry(20f, 5))



        print("yuhu")

        /*Load Chart*/
        setData(category, pieChartEntry, colors)
    }

    private fun setData(
        cat: ArrayList<String>,
        pieEntries: List<PieEntry>?,
        colors: List<Int>
    ) {

        /* mPie dataSet related */
        val mpieDataset = PieDataSet(pieEntries, null)

        val data = PieData(mpieDataset)
        data.setValueFormatter(PercentFormatter())
        mpieDataset.setColors(colors)

        //mpieDataset.setColors(colors);
        mpieDataset.valueTextSize = 16f
        mpieDataset.setColors(colors)
        //bindings
        binding.chartIncluded.pieChart.data = data
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
        TODO("Not yet implemented")
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        TODO("Not yet implemented")
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        TODO("Not yet implemented")
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected() {
        TODO("Not yet implemented")
    }

}
