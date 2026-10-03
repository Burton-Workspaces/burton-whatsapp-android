package com.burton.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.burton.chat.core.designsystem.theme.BurtonChatTheme
import com.burton.chat.navigation.BurtonChatApp
import com.burton.chat.report.ShakeToReport
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val shakeToReport by lazy { ShakeToReport(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BurtonChatTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BurtonChatApp()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        shakeToReport.start()
    }

    override fun onPause() {
        shakeToReport.stop()
        super.onPause()
    }
}
