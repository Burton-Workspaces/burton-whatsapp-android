package com.burton.chat

import android.app.Application
import com.burton.chat.core.data.di.ApplicationScope
import com.burton.chat.core.data.local.ChatDatabaseInitializer
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class BurtonChatApplication : Application() {
    @Inject lateinit var databaseInitializer: ChatDatabaseInitializer

    @Inject
    @ApplicationScope
    lateinit var applicationScope: CoroutineScope

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            databaseInitializer.seedIfNeeded()
        }
    }
}
