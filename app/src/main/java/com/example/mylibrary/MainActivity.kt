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
    lateinit var sqlHelper: SqlHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //SE OBTIENE LA INSTANCIA DE LA BBDD
        sqlHelper = SqlHelper.getInstance(this)

        //CONFIGURO LA TOOLBAR DEL MAIN
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //RECOJO LOS ELEMENTOS QUE ACOMPAÑAN AL NAV DRAWER
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        //QUE ELEMENTOS DEL NAV_GRAPH VAN EN EL NAVIGATION
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.firstFragment, R.id.secondFragment,
            R.id.thirdFragment, R.id.fourthFragment,
        ), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)

        //CONFIGURO LA NAVEGACIÓN
        navView.setupWithNavController(navController)

        //CÓDIGO PARA CUANDO SE PULSA EL BOTÓN DE HAMBUGUESA
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //MANEJAR LOS ENLACES EN EL NAV DRAWER
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
                else -> false
            }
        }
    }
}
