package com.elpoint.presentation.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elpoint.presentation.search.ui.theme.ElPointTheme
import com.google.maps.android.compose.GoogleMap

class SearchPointActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ElPointTheme {
                Surface {
                    PointMap()
                }
            }
        }
    }
}

@Composable
internal fun PointMap() {
    var isMapLoaded by remember { mutableStateOf(false) }

    Box( modifier = Modifier
        .fillMaxSize()
    ) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            onMapLoaded = { isMapLoaded = true }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ElPointTheme {
        PointMap()
    }
}