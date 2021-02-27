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
import com.example.android.quote_catalog.DEFAULT_BG_COLOR
import com.example.android.quote_catalog.DEFAULT_TXT_COLOR
import com.example.android.quote_catalog.R
import com.example.android.quote_catalog.databinding.FragmentCreateBinding
import com.example.android.quote_catalog.hideKeyboardFrom
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

    val args = CreateFragmentArgs.fromBundle(requireArguments())
    currentBgColor = args.bundleBgColor
    // Apply color to button
    binding.createBgColorSelect.setBackgroundColor(currentBgColor)

    currentTxtColor = args.bundleTxtColor
    // Apply color to button
    binding.createTxtColorSelect.setBackgroundColor(currentTxtColor)

    val currentQuoteTxt : String? = args.bundleQuoteTxt

    binding.apply {
      // Log.i("CreateFragment", "bg color:${currentBgColor.toString()}")
      // Log.i("CreateFragment", "txt color:${currentTxtColor.toString()}")
      // Configure colours for quote box
      quoteText.setBackgroundColor(currentBgColor)
      quoteText.setTextColor(currentTxtColor)
      currentQuoteTxt?.let { quoteText.setText(it) }

      // Attach listener to Save button
      createSave.setOnClickListener {
        // Hide the keyboard
        hideKeyboardFrom(requireActivity(), quoteText)
        // Insert quote into database
        createViewModel.insert(quoteText.text.toString(), currentBgColor, currentTxtColor)
        Toast.makeText(context, getString(R.string.quote_stored), Toast.LENGTH_LONG).show()
      }

      // Attach listeners to colour pickers
      createBgColorSelect.setOnClickListener {
        val txt = binding.quoteText.text.toString()
        val action = CreateFragmentDirections.actionCreateFragmentToBgColorFragment(txt)
        action.bundleBgColor = currentBgColor
        action.bundleTxtColor = currentTxtColor
        findNavController().navigate(action)
      }

      createTxtColorSelect.setOnClickListener {
        val txt = binding.quoteText.text.toString()
        val action = CreateFragmentDirections.actionCreateFragmentToTxtColorFragment(txt)
        action.bundleBgColor = currentBgColor
        action.bundleTxtColor = currentTxtColor
        findNavController().navigate(action)
      }
    }
  }

//  private fun createBundle() : Bundle {
//    val localBundle = Bundle()
//    localBundle.apply {
//      putInt(BUNDLE_BG_COLOR, currentBgColor)
//      putInt(BUNDLE_TXT_COLOR, currentTxtColor)
//      putString(BUNDLE_QUOTE_TXT, binding.quoteText.text.toString())
//    }
//    return localBundle
//  }
}