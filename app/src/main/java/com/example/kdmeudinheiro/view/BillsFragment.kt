package com.example.kdmeudinheiro.view

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.kdmeudinheiro.viewModel.BillsViewModel
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.BillsFragmentBinding
import com.example.kdmeudinheiro.model.BillsModel
import com.google.firebase.auth.FirebaseUser

class BillsFragment : Fragment(R.layout.bills_fragment) {
    //var goes here
    private lateinit var viewModel: BillsViewModel
    private lateinit var binding: BillsFragmentBinding
    //observers gore here
    private var observerGetBills = Observer<List<BillsModel>> {}
    private var observerError = Observer<String> {}
    private var observerAddResponse = Observer<Boolean> {}
    private var observerUser = Observer<FirebaseUser> { user ->
        viewModel.getAllBills(user.uid)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(BillsViewModel::class.java)
        LoadViewModelAndsObservers()
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