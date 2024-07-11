package me.yavuz.delta_a_project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.yavuz.delta_a_project.databinding.FragmentProductListItemBinding
import me.yavuz.delta_a_project.model.Product

class SettingsProductListAdapter :
    ListAdapter<Product, SettingsProductListAdapter.ProductListViewHolder>(diffUtil), Filterable {

    private var fullItemList: List<Product> = emptyList()
    var onItemClick: ((Product) -> Unit)? = null
    lateinit var onActionListener: OnActionListener

    inner class ProductListViewHolder(private val binding: FragmentProductListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.productText.text = product.name
            binding.productDelete.setOnClickListener {
                onActionListener.onDelete(adapterPosition)
            }

            binding.productUpdate.setOnClickListener {
                onActionListener.onUpdate(adapterPosition)
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(fullItemList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentProductListItemBinding.inflate(layoutInflater, parent, false)
        return ProductListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(updatedList: List<Product>) {
        fullItemList = updatedList
        submitList(updatedList)
    }

    fun getData(): List<Product> {
        return currentList
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    fullItemList
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    fullItemList.filter {
                        it.name.lowercase().contains(filterPattern)
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                val filteredList = results?.values as? List<Product> ?: emptyList()
                submitList(filteredList)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

}

interface OnActionListener {
    fun onDelete(position: Int)
    fun onUpdate(position: Int)
}