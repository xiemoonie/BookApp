package com.example.coolbookshelf.adapter

import android.os.Handler
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.coolbookshelf.R
import com.example.coolbookshelf.data.database.entities.FavoritesEntity
import com.example.coolbookshelf.databinding.FavoritesRowLayoutBinding
import com.example.coolbookshelf.ui.fragments.FavoriteBookFragmentDirections
import com.example.coolbookshelf.util.BookDiffUtil
import com.example.coolbookshelf.viewmodel.MainViewModel

import com.google.android.material.snackbar.Snackbar

class FavoriteBooksAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<FavoriteBooksAdapter.MyViewHolder>(), ActionMode.Callback{

    private var multiSelection = false
    private var selectedBooks = arrayListOf<FavoritesEntity>()

    private var starAnim = false

    private var myViewHolders = arrayListOf<MyViewHolder>()
    private lateinit var rootView: View

    private lateinit var mActionMode: ActionMode

private var favoriteBooks = emptyList<FavoritesEntity>()

    class MyViewHolder (val binding: FavoritesRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(favoritesEntity: FavoritesEntity) {
            binding.favoriteEntity = favoritesEntity
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): FavoriteBooksAdapter.MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoritesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return FavoriteBooksAdapter.MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        myViewHolders.add(holder)
        rootView = holder.itemView.rootView

       val selectedBook = favoriteBooks[position]
        holder.bind(selectedBook)
        /* *
        * Single Click Listener
        * */
        holder.binding.favoritesBookRowLayout.setOnClickListener{

            if(multiSelection){
             applySelectedItem(holder, selectedBook)
            }else{
              val action = FavoriteBookFragmentDirections.actionFragmentFavoriteToDetailsActivity(selectedBook.item)
              holder.itemView.findNavController().navigate(action)

            }
        }
         /* *
         * Long Click Listener
         * */
        holder.binding.favoritesBookRowLayout.setOnLongClickListener{
            if(!multiSelection){
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelectedItem(holder, selectedBook)
                true
            }else{
                multiSelection = false
                false
            }
        }

        if(selectedBooks.contains(selectedBook) && starAnim == true){
            Log.d("currentState","" +holder.binding.favoritesBookRowLayout.currentState)

            holder.binding.favoritesBookRowLayout.transitionToState(R.id.end)
            Log.d("currentState2","" +R.id.end)
        }

    }

    fun applySelectedItem(holder: MyViewHolder, currentBook: FavoritesEntity){

        if(selectedBooks.contains(currentBook)){
            selectedBooks.remove(currentBook)
            changeBookStyle(holder,R.color.transparent, R.color.transparent)
            applyActionModeTitle()
        }else{
            selectedBooks.add(currentBook)
            changeBookStyle(holder, R.color.transparent, R.color.transparent)
            applyActionModeTitle()
        }
    }

    private fun ItemErased(selectedItemsToDelete: ArrayList <FavoritesEntity>){
       starAnim = true;
        Log.d("itemErased","it works")
        notifyDataSetChanged()
    }

    private fun changeBookStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int){

        holder.binding.favoritesBookRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.binding.favoriteCardBook.setCardBackgroundColor((ContextCompat.getColor(requireActivity, strokeColor)))
    }

    private fun applyActionModeTitle(){
        when(selectedBooks.size){
            0 ->{
                mActionMode.finish()
            }
            1 ->{
                mActionMode.title = "${selectedBooks.size} item selected"
            }
            else ->{
                mActionMode.title = "${selectedBooks.size} items selected"
            }
        }
    }

    override fun getItemCount(): Int {
       return favoriteBooks.size
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorite_contextual_menu, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
       // applyStatusBarColor(R.color.transparent)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
       return true
    }



    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
      if(menu?.itemId == R.id.delete_favorite_item){
          ItemErased(selectedBooks)
          Handler().postDelayed({
              starAnim = false
              selectedBooks.forEach{
                  mainViewModel.deleteFavoriteBook(it)
              }
              selectedBooks.clear()
              multiSelection = false
              // selectedBooks.clear()
              actionMode?.finish()
          }, 2000)
          if(selectedBooks.size == 1){
              showSnackBar("${selectedBooks.size}Book removed.")
          }else{
              showSnackBar("${selectedBooks.size}Books removed.")
          }
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolders.forEach{ holder->
            changeBookStyle(holder, R.color.nocolor, R.color.transparent)
            applyStatusBarColor(R.color.statusBar)
            multiSelection = false
            selectedBooks.clear()
          }
    }

    private fun applyStatusBarColor(color: Int){
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)
    }

    fun setData(newFavoriteBooks: List<FavoritesEntity>){
        val favoriteBookDiffuilt = BookDiffUtil(favoriteBooks, newFavoriteBooks)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteBookDiffuilt)
        favoriteBooks = newFavoriteBooks
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message: String){
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}.show()
    }

    fun clearContextualActionMode(){
        if(this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }
}