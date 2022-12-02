package com.practice.branchinternapp.domain.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.branchinternapp.domain.data.AuthModel
import com.practice.branchinternapp.domain.data.MessageListResponse
import com.practice.branchinternapp.domain.data.OutputResponse
import com.practice.branchinternapp.domain.data.ParticularMessage
import com.practice.branchinternapp.domain.repository.Repository
import com.practice.branchinternapp.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class NewViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _listofState = MutableStateFlow<ListState>(ListState.Empty)
    val listofState: StateFlow<ListState> = _listofState.asStateFlow()

    private val _mssglistofState = MutableStateFlow<MessageListState>(MessageListState.Empty)
    val mssglistofState: StateFlow<MessageListState> = _mssglistofState.asStateFlow()

    private val _particularmsglistofState =
        MutableStateFlow<ParticularMssgListState>(ParticularMssgListState.Empty)
    val particularmsglistofState: StateFlow<ParticularMssgListState> =
        _particularmsglistofState.asStateFlow()

    fun listofResponse(authModel: AuthModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                when (val response = repository.getData(authModel)) {
                    is Resources.Success -> {

                        Log.i("inside", "in sucess viewmodel")
                        Log.i("dataheregetviewmodel", response.data.toString())
                        if (response.data != null) {
                            _listofState.value = ListState.Success(response.data)
                        }

                    }
                    is Resources.Error -> {

                        Log.i("inside", "in error viewmodel")
                        _listofState.value =
                            ListState.Error(response.message.toString())
                    }

                    else -> {}
                }


            } catch (e: Exception) {

                Log.i("inside", "in catch viemodel")
                _listofState.value = ListState.Error(e.message.toString())
            }
        }
    }

    fun listofMssgResponse(authToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                when (val response = repository.getMessage(authToken)) {
                    is Resources.Success -> {

                        Log.i("inside", "in sucess viewmodel")
                        Log.i("dataheregetviewmodel", response.data.toString())
                        if (response.data != null) {
                            _mssglistofState.value = MessageListState.Success(response.data)
                        }

                    }
                    is Resources.Error -> {

                        Log.i("inside", "in error viewmodel")
                        _mssglistofState.value =
                            MessageListState.Error(response.message.toString())
                    }

                    else -> {}
                }


            } catch (e: Exception) {

                Log.i("inside", "in catch viemodel")
                _mssglistofState.value = MessageListState.Error(e.message.toString())
            }
        }
    }

    fun listofParticularMssgResponse(authToken: String, particularMessage: ParticularMessage) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                Log.i("valueparticular", "$authToken $particularMessage")
                when (val response =
                    repository.getParticularMessage(authToken, particularMessage)) {
                    is Resources.Success -> {

                        Log.i("dataofdataclass3", response.data.toString())
                        if (response.data != null) {
                            _particularmsglistofState.value =
                                ParticularMssgListState.Success(response.data)
                        }

                    }
                    is Resources.Error -> {

                        Log.i("insidemf", "in error viewmodel")
                        _particularmsglistofState.value =
                            ParticularMssgListState.Error(response.message.toString())
                    }

                    else -> {}
                }


            } catch (e: Exception) {

                Log.i("inside", "in catch viemodel")
                _particularmsglistofState.value =
                    ParticularMssgListState.Error(e.message.toString())
            }
        }
    }

    sealed class ListState {
        data class Success(
            val data: OutputResponse
        ) : ListState()

        data class Error(val message: String) : ListState()
        object Empty : ListState()
    }

    sealed class MessageListState {
        data class Success(
            val data: ArrayList<MessageListResponse>
        ) : MessageListState()

        data class Error(val message: String) : MessageListState()
        object Loading : MessageListState()
        object Empty : MessageListState()
    }

    sealed class ParticularMssgListState {
        data class Success(
            val data: MessageListResponse
        ) : ParticularMssgListState()

        data class Error(val message: String) : ParticularMssgListState()
        object Empty : ParticularMssgListState()
    }
}