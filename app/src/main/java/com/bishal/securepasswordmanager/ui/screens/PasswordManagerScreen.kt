package com.bishal.securepasswordmanager.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bishal.securepasswordmanager.R
import com.bishal.securepasswordmanager.model.PasswordEntity
import com.bishal.securepasswordmanager.ui.components.InsertPasswordDialog
import com.bishal.securepasswordmanager.ui.components.ManagePasswordDialog
import com.bishal.securepasswordmanager.ui.components.ViewPasswordDetailDialog
import com.bishal.securepasswordmanager.ui.theme.*
import com.bishal.securepasswordmanager.viewmodel.PasswordViewModel

/**
 * Composable function to display the Password Manager screen.
 *
 * @param viewModel PasswordViewModel to manage passwords.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PasswordManagerScreen(viewModel: PasswordViewModel) {
    // Observing the list of all passwords from ViewModel
    val allPasswords by viewModel.allPasswords.observeAsState(emptyList())

    // State variables to manage dialog visibility and selected password
    val showAddDialog = remember { mutableStateOf(false) }
    val showManageDialog = remember { mutableStateOf(false) }
    val showDetailDialog = remember { mutableStateOf(false) }
    val selectedPassword = remember { mutableStateOf<PasswordEntity?>(null) }

    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            // Floating action button to add a new password
            FloatingActionButton(
                onClick = {
                    showAddDialog.value = true
                    selectedPassword.value = null
                },
                containerColor = blue1,
                modifier = Modifier
                    .size(60.dp)
                    .offset(x = (-8).dp, y = (-8).dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add",
                    tint = white,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    ) {
        Column {
            Spacer(modifier = Modifier.height(40.dp))

            // Title of the screen
            Text(
                modifier = Modifier
                    .padding(16.dp),
                text = "Password Manager",
                fontSize = 20.sp, color = blackLight
            )

            HorizontalDivider(color = greyLight, thickness = 1.dp)

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Displaying all passwords as items
                allPasswords.forEach { password ->
                    PasswordItem(password) {
                        // Handle click on a password item
                        selectedPassword.value = password
                        showDetailDialog.value = true
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Conditional rendering of the dialogs

        // Dialog for adding a new password
        if (showAddDialog.value) {
            InsertPasswordDialog(
                onDismiss = { showAddDialog.value = false },
                viewModel = viewModel
            )
        }

        // Dialog for viewing password details
        if (showDetailDialog.value && selectedPassword.value != null) {
            ViewPasswordDetailDialog(
                password = selectedPassword.value!!,
                onUpdateClick = {
                    showDetailDialog.value = false
                    showManageDialog.value = true
                },
                onDeleteClick = {
                    viewModel.deletePassword(selectedPassword.value!!)
                    showDetailDialog.value = false
                },
                onDismiss = {
                    showDetailDialog.value = false
                }
            )
        }

        // Dialog for managing (editing) password details
        if (showManageDialog.value && selectedPassword.value != null) {
            ManagePasswordDialog(
                password = selectedPassword.value!!,
                onUpdatePassword = { updatedPassword ->
                    viewModel.updatePassword(updatedPassword)
                    showManageDialog.value = false
                },
                onDismiss = {
                    showManageDialog.value = false
                }
            )
        }
    }
}

/**
 * Composable function to display a single Password item.
 *
 * @param password PasswordEntity representing the password item to display.
 * @param onItemClick Callback function when the password item is clicked.
 */
@Composable
fun PasswordItem(password: PasswordEntity, onItemClick: () -> Unit) {
    // Container box for each password item
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .background(color = white, shape = RoundedCornerShape(50.dp))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Displaying password name and asterisks for hidden password
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = password.name, fontSize = 20.sp, color = blackLight)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "*******", fontSize = 20.sp, color = grey,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.baseline_chevron_right_24),
                contentDescription = "Forward Icon",
                // Adjust size as needed
            )
        }
    }
}
