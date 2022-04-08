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
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.model.IncomeModel
import com.example.kdmeudinheiro.pieChart.PieChartClass
import com.example.kdmeudinheiro.utils.feedback
import com.example.kdmeudinheiro.utils.formatCurrency
import com.example.kdmeudinheiro.viewModel.MainViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment), ChartClickInterceptor {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private var userId = ""
    private var incomeValue: IncomeModel? = null
    private var restValue: Double? = null
    private var outCome: Double? = null
    private var articlesList = mutableListOf<Articles>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = MainFragmentBinding.bind(view)
        loadViewModelsObservers()
        loadComponents()
        checkUser()
    }


    private fun loadViewModelsObservers() {
        viewModel.mFirebaseUser.observe(viewLifecycleOwner, { checkUserIdAndRequestIncome(it) })

        viewModel.mIncomeModel.observe(viewLifecycleOwner, { handlerIncome(it) })

        viewModel.mError.observe(viewLifecycleOwner, { feedbackErrorOnExecuteAction() })

        viewModel.articlesList.observe(viewLifecycleOwner, { loadNewArticlesList(it) })

        viewModel.outCome.observe(viewLifecycleOwner, {
            if (it != null) {
                handlerOutCome(it)
            }
        })

        viewModel.billsPercentage.observe(viewLifecycleOwner, { handlerBillsList(it) })

    }

    private fun checkUserIdAndRequestIncome(user: FirebaseUser?) {
        if (user != null) {
            userId = user.uid
            viewModel.getIncome(userId)
        }
    }

    private fun handlerIncome(incomeFounded: IncomeModel?) {
        if (incomeFounded != null) {
            binding.addYourIncomeWarn.visibility = View.GONE
            val auxFormat = incomeFounded.income.toDouble()
            binding.incomeValue.text = "${this.getString(R.string.monthy_income)} ${auxFormat.formatCurrency()}"
            incomeValue = incomeFounded
            viewModel.getOutcome(userId)
        } else {
            setupIncomeWarns()
        }
    }

    private fun setupIncomeWarns() {
        binding.addYourIncomeWarn.visibility = View.VISIBLE
        binding.tvNoGraph.visibility = View.VISIBLE
        binding.ivNoGraph.visibility = View.VISIBLE
    }

    private fun feedbackErrorOnExecuteAction() {
        feedback(requireView(), R.string.error_to_excute_action, R.color.failure)
    }

    private fun loadNewArticlesList(newList: List<Articles>) {
        articlesList.clear()
        articlesList.addAll(newList)
    }

    private fun handlerOutCome(outcome: Double) {
        when (incomeValue) {
            null -> {
                binding.tvRest.text = this.getString(R.string.none_leftovers)
            }
            else -> {
                val sum = incomeValue?.income?.toDouble()?.minus(outcome)
                binding.tvRest.text =
                    "${this.getString(R.string.leftovers)} ${sum?.formatCurrency()}"
            }
        }
        outCome = outcome
        restValue = (incomeValue?.income?.toDouble()?.minus(outcome))
        binding.tvOutcome.text =
            "${this.getString(R.string.total_expenses)} ${outcome.formatCurrency()}"
    }

    private fun handlerBillsList(billsList: List<BillsModel>) {
        if (billsList.isNullOrEmpty() && incomeValue != null) {
            showPieChart()
            loadPieChartInfo(billsList)

        } else {
            hidePieChart()
        }
    }

    private fun showPieChart() {
        binding.chartIncluded.pieChart.visibility = View.VISIBLE
        binding.ivGraphLegend.cardLegend.visibility = View.VISIBLE
        binding.ivNoGraph.visibility = View.GONE
        binding.tvNoGraph.visibility = View.GONE
        binding.addYourIncomeWarn.visibility = View.GONE
    }

    private fun hidePieChart() {
        binding.chartIncluded.pieChart.visibility = View.GONE
        binding.ivGraphLegend.cardLegend.visibility = View.GONE
        binding.ivNoGraph.visibility = View.VISIBLE
        binding.tvNoGraph.visibility = View.VISIBLE
    }

    private fun loadPieChartInfo(billsList: List<BillsModel>) {
        val mIncome = incomeValue
        val mOutCome = outCome
        if (mIncome != null && mOutCome != null) {
            PieChartClass(
                requireView(),
                billsList,
                mIncome,
                mOutCome.toFloat(),
                this
            ).loadChart()
        } else {
            // TODO() error treatment
        }
    }

    private fun loadComponents() {
        viewModel.getArticles()
        binding.addButton.setOnClickListener {
            handleAddButtonClick()
        }

    }

    private fun handleAddButtonClick() {
        BottomSheetIncome(requireView()).loadIncome() { incomeFound ->
            val mIncome = IncomeModel(null, incomeFound.toString(), userId)

            if (incomeValue != null) {
                incomeValue!!.income = incomeFound.toString()
                viewModel.editIncome(incomeValue!!)
            } else {
                viewModel.addIncome(mIncome)
            }

            viewModel.getIncome(userId)
        }
    }

    override fun interceptClick(index: Int) {
        BottomSheetChart(requireView(), index, this, articlesList).loadBottomSheet()
    }

    override fun interceptSelectedArticle(article: Articles) {
        val browser = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
        startActivity(browser)
    }

    private fun checkUser() {
        val mSharedPreferences =
            requireActivity().getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)
        userId = mSharedPreferences.getString(KeysShared.USERID.key, "").toString()
        if (userId.isNullOrBlank()) {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }
        viewModel.getIncome(userId)
    }
}