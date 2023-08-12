package com.mrdmitriy65.serbianwordstrainer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mrdmitriy65.serbianwordstrainer.R
import com.mrdmitriy65.serbianwordstrainer.models.Exercise
import com.mrdmitriy65.serbianwordstrainer.models.ExerciseType

class ResultListAdapter(private val results: List<Exercise>) :
    RecyclerView.Adapter<ResultListAdapter.ResultListViewHolder>() {

    class ResultListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val questionText: TextView = view.findViewById(R.id.question_text)
        val answerText: TextView = view.findViewById(R.id.answer_text)
        val resultText: TextView = view.findViewById(R.id.result_text)
        val exerciseTypeText: TextView = view.findViewById(R.id.exercise_type_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultListViewHolder {
        val inflater = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.exercise_result_layout, parent, false)

        return ResultListViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ResultListViewHolder, position: Int) {
        holder.questionText.text = results[position].pair.question
        holder.answerText.text = results[position].pair.answer
        holder.resultText.text =
            if (results[position].isCorrect) holder.view.context.getString(R.string.answer_activity_correct)
            else holder.view.context.getString(R.string.answer_activity_incorrect)
        holder.exerciseTypeText.text = when(results[position].exerciseType){
            ExerciseType.CHOSE_FROM_VARIANTS -> "Тип 1"
            ExerciseType.WRITE_WORD_FROM_CHARACTERS -> "Тип 2"
            ExerciseType.WRITE_ANSWER -> "Тип 3"
        }
    }

    override fun getItemCount(): Int {
        return results.count()
    }
}