package me.yavuz.delta_a_project.settings

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.databinding.FragmentSettingsUserAddBinding

class SettingsUserAddFragment : Fragment() {

    private lateinit var binding: FragmentSettingsUserAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsUserAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbHelper = DbHelper.getInstance(binding.root.context)
        val userTypes = dbHelper.getUserTypes().map { it.name }
        val adapter =
            ArrayAdapter(binding.root.context, R.layout.spinner_item, userTypes)
        binding.spinner.adapter = adapter

        binding.button.setOnClickListener {
            userSave(dbHelper)
        }
    }

    private fun userSave(dbHelper: DbHelper) {
        val position = binding.spinner.selectedItemPosition
        val userType = binding.spinner.getItemAtPosition(position) as String
        val name = binding.userSaveName.text.toString()
        val password = binding.saveUserPassword.text.toString()
        val passwordCorrection = binding.saveUserPasswordCorrection.text.toString()

        if(TextUtils.isEmpty(name) ||
            TextUtils.isEmpty(password) ||
            TextUtils.isEmpty(passwordCorrection)) {
            Toast.makeText(
                binding.root.context,
                "Please fill all fields!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (password == passwordCorrection) {
            dbHelper.saveUser(userType, name, password)
            Toast.makeText(
                binding.root.context,
                "User saved!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                binding.root.context,
                "Passwords must be same!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}