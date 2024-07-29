package me.yavuz.delta_a_project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.yavuz.delta_a_project.databinding.FragmentUserListItemBinding
import me.yavuz.delta_a_project.model.User

class SettingsUserListAdapter :
    ListAdapter<User, SettingsUserListAdapter.UserListViewHolder>(diffUtil), Filterable {

    private var fullItemList: List<User> = emptyList()
    var onItemClick: ((User) -> Unit)? = null
    lateinit var onActionListener: OnActionListener

    inner class UserListViewHolder(private val binding: FragmentUserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.userListName.text = user.name
            binding.userListUserType.text = user.userTypeName
            binding.deleteImage.setOnClickListener {
                onActionListener.onDelete(adapterPosition)
            }

            binding.updateImage.setOnClickListener {
                onActionListener.onUpdate(adapterPosition)
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(fullItemList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentUserListItemBinding.inflate(layoutInflater, parent, false)
        return UserListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(updatedList: List<User>) {
        fullItemList = updatedList
        submitList(updatedList)
    }

    fun getData(): List<User> {
        return fullItemList
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    fullItemList
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    fullItemList.filter {
                        it.name.lowercase().contains(filterPattern) ||
                        it.userTypeName.lowercase().contains(filterPattern)
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                val filteredList = results?.values as? List<User> ?: emptyList()
                submitList(filteredList)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.userTypeName == newItem.userTypeName
            }
        }
    }
}