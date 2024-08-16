package me.yavuz.delta_a_project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.yavuz.delta_a_project.databinding.ReportListItemBinding
import me.yavuz.delta_a_project.model.ReportX
import java.text.SimpleDateFormat
import java.util.Date

class ReportXListAdapter :
    RecyclerView.Adapter<ReportXListAdapter.ReportListViewHolder>() {

    private var list: List<ReportX> = emptyList()
    var onItemClick: ((ReportX) -> Unit)? = null

    inner class ReportListViewHolder(private val binding: ReportListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reportX: ReportX) {
            val formattedTime =
                SimpleDateFormat.getDateTimeInstance().format(Date(reportX.timestamp))
            binding.reportId.text = "${binding.reportId.text}: ${reportX.id}"
            binding.reportTime.text = "${binding.reportTime.text}: \n\t$formattedTime"
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(list[adapterPosition])
            }
        }
    }

    fun setData(updatedData: List<ReportX>) {
        list = updatedData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ReportListItemBinding.inflate(layoutInflater, parent, false)
        return ReportListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ReportListViewHolder, position: Int) {
        holder.bind(list[position])
    }
}