package com.jnyakush.rickymorty

import android.app.Application
import com.jnyakush.rickymorty.di.apiModules
import com.jnyakush.rickymorty.di.networkingModules
import com.jnyakush.rickymorty.di.repositoryModule
import com.jnyakush.rickymorty.di.viewModelModule
import com.jnyakush.rickymorty.util.CrashlyticsTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class RickyMorty : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
        initTimber()
    }

    // Koin
    private fun initKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@RickyMorty)
            modules(
                repositoryModule,
                viewModelModule,
                networkingModules,
                apiModules,
            )
        }
    }

    // Timber
    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return super.createStackElementTag(element) + ":" + element.lineNumber
                }
            })
        } else {
            Timber.plant(CrashlyticsTree())
        }
    }
}