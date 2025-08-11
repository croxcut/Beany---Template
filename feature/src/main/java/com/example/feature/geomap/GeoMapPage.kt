package com.example.feature.geomap

import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun GeoMapPage(
    viewModel: GeoMapViewModel
) {
    val context = LocalContext.current
    var totalAcres by remember { mutableStateOf(0.0) }
    var selectedType by remember { mutableStateOf(MapType.DEFAULT) }

    LaunchedEffect(Unit) {
        setupOSM(context)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            factory = { ctx ->
                MapView(ctx).apply {
                    // Set this before setting tile source
                    Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

                    setMultiTouchControls(true)
                    controller.setZoom(6.0)
                    controller.setCenter(GeoPoint(12.8797, 121.7740))

                    // Set the initial tile source
                    setTileSource(TileSourceFactory.MAPNIK)

                    val philippinesBounds = BoundingBox(
                        21.1207, 126.6045,
                        4.2153, 116.9315
                    )
                    setScrollableAreaLimitDouble(philippinesBounds)
                    minZoomLevel = 5.0
                    maxZoomLevel = 18.0

                    viewModel.calculateAndAddPoints(this, viewModel.manilaPoints, 50.0)
                    totalAcres = viewModel.getTotalAcres
                }
            },
            update = { mapView ->
                viewModel.setMapType(mapView, selectedType)
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
    }
}

private fun setupOSM(context: Context) {
    Configuration.getInstance().userAgentValue = context.packageName
    Configuration.getInstance().osmdroidBasePath = context.cacheDir.resolve("osmdroid")
    Configuration.getInstance().osmdroidTileCache = context.cacheDir.resolve("osmdroid/tiles")
}