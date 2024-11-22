package com.example.firebasehw

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasehw.databinding.FragmentBaseBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database


class BaseFragment : Fragment() {

    private var _binding: FragmentBaseBinding? = null
    private val binding get() = _binding!!
    var item: Int? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBaseBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.baseFragmentSaveButtonBTN.setOnClickListener {
            val name = binding.baseFragmentNamelET.text.toString()
            val age = binding.baseFragmentPhoneET.text.toString()
            if (name.isBlank() || age.isBlank()) {
                Toast.makeText(requireActivity(),"Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                val userAdd = UserData(name,age)
                addUser(userAdd)
                with(binding) {
                    baseFragmentNamelET.text.clear()
                    baseFragmentPhoneET.text.clear()
                }
            }
        }

        binding.baseFragmentReadButtonBTN.setOnClickListener {
            readUsers()
        }

        binding.backButtonBTN.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        binding.exitButtonBTN.setOnClickListener {
            requireActivity().finishAffinity()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun readUsers() {
        val id = FirebaseAuth.getInstance().currentUser!!.uid
        Firebase.database.reference.child("users")
            .child(id).get().addOnSuccessListener {
                val listUserFromFirebase = mutableListOf<UserData>()
                for (elem in it.children) {
                    val user: UserData = elem.getValue(UserData::class.java)!!
                    listUserFromFirebase.add(user)
                }
                val adapterRecycler = CustomAdapter(listUserFromFirebase)
                binding.baseFragmentRecyclerViewRV.adapter = adapterRecycler
                adapterRecycler.notifyDataSetChanged()

            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addUser(userAdd: UserData){
        val id = FirebaseAuth.getInstance().currentUser!!.uid
        val database = Firebase.database.reference
            .child("users")
            .child(id)

        val map: HashMap<String, UserData> = HashMap()
        map[userAdd.name.toString()] = userAdd
        database.updateChildren(map as Map<String, Any>)
    }



}