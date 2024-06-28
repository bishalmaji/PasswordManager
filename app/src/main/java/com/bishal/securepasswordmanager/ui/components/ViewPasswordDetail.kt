package com.bishal.securepasswordmanager.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bishal.securepasswordmanager.model.PasswordEntity
import com.bishal.securepasswordmanager.ui.components.CustomButton
import com.bishal.securepasswordmanager.ui.theme.blackLight
import com.bishal.securepasswordmanager.ui.theme.blue1
import com.bishal.securepasswordmanager.ui.theme.greyHint
import com.bishal.securepasswordmanager.ui.theme.red
import com.bishal.securepasswordmanager.ui.theme.whiteBg

/**
 * Composable function for viewing password details in a modal bottom sheet.
 *
 * @param password PasswordEntity object containing details to display.
 * @param onUpdateClick Callback function for the "Edit" button click event.
 * @param onDeleteClick Callback function for the "Delete" button click event.
 * @param onDismiss Callback function invoked when the bottom sheet is dismissed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewPasswordDetailDialog(
    password: PasswordEntity,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit
) {
    // State to toggle password visibility
    var passwordVisible by remember { mutableStateOf(false) }

    // Modal bottom sheet state
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        containerColor = whiteBg,
        sheetState = bottomSheetState,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(text = "Account Details", fontSize = 24.sp, color = blue1)
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Account Type", fontSize = 16.sp, color = greyHint)
                Text(text = password.name, fontSize = 20.sp, color = blackLight)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Username/ Email", fontSize = 16.sp, color = greyHint)
                Text(text = password.username, fontSize = 20.sp, color = blackLight)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Password", fontSize = 16.sp, color = greyHint)

                // Row to display password text and toggle visibility icon
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Text(
                        text = if (passwordVisible) password.password else "******",
                        fontSize = 20.sp,
                        color = blackLight,
                        modifier = Modifier.weight(1f) // Expand to take remaining space
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Icon to toggle password visibility
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                        tint = if (passwordVisible) blue1 else blackLight,
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Row with buttons for Edit and Delete actions
                Row(
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomButton(
                        text = "Edit",
                        onClick = onUpdateClick,
                        width = 150
                    )

                    CustomButton(
                        text = "Delete",
                        onClick = onDeleteClick,
                        backgroundColor = red,
                        width = 150
                    )
                }
            }
        },
        onDismissRequest = onDismiss
    )
}
