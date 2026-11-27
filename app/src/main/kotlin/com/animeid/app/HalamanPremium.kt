package com.animeid.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

// ================= Premium =================
// Keuntungan yang ditawarkan. Pembayaran belum tersambung: tombol Upgrade masih "Segera hadir".
private val KeuntunganPremium =
    listOf(
        Triple("Bebas iklan", "Tanpa gangguan", "centang"),
        Triple("Kualitas terbaik", "Resolusi tertinggi", "layarpenuh"),
        Triple("Unduh leluasa", "Simpan untuk offline", "unduhan"),
        Triple("Dukung kami", "Bantu aplikasi berkembang", "hati"),
    )

@Composable
fun HalamanPremium(onKembali: () -> Unit) {
    val ctx = LocalContext.current
    BackHandler { onKembali() }
    val bentuk = RoundedCornerShape(26.dp)
    Column(
        Modifier
            .fillMaxSize()
            .zIndex(2f)
            .background(Latar)
            .statusBarsPadding(),
    ) {
        BilahHalaman("Premium", onKembali)
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 32.dp)) {
            Spacer(Modifier.height(8.dp))
            // Hero
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(bentuk)
                    .background(Brush.linearGradient(listOf(Kartu2, Kartu)))
                    .border(
                        1.dp,
                        Brush.linearGradient(listOf(Aksen.copy(alpha = 0.7f), Garis, Zamrud.copy(alpha = 0.4f))),
                        bentuk,
                    ),
            ) {
                Box(
                    Modifier
                        .matchParentSize()
                        .background(
                            Brush.radialGradient(
                                listOf(Aksen.copy(alpha = 0.22f), Color.Transparent),
                                center = Offset(Float.POSITIVE_INFINITY, 0f),
                                radius = 760f,
                            ),
                        ),
                )
                Column(
                    Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Box(Modifier.size(96.dp).clip(CircleShape).background(Aksen.copy(alpha = 0.10f)))
                        Box(
                            Modifier
                                .size(74.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(Aksen, AksenGelap))),
                            contentAlignment = Alignment.Center,
                        ) { Ikon("mahkota", Modifier.size(40.dp), DiAtasAksen) }
                    }
                    Spacer(Modifier.height(18.dp))
                    Text(
                        "PREMIUM",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = Aksen,
                        modifier =
                            Modifier
                                .clip(RoundedCornerShape(50))
                                .border(1.dp, Aksen.copy(alpha = 0.55f), RoundedCornerShape(50))
                                .padding(horizontal = 14.dp, vertical = 5.dp),
                    )
                    Spacer(Modifier.height(14.dp))
                    Text(
                        "Bebas iklan",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Teks,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
            }
            Spacer(Modifier.height(26.dp))
            JudulMenu("Keuntungan")
            // Kisi 2 x 2
            Column(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                KeuntunganPremium.chunked(2).forEach { baris ->
                    Row(
                        Modifier.fillMaxWidth().height(androidx.compose.foundation.layout.IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        baris.forEach { (judul, isi, ikon) ->
                            Column(
                                Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Kartu)
                                    .border(1.dp, Garis, RoundedCornerShape(20.dp))
                                    .padding(16.dp),
                            ) {
                                Box(
                                    Modifier.size(42.dp).clip(RoundedCornerShape(13.dp)).background(Aksen.copy(alpha = 0.14f)),
                                    contentAlignment = Alignment.Center,
                                ) { Ikon(ikon, Modifier.size(22.dp), Aksen) }
                                Spacer(Modifier.height(14.dp))
                                Text(judul, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Teks)
                                Spacer(Modifier.height(4.dp))
                                Text(isi, fontSize = 12.5.sp, lineHeight = 18.sp, color = Redup)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(28.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(58.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Brush.linearGradient(listOf(Aksen, AksenGelap)))
                    .clickable {
                        android.widget.Toast
                            .makeText(ctx, "Segera hadir", android.widget.Toast.LENGTH_SHORT)
                            .show()
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Ikon("mahkota", Modifier.size(22.dp), DiAtasAksen)
                Spacer(Modifier.width(10.dp))
                Text("Langganan", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = DiAtasAksen)
            }
            Spacer(Modifier.height(10.dp))
            Text(
                "Segera hadir",
                fontSize = 12.sp,
                color = Samar,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
