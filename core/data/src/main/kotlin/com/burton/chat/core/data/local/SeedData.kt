package com.burton.chat.core.data.local

import com.burton.chat.core.common.CurrentUser
import com.burton.chat.core.data.local.entity.ContactEntity
import com.burton.chat.core.data.local.entity.ConversationEntity
import com.burton.chat.core.data.local.entity.ConversationMemberEntity
import com.burton.chat.core.data.local.entity.MessageEntity
import com.burton.chat.core.data.local.entity.ReadStateEntity
import java.util.UUID

internal object SeedData {
    const val MAYA = "contact-maya"
    const val JORDAN = "contact-jordan"
    const val SAM = "contact-sam"
    const val PRIYA = "contact-priya"
    const val ALEX = "contact-alex"
    const val CHRIS = "contact-chris"

    const val DM_MAYA = "convo-maya"
    const val DM_JORDAN = "convo-jordan"
    const val DM_SAM = "convo-sam"
    const val GROUP_BURTON = "group-burton-team"
    const val GROUP_WEEKEND = "group-weekend"
    const val GROUP_DESIGN = "group-design-guild"
    const val GROUP_RUNNING = "group-running-club"

    fun contacts(): List<ContactEntity> = listOf(
        ContactEntity(CurrentUser.ID, "Ryco", "+1 555 0100", "Hey there! I am using Burton Chat.", true, 1),
        ContactEntity(MAYA, "Maya Chen", "+1 555 0142", "Building things.", false, 2),
        ContactEntity(JORDAN, "Jordan Blake", "+1 555 0177", "Coffee first.", false, 3),
        ContactEntity(SAM, "Sam Okonkwo", "+1 555 0133", "On a run.", false, 4),
        ContactEntity(PRIYA, "Priya Nair", "+1 555 0190", "Ask me about books.", false, 5),
        ContactEntity(ALEX, "Alex Rivera", "+1 555 0118", "Weekend planner.", false, 6),
        ContactEntity(CHRIS, "Chris Patel", "+1 555 0164", "Design systems forever.", false, 7),
    )

    fun conversations(now: Long): List<ConversationEntity> = listOf(
        ConversationEntity(DM_MAYA, null, "DIRECT", now - days(8), now - hours(1)),
        ConversationEntity(DM_JORDAN, null, "DIRECT", now - days(5), now - hours(5)),
        ConversationEntity(DM_SAM, null, "DIRECT", now - days(2), now - days(1)),
        ConversationEntity(GROUP_BURTON, "Burton Team", "GROUP", now - days(20), now - minutes(25)),
        ConversationEntity(GROUP_WEEKEND, "Weekend Plans", "GROUP", now - days(12), now - hours(3)),
        ConversationEntity(GROUP_DESIGN, "Design Guild", "GROUP", now - days(30), now - hours(8)),
        ConversationEntity(GROUP_RUNNING, "Running Club", "GROUP", now - days(14), now - days(2)),
    )

    fun members(): List<ConversationMemberEntity> = listOf(
        member(DM_MAYA, CurrentUser.ID),
        member(DM_MAYA, MAYA),
        member(DM_JORDAN, CurrentUser.ID),
        member(DM_JORDAN, JORDAN),
        member(DM_SAM, CurrentUser.ID),
        member(DM_SAM, SAM),
        member(GROUP_BURTON, CurrentUser.ID, "ADMIN"),
        member(GROUP_BURTON, MAYA, "ADMIN"),
        member(GROUP_BURTON, JORDAN),
        member(GROUP_BURTON, SAM),
        member(GROUP_WEEKEND, CurrentUser.ID),
        member(GROUP_WEEKEND, PRIYA, "ADMIN"),
        member(GROUP_WEEKEND, ALEX),
        member(GROUP_DESIGN, CHRIS, "ADMIN"),
        member(GROUP_DESIGN, MAYA),
        member(GROUP_DESIGN, PRIYA),
        member(GROUP_RUNNING, JORDAN, "ADMIN"),
        member(GROUP_RUNNING, ALEX),
        member(GROUP_RUNNING, SAM),
    )

    fun messages(now: Long): List<MessageEntity> = listOf(
        message(DM_MAYA, MAYA, "Did the chat list land in review?", now - hours(5)),
        message(DM_MAYA, CurrentUser.ID, "Yes — pushing a build after lunch.", now - hours(4), "READ"),
        message(DM_MAYA, MAYA, "Nice. I will join the Burton Team thread if anything blocks.", now - hours(1)),
        message(DM_JORDAN, JORDAN, "Coffee at 10 still good?", now - hours(6)),
        message(DM_JORDAN, CurrentUser.ID, "Make it 10:15 — wrapping a meeting.", now - hours(5), "DELIVERED"),
        message(DM_SAM, SAM, "Trail was packed this morning. Same loop tomorrow?", now - days(1)),
        message(GROUP_BURTON, MAYA, "Standup notes are in the doc. Ship window is Thursday.", now - hours(6)),
        message(GROUP_BURTON, JORDAN, "I can take the groups screen if anyone is swamped.", now - hours(4)),
        message(GROUP_BURTON, SAM, "I will pair on the unread-count query.", now - hours(2)),
        message(GROUP_BURTON, MAYA, "Thread is open if you want a second pair of eyes.", now - minutes(25)),
        message(GROUP_WEEKEND, PRIYA, "Saturday hike at 8, lunch after. Who is in?", now - hours(8)),
        message(GROUP_WEEKEND, ALEX, "In. I can drive.", now - hours(6)),
        message(GROUP_WEEKEND, CurrentUser.ID, "Count me in — I will bring snacks.", now - hours(3), "READ"),
        message(GROUP_DESIGN, CHRIS, "New token set is in Figma. Please sanity-check contrast.", now - hours(10)),
        message(GROUP_DESIGN, PRIYA, "Looks solid on the chat bubbles.", now - hours(8)),
        message(GROUP_RUNNING, JORDAN, "5k this Sunday, 7am at the park gate.", now - days(2)),
        message(GROUP_RUNNING, ALEX, "I will be there.", now - days(2) + minutes(20)),
    )

    fun readState(now: Long): List<ReadStateEntity> = listOf(
        ReadStateEntity(DM_JORDAN, now),
        ReadStateEntity(DM_SAM, now),
        ReadStateEntity(GROUP_WEEKEND, now),
    )

    private fun member(
        conversationId: String,
        contactId: String,
        role: String = "MEMBER",
    ) = ConversationMemberEntity(
        conversationId = conversationId,
        contactId = contactId,
        role = role,
    )

    private fun message(
        conversationId: String,
        senderId: String,
        body: String,
        createdAt: Long,
        status: String = "SENT",
    ) = MessageEntity(
        id = UUID.nameUUIDFromBytes("$conversationId-$createdAt-$senderId".toByteArray()).toString(),
        conversationId = conversationId,
        senderId = senderId,
        body = body,
        createdAt = createdAt,
        status = status,
    )

    private fun minutes(value: Long) = value * 60_000L
    private fun hours(value: Long) = value * 3_600_000L
    private fun days(value: Long) = value * 86_400_000L
}
