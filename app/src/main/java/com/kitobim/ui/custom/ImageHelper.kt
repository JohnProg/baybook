package com.kitobim.ui.custom

import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso




object ImageHelper {

    fun setBookCover(view: ImageView, url: String) {
        Picasso.get()
                .load(url)
                .fit()
                .transform(RoundedCornersTransformation(4f, 0))
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(view, object : Callback {
                    override fun onSuccess() {
                        Log.i("Picasso", "Rectangle image from cache: $url")
                    }

                    override fun onError(e: Exception) {
                        Picasso.get()
                                .load(url)
                                .fit()
                                .transform(RoundedCornersTransformation(4f, 0))
                                .into(view, object : Callback {
                                    override fun onSuccess() {
                                        Log.i("Picasso", "Rectangle image from network: $url")
                                    }

                                    override fun onError(e: Exception) {
                                        Log.v("Picasso", "Could not fetch image")
                                    }
                                })
                    }
                })
    }

    fun setAuthorImage(view: ImageView, url: String) {
        Picasso.get()
                .load(url)
                .fit()
                .transform(CircleTransform())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(view, object : Callback {
                    override fun onSuccess() {
                        Log.i("Picasso", "Circle image from cache: $url")
                    }

                    override fun onError(e: Exception) {
                        Picasso.get()
                                .load(url)
                                .fit()
                                .transform(CircleTransform())
                                .into(view, object : Callback {
                                    override fun onSuccess() {
                                        Log.i("Picasso", "Circle image from network: $url")
                                    }

                                    override fun onError(e: Exception) {
                                        Log.v("Picasso", "Could not fetch image")
                                    }
                                })
                    }
                })
    }

}