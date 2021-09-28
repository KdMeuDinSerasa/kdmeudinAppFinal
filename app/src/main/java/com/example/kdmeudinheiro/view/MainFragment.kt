package com.example.kdmeudinheiro.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.bottomSheet.BottomSheetChart
import com.example.kdmeudinheiro.bottomSheet.BottomSheetIncome
import com.example.kdmeudinheiro.databinding.MainFragmentBinding
import com.example.kdmeudinheiro.enums.KeysShared
import com.example.kdmeudinheiro.interfaces.ChartClickInterceptor
import com.example.kdmeudinheiro.model.Articles
import com.example.kdmeudinheiro.model.IncomeModel
import com.example.kdmeudinheiro.pieChart.PieChartClass
import com.example.kdmeudinheiro.utils.feedback
import com.example.kdmeudinheiro.utils.formatCurrency
import com.example.kdmeudinheiro.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment), ChartClickInterceptor {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private var userId = ""
    private var incomeValue: IncomeModel? = null
    private var restValue: Double? = null
    private var outCome: Double? = null
    private var articlesList = mutableListOf<Articles>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = MainFragmentBinding.bind(view)
        loadViewModels()
        loadComponents()
        checkUser()
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
                binding.addYourIncomeWarn.visibility = View.GONE
                val auxFormat = it.income.toDouble()
                binding.incomeValue.text = "Renda Mensal: ${auxFormat.formatCurrency()}"
                incomeValue = it
                viewModel.getOutcome(userId)
            } else {
                binding.addYourIncomeWarn.visibility = View.VISIBLE
                binding.tvNoGraph.visibility = View.VISIBLE
                binding.ivNoGraph.visibility = View.VISIBLE
            }
        })
        viewModel.mError.observe(viewLifecycleOwner, {
            feedback(requireView(), R.string.error_to_excute_action, R.color.failure)
        })
        viewModel.articlesList.observe(viewLifecycleOwner, {
            articlesList.clear()
            articlesList.addAll(it)
        })
        viewModel.outCome.observe(viewLifecycleOwner, {
            if (incomeValue == null)
                binding.tvRest.text = "Sobras 0"
            else {
                val sum = incomeValue?.income?.toDouble()?.minus(it!!.toDouble())
                binding.tvRest.text = "Sobras: ${sum?.formatCurrency()}"
            }

            binding.tvOutcome.text = "Gastos totais: ${it?.formatCurrency()}"
            outCome = it
            restValue = (incomeValue?.income?.toDouble()?.minus(it!!.toDouble()))

        })

        viewModel.billsPercentage.observe(viewLifecycleOwner, {
            if (it.size >= 1 && incomeValue != null) {
                binding.chartIncluded.pieChart.visibility = View.VISIBLE
                binding.ivGraphLegend.cardLegend.visibility = View.VISIBLE
                binding.ivNoGraph.visibility = View.GONE
                binding.tvNoGraph.visibility = View.GONE
                binding.addYourIncomeWarn.visibility = View.GONE
                PieChartClass(
                    requireView(),
                    it,
                    incomeValue!!,
                    outCome!!.toFloat(),
                    this
                ).loadChart()
            } else {
                binding.chartIncluded.pieChart.visibility = View.GONE
                binding.ivGraphLegend.cardLegend.visibility = View.GONE
                binding.ivNoGraph.visibility = View.VISIBLE
                binding.tvNoGraph.visibility = View.VISIBLE
            }

        })

    }

    private fun loadComponents() {
        viewModel.getArticles()
        binding.addButton.setOnClickListener {
            BottomSheetIncome(requireView()).loadIncome() { incomeFound ->
                val mIncome = IncomeModel(null, incomeFound.toString(), userId)
                if (incomeValue == null)
                    viewModel.addIncome(mIncome)
                else {
                    incomeValue!!.income = incomeFound.toString()
                    viewModel.editIncome(incomeValue!!)
                }

                viewModel.getIncome(userId)
            }
        }

    }

    override fun interceptClick(index: Int) {
        BottomSheetChart(requireView(), index, this, articlesList).loadBottomSheet()
    }

    override fun interceptSelectedArticle(article: Articles) {
        val browser = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
        startActivity(browser)
    }

    fun checkUser() {
        val mSharedPreferences =
            requireActivity().getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)
        userId = mSharedPreferences.getString(KeysShared.USERID.key, "").toString()
        if (userId.isNullOrBlank()) {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }
        viewModel.getIncome(userId)
    }
}