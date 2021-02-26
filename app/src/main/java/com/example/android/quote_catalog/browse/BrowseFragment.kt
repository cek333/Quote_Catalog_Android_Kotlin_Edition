package com.example.android.quote_catalog.browse

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.quote_catalog.R
import com.example.android.quote_catalog.databinding.FragmentBrowseBinding
import com.example.android.quote_catalog.hideKeyboardFrom
import com.example.android.quote_catalog.store.QuoteDatabase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class BrowseFragment : Fragment() {

  private lateinit var binding: FragmentBrowseBinding
  private lateinit var browseViewModel: BrowseFragmentViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate<FragmentBrowseBinding>(inflater,
      R.layout.fragment_browse, container, false)

    // Instantiate ViewModel
    val application = requireNotNull(this.activity).application
    val dataSource = QuoteDatabase.getInstance(application).quoteDatabaseDao
    val browseViewModelFactory = BrowseFragmentViewModelFactory(dataSource, application)
    browseViewModel =
      ViewModelProvider(this, browseViewModelFactory).get(BrowseFragmentViewModel::class.java)

    val adapter = QuoteAdapter(BrowseEventHandler(requireActivity(), browseViewModel))
    binding.quoteList.adapter = adapter

    // Enable settings menu or this fragment
    setHasOptionsMenu(true)

    browseViewModel.quotes.observe(viewLifecycleOwner, Observer {
      it?.let { adapter.submitList(it) }
    })

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.browseBtnAddQuote.setOnClickListener {
      findNavController().navigate(R.id.action_BrowseFragment_to_CreateFragment)
    }

    binding.browseBtnSearch.setOnClickListener {
      // Hide the keyboard
      hideKeyboardFrom(requireActivity(), binding.browseSearchTerm)

      val query = binding.browseSearchTerm.text.toString().trim()
      if (query != "") {
        browseViewModel.setQuoteFilter(query)
      }
    }

    binding.browseBtnClearSearch.setOnClickListener {
      binding.browseSearchTerm.setText("")
      browseViewModel.clearQuoteFilter()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.browse_menu, menu)
  }
}