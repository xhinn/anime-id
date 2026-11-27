package com.animeid.app

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// Umpan balik lompat ketuk dua kali, gaya 2026: sapuan cahaya lembut di sisi yang diketuk,
// riak melebar dari titik ketuk, dan pil kaca berisi panah bergelombang + total detik
// yang "memantul" tiap ketukan.
@Composable
fun UmpanBalikLompat(
    aktif: Boolean,
    arah: Int,
    total: Int,
    kunci: Int,
    titik: Offset,
    modifier: Modifier = Modifier,
) {
    val alfa by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (aktif) 1f else 0f,
        animationSpec =
            androidx.compose.animation.core
                .tween(if (aktif) 80 else 280),
        label = "lompatAlfa",
    )
    val riak =
        remember {
            androidx.compose.animation.core
                .Animatable(1f)
        }
    val pantul =
        remember {
            androidx.compose.animation.core
                .Animatable(1f)
        }
    LaunchedEffect(kunci) {
        if (kunci > 0) {
            launch {
                riak.snapTo(0f)
                riak.animateTo(
                    1f,
                    androidx.compose.animation.core
                        .tween(550, easing = LinearOutSlowInEasing),
                )
            }
            pantul.snapTo(0.84f)
            pantul.animateTo(
                1f,
                androidx.compose.animation.core
                    .spring(dampingRatio = 0.45f, stiffness = 520f),
            )
        }
    }
    val gerak =
        androidx.compose.animation.core
            .rememberInfiniteTransition(label = "lompatPanah")
    val fase by gerak.animateFloat(
        initialValue = 0f,
        targetValue = 2f,
        animationSpec =
            androidx.compose.animation.core.infiniteRepeatable(
                androidx.compose.animation.core
                    .tween(700, easing = androidx.compose.animation.core.LinearEasing),
            ),
        label = "lompatFase",
    )
    if (alfa <= 0.01f) return

    val maju = arah > 0
    val sisi = if (maju) Alignment.CenterEnd else Alignment.CenterStart
    Box(modifier.graphicsLayer { this.alpha = alfa }) {
        // sapuan cahaya lembut (tanpa tepi keras) di setengah layar yang diketuk
        Box(
            Modifier.align(sisi).fillMaxHeight().fillMaxWidth(0.5f).background(
                Brush.horizontalGradient(
                    if (maju) {
                        listOf(Color.Transparent, Color.White.copy(alpha = 0.16f))
                    } else {
                        listOf(Color.White.copy(alpha = 0.16f), Color.Transparent)
                    },
                ),
            ),
        )
        // riak dari titik ketuk
        Canvas(Modifier.matchParentSize()) {
            val p = riak.value
            if (p < 1f) {
                drawCircle(
                    Color.White.copy(alpha = 0.22f * (1f - p)),
                    radius = size.width * 0.32f * p,
                    center = titik,
                )
            }
        }
        // pil kaca: panah + total detik
        Box(Modifier.align(sisi).fillMaxHeight().fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
            val panah: @Composable () -> Unit = {
                Row(horizontalArrangement = Arrangement.spacedBy((-4).dp)) {
                    for (i in 0 until 2) {
                        val urut = if (maju) i else 1 - i // gelombang bergerak ke arah lompat
                        val terang = fase.toInt().coerceIn(0, 1) == urut
                        Canvas(Modifier.size(width = 14.dp, height = 18.dp).rotate(if (maju) 0f else 180f)) {
                            val p = Path()
                            p.moveTo(size.width * 0.3f, size.height * 0.18f)
                            p.lineTo(size.width * 0.75f, size.height * 0.5f)
                            p.lineTo(size.width * 0.3f, size.height * 0.82f)
                            drawPath(
                                p,
                                Color.White.copy(alpha = if (terang) 1f else 0.4f),
                                style =
                                    Stroke(
                                        width = size.width * 0.2f,
                                        cap = androidx.compose.ui.graphics.StrokeCap.Round,
                                        join = androidx.compose.ui.graphics.StrokeJoin.Round,
                                    ),
                            )
                        }
                    }
                }
            }
            Row(
                Modifier
                    .graphicsLayer {
                        scaleX = pantul.value
                        scaleY = pantul.value
                    }.clip(RoundedCornerShape(50))
                    .background(Color.Black.copy(alpha = 0.42f))
                    .tepiKaca()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (!maju) panah()
                androidx.compose.animation.AnimatedContent(
                    targetState = total,
                    transitionSpec = {
                        (androidx.compose.animation.slideInVertically { it / 2 } + fadeIn()) togetherWith
                            (androidx.compose.animation.slideOutVertically { -it / 2 } + fadeOut())
                    },
                    label = "lompatAngka",
                ) { n ->
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            (if (maju) "+" else "\u2212") + n,
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(Modifier.width(3.dp))
                        Text(
                            "dtk",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(bottom = 2.dp),
                        )
                    }
                }
                if (maju) panah()
            }
        }
    }
}

// garis tepi tipis pada pil kaca
private fun Modifier.tepiKaca(): Modifier = this.border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(50))
