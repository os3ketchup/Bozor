package uz.os3ketchup.bozor.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import uz.os3ketchup.bozor.CONSTANTS.PHONE_KEY
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.databinding.FragmentOTPBinding
import uz.os3ketchup.bozor.databinding.FragmentRegisterBinding
import java.util.concurrent.TimeUnit

class OTPFragment : Fragment() {
    lateinit var binding: FragmentOTPBinding
    private lateinit var phoneNumber: String
    lateinit var mAuth: FirebaseAuth
    lateinit var mVerificationId: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOTPBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        phoneNumber = "+998" + arguments?.getString(PHONE_KEY)!!
        mAuth = Firebase.auth
        sendCode()

        with(binding) {
            btnSend.setOnClickListener {
                val etCode = etCode.text.toString()
                if (etCode.isNotEmpty()) {
                    val credential = PhoneAuthProvider.getCredential(mVerificationId, etCode)
                    signInWithPhoneAuthCredential(credential)
                }
            }

        }
    }

    private fun sendCode() {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential).addOnSuccessListener {
        }
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    /*  binding.progress.visibility = View.INVISIBLE*/
                    // Sign in success, update UI with the signed-in user's information
                    findNavController().navigate(R.id.registerFragment)
                    Toast.makeText(requireContext(), "  ${credential.smsCode}", Toast.LENGTH_SHORT)
                        .show()
                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.

            signInWithPhoneAuthCredential(credential)

        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

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

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId
            /* resendToken = token*/
        }
    }
}