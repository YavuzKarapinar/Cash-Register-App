package me.yavuz.delta_a_project.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import me.yavuz.delta_a_project.databinding.FragmentSettingsChildItemBinding
import me.yavuz.delta_a_project.databinding.FragmentSettingsTitleItemBinding

class SettingsExpandableListAdapter(
    private val context: Context,
    private val headerList: List<String>,
    private val childMap: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return headerList.size
    }

    override fun getChildrenCount(p0: Int): Int {
        return childMap[headerList[p0]]?.size ?: 0
    }

    override fun getGroup(p0: Int): Any {
        return headerList[p0]
    }

    override fun getChild(p0: Int, p1: Int): Any {
        return childMap[headerList[p0]]?.get(p1) ?: ""
    }

    override fun getGroupId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getChildId(p0: Int, p1: Int): Long {
        return p1.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPos: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val title = getGroup(groupPos).toString()
        val groupBinding: FragmentSettingsTitleItemBinding

        if (convertView == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            groupBinding = FragmentSettingsTitleItemBinding.inflate(layoutInflater)
        } else {
            groupBinding = FragmentSettingsTitleItemBinding.bind(convertView)
        }

        groupBinding.settingsTitleTextView.text = title

        return groupBinding.root
    }

    override fun getChildView(
        groupPos: Int,
        childPos: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val child = getChild(groupPos, childPos).toString()
        val childBinding: FragmentSettingsChildItemBinding

        if (convertView == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            childBinding = FragmentSettingsChildItemBinding.inflate(layoutInflater)
        } else {
            childBinding = FragmentSettingsChildItemBinding.bind(convertView)
        }

        childBinding.settingsChildTextView.text = child

        return childBinding.root
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }
}