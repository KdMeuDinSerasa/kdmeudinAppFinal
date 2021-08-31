package com.example.kdmeudinheiro.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kdmeudinheiro.model.BillsModel

class AdapterBillsList(val onItemClick: (BillsModel) -> Unit): RecyclerView.Adapter<BillsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillsViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: BillsViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}
class BillsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

}