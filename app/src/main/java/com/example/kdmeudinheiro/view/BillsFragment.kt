package com.example.kdmeudinheiro.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.adapter.AdapterBillsList
import com.example.kdmeudinheiro.bottomSheet.BottomSheet
import com.example.kdmeudinheiro.bottomSheet.BottomSheetTips
import com.example.kdmeudinheiro.databinding.BillsFragmentBinding
import com.example.kdmeudinheiro.enums.KeysShared
import com.example.kdmeudinheiro.enums.TipType
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.viewModel.BillsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillsFragment : Fragment(R.layout.bills_fragment) {
    //var goes here
    private lateinit var viewModel: BillsViewModel
    private lateinit var binding: BillsFragmentBinding
    private lateinit var recyclerView: RecyclerView

    private lateinit var userId: String
    private var adapter = AdapterBillsList() { bill ->
        BottomSheet(requireView(), bill).loadBottomBill() { billFromCb, type ->
            handlerForDialogResponse(billFromCb, type)
        }
    }

    /* Observers goes here */
    private var observerEdit = Observer<Boolean> {
        if (it == true) {
            Snackbar.make(requireView(), "Conta Editada Com Sucesso", Snackbar.LENGTH_LONG)
                .show()
        } else {
            Snackbar.make(requireView(), "Deu erro nessa merda.", Snackbar.LENGTH_LONG)
                .show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(BillsViewModel::class.java)
        LoadViewModelAndsObservers()
        binding = BillsFragmentBinding.bind(view)
        recyclerView = binding.recyclerViewIdNoXML
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        loadBinding()
        checkUser()
    }


    private fun loadBinding() {
        binding.floatButtonAddBill.setOnClickListener {
            BottomSheet(requireView(), null).loadBottomBill() { bill, type ->
                bill.id_user = userId
                viewModel.addBill(bill)
                viewModel.getAllBills(userId)
            }
        }
        binding.About.setOnClickListener {
            BottomSheetTips(requireView(), TipType.TIP_BILL_CATEGORY).loadTip()
        }
    }

    private fun LoadViewModelAndsObservers() {
        viewModel.billList.observe(viewLifecycleOwner, { adapter.refresh(it.toMutableList()) })
        viewModel.error.observe(viewLifecycleOwner, {
            if (it != null) {
                Snackbar.make(requireView(), "Erro ao solicitar contas ${it}", Snackbar.LENGTH_LONG)
                    .show()
            }
        })
        viewModel.addResponse.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(requireView(), "Conta Adicionada Com Sucesso", Snackbar.LENGTH_LONG)
                    .show()
            } else {
                Snackbar.make(requireView(), "Erro ao adicionar a conta", Snackbar.LENGTH_LONG)
                    .show()
            }
        })

    }

    private fun handlerForDialogResponse(billFromCb: BillsModel, type: Int?) {

        if (type == 1) {
            viewModel.editBill(billFromCb)
            viewModel.getAllBills(userId)
            observerEdit.onChanged(true)

        } else if (type == 2) {
            viewModel.deleteBill(billFromCb)
            viewModel.getAllBills(userId)

        } else if (type == 3) {
            observerEdit.onChanged(false)

        } else if (type == 4) {
            viewModel.editBill(billFromCb)
            viewModel.getAllBills(userId)
            Snackbar.make(requireView(), "Conta Paga Com Sucesso", Snackbar.LENGTH_LONG)
                .show()
        }
        else viewModel.getAllBills(userId)
    }

    companion object {
        fun newInstance() = BillsFragment()
    }
    fun checkUser() {
        val mSharedPreferences =
            requireActivity().getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)
        userId = mSharedPreferences.getString(KeysShared.USERID.key, "").toString()
        if (userId.isNullOrBlank()) {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }
        viewModel.getAllBills(userId)
    }
}