package me.yavuz.delta_a_project.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import me.yavuz.delta_a_project.adapter.MainItemAdapter
import me.yavuz.delta_a_project.databinding.FragmentMainBinding
import me.yavuz.delta_a_project.model.Product
import java.text.DecimalFormat

class MainFragment : Fragment() {

    private val builder: StringBuilder = StringBuilder()
    private lateinit var decimalFormat: DecimalFormat
    private lateinit var binding: FragmentMainBinding
    private val mainItemAdapter = MainItemAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        declaringViews()
        searchFilterListener()
        buttonClickListeners()
        return binding.root
    }

    private fun declaringViews() {
        decimalFormat = DecimalFormat("#,###.##")
        // test items
        val product1 = Product(1, "test", 1.0, 1, 1, 1, 1)
        val product2 = Product(1, "far", 1.0, 1, 1, 1, 1)
        val product3 = Product(1, "bar", 1.0, 1, 1, 1, 1)
        val itemList = arrayListOf(product1, product2, product3)

        binding.itemRecyclerView.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = mainItemAdapter
        }

        mainItemAdapter.setData(itemList)
    }

    private fun searchFilterListener() {
        binding.mainItemSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mainItemAdapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun buttonClickListeners() {
        binding.button0.setOnClickListener {
            appendToBuilder("0")
        }

        binding.button1.setOnClickListener {
            appendToBuilder("1")
        }

        binding.button2.setOnClickListener {
            appendToBuilder("2")
        }

        binding.button3.setOnClickListener {
            appendToBuilder("3")
        }

        binding.button4.setOnClickListener {
            appendToBuilder("4")
        }

        binding.button5.setOnClickListener {
            appendToBuilder("5")
        }

        binding.button6.setOnClickListener {
            appendToBuilder("6")
        }

        binding.button7.setOnClickListener {
            appendToBuilder("7")
        }

        binding.button8.setOnClickListener {
            appendToBuilder("8")
        }

        binding.button9.setOnClickListener {
            appendToBuilder("9")
        }

        binding.button00.setOnClickListener {
            appendToBuilder("00")
        }

        binding.buttonC.setOnClickListener {
            builder.clear()
            updateTextView()
        }
    }

    private fun appendToBuilder(text: String) {
        builder.append(text)
        updateTextView()
    }

    private fun updateTextView() {
        val value = builder.takeIf { it.isNotEmpty() }.toString().toDoubleOrNull()?.div(100.0)

        if (value != null) {
            binding.showNumbers.text = decimalFormat.format(value)
        } else {
            binding.showNumbers.text = "0.00"
        }
    }
}
