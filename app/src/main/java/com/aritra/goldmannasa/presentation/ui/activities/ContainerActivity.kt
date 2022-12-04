package com.aritra.goldmannasa.presentation.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aritra.goldmannasa.NasaApp
import com.aritra.goldmannasa.R
import com.aritra.goldmannasa.databinding.ActivityContainerBinding
import com.aritra.goldmannasa.presentation.utils.THEME_TYPE
import com.aritra.goldmannasa.presentation.viewmodel.factories.ApodViewModelFactory
import com.aritra.goldmannasa.presentation.viewmodel.vm.ApodViewModel
import com.google.android.material.navigation.NavigationView
import javax.inject.Inject

class ContainerActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityContainerBinding


    @Inject
    lateinit var factory: ApodViewModelFactory
    private lateinit var viewModel: ApodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarContainer.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_container)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        // update theme according to saved preference
        val pref = this.getPreferences(Context.MODE_PRIVATE)
        AppCompatDelegate.setDefaultNightMode(pref.getInt(THEME_TYPE,AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM))

        NasaApp.appInstance.appComponent.inject(this)
        viewModel = ViewModelProvider(this,factory)[ApodViewModel::class.java]

        if(savedInstanceState==null) viewModel.getLatestAPOD()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.container, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.select_theme_default ->{
                saveThemePreference(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                true
            }
            R.id.select_theme_day->{
                saveThemePreference(AppCompatDelegate.MODE_NIGHT_NO)
                true
            }
            R.id.select_theme_dark->{
                saveThemePreference(AppCompatDelegate.MODE_NIGHT_YES)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_container)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun saveThemePreference(mode:Int){
        //val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val prefs = this.getPreferences(Context.MODE_PRIVATE)
        prefs.edit().putInt(THEME_TYPE, mode).apply()
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}