package com.example.coolbookshelf.ui.fragments.bottomsheet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.coolbookshelf.R
import com.example.coolbookshelf.databinding.BookBottomSheetBinding
import com.example.coolbookshelf.util.Constants.Companion.DEFAULT_PRINT_TYPE
import com.example.coolbookshelf.util.Constants.Companion.DEFAULT_Q_KIND
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.example.coolbookshelf.viewmodel.BookViewModel
import java.util.*

class BookBottomSheet : BottomSheetDialogFragment() {

    private lateinit var booksViewModel: BookViewModel
    private lateinit var mView: BookBottomSheetBinding

    private var bookTypeChip = DEFAULT_Q_KIND
    private var bookTypeChipId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        booksViewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = BookBottomSheetBinding.inflate(inflater, container, false)

        booksViewModel.readBookKind.asLiveData().observe(viewLifecycleOwner, {value ->
            bookTypeChip = value.selectedBookKind
            updateChip(value.selectedBookKindId, mView.printTypeChipGroup)
        })

        mView.printTypeChipGroup.setOnCheckedStateChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId.first())
            Log.d("chip", selectedChipId.first().toString() + " " + selectedChipId.size)
            val selectedBookType = chip.text.toString().lowercase(Locale.ROOT)
            bookTypeChip = selectedBookType
            bookTypeChipId = selectedChipId.first()
        }


        mView.searchButton.setOnClickListener {
            Log.d("button", "pressed")
            booksViewModel.saveBookKind(
                bookTypeChip,
                bookTypeChipId
                )
            val action = BookBottomSheetDirections.actionBookBottomSheetToFragmentBooks(true)
            findNavController().navigate(action)
        }
        return mView.root
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if(chipId != 0){
            try{
                chipGroup.findViewById<Chip>(chipId).isChecked = true
                Log.d("chipId", "not zero" +chipId)
            }catch(e : java.lang.Exception){
                Log.d("BookBottomSheet", e.message.toString(), e)
            }
        }
    }


}