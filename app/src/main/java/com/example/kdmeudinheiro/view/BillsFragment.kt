package com.example.kdmeudinheiro.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.adapter.AdapterBillsList
import com.example.kdmeudinheiro.bottomSheet.BottomSheet
import com.example.kdmeudinheiro.databinding.BillsFragmentBinding
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.viewModel.BillsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser

class BillsFragment : Fragment(R.layout.bills_fragment) {
    //var goes here
    private lateinit var viewModel: BillsViewModel
    private lateinit var binding: BillsFragmentBinding
    private lateinit var recyclerView: RecyclerView

    private lateinit var userId: String
    private var adapter = AdapterBillsList() { bill ->
        BottomSheet(requireView(), bill).loadBottomBill() { billFromCb, type ->
            if (type == 1) {
                viewModel.editBill(billFromCb)
                viewModel.getAllBills(userId)
                observerEdit.onChanged(true)

            } else if (type == 2) {
                viewModel.deleteBill(billFromCb)
                viewModel.getAllBills(userId)

            } else if (type == 3){
                observerEdit.onChanged(false)
                
            } else viewModel.getAllBills(userId)
        }
    }

    /* Observers goes here */
    private var observerGetBills = Observer<List<BillsModel>> {
        adapter.refresh(it.toMutableList())
    }
    private var observerError = Observer<String> {
        if (it != null) {
            Snackbar.make(requireView(), "Erro ao solicitar contas ${it}", Snackbar.LENGTH_LONG)
                .show()
        }
    }

    private var observerAddResponse = Observer<Boolean> {
        if (it == true) {
            Snackbar.make(requireView(), "Conta Adicionada Com Sucesso", Snackbar.LENGTH_LONG)
                .show()
        } else {
            Snackbar.make(requireView(), "Deu erro nessa merda.", Snackbar.LENGTH_LONG)
                .show()
        }
    }

    private var observerUser = Observer<FirebaseUser> { user ->
        viewModel.getAllBills(user.uid)
        userId = user.uid
    }

    private var observerEdit = Observer<Boolean> {
        if (it == true) {
            Snackbar.make(requireView(), "Conta Editada Com Sucesso", Snackbar.LENGTH_LONG)
                .show()
        } else {
            Snackbar.make(requireView(), "Deu erro nessa merda.", Snackbar.LENGTH_LONG)
                .show()
        }
    }
    private var observerDelete = Observer<Boolean> {
        if (it == true) {
            Snackbar.make(requireView(), "Conta Deletada Com Sucesso", Snackbar.LENGTH_LONG)
                .show()
        } else {
            Snackbar.make(requireView(), "Deu erro nessa merda.", Snackbar.LENGTH_LONG)
                .show()
        }
    }

    /* Lifecycle functions goes here */
    override fun onAttach(context: Context) {
        super.onAttach(context)
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
    }

    private fun loadBinding() {
        binding.floatButtonAddBill.setOnClickListener {
            BottomSheet(requireView(), null).loadBottomBill() { bill, type ->
                bill.id_user = userId
                viewModel.addBill(bill)
                viewModel.getAllBills(userId)
            }
        }
    }

    private fun LoadViewModelAndsObservers() {
        viewModel.billList.observe(viewLifecycleOwner, observerGetBills)
        viewModel.error.observe(viewLifecycleOwner, observerError)
        viewModel.addResponse.observe(viewLifecycleOwner, observerAddResponse)
        viewModel.user.observe(viewLifecycleOwner, observerUser)
        viewModel.getUserId()
    }

    companion object {
        fun newInstance() = BillsFragment()
    }
}