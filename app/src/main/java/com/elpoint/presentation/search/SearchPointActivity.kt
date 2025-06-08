package com.elpoint.presentation.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elpoint.ui.theme.ElPointTheme
import com.google.android.gms.maps.GoogleMapOptions
import com.google.maps.android.compose.GoogleMap

private val SearchBoxShape = RoundedCornerShape(16.dp)


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

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PointMap() {
    var isMapLoaded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        stickyHeader {
            SearchBox(query = "", onQueryChange = {}) { }
        }
        item {
            GoogleMap(
                modifier = Modifier
                    .height(height = 200.dp)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                   /* .drawBehind{
                        drawRoundRect(
                            color = Color.LightGray
                        )
                    },*/
                onMapLoaded = { isMapLoaded = true },
                googleMapOptionsFactory = {
                    GoogleMapOptions().mapId("af43105a3bd1a3c3c025bb1a")
                }
            )

        }

        items(count = 20, key = { "item_$it" }, contentType = { it }) {
            Text(modifier = Modifier.padding(top = 16.dp), text = "Item $it")
        }

    }
}

@Composable
private fun SearchBox(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onClick: () -> Unit

) {
    val placeholderText = @Composable { Text("Buscar...") }

    Box(
        Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .drawBehind {
                drawRoundRect(
                    color = Color.Transparent,
                    cornerRadius = CornerRadius(10.dp.toPx())
                )
            }) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = placeholderText,
            singleLine = true,
            readOnly = true,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .clip(SearchBoxShape),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            imageVector = Icons.Default.Search,
            contentDescription = "Buscar"
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