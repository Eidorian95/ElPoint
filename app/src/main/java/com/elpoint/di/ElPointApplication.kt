package com.elpoint.di

import android.app.Application
import com.elpoint.R
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ElPointApp : Application(){
    override fun onCreate() {
        super.onCreate()
        val key = "AIzaSyA3MHrNUvuuGvu-Rmh-BRxtCONqhep7XVM"
        Places.initialize(applicationContext, key)
    }
}