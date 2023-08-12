package com.mrdmitriy65.serbianwordstrainer.logic

class WriteWordExerciseChecker(private val answer: String) {

    private val maxMistakes = 3
    private val formattedAnswer = answer.trim()
    private var mistakesCounter = 0
    private var currentAnswerIndex = 0

    fun checkChar(letter: Char): Boolean {
        if (isCompleted()) {
            return false
        }

        if (formattedAnswer.get(currentAnswerIndex).equals(letter, true)) {
            currentAnswerIndex++
            return true
        } else {
            mistakesCounter++
            return false
        }
    }

    fun isCompleted(): Boolean =
        mistakesCounter >= maxMistakes || currentAnswerIndex >= formattedAnswer.length

    fun isCompletedCorrect(): Boolean =
        mistakesCounter < maxMistakes && currentAnswerIndex >= formattedAnswer.length

    fun getAnsweredPart(): String {
        return answer.substring(0, currentAnswerIndex)
    }
}