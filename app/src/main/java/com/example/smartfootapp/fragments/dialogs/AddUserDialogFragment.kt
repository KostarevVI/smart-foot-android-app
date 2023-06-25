package com.example.smartfootapp.fragments.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.smartfootapp.appComponent
import com.example.smartfootapp.databinding.FragmentAddUserBinding
import com.example.smartfootapp.db.entity.User
import com.example.smartfootapp.utils.Error
import com.example.smartfootapp.utils.Loading
import com.example.smartfootapp.utils.Success
import com.example.smartfootapp.viewmodels.UserScreenViewModel
import java.lang.Exception

class AddUserDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentAddUserBinding

    private val userViewModel: UserScreenViewModel by activityViewModels {requireContext().appComponent.viewModelFactory()}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddUserBinding.inflate(inflater, container, false)

        setupViews()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val window = dialog?.window
        window?.let {
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(it.attributes)

            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            it.attributes = lp
        }
    }

    private fun setupViews() {
        with(binding) {
            bAddUser.setOnClickListener { addNewUser() }
        }
    }

    private fun addNewUser() {
        with(binding) {
            var isAllFine = true
            val name = etName.text.toString()
            val dateOfBirth = etDateOfBirth.text.toString()
            val height: Float = try {
                etHeight.text.toString().toFloat()
            } catch (e: Exception) {
                isAllFine = false
                0.0f
            }
            val weight: Float = try {
                etWeight.text.toString().toFloat()
            } catch (e: Exception) {
                isAllFine = false
                0.0f
            }

            if (!isAllFine) {
                // TODO показать ошибку
                return
            }
            userViewModel.addUser(
                User(
                    id = 0L,
                    name = name,
                    dateOfBirth = dateOfBirth,
                    weight = weight,
                    height = height
                )
            ).observe(viewLifecycleOwner) {
                when (it) {
                    is Success -> {
                        dismiss()
                    }

                    is Error -> {
                        // TODO покзаать ошибку
                        pbAddUser.visibility = View.GONE
                        bAddUser.visibility = View.VISIBLE
                    }

                    is Loading -> {
                        pbAddUser.visibility = View.VISIBLE
                        bAddUser.visibility = View.GONE
                    }
                }
            }
        }
    }
}