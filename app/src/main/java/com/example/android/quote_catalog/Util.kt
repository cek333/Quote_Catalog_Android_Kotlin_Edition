package com.example.android.quote_catalog

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

// Default colours
const val DEFAULT_BG_COLOR = -1 // white
const val DEFAULT_TXT_COLOR = -16777216 // black

// Bundle keys
const val BUNDLE_BG_COLOR = "BUNDLE_BG_COLOR"
const val BUNDLE_TXT_COLOR = "BUNDLE_TXT_COLOR"
const val BUNDLE_QUOTE_TXT = "BUNDLE_QUOTE_TXT"

// Other constants
const val MAX_FILENAME_LEN = 75

// Shared Permissions Checking Code
val PERMISSIONS = arrayOf(
  Manifest.permission.READ_EXTERNAL_STORAGE,
  Manifest.permission.WRITE_EXTERNAL_STORAGE
)

fun checkAllPermissions(context: Context): Boolean {
  var hasPermissions = true
  for (permission in PERMISSIONS) {
    hasPermissions = hasPermissions and isPermissionGranted(context, permission)
  }
  return hasPermissions
}

private fun isPermissionGranted(context: Context, permission: String) =
  ContextCompat.checkSelfPermission(context, permission) ==
      PackageManager.PERMISSION_GRANTED

// Shared utility function
fun hideKeyboardFrom(context : Context, view: View) {
  val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(view.windowToken, 0);
}