package com.experiencetv.androidtv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.experiencetv.core.Catalogue
import com.experiencetv.core.MediaTitle

private val TvBg = Color(0xFF08090D)
private val TvGold = Color(0xFFE9B85C)
private val TvMuted = Color(0xFFB0B2BA)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(8, 9, 13)
        window.navigationBarColor = android.graphics.Color.rgb(8, 9, 13)
        setContent { MaterialTheme(colorScheme = darkColorScheme(primary = TvGold, background = TvBg, surface = Color(0xFF171922))) { TvExperienceApp() } }
    }
}

@Composable
private fun TvExperienceApp() {
    var selected by remember { mutableStateOf<MediaTitle?>(null) }
    var section by remember { mutableStateOf("Home") }
    val saved = remember { mutableStateListOf<String>() }
    if (selected != null) {
        TvDetail(selected!!, saved.contains(selected!!.id), { if (saved.contains(selected!!.id)) saved.remove(selected!!.id) else saved.add(selected!!.id) }, { selected = null })
    } else {
        Row(Modifier.fillMaxSize().background(TvBg)) {
            Column(Modifier.width(190.dp).fillMaxHeight().padding(start = 26.dp, top = 34.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("EXPERIENCE", color = TvGold, fontWeight = FontWeight.Black, letterSpacing = 2.sp, fontSize = 18.sp)
                Text("TV", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Light)
                Spacer(Modifier.height(18.dp))
                listOf("Home", "Discover", "My List").forEach { item ->
                    TvNavItem(item, section == item) { section = item }
                }
                Spacer(Modifier.weight(1f))
                Text("Entertainment\nwithout boundaries.", color = TvMuted, fontSize = 11.sp, lineHeight = 16.sp)
            }
            Column(Modifier.fillMaxSize().padding(top = 30.dp, bottom = 26.dp)) {
                Text(when(section) { "My List" -> "Your watchlist"; "Discover" -> "Explore stories"; else -> "Welcome to Experience TV" }, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(18.dp))
                if (section == "Home") {
                    TvHero(Catalogue.featured) { selected = Catalogue.featured }
                    Spacer(Modifier.height(22.dp))
                    TvRow("Trending now", Catalogue.titles) { selected = it }
                    Spacer(Modifier.height(16.dp))
                    TvRow("Discover more", Catalogue.titles.reversed()) { selected = it }
                } else {
                    val list = if (section == "My List") Catalogue.titles.filter { saved.contains(it.id) } else Catalogue.titles
                    if (list.isEmpty()) Text("Your list is empty. Open a title and add it to My List.", color = TvMuted)
                    else TvRow(if (section == "My List") "Saved titles" else "All titles", list) { selected = it }
                }
            }
        }
    }
}

@Composable
private fun TvNavItem(label: String, active: Boolean, onClick: () -> Unit) {
    var focused by remember { mutableStateOf(false) }
    Text(label, Modifier.fillMaxWidth().onFocusChanged { focused = it.isFocused }.clip(RoundedCornerShape(12.dp))
        .background(if (active || focused) Color(0xFF28251E) else Color.Transparent)
        .clickable { onClick() }.focusable().padding(horizontal = 16.dp, vertical = 13.dp),
        color = if (active || focused) TvGold else TvMuted, fontWeight = if (active || focused) FontWeight.Bold else FontWeight.Normal)
}

@Composable
private fun TvHero(media: MediaTitle, onClick: () -> Unit) {
    Box(Modifier.fillMaxWidth().height(270.dp).padding(end = 28.dp).clip(RoundedCornerShape(18.dp)).clickable { onClick() }) {
        AsyncImage(media.artwork, media.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(TvBg.copy(.96f), TvBg.copy(.5f), Color.Transparent))))
        Column(Modifier.align(Alignment.CenterStart).width(390.dp).padding(26.dp)) {
            Text("FEATURED", color = TvGold, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Text(media.name, color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Black)
            Text("${media.year}  •  ${media.rating}  •  ${media.genre}", color = TvMuted, fontSize = 13.sp)
            Spacer(Modifier.height(10.dp))
            Text(media.synopsis, color = Color.White, maxLines = 3, fontSize = 13.sp, lineHeight = 18.sp)
            Spacer(Modifier.height(14.dp))
            Text("▶   EXPLORE TITLE", color = TvBg, modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(TvGold).padding(horizontal = 18.dp, vertical = 10.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

@Composable
private fun TvRow(title: String, media: List<MediaTitle>, onSelect: (MediaTitle) -> Unit) {
    Column {
        Text(title, color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            items(media) { item ->
                var focused by remember { mutableStateOf(false) }
                Column(Modifier.width(145.dp).onFocusChanged { focused = it.isFocused }.clickable { onSelect(item) }.focusable()) {
                    AsyncImage(item.artwork, item.name, Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(10.dp))
                        .then(if (focused) Modifier.background(TvGold) else Modifier), contentScale = ContentScale.Crop)
                    Spacer(Modifier.height(6.dp))
                    Text(item.name, color = if (focused) TvGold else Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                    Text("${item.year} • ${item.genre}", color = TvMuted, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
private fun TvDetail(media: MediaTitle, isSaved: Boolean, onSave: () -> Unit, onBack: () -> Unit) {
    Box(Modifier.fillMaxSize().background(TvBg)) {
        AsyncImage(media.artwork, media.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop, alpha = .32f)
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(TvBg, TvBg.copy(.92f), Color.Transparent))))
        Column(Modifier.width(620.dp).align(Alignment.CenterStart).padding(start = 58.dp)) {
            Text("‹  BACK", Modifier.clickable { onBack() }.focusable().padding(8.dp), color = TvGold, fontSize = 13.sp)
            Spacer(Modifier.height(18.dp))
            Text(media.name, color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.Black)
            Text("${media.year}  •  ${media.rating}  •  ${media.genre}", color = TvGold, fontSize = 15.sp)
            Spacer(Modifier.height(18.dp))
            Text(media.synopsis, color = TvMuted, fontSize = 17.sp, lineHeight = 25.sp)
            Spacer(Modifier.height(26.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Button(onClick = { /* Real playback backend is a future milestone. */ }) { Text("▶  Play") }
                OutlinedButton(onClick = onSave) { Text(if (isSaved) "✓  My List" else "+  My List") }
            }
            Text("Streaming becomes available when a licensed media source is connected.", color = TvMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 18.dp))
        }
    }
}
