package com.example.subsmission3_robbyramadhana_md_07

import com.squareup.moshi.Json

data class SearchList(
    @field:Json(name = "items")
    val items: List<UserData>
)
