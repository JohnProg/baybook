package com.kitobim.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kitobim.CoverView
import com.kitobim.R
import com.kitobim.data.model.Book
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_list_item.view.*


class BookAdapter(private val context: Context,
                  private var list: MutableList<Book>,
                  private val isGridLayout: Boolean)
    : RecyclerView.Adapter<BookAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout =
                if (isGridLayout) {
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.recycler_grid_item, parent, false) as ConstraintLayout

                } else {
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.recycler_list_item, parent, false) as ConstraintLayout
                }

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = list[position]

        holder.title.text = book.title
        holder.author.text = book.authors.toString()
        holder.price.text = book.price

        holder.coverImage.setImageUrl(book.cover)
    }

    override fun getItemCount(): Int = list.size

    fun addAllAndUpdate(list: List<Book>) {
        // todo check Picasso fetching and test list.addAll() and notifyDataSetChanged() not worked

        for (book in list) {
            val coverUrl = book.cover
            if (!TextUtils.isEmpty(coverUrl)) {
                Picasso.get().load(coverUrl).fetch()
            }
        }

        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView = view.card_title
        val author: TextView = view.card_subtitle
        val price: TextView = view.card_price
        val coverImage: CoverView = view.card_cover
    }
}