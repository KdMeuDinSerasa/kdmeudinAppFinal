package com.example.kdmeudinheiro.view

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import com.example.kdmeudinheiro.viewModel.BillsViewModel
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.BillsFragmentBinding

class BillsFragment : Fragment(R.layout.bills_fragment) {
    private lateinit var viewModel: BillsViewModel
    private lateinit var binding: BillsFragmentBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(BillsViewModel::class.java)
    }

    companion object {
        fun newInstance() = BillsFragment()
    }
}