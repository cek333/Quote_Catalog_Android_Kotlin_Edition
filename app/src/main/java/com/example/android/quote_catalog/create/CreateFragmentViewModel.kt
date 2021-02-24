package com.example.android.quote_catalog.create

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android.quote_catalog.MAX_FILENAME_LEN
import com.example.android.quote_catalog.store.Quote
import com.example.android.quote_catalog.store.QuoteDatabaseDao
import kotlinx.coroutines.launch

class CreateFragmentViewModel(
  private val database: QuoteDatabaseDao,
  application: Application) : AndroidViewModel(application) {
  fun insert(text : String, bgColor : Int, txtColor : Int) {
    viewModelScope.launch {
      // Create filename from quote
      var quoteFilename = text.replace(Regex("""\W+"""), "_")
      if (quoteFilename.length > MAX_FILENAME_LEN) {
        quoteFilename = quoteFilename.substring(0, MAX_FILENAME_LEN)
      }
      quoteFilename += ".jpg"
      database.insert(Quote(quoteText=text, bgColor=bgColor, txtColor=txtColor, fileName=quoteFilename))
    }
  }
}

class CreateFragmentViewModelFactory(
  private val dataSource: QuoteDatabaseDao,
  private val application: Application) : ViewModelProvider.Factory {
  @Suppress("unchecked_cast")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(CreateFragmentViewModel::class.java)) {
      return CreateFragmentViewModel(dataSource, application) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}