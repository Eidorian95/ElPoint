package com.elpoint.presentation.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.elpoint.presentation.detail.DetailActivity
import com.elpoint.ui.theme.ElPointTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchPointActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ElPointTheme {
                SearchScreen{goToDetailScreen()}
            }
        }
    }

    private fun goToDetailScreen(){
        val intent = Intent(this, DetailActivity::class.java)
        startActivity(intent)
    }
}


