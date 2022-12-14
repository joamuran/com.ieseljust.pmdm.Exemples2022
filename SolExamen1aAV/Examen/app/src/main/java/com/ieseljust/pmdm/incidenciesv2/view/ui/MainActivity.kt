package com.ieseljust.pmdm.incidenciesv2.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import com.ieseljust.pmdm.incidenciesv2.R
import com.ieseljust.pmdm.incidenciesv2.databinding.ActivityMainBinding
import com.ieseljust.pmdm.incidenciesv2.viewmodel.IncidenciesViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: IncidenciesViewModel // ref. al ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adaptaci√≥ a MVVM: Instanciem el ViewModel, utilitzant la classe ViewModelProvider
        viewModel = ViewModelProvider(this)[IncidenciesViewModel::class.java]
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Modificaci√≥ per anar al segon fragment
        binding.fab.setOnClickListener {
            viewModel.cleanIncidenciaEnEdicio();
            navController.navigate(R.id.action_MainFragment_to_EditFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when ((destination as FragmentNavigator.Destination).className) {
                // show fab in recipe fragment
                MainFragment::class.qualifiedName -> {
                    binding.fab.visibility = View.VISIBLE
                }
                else -> {
                    binding.fab.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
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
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}