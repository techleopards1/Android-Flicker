package com.androidflicker.flickergallery.domain.usecase

import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.model.HomeCategory
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.domain.repository.PhotoRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetHomeCategoriesUseCase
    @Inject
    constructor(
        private val repository: PhotoRepository,
    ) {
        suspend operator fun invoke(): List<Pair<HomeCategory, AppResult<List<Photo>>>> =
            coroutineScope {
                HomeCategory.entries
                    .map { category ->
                        async {
                            val result =
                                when (category) {
                                    HomeCategory.POPULAR -> repository.getPopularPhotos()
                                    HomeCategory.RECENT -> repository.getRecentPhotos()
                                    else -> repository.searchPhotos(category.id)
                                }
                            category to result
                        }
                    }.map { it.await() }
            }
    }
