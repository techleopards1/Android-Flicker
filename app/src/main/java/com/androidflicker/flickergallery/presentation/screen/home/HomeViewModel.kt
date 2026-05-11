package com.androidflicker.flickergallery.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidflicker.flickergallery.core.error.userMessage
import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.domain.usecase.GetPopularPhotosUseCase
import com.androidflicker.flickergallery.domain.usecase.GetRecentPhotosUseCase
import com.androidflicker.flickergallery.domain.usecase.SearchPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TRENDING_QUERY = "nature"

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getRecentPhotosUseCase: GetRecentPhotosUseCase,
        private val getPopularPhotosUseCase: GetPopularPhotosUseCase,
        private val searchPhotosUseCase: SearchPhotosUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(HomeUiState())
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

        init {
            onEvent(HomeEvent.LoadContent)
        }

        fun onEvent(event: HomeEvent) {
            when (event) {
                HomeEvent.LoadContent, HomeEvent.RetryLoad -> loadContent()
            }
        }

        private fun loadContent() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val popularDeferred = async { getPopularPhotosUseCase() }
                val recentDeferred = async { getRecentPhotosUseCase() }
                val trendingDeferred = async { searchPhotosUseCase(TRENDING_QUERY) }

                val popular = popularDeferred.await()
                val recent = recentDeferred.await()
                val trending = trendingDeferred.await()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        popularItems = popular.toUiModels(),
                        recentItems = recent.toUiModels(),
                        trendingItems = trending.toUiModels(),
                        errorMessage =
                            if (popular is AppResult.Error && recent is AppResult.Error) {
                                popular.error.userMessage()
                            } else {
                                null
                            },
                    )
                }
            }
        }
    }

private fun AppResult<List<Photo>>.toUiModels(): List<HomeItemUiModel> =
    (this as? AppResult.Success)?.data?.map { it.toUiModel() } ?: emptyList()

private fun Photo.toUiModel() =
    HomeItemUiModel(
        id = id,
        title = title,
        imageUrl = imageUrl,
        subtitle = owner,
    )
