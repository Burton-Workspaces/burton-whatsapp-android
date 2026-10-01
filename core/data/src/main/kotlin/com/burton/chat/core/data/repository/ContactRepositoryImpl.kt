package com.burton.chat.core.data.repository

import com.burton.chat.core.data.local.dao.ContactDao
import com.burton.chat.core.data.mapper.toModel
import com.burton.chat.core.domain.repository.ContactRepository
import com.burton.chat.core.model.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactDao: ContactDao,
) : ContactRepository {
    override fun observeContacts(): Flow<List<Contact>> =
        contactDao.observeOthers().map { contacts -> contacts.map { it.toModel() } }

    override fun observeContact(id: String): Flow<Contact?> =
        contactDao.observeById(id).map { it?.toModel() }

    override suspend fun getContact(id: String): Contact? =
        contactDao.getById(id)?.toModel()
}
