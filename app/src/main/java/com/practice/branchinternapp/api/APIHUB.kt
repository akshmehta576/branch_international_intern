package com.practice.branchinternapp.api

import com.practice.branchinternapp.domain.data.AuthModel
import com.practice.branchinternapp.domain.data.MessageListResponse
import com.practice.branchinternapp.domain.data.OutputResponse
import com.practice.branchinternapp.domain.data.ParticularMessage
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface APIHUB {

    @POST("api/login")
    suspend fun getData(
        @Body authModel: AuthModel
    ): Response<OutputResponse>

    @GET("api/messages")
    suspend fun getMessages(@Header("X-Branch-Auth-Token") header: String): Response<ArrayList<MessageListResponse>>

    @POST("api/messages")
    suspend fun getParticularMessage(
        @Header("X-Branch-Auth-Token") header: String,
        @Body particularMessage: ParticularMessage
    ): Response<MessageListResponse>
}
