package com.example.kdmeudinheiro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.BillItemListBinding
import com.example.kdmeudinheiro.model.BillsModel

class AdapterBillsList(val onItemClick: (BillsModel) -> Unit): RecyclerView.Adapter<BillsViewHolder>() {
        private var listOfBills = mutableListOf<BillsModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillsViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.bill_item_list, parent, false).apply {
            return BillsViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: BillsViewHolder, position: Int) {
        listOfBills[position].apply{
            holder.bind(this)
            holder.itemView.setOnClickListener { onItemClick(this) }
        }
    }
    fun refresh(newList: MutableList<BillsModel>){
        listOfBills.clear()
        listOfBills.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listOfBills.size
}
class BillsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var binding = BillItemListBinding.bind(itemView)

    fun bind(bill: BillsModel){
        binding.textViewBillNameList.text = "${itemView.context.getString(R.string.Bill_Name_label_list_card)} ${bill.name_bill}"
        binding.textViewBillExpireDate.text = "${itemView.context.getString(R.string.Bill_ExpireDate_label_list_card)} ${bill.expire_date}"
        binding.textViewBillPriceList.text = "${itemView.context.getString(R.string.Bill_Price_label_list_card)} ${bill.price.toString()}"
        if (bill.status == 1){
            binding.tvStatus.visibility = View.VISIBLE
        }
    }
}