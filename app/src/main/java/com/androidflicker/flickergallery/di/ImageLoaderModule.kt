package com.androidflicker.flickergallery.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import com.androidflicker.flickergallery.core.config.EnvironmentConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

private const val MEMORY_CACHE_SIZE_PERCENT = 0.25
private const val DISK_CACHE_MAX_BYTES = 100L * 1024 * 1024

@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {
    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient,
    ): ImageLoader =
        ImageLoader
            .Builder(context)
            .memoryCache {
                MemoryCache
                    .Builder(context)
                    .maxSizePercent(MEMORY_CACHE_SIZE_PERCENT)
                    .build()
            }.diskCache {
                DiskCache
                    .Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizeBytes(DISK_CACHE_MAX_BYTES)
                    .build()
            }.okHttpClient(okHttpClient)
            .crossfade(true)
            .apply {
                if (EnvironmentConfig.enableLogging) logger(DebugLogger())
            }.build()
}
