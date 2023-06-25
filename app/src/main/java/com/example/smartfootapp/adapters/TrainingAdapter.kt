package com.example.smartfootapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartfootapp.R
import com.example.smartfootapp.domain.TrainingElement

class TrainingAdapter : RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>() {

    private var trainings: List<TrainingElement> = emptyList()

    fun submitData(newTrainingList: List<TrainingElement>) {
        trainings = newTrainingList
        notifyDataSetChanged()
    }

    inner class TrainingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.tv_training_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_training, parent, false)

        return TrainingViewHolder(itemView)
    }

    override fun getItemCount(): Int = trainings.size

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val item = trainings[position]

        holder.textView.text = item.name
    }
}