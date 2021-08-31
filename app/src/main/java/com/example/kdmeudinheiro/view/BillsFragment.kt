package com.example.kdmeudinheiro.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kdmeudinheiro.viewModel.BillsViewModel
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.adapter.AdapterBillsList
import com.example.kdmeudinheiro.databinding.BillsFragmentBinding
import com.example.kdmeudinheiro.databinding.InputBillLayoutBinding
import com.example.kdmeudinheiro.model.BillsModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser

class BillsFragment : Fragment(R.layout.bills_fragment) {
    //var goes here
    private lateinit var viewModel: BillsViewModel
    private lateinit var binding: BillsFragmentBinding
    private lateinit var bottomSheetBinding: InputBillLayoutBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var adapter = AdapterBillsList(){

    }
    //observers gore here
    private var observerGetBills = Observer<List<BillsModel>> {
        adapter.refresh(it.toMutableList())
    }
    private var observerError = Observer<String> {
        Snackbar.make(requireView(), "Erro ao solicitar contas ${it}", Snackbar.LENGTH_LONG).show()
    }
    private var observerAddResponse = Observer<Boolean> {
        //TODO conta foi adicionada.
    }
    private var observerUser = Observer<FirebaseUser> { user ->
        viewModel.getAllBills(user.uid)
    }

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
        loadBinding(view)
    }

    private fun loadBinding(view: View) {
        binding.floatButtonAddBill.setOnClickListener {
            loadBottomSheet(view)
        }
    }
    fun loadBottomSheet(view: View) {

        bottomSheetView = View.inflate(view.context, R.layout.input_bill_layout, null)
        bottomSheetDialog = BottomSheetDialog(view.context)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
        bottomSheetView

        loadBottomSheetComponents(view)
    }

    fun loadBottomSheetComponents(view: View) {
        bottomSheetBinding = InputBillLayoutBinding.bind(view)

        val billName = bottomSheetBinding.editTextInputBillName.text
        //TODO

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