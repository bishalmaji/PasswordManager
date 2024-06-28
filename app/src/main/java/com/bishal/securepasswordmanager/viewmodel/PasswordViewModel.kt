package com.bishal.securepasswordmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bishal.securepasswordmanager.data.repository.PasswordRepository
import com.bishal.securepasswordmanager.data.database.PasswordDatabase
import com.bishal.securepasswordmanager.model.PasswordEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for managing PasswordEntity data, including encryption and decryption operations.
 *
 * @param application Application context needed for accessing resources and databases.
 */
class PasswordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PasswordRepository

    // LiveData to hold the list of all passwords
    private val _allPasswords = MutableLiveData<List<PasswordEntity>>()
    val allPasswords: LiveData<List<PasswordEntity>> get() = _allPasswords

    init {
        // Initialize the repository with the DAO from the database
        val passwordDao = PasswordDatabase.getDatabase(application).passwordDao()
        repository = PasswordRepository(passwordDao)

        // Fetch all passwords from the repository and decrypt each one asynchronously
        viewModelScope.launch {
            repository.getAllPasswords().collect { passwords ->
                val decryptedPasswords = passwords.map { decryptPassword(it) }
                _allPasswords.postValue(decryptedPasswords)
            }
        }
    }

    /**
     * Decrypts the password of a PasswordEntity object.
     *
     * @param passwordEntity PasswordEntity to decrypt.
     * @return Decrypted PasswordEntity object.
     */
    private fun decryptPassword(passwordEntity: PasswordEntity): PasswordEntity {
        val decryptedPassword = EncryptionUtil.decrypt(getApplication(), passwordEntity.password)
        return passwordEntity.copy(password = decryptedPassword ?: passwordEntity.password)
    }

    /**
     * Inserts a new password into the repository.
     *
     * @param name Name of the password entry.
     * @param username Username associated with the password.
     * @param password Password to be encrypted and stored.
     */
    fun insertPassword(name: String, username: String, password: String) {
        // Encrypt the password before storing it in the database
        val encryptedPassword = EncryptionUtil.encrypt(getApplication(), password) ?: password
        val passwordEntity = PasswordEntity(name = name, username = username, password = encryptedPassword)

        // Launch a coroutine on the IO dispatcher to insert the password into the repository
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPassword(passwordEntity)
        }
    }

    /**
     * Updates an existing password in the repository.
     *
     * @param password Updated PasswordEntity object.
     */
    fun updatePassword(password: PasswordEntity) {
        // Launch a coroutine on the IO dispatcher to update the password in the repository
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePassword(password)
        }
    }

    /**
     * Deletes a password from the repository.
     *
     * @param password PasswordEntity object to delete.
     */
    fun deletePassword(password: PasswordEntity) {
        // Launch a coroutine on the IO dispatcher to delete the password from the repository
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePassword(password)
        }
    }
}
