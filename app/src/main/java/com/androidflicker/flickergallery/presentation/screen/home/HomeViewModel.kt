package com.androidflicker.flickergallery.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidflicker.flickergallery.domain.usecase.GetHomeCategoriesUseCase
import com.androidflicker.flickergallery.presentation.screen.home.mapper.toCategoryUiModel
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
        private val getHomeCategoriesUseCase: GetHomeCategoriesUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(HomeUiState())
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

        init {
            onEvent(HomeEvent.LoadContent)
        }

        fun onEvent(event: HomeEvent) {
            when (event) {
                HomeEvent.LoadContent, HomeEvent.RetryLoad -> loadCategories()
            }
        }

        fun loadCategories() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null, isEmpty = false) }

                val results = getHomeCategoriesUseCase()
                val categories = results.map { it.toCategoryUiModel() }
                val allFailed = categories.isNotEmpty() && categories.all { it.errorMessage != null }
                val allEmpty = categories.isNotEmpty() && categories.all { it.isEmpty }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        categories = categories,
                        isEmpty = allEmpty,
                        errorMessage = if (allFailed) categories.first().errorMessage else null,
                    )
                }
            }
        }
    }
