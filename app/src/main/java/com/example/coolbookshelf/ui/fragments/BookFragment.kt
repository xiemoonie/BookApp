package com.example.coolbookshelf.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coolbookshelf.R
import com.example.coolbookshelf.adapter.BookAdapter
import com.example.coolbookshelf.data.network.NetworkListener
import com.example.coolbookshelf.util.NetworkResult
import com.example.coolbookshelf.databinding.FragmentBooksBinding
import com.example.coolbookshelf.ui.observeOnce
import com.example.coolbookshelf.viewmodel.BookViewModel
import com.example.coolbookshelf.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class BookFragment : Fragment(), SearchView.OnQueryTextListener {

    private val args by navArgs<BookFragmentArgs>()
    private val mAdapter by lazy { BookAdapter() }

    private var _binding : FragmentBooksBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var booksViewModel: BookViewModel

    private lateinit var networkListener : NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        booksViewModel = ViewModelProvider(requireActivity())[BookViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBooksBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)

        setupRecyclerView()
        readDatabase()

        booksViewModel.readBackOnline.observe(viewLifecycleOwner,{
            booksViewModel.backOnline = it
        })


        lifecycleScope.launch{
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    booksViewModel.networkStatus = status
                    booksViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

    binding.bookFab.setOnClickListener{
        findNavController().navigate(R.id.action_fragment_books_to_bookBottomSheet)
    }
        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.book_menu, menu)

        val search = menu.findItem(R.id.book_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    private fun readDatabase() {
        Log.d("readDataBase","CALLED")
        lifecycleScope.launch {
            mainViewModel.readBook.observeOnce(viewLifecycleOwner, {database->
                if(database.isNotEmpty() && !args.bbackFromBottomSheet){
                    Log.d("readDataBase1","CALLED not empty")
                    mAdapter.setData(database[0].book)
                    hideShimmerEffects()
                }else{
                    requestApiData()}
            })
        }
    }

    private fun searchApiData(searchQuery: String){
       // showShimmerEffect()
        Log.d("searchAPI","in searchApiData")
        mainViewModel.searchBooks(booksViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchBooksResponse.observe(viewLifecycleOwner,{response ->
            when(response){
                is NetworkResult.Success->{
                    hideShimmerEffects()
                    val book = response.data
                    book?.let{mAdapter.setData(it)}
                    Log.d("response","response")
                }
                is NetworkResult.Error ->{
                    hideShimmerEffects()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT).show()
                    Log.d("error","error")
                }
                is NetworkResult.Loading ->{
                    showShimmerEffect()
                    Log.d("loading","loading")
                }
            }
        })
    }

    private fun loadDataFromCache(){
        lifecycleScope.launch{
            mainViewModel.readBook.observe(viewLifecycleOwner,{database ->
                if(database.isNotEmpty()){
                    mAdapter.setData(database[0].book)
                }
            })
        }
    }

    private fun requestApiData(){
        mainViewModel.getBooks(booksViewModel.applyQueries())
        mainViewModel.bookResponse.observe(viewLifecycleOwner, { response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffects()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error->{
                    hideShimmerEffects()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(), response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun setupRecyclerView(){
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }
    private fun showShimmerEffect(){
        binding.recyclerView.showShimmer()
    }
    private fun hideShimmerEffects(){
        binding.recyclerView.hideShimmer()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

}