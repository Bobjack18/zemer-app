package com.jtech.zemer.supabase

import kotlinx.serialization.Serializable

fun TrendingWithComparisonRow.toSupabaseItem() = SupabaseItem.TrendingWithComparison(
    trackId = track_id,
    title = title,
    artistNames = artist_names,
    artistIds = artist_ids,
    category = category,
    currentSpot = current_spot,
    previousSpot = previous_spot,
    spotChange = spot_change,
    rank = rank,
)

@Serializable
data class TrendingWithComparisonRow(
    val track_id: String,
    val title: String,
    val artist_names: String,
    val artist_ids: List<String>,
    val category: String,
    val current_spot: Int? = null,
    val previous_spot: Int? = null,
    val spot_change: String? = null,
    val rank: Int? = null,
)

sealed interface SupabaseItem {
    data class TrendingWithComparison(
        val trackId: String,
        val title: String,
        val artistNames: String,
        val artistIds: List<String>,
        val category: String,
        val currentSpot: Int?,
        val previousSpot: Int?,
        val spotChange: String?,
        val rank: Int?,
    ) : SupabaseItem
}
