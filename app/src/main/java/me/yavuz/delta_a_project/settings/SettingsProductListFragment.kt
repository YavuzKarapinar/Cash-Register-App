package me.yavuz.delta_a_project.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.adapter.OnActionListener
import me.yavuz.delta_a_project.adapter.SettingsProductListAdapter
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.databinding.FragmentSettingsListProductBinding

class SettingsProductListFragment : Fragment() {

    private lateinit var binding: FragmentSettingsListProductBinding
    private lateinit var dbHelper: DbHelper
    private lateinit var productListAdapter: SettingsProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsListProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productListAdapter = SettingsProductListAdapter()
        binding.productListRecyclerView.apply {
            adapter = productListAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }

        dbHelper = DbHelper.getInstance(binding.root.context)

        val products = dbHelper.getProducts()
        productListAdapter.setData(products)

        productListAdapter.onActionListener = object : OnActionListener {
            override fun onDelete(position: Int) {
                dbHelper.deleteProduct(products[position])
                Toast.makeText(
                    binding.root.context,
                    "Deleted!",
                    Toast.LENGTH_SHORT
                ).show()
                productListAdapter.setData(dbHelper.getProducts())
            }

            override fun onUpdate(position: Int) {
                val fragment = SettingsProductAddFragment()
                val bundle = Bundle()
                bundle.putInt("productId", products[position].id)
                fragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.settingsFragmentContainer, fragment, "productId")
                    .commit()
            }
        }
    }

}