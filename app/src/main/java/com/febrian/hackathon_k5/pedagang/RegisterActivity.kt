package com.febrian.hackathon_k5.pedagang

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.febrian.hackathon_k5.MainActivity
import com.febrian.hackathon_k5.R
import com.febrian.hackathon_k5.databinding.ActivityRegisterBinding
import com.febrian.hackathon_k5.pembeli.RegisterActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.kirimOtpButton.setOnClickListener() {
            when {
                TextUtils.isEmpty(
                    binding.nomorTeleponInput.text.toString().trim() { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Nomor telepon salah.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    startPhoneNumberVerification(binding.nomorTeleponInput.text.toString())
                    if (storedVerificationId == null && savedInstanceState != null) {
                        onRestoreInstanceState(savedInstanceState);
                    }
                }
            }
        }
        binding.kirimUlangButton.setOnClickListener() {
            when {
                TextUtils.isEmpty(
                    binding.nomorTeleponInput.text.toString().trim() { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Nomor telepon salah.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    resendVerificationCode(binding.nomorTeleponInput.text.toString(), resendToken)
                }
            }
        }
            binding.periksaButton.setOnClickListener() {
                when {
                    TextUtils.isEmpty(binding.otpInput.text.toString().trim() { it <= ' ' }) -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            "OTP salah.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        verifyPhoneNumberWithCode(
                            storedVerificationId,
                            binding.otpInput.text.toString()
                        )
                    }
                }
                }
                callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // This callback will be invoked in two situations:
                        // 1 - Instant verification. In some cases the phone number can be instantly
                        //     verified without needing to send or enter a verification code.
                        // 2 - Auto-retrieval. On some devices Google Play services can automatically
                        //     detect the incoming verification SMS and perform verification without
                        //     user action.
                        Log.d(TAG, "onVerificationCompleted:$credential")
                        signInWithPhoneAuthCredential(credential)
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.
                        Log.w(TAG, "onVerificationFailed", e)

                        if (e is FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                        } else if (e is FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                        }

                        // Show a message and update the UI
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
                        Log.d(TAG, "onCodeSent:$verificationId")

                        // Save verification ID and resending token so we can use them later
                        storedVerificationId = verificationId
                        resendToken = token
                    }
                }
                // [END phone_auth_callbacks]
            }

            // [START on_start_check_user]
            override fun onStart() {
                super.onStart()
                // Check if user is signed in (non-null) and update UI accordingly.
                val currentUser = auth.currentUser
                updateUI(currentUser)
            }
            // [END on_start_check_user]

            private fun startPhoneNumberVerification(phoneNumber: String) {
                // [START start_phone_auth]
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber)       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this)                 // Activity (for callback binding)
                    .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
                // [END start_phone_auth]
            }

            private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
                // [START verify_with_code]
                val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
                signInWithPhoneAuthCredential(credential)
            // [END verify_with_code]
            }

            // [START resend_verification]
            private fun resendVerificationCode(
                phoneNumber: String,
                token: PhoneAuthProvider.ForceResendingToken?
            ) {
                val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber)       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this)                 // Activity (for callback binding)
                    .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                if (token != null) {
                    optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
                }
                PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
            }
            // [END resend_verification]

            // [START sign_in_with_phone]
            private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success")

                            val user = task.result?.user
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.exception)
                            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                            // Update UI
                        }
                    }
            }
            // [END sign_in_with_phone]

            private fun updateUI(user: FirebaseUser? = auth.currentUser) {

            }

            companion object {
            private const val TAG = "PhoneAuthActivity"
        }
}
