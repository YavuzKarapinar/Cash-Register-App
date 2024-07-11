package me.yavuz.delta_a_project.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.adapter.OnActionListener
import me.yavuz.delta_a_project.adapter.SettingsProductListAdapter
import me.yavuz.delta_a_project.databinding.FragmentSettingsListProductBinding
import me.yavuz.delta_a_project.viewmodel.MainViewModel

class SettingsProductListFragment : Fragment() {

    private lateinit var binding: FragmentSettingsListProductBinding
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var productListAdapter: SettingsProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsListProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeProducts()
    }

    private fun setupRecyclerView() {
        productListAdapter = SettingsProductListAdapter().apply {
            onActionListener = object : OnActionListener {
                override fun onDelete(position: Int) {
                    onDeleteClicked(position)
                }

                override fun onUpdate(position: Int) {
                    onUpdateClicked(position)
                }
            }
        }
        binding.productListRecyclerView.apply {
            adapter = productListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeProducts() {
        viewModel.getProducts().observe(viewLifecycleOwner) { products ->
            productListAdapter.setData(products)
        }
    }

    private fun onDeleteClicked(position: Int) {
        val products = productListAdapter.getData()
        if (position in products.indices) {
            viewModel.deleteProduct(products[position])
            observeProducts()
            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Product not found!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onUpdateClicked(position: Int) {
        val products = productListAdapter.getData()
        if (position in products.indices) {
            val fragment = SettingsProductAddFragment()
            val bundle = Bundle().apply {
                putInt("productId", products[position].id)
            }
            fragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.settingsFragmentContainer, fragment, "productId")
                .commit()
        } else {
            Toast.makeText(context, "Product not found!", Toast.LENGTH_SHORT).show()
        }
    }
}
