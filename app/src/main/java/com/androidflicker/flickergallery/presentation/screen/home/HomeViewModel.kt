package com.androidflicker.flickergallery.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidflicker.flickergallery.core.error.userMessage
import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.usecase.GetRecentPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getRecentPhotosUseCase: GetRecentPhotosUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(HomeUiState())
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

        init {
            onEvent(HomeEvent.LoadPhotos)
        }

        fun onEvent(event: HomeEvent) {
            when (event) {
                HomeEvent.LoadPhotos, HomeEvent.RetryLoad -> loadPhotos()
            }
        }

        private fun loadPhotos() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, error = null) }
                when (val result = getRecentPhotosUseCase()) {
                    is AppResult.Success ->
                        _uiState.update {
                            it.copy(isLoading = false, photos = result.data)
                        }
                    is AppResult.Error ->
                        _uiState.update {
                            it.copy(isLoading = false, error = result.error.userMessage())
                        }
                }
            }
        }
    }
