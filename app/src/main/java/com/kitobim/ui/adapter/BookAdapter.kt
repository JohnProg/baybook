package com.kitobim.ui.adapter

import android.content.Context
import android.support.v7.widget.AppCompatRatingBar
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.kitobim.R
import com.kitobim.data.local.database.entity.BookEntity
import com.kitobim.ui.custom.ImageHelper
import kotlinx.android.synthetic.main.item_grid_book.view.*
import kotlinx.android.synthetic.main.item_list_book.view.*


class BookAdapter(private val context: Context, private val isGridLayout: Boolean = false)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mList = emptyList<BookEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isGridLayout) {
            GridViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_grid_book, parent, false) as LinearLayout)

        } else {
            ListViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_book, parent, false) as CardView)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val book = mList[position]

        if (isGridLayout) {
            val holder = viewHolder as GridViewHolder

            holder.title.text = book.title
            if (book.thumbnail != null) {
                ImageHelper.setBookCover(holder.coverImage, book.thumbnail)
            }
        } else {
            val holder = viewHolder as ListViewHolder
            val currency = context.resources.getString(R.string.sum)

            holder.title.text = book.title

//            if (book.authors.isNotEmpty()) {
//                holder.author.text = TextUtils.join(", ", book.authors.map { it.name })
//            }
//
            if (book.rating != null && book.rating >= 0 && book.rating <= 5) {
                holder.rateBar.rating = book.rating
                holder.rateText.text = book.rating.toString()
            }

            holder.price.text = if (book.price > 0) {
                "${(book.price)} $currency"
            } else {
                context.resources.getString(R.string.free)
            }

            if (book.thumbnail != null) {
                ImageHelper.setBookCover(holder.coverImage, book.thumbnail)
            }
        }
    }

    override fun getItemCount(): Int = mList.size

    fun updateData(list: List<BookEntity>) {
        mList = list
        notifyDataSetChanged()
    }

    fun clearData() {
        mList = emptyList()
        notifyDataSetChanged()
    }


    fun getItem(position: Int) = mList[position]

    inner class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = view.title_list_book
        val author: TextView = view.txt_authors_book
        val price: TextView = view.txt_book_price
        val coverImage: ImageView = view.img_list_book
        val rateBar: AppCompatRatingBar = view.rating_bar_list
        val rateText: TextView = view.txt_rating_list
    }

    inner class GridViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = view.title_grid_book
        val coverImage: ImageView = view.img_grid_book
    }
}