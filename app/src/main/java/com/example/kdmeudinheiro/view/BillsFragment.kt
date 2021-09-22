package com.example.kdmeudinheiro.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
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
import com.example.kdmeudinheiro.utils.DialogToFilter
import com.example.kdmeudinheiro.utils.feedback
import com.example.kdmeudinheiro.viewModel.BillsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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
        searchBill()
        filterByValidate()

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
        viewModel.billList.observe(viewLifecycleOwner, {
            if (it.size == 0) {
                binding.ivArrowDown.visibility = View.VISIBLE
                binding.tvNoBills.visibility = View.VISIBLE
                binding.tvAddBillHint.visibility = View.VISIBLE
                binding.progressAnimation.visibility = View.GONE
                binding.recyclerViewIdNoXML.visibility = View.GONE
            } else {
                adapter.refresh(it.toMutableList())
                binding.recyclerViewIdNoXML.visibility = View.VISIBLE
                binding.progressAnimation.visibility = View.GONE
                binding.ivArrowDown.visibility = View.GONE
                binding.tvAddBillHint.visibility = View.GONE
                binding.tvNoBills.visibility = View.GONE
            }
        })

        viewModel.billList.observe(viewLifecycleOwner, { adapter.refresh(it.toMutableList()) })
        viewModel.copyBillList.observe(viewLifecycleOwner, { adapter.refresh(it.toMutableList()) })
        viewModel.error.observe(viewLifecycleOwner, {
            if (it != null) {
                feedback(requireView(), R.string.error_to_call_bills, R.color.failure)
            }
        })
        viewModel.addResponse.observe(viewLifecycleOwner, {
            if (it == true) {
                feedback(requireView(), R.string.bill_add_success, R.color.success)
            } else {
                feedback(requireView(), R.string.bill_add_failure, R.color.failure)
            }
        })
        viewModel.editResponse.observe(viewLifecycleOwner, {
            if (it == true) {
                feedback(requireView(), R.string.bill_edit_success, R.color.success)
            } else {
                feedback(requireView(), R.string.bill_edit_success, R.color.success)
            }
        })
        viewModel.deleteResponse.observe(viewLifecycleOwner, {
            if (it == true) {
                feedback(requireView(), R.string.bill_delete_success, R.color.success)
            } else {
                feedback(requireView(), R.string.bill_delete_failure, R.color.failure)
            }
        })

    }

    private fun handlerForDialogResponse(billFromCb: BillsModel, type: Int?) {

        if (type == 1) {
            viewModel.editBill(billFromCb)
            viewModel.getAllBills(userId)

        } else if (type == 2) {
            viewModel.deleteBill(billFromCb)
            viewModel.getAllBills(userId)

        } else if (type == 3) {


        } else if (type == 4) {
            viewModel.editBill(billFromCb)
            viewModel.getAllBills(userId)
            feedback(requireView(), R.string.bill_paid_success, R.color.success)

        } else viewModel.getAllBills(userId)
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

    private fun searchBill() {
        binding.textFieldSearch.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0.let {
                    if (it?.length!! >= 1)
                        viewModel.filterBill(it.toString())
                    if (it.isEmpty())
                        viewModel.filterBill(it.toString())
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun filterByValidate(){
        val date = Calendar.getInstance()

        binding.buttonFilter.setOnClickListener {
            DialogToFilter.dialogToFilter(requireContext()) { map ->

                val hashToList = map?.map {
                    it.key
                }
                val getListPosition = hashToList?.get(0)

                if (getListPosition == 3)
                    viewModel.filterBill("")

                viewModel.filterPay(date.time, getListPosition!!)
            }
        }
    }
}