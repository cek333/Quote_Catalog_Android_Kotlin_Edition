package com.example.android.quote_catalog.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.quote_catalog.*
import com.example.android.quote_catalog.databinding.FragmentCreateBinding
import com.example.android.quote_catalog.store.QuoteDatabase

class CreateFragment : Fragment() {

  private lateinit var binding: FragmentCreateBinding
  private lateinit var createViewModel: CreateFragmentViewModel
  private var currentBgColor = DEFAULT_BG_COLOR
  private var currentTxtColor = DEFAULT_TXT_COLOR

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate<FragmentCreateBinding>(inflater,
      R.layout.fragment_create, container, false)

    // Instantiate ViewModel
    val application = requireNotNull(this.activity).application
    val dataSource = QuoteDatabase.getInstance(application).quoteDatabaseDao
    val createViewModelFactory = CreateFragmentViewModelFactory(dataSource, application)
    createViewModel =
      ViewModelProvider(this, createViewModelFactory).get(CreateFragmentViewModel::class.java)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    currentBgColor = arguments?.getInt(BUNDLE_BG_COLOR) ?: currentBgColor
    // Apply color to button
    binding.createBgColorSelect.setBackgroundColor(currentBgColor)

    currentTxtColor = arguments?.getInt(BUNDLE_TXT_COLOR) ?: currentTxtColor
    // Apply color to button
    binding.createTxtColorSelect.setBackgroundColor(currentTxtColor)

    val currentQuoteTxt = arguments?.getString(BUNDLE_QUOTE_TXT) ?: ""

    binding.apply {
      // Log.i("CreateFragment", "bg color:${currentBgColor.toString()}")
      // Log.i("CreateFragment", "txt color:${currentTxtColor.toString()}")
      // Configure colours for quote box
      quoteText.setBackgroundColor(currentBgColor)
      quoteText.setTextColor(currentTxtColor)
      quoteText.setText(currentQuoteTxt)

      // Attach listener to Save button
      createSave.setOnClickListener {
        createViewModel.insert(quoteText.text.toString(), currentBgColor, currentTxtColor)
        Toast.makeText(context, getString(R.string.quote_stored), Toast.LENGTH_LONG).show()
      }

      // Attach listeners to colour pickers
      createBgColorSelect.setOnClickListener {
        findNavController().navigate(R.id.action_CreateFragment_to_BgColorFragment, createBundle())
      }

      createTxtColorSelect.setOnClickListener {
        findNavController().navigate(R.id.action_CreateFragment_to_TxtColorFragment, createBundle())
      }
    }
  }

  private fun createBundle() : Bundle {
    val localBundle = Bundle()
    localBundle.apply {
      putInt(BUNDLE_BG_COLOR, currentBgColor)
      putInt(BUNDLE_TXT_COLOR, currentTxtColor)
      putString(BUNDLE_QUOTE_TXT, binding.quoteText.text.toString())
    }
    return localBundle
  }
}