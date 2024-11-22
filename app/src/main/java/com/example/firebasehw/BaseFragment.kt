package com.example.firebasehw

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebasehw.databinding.FragmentBaseBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database


class BaseFragment : Fragment() {

    private var _binding: FragmentBaseBinding? = null
    private val binding get() = _binding!!
    private var listUserFromFirebase: MutableList<UserData> = mutableListOf()
    private lateinit var adapterRecycler: CustomAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBaseBinding.inflate(inflater, container, false)

        adapterRecycler = CustomAdapter(listUserFromFirebase)
        binding.baseFragmentRecyclerViewRV.adapter = adapterRecycler

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.baseFragmentSaveButtonBTN.setOnClickListener {
            val name = binding.baseFragmentNamelET.text.toString()
            val age = binding.baseFragmentPhoneET.text.toString()
            if (name.isBlank() || age.isBlank()) {
                Toast.makeText(requireActivity(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                val userAdd = UserData(name, age)
                addUser(userAdd)
                with(binding) {
                    baseFragmentNamelET.text.clear()
                    baseFragmentPhoneET.text.clear()
                }
            }
        }

        adapterRecycler.setUserClickListener(
            object : CustomAdapter.OnUserClickListener {
                override fun onUserClick(user: UserData, position: Int) {
                    deleteUser(user)

                }
            }
        )


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
    private fun deleteUser(user: UserData) {
        val index = search(listUserFromFirebase, user)
        val id = FirebaseAuth.getInstance().currentUser!!.uid
        val database = Firebase.database.reference.child("users")
            .child(id)

        database.child(user.name).removeValue().addOnSuccessListener {
            listUserFromFirebase.removeAt(index)
            adapterRecycler.notifyDataSetChanged()
            Toast.makeText(requireContext(), "Пользователь ${user.name} удален", Toast.LENGTH_SHORT)
                .show()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Пользователь не удален", Toast.LENGTH_SHORT).show()
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun readUsers() {
        val id = FirebaseAuth.getInstance().currentUser!!.uid
        Firebase.database.reference.child("users")
            .child(id).get().addOnSuccessListener {
                listUserFromFirebase.clear()
                for (elem in it.children) {
                    val user: UserData = elem.getValue(UserData::class.java)!!
                    listUserFromFirebase.add(user)
                }
                if (listUserFromFirebase.isEmpty()) {
                    Toast.makeText(
                        requireContext(), "В базе нет пользователей",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    adapterRecycler.updateAdapter(listUserFromFirebase)
                    adapterRecycler.notifyDataSetChanged()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addUser(userAdd: UserData) {
        val id = FirebaseAuth.getInstance().currentUser!!.uid
        val database = Firebase.database.reference
            .child("users")
            .child(id)

        val map: HashMap<String, UserData> = HashMap()
        map[userAdd.name.toString()] = userAdd
        database.updateChildren(map as Map<String, Any>)
    }


}

private fun search(users: MutableList<UserData>, user: UserData): Int {
    var result = -1
    for (i in users.indices) {
        if ((user.name == users[i].name) && (user.phone == users[i].phone)) result = i
    }
    return result
}