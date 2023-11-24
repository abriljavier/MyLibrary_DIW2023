package com.example.mylibrary

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.ui.AppBarConfiguration
import androidx.appcompat.app.AppCompatActivity
import com.example.mylibrary.databinding.ActivityMainBinding
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout

class MainActivity : AppCompatActivity() {

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //CONFIGURAR LA TOOLBAR
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //DESPLEGAR EL DRAWER
//        var displayDrawerButton = findViewById<ImageButton>(R.id.imgButtonDisplayDrawer)
//        displayDrawerButton.setOnClickListener{
//            println("Despliego el menú del drawer")
//        }
        drawerLayout = findViewById(R.menu.drawer_layout)

        //DESPLEGAR EL MENÚ DE MÁS
        var displayMoreButton = findViewById<ImageButton>(R.id.imgButtonDisplayMore)
        displayMoreButton.setOnClickListener{
            var moreDialog = MoreDialog()
            moreDialog.show(supportFragmentManager, "123")
        }

        //MANEJAR EL DRAWER


    }

}