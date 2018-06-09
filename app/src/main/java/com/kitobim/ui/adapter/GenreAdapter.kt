package com.kitobim.ui.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kitobim.R
import com.kitobim.data.local.database.entity.GenreEntity
import com.kitobim.ui.custom.ImageHelper
import kotlinx.android.synthetic.main.item_list_genre.view.*


class GenreAdapter(private val context: Context)
    : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    private var mList = emptyList<GenreEntity>()
    private lateinit var mClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_genre, parent, false) as ConstraintLayout

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = mList[position]

        holder.bind(mList[position].id, mClickListener)
        holder.name.text = genre.name

        if (genre.image != null) {
            ImageHelper.setBookCover(holder.image, genre.image)
        }
    }

    override fun getItemCount(): Int = mList.size

    fun updateGenres(list: List<GenreEntity>) {
        mList = list
        notifyDataSetChanged()
    }
    fun setItemClickListener(listener: OnItemClickListener) {
        mClickListener = listener
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){

        val name: TextView = view.txt_name_genre
        val image: ImageView = view.img_genre

        fun bind(item: Int, listener: OnItemClickListener) {
            itemView.setOnClickListener { listener.onItemClick(item) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }

}