package com.example.android.quote_catalog.browse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.quote_catalog.databinding.QuoteItemViewBinding
import com.example.android.quote_catalog.store.Quote

class QuoteAdapter(private val handler : BrowseEventHandler) : ListAdapter<Quote, QuoteAdapter.ViewHolder>(QuoteDiffCallback()) {
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    // Bind the current item to the view holder
    val item = getItem(position)
    holder.bind(item, handler)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    // Create a ViewHolder for storing an item
    return ViewHolder.from(parent)
  }

  class ViewHolder private constructor(
    val binding: QuoteItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
      fun from(parent: ViewGroup) : ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : QuoteItemViewBinding =
          QuoteItemViewBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
      }
    }

    fun bind(item: Quote, handler: BrowseEventHandler) {
      binding.apply {
        quoteTextFromDb.text = item.quoteText
        quoteTextFromDb.setBackgroundColor(item.bgColor)
        quoteTextFromDb.setTextColor(item.txtColor)
        quoteDelete.setOnClickListener() {
          handler.handleDelete(item)
        }
        quoteDownload.setOnClickListener() {
          handler.handleDownload(quoteTextFromDb, item.fileName)
        }
        quoteShare.setOnClickListener() {
          handler.handleShare(quoteTextFromDb, item.fileName)
        }
      }
    }
  }

}

class QuoteDiffCallback : DiffUtil.ItemCallback<Quote>() {
  override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
    return oldItem.quoteId == newItem.quoteId
  }

  override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
    return oldItem == newItem
  }
}