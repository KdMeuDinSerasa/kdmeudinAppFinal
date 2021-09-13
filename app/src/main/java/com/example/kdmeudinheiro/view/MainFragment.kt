package com.example.kdmeudinheiro.view

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.example.kdmeudinheiro.bottomSheet.bottomSheetIncome
import com.example.kdmeudinheiro.databinding.MainFragmentBinding
import com.example.kdmeudinheiro.model.IncomeModel
import com.example.kdmeudinheiro.viewModel.MainViewModel
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.enums.TypesOfBills
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.pieChart.PieChartClass
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private var userId = ""
    private var incomeValue: IncomeModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = MainFragmentBinding.bind(view)
        loadViewModels()
        loadComponents()
        viewModel.userLoged()
    }

    fun loadViewModels() {
        viewModel.mFirebaseUser.observe(viewLifecycleOwner, {
            if (it != null) {
                userId = it.uid
                viewModel.getIncome(userId)
            }
        })

        viewModel.mIncomeModel.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.incomeValue.text = "Renda Mensal: ${it.income}"
                incomeValue = it
                viewModel.getOutcome(userId)

            }
        })
        viewModel.mError.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "Erro $it", Toast.LENGTH_SHORT).show()
        })
        viewModel.outCome.observe(viewLifecycleOwner, {
            if (incomeValue == null)
                binding.tvRest.text = "Sobras 0"
            else
                binding.tvRest.text =
                    "Sobras " + (incomeValue?.income?.toDouble()?.minus(it!!.toDouble())).toString()
            binding.tvOutcome.text = "gastos totais: $it"
        })
        viewModel.billsPercentage.observe(viewLifecycleOwner, {
            PieChartClass(requireView(), it, incomeValue!!  ).loadChart()
        })

    }

    fun loadChart(listBills: List<BillsModel>){

        val category = ArrayList<String>()
        category.add("Emergenciais")
        category.add("Lazer")
        category.add("Fixas")
        category.add("Lazer")
        /* Aways create the same quantity */



        /* values colors*/
        val colors = java.util.ArrayList<Int>()
        colors.add(Color.GREEN)
        colors.add(Color.RED)
        colors.add(Color.GRAY)
        colors.add(Color.CYAN)



        val pieChartEntry = ArrayList<Entry>()
        val arrayDoubles = arrayListOf<Float>(0f, 0f, 0f, 0f)

        listBills.forEach {
            if (it.type_bill == TypesOfBills.EMERGENCY_BILL.catName)
                arrayDoubles[0] =+ it.price.toFloat()
            if (it.type_bill == TypesOfBills.LEISURE_BILLS.catName)
                arrayDoubles[1] =+ it.price.toFloat()
            if (it.type_bill == TypesOfBills.FIX_BILLS.catName)
                arrayDoubles[2] =+ it.price.toFloat()
            if (it.type_bill == TypesOfBills.MONTHLY_BILLS.catName)
                arrayDoubles[3] =+ it.price.toFloat()
        }

        for (categories in arrayDoubles.withIndex()) {
            pieChartEntry.add(Entry(categories.value, categories.index))
        }


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

    fun loadComponents() {
        binding.addButton.setOnClickListener {
            bottomSheetIncome(requireView()).loadIncome() { incomeFound ->
                var mIncome = IncomeModel(null, incomeFound.toString(), userId)
                viewModel.addIncome(mIncome)
                viewModel.getIncome(userId)
            }
        }

    }
}