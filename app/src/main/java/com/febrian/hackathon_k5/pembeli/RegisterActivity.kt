package com.febrian.hackathon_k5.pembeli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.febrian.hackathon_k5.databinding.ActivityRegisterPembeliBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterPembeliBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPembeliBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginHere.setOnClickListener(){
            val intent =
                Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.registerButton.setOnClickListener(){
            when{
                TextUtils.isEmpty(binding.passwordPass.text.toString().trim(){ it <= ' '})->{
                    Toast.makeText(
                        this@RegisterActivity,
                        "Tolong isi password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(binding.usernamePass.text.toString().trim(){ it <= ' '})->{
                    Toast.makeText(
                        this@RegisterActivity,
                        "Tolong isi email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(binding.phonePass.text.toString().trim(){ it <= ' '})->{
                    Toast.makeText(
                        this@RegisterActivity,
                        "Tolong isi nomor telepon.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email: String = binding.usernamePass.text.toString().trim(){it <= ' '}
                    val password: String = binding.passwordPass.text.toString().trim(){it <= ' '}

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            { task ->
                                if(task.isSuccessful){
                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Anda berhasil terdaftar.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent =
                                        Intent(this@RegisterActivity, LoginActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", firebaseUser.uid)
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    finish()
                                }
                                else {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                }
            }

        }

    }
}