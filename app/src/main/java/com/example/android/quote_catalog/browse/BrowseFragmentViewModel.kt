package com.example.android.quote_catalog.browse

import android.app.Application
import androidx.lifecycle.*
import com.example.android.quote_catalog.store.Quote
import com.example.android.quote_catalog.store.QuoteDatabaseDao
import kotlinx.coroutines.launch

class BrowseFragmentViewModel(private val database: QuoteDatabaseDao,
                              application: Application) : AndroidViewModel(application) {

  private val _filter = MutableLiveData<String?>()
  val quotes : LiveData<List<Quote>> = Transformations.switchMap(_filter) {
    if (it == null) database.getAllQuotes() else database.queryQuotes("%$it%")
  }

  init {
    _filter.value = null
  }

  fun setQuoteFilter(query: String) {
    _filter.value = query
  }

  fun clearQuoteFilter() {
    _filter.value = null
  }

  fun deleteQuote(quote: Quote) {
    viewModelScope.launch {
      database.delete(quote)
    }
  }
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