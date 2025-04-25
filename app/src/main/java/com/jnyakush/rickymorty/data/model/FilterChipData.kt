package com.jnyakush.rickymorty.data.model

data class FilterChipData(
    val label: String,
    val onRemove: () -> Unit
)