package com.example.android.quote_catalog.browse

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.quote_catalog.store.QuoteDatabaseDao

class BrowseFragmentViewModel(private val database: QuoteDatabaseDao,
                              application: Application) : AndroidViewModel(application) {
//  private val _quotes = MutableLiveData<List<Quote>>()
//  val quotes: LiveData<List<Quote>>
//    get() = _quotes

  val quotes = database.getAllQuotes()
//  init {
//    getAllQuotes()
//  }

//  fun getAllQuotes() {
//    viewModelScope.launch {
//      _quotes.value = database.getAllQuotes()
//    }
//  }
}

class BrowseFragmentViewModelFactory(
  private val dataSource: QuoteDatabaseDao,
  private val application: Application
) : ViewModelProvider.Factory {
  @Suppress("unchecked_cast")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(BrowseFragmentViewModel::class.java)) {
      return BrowseFragmentViewModel(dataSource, application) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}