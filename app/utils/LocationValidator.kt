package com.ai.franchise.utils

import android.location.Location

object LocationValidator {

    // Franchise Location (Mock: Example Fixed Coordinates)
    private const val OUTLET_LAT = 28.6139
    private const val OUTLET_LONG = 77.2090
    private const val ALLOWED_RADIUS_METERS = 100.0

    fun isWithinRadius(currentLat: Double, currentLong: Double): Boolean {
        val outletLoc = Location("outlet").apply {
            latitude = OUTLET_LAT
            longitude = OUTLET_LONG
        }
        
        val currentLoc = Location("current").apply {
            latitude = currentLat
            longitude = currentLong
        }
        
        val distance = currentLoc.distanceTo(outletLoc)
        return distance <= ALLOWED_RADIUS_METERS
    }
}
