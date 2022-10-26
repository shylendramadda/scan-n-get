package com.geeklabs.myscanner.di.modules

import android.app.Application
import com.geeklabs.myscanner.di.scope.ApplicationScope
import com.geeklabs.myscanner.local.AppDatabaseWrapper
import dagger.Module
import dagger.Provides

@Module
class DbModule(private val application: Application) {

    @ApplicationScope
    @Provides
    fun providesAppDatabaseWrapper() = AppDatabaseWrapper(application.applicationContext)
}
