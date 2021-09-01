package com.example.kdmeudinheiro.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
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
import com.example.kdmeudinheiro.enums.TypesOfBills
import com.example.kdmeudinheiro.model.BillsModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser

class BillsFragment : Fragment(R.layout.bills_fragment) {
    //var goes here
    private lateinit var viewModel: BillsViewModel
    private lateinit var binding: BillsFragmentBinding
    private lateinit var bottomSheetBindingAddBill: InputBillLayoutBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var userId: String
    private var adapter = AdapterBillsList() {
        loadBottomSheetCrudBill(requireView(), it)
    }

    //observers gore here
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
            loadBottomSheetAddBill(view)
        }
    }

    /* load and runs bottom sheet who add the bills */
    fun loadBottomSheetAddBill(view: View) {

        bottomSheetView = View.inflate(view.context, R.layout.input_bill_layout, null)
        bottomSheetDialog = BottomSheetDialog(view.context)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
        bottomSheetBindingAddBill = InputBillLayoutBinding.bind(bottomSheetView)

        val listType = listOf<String>(
            TypesOfBills.EMERGENCY_BILL.catName,
            TypesOfBills.LEISURE_BILLS.catName,
            TypesOfBills.FIX_BILLS.catName,
            TypesOfBills.MONTHLY_BILLS.catName,
        )

        bottomSheetBindingAddBill.spinnerBillType.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item,
            listType
        )


        loadBottomSheetAddBillsComponents(bottomSheetView)
    }

    fun loadBottomSheetAddBillsComponents(view: View) {
        bottomSheetBindingAddBill.saveBillButtom.setOnClickListener {

            val selectedType = bottomSheetBindingAddBill.spinnerBillType.selectedItem.toString()


            val billName = bottomSheetBindingAddBill.editTextInputBillName.text.toString()
            val billPrice = bottomSheetBindingAddBill.editTextInputBillPrice.text.toString()
            val billExpireDate =
                bottomSheetBindingAddBill.editTextInputBillExpireDate.text.toString()
            val billObject =
                BillsModel(null, userId, billPrice, selectedType, billName, billExpireDate)
            viewModel.addBill(billObject)
            bottomSheetDialog.dismiss()
            viewModel.getAllBills(userId)
        }

    }

    private fun LoadViewModelAndsObservers() {
        viewModel.billList.observe(viewLifecycleOwner, observerGetBills)
        viewModel.error.observe(viewLifecycleOwner, observerError)
        viewModel.addResponse.observe(viewLifecycleOwner, observerAddResponse)
        viewModel.user.observe(viewLifecycleOwner, observerUser)
        viewModel.getUserId()
    }


    /* loads bill crud bottom sheet */
    fun loadBottomSheetCrudBill(view: View, bill: BillsModel) {

        bottomSheetView = View.inflate(view.context, R.layout.input_bill_layout, null)
        bottomSheetDialog = BottomSheetDialog(view.context)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
        bottomSheetBindingAddBill = InputBillLayoutBinding.bind(bottomSheetView)

        val listType = listOf<String>(
            TypesOfBills.EMERGENCY_BILL.catName,
            TypesOfBills.LEISURE_BILLS.catName,
            TypesOfBills.FIX_BILLS.catName,
            TypesOfBills.MONTHLY_BILLS.catName,
        )

        bottomSheetBindingAddBill.spinnerBillType.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item,
            listType
        )


        loadBottomSheetCrudBillsComponents(bottomSheetView)
    }

    fun loadBottomSheetCrudBillsComponents(view: View) {
        bottomSheetBindingAddBill.saveBillButtom.setOnClickListener {

            val selectedType = bottomSheetBindingAddBill.spinnerBillType.selectedItem.toString()


            val billName = bottomSheetBindingAddBill.editTextInputBillName.text.toString()
            val billPrice = bottomSheetBindingAddBill.editTextInputBillPrice.text.toString()
            val billExpireDate =
                bottomSheetBindingAddBill.editTextInputBillExpireDate.text.toString()
            val billObject =
                BillsModel(null, userId, billPrice, selectedType, billName, billExpireDate)
            viewModel.addBill(billObject)
            bottomSheetDialog.dismiss()
            viewModel.getAllBills(userId)
        }

    }


    companion object {
        fun newInstance() = BillsFragment()
    }
}