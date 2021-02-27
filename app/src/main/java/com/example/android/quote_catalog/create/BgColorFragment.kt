package com.example.android.quote_catalog.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.iterator
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.quote_catalog.DEFAULT_BG_COLOR
import com.example.android.quote_catalog.R
import com.example.android.quote_catalog.databinding.FragmentBgColorBinding
import com.google.android.material.chip.Chip

class BgColorFragment : Fragment() {

  private lateinit var binding: FragmentBgColorBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate<FragmentBgColorBinding>(inflater,
      R.layout.fragment_bg_color, container, false)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val args = BgColorFragmentArgs.fromBundle(requireArguments())
    val currentBgColor = args.bundleBgColor

    // Select the chip that corresponds to currentBgColor
    for (view in binding.bgColors) {
      val chip = view as Chip
      if (chip.chipBackgroundColor?.defaultColor?.equals(currentBgColor) == true) {
        binding.bgColors.check(chip.id)
        break
      }
    }

    // Setup listener for chipgroup
    binding.bgColors.setOnCheckedChangeListener { group, checkedId ->
      // Select the chip that corresponds to currentBgColor
      val selectedChip = getView()?.findViewById<Chip>(checkedId) as Chip
      val selectedColor = selectedChip.chipBackgroundColor?.defaultColor ?: DEFAULT_BG_COLOR
      val action = BgColorFragmentDirections.actionBgColorFragmentToCreateFragment(args.bundleQuoteTxt)
      action.bundleBgColor = selectedColor
      action.bundleTxtColor = args.bundleTxtColor
      findNavController().navigate(action)
    }
  }
}