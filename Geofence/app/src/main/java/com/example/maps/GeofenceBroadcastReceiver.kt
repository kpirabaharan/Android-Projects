package com.example.maps

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val TAG = "GeofenceBroadcastReceiver"

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {

        val notificationHelper = NotificationHelper(context)

        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        if(geofencingEvent.hasError()){
            Log.d(TAG, "onReceive: Error receiving geofence even...")
            return
        }
        var geofenceList: List<Geofence> = geofencingEvent.triggeringGeofences
        //val location = geofencingEvent.triggeringLocation
        val transitionType = geofencingEvent.geofenceTransition
        for(geofence in geofenceList){
            Log.d(TAG, "onReceive" + geofence.requestId)
        }

        // More to be added
        when(transitionType){
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                Toast.makeText(context, "Geofence Transition Enter", Toast.LENGTH_SHORT).show()
                notificationHelper.sendHighPriorityNotification("Geofence Transition Enter", "", MapsActivity::class.java)
            }
            Geofence.GEOFENCE_TRANSITION_DWELL -> {
                Toast.makeText(context, "Geofence Transition Dwell", Toast.LENGTH_SHORT).show()
                notificationHelper.sendHighPriorityNotification("Geofence Transition Dwell", "", MapsActivity::class.java)
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Toast.makeText(context, "Geofence Transition Exit", Toast.LENGTH_SHORT).show()
                notificationHelper.sendHighPriorityNotification("Geofence Transition Exit", "", MapsActivity::class.java)
            }
        }
    }
}