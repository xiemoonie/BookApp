package com.example.coolbookshelf.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.example.coolbookshelf.R
import com.example.coolbookshelf.databinding.FragmentOverviewBinding
import com.example.coolbookshelf.modelstwo.Item
import com.example.coolbookshelf.util.Constants.Companion.BOOK_RESULT_KEY
import org.jsoup.Jsoup


class OverviewFragment : Fragment() {

    lateinit var _binding : FragmentOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false )
        var view = _binding.root


        val args = arguments
        val myBundle: Item? = args?.getParcelable(BOOK_RESULT_KEY)

        _binding.mainImageView.load(myBundle?.selfLink)
        _binding.titleTextView.text = myBundle?.volumeInfo?.title
        _binding.likeTextView.text = myBundle?.likes.toString()
        myBundle?.description.let {
            val summary = Jsoup.parse(it).text()
            _binding.summaryTextView.text = summary
        }
//In Item set booleans horror
        if(myBundle?.horror == true){
            _binding.horrorImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            _binding.horrorTextView.setTextColor(ContextCompat.getColor(requireContext(),  R.color.green))
        }

        if(myBundle?.romance == true){
            _binding.romanceImageView.setColorFilter(ContextCompat.getColor(requireContext(),  R.color.green))
            _binding.romanceTextView.setTextColor(ContextCompat.getColor(requireContext(),  R.color.green))
        }

        if(myBundle?.art == true){
            _binding.artImageView.setColorFilter(ContextCompat.getColor(requireContext(),  R.color.green))
            _binding.artTextView.setTextColor(ContextCompat.getColor(requireContext(),  R.color.green))
        }

        if(myBundle?.scifi == true){
            _binding.scifiImageView.setColorFilter(ContextCompat.getColor(requireContext(),  R.color.green))
            _binding.scifiTextView.setTextColor(ContextCompat.getColor(requireContext(),  R.color.green))
        }

        if(myBundle?.dystopian == true){
            _binding.dystopianImageView.setColorFilter(ContextCompat.getColor(requireContext(),  R.color.green))
            _binding.dystopianTextView.setTextColor(ContextCompat.getColor(requireContext(),  R.color.green))
        }

        if(myBundle?.travel == true){
            _binding.travelImageView.setColorFilter(ContextCompat.getColor(requireContext(),  R.color.green))
            _binding.travelTextView.setTextColor(ContextCompat.getColor(requireContext(),  R.color.green))
        }
        if(myBundle?.historical == true){
            _binding.historicalImageView.setColorFilter(ContextCompat.getColor(requireContext(),  R.color.green))
            _binding.historicalTextView.setTextColor(ContextCompat.getColor(requireContext(),  R.color.green))
        }
        if(myBundle?.science == true){
            _binding.scienceImageView.setColorFilter(ContextCompat.getColor(requireContext(),  R.color.green))
            _binding.scienceTextView.setTextColor(ContextCompat.getColor(requireContext(),  R.color.green))
        }
        if(myBundle?.selfhelp == true){
            _binding.selfhelpImageView.setColorFilter(ContextCompat.getColor(requireContext(),  R.color.green))
            _binding.selfhelpTextView.setTextColor(ContextCompat.getColor(requireContext(),  R.color.green))
        }
        if(myBundle?.biography == true){
            _binding.biographyImageView.setColorFilter(ContextCompat.getColor(requireContext(),  R.color.green))
            _binding.biographyTextView.setTextColor(ContextCompat.getColor(requireContext(),  R.color.green))
        }
        if(myBundle?.spirituality == true){
            _binding.spiritualityImageView.setColorFilter(ContextCompat.getColor(requireContext(),  R.color.green))
            _binding.spiritualityTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        if(myBundle?.fantasy == true){
            _binding.fantasyImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            _binding.fantasyTextView.setTextColor(ContextCompat.getColor(requireContext(),  R.color.green))
        }

        return view
    }
        }