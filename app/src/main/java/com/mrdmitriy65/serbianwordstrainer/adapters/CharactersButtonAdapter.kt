package com.mrdmitriy65.serbianwordstrainer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.mrdmitriy65.serbianwordstrainer.R
import com.mrdmitriy65.serbianwordstrainer.logic.WriteWordExerciseChecker

class CharactersButtonAdapter(
    val answer: String,
    val onCompleteExercise: (String?) -> Unit,
    val onAnswerChange: (String) -> Unit
) :
    RecyclerView.Adapter<CharactersButtonAdapter.CharactersButtonViewHolder>() {

    val checker = WriteWordExerciseChecker(answer)
    val answerCharacters = answer.toMutableList().shuffled()

    class CharactersButtonViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.character_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersButtonViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.characters_button_layout, parent, false)

        return CharactersButtonViewHolder(layout)
    }

    override fun onBindViewHolder(holder: CharactersButtonViewHolder, position: Int) {
        val item = answerCharacters.get(position)
        holder.button.text = item.toString()
        holder.button.setOnClickListener {
            val isCorrect = checker.checkChar(item)
            if (isCorrect) {
                holder.button.isEnabled = false
                onAnswerChange(checker.getAnsweredPart())
            }
            if (checker.isCompleted()) {
                val exerciseResult =
                    if (checker.isCompletedCorrect()) answer
                    else String()
                onCompleteExercise(exerciseResult)
            }
        }
    }

    override fun getItemCount(): Int {
        return answerCharacters.count()
    }
}