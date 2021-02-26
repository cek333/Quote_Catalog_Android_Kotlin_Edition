package com.example.android.quote_catalog

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {

  private lateinit var requestPermissionLauncher : ActivityResultLauncher<Array<String>>
  private lateinit var navController: NavController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val navHostFragment =
      supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    navController = navHostFragment.navController
    val appBarConfiguration = AppBarConfiguration(navController.graph)
    val toolbar = findViewById<Toolbar>(R.id.toolbar)
    // Designate the Toolbar as the ActionBar
    //  Must call setSupportActionBar() before attaching navController
    setSupportActionBar(toolbar)
    NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)

    // To investigate: Why does the following not work?
    // Compare to https://developer.android.com/codelabs/android-navigation#0
    // setupActionBarWithNavController() is an extension function defined in androidx.navigation.ui
    // setupActionBarWithNavController(navController, appBarConfiguration)

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog.
    requestPermissionLauncher =
      registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
          isGranted: Map<String, Boolean> ->
            if (isGranted.keys.all { permission -> isGranted[permission] == true }) {
              // Permission is granted.
              Toast.makeText(
                this,
                getString(R.string.permissions_success),
                Toast.LENGTH_LONG
              ).show()
            } else {
              // Explain to the user that the feature is unavailable
              Toast.makeText(
                this,
                getString(R.string.permissions_warning),
                Toast.LENGTH_LONG
              ).show()
            }
      }
  }

//   override fun onNavigateUp(): Boolean {
//     return navController.navigateUp(appBarConfiguration)
//   }

//  override fun onCreateOptionsMenu(menu: Menu): Boolean {
//    // Inflate the menu; this adds items to the action bar if it is present.
//    menuInflater.inflate(R.menu.menu_main, menu)
//    return true
//  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    return when {
      NavigationUI.onNavDestinationSelected(item, navController) -> true
      item.itemId == R.id.menu_enable_permissions -> {
        requestPermissionsIfNecessary()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun requestPermissionsIfNecessary() {
    when {
      checkAllPermissions(this) -> {
        Toast.makeText(
          this,
          getString(R.string.permissions_confirmation),
          Toast.LENGTH_LONG
        ).show()
      }
      else -> {
        // You can directly ask for the permission.
        // The registered ActivityResultCallback gets the result of this request.
        requestPermissionLauncher.launch(PERMISSIONS)
      }
    }
  }

}