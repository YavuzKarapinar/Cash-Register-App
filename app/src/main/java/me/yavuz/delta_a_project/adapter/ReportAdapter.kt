package me.yavuz.delta_a_project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.yavuz.delta_a_project.databinding.ReportDialogRecyclerItemBinding
import me.yavuz.delta_a_project.model.ReportItem

class ReportAdapter(val list: List<ReportItem>) :
    RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    inner class ReportViewHolder(val binding: ReportDialogRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ReportDialogRecyclerItemBinding.inflate(layoutInflater, parent, false)
        return ReportViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val item = list[position]
        holder.binding.type.text = item.type
        holder.binding.quantity.text = item.quantity.toString()
        holder.binding.sale.text = item.sale.toString()
        holder.binding.returnSale.text = item.returnSale.toString()
    }

}