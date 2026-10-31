package com.experiencetv.core

data class MediaTitle(
    val id: String,
    val name: String,
    val year: Int,
    val rating: String,
    val genre: String,
    val synopsis: String,
    val artwork: String,
    val featured: Boolean = false,
    val isSeries: Boolean = false
)

object Catalogue {
    // Original demo entries and artwork placeholders; replace with licensed catalogue data from the API.
    val titles = listOf(
        MediaTitle("beyond-earth", "Beyond Earth", 2025, "PG-13", "Sci-Fi", "A lone researcher receives a signal from beyond the solar system and must decide whether humanity is ready to answer.", "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?w=1200", true),
        MediaTitle("silent-coast", "The Silent Coast", 2024, "PG", "Drama", "A quiet coastal town is changed forever when an old lighthouse begins shining again.", "https://images.unsplash.com/photo-1518837695005-2083093ee35b?w=900"),
        MediaTitle("wild-frontier", "Wild Frontier", 2025, "PG", "Documentary", "Journey through remote landscapes and meet the people protecting the world's wild places.", "https://images.unsplash.com/photo-1472396961693-142e6e269027?w=900"),
        MediaTitle("neon-city", "Neon City", 2023, "16+", "Thriller", "In a city that never sleeps, one missing file exposes a web of secrets.", "https://images.unsplash.com/photo-1519608487953-e999c86e7455?w=900"),
        MediaTitle("blue-horizon", "Blue Horizon", 2024, "PG", "Adventure", "A young crew sets sail to chart an ocean no map has ever captured.", "https://images.unsplash.com/photo-1500375592092-40eb2168fd21?w=900"),
        MediaTitle("the-makers", "The Makers", 2025, "PG", "Series", "Meet the inventors, artists and dreamers building tomorrow, one idea at a time.", "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=900", isSeries = true)
    )
    val featured get() = titles.first { it.featured }
    fun find(id: String) = titles.firstOrNull { it.id == id }
}
