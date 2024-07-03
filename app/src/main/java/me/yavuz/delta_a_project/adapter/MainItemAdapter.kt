package me.yavuz.delta_a_project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.yavuz.delta_a_project.databinding.FragmentMainItemBinding
import me.yavuz.delta_a_project.model.Product

class MainItemAdapter : ListAdapter<Product, MainItemAdapter.MainItemViewHolder>(diffUtil), Filterable {

    private var fullItemList: List<Product> = emptyList()
    var onItemClick: ((Product) -> Unit)? = null

    inner class MainItemViewHolder(private val binding: FragmentMainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.itemName.text = product.name
            binding.itemQuantity.text = product.quantity.toString()
            binding.itemPrice.text = product.price.toString()
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(fullItemList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentMainItemBinding.inflate(layoutInflater, parent, false)
        return MainItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(updatedList: List<Product>) {
        fullItemList = updatedList
        submitList(updatedList)
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
                return oldItem.name == newItem.name &&
                        oldItem.price == newItem.price &&
                        oldItem.quantity == newItem.quantity
            }
        }
    }
}
