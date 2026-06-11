package com.scanwise.app

import android.app.Application
import com.scanwise.app.data.local.ScanWiseDatabase
import com.scanwise.app.data.repository.ScanRepository

class ScanWiseApp : Application() {
    val database: ScanWiseDatabase by lazy { ScanWiseDatabase.getInstance(this) }
    val repository: ScanRepository by lazy {
        ScanRepository(database.scanHistoryDao(), database.blacklistedUrlDao())
    }
}
