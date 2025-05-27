package com.elpoint.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.elpoint.presentation.state.DirectionUI
import com.elpoint.presentation.state.HourlyForecastUI
import com.elpoint.presentation.state.PointUiModel
import com.elpoint.presentation.state.UserPointsUiModel
import com.elpoint.presentation.state.WaveDataUI
import com.elpoint.presentation.state.WindDataUI
import com.elpoint.ui.theme.ElPointTheme

private val FallbackCardColor = Color(0xFF71b3e8)
private val GradientBottomColorStart = Color.Transparent
private val GradientBottomColorMid = Color.Black.copy(alpha = 0.3f)
private val GradientBottomColorEnd = Color.Black.copy(alpha = 0.75f)
private val CardGradientBrush = Brush.verticalGradient(
    colors = listOf(GradientBottomColorStart, GradientBottomColorMid, GradientBottomColorEnd)
)
private val TextColorWhiteAlpha90 = Color.White.copy(alpha = 0.9f)
private val SearchBoxShape = RoundedCornerShape(16.dp)
private val CardShape = RoundedCornerShape(12.dp)

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    uiModel: UserPointsUiModel,
    query: String,
    onQueryChange: (String) -> Unit,
    onPointClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSearchBarClick: () -> Unit
) {

    LazyColumn(
        modifier = modifier.background(
            Color.White
        )
    ) {
        item(key = "topAppBar") {
            HomeTopAppBar(
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick
            )
        }

        stickyHeader(key = "stickySearch") {
            SearchBox(query = query, onQueryChange = { onQueryChange }) { onSearchBarClick() }
        }

        items(uiModel.points, key = { "point_${it.id}" }) {
            SurfSpotCard(
                point = it,
                onPointClick = { onPointClick(it.id) }
            )

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
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Red)
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                onClick()
            }
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = placeholderText,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .clip(SearchBoxShape),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
internal fun SurfSpotCard(
    point: PointUiModel,
    onPointClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageRequest = remember(point.imageUrl) {
        ImageRequest.Builder(context)
            .data(point.imageUrl.ifBlank { null })
            .crossfade(true)
            .error(android.R.drawable.ic_menu_gallery) // Ejemplo de placeholder/error
            .build()
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onPointClick),
        shape = CardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (point.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = imageRequest,
                    contentDescription = "Imagen de ${point.name}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FallbackCardColor)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = CardGradientBrush)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = point.name,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    point.currentForecast?.let { forecast ->
                        Text(
                            text = "Olas: ${forecast.waves.direction.cardinal} ${forecast.waves.height}",
                            color = TextColorWhiteAlpha90,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Viento: ${forecast.winds.direction.cardinal} ${forecast.winds.speed}",
                            color = TextColorWhiteAlpha90,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "El Point",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
            }
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Configuración")
            }
        }
    )
}

@Composable
@Preview
private fun HomeScreenPreview() {
    HomeScreen(
        uiModel = UserPointsUiModel(
            points = listOf(
                PointUiModel(
                    id = "spot_001",
                    name = "Mavericks, California",
                    latitude = 37.4903,
                    longitude = -122.5084,
                    imageUrl = "https://images.unsplash.com/photo-1588098860521-6131236f7907?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1974&q=80",
                    alarm = true,
                    currentForecast = HourlyForecastUI(
                        time = "09:00",
                        waves = WaveDataUI(
                            direction = DirectionUI("NNO"),
                            height = "15-20 ft",
                            period = "17s"
                        ),
                        winds = WindDataUI(
                            direction = DirectionUI("N"),
                            speed = "5-10 mph",
                            type = "Light Offshore"
                        )
                    )
                ),
                PointUiModel(
                    id = "spot_002",
                    name = "Playa Grande, Mar del Plata",
                    latitude = -38.0234,
                    longitude = -57.5219,
                    imageUrl = "", // Sin imagen, se usará el color de fallback
                    alarm = false,
                    currentForecast = HourlyForecastUI(
                        time = "14:00",
                        waves = WaveDataUI(
                            direction = DirectionUI("SE"),
                            height = "1-1.5 m",
                            period = "9s"
                        ),
                        winds = WindDataUI(
                            direction = DirectionUI("E"),
                            speed = "15 km/h",
                            type = "Onshore"
                        )
                    )
                ),
                PointUiModel(
                    id = "spot_003",
                    name = "Jeffreys Bay, Sudáfrica",
                    latitude = -34.0489,
                    longitude = 24.9200,
                    imageUrl = "https://images.unsplash.com/photo-1605904434991-35097e0ba5f4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80",
                    alarm = false,
                    currentForecast = null // Sin pronóstico actual
                ),
                PointUiModel(
                    id = "spot_004",
                    name = "The Pass, Byron Bay, Australia",
                    latitude = -28.6399,
                    longitude = 153.6168,
                    imageUrl = "https://images.unsplash.com/photo-1516701614002-769a147270ac?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80",
                    alarm = true,
                    currentForecast = HourlyForecastUI(
                        time = "11:30",
                        waves = WaveDataUI(
                            direction = DirectionUI("NE"),
                            height = "2-3 ft",
                            period = "10s"
                        ),
                        winds = WindDataUI(
                            direction = DirectionUI("NNE"),
                            speed = "8 knots",
                            type = "Light Cross-shore"
                        )
                    )
                ),
                PointUiModel(
                    id = "spot_005",
                    name = "Punta de Lobos, Pichilemu, Chile (Un lugar increíble con olas largas y potentes)",
                    latitude = -34.4058,
                    longitude = -72.0503,
                    imageUrl = "https://images.unsplash.com/photo-1562039943-672941527005?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1932&q=80",
                    alarm = false,
                    currentForecast = HourlyForecastUI(
                        time = "16:00",
                        waves = WaveDataUI(
                            direction = DirectionUI("SO"),
                            height = "3-4 m",
                            period = "14s"
                        ),
                        winds = WindDataUI(
                            direction = DirectionUI("S"),
                            speed = "12-18 km/h",
                            type = "Offshore"
                        )
                    )
                ),
                PointUiModel(
                    id = "spot_006",
                    name = "Secret Spot (No Image, No Forecast)",
                    latitude = 10.1234,
                    longitude = -10.5678,
                    imageUrl = "invalid_url_should_fallback.jpg", // URL inválida para probar fallback
                    alarm = false,
                    currentForecast = null
                )
            )

        ),
        query = "",
        onQueryChange = {},
        onPointClick = {},
        onBackClick = {},
        onSettingsClick = {},
        onSearchBarClick = {}
    )
}

@Composable
@Preview
fun SurfSpotCardPreviewNoImage() {
    val samplePoint = PointUiModel(
        id = "2",
        name = "Mavericks, California",
        latitude = 37.4903,
        longitude = -122.5084,
        imageUrl = "", // URL vacía para probar el fallback
        alarm = true,
        currentForecast =
            HourlyForecastUI(
                time = "08:00",
                waves = WaveDataUI(DirectionUI("NNO"), "1.5m", "18s"),
                winds = WindDataUI(DirectionUI("N"), "5 m/s", "cross-off")
            )
    )
    ElPointTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SurfSpotCard(point = samplePoint, onPointClick = {})
        }
    }
}