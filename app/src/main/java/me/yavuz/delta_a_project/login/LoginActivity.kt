package me.yavuz.delta_a_project.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.databinding.ActivityLoginBinding
import me.yavuz.delta_a_project.main.MainActivity
import me.yavuz.delta_a_project.viewmodel.MainViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<MainViewModel>()

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

        binding.loginButton.setOnClickListener {
            retrieveUserData()
        }

    }

    private fun retrieveUserData() {
        if (!TextUtils.isEmpty(binding.loginName.text) &&
            !TextUtils.isEmpty(binding.loginPassword.text)) {

            val user = viewModel.getUserByNameAndPassword(
                binding.loginName.text.toString(),
                binding.loginPassword.text.toString()
            )

            user.observe(this) {
                if (it != null) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("userId", it.id)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Username or password wrong!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}