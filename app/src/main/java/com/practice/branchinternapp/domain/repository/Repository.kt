package com.practice.branchinternapp.domain.repository

import android.util.Log
import com.practice.branchinternapp.api.APIHUB
import com.practice.branchinternapp.domain.data.AuthModel
import com.practice.branchinternapp.domain.data.MessageListResponse
import com.practice.branchinternapp.domain.data.OutputResponse
import com.practice.branchinternapp.domain.data.ParticularMessage
import com.practice.branchinternapp.util.Resources
import javax.inject.Inject

class Repository @Inject constructor(private val apiHub: APIHUB) {

    suspend fun getData(authModel: AuthModel): Resources<OutputResponse?> {
        val response = try {

            Log.i("inside", "apiResponse.toString()")
            apiHub.getData(authModel)
        } catch (e: Exception) {

            Log.i("inside", "error.toString()")
            return Resources.Error("Error: ${e.message}")
        }
        return Resources.Success(data = response.body())

    }

    suspend fun getMessage(authToken: String): Resources<ArrayList<MessageListResponse>?> {
        val response = try {

            Log.i("inside", "apiResponse.toString()")
            apiHub.getMessages(authToken)
        } catch (e: Exception) {

            Log.i("inside", "error.toString()")
            return Resources.Error("Error: ${e.message}")
        }
        return Resources.Success(data = response.body())

    }

    suspend fun getParticularMessage(
        authToken: String,
        particularMessage: ParticularMessage
    ): Resources<MessageListResponse?> {
        val response = try {
            apiHub.getParticularMessage(authToken, particularMessage)
        } catch (e: Exception) {

            Log.i("inside", "error.toString()")
            return Resources.Error("Error: ${e.message}")
        }
        return Resources.Success(data = response.body())

    }
}