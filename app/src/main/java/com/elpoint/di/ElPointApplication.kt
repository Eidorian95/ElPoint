package com.elpoint.di

import android.app.Application
import android.util.Log
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ElPointApp : Application(){
    override fun onCreate() {
        super.onCreate()
        val key = "AIzaSyA3MHrNUvuuGvu-Rmh-BRxtCONqhep7XVM"
        signInAnonymously()
        Places.initialize(applicationContext, key)
    }

    private fun signInAnonymously() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Auth", "Inicio de sesión anónimo exitoso.")
                    } else {
                        Log.w("Auth", "Falló el inicio de sesión anónimo.", task.exception)
                    }
                }
        }
    }
}