package me.yavuz.delta_a_project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.yavuz.delta_a_project.databinding.ReceiptRecyclerItemBinding
import me.yavuz.delta_a_project.model.Product
import me.yavuz.delta_a_project.utils.CalculateUtils

class ReceiptItemAdapter(private val cartList: MutableList<Pair<Product, Int>>) :
    RecyclerView.Adapter<ReceiptItemAdapter.ReceiptItemViewHolder>() {

    inner class ReceiptItemViewHolder(private val binding: ReceiptRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: Pair<Product, Int>) {
            val product = cartItem.first
            val quantity = cartItem.second
            binding.itemQuantity.text = quantity.toString()
            binding.itemName.text = product.name
            binding.itemPrice.text = CalculateUtils.formatDouble(product.price * quantity)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ReceiptRecyclerItemBinding.inflate(layoutInflater, parent, false)
        return ReceiptItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: ReceiptItemViewHolder, position: Int) {
        holder.bind(cartList[position])
    }

}