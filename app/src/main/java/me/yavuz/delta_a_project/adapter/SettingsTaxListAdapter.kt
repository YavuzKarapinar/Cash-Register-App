package me.yavuz.delta_a_project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.yavuz.delta_a_project.databinding.FragmentTaxListItemBinding
import me.yavuz.delta_a_project.model.Tax

class SettingsTaxListAdapter :
    ListAdapter<Tax, SettingsTaxListAdapter.TaxListViewHolder>(diffUtil) {

    private var fullItemList: List<Tax> = emptyList()
    var onItemClick: ((Tax) -> Unit)? = null
    lateinit var onActionListener: OnActionListener

    inner class TaxListViewHolder(private val binding: FragmentTaxListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tax: Tax) {
            binding.taxText.text = tax.name
            binding.taxDelete.setOnClickListener {
                onActionListener.onDelete(adapterPosition)
            }
            binding.taxUpdate.setOnClickListener {
                onActionListener.onUpdate(adapterPosition)
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(fullItemList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaxListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentTaxListItemBinding.inflate(layoutInflater, parent, false)
        return TaxListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaxListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(updatedList: List<Tax>) {
        fullItemList = updatedList
        submitList(updatedList)
    }

    fun getData(): List<Tax> {
        return currentList
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Tax>() {
            override fun areItemsTheSame(oldItem: Tax, newItem: Tax): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Tax, newItem: Tax): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}