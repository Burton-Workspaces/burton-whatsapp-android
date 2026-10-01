package com.burton.chat.core.domain.repository

import com.burton.chat.core.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun observeContacts(): Flow<List<Contact>>
    fun observeContact(id: String): Flow<Contact?>
    suspend fun getContact(id: String): Contact?
}
