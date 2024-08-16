package me.yavuz.delta_a_project.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.adapter.ReportAdapter
import me.yavuz.delta_a_project.adapter.ReportXListAdapter
import me.yavuz.delta_a_project.databinding.FragmentSettingsReportXBinding
import me.yavuz.delta_a_project.databinding.ReportDialogBinding
import me.yavuz.delta_a_project.model.ReportItem
import me.yavuz.delta_a_project.model.ReportX
import me.yavuz.delta_a_project.model.SellingProcess
import me.yavuz.delta_a_project.utils.CalculateUtils
import me.yavuz.delta_a_project.viewmodel.MainViewModel
import me.yavuz.delta_a_project.viewmodel.SharedViewModel

class SettingsReportXFragment : Fragment() {

    private lateinit var binding: FragmentSettingsReportXBinding
    private lateinit var alertBinding: ReportDialogBinding
    private lateinit var itemAdapter: ReportAdapter
    private val reportXListAdapter = ReportXListAdapter()
    private val viewModel by viewModels<MainViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsReportXBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeList()

        binding.printReportButton.setOnClickListener {
            onPrintClick()
        }

        reportXListAdapter.onItemClick = {
            onListItemClicked(it)
        }

        binding.reportListRecyclerView.apply {
            adapter = reportXListAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun onListItemClicked(reportX: ReportX) {
        alertBinding = xReportDialogBinding()
        lifecycleScope.launch {
            viewModel.getSellingProcessListByXAndZId(reportX.id, reportX.zId)
                .observe(viewLifecycleOwner) {
                    CoroutineScope(Dispatchers.Main).launch {
                        getTotalSales(it)
                        getPaymentTypes(it)
                        getDepartmentSales(it)
                        getGroupSales(it)
                    }
                }
        }
    }

    private fun observeList() {
        viewModel.getAllReportX().observe(viewLifecycleOwner) {
            reportXListAdapter.setData(it)
        }
    }

    private fun setupRecycler(recyclerView: RecyclerView, list: List<ReportItem>) {
        itemAdapter = ReportAdapter(list)
        recyclerView.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun onPrintClick() {
        alertBinding = xReportDialogBinding()
        val userId = sharedViewModel.data.value
        lifecycleScope.launch {
            val zId = viewModel.getLastZNumber()
                .takeIf { it > 0 } ?: viewModel.insertReportZ()
            val xId = viewModel.getLastXNumber()
                .takeIf { it > 0 } ?: viewModel.insertReportX(zId)
            if (binding.singleUser.isChecked) {
                singleUser(userId, xId, zId)
            } else if (binding.multiUser.isChecked) {
                multipleUser(xId, zId)
            }
        }
    }

    private suspend fun singleUser(userId: Int? = null, xId: Int, zId: Int) {
        withContext(Dispatchers.Main) {
            alertBinding.usersTitle.text = "Single User"
            alertBinding.usersListTextView.text = userId.toString()
            alertBinding.extraInformationTextView.text =
                "X Report ID: $xId"
        }
        showReportX(userId, zId)
    }

    private suspend fun multipleUser(xId: Int, zId: Int) {
        withContext(Dispatchers.Main) {
            alertBinding.usersTitle.text = "Multiple Users"
            observeUserData()
            alertBinding.extraInformationTextView.text =
                "X Report ID: $xId"
        }
        showReportX(zId = zId)
    }

    private fun observeUserData() {
        viewModel.getUsers().observe(viewLifecycleOwner) {
            alertBinding.usersListTextView.text = it.toString()
        }
    }

    private suspend fun showReportX(userId: Int? = null, zId: Int) {
        viewModel.getSellingProcessesByZReportId(zId)
            .observe(viewLifecycleOwner) { list ->
                val filteredList = if (userId != null) {
                    list.filter { it.userId == userId }
                } else {
                    list
                }

                CoroutineScope(Dispatchers.Main).launch {
                    getTotalSales(filteredList)
                    getPaymentTypes(filteredList)
                    getDepartmentSales(filteredList)
                    getGroupSales(filteredList)
                }

                if (filteredList.isNotEmpty()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.insertReportX(zId)
                    }
                }
            }
    }

    private fun xReportDialogBinding(): ReportDialogBinding {
        val alertBinding = inflateReportDialog()
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(alertBinding.root)

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
        return alertBinding
    }

    private fun inflateReportDialog(): ReportDialogBinding {
        val customLayout = layoutInflater.inflate(R.layout.report_dialog, binding.root, false)
        val alertBinding = ReportDialogBinding.bind(customLayout)
        return alertBinding
    }

    private suspend fun getTotalSales(list: List<SellingProcess>) {
        withContext(Dispatchers.Main) {
            val type = "Total Sales"
            val tempReportItem = ReportItem(type, 0, 0.0, 0.0)
            var reportItem: ReportItem = tempReportItem
            list.forEach { reportItem = getReportItemInfo(tempReportItem, it) }
            setupRecycler(alertBinding.totalSalesRecycler, listOf(reportItem))
        }
    }

    private suspend fun getGroupSales(list: List<SellingProcess>) {
        val groupSalesMap = mutableMapOf<String, ReportItem>()

        list.forEach { sellingProcess ->
            val department =
                viewModel.getDepartmentById(
                    viewModel.getProductById(sellingProcess.productId)!!.departmentId
                )

            val groupName = viewModel.getGroupById(department!!.groupId)?.name.toString()

            val reportItem = groupSalesMap.getOrPut(groupName) {
                ReportItem(groupName, 0, 0.0, 0.0)
            }

            getReportItemInfo(reportItem, sellingProcess)
        }

        setupRecycler(alertBinding.groupsRecycler, groupSalesMap.values.toList())
    }

    private suspend fun getDepartmentSales(list: List<SellingProcess>) {
        val departmentSalesMap = mutableMapOf<String, ReportItem>()

        list.forEach { sellingProcess ->
            val departmentName =
                viewModel.getDepartmentById(
                    viewModel.getProductById(sellingProcess.productId)!!.departmentId
                )?.name ?: "Unknown Department"

            val reportItem = departmentSalesMap.getOrPut(departmentName) {
                ReportItem(departmentName, 0, 0.0, 0.0)
            }

            getReportItemInfo(reportItem, sellingProcess)
        }

        setupRecycler(alertBinding.departmentsRecycler, departmentSalesMap.values.toList())
    }

    private suspend fun getPaymentTypes(list: List<SellingProcess>) {
        val reportItemList = mutableListOf<ReportItem>()
        reportItemList.add(ReportItem("Cash", 0, 0.0, 0.0))
        reportItemList.add(ReportItem("Card", 0, 0.0, 0.0))
        reportItemList.add(ReportItem("Other", 0, 0.0, 0.0))

        list.forEach {
            val sellingType = viewModel.getSellingTypeById(it.sellingProcessTypeId)
            when (sellingType?.name) {
                "Cash" -> {
                    reportItemList[0] = getReportItemInfo(reportItemList[0], it)
                }

                "Card" -> {
                    reportItemList[1] = getReportItemInfo(reportItemList[1], it)
                }

                "Other" -> {
                    reportItemList[2] = getReportItemInfo(reportItemList[2], it)
                }
            }
        }

        setupRecycler(alertBinding.paymentFunctionRecycler, reportItemList)
    }

    private fun getReportItemInfo(reportItem: ReportItem, it: SellingProcess): ReportItem {
        reportItem.quantity += it.quantity
        val total = it.quantity * it.priceSell
        if (total > 0)
            reportItem.sale += CalculateUtils.formatDouble(total).toDouble()
        else if (total < 0)
            reportItem.returnSale += CalculateUtils.formatDouble(total).toDouble()

        return reportItem
    }

}