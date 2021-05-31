package com.zoho.countries.utils

import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import androidx.appcompat.widget.AppCompatImageView
import com.zoho.countries.R

fun AppCompatImageView.loadSvg(url: String) {
    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
        .build()

    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .placeholder(R.drawable.flag_placeholder)
        .error(R.drawable.flag_placeholder)
        .data(url)
        .target(this)
        .build()

    imageLoader.enqueue(request)
}