package com.febrian.hackathon_k5.pembeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.febrian.hackathon_k5.MainActivity
import com.febrian.hackathon_k5.databinding.ActivityLoginBinding
import com.febrian.hackathon_k5.databinding.ActivityLoginPembeliBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginPembeliBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPembeliBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerHere.setOnClickListener(){
            val intent =
                Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.loginButton.setOnClickListener() {
            when {
                TextUtils.isEmpty(
                    binding.passwordPassLogin.text.toString().trim() { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Tolong isi password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(
                    binding.usernamePassLogin.text.toString().trim() { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Tolong isi email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val email: String =
                        binding.usernamePassLogin.text.toString().trim() { it <= ' ' }
                    val password: String =
                        binding.passwordPassLogin.text.toString().trim() { it <= ' ' }
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener()
                        { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Anda berhasil masuk.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent =
                                    Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", firebaseUser.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }
}