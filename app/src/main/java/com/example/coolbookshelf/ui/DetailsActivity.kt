package com.example.coolbookshelf.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.coolbookshelf.R
import com.example.coolbookshelf.adapter.PagerAdapter
import com.example.coolbookshelf.data.database.entities.FavoritesEntity
import com.example.coolbookshelf.databinding.ActivityDetailsBinding
import com.example.coolbookshelf.ui.fragments.information.InformationFragment
import com.example.coolbookshelf.ui.fragments.overview.OverviewFragment
import com.example.coolbookshelf.util.Constants.Companion.BOOK_RESULT_KEY
import com.example.coolbookshelf.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private var bookSaved = false
    private var savedBookId = 0

    lateinit var binding: ActivityDetailsBinding
    private val args by navArgs<DetailsActivityArgs>()

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(InformationFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Information")


        val resultBundle = Bundle()
        resultBundle.putParcelable(BOOK_RESULT_KEY, args.item)

        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager
        )

        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)


        var view = binding.root
        setContentView(view)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favorites_menu)
        checkSavedBooks(menuItem!!)
        return true
    }

    private fun checkSavedBooks(menuItem: MenuItem) {
        mainViewModel.readFavoriteBooks.observe(this, { favoritesEntity ->
            try {
                for (savedBook in favoritesEntity) {
                    if (savedBook.item.id == args.item.id) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedBookId = savedBook.id
                        bookSaved = true
                    } else {
                        changeMenuItemColor(menuItem, R.color.white)
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        })
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon?.setTint(ContextCompat.getColor(this,color))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }else if(item.itemId == R.id.save_to_favorites_menu && !bookSaved){
            saveToFavorites(item)
        }else if(item.itemId == R.id.save_to_favorites_menu && bookSaved){
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavorites(item: MenuItem){
        val favoritesEntity =
            FavoritesEntity(
                0,
                args.item
            )
        mainViewModel.insertFavoriteBook(favoritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Book saved.")
        bookSaved = true
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.detailLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoritesEntity =
            FavoritesEntity(
                savedBookId,
                args.item
            )
        mainViewModel.deleteFavoriteBook(favoritesEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from Favorites.")
        bookSaved = false
    }




}