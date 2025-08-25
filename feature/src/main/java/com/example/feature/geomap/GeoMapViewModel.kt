// ===============================================================================
//
// Copyright (C) 2025-2026 by John Paul Valenzuela
//
// This source is available for distribution and/or modification
// only under the terms of the Beany Source Code License as
// published by Beany. All rights reserved.
//
// The source is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// FITNESS FOR A PARTICULAR PURPOSE. See the Beany Source Code License
// for more details.
//
// ===============================================================================

package com.example.feature.geomap

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon
import javax.inject.Inject
import kotlin.math.PI

class GeoMapViewModel @Inject constructor(

): ViewModel() {

    private var _totalAcres: Double = 0.0
    val getTotalAcres: Double
        get() = _totalAcres

    val manilaPoints = listOf(
        GeoPoint(14.5896, 120.9810), GeoPoint(14.5880, 120.9749), GeoPoint(14.6116, 120.9818),
        GeoPoint(14.5994, 120.9842), GeoPoint(14.5740, 120.9822), GeoPoint(14.6080, 120.9782),
        GeoPoint(14.5721, 120.9933), GeoPoint(14.6019, 120.9733), GeoPoint(14.5823, 120.9785),
        GeoPoint(14.5952, 120.9901), GeoPoint(14.6033, 120.9890), GeoPoint(14.5798, 120.9756),
        GeoPoint(14.5874, 120.9837), GeoPoint(14.5921, 120.9724), GeoPoint(14.6067, 120.9755),
        GeoPoint(14.5765, 120.9872), GeoPoint(14.5988, 120.9773), GeoPoint(14.5849, 120.9918),
        GeoPoint(14.6092, 120.9830), GeoPoint(14.5937, 120.9856), GeoPoint(14.5812, 120.9803),
        GeoPoint(14.5975, 120.9829), GeoPoint(14.5860, 120.9768), GeoPoint(14.6024, 120.9805),
        GeoPoint(14.5908, 120.9887), GeoPoint(14.5783, 120.9841), GeoPoint(14.6049, 120.9863),
        GeoPoint(14.5756, 120.9794), GeoPoint(14.5915, 120.9742), GeoPoint(14.6076, 120.9811),
        GeoPoint(14.5854, 120.9892), GeoPoint(14.5948, 120.9780), GeoPoint(14.5806, 120.9779),
        GeoPoint(14.5967, 120.9848), GeoPoint(14.5837, 120.9826), GeoPoint(14.6005, 120.9759),
        GeoPoint(14.5890, 120.9854), GeoPoint(14.5774, 120.9815), GeoPoint(14.6058, 120.9847),
        GeoPoint(14.5739, 120.9763), GeoPoint(14.5929, 120.9798), GeoPoint(14.6088, 120.9796),
        GeoPoint(14.5843, 120.9780), GeoPoint(14.5959, 120.9815), GeoPoint(14.5791, 120.9832),
        GeoPoint(14.5978, 120.9787), GeoPoint(14.5826, 120.9859), GeoPoint(14.6012, 120.9824),
        GeoPoint(14.5887, 120.9801), GeoPoint(14.5768, 120.9780), GeoPoint(14.6062, 120.9778),
        GeoPoint(14.5747, 120.9849), GeoPoint(14.5933, 120.9761), GeoPoint(14.6097, 120.9825),
        GeoPoint(14.5858, 120.9830), GeoPoint(14.5964, 120.9873), GeoPoint(14.5800, 120.9797),
        GeoPoint(14.5985, 120.9809), GeoPoint(14.5831, 120.9874), GeoPoint(14.6020, 120.9789),
        GeoPoint(14.5902, 120.9826), GeoPoint(14.5779, 120.9858), GeoPoint(14.6051, 120.9859),
        GeoPoint(14.5732, 120.9775), GeoPoint(14.5925, 120.9749), GeoPoint(14.6083, 120.9804),
        GeoPoint(14.5846, 120.9807), GeoPoint(14.5955, 120.9832), GeoPoint(14.5794, 120.9819),
        GeoPoint(14.5971, 120.9765), GeoPoint(14.5820, 120.9843), GeoPoint(14.6008, 120.9816),
        GeoPoint(14.5893, 120.9792), GeoPoint(14.5761, 120.9826), GeoPoint(14.6069, 120.9838),
        GeoPoint(14.5744, 120.9788), GeoPoint(14.5939, 120.9773), GeoPoint(14.6101, 120.9787),
        GeoPoint(14.5867, 120.9814), GeoPoint(14.5960, 120.9855), GeoPoint(14.5803, 120.9782),
        GeoPoint(14.5982, 120.9837), GeoPoint(14.5834, 120.9865), GeoPoint(14.6015, 120.9775),
        GeoPoint(14.5905, 120.9849), GeoPoint(14.5776, 120.9802), GeoPoint(14.6054, 120.9820),
        GeoPoint(14.5735, 120.9835), GeoPoint(14.5928, 120.9756), GeoPoint(14.6085, 120.9841),
        GeoPoint(14.5851, 120.9821), GeoPoint(14.5958, 120.9790), GeoPoint(14.5797, 120.9850),
        GeoPoint(14.5974, 120.9818), GeoPoint(14.5823, 120.9880), GeoPoint(14.6003, 120.9831),
        GeoPoint(14.5899, 120.9778), GeoPoint(14.5764, 120.9839), GeoPoint(14.6065, 120.9800),
        GeoPoint(14.5740, 120.9805), GeoPoint(14.5936, 120.9787), GeoPoint(14.6099, 120.9813),
        GeoPoint(14.5864, 120.9848), GeoPoint(14.5963, 120.9824), GeoPoint(14.5809, 120.9771),
        GeoPoint(14.5988, 120.9795), GeoPoint(14.5838, 120.9839), GeoPoint(14.6017, 120.9846),
        GeoPoint(14.5900, 120.9862), GeoPoint(14.5772, 120.9818), GeoPoint(14.6057, 120.9783),
        GeoPoint(14.5738, 120.9854), GeoPoint(14.5922, 120.9768), GeoPoint(14.6081, 120.9833),
        GeoPoint(14.5855, 120.9810), GeoPoint(14.5951, 120.9841), GeoPoint(14.5790, 120.9824),
        GeoPoint(14.5977, 120.9776), GeoPoint(14.5829, 120.9852), GeoPoint(14.6006, 120.9802),
        GeoPoint(14.5896, 120.9785), GeoPoint(14.5759, 120.9844), GeoPoint(14.6060, 120.9817),
        GeoPoint(14.5743, 120.9770), GeoPoint(14.5930, 120.9793), GeoPoint(14.6103, 120.9809),
        GeoPoint(14.5869, 120.9833), GeoPoint(14.5967, 120.9807), GeoPoint(14.5806, 120.9847),
        GeoPoint(14.5985, 120.9820), GeoPoint(14.5832, 120.9818), GeoPoint(14.6010, 120.9853),
        GeoPoint(14.5908, 120.9759), GeoPoint(14.5767, 120.9809), GeoPoint(14.6052, 120.9836),
        GeoPoint(14.5731, 120.9820), GeoPoint(14.5924, 120.9779), GeoPoint(14.6087, 120.9774),
        GeoPoint(14.5853, 120.9855), GeoPoint(14.5954, 120.9783), GeoPoint(14.5793, 120.9838),
        GeoPoint(14.5970, 120.9840), GeoPoint(14.5821, 120.9827), GeoPoint(14.6001, 120.9780),
        GeoPoint(14.5892, 120.9871), GeoPoint(14.5760, 120.9812), GeoPoint(14.6068, 120.9850),
        GeoPoint(14.5737, 120.9799), GeoPoint(14.5938, 120.9764), GeoPoint(14.6100, 120.9839)
    )

    fun setMapType(mapView: MapView, mapType: MapType) {
        try {
            when (mapType) {
                MapType.DEFAULT -> mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
                MapType.SATELLITE -> mapView.setTileSource(TileSourceFactory.USGS_SAT)
                MapType.ROADS -> mapView.setTileSource(TileSourceFactory.USGS_TOPO)
                MapType.TRAVEL -> mapView.setTileSource(TileSourceFactory.HIKEBIKEMAP)
            }

            mapView.invalidate()


        } catch (e: Exception) {
            Log.e("GeoMap", "Error setting map type", e)

            mapView.setTileSource(TileSourceFactory.MAPNIK)
        }
    }

    fun calculateAndAddPoints(
        mapVew: MapView,
        points: List<GeoPoint>,
        radius: Double
    ) {
        var totalAreaSqMeters = 0.0
        points.forEach { p ->
            var circle = Polygon(mapVew).apply {
                setPoints(Polygon.pointsAsCircle(p, radius))
                outlinePaint.color = android.graphics.Color.RED
                outlinePaint.strokeWidth = 1f
                fillColor = android.graphics.Color.argb(255, 255, 0,0)
            }
            mapVew.overlays.add(circle)
            totalAreaSqMeters += PI * radius * radius
        }

        _totalAcres = totalAreaSqMeters / 4046.856
    }

}

enum class MapType {
    DEFAULT,
    SATELLITE,
    ROADS,
    TRAVEL
}