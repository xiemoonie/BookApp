package com.example.coolbookshelf.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.coolbookshelf.R
import com.example.coolbookshelf.modelstwo.Item
import com.example.coolbookshelf.ui.fragments.BookFragmentDirections
import org.jsoup.Jsoup

class BookRowBinding {

    companion object {

        @BindingAdapter("onBookClickListener")
        @JvmStatic
        fun onBookClickListener(bookRowLayout: ConstraintLayout, item: Item) {
            Log.d("listenerCalled", "listener Called! :D")
            bookRowLayout.setOnClickListener {
                try {
                    Log.d("navBar", "hey it did worked! :D")
                    val action = BookFragmentDirections.actionFragmentBooksToDetailsActivity(item)
                    bookRowLayout.findNavController().navigate(action)

                } catch (e: java.lang.Exception) {
                    Log.e("navBar1", e.toString(), e)
                }
            }
        }

        @BindingAdapter("loadImageBook")
        @JvmStatic
        fun loadImageBook(imageView: ImageView, imageUrl: String?) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.error_placeholder)
            }
        }


        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: View, vegan: Boolean) {
            if (vegan) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(view.context, R.color.blue)
                        )
                    }
                    is ImageView -> {
                        view.setColorFilter(ContextCompat.getColor(view.context, R.color.blue))
                    }
                }
            }
        }

        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView, description: String?) {
            if (description != null) {
                val desc = Jsoup.parse(description).text()
                textView.text = desc
            }
        }

    }
}