package me.yavuz.delta_a_project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.yavuz.delta_a_project.databinding.FragmentDepartmentListItemBinding
import me.yavuz.delta_a_project.model.Department

class SettingsDepartmentListAdapter :
    ListAdapter<Department, SettingsDepartmentListAdapter.DepartmentListViewHolder>(diffUtil) {

    private var fullItemList: List<Department> = emptyList()
    var onItemClick: ((Department) -> Unit)? = null
    lateinit var onActionListener: OnActionListener

    inner class DepartmentListViewHolder(private val binding: FragmentDepartmentListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(department: Department) {
            binding.departmentText.text = department.name
            binding.departmentDelete.setOnClickListener {
                onActionListener.onDelete(adapterPosition)
            }
            binding.departmentUpdate.setOnClickListener {
                onActionListener.onUpdate(adapterPosition)
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(fullItemList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentDepartmentListItemBinding.inflate(layoutInflater, parent, false)
        return DepartmentListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DepartmentListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(updatedList: List<Department>) {
        fullItemList = updatedList
        submitList(updatedList)
    }

    fun getData(): List<Department> {
        return currentList
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Department>() {
            override fun areItemsTheSame(oldItem: Department, newItem: Department): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Department, newItem: Department): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}