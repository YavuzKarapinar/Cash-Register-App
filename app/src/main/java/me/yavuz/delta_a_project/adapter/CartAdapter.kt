package me.yavuz.delta_a_project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.yavuz.delta_a_project.databinding.FragmentMainCartItemBinding
import me.yavuz.delta_a_project.model.Product

class CartAdapter(val cartList: MutableList<Pair<Product, Int>>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: FragmentMainCartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: Pair<Product, Int>) {
            val product = cartItem.first
            val quantity = cartItem.second
            binding.cartItemQuantity.text = quantity.toString()
            binding.cartItemName.text = product.name
            binding.cartItemPrice.text = (product.price * quantity).toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentMainCartItemBinding.inflate(layoutInflater, parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartList[position])
    }

    fun updateItem(product: Product) {
        val index = cartList.indexOfFirst { it.first.id == product.id }

        if (index == -1) {
            cartList.add(product to 1)
            notifyItemInserted(cartList.size - 1)
            return
        }
        val currentPair = cartList[index]

        if (currentPair.second >= 0) {
            changeCartList(index, currentPair, currentPair.second + 1)
            return
        }

        changeCartList(index, currentPair, currentPair.second - 1)
    }

    private fun changeCartList(
        index: Int,
        currentPair: Pair<Product, Int>,
        quantity: Int
    ) {
        cartList[index] = currentPair.copy(second = quantity)
        notifyItemChanged(index)
    }
}