package com.practice.branchinternapp.domain.data

data class AuthModel(
    val username: String?,
    val password: String?
)

data class OutputResponse(
    val auth_token: String
)

data class MessageListResponse(
    val agent_id: Any?,
    val body: String?,
    val id: Int?,
    val thread_id: Int?,
    val timestamp: String?,
    val user_id: String?
)

data class ParticularMessage(
    val thread_id: Int?,
    val body: String?
)


