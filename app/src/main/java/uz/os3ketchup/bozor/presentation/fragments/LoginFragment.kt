package uz.os3ketchup.bozor.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.databinding.FragmentLoginBinding
import uz.os3ketchup.bozor.CONSTANTS.PHONE_KEY

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnLogin.setOnClickListener {
                if (etPhoneNumber.text.isNotEmpty() && etPhoneNumber.text.toString().length <= 12) {
                    findNavController().navigate(
                        R.id.OTPFragment,
                        bundleOf(PHONE_KEY to etPhoneNumber.text.toString())
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please enter your number correct order",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }

    }
}