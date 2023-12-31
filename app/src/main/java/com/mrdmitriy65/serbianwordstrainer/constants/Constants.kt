package com.mrdmitriy65.serbianwordstrainer.constants

class Constants {
    companion object {
        const val SERBIAN_TTS_VOICE = "sr"

        // Words constants
        const val WORD_START_LEARN_LEVEL = 0
        const val WORD_LAST_LEARN_LEVEL = 8
        const val WORD_ANSWERS_TO_INCREASE_EXERCISE_LEVEL = 3

        const val EXERCISE_BUFFER_WORDS_COUNT = 30
        const val EXERCISE_WORDS_COUNT_IN_EXERCISE = 15
        const val EXERCISE_PORTION_ADD_TO_BUFFER_WORDS_COUNT = 5

        // Log tags
        const val LOG_TAG_EXERCISE_TRAINING = "Exercise_training"

        // Backup constants
        const val BACKUP_FILE_NAME = "WordsBackup.json"
    }
}