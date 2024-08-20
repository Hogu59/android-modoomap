package com.example.modoomap

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.modoomap.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.tvLogin.setOnClickListener {
            login()
        }

        binding.tvSignUp.setOnClickListener {
            navigateToSignUp()
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    navigateToMain()
                } else {
                    showErrorMessage("로그인에 실패했습니다.")
                }
            }
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun validateForm(): Boolean {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty()) {
            binding.etEmail.error = "이메일을 입력하세요."
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "비밀번호를 입력하세요."
            return false
        }

        return true
    }

    private fun navigateToSignUp() {
        startActivity(SignUpActivity.newIntent(this))
    }

    private fun navigateToMain() {
        startActivity(MainActivity.newIntent(this))
    }
}
