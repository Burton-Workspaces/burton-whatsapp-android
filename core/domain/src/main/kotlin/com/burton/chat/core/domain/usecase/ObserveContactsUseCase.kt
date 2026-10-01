package com.burton.chat.core.domain.usecase

import com.burton.chat.core.domain.repository.ContactRepository
import com.burton.chat.core.model.Contact
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveContactsUseCase @Inject constructor(
    private val contactRepository: ContactRepository,
) {
    operator fun invoke(): Flow<List<Contact>> = contactRepository.observeContacts()
}
