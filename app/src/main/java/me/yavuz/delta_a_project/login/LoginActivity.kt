package me.yavuz.delta_a_project.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import me.yavuz.delta_a_project.main.MainActivity
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DbHelper(this)

        binding.loginButton.setOnClickListener {
            retrieveUserData()
        }

        dbHelper.close()
    }

    private fun retrieveUserData() {
        if (!TextUtils.isEmpty(binding.loginName.text) &&
            !TextUtils.isEmpty(binding.loginPassword.text)) {

            val userId = dbHelper.checkUserExistence(
                binding.loginName.text.toString(),
                binding.loginPassword.text.toString()
            )
            if (userId != -1) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@LoginActivity, "Wrong name or password", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }
}