package com.geeklabs.myscanner.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.geeklabs.myscanner.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject

@Module
class ApplicationModule(private val application: Application) {

    @ApplicationScope
    @Provides
    fun providesApplicationContext(): Context = application.applicationContext

    @ApplicationScope
    @Provides
    fun providesSharedpreferences(): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application)

    @ApplicationScope
    @Provides
    fun providerDisplayMetrics() = application.resources.displayMetrics

    @ApplicationScope
    @Provides
    fun providerPublishSubject(): PublishSubject<Any> = PublishSubject.create<Any>()
}
