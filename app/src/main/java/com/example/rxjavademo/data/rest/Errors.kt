package com.example.rxjavademo.data.rest

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Errors(
    @SerializedName("Code")
    val Code: String="",
    @SerializedName("Description")
    val Description: String=""
) : Serializable