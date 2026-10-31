package com.experiencetv.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.experiencetv.core.Catalogue
import com.experiencetv.core.MediaTitle

private val Ink = Color(0xFF090A0E)
private val Panel = Color(0xFF171922)
private val Gold = Color(0xFFE9B85C)
private val Muted = Color(0xFFA6A8B2)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(9, 10, 14)
        window.navigationBarColor = android.graphics.Color.rgb(9, 10, 14)
        setContent { ExperienceTheme { ExperienceApp() } }
    }
}

@Composable
private fun ExperienceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Gold, onPrimary = Ink, background = Ink, surface = Panel,
            onBackground = Color.White, onSurface = Color.White, secondary = Gold
        ),
        content = content
    )
}

@Composable
private fun ExperienceApp() {
    var tab by remember { mutableStateOf("Home") }
    var query by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf<MediaTitle?>(null) }
    val saved = remember { mutableStateListOf<String>() }
    val titles = Catalogue.titles

    Scaffold(
        containerColor = Ink,
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF11131A), contentColor = Color.White) {
                listOf("Home", "Discover", "Search", "My List").forEach { item ->
                    NavigationBarItem(
                        selected = tab == item,
                        onClick = { tab = item; selected = null },
                        icon = { Text(when (item) { "Home" -> "⌂"; "Discover" -> "✦"; "Search" -> "⌕"; else -> "♡" }, fontSize = 20.sp) },
                        label = { Text(item, fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Gold, selectedTextColor = Gold, indicatorColor = Panel, unselectedIconColor = Muted, unselectedTextColor = Muted)
                    )
                }
            }
        }
    ) { padding ->
        if (selected != null) {
            DetailScreen(selected!!, saved.contains(selected!!.id), { if (saved.contains(selected!!.id)) saved.remove(selected!!.id) else saved.add(selected!!.id) }, { selected = null }, { /* Playback service is connected in a later milestone. */ })
        } else {
            Column(Modifier.fillMaxSize().padding(padding)) {
                when (tab) {
                    "Search" -> {
                        Header()
                        OutlinedTextField(
                            value = query, onValueChange = { query = it }, modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp),
                            placeholder = { Text("Search movies and series", color = Muted) }, singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Gold, unfocusedBorderColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                        val results = titles.filter { it.name.contains(query, true) || it.genre.contains(query, true) }
                        TitleGrid(results) { selected = it }
                    }
                    "My List" -> {
                        Header()
                        if (saved.isEmpty()) EmptyState("Your list is waiting", "Save films and series to find them here.")
                        else TitleGrid(titles.filter { saved.contains(it.id) }) { selected = it }
                    }
                    "Discover" -> {
                        Header()
                        Text("Explore all stories", Modifier.padding(horizontal = 20.dp, vertical = 12.dp), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        TitleGrid(titles) { selected = it }
                    }
                    else -> HomeScreen(onSelect = { selected = it }, saved = saved)
                }
            }
        }
    }
}

@Composable
private fun Header() {
    Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Text("EXPERIENCE", color = Gold, fontWeight = FontWeight.Black, letterSpacing = 2.sp, fontSize = 18.sp)
        Text(" TV", color = Color.White, fontWeight = FontWeight.Light, fontSize = 18.sp)
        Spacer(Modifier.weight(1f))
        Text("●", color = Gold, fontSize = 10.sp)
        Text("  Guest", color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun HomeScreen(onSelect: (MediaTitle) -> Unit, saved: List<String>) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Header()
        val hero = Catalogue.featured
        Box(Modifier.fillMaxWidth().height(430.dp)) {
            AsyncImage(hero.artwork, null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Ink.copy(alpha = .35f), Ink))))
            Column(Modifier.align(Alignment.BottomStart).padding(horizontal = 22.dp, vertical = 24.dp)) {
                Text("EXPERIENCE ORIGINAL", color = Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Text(hero.name, color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Black)
                Text("${hero.year}  •  ${hero.rating}  •  ${hero.genre}", color = Muted, fontSize = 12.sp)
                Spacer(Modifier.height(8.dp))
                Text(hero.synopsis, color = Color.White.copy(alpha = .86f), fontSize = 13.sp, lineHeight = 19.sp, maxLines = 3)
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = { onSelect(hero) }, colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink)) { Text("▶  Explore title", fontWeight = FontWeight.Bold) }
                    OutlinedButton(onClick = { onSelect(hero) }, colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)) { Text("More info") }
                }
            }
        }
        Section("Trending now", Catalogue.titles, onSelect)
        Section("Series to discover", Catalogue.titles.filter { it.isSeries || it.genre == "Drama" }, onSelect)
        Spacer(Modifier.height(20.dp))
        Text("Entertainment without boundaries.", Modifier.align(Alignment.CenterHorizontally).padding(16.dp), color = Muted, fontSize = 11.sp)
    }
}

@Composable
private fun Section(title: String, items: List<MediaTitle>, onSelect: (MediaTitle) -> Unit) {
    Column(Modifier.padding(top = 18.dp)) {
        Text(title, Modifier.padding(horizontal = 20.dp, vertical = 10.dp), color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
        LazyRow(contentPadding = PaddingValues(horizontal = 18.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(items) { media ->
                Column(Modifier.width(142.dp).clickable { onSelect(media) }) {
                    AsyncImage(media.artwork, media.name, Modifier.fillMaxWidth().height(190.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
                    Spacer(Modifier.height(7.dp))
                    Text(media.name, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, maxLines = 1)
                    Text("${media.year} • ${media.genre}", color = Muted, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
private fun TitleGrid(items: List<MediaTitle>, onSelect: (MediaTitle) -> Unit) {
    androidx.compose.foundation.lazy.LazyVerticalGrid(
        columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { media ->
            Column(Modifier.clickable { onSelect(media) }) {
                AsyncImage(media.artwork, media.name, Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
                Spacer(Modifier.height(7.dp))
                Text(media.name, color = Color.White, fontWeight = FontWeight.SemiBold)
                Text("${media.year} • ${media.genre}", color = Muted, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun DetailScreen(media: MediaTitle, isSaved: Boolean, onSave: () -> Unit, onBack: () -> Unit, onPlay: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Box(Modifier.fillMaxWidth().height(320.dp)) {
            AsyncImage(media.artwork, media.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Ink))))
            Text("‹  Back", Modifier.align(Alignment.TopStart).padding(18.dp).clip(RoundedCornerShape(20.dp)).background(Ink.copy(.7f)).clickable { onBack() }.padding(horizontal = 14.dp, vertical = 8.dp), color = Color.White)
        }
        Column(Modifier.padding(horizontal = 22.dp)) {
            Text(media.name, color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Black)
            Text("${media.year}  •  ${media.rating}  •  ${media.genre}", color = Gold, fontSize = 12.sp)
            Spacer(Modifier.height(14.dp))
            Text(media.synopsis, color = Muted, fontSize = 14.sp, lineHeight = 22.sp)
            Spacer(Modifier.height(22.dp))
            Button(onClick = onPlay, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Ink)) { Text("▶   Play", fontWeight = FontWeight.Bold) }
            OutlinedButton(onClick = onSave, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)) { Text(if (isSaved) "✓  Added to My List" else "+  Add to My List") }
            Text("Playback will become available when a licensed stream provider is connected.", color = Muted, fontSize = 11.sp, modifier = Modifier.padding(top = 12.dp, bottom = 30.dp))
        }
    }
}

@Composable
private fun EmptyState(title: String, subtitle: String) {
    Column(Modifier.fillMaxSize().padding(30.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("♡", color = Gold, fontSize = 42.sp)
        Text(title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, color = Muted, fontSize = 13.sp)
    }
}
