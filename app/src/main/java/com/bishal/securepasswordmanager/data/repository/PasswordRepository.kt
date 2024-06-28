package com.bishal.securepasswordmanager.data.repository

import com.bishal.securepasswordmanager.data.dao.PasswordDao
import com.bishal.securepasswordmanager.model.PasswordEntity
import kotlinx.coroutines.flow.Flow

class PasswordRepository(private val passwordDao: PasswordDao) {

    fun getAllPasswords(): Flow<List<PasswordEntity>> {
        return passwordDao.getAllPasswords()
    }

    suspend fun insertPassword(password: PasswordEntity) {
        passwordDao.insertPassword(password)
    }
    suspend fun updatePassword(password: PasswordEntity) {
        passwordDao.updatePassword(password)
    }
    suspend fun deletePassword(password: PasswordEntity) {
        passwordDao.deletePassword(password)
    }
}
