package com.crtyiot.ept.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.crtyiot.ept.network.MatApi
import com.crtyiot.ept.MainApplication

// use flow observe data from repo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class bbbViewModel : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var _bbbUiState = MutableStateFlow ("")
    var bbbUiState : StateFlow<String> = _bbbUiState

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */
    fun getMat() {
        //UiState = "Set the Mars API status response here!"
        viewModelScope.launch {
            val listResult = MatApi.retrofitService.getMaterials()
            Log.i("bbbViewModel-a", "getMat: $listResult")
            _bbbUiState.value = listResult
            Log.i("bbbViewModel-b", "getMat: $listResult")
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)
                bbbViewModel() // return an instance of bbbViewModel
            }
        }
    }
}