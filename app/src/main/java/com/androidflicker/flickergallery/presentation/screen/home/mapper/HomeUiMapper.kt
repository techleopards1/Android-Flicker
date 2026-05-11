package com.androidflicker.flickergallery.presentation.screen.home.mapper

import com.androidflicker.flickergallery.core.error.userMessage
import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.model.HomeCategory
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.presentation.screen.home.HomeCategoryUiModel
import com.androidflicker.flickergallery.presentation.screen.home.HomeItemUiModel

fun Photo.toHomeItemUiModel() =
    HomeItemUiModel(
        id = id,
        title = title,
        imageUrl = imageUrl,
        subtitle = owner,
    )

fun AppResult<List<Photo>>.toHomeItemUiModels(): List<HomeItemUiModel> =
    (this as? AppResult.Success)?.data?.map { it.toHomeItemUiModel() } ?: emptyList()

fun Pair<HomeCategory, AppResult<List<Photo>>>.toCategoryUiModel(): HomeCategoryUiModel {
    val (category, result) = this
    val items = result.toHomeItemUiModels()
    return HomeCategoryUiModel(
        id = category.id,
        title = category.displayTitle,
        items = items,
        isEmpty = result is AppResult.Success && items.isEmpty(),
        errorMessage = (result as? AppResult.Error)?.error?.userMessage(),
    )
}
