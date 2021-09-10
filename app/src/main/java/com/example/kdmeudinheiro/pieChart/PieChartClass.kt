package com.example.kdmeudinheiro.pieChart

import android.view.View
import com.example.kdmeudinheiro.databinding.MainFragmentBinding
import com.example.kdmeudinheiro.enums.TypesOfBills
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet

class PieChartClass(val parentView: View) /*: SeekBar.OnSeekBarChangeListener,
    OnChartValueSelectedListener */{

    private lateinit var binding: MainFragmentBinding

    fun loadChart() {
        binding = MainFragmentBinding.bind(parentView)
        /**
         * valores X
         */
        val category = ArrayList<String>()
        category.add(TypesOfBills.MONTHLY_BILLS.catName)
        category.add(TypesOfBills.EMERGENCY_BILL.catName)
        category.add(TypesOfBills.FIX_BILLS.catName)
        category.add(TypesOfBills.LEISURE_BILLS.catName)

        /**
         * Sempre criar na mesma quantidade de categorias
         * primeiro parametro o valor o segundo a posiçao
         */
        val pieChartEntry = ArrayList<Entry>()
        pieChartEntry.add(Entry(23f, 0))
        pieChartEntry.add(Entry(23f, 1))
        pieChartEntry.add(Entry(23f, 2))
        pieChartEntry.add(Entry(23f, 3))

        /**
         * Carrega o grafico
         */
        val mpieDataset = PieDataSet(pieChartEntry, "Consumir dados")
        val data = PieData(category, mpieDataset)
        binding.chartIncluded.pieChart.data = data
    }
    }