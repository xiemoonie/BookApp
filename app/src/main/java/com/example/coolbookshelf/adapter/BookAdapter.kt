package com.example.coolbookshelf.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.coolbookshelf.databinding.BookRowLayoutBinding
import com.example.coolbookshelf.modelstwo.Item
import com.example.coolbookshelf.modelstwo.NewBookResult
import com.example.coolbookshelf.util.BookDiffUtil

class BookAdapter : RecyclerView.Adapter<BookAdapter.MyViewHolder>() {

    private var books = emptyList<Item>()

    class MyViewHolder(private val binding: BookRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item){
            binding.result = item
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup) : MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BookRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentBook = books[position]
        holder.bind(currentBook)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    fun setData(newData: NewBookResult){
        val booksDiffUtil = BookDiffUtil(books, newData.items)
        val diffUtilResult = DiffUtil.calculateDiff(booksDiffUtil)
        books = newData.items
        diffUtilResult.dispatchUpdatesTo(this)

    }
}