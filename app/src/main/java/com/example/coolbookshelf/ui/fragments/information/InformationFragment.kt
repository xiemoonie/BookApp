package com.example.coolbookshelf.ui.fragments.information

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient

import com.example.coolbookshelf.databinding.FragmentInformationBinding
import com.example.coolbookshelf.modelstwo.Item
import com.example.coolbookshelf.util.Constants



class InformationFragment : Fragment() {

    lateinit var binding : FragmentInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInformationBinding.inflate(inflater, container,false)
        var view = binding.root

        val args = arguments
        val myBundle: Item? = args?.getParcelable(Constants.BOOK_RESULT_KEY)
        binding.informationWebView.webViewClient= object : WebViewClient(){}
        val websiteUrl: String = myBundle!!.selfLink
        binding.informationWebView.loadUrl(websiteUrl)

        return view
    }

}