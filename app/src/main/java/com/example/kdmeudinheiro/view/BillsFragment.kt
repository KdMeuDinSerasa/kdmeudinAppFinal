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
import com.example.kdmeudinheiro.bottomSheet.BottomSheetBills
import com.example.kdmeudinheiro.bottomSheet.BottomSheetTips
import com.example.kdmeudinheiro.databinding.BillsFragmentBinding
import com.example.kdmeudinheiro.enums.KeysShared
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.utils.DialogToFilter
import com.example.kdmeudinheiro.utils.feedback
import com.example.kdmeudinheiro.viewModel.BillsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class BillsFragment : Fragment(R.layout.bills_fragment) {
    private lateinit var viewModel: BillsViewModel
    private lateinit var binding: BillsFragmentBinding
    private lateinit var recyclerView: RecyclerView

    private lateinit var userId: String
    private var adapter = AdapterBillsList() { bill -> handlerSelectBill(bill) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(BillsViewModel::class.java)
        loadViewModelObservers()
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
        binding.floatButtonAddBill.setOnClickListener { loadBottomSheetBills() }
        binding.About.setOnClickListener { loadBottomSheetTips() }
    }

    private fun loadViewModelObservers() {
        viewModel.billList.observe(viewLifecycleOwner, { handleBillList(it) })

        viewModel.copyBillList.observe(viewLifecycleOwner, { refreshAdapterBillList(it) })

        viewModel.error.observe(viewLifecycleOwner, {
            if (it != null) {
                showFeedBack(
                    type = false,
                    successString = R.string.error_to_call_bills,
                    failureString = R.string.error_to_call_bills
                )
            }
        })
        viewModel.addResponse.observe(viewLifecycleOwner, {
            showFeedBack(
                type = it,
                successString = R.string.bill_add_success,
                failureString = R.string.bill_add_failure
            )
        })
        viewModel.editResponse.observe(viewLifecycleOwner, {
            showFeedBack(
                type = it,
                successString = R.string.bill_edit_success,
                failureString = R.string.bill_edit_failure
            )
        })
        viewModel.deleteResponse.observe(viewLifecycleOwner, {
            showFeedBack(
                type = it,
                successString = R.string.bill_delete_success,
                failureString = R.string.bill_delete_failure
            )
        })

    }

    private fun handlerSelectBill(bill: BillsModel) {
        BottomSheetBills(requireView(), bill).loadBottomBill() { billFromCb, type ->
            handlerForDialogResponse(billFromCb, type)
        }
    }

    private fun handlerForDialogResponse(billFromCb: BillsModel, type: Int?) {

        if (type == 1) {
            viewModel.editBill(billFromCb)
            viewModel.getAllBills(userId)

        } else if (type == 2) {
            viewModel.deleteBill(billFromCb)
            viewModel.getAllBills(userId)

        } else if (type == 3) {
            feedback(requireView(), R.string.validation_registration_failure, R.color.failure)

        } else if (type == 4) {
            viewModel.editBill(billFromCb)
            viewModel.getAllBills(userId)
            feedback(requireView(), R.string.bill_paid_success, R.color.success)

        } else viewModel.getAllBills(userId)
    }

    private fun loadBottomSheetBills() {
        BottomSheetBills(requireView(), null).loadBottomBill() { bill, _ ->
            bill.id_user = userId
            viewModel.addBill(bill)
            viewModel.getAllBills(userId)
        }
    }

    private fun loadBottomSheetTips() {
        BottomSheetTips(requireView()).loadTip()
    }

    private fun handleBillList(billList: List<BillsModel>) {
        if (billList.isEmpty()) {
            loadEmptyBillListTips()
        } else {
            refreshAdapterBillList(billList)
            loadBillListUIState()
        }
    }

    private fun loadEmptyBillListTips() {
        binding.ivArrowDown.visibility = View.VISIBLE
        binding.tvNoBills.visibility = View.VISIBLE
        binding.tvAddBillHint.visibility = View.VISIBLE
        binding.progressAnimation.visibility = View.GONE
        binding.recyclerViewIdNoXML.visibility = View.GONE
    }

    private fun loadBillListUIState() {
        binding.recyclerViewIdNoXML.visibility = View.VISIBLE
        binding.progressAnimation.visibility = View.GONE
        binding.ivArrowDown.visibility = View.GONE
        binding.tvAddBillHint.visibility = View.GONE
        binding.tvNoBills.visibility = View.GONE
    }

    private fun refreshAdapterBillList(newBillList: List<BillsModel>) {
        adapter.refresh(newBillList.toMutableList())
    }

    private fun showFeedBack(type: Boolean, successString: Int, failureString: Int) {
        if (type) feedback(requireView(), successString, R.color.success)
        else feedback(requireView(), failureString, R.color.success)
    }

    private fun checkUser() {
        val mSharedPreferences =
            requireActivity().getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)
        userId = mSharedPreferences.getString(KeysShared.USERID.key, "").toString()
        if (userId.isNullOrBlank()) {
          startLogin()
        }
        viewModel.getAllBills(userId)
    }

    private fun startLogin(){
        startActivity(Intent(requireActivity(), LoginActivity::class.java))
    }

    //TODO NEEDS FURTHER ANALYSIS TO REFACTOR THIS FUNCTIONS
    private fun searchBill() {
        binding.textFieldSearch.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0.let {
                    if (it?.length!! >= 1)
                        viewModel.filterBill(it.toString())
                    else
                        feedback(requireView(), R.string.error_search_bill, R.color.failure)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun filterByValidate() {
        val date = Calendar.getInstance()

        binding.buttonFilter.setOnClickListener {
            DialogToFilter.dialogToFilter(requireContext()) { map ->

                val hashToList = map?.map {
                    it.key
                }
                val getListPosition = hashToList?.get(0)

                if (getListPosition == 3)
                    viewModel.filterBill(it.toString())

                viewModel.filterPay(date.time, getListPosition!!)
            }
        }
    }
}