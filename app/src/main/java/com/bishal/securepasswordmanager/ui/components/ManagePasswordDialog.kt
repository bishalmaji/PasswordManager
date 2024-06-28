package com.bishal.securepasswordmanager.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import com.bishal.securepasswordmanager.model.PasswordEntity
import com.bishal.securepasswordmanager.ui.components.CustomButton
import com.bishal.securepasswordmanager.ui.theme.greyHint
import com.bishal.securepasswordmanager.ui.theme.red
import com.bishal.securepasswordmanager.ui.theme.whiteBg

/**
 * Composable function for managing password details in a modal bottom sheet.
 *
 * @param password Initial PasswordEntity object to be edited.
 * @param onUpdatePassword Callback function to update the edited PasswordEntity.
 * @param onDismiss Callback function invoked when the bottom sheet is dismissed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePasswordDialog(
    password: PasswordEntity,
    onUpdatePassword: (PasswordEntity) -> Unit,
    onDismiss: () -> Unit
) {
    // Mutable state variables for edited fields
    var editedName by remember { mutableStateOf(password.name) }
    var editedUsername by remember { mutableStateOf(password.username) }
    var editedPassword by remember { mutableStateOf(password.password) }

    // Validation function to check if all fields are non-empty
    fun isValid(): Boolean {
        return editedName.isNotBlank() && editedUsername.isNotBlank() && editedPassword.isNotBlank()
    }

    // State for managing bottom sheet visibility
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        containerColor = whiteBg,
        sheetState = bottomSheetState,
        content = {
            Column {
                // TextFields for editing fields
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Name", color = if (editedName.isBlank()) red else greyHint) },
                    isError = editedName.isBlank(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).padding(horizontal = 24.dp)
                )
                OutlinedTextField(
                    value = editedUsername,
                    onValueChange = { editedUsername = it },
                    label = { Text("Username", color = if (editedUsername.isBlank()) red else greyHint) },
                    isError = editedUsername.isBlank(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).padding(horizontal = 24.dp)
                )
                OutlinedTextField(
                    value = editedPassword,
                    onValueChange = { editedPassword = it },
                    label = { Text("Password", color = if (editedPassword.isBlank()) red else greyHint) },
                    isError = editedPassword.isBlank(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).padding(horizontal = 24.dp)
                )
                CustomButton(
                    text = "Update",
                    onClick = {
                        // Validate fields before updating
                        if (isValid()) {
                            // Create updated PasswordEntity object with edited values
                            val updatedPassword = PasswordEntity(
                                id = password.id,
                                name = editedName,
                                username = editedUsername,
                                password = editedPassword
                            )
                            onUpdatePassword(updatedPassword) // Call callback to update password
                            onDismiss() // Dismiss the bottom sheet after updating
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                )
            }
        },
        onDismissRequest = {
            onDismiss() // Handle dismiss request
        }
    )
}
