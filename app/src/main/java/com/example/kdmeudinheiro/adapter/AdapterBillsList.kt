package com.example.kdmeudinheiro.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.BillItemListBinding
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.utils.adjustYear
import com.example.kdmeudinheiro.utils.formatCurrency


class AdapterBillsList(private val onItemClick: (BillsModel) -> Unit) :
    RecyclerView.Adapter<BillsViewHolder>() {
    private var listOfBills = mutableListOf<BillsModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillsViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.bill_item_list, parent, false).apply {
            return BillsViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: BillsViewHolder, position: Int) {
        listOfBills[position].apply {
            holder.bind(this)
            holder.itemView.setOnClickListener { onItemClick(this) }
        }
    }

    fun refresh(newList: MutableList<BillsModel>) {
        listOfBills.clear()
        listOfBills.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listOfBills.size
}

class BillsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var binding = BillItemListBinding.bind(itemView)

    @SuppressLint("SetTextI18n")
    fun bind(bill: BillsModel) {
        binding.textViewBillNameList.text =
            "${itemView.context.getString(R.string.Bill_Name_label_list_card)} ${bill.name_bill}"
        binding.textViewBillExpireDate.text =
            "${itemView.context.getString(R.string.Bill_ExpireDate_label_list_card)} ${
                bill.expire_date.toString().substring(8, 10)
            }/0${bill.expire_date.month + 1}/${bill.expire_date.adjustYear()}"
        var teste = bill.price.toDouble()
        binding.textViewBillPriceList.text =
            "${itemView.context.getString(R.string.Bill_Price_label_list_card)} ${teste.formatCurrency()}"
        if (bill.status == 1) {
            binding.tvStatus.visibility = View.VISIBLE
        }
        if (bill.status == 0) {
            binding.tvStatus.visibility = View.GONE
        }
    }
}