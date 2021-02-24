package com.example.android.quote_catalog.create

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.quote_catalog.*
import com.example.android.quote_catalog.databinding.FragmentCreateBinding

class CreateFragment : Fragment() {

  private lateinit var binding: FragmentCreateBinding
  private var currentBgColor = DEFAULT_BG_COLOR
  private var currentTxtColor = DEFAULT_TXT_COLOR

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate<FragmentCreateBinding>(inflater,
      R.layout.fragment_create, container, false)

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
      currentBgColor?.let { quoteText.setBackgroundColor(it) }
      currentTxtColor?.let { quoteText.setTextColor(it) }
      currentQuoteTxt?.let { quoteText.setText(it) }

      // Attach listener to Save button
      createSave.setOnClickListener {
        Log.i("CreateFragment", "quote:${quoteText.text.toString()}")
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