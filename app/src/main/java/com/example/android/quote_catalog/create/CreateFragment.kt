package com.example.android.quote_catalog.create

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.quote_catalog.DEFAULT_BG_COLOR
import com.example.android.quote_catalog.DEFAULT_TXT_COLOR
import com.example.android.quote_catalog.R
import com.example.android.quote_catalog.databinding.FragmentCreateBinding

class CreateFragment : Fragment() {

  private lateinit var binding: FragmentCreateBinding

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

    val currentBgColor = arguments?.getString("SelectedBgColor")?.toInt() ?: DEFAULT_BG_COLOR
    // Apply color to button
    binding.createBgColorSelect.setBackgroundColor(currentBgColor)

    val currentTxtColor = arguments?.getString("SelectedTxtColor")?.toInt() ?: DEFAULT_TXT_COLOR
    // Apply color to button
    binding.createTxtColorSelect.setBackgroundColor(currentTxtColor)

    binding.apply {
      Log.i("CreateFragment", "bg color:${currentBgColor.toString()}")
      Log.i("CreateFragment", "txt color:${currentTxtColor.toString()}")
      // Configure colours for quote box
      currentBgColor?.let { quoteText.setBackgroundColor(it) }
      currentTxtColor?.let { quoteText.setTextColor(it) }

      // Attach listener to Save button
      createSave.setOnClickListener {
        Log.i("CreateFragment", "quote:${quoteText.text.toString()}")
      }

      // Attach listeners to colour pickers
      createBgColorSelect.setOnClickListener {
        val bundle = bundleOf("CurrentBgColor" to currentBgColor.toString())
        findNavController().navigate(R.id.action_CreateFragment_to_BgColorFragment, bundle)
      }

      createTxtColorSelect.setOnClickListener {
        val bundle = bundleOf("CurrentTxtColor" to currentTxtColor.toString())
        findNavController().navigate(R.id.action_CreateFragment_to_TxtColorFragment, bundle)
      }
    }
  }
}