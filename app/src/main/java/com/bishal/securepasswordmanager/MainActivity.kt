package com.bishal.securepasswordmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import com.bishal.securepasswordmanager.ui.screens.PasswordManagerScreen
import com.bishal.securepasswordmanager.ui.theme.SecurePasswordManagerTheme
import com.bishal.securepasswordmanager.viewmodel.PasswordViewModel

/**
 * MainActivity is the entry point of the application and manages the initial setup.
 */
class MainActivity : ComponentActivity() {
    // Lazy initialization of ViewModel
    private val viewModel: PasswordViewModel by lazy { PasswordViewModel(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = applicationContext

        try {
            // Initialize EncryptedSharedPreferences and ensure key setup
            KeyMaker.init(context)

            // Generate and save a secret key if not already present
            if (KeyMaker.getKey(context) == null) {
                KeyMaker.generateAndSaveSecretKey(context)
                Log.d("MainActivity", "Secret key generated and saved.")
            } else {
                Log.d("MainActivity", "Secret key already exists.")
            }

            // Set up Compose UI using SecurePasswordManagerTheme
            setContent {
                SecurePasswordManagerTheme {
                    // Display the main screen of the application
                    Surface(color = colorScheme.background) {
                        PasswordManagerScreen(viewModel)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Log any errors that occur during key generation or retrieval
            Log.e("MainActivity", "Error during key generation or retrieval: ${e.message}")
        }
    }

    companion object {
        /**
         * Creates a new Intent for launching MainActivity.
         *
         * @param context The application context.
         * @return Intent to launch MainActivity.
         */
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
