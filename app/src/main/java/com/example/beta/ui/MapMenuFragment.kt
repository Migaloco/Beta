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
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.navigation.Navigation
import com.example.beta.others.ConverterForUI
import com.example.beta.others.StringToDouble
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.internal.PolylineEncoding
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.DirectionsApi
import kotlinx.android.synthetic.main.fragment_map_menu.*


class MapMenuFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationClickListener,
    GoogleMap.OnMyLocationButtonClickListener {

    private val PERMISSIONS_REQUEST_ENABLE_GPS = 2
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3

    private var mLocationPermissionDenied = false
    private lateinit var mMapView: MapView
    private lateinit var gMap: GoogleMap
    private lateinit var inicialBound: LatLngBounds
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mGeoApiContext: GeoApiContext? = null
    private var display: Boolean? = null
    private var startC: Boolean? = null
    private var finish: String? = null
    private var start: String? = null
    private var points: Int = 0
    private var waypoints: ArrayList<String>? = null
    private var myLoc: LatLng? = null

    private val completedPoints: ArrayList<Boolean> = arrayListOf()

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
        fragment_map_button_check.setOnClickListener { getLocationForCheck() }

        fragment_map_button_check.setBackgroundColor(ContextCompat.getColor(context!!, R.color.darkGrey))
        fragment_map_menu_cancel.setBackgroundColor(ContextCompat.getColor(context!!, R.color.darkGrey))
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
        }

        display = arguments?.getBoolean("course")
        startC = arguments?.getBoolean("initiate")

        if(display != null || startC != null){

            resetMap()

            waypoints = arguments?.getStringArrayList("waypoints")
            finish = arguments?.getString("finish")
            start = arguments?.getString("start")

            if(display as Boolean) {

                displayCourse()
            } else{
                startCourse()
            }
        } else{

            getLastKnownLocationCameraView()
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

    private fun getLastKnownLocationNav(){

        if (ContextCompat.checkSelfPermission(
                    context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationClient.lastLocation.addOnCompleteListener {

                if(it.isSuccessful) {
                    myLoc = LatLng(it.result!!.latitude, it.result!!.longitude)
                    calculateDirections()
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
            }.setNegativeButton("No"){dialog,_ ->
                dialog.dismiss()
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

        myLoc = null
        calculateDirections()
    }

    fun startCourse(){

        getLastKnownLocationNav()
    }

    private fun calculateDirections(){

        Log.d(TAG, "started to calculate")

        val directions = DirectionsApiRequest(mGeoApiContext)

        if(myLoc != null){

            waypoints!!.add(0,start!!)
            start = "${myLoc!!.latitude},${myLoc!!.longitude}"
            myLoc = null
        }

        val array = arrayListOf<LatLng>()
        array.addAll(StringToDouble().getStringToListDoubles(start!!))

        for(i in 0 until waypoints!!.size){

            array.addAll(StringToDouble().getStringToListDoubles(waypoints!![i]))
        }

        array.addAll(StringToDouble().getStringToListDoubles(finish!!))

        directions.mode(TravelMode.WALKING)
        directions.origin(start)

        if(display != null && display as Boolean) {

            directions.waypoints(waypoints!![0], waypoints!![1], waypoints!![2])
        }
        else if(startC!= null && startC as Boolean){
            directions.waypoints(waypoints!![0], waypoints!![1], waypoints!![2], waypoints!![3])
        }

        directions.destination(finish).setCallback(object : PendingResult.Callback<DirectionsResult>{

                override fun onFailure(e: Throwable?) {
                    Log.d(TAG, "failed")
                }

                override fun onResult(result: DirectionsResult?) {
                    if(result != null){
                        addCourseMarkers(array)
                        addPolylinesToMap(result)
                    }
                    Log.d(TAG, "finished calculating")
                }
        })
    }


    fun addPolylinesToMap(result: DirectionsResult){
      Handler(Looper.getMainLooper()).post{
          run {
              Log.d(TAG, "run: result routes: " + result.routes.size)

              for(route in result.routes){
                  Log.d(TAG, "run: leg: " + route.legs[0].toString())
                  val  decodedPath: List<com.google.maps.model.LatLng> = PolylineEncoding.decode(route.overviewPolyline.encodedPath)

                  val newDecodedPath: ArrayList<LatLng> =  arrayListOf()

                  for(latLng in decodedPath){

                      newDecodedPath.add(LatLng(latLng.lat, latLng.lng))
                  }

                  val polyline: Polyline = gMap.addPolyline(PolylineOptions().addAll(newDecodedPath))
                  polyline.color = ContextCompat.getColor(context!!, R.color.darkGrey)

                  zoomRoute(polyline.points)
              }
          }
      }
    }

    fun addCourseMarkers(example: List<LatLng>){
        Handler(Looper.getMainLooper()).post {
             run {
                 for (i in example) {
                     val marker = gMap.addMarker(
                         MarkerOptions()
                             .position(i)
                             .title("Coords: (${i.latitude}, ${i.longitude})")
                             .snippet("Click this window if you're near the activity")
                     )
                     marker.showInfoWindow()
                 }
             }
        }
    }

    fun zoomRoute(lstLatLngRoute: List<LatLng>) {

        if (lstLatLngRoute.isEmpty()) return

        val boundsBuilder = LatLngBounds.Builder()
        for (latLngPoint in lstLatLngRoute) boundsBuilder.include(latLngPoint)
        val routePadding = 120
        val latLngBounds = boundsBuilder.build()

        gMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
            600,
            null
        )
    }

    fun getLocationForCheck(){

        if (ContextCompat.checkSelfPermission(
                context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.lastLocation.addOnCompleteListener {

                if(it.isSuccessful) {
                    checkCloseToCheckPoint(it.result!!.latitude, it.result!!.longitude)
                }
            }
        }
    }

    fun checkCloseToCheckPoint(lat:Double, lng: Double){

        var check = 0

        for(i in 0 until completedPoints!!.size){

            if(completedPoints[i] == false){
                check = i
                break
            }
        }

        val userLoc = Location("user")
        userLoc.longitude = lng
        userLoc.latitude = lat

        val pointLoc = Location("point")
        pointLoc.latitude = ConverterForUI().stringToLatLng(waypoints!![check]).latitude
        pointLoc.longitude = ConverterForUI().stringToLatLng(waypoints!![check]).longitude

        val dist = userLoc.distanceTo(pointLoc)

        if (dist < 75){

            completedPoints[check] = true

            if(check == completedPoints.size - 2){

                Toast.makeText(context, "Only missing the final activity", Toast.LENGTH_SHORT).show()
            }
            else if(check == completedPoints.size - 1){

                //ultimo ponto, fazer ecrã de celebração

            }else{
                Toast.makeText(context, "Activity was reached", Toast.LENGTH_SHORT).show()
            }
        }else{

            Toast.makeText(context, "Not close enough", Toast.LENGTH_SHORT).show()
        }
    }
}
