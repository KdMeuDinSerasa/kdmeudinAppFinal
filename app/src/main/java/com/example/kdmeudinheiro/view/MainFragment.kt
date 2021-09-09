package com.example.kdmeudinheiro.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.MainFragmentBinding
import com.example.kdmeudinheiro.model.IncomeModel
import com.example.kdmeudinheiro.viewModel.MainViewModel

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

    fun loadViewModels(){
        viewModel.mFirebaseUser.observe(viewLifecycleOwner, {
            if (it != null) {
                userId = it.uid
                viewModel.getIncome(userId)
                viewModel.getOutcome(userId)
            }
        })

        viewModel.mIncomeModel.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.incomeValue.text = "Renda Mensal: ${it.income}"
                binding.btnAddIncome.text = "Editar"
                incomeValue = it
            }
        })
        viewModel.mError.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "Erro $it", Toast.LENGTH_SHORT).show()
        })
        viewModel.outCome.observe(viewLifecycleOwner, {
            binding.tvRest.text = (incomeValue!!.income.toDouble() - it!!.toDouble()).toString()
            binding.tvOutcome.text = "gastos totais: $it"
        })
        viewModel.totalBills.observe(viewLifecycleOwner, {
            binding.billChartFromInclude.textViewTotalOfBills.text = "Total de contas: ${it.toString()}"
            binding.billChartFromInclude.textViewBillToPay.text = "Total há pagar: ${it.toString()}"
        })
    }
    fun loadComponents(){
        binding.btnAddIncome.setOnClickListener {
        if (!binding.etAddIncome.text.toString().isNullOrBlank()){
            if (incomeValue == null){
                viewModel.addIncome(IncomeModel(null, binding.etAddIncome.text.toString(), userId))
                binding.btnAddIncome.isClickable = false
                viewModel.getIncome(userId)
            } else {
                incomeValue!!.income = binding.etAddIncome.text.toString()
                viewModel.editIncome(incomeValue!!)
                viewModel.getIncome(userId)
            }

        }  else Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            
        }


    }


}