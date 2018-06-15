package com.kitobim.ui.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kitobim.R
import com.kitobim.data.local.database.entity.GenreEntity
import com.kitobim.ui.custom.ImageHelper
import kotlinx.android.synthetic.main.item_grid_genre.view.*
import kotlinx.android.synthetic.main.item_list_genre.view.*

class GenreAdapter(private val context: Context, private val isGridLayout: Boolean = false)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mList = emptyList<GenreEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isGridLayout) {
            GridViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_grid_genre, parent, false) as CardView)
        } else {
            ListViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_genre, parent, false) as ConstraintLayout)
        }
    }

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) {
        val genre = mList[position]

        if (isGridLayout) {
            val holder = viewholder as GridViewHolder
            holder.name.text = genre.name

            if (genre.image != null) {
                ImageHelper.setBookCover(holder.thumbnail, genre.image)
            }
        } else {
            val holder = viewholder as ListViewHolder
            holder.name.text = genre.name

            if (genre.image != null) {
                ImageHelper.setBookCover(holder.thumbnail, genre.image)
            }
        }
    }

    override fun getItemCount(): Int = mList.size

    fun updateData(list: List<GenreEntity>) {
        mList = list
        notifyDataSetChanged()
    }

    fun clearData() {
        mList = emptyList()
        notifyDataSetChanged()
    }

    fun getItem(position: Int) = mList[position]

    inner class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val name: TextView = view.txt_name_list_genre
        val thumbnail: ImageView = view.img_list_genre
    }

    inner class GridViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val name: TextView = view.txt_name_grid_genre
        val thumbnail: ImageView = view.img_grid_genre
    }
}
