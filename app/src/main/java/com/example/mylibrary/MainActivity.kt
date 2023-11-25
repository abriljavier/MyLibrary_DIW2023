package com.example.mylibrary

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configura la ActionBar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        // Configura la barra de herramientas con el botón de hamburguesa
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.firstFragment, R.id.secondFragment,
            R.id.thirdFragment, R.id.fourthFragment, R.id.fifthFragment
        ), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)

        // Configura la navegación del NavigationView
        navView.setupWithNavController(navController)

        // Añade el código de ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Maneja los clics en los elementos del NavigationView
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.firstFragmentNav -> {
                    navController.navigate(R.id.firstFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.secondFragmentNav -> {
                    navController.navigate(R.id.secondFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.thirdFragmentNav -> {
                    navController.navigate(R.id.thirdFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.fourthFragmentNav -> {
                    navController.navigate(R.id.fourthFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.fifthFragmentNav -> {
                    navController.navigate(R.id.fifthFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d("MainActivity", "onSupportNavigateUp called")
        val navController = findNavController(R.id.nav_host_fragment)
        val navigateUpResult = navController.navigateUp() || super.onSupportNavigateUp()
        Log.d("MainActivity", "Navigate Up Result: $navigateUpResult")
        return navigateUpResult
    }
}
