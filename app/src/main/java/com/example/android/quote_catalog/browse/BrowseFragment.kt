package com.example.android.quote_catalog.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.quote_catalog.R
import com.example.android.quote_catalog.databinding.FragmentBrowseBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class BrowseFragment : Fragment() {

  private lateinit var binding: FragmentBrowseBinding

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate<FragmentBrowseBinding>(inflater,
      R.layout.fragment_browse, container, false)

//    binding.browseBtnAddQuote.setOnClickListener { view ->
//      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//        .setAction("Action", null).show()
//    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.browseBtnAddQuote.setOnClickListener {
      findNavController().navigate(R.id.action_BrowseFragment_to_CreateFragment)
    }
  }
}