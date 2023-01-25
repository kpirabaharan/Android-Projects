package com.example.maps

import android.Manifest
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat

import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.maps.databinding.ActivityMapsBinding
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.tasks.OnFailureListener
import java.lang.Exception
import java.security.acl.Permission

const val GEOFENCE_ID = "GEOFENCE_ID"
const val TAG = "MapsActivity"
const val GEOFENCE_RADIUS = 200.0
const val CAMERA_ZOOM_LEVEL = 17f
const val LOCATION_ACCESS_REQUEST_CODE = 10001

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var geofenceHelper: GeofenceHelper

    var circle: Circle?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        geofencingClient = LocationServices.getGeofencingClient(this)
        geofenceHelper = GeofenceHelper(this)

        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        // Add a marker in Sydney and move the camera
        val Home = LatLng(43.881868, -79.054884)
        with(map){
            //addMarker(MarkerOptions().position(Home).title("Marker at Home"))
            moveCamera(CameraUpdateFactory.newLatLngZoom(Home, CAMERA_ZOOM_LEVEL))
        }
        //addCircle(map)
        enableUserLocation()
        map.setOnMapLongClickListener(this)
    }

    private fun enableUserLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            ==PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
            ==PackageManager.PERMISSION_GRANTED){
            map.isMyLocationEnabled = true
        }
        else{
            // Ask for Permission
                // FIX, ASK FOR PERMISSIONS BETTER, NO DIALOG SHOWING
                var permissions = mutableListOf<String>()
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                // We need to show user a dialog for displaying why the permission is needed and then
            // ask for the permission
                ActivityCompat.requestPermissions(this, permissions.toTypedArray(),
                    LOCATION_ACCESS_REQUEST_CODE)
            }else {
                ActivityCompat.requestPermissions(this, permissions.toTypedArray(),
                    LOCATION_ACCESS_REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == LOCATION_ACCESS_REQUEST_CODE){
            if(grantResults.isNotEmpty() &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // We have the permission
                map.isMyLocationEnabled = true
            }else{
                // We do not have the permission
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onMapLongClick(latLng: LatLng) {
        map.clear()
        addMarker(latLng)
        addCircle(latLng, GEOFENCE_RADIUS)
        addGeofence(latLng, GEOFENCE_RADIUS.toFloat())
    }

    // Add geofence
    private fun addGeofence(latLng: LatLng, radius: Float){
        val geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_EXIT) // How do I pass multiple transitions?
        val geofencingRequest = geofenceHelper.getGeofencingRequest(geofence)
        Log.d(TAG, "Checkpoint 1")
        val pendingIntent = geofenceHelper.getPendingIntent()
        Log.d(TAG, "Checkpoint 4")

        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
            .addOnSuccessListener {
                Log.d(TAG, "onSuccess: Geofence Added...")
            }
            .addOnFailureListener { e:Exception ->
                val errorMessage = geofenceHelper.getErrorString(e)
                Log.d(TAG, "Failed to make Geofence")
                Log.d(TAG, "onFailure$errorMessage")
            }
    }

    private fun addMarker(latLng: LatLng){
        map.addMarker(MarkerOptions().position(latLng).title("Marker"))
    }
    private fun addCircle(latLng: LatLng, radius: Double){
        circle = map.addCircle(
            CircleOptions()
                .center(latLng)
                .radius(radius)
                .strokeColor(Color.argb(255, 255, 0, 0))
                .fillColor(Color.argb(64, 255, 0, 0))
        )
    }
}