package com.example.beta.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.beta.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.ConnectionResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.location.LocationManager
import androidx.core.content.ContextCompat.getSystemService
import android.content.DialogInterface
import android.content.Context
import android.location.Location
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Constraints
import androidx.constraintlayout.widget.Constraints.TAG
import com.example.beta.data.ExampleCourses
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.internal.PolylineEncoding
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import kotlinx.android.synthetic.main.fragment_map_menu.*
import org.slf4j.Marker


class MapMenuFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationClickListener,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnInfoWindowClickListener {

    private val PERMISSIONS_REQUEST_ENABLE_GPS = 2
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3

    private var mLocationPermissionDenied = false
    private lateinit var mMapView: MapView
    private lateinit var gMap: GoogleMap
    private lateinit var inicialBound: LatLngBounds
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mGeoApiContext: GeoApiContext? = null
    private var displayCourse: Boolean? = null

    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_map_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var mapViewBundle: Bundle? = null

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

        mMapView = view.findViewById(R.id.appMapMenu)
        mMapView.onCreate(mapViewBundle)

        mMapView.getMapAsync(this)

        if(mGeoApiContext == null){

            mGeoApiContext = GeoApiContext.Builder().apiKey("AIzaSyDNHrmWt21kUBGfx3AaBRK01cHcq_fDIyk").build()
        }

        framgent_map_menu_reset_map.setOnClickListener { resetMap() }

        //displayCourse = arguments?.getBoolean("display")
        displayCourse = true
    }

     override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)

        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mMapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mMapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView.onStop()
    }

    override fun onMapReady(map: GoogleMap) {

        gMap = map

        if(isMapsEnabled()) {

            enableMyLocation()
            gMap.setOnMyLocationButtonClickListener(this)
            gMap.setOnMyLocationClickListener(this)
            gMap.setOnInfoWindowClickListener(this)
        }

        if(displayCourse == null){

            getLastKnownLocationCameraView()
        }else{
            displayCourse()
        }
    }

    private fun getLastKnownLocationCameraView(){

        if (ContextCompat.checkSelfPermission(
                context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.lastLocation.addOnCompleteListener {

                if(it.isSuccessful) {
                    setCameraViewInitial(it.result!!.latitude, it.result!!.longitude)
                }
            }
        }
    }

    private fun setCameraViewInitial(latitude: Double, longitude:Double){

        val bottomBoundary = latitude - .01
        val leftBoundary = longitude -.01
        val topBoundary = latitude + .01
        val rightBoundary = longitude + .01

        inicialBound = LatLngBounds(LatLng(bottomBoundary, leftBoundary), LatLng(topBoundary, rightBoundary))

        gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(inicialBound, 0))
    }

    private fun enableMyLocation(){

        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            gMap.isMyLocationEnabled = true

        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(context, "Current location:\n" + p0, Toast.LENGTH_LONG).show()
    }

    override fun onMyLocationButtonClick(): Boolean {

        Toast.makeText(context, "MyLocation button clicked", Toast.LENGTH_SHORT).show();

        return false
    }

    override fun onPause() {
        mMapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mMapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(activity!!)
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS)
            }
        val alert = builder.create()
        alert.show()
    }

    private fun isMapsEnabled(): Boolean {

        val manager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation()
                    refresh()
                }else{
                    mLocationPermissionDenied = true
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: called.")

        refresh()
    }

    fun refresh(){

        val frag = fragmentManager!!.beginTransaction()
        if (Build.VERSION.SDK_INT >= 26) frag.setReorderingAllowed(false)
        frag.detach(this).attach(this).commit()
    }

    private fun resetMap(){
        gMap.clear()
    }

    fun displayCourse(){

        resetMap()

        val example = ExampleCourses().getCourseCoordinates()

        calculateDirections(example)
        addCourseMarkers(example)
    }

    private fun calculateDirections(example: List<LatLng>){

        Log.d(TAG, "started to calculate")

        val max = example.size - 1

        Log.d(TAG, "max: ${max}")

        val destination = com.google.maps.model.LatLng(example[max].latitude, example[max].longitude)
        val directions =  DirectionsApiRequest(mGeoApiContext)

        directions.mode(TravelMode.WALKING)
        directions.origin(com.google.maps.model.LatLng(example[0].latitude, example[0].longitude))

        var i = 1
        while(i < max){
            directions.waypoints(com.google.maps.model.LatLng(example[i].latitude, example[i].longitude))
            Log.d(TAG, "coord: ${example[i].latitude} , ${example[i].longitude}")
            i++
        }

        Log.d(TAG, "calculateDirections: destination: $destination")

        directions.destination(destination).setCallback( object :PendingResult.Callback<DirectionsResult>{

            override fun onFailure(e: Throwable?) {
                Log.d(TAG, "failed")
            }

            override fun onResult(result: DirectionsResult?) {
                if(result != null) addPolylinesToMap(result)
                Log.d(TAG, "finished calculating")
            }
        })
    }

    fun addPolylinesToMap(result: DirectionsResult){
      Handler(Looper.getMainLooper()).post{

          run {
              Log.d(TAG, "run: result routes: " + result.routes.size);

              for(route in result.routes){
                  Log.d(TAG, "run: leg: " + route.legs[0].toString());
                  val  decodedPath: List<com.google.maps.model.LatLng> = PolylineEncoding.decode(route.overviewPolyline.encodedPath)

                  val newDecodedPath: ArrayList<LatLng> =  arrayListOf()

                  // This loops through all the LatLng coordinates of ONE polyline.
                  for(latLng in decodedPath){

                      newDecodedPath.add(LatLng(latLng.lat, latLng.lng))
                  }

                  val polyline: Polyline = gMap.addPolyline(PolylineOptions().addAll(newDecodedPath));
                  polyline.color = ContextCompat.getColor(context!!, R.color.darkGrey)

                  //addCourseMarkers(polyline.points)

                  zoomRoute(polyline.points)
              }
          }
      }
    }

    fun addCourseMarkers(example: List<LatLng>){

        for(i in example) {
            val marker = gMap.addMarker(
                MarkerOptions()
                    .position(i)
                    .title("Coords: (${i.latitude}, ${i.longitude})")
                    .snippet("Click this window if you're near the activity")
            )
            marker.showInfoWindow()
        }
    }

    override fun onInfoWindowClick(p0: com.google.android.gms.maps.model.Marker?) {

        val builder = AlertDialog.Builder(context!!)
        builder.setMessage(p0!!.title)
            .setCancelable(true)
            .setPositiveButton("Yes", DialogInterface.OnClickListener{dialog,_ ->
                checkCloseToCheckPoint()
                dialog.dismiss()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener() {dialog,_ ->
                dialog.cancel()
            })
        val alert = builder.create()
        alert.show()
    }

    fun zoomRoute(lstLatLngRoute: List<LatLng>) {

        if (lstLatLngRoute.isEmpty()) return

        val boundsBuilder = LatLngBounds.Builder();
        for (latLngPoint in lstLatLngRoute) boundsBuilder.include(latLngPoint);
        val routePadding = 120;
        val latLngBounds = boundsBuilder.build();

        gMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
            600,
            null
        )
    }

    fun checkCloseToCheckPoint(){}
}