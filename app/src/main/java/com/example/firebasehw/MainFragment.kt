package com.example.firebasehw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebasehw.databinding.FragmentMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = Firebase.auth
        binding.signInButtonBTN.setOnClickListener {
            signUpUser()
        }

        binding.tvRedirectLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun signUpUser() {
        val email = binding.emailSignUpEditText.text.toString()
        val pass = binding.passwordSignUpEditText.text.toString()
        val confirmPass = binding.confirmPasswordSignUpEditText.text.toString()

        if (email.isBlank() || pass.isBlank() || confirmPass.isBlank()) {
            myToast("Заполните все необходимые поля")
            return
        }
        if (pass != confirmPass) {
            myToast("Пароли не совпадают")
            return
        }

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(requireActivity()) {
            if (it.isSuccessful) {
                myToast("Регистрация прошла успешно")
                findNavController().navigate(R.id.loginFragment)
            } else {
                if (auth.currentUser != null) {
                    myToast("Пользователь уже существует")
                    findNavController().navigate(R.id.loginFragment)
                }
                myToast("Не удалось зарегистрироваться")

            }
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