package com.example.subsmission3_robbyramadhana_md_07

import androidx.annotation.StringRes

object Complement {

    const val SPLASH_TIME = 3000L

    const val EXTRA_USER = "EXTRA_USER"

    @StringRes
    val TAB_TITLES = intArrayOf(
        R.string.followers,
        R.string.following
    )

    const val BASE_URL = "https://api.github.com"
}