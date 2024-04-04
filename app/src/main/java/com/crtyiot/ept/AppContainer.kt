package com.crtyiot.ept

import android.content.Context
import android.app.Application
import com.crtyiot.ept.ui.viewModel.bbbViewModel

// Build application container after define Data Layer
// Build application container step 1:
// https://developer.android.com/codelabs/basic-android-kotlin-compose-add-repository#4

// this version commit only for data source from Room, not for Retrofit

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val bbbViewModel: bbbViewModel
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [Repository]
     */
    override val bbbViewModel: bbbViewModel by lazy {
        bbbViewModel()
    }
}
