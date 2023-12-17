package com.mrdmitriy65.serbianwordstrainer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.mrdmitriy65.serbianwordstrainer.constants.Constants
import com.mrdmitriy65.serbianwordstrainer.data.entities.Category
import com.mrdmitriy65.serbianwordstrainer.data.entities.WordPair
import com.mrdmitriy65.serbianwordstrainer.databinding.ActivityMainBinding
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.moveToCategoriesButton.setOnClickListener { moveToTraining() }
        binding.moveToDictionaryButton.setOnClickListener { moveToDictionary() }
        binding.moveToExperimentalButton.setOnClickListener {
        }
        binding.createBackup.setOnClickListener { createBackup() }
        binding.loadBackup.setOnClickListener { loadBackup() }
    }

    private fun moveToDictionary() {
        val intent = Intent(this, DictionaryActivity::class.java)
        startActivity(intent)
    }

    private fun moveToTraining() {
        val intent = Intent(this, TrainerActivity::class.java)
        startActivity(intent)
    }

    private fun createBackup() {
        try {
            val dao = (this.application as SerbianWordsTrainerApplication)
                .database.wordPairDao()
            runBlocking {
                val data = dao.getAllCategoriesWithWords()

                val wordsBackup = data.flatMap { c ->
                    c.wordPairs.map {
                        BackupWord(
                            it.russian,
                            it.serbian,
                            it.comment,
                            c.category.name,
                            it.pronunciation,
                            it.learnLevel
                        )
                    }
                }
                val json = Gson().toJson(wordsBackup)

                val outputStreamWriter =
                    OutputStreamWriter(
                        this@MainActivity.openFileOutput(
                            Constants.BACKUP_DATABASE_FILE_PATH,
                            MODE_PRIVATE
                        )
                    )
                outputStreamWriter.write(json)
                outputStreamWriter.close()
            }
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }
    }

    private fun loadBackup() {
        var ret = ""

        try {
            val inputStream: InputStream = this.openFileInput(Constants.BACKUP_DATABASE_FILE_PATH)

            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var receiveString: String? = ""
            val stringBuilder = StringBuilder()
            while (bufferedReader.readLine().also { receiveString = it } != null) {
                stringBuilder.append("\n").append(receiveString)
            }
            inputStream.close()
            ret = stringBuilder.toString()

        } catch (e: FileNotFoundException) {
            Log.e("login activity", "File not found: $e")
        } catch (e: IOException) {
            Log.e("login activity", "Can not read file: $e")
        }


        val dao = (this.application as SerbianWordsTrainerApplication)
            .database.wordPairDao()
        val words = Gson().fromJson(ret, Array<BackupWord>::class.java)

        val gWords = words.groupBy { it.cN }
        runBlocking {

            for (w in gWords) {
                val category = if (dao.isCategoryExists(w.key)) {
                    dao.getCategoryByName(w.key)
                } else {
                    dao.insert(Category(name = w.key))
                    dao.getCategoryByName(w.key)
                }
                var mappedWords = w.value.map {
                    WordPair(
                        russian = it.r,
                        serbian = it.s,
                        comment = it.com,
                        learnLevel = it.l,
                        pronunciation = it.p,
                        categoryId = category.id
                    )
                }
                for (word in mappedWords) {
                    if (dao.isWordPairExists(word.russian, word.serbian)) {
                        continue
                    }
                    dao.insert(word)
                }
            }
        }
    }

    class BackupWord(
        val r: String,
        val s: String,
        val com: String,
        val cN: String,
        val p: String,
        val l: Int,
    )
}