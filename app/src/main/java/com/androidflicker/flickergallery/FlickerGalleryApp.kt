package com.androidflicker.flickergallery

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FlickerGalleryApp :
    Application(),
    ImageLoaderFactory {
    // Lazy<T> avoids the Hilt injection timing issue — get() is called
    // only after onCreate() completes, well after member injection runs.
    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    override fun newImageLoader(): ImageLoader = imageLoader.get()
}
