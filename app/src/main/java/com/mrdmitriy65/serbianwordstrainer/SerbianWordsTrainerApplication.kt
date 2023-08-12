package com.mrdmitriy65.serbianwordstrainer

import android.app.Application
import com.mrdmitriy65.serbianwordstrainer.data.AppDatabase

class SerbianWordsTrainerApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}