package com.geeklabs.myscanner

import android.app.Application
import com.geeklabs.myscanner.di.components.ApplicationComponent
import com.geeklabs.myscanner.di.components.DaggerApplicationComponent
import com.geeklabs.myscanner.di.modules.ApplicationModule
import com.geeklabs.myscanner.di.modules.DbModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.reactivex.plugins.RxJavaPlugins
import javax.inject.Inject


class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        applicationComponent.inject(this)
        RxJavaPlugins.setErrorHandler {
            it.printStackTrace()
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingServiceInjector
    }

    val applicationComponent: ApplicationComponent by lazy<ApplicationComponent>(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .dbModule(DbModule(this))
            .build()
    }
}