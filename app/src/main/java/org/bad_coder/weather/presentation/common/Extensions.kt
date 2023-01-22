package org.bad_coder.weather.presentation.common

import android.view.View
import androidx.fragment.app.Fragment

fun View.toHide() {
    this.visibility = View.GONE
}

fun View.toShow() {
    this.visibility = View.VISIBLE
}

fun Fragment.getDrawableByName(name: String): Int {
    return resources.getIdentifier(
        name,
        "drawable",
        requireActivity().packageName
    )
}