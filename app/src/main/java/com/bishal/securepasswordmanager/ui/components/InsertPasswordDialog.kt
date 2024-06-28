package com.bishal.securepasswordmanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.bishal.securepasswordmanager.ui.theme.blackLight
import com.bishal.securepasswordmanager.ui.theme.blue1
import com.bishal.securepasswordmanager.ui.theme.green
import com.bishal.securepasswordmanager.ui.theme.greyHint
import com.bishal.securepasswordmanager.ui.theme.red
import com.bishal.securepasswordmanager.ui.theme.whiteBg
import com.bishal.securepasswordmanager.viewmodel.PasswordViewModel

/**
 * Composable function for displaying a modal bottom sheet to insert a new password entry.
 *
 * @param onDismiss Callback function invoked when the bottom sheet is dismissed.
 * @param viewModel ViewModel containing business logic for password management.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertPasswordDialog(
    onDismiss: () -> Unit,
    viewModel: PasswordViewModel
) {
    // Local mutable state for managing input fields and their error states
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // State variables for managing error states of input fields
    var nameError by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    // Validation function to check if input fields are filled correctly
    fun isValid(): Boolean {
        var valid = true

        if (name.isBlank()) {
            nameError = true
            valid = false
        } else {
            nameError = false
        }

        if (username.isBlank()) {
            usernameError = true
            valid = false
        } else {
            usernameError = false
        }

        if (password.isBlank()) {
            passwordError = true
            valid = false
        } else {
            passwordError = false
        }

        return valid
    }

    // Modal bottom sheet state
    val bottomSheetState = rememberModalBottomSheetState()

    // Composable content for the modal bottom sheet
    ModalBottomSheet(
        containerColor = whiteBg,
        sheetState = bottomSheetState,
        content = {
            Column {
                // Input field for account name
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false
                    },
                    label = { Text("Account Name", color = if (nameError) red else greyHint) },
                    textStyle = TextStyle(color = blackLight),
                    singleLine = true,
                    isError = nameError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .padding(horizontal = 24.dp)
                )

                // Input field for username/email
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        usernameError = false // Reset error when user starts typing
                    },
                    label = {
                        Text(
                            "Username/Email",
                            color = if (usernameError) red else greyHint
                        )
                    },
                    textStyle = TextStyle(color = blackLight),
                    singleLine = true,
                    isError = usernameError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .padding(horizontal = 24.dp)
                )

                // Input field for password
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = false // Reset error when user starts typing
                    },
                    label = { Text("Password", color = if (passwordError) red else greyHint) },
                    textStyle = TextStyle(color = blackLight),
                    singleLine = true,
                    isError = passwordError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, top = 8.dp)
                        .padding(horizontal = 24.dp)
                )

                // Component displaying password strength meter
                PasswordStrengthMeter(password)

                // Button to add a new account
                CustomButton(
                    text = "Add New Account",
                    onClick = {
                        if (isValid()) {
                            viewModel.insertPassword(name, username, password)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                )
            }
        },
        onDismissRequest = {
            onDismiss()
        }
    )
}

/**
 * Composable function to display the strength meter of a given password.
 *
 * @param password Password string to evaluate strength.
 */
@Composable
fun PasswordStrengthMeter(password: String) {
    // Calculate password strength based on various criteria
    val strength = calculatePasswordStrength(password)

    // Determine color for displaying strength level
    val color = when (strength) {
        0, 1 -> red
        2, 3 -> blue1
        4, 5 -> green
        else -> greyHint
    }

    // Label for password strength based on score
    val strengthLabel = when (strength) {
        0, 1 -> "Weak"
        2, 3 -> "Medium"
        4, 5 -> "Strong"
        else -> ""
    }

    // Composable column to display strength label and meter
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)) {
        Text("Password Strength: $strengthLabel", color = color)
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(color)
        )
    }
}

/**
 * Function to calculate the strength of a given password.
 *
 * @param password Password string to evaluate strength.
 * @return An integer representing the strength score of the password.
 */
fun calculatePasswordStrength(password: String): Int {
    var score = 0
    if (password.length >= 8) score++
    if (password.any { it.isUpperCase() }) score++
    if (password.any { it.isLowerCase() }) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++
    return score
}
