package com.example.smartfootapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.smartfootapp.MainActivity
import com.example.smartfootapp.R
import com.example.smartfootapp.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        with(binding) {
            tvUsers.setOnClickListener {
                val action =
                    SettingsFragmentDirections.actionSettingsFragmentToUserManagementFragment()
                findNavController().navigate(action)
            }
            bTest.setOnClickListener {
                (requireActivity() as MainActivity).toggleBleScan()
            }
        }
    }
}