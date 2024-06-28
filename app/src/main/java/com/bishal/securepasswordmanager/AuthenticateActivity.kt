package com.bishal.securepasswordmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

/**
 * Activity responsible for biometric authentication.
 */
class AuthenticateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authenticate)

        // Check if biometric authentication is supported on this device
        if (isBiometricSupported()) {
            showBiometricPrompt()
        } else {
            // If biometric authentication is not supported, navigate to the main activity
            navigateToMainActivity()
        }
    }

    /**
     * Navigates to the main activity after successful biometric authentication.
     */
    private fun navigateToMainActivity() {
        val intent = MainActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    /**
     * Checks if biometric authentication is supported on this device.
     *
     * @return True if biometric authentication is supported, false otherwise.
     */
    private fun isBiometricSupported(): Boolean {
        val biometricManager = BiometricManager.from(this)
        val canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        when (canAuthenticate) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // The user can authenticate with biometrics, continue with the authentication process
                return true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE, BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE, BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Handle the error cases as needed in your app
                return false
            }
            else -> {
                // Biometric status unknown or another error occurred
                return false
            }
        }
    }

    /**
     * Shows the biometric prompt to the user for authentication.
     */
    private fun showBiometricPrompt() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric to continue")
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Show error message to the user
                    showMessage("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Handle authentication success
                    navigateToMainActivity()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Show authentication failure message to the user
                    showMessage("Authentication failed.")
                }
            })

        // Start biometric authentication with the configured prompt
        biometricPrompt.authenticate(promptInfo)
    }

    /**
     * Displays a toast message to the user.
     *
     * @param message Message to display in the toast.
     */
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
