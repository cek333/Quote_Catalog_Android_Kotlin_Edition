package com.example.android.quote_catalog.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.iterator
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.quote_catalog.DEFAULT_TXT_COLOR
import com.example.android.quote_catalog.R
import com.example.android.quote_catalog.databinding.FragmentTxtColorBinding
import com.google.android.material.chip.Chip

class TxtColorFragment : Fragment() {

    private lateinit var binding: FragmentTxtColorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentTxtColorBinding>(inflater,
            R.layout.fragment_txt_color, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentTxtColor : Int = arguments?.getString("CurrentTxtColor")?.toInt() ?: DEFAULT_TXT_COLOR

        // Select the chip that corresponds to currentBgColor
        for (view in binding.txtColors) {
            val chip = view as Chip
            // val intColor = chip.chipBackgroundColor?.defaultColor
            // Log.i("TxtColorFragment", "bg color:${intColor.toString()}")
            if (chip.chipBackgroundColor?.defaultColor?.equals(currentTxtColor) == true) {
                binding.txtColors.check(chip.id)
                break
            }
        }

        // Setup listener for chipgroup
        binding.txtColors.setOnCheckedChangeListener { group, checkedId ->
            // Select the chip that corresponds to currentBgColor
            val selectedChip = getView()?.findViewById<Chip>(checkedId) as Chip
            val selectedColor = selectedChip.chipBackgroundColor?.defaultColor
            val bundle = bundleOf("SelectedTxtColor" to selectedColor.toString())
            findNavController().navigate(R.id.action_TxtColorFragment_to_CreateFragment, bundle)
        }
    }
}