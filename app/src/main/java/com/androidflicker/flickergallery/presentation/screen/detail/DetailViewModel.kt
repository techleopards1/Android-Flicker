package com.androidflicker.flickergallery.presentation.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidflicker.flickergallery.core.error.userMessage
import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.usecase.GetPhotoDetailsUseCase
import com.androidflicker.flickergallery.presentation.navigation.AppDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getPhotoDetailsUseCase: GetPhotoDetailsUseCase,
    ) : ViewModel() {
        private val photoId: String =
            savedStateHandle[AppDestination.Detail.ARG_PHOTO_ID] ?: ""

        private val _uiState = MutableStateFlow(DetailUiState())
        val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

        init {
            if (photoId.isBlank()) {
                _uiState.update { it.copy(isEmpty = true) }
            } else {
                loadDetails()
            }
        }

        fun onEvent(event: DetailEvent) {
            when (event) {
                DetailEvent.Retry -> loadDetails()
            }
        }

        private fun loadDetails() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                when (val result = getPhotoDetailsUseCase(photoId)) {
                    is AppResult.Success ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                item = result.data.toDetailUiModel(),
                                isEmpty = false,
                            )
                        }
                    is AppResult.Error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.error.userMessage(),
                            )
                        }
                }
            }
        }
    }
