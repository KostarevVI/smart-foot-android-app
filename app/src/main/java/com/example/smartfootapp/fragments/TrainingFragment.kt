package com.example.smartfootapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartfootapp.MainActivity
import com.example.smartfootapp.adapters.TrainingAdapter
import com.example.smartfootapp.databinding.FragmentTrainingBinding
import com.example.smartfootapp.domain.TrainingElement

class TrainingFragment : Fragment() {
    private lateinit var binding: FragmentTrainingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrainingBinding.inflate(inflater, container, false)

        setupViews()

        return binding.root
    }

    private fun setupViews() {
        with(binding.rvTrainings) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = TrainingAdapter()

            val dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL)
            addItemDecoration(dividerItemDecoration)

            (adapter as TrainingAdapter).submitData(listOf(TrainingElement("Подъем на носки"), TrainingElement("Покачивания влево-вправо"), TrainingElement("Покачивания вперед-назад")))
        }

        with(binding) {
            bStartScan.setOnClickListener {
                (requireActivity() as MainActivity).toggleBleScan()
            }
        }
    }
}