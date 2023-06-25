package com.example.smartfootapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartfootapp.MainActivity
import com.example.smartfootapp.R
import com.example.smartfootapp.adapters.UserAdapter
import com.example.smartfootapp.appComponent
import com.example.smartfootapp.databinding.FragmentUserManagementBinding
import com.example.smartfootapp.db.entity.User
import com.example.smartfootapp.fragments.dialogs.AddUserDialogFragment
import com.example.smartfootapp.viewmodels.UserScreenViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserManagementFragment : Fragment() {
    private lateinit var binding: FragmentUserManagementBinding

    private val userViewModel: UserScreenViewModel by activityViewModels { requireActivity().appComponent.viewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserManagementBinding.inflate(inflater, container, false)

        setupViews()
        setupObservers()

        return binding.root
    }

    private fun setupViews() {
        with(binding) {
            fabAdd.setOnClickListener {
                AddUserDialogFragment().show(childFragmentManager, "ADD_USER_DIALOG")
            }
            rvUsers.layoutManager = LinearLayoutManager(requireContext())
            rvUsers.adapter = UserAdapter(
                onDeleteClicked = this@UserManagementFragment::onUserDelete,
                onEditClicked = this@UserManagementFragment::onUserEdit
            )
        }
    }

    private fun onUserDelete(userToDelete: User) {
        userViewModel.deleteUserById(userToDelete.id)
    }

    private fun onUserEdit(userToEdit: User) {

    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    userViewModel.users.collect {
                        Log.d("TTTEST", it.toString())
                        (binding.rvUsers.adapter as UserAdapter).submitData(it)
                    }
                }
            }
        }
    }
}