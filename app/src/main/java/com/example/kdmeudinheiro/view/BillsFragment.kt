package com.example.kdmeudinheiro.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kdmeudinheiro.viewModel.BillsViewModel
import com.example.kdmeudinheiro.R

class BillsFragment : Fragment() {

    companion object {
        fun newInstance() = BillsFragment()
    }

    private lateinit var viewModel: BillsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.accounts_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BillsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}