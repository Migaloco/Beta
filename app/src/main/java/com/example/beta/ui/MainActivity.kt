package com.example.beta.ui


import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import com.example.beta.R
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        val navController = host.navController

        setupBottomNavMenu(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            try {
                val dest = destination.id
                if(dest == R.id.mapMenuFragment || dest == R.id.rankingMenuFragment2 || dest == R.id.profileFragment || dest == R.id.coursesMenuFragment) {

                    bottom_nav.visibility = View.VISIBLE
                }
                else{

                    bottom_nav.visibility = View.GONE
                }
            }catch (e:Exception){
                Toast.makeText(this@MainActivity, "ResourceNotFound", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun buildAlertMessageLeave() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to exit?")
            .setCancelable(true)
            .setPositiveButton("Yes") { _, _ ->

                //Enviar pedido logout ao servidor

                val editor = this.getSharedPreferences("AUTHENTICATION", 0).edit()
                editor.clear()
                editor.apply()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask()
                }else {
                    appExit()
                }
            }.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun appExit() {

        this.finish()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun setupBottomNavMenu(navController: NavController) {

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        setupWithNavController(bottomNav!!, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.drawer_nav_layout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.logout -> buildAlertMessageLeave()
        }

        return onNavDestinationSelected(item, findNavController(findViewById(R.id.my_nav_host_fragment)))
                || super.onOptionsItemSelected(item)
    }
}