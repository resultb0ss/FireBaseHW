package com.example.firebasehw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebasehw.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.logInButtonBTN.setOnClickListener {
            login()
        }

        binding.redirectSignUpTV.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }

    }

    private fun login() {
        val email = binding.emailET.text.toString()
        val pass = binding.passwordET.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(requireActivity()) {
                if (it.isSuccessful) {
                    myToast("Успешный вход в систему")
                    findNavController().navigate(R.id.baseFragment)
                } else {
                    myToast("Не удалось войти в систему")
                    binding.redirectSignUpTV.visibility = View.VISIBLE
                }
            }
        } else {
            Toast.makeText(requireContext(), "Заполните необходимые поля", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun myToast(text: String) {
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_SHORT
        ).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}