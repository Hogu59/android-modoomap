package com.example.modoomap

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.modoomap.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = Firebase.firestore

        binding.tvSignUpButton.setOnClickListener {
            onSignUp()
        }
    }

    private fun onSignUp() {
        if(!checkFormComplete()) return

        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show()
                    saveInfoToDatabase()
                    navigateToLogin()
                } else {
                    showErrorMessage("회원가입에 실패했습니다.")
                }
            }
    }

    private fun saveInfoToDatabase() {
        val user = hashMapOf(
            "email" to binding.etEmail.text.toString(),
            "birth" to binding.etBirth.text.toString(),
            "contact" to binding.etContact.text.toString()
        )

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "DocumentSnapshot added with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding document", Toast.LENGTH_SHORT).show()
            }

    }

    private fun checkFormComplete(): Boolean {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val checkPassword = binding.etCheckPassword.text.toString()

        if(email.isNullOrEmpty() || password.isNullOrEmpty() || checkPassword.isNullOrEmpty()) {
            showErrorMessage("모든 항목을 입력해주세요.")
            return false
        }

        if(password != checkPassword) {
            showErrorMessage("비밀번호가 일치하지 않습니다.")
            return false
        }

        if(!checkAdditionalInfo()) return false

        return true
    }

    private fun checkAdditionalInfo(): Boolean {
        val birth = binding.etBirth.text.toString()
        val contact = binding.etContact.text.toString()

        if(birth.isNullOrEmpty()) {
            showErrorMessage("생년월일을 입력해주세요.")
            return false
        }
        if(contact.isNullOrEmpty()) {
            showErrorMessage("연락처를 입력해주세요.")
            return false
        }
        return true
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLogin() {
        finish()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }
}
