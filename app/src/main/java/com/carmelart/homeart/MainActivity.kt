package com.carmelart.homeart

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.carmelart.homeart.database.DataDb
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.databinding.ActivityMainBinding
import com.carmelart.homeart.ui.configuracion.ConfiguracionFragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var dbController: DataDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_seguridad,
                R.id.nav_iluminacion,
                R.id.nav_ventanas,
                R.id.nav_tiempo ,
                R.id.nav_exterior,

            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        this.dbController = Room.databaseBuilder(
            applicationContext,
            DataDb::class.java,
            "HomeArt_DB",
        ).allowMainThreadQueries().build()

        dbController.DataDAO().addData(DataEntity(

            // Seguridad
            0 , 0, 0,

            // Iluminación
            0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0,0, 0, 0,
            0, 0, 0,0, 0, 0, 0,

            // Puertas y ventanas
            0, 0, 0, 0, 0,

            // Tiempo
            0,0, 0, 0, 0,

            // Exterior
            0,

            // Salón
            0,

            // Huerto
            0,

            // Horarios
            "", "", bEncendidoAlarma = false, bApagadoAlarma = false,

            // Huerto
            0, 0, bMinHuerto = false, bMaxHuerto = false,

        )) // Inicializar todas las variables a 0
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)

        binding.buttExterior.setOnClickListener {
            findNavController().navigate(R.id.nav_exterior)
        }
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.butt_configuracion -> {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.nav_configuracion)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

// Funciones de extensión
val Fragment.dbController: DataDb
    get() = (requireActivity() as MainActivity).dbController


