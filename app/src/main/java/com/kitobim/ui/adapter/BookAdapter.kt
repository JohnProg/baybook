package com.kitobim.ui.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kitobim.R
import com.kitobim.data.local.database.entity.BookEntity
import com.kitobim.ui.custom.CoverView
import kotlinx.android.synthetic.main.item_list_book.view.*


class BookAdapter(private val context: Context,
                  private val isGridLayout: Boolean = false)
    : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private var mList = emptyList<BookEntity>()
    private lateinit var mClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = if (isGridLayout) {
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_grid_book, parent, false) as ConstraintLayout
        } else {
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_book, parent, false) as ConstraintLayout
        }

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = mList[position]
        val currency = context.resources.getString(R.string.sum)

        holder.bind(mList[position].id, mClickListener)
        holder.author.text = book.authors
        holder.title.text = book.title
        holder.price.text = if (book.price == 0) {
            "${(book.price)} $currency"
        } else {
            context.resources.getString(R.string.free)
        }

        if (book.thumbnail != null) {
            holder.coverImage.setImageUrl(book.thumbnail)
        }
    }

    override fun getItemCount(): Int = mList.size

    fun updateBooks(list: List<BookEntity>) {
        mList = list
        notifyDataSetChanged()
    }
    fun setItemClickListener(listener: OnItemClickListener) {
        mClickListener = listener
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){

        val title: TextView = view.card_title
        val author: TextView = view.card_subtitle
        val price: TextView = view.card_price
        val coverImage: CoverView = view.card_cover

        fun bind(item: Int, listener: OnItemClickListener) {
            itemView.setOnClickListener { listener.onItemClick(item) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }

}