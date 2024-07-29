package me.yavuz.delta_a_project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.yavuz.delta_a_project.databinding.FragmentGroupListItemBinding
import me.yavuz.delta_a_project.model.Group

class SettingsGroupListAdapter :
    ListAdapter<Group, SettingsGroupListAdapter.GroupListViewHolder>(diffUtil) {

    private var fullItemList: List<Group> = emptyList()
    var onItemClick: ((Group) -> Unit)? = null
    lateinit var onActionListener: OnActionListener

    inner class GroupListViewHolder(private val binding: FragmentGroupListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(group: Group) {
            binding.groupText.text = group.name
            binding.groupDelete.setOnClickListener {
                onActionListener.onDelete(adapterPosition)
            }
            binding.groupUpdate.setOnClickListener {
                onActionListener.onUpdate(adapterPosition)
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(fullItemList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentGroupListItemBinding.inflate(layoutInflater, parent, false)
        return GroupListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(updatedList: List<Group>) {
        fullItemList = updatedList
        submitList(updatedList)
    }

    fun getData(): List<Group> {
        return currentList
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Group>() {
            override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

}