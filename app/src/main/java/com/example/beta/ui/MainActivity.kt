package com.example.beta.ui


import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import com.example.beta.R
import com.example.beta.others.DbManager
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
                if(dest == R.id.logInFragment || dest == R.id.registerFragment){

                    bottom_nav.visibility = View.GONE
                }
                else{
                    bottom_nav.visibility = View.VISIBLE
                }
            }catch (e:Exception){
                Toast.makeText(this@MainActivity, "ResourceNotFound", Toast.LENGTH_SHORT).show()
            }
        }

        var listCt: List<String> = listOf("Gastronomia","Populares", "Novos", "Relevantes", "Culturais", "Familiares", "Grátis", "Natureza", "Desportivo", "Educacional")

        fillDatabase(listCt)
    }

    private fun setupBottomNavMenu(navController: NavController) {

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        setupWithNavController(bottomNav!!, navController)
    }

    private fun fillDatabase(listCt: List<String>){

        var dbManager = DbManager(this)
        var added:Boolean

        for(i in listCt){

            added = dbManager.insert(i)

            if (!added) break
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.drawer_nav_layout, menu)

        val sv:SearchView = menu.findItem(R.id.search_view).actionView as SearchView

        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("not implemented") //Use database list to search.(Locais / Percursos / Users)
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return onNavDestinationSelected(item, findNavController(findViewById(R.id.my_nav_host_fragment)))
                || super.onOptionsItemSelected(item)
    }


}