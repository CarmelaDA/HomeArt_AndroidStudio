package com.carmelart.homeart

import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.activity.viewModels

import android.view.Menu
import com.google.android.material.navigation.NavigationView

import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout

import android.view.MenuItem
import android.widget.Toast


import androidx.room.Room
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.databinding.ActivityMainBinding

import com.carmelart.homeart.database.ProductsDatabase

class MainActivity : AppCompatActivity(){

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    lateinit var dbController: ProductsDatabase
    //private lateinit var adapter: ProductListAdapter


    /**
     * Los observadores se preparan para estar pendientes de cambios
     * en las variables LiveData del ViewModel y se asignan luego a estas variables.
    **/
    /*private val listObserver = Observer<List<ProductsEntity>> { newList ->
        adapter.submitList(newList)
    }*/
    /*private val loadingObserver = Observer<Boolean> { loading ->
        binding.loading.isVisible = loading
    }*/

    private var nLED = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /**
         * Para hacerlo de forma estructurada, aquí se aginan los observadores
         * a sus variables LiveData del ViewModel.
        **/


        setSupportActionBar(findViewById(R.id.toolbar))

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_seguridad, R.id.nav_ventanas, R.id.nav_exterior, R.id.nav_iluminacion, R.id.nav_tiempo
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        /**
         * Compartir datos entre fragmentos y guardar datos en local.
        **/
        this.dbController = Room.databaseBuilder(
            applicationContext,
            ProductsDatabase::class.java,
            "HomeArt_DB",
        ).allowMainThreadQueries().build()

        dbController.DataDAO().addData(DataEntity(0)) // inicializar insert a 0 all

        Toast.makeText(this, dbController.DataDAO().getData().led.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /*private fun formatStringsForTextView()
    {
        val stringLED: String

        stringLED = if (nLED == 0) "0"
        else "1"

        //binding.textSet.text = "SET_LED:$stringLED \n" // no quitar
        //binding.textSet.text = "$stringLED" // no quitar
    }

    fun Fragment.formatStringsForTextView()
    {
        val stringLED: String

        var xLED=0

        stringLED = if (xLED == 0) "0"
        else "1"


        //binding.textSet.text = "SET_LED:$stringLED \n" // no quitar
    }*/


}

val Fragment.dbController: ProductsDatabase
    get() = (requireActivity() as MainActivity).dbController


