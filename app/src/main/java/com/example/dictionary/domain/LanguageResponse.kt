package com.example.dictionary.domain

import com.google.gson.annotations.SerializedName

data class LanguageResponse (
    @SerializedName("translatedText") val text: String?
)