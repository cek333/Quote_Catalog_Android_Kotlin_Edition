package com.example.android.quote_catalog.browse

import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.drawToBitmap
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.coroutineScope
import com.example.android.quote_catalog.R
import com.example.android.quote_catalog.checkAllPermissions
import com.example.android.quote_catalog.store.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BrowseEventHandler(private val activity : FragmentActivity,
                         private val viewModel : BrowseFragmentViewModel) {

  enum class OperationStatus(@StringRes val value: Int) {
    DOWNLOADED(R.string.status_downloaded),
    SHARED(R.string.status_shared),
    EXISTS(R.string.status_exists),
    NO_PERMISSIONS(R.string.status_no_permissions),
    ERROR(R.string.status_error)
  }

  fun handleDelete(quote: Quote) {
    viewModel.deleteQuote(quote)
  }

  fun handleDownload(view : View, filename : String) {
    var result = OperationStatus.DOWNLOADED
    activity.lifecycle.coroutineScope.launch {
      if (checkAllPermissions(activity.applicationContext)) {
        var quoteUri = findImage(filename)
        if (quoteUri == null) {
          quoteUri = saveImage(view, filename)
        } else {
          result = OperationStatus.EXISTS
        }
      } else {
        result = OperationStatus.NO_PERMISSIONS
      }
      withContext(Dispatchers.Main) {
        Toast.makeText(
          activity.applicationContext,
          activity.getString(result.value),
          Toast.LENGTH_LONG).show()
      }
    }
  }

  fun handleShare(view : View, filename : String) {
    var result = OperationStatus.SHARED
    activity.lifecycle.coroutineScope.launch {
      if (checkAllPermissions(activity.applicationContext)) {
        var quoteUri = findImage(filename)
        if (quoteUri == null) {
          quoteUri = saveImage(view, filename)
        }
        if (quoteUri != null) {
          val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, quoteUri)
            type = "image/jpeg"
          }
          try {
            activity.startActivity(shareIntent)
          } catch(e : Exception) {
            result = OperationStatus.ERROR
          }
        } else {
          result = OperationStatus.ERROR
        }
      } else {
        result = OperationStatus.NO_PERMISSIONS
      }
      withContext(Dispatchers.Main) {
        if (result != OperationStatus.SHARED) {
          Toast.makeText(
            activity.applicationContext,
            activity.getString(result.value),
            Toast.LENGTH_LONG
          ).show()
        }
      }
    }
  }

  private suspend fun findImage(filename: String) : Uri? {
    // https://developer.android.com/training/data-storage/shared/media#query-collection
    val resolver = activity.applicationContext.contentResolver

    val collection =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(
          MediaStore.VOLUME_EXTERNAL_PRIMARY
        )
      } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
      }

    val projection = arrayOf(
      MediaStore.Images.Media._ID,
      MediaStore.Images.Media.DISPLAY_NAME,
      MediaStore.Images.Media.MIME_TYPE
    )
    val selection: String = MediaStore.Images.Media.DISPLAY_NAME + " = ?"
    val selectionArgs: Array<String> = arrayOf(filename)

    // Display videos in alphabetical order based on their display name.
    val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC"

    val query = resolver.query(
      collection,
      projection,
      selection,
      selectionArgs,
      sortOrder
    )

    if (query?.count == null || query?.count != 1) {
      return null
    }
    query?.use { cursor ->
      // Cache column indices.
      val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
      val nameColumn =
        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

      cursor.moveToNext()
      // Get values of columns for a given video.
      val id = cursor.getLong(idColumn)
      val name = cursor.getString(nameColumn)

      // Log.i("findImage", "$name")
      return if (name != filename) {
        null
      } else {
        ContentUris.withAppendedId(collection, id)
      }
    }
    return null
  }

  private suspend fun saveImage(view : View, filename: String) : Uri? {
    // https://developer.android.com/training/data-storage/shared/media#add-item
    val resolver = activity.applicationContext.contentResolver
    val collection =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(
          MediaStore.VOLUME_EXTERNAL_PRIMARY
        )
      } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
      }

    // ADD IMAGE TO COLLECTION
    val imgDetails = ContentValues().apply {
      put(MediaStore.Images.Media.DISPLAY_NAME, filename)
      put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        put(MediaStore.Images.Media.IS_PENDING, 1)
      }
    }

    val imgUri = resolver.insert(collection, imgDetails)
    // Log.i("saveImage", "imgUri=${imgUri.toString()} filename=$filename")
    if (imgUri != null) {
      // Log.i("saveImage", "Attempting to save image!")
      val quoteBitmap = view.drawToBitmap()
      resolver.openFileDescriptor(imgUri, "w", null).use { pfd ->
        // Write data into the pending audio file.
        val stream = ParcelFileDescriptor.AutoCloseOutputStream(pfd)
        quoteBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.close()
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Now that we're finished, release the "pending" status
        imgDetails.clear()
        imgDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
        resolver.update(imgUri, imgDetails, null, null)
      }
      // Log.i("saveImage", "Image saved!")
    }
    return imgUri
  }

}