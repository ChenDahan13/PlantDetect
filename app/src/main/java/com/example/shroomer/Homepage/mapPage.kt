package com.example.shroomer.Homepage

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shroomer.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.shroomer.Entities.MarkerClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener

class mapPage : Fragment(), OnMapReadyCallback {

    private var mGoogleMap: GoogleMap? = null
    private lateinit var autocompleteFragment: AutocompleteSupportFragment
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReferenceMarker: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_map_page, container, false)

        // Create the database reference
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReferenceMarker = firebaseDatabase.reference.child("Marker")

        // Initialize the autocomplete support fragment
        Places.initialize(requireContext(), getString(R.string.google_map_api_key))
        autocompleteFragment = childFragmentManager
            .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.ADDRESS)) // Specify the fields to return
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: com.google.android.gms.common.api.Status) {
                Toast.makeText(context, "Error in the search", Toast.LENGTH_SHORT).show()
            }
            override fun onPlaceSelected(place: Place) {
                // val add = place.address
                // val id = place.id
                val latLng = place.latLng!!
                zoomOnMap(latLng)

            }
        })

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map_Fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val mapOptionButton: ImageButton = view.findViewById(R.id.mapOptionsMenu)
        // Create a popup menu
        val popupMenu = PopupMenu(this.context, mapOptionButton)
        // Inflate the menu
        popupMenu.menuInflater.inflate(R.menu.map_options, popupMenu.menu)
        // Set a click listener for the popup menu
        popupMenu.setOnMenuItemClickListener { menuItem ->
            changeMapType(menuItem.itemId)
            true
        }

        mapOptionButton.setOnClickListener {
            popupMenu.show()
        }
        return view
    }

    private fun changeMapType(itemId: Int) {
        when (itemId) {
            R.id.normal_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
            R.id.hybrid_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
            R.id.satellite_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
            R.id.terrain_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        // Load saved markers
        // loadMarkers()

        // Add a marker
        mGoogleMap?.setOnMapClickListener {
            showMarkerInputDialog(it)
        }
   }

//    // Load markers from Firebase
//    private fun loadMarkers() {
//        databaseReferenceMarker.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (markerSnapshot in snapshot.children) {
//                    val marker = markerSnapshot.getValue(MarkerClass::class.java)
//                    if (marker != null) {
//                        val title = marker.getTitle()
//                        val lat = marker.getLatitude()
//                        val lng = marker.getLongitude()
//                        Toast.makeText(context, "title: $title", Toast.LENGTH_SHORT).show()
//                        Toast.makeText(context, "lat: $lat", Toast.LENGTH_SHORT).show()
//                        Toast.makeText(context, "lng: $lng", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
//                Toast.makeText(context, "Error loading markers", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

    // Show a dialog to add a marker
    private fun showMarkerInputDialog(position: com.google.android.gms.maps.model.LatLng) {
        val inputDialog = AlertDialog.Builder(requireContext())
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.hint = "Enter type of plant"
        inputDialog.setView(input)
        inputDialog.setPositiveButton("OK") { dialog, _ ->
            val title = input.text.toString()
            addSimpleMarker(title, position)
            dialog.dismiss()
        }
        inputDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        inputDialog.show()
    }

    // Save marker to Firebase
    private fun saveMarker(marker: Marker) {
        val lat = marker.position.latitude
        val lng = marker.position.longitude
        val title = marker.title.toString()
        val primaryKey = databaseReferenceMarker.push().key.toString() // Generate a unique key
        val markerData = MarkerClass(primaryKey, title, lat, lng, "user_id")
        databaseReferenceMarker.child(primaryKey).setValue(markerData.toMap()) // Save marker to Firebase
        Toast.makeText(context, "Marker saved", Toast.LENGTH_SHORT).show()
    }

    // Zoom on the map
    private fun zoomOnMap(latLng: com.google.android.gms.maps.model.LatLng) {
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(latLng, 15f) //
        mGoogleMap?.animateCamera(newLatLngZoom)
    }

    // Add a simple marker without saving it to Firebase
    private fun addSimpleMarkerWithoutSave(title: String, position: com.google.android.gms.maps.model.LatLng) {
        mGoogleMap?.addMarker(
            com.google.android.gms.maps.model.MarkerOptions()
                .position(position)
                .title(title)
        )
    }

    // Add a simple marker
    private fun addSimpleMarker(title: String, position: com.google.android.gms.maps.model.LatLng) {
        Toast.makeText(context, "title before: $title", Toast.LENGTH_SHORT).show()

        val marker = mGoogleMap?.addMarker(
            com.google.android.gms.maps.model.MarkerOptions()
                .position(position)
                .title(title)
        )
        // Save marker data
        marker?.let { saveMarker(it) }
    }
}