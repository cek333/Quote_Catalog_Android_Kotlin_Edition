package com.example.android.quote_catalog.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.quote_catalog.R

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CreateFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_create, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

//    view.findViewById<Button>(R.id.create_save).setOnClickListener {
//      findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//    }
  }
}