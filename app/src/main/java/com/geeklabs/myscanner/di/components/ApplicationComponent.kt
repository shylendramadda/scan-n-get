package com.geeklabs.myscanner.di.components

import com.geeklabs.myscanner.di.scope.ApplicationScope
import com.geeklabs.myscanner.App
import com.geeklabs.myscanner.ui.MainActivity
import com.geeklabs.myscanner.ui.SplashActivity
import com.geeklabs.myscanner.di.modules.ApplicationModule
import com.geeklabs.myscanner.di.modules.DbModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@ApplicationScope
@Component(modules = [AndroidSupportInjectionModule::class, ApplicationModule::class, DbModule::class])
interface ApplicationComponent {
    fun inject(app: App)
    fun inject(activity: SplashActivity)
    fun inject(activity: MainActivity)
}