package com.example.smartfootapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartfootapp.adapters.TrainingAdapter
import com.example.smartfootapp.databinding.FragmentDiagnosticBinding
import com.example.smartfootapp.domain.TrainingElement

class DiagnosticFragment : Fragment() {
    private lateinit var binding: FragmentDiagnosticBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiagnosticBinding.inflate(inflater, container, false)

        with(binding.rvTrainings) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = TrainingAdapter()

            val dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL)
            addItemDecoration(dividerItemDecoration)

            (adapter as TrainingAdapter).submitData(listOf(TrainingElement("Тест Ромберга"), TrainingElement("Оценка по шкале Тинитти"), TrainingElement("Оценка по шкале Берга")))

        }

        return binding.root
    }
}