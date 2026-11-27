@file:OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class,
)

package com.animeid.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.StatFs
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil3.SingletonImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.toBitmap
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

// ================= Palet Zamrud =================
val Latar = Color(0xFF0A140F)
val Kartu = Color(0xFF132219)
val Kartu2 = Color(0xFF1A2D21)
val Garis = Color(0xFF24402F)
val Zamrud = Color(0xFF2FBF8F)
private val ZamrudGelap = Color(0xFF1E7A5A)
private val ZamrudTerang = Color(0xFF4FD6AA) // tint lebih terang dari Zamrud, untuk gradien
private val DiAtasZamrud = Color(0xFF06231A)
val Aksen = ZamrudTerang // aksen Premium = warna zamrud aplikasi
val AksenGelap = Zamrud
val DiAtasAksen = Color(0xFF2B1A00) // warna teks/ikon di atas permukaan Zamrud
val Teks = Color(0xFFF2F7F4)
val Redup = Color(0xFFA7BFB2)
val Samar = Color(0xFF6E8A7B)
private val BintangW = Color(0xFFFFC94D)
private val MerahLembut = Color(0xFFE88A8A)

// ================= Tema (Material 3 Expressive + Inter) =================
private val Inter =
    FontFamily(
        Font(R.font.inter_regular, FontWeight.Normal),
        Font(R.font.inter_medium, FontWeight.Medium),
        Font(R.font.inter_semibold, FontWeight.SemiBold),
        Font(R.font.inter_bold, FontWeight.Bold),
        Font(R.font.inter_extrabold, FontWeight.ExtraBold),
    )

private fun Typography.denganFont(f: FontFamily) =
    copy(
        displayLarge = displayLarge.copy(fontFamily = f),
        displayMedium = displayMedium.copy(fontFamily = f),
        displaySmall = displaySmall.copy(fontFamily = f),
        headlineLarge = headlineLarge.copy(fontFamily = f),
        headlineMedium = headlineMedium.copy(fontFamily = f),
        headlineSmall = headlineSmall.copy(fontFamily = f),
        titleLarge = titleLarge.copy(fontFamily = f),
        titleMedium = titleMedium.copy(fontFamily = f),
        titleSmall = titleSmall.copy(fontFamily = f),
        bodyLarge = bodyLarge.copy(fontFamily = f),
        bodyMedium = bodyMedium.copy(fontFamily = f),
        bodySmall = bodySmall.copy(fontFamily = f),
        labelLarge = labelLarge.copy(fontFamily = f),
        labelMedium = labelMedium.copy(fontFamily = f),
        labelSmall = labelSmall.copy(fontFamily = f),
    )

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TemaAnime(content: @Composable () -> Unit) {
    MaterialExpressiveTheme(
        colorScheme =
            darkColorScheme(
                primary = Zamrud,
                onPrimary = DiAtasZamrud,
                secondary = ZamrudGelap,
                background = Latar,
                onBackground = Teks,
                surface = Kartu,
                onSurface = Teks,
                surfaceVariant = Kartu2,
                outline = Garis,
                error = MerahLembut,
            ),
        motionScheme = MotionScheme.expressive(),
        typography = Typography().denganFont(Inter),
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides LocalTextStyle.current.merge(TextStyle(fontFamily = Inter)),
        ) { content() }
    }
}

private data class Nav(
    val putar: Pair<Judul, Int>?,
    val detail: Judul?,
)

private fun Nav.jenis() =
    if (putar != null) {
        2
    } else if (detail != null) {
        1
    } else {
        0
    }

// Penanda halaman putar untuk nonton jendela kecil (PiP): diisi LayarPutar
// selama halaman putar tampil, dibaca MainActivity saat pengguna meninggalkan
// aplikasi (tombol Beranda) supaya video mengecil jadi jendela melayang.
object KeadaanPip {
    // state Compose: MainActivity memantaunya untuk menyalakan/mematikan masuk-PiP otomatis
    var halamanPutarAktif by mutableStateOf(false)

    // true selama aplikasi berada di jendela kecil (PiP) sistem; dibaca Compose
    var sedangPip by mutableStateOf(false)

    // pemutar yang sedang aktif (halaman putar ATAU mini pemutar): dipakai tombol putar/jeda PiP
    var pemutar: androidx.media3.common.Player? = null

    // status putar terakhir, supaya ikon aksi PiP selalu sesuai
    var sedangMain by mutableStateOf(true)

    // area video di layar (jendela), dipakai sebagai petunjuk animasi masuk/keluar PiP
    var rect by mutableStateOf<android.graphics.Rect?>(null)

    // PiP selalu 16:9 (sama dengan kartu mini dalam aplikasi): petunjuk area dipotong
    // jadi 16:9 di tengah area video, supaya animasi sistem tidak meregang.
    fun rectPip(b: androidx.compose.ui.geometry.Rect): android.graphics.Rect {
        val l = b.width
        val t = b.height
        if (l <= 1f || t <= 1f) return android.graphics.Rect()
        val w = if (l / t > 16f / 9f) t * 16f / 9f else l
        val h = w * 9f / 16f
        val x = b.left + (l - w) / 2f
        val y = b.top + (t - h) / 2f
        return android.graphics.Rect(x.roundToInt(), y.roundToInt(), (x + w).roundToInt(), (y + h).roundToInt())
    }
}

// Putar otomatis ke episode berikutnya membuat ulang halaman putar; penanda ini
// menjaga mode layar penuh (dan kunci lanskap) tetap menyala di antara dua episode.
// Judul yang diminta dibuka dari luar (ketuk notifikasi). Diisi MainActivity,
// dibaca Aplikasi() begitu katalog siap, lalu dikosongkan.
object KeadaanBuka {
    const val EKSTRA = "buka_judul"
    var id: String? by mutableStateOf(null)
}

object KeadaanPutar {
    var penuhLanjut = false
}

// Mini pemutar (PiP dalam aplikasi): saat pengguna menekan kembali dari halaman putar,
// video tidak mati tetapi mengecil jadi kartu melayang di atas halaman mana pun.
object KeadaanMini {
    var judul: Judul? by mutableStateOf(null)
    var indeks by mutableStateOf(0)
    var jalur by mutableStateOf("")
    var posisi by mutableStateOf(0)
    var versi by mutableStateOf(0) // naik tiap sesi baru supaya pemutar mini dibuat ulang

    // sudut tempat kartu terakhir diparkir; bertahan lintas sesi mini dan rotasi
    var diKanan by mutableStateOf(true)
    var diBawah by mutableStateOf(true)

    fun siapkan(
        j: Judul,
        idx: Int,
        alamat: String,
        pos: Int,
    ) {
        indeks = idx
        jalur = alamat
        posisi = pos
        judul = j
        versi += 1
    }

    fun tutup() {
        judul = null
    }
}

// Pengait pramuat: KartuPoster memanggilnya begitu poster mulai ditekan (sebelum jari
// diangkat), Aplikasi() mengisinya. Detail + banner sudah diambil saat ketukan selesai,
// jadi halaman detail terbuka dengan episode dan gambar siap.
object PramuatDetail {
    var aksi: ((Judul) -> Unit)? = null
}

// ================= Model =================
data class Episode(
    val number: Int,
    val playlist: String?,
    val opMulai: Int = -1,
    val opSelesai: Int = -1,
)

data class Judul(
    val id: String,
    val judul: String,
    val poster: String,
    val banner: String,
    val sinopsis: String,
    val tahun: Int,
    val skor: Double,
    val status: String,
    val genres: List<String>,
    val repo: String?,
    val pin: String?,
    val episodes: List<Episode>,
    // asal karya: "" / "anime" untuk anime Jepang, "donghua" untuk animasi Tiongkok.
    // Diisi dari indeks katalog (bidang "jenis"); judul lama tanpa bidang itu = anime.
    val jenis: String = "",
    // tanggal judul ini (atau episode terbarunya) masuk katalog, format "yyyy-MM-dd",
    // dari bidang "segar" di indeks. Dipakai baris "Baru Ditambahkan": urutan segar
    // dan lencana BARU yang kedaluwarsa sesudah 14 hari (gaya aplikasi populer).
    val segar: String = "",
    // jumlah episode menurut indeks katalog (bidang "jumlahEpisode"). Entri indeks
    // tidak membawa daftar episode, jadi tanpa bidang ini seri terbaca 0 episode
    // sebelum halaman detailnya dibuka.
    val jumlahTercatat: Int = 0,
    // titik fokus mendatar banner: -1 = kiri penuh, 0 = tengah, 1 = kanan penuh
    // (bidang "fokus" di katalog). Banner yang tokohnya di pinggir wajib digeser
    // supaya potongannya rapi — tidak kosong dan tokohnya tidak kepotong.
    val fokus: Float = 0f,
    // penanda film EKSPLISIT dari katalog (bidang "film": true pada entri indeks /
    // berkas judul). Hanya film asli yang ditandai pabrik; bidang absen = BUKAN
    // film. Dulu label Film diturunkan dari jumlahEpisode <= 1 sehingga seri satu
    // episode ikut berlabel Film — penurunan itu dibuang 2026-11-25.
    val film: Boolean = false,
    // tipe tayang dari katalog (bidang "tipe" atau "format" pada entri indeks / berkas
    // judul): TV, ONA, OVA, Film, Special. Kosong = belum dicatat katalog; tampilan
    // memakai tipeJudul() yang punya cadangan.
    val tipe: String = "",
) {
    val jumlahEpisode: Int get() = if (episodes.isNotEmpty()) episodes.size else jumlahTercatat
}

// Judul katalog ada yang huruf besar semua — rapikan tampilannya kata per kata
fun rapikanJudul(s: String): String {
    val tetap = setOf("TV", "OVA", "ONA", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X")
    return s.split(" ").joinToString(" ") { kata ->
        val huruf = kata.filter { it.isLetter() }
        if (huruf.length >= 2 && huruf.all { it.isUpperCase() } && kata.uppercase() !in tetap) {
            kata.lowercase().replaceFirstChar { it.uppercase() }
        } else {
            kata
        }
    }
}

// Masih pantas menyandang lencana BARU: tanggal segar judul berjarak
// paling lama 14 hari dari hari ini (gaya aplikasi populer: lencana
// kesegaran kedaluwarsa, tidak menempel selamanya).
fun masihSegar(j: Judul): Boolean {
    if (j.segar.isBlank()) return false
    val fmt = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
    val tSegar =
        try {
            fmt.parse(j.segar)?.time
        } catch (e: Exception) {
            null
        } ?: return false
    val tKini = fmt.parse(fmt.format(java.util.Date()))?.time ?: return false
    val selisihHari = (tKini - tSegar) / 86_400_000L
    return selisihHari in 0..14
}

object KonfigPabrik {
    // Buku alamat hanya dari Cloudflare Pages — cermin lama dibersihkan
    // total dari aplikasi atas perintah pengguna 2026-11-23 23:14 WIB.
    val cerminPintu =
        listOf(
            "https://animeid-katalog.pages.dev/Etalase.json",
        )
    var siap: Boolean by mutableStateOf(false)
    var akar: JSONObject? = null

    // berkas cache Etalase.json di penyimpanan internal; diisi dari Aplikasi()
    var berkas: java.io.File? = null

    // Tahap 1 (cepat, hanya disk): pakai salinan terakhir Etalase.json bila ada,
    // jadi alamat indeks sudah diketahui tanpa menunggu jaringan.
    fun muatCache() {
        if (akar != null) return
        try {
            val teks = berkas?.takeIf { it.exists() }?.readText() ?: return
            akar = JSONObject(teks)
            siap = true
        } catch (e: Exception) {
            hapusSalinan(berkas)
        }
    }

    // Tahap 2 (jaringan, di latar): periksa pembaruan. Memakai ETag, jadi bila
    // tidak berubah server hanya menjawab 304 tanpa isi.
    suspend fun segarkan() {
        withContext(Dispatchers.IO) {
            val et = bacaEtag(berkas)
            val h =
                balapTeks(cerminPintu, et?.first, et?.second) {
                    it.contains("\"sumber\"") || it.contains("\"indeks\"")
                } ?: return@withContext
            if (h.teks == TIDAK_BERUBAH) return@withContext
            try {
                akar = JSONObject(h.teks)
                siap = true
                try {
                    berkas?.writeText(h.teks)
                } catch (e: Exception) {
                }
                tulisEtag(berkas, h.url, h.etag)
            } catch (e: Exception) {
            }
        }
    }

    suspend fun muat() {
        withContext(Dispatchers.IO) { muatCache() }
        segarkan()
    }

    fun sumber(id: String): JSONObject? {
        val arr = akar?.optJSONArray("sumber") ?: return null
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            if (o.optString("id") == id) return o
        }
        return null
    }

    fun baseUrl(
        id: String,
        bawaan: String,
    ): String = sumber(id)?.optString("baseUrl")?.ifBlank { null } ?: bawaan

    fun aturan(id: String): JSONObject? = akar?.optJSONObject("aturan")?.optJSONObject(id)

    // Etalase v4: daftar cermin berkas indeks katalog hasil pabrik.
    fun daftarIndeks(): List<String> {
        val arr = akar?.optJSONArray("indeks") ?: return emptyList()
        return (0 until arr.length()).mapNotNull { arr.optString(it).ifBlank { null } }
    }

    // Etalase v4: daftar dasar alamat berkas judul; berkas satu judul =
    // dasar + <id> + ".json", dicoba berurutan sampai berhasil.
    fun daftarDasarJudul(): List<String> {
        val arr = akar?.optJSONArray("dasarJudul") ?: return emptyList()
        return (0 until arr.length()).mapNotNull { arr.optString(it).ifBlank { null } }
    }
}

// Kunci dedupe judul: bagian id sebelum "/" (sumber + nomor id anime,
// TANPA slug). Satu anime bisa muncul dengan beberapa slug berbeda
// (slug lama/baru, tautan sidebar) — nomornya tetap sama, jadi
// dedupe wajib memakai nomor, bukan id utuh, supaya tidak dobel.
fun kunciJudul(id: String): String = id.substringBefore('/')

// Lencana pembeda varian: satu seri di katalog sumber bisa punya beberapa
// entri berposter sama persis (Season 2, Dub ID, Dub JP — terverifikasi di
// situs untuk Arcane: 6 entri sah berbeda nomor). Tanpa lencana, kartu-kartu
// itu terlihat dobel karena judulnya terpotong sebelum bagian pembedanya.
fun lencanaVarian(judul: String): String? {
    val dub =
        Regex("""\(Dub\s*(ID|JP|EN)\)""", RegexOption.IGNORE_CASE)
            .find(judul)
            ?.groupValues
            ?.get(1)
            ?.uppercase()
    val musim =
        Regex("""Season\s*(\d+)""", RegexOption.IGNORE_CASE).find(judul)?.groupValues?.get(1)
            ?: Regex("""(\d+)(?:st|nd|rd|th)\s*Season""", RegexOption.IGNORE_CASE).find(judul)?.groupValues?.get(1)
    val bagian =
        listOfNotNull(
            musim?.let { "S$it" },
            dub?.let { "DUB $it" },
        )
    return if (bagian.isEmpty()) null else bagian.joinToString(" • ")
}

// Samakan penulisan tipe tayang dari katalog: TV, ONA, OVA, Film, Special.
fun normalTipe(s: String): String =
    when (
        s
            .trim()
            .uppercase()
            .replace(' ', '_')
            .replace('-', '_')
    ) {
        "TV", "TV_SHORT", "SERIES", "SERI" -> "TV"
        "ONA", "WEB" -> "ONA"
        "OVA", "OAD" -> "OVA"
        "MOVIE", "FILM" -> "Film"
        "SPECIAL", "TV_SPECIAL" -> "Special"
        else -> ""
    }

// Tipe yang ditampilkan di kartu. Urutan: bidang katalog, penanda film, kata penanda di
// judul (OVA / ONA / Special / Movie), terakhir TV sebagai bawaan seri. Judul ONA tanpa
// bidang "tipe" di katalog akan terbaca TV sampai katalog mencatatnya.
fun tipeJudul(j: Judul): String {
    normalTipe(j.tipe).let { if (it.isNotEmpty()) return it }
    if (j.film) return "Film"
    Regex("""\b(OVA|ONA|Special|Movie)\b""", RegexOption.IGNORE_CASE)
        .find(j.judul)
        ?.let { normalTipe(it.groupValues[1]) }
        ?.let { if (it.isNotEmpty()) return it }
    return "TV"
}

fun labelStatus(status: String): String =
    when (status) {
        "RELEASING", "Sedang Tayang" -> "Berlangsung"
        "FINISHED", "Selesai Tayang", "Tamat" -> "Tamat"
        "NOT_YET_RELEASED", "Belum Tayang" -> "Segera Tayang"
        "" -> "—"
        else -> status
    }

// Keterangan kecil di bawah judul kartu poster (gaya Bstation/Crunchyroll):
// jumlah episode + keadaan tayang. Judul yang belum tayang menulis "Segera tayang".
fun keteranganEpisode(j: Judul): String? {
    val st = labelStatus(j.status)
    val ekor =
        when (st) {
            "Tamat" -> " • Tamat"
            "Berlangsung" -> " • Masih tayang"
            else -> ""
        }
    return when {
        j.jumlahEpisode <= 0 -> if (st == "Segera Tayang") "Segera tayang" else null
        j.film -> "Film$ekor"
        else -> "${j.jumlahEpisode} episode$ekor"
    }
}

object KatalogSumber {
    var daftar: List<Judul> by mutableStateOf(emptyList())
    var terbaru: List<Judul> by mutableStateOf(emptyList())

    // Id judul yang sedang tren, berurutan dari peringkat 1 (dari bidang
    // "sedangTren" indeks katalog). Penentu isi + peringkat baris
    // "Sedang Trending".
    var idTren: List<String> by mutableStateOf(emptyList())
    var siap: Boolean by mutableStateOf(false)
    var gagal: Boolean by mutableStateOf(false)
    var mengisi: Boolean by mutableStateOf(false)
    private var jalan = false

    suspend fun muat() {
        if (jalan) return
        jalan = true
        try {
            // Tahap 1 — CACHE DULU: bila daftar masih kosong (baru buka aplikasi),
            // isi dari salinan disk saat itu juga. Beranda langsung penuh tanpa
            // menunggu jaringan.
            if (daftar.isEmpty()) {
                val salinan =
                    withContext(Dispatchers.IO) {
                        KonfigPabrik.muatCache()
                        try {
                            berkasIndeks()?.takeIf { it.exists() }?.readText()
                        } catch (e: Exception) {
                            null
                        }
                    }
                if (salinan != null) {
                    val hasil = withContext(Dispatchers.Default) { uraiIndeks(salinan) }
                    if (hasil != null) {
                        terapkan(hasil)
                        teksTerakhir = salinan
                    } else {
                        hapusSalinan(berkasIndeks())
                    }
                }
            }
            // Tahap 2 — JARINGAN di latar: Etalase.json dan indeks diperiksa
            // BERSAMAAN (alamat indeks dari salinan lama), semua cermin indeks
            // dibalap paralel, dan ETag membuat "tidak berubah" cukup 304.
            // Daftar hanya ditukar bila indeks baru berhasil dibaca; bila gagal,
            // daftar lama dibiarkan (tidak ada judul karangan).
            val teksIndeks =
                coroutineScope {
                    val alamatLama = KonfigPabrik.daftarIndeks()
                    val etalase = async { KonfigPabrik.segarkan() }
                    var t = if (alamatLama.isNotEmpty()) ambilIndeks() else null
                    etalase.await()
                    // pemasangan baru (belum punya alamat) atau alamat lama gagal DAN
                    // Etalase membawa alamat baru: coba lagi dengan alamat yang baru.
                    if (t == null && KonfigPabrik.daftarIndeks() != alamatLama) t = ambilIndeks()
                    t
                }
            if (teksIndeks != null && teksIndeks != TIDAK_BERUBAH && teksIndeks == teksTerakhir) {
                // isi sama tapi server menjawab 200 (ETag berubah / belum tersimpan):
                // simpan ETag-nya supaya pemeriksaan berikutnya cukup 304 tanpa unduh ulang
                withContext(Dispatchers.IO) { tulisEtag(berkasIndeks(), etagBaru.first, etagBaru.second) }
            }
            if (teksIndeks != null && teksIndeks != TIDAK_BERUBAH && teksIndeks != teksTerakhir) {
                val hasil = withContext(Dispatchers.Default) { uraiIndeks(teksIndeks) }
                if (hasil != null) {
                    terapkan(hasil)
                    teksTerakhir = teksIndeks
                    withContext(Dispatchers.IO) {
                        try {
                            berkasIndeks()?.writeText(teksIndeks)
                        } catch (e: Exception) {
                        }
                        tulisEtag(berkasIndeks(), etagBaru.first, etagBaru.second)
                    }
                }
            }
            gagal = daftar.isEmpty() && terbaru.isEmpty()
        } catch (e: kotlinx.coroutines.CancellationException) {
            // dibatalkan (mis. LaunchedEffect keluar dari komposisi) bukan berarti jaringan gagal
            throw e
        } catch (e: Exception) {
            gagal = daftar.isEmpty() && terbaru.isEmpty()
        } finally {
            jalan = false
            mengisi = false
        }
    }

    // Berkas cache indeks, di samping berkas cache Etalase.json.
    private fun berkasIndeks(): java.io.File? = KonfigPabrik.berkas?.let { java.io.File(it.parentFile, "indeks_cache.json") }

    private fun terapkan(h: Triple<List<Judul>, List<Judul>, List<String>>) {
        daftar = h.first
        terbaru = h.second
        idTren = h.third
        siap = true
        gagal = false
    }

    private var teksTerakhir: String? = null

    // ETag (alamat, nilai) dari unduhan indeks terakhir yang berhasil
    private var etagBaru: Pair<String?, String?> = null to null

    // Semua cermin indeks diminta bersamaan; yang pertama berhasil menang.
    // null = semua gagal (tidak ada isi baru); TIDAK_BERUBAH = salinan masih mutakhir.
    private suspend fun ambilIndeks(): String? =
        withContext(Dispatchers.IO) {
            val berkasI = berkasIndeks()
            val et = bacaEtag(berkasI)
            val h =
                balapTeks(KonfigPabrik.daftarIndeks(), et?.first, et?.second) {
                    it.contains("\"judul\"")
                } ?: return@withContext null
            etagBaru = h.url to h.etag
            h.teks
        }

    private fun uraiGenres(o: JsonObject): List<String> = o.jLarik("genres")?.daftarTeks() ?: emptyList()

    private fun uraiIndeks(teks: String): Triple<List<Judul>, List<Judul>, List<String>>? {
        return try {
            val akarI = teks.keJsonObjek()
            val arr = akarI.jLarik("judul") ?: return null
            val semua = ArrayList<Judul>()
            for (el in arr) {
                val o = el as? JsonObject ?: continue
                val id = o.jTeks("id")
                if (id.isBlank()) continue
                semua.add(
                    Judul(
                        id = id,
                        judul = rapikanJudul(o.jTeks("judul", id)),
                        poster = o.jTeks("poster"),
                        banner = o.jTeks("banner"),
                        sinopsis = o.jTeks("sinopsis"),
                        tahun = o.jInt("tahun"),
                        skor = o.jDesimal("skor", 0.0),
                        status = o.jTeks("status"),
                        genres = uraiGenres(o),
                        repo = null,
                        pin = null,
                        episodes = emptyList(),
                        jenis = o.jTeks("jenis"),
                        segar = o.jTeks("segar"),
                        jumlahTercatat = o.jInt("jumlahEpisode"),
                        fokus = o.jDesimal("fokus", 0.0).toFloat(),
                        film = o.jBenar("film", false),
                        tipe = o.jTeks("tipe").ifBlank { o.jTeks("format") },
                    ),
                )
            }
            // terbaru: yang sedang tren didahulukan (urut sesuai indeks),
            // lalu sisanya dari yang paling akhir masuk indeks.
            val idTren = (akarI.jLarik("sedangTren")?.daftarTeks() ?: emptyList()).distinct()
            val depan = idTren.mapNotNull { idt -> semua.firstOrNull { it.id == idt } }
            val terbaruList = (depan + semua.asReversed()).distinctBy { it.id }
            // id ganda di indeks membuat key LazyColumn/LazyRow kembar -> aplikasi crash
            Triple(semua.distinctBy { it.id }, terbaruList, idTren)
        } catch (e: Exception) {
            null
        }
    }

    // Ambil berkas satu judul dari etalase (daftar episode + alamat putar),
    // lalu tukar entri ringkasnya di katalog dengan yang lengkap.
    suspend fun muatDetail(j: Judul): Judul? =
        withContext(Dispatchers.IO) {
            for (dasar in KonfigPabrik.daftarDasarJudul()) {
                val teks =
                    try {
                        ambilTeksUrl(dasar + j.id + ".json")
                    } catch (e: Exception) {
                        null
                    } ?: continue
                val lengkap = uraiJudul(teks, j) ?: continue
                ganti(lengkap)
                return@withContext lengkap
            }
            null
        }

    private fun uraiJudul(
        teks: String,
        dasarJ: Judul,
    ): Judul? =
        try {
            val o = teks.keJsonObjek()
            val eps = ArrayList<Episode>()
            // Nomor episode harus unik di seluruh judul: musim berikutnya yang nomornya mulai
            // dari 1 lagi disambung (S2 E1 -> E13 bila S1 punya 12 episode). Tanpa ini kunci
            // unduhan dan penanda "sudah terunduh" bentrok antar musim.
            var maksSebelumnya = 0
            for (musimEl in o.jLarik("musim") ?: emptyList<kotlinx.serialization.json.JsonElement>()) {
                val mo = musimEl as? JsonObject ?: continue
                val earr = mo.jLarik("episode") ?: continue
                var geser = 0
                var geserDitentukan = false
                var maksMusim = 0
                for (epEl in earr) {
                    val eo = epEl as? JsonObject ?: continue
                    val rusak = eo.jBenar("rusak", false)
                    val mentah = eo.jInt("nomor", 0)
                    if (!geserDitentukan && mentah > 0) {
                        geser = if (mentah <= maksSebelumnya) maksSebelumnya else 0
                        geserDitentukan = true
                    }
                    val nomorEp = if (mentah > 0) mentah + geser else eps.size + 1
                    if (nomorEp > maksMusim) maksMusim = nomorEp
                    eps.add(
                        Episode(
                            number = nomorEp,
                            playlist = if (rusak) null else eo.jTeks("putar").ifBlank { null },
                        ),
                    )
                }
                if (maksMusim > maksSebelumnya) maksSebelumnya = maksMusim
            }
            dasarJ.copy(
                judul = rapikanJudul(o.jTeks("judul", dasarJ.judul)),
                poster = o.jTeks("poster", dasarJ.poster),
                banner = o.jTeks("banner", dasarJ.banner),
                fokus = if (o.containsKey("fokus")) o.jDesimal("fokus").toFloat() else dasarJ.fokus,
                film = if (o.containsKey("film")) o.jBenar("film", false) else dasarJ.film,
                tipe = o.jTeks("tipe").ifBlank { o.jTeks("format") }.ifBlank { dasarJ.tipe },
                sinopsis = o.jTeks("sinopsis", dasarJ.sinopsis),
                tahun = if (o.containsKey("tahun")) o.jInt("tahun") else dasarJ.tahun,
                skor = if (o.containsKey("skor")) o.jDesimal("skor") else dasarJ.skor,
                status = o.jTeks("status", dasarJ.status),
                genres = uraiGenres(o).ifEmpty { dasarJ.genres },
                episodes = eps,
            )
        } catch (e: Exception) {
            null
        }

    fun ganti(j: Judul) {
        daftar = daftar.map { if (it.id == j.id) j else it }
        terbaru = terbaru.map { if (it.id == j.id) j else it }
    }

    private val lingkup = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun muatLatar() {
        // Ketukan "Coba Lagi": bila daftar masih kosong, matikan status gagal lebih dulu
        // supaya layar kembali ke kerangka pemuatan. Tanpa ini layar galat tetap tampil
        // selama jaringan dicoba ulang, jadi tombolnya terasa tidak bekerja.
        if (!jalan && daftar.isEmpty()) gagal = false
        lingkup.launch { muat() }
    }
}

// ================= Mesin Unduhan =================
data class Unduhan(
    val kunci: String,
    val idJudul: String,
    val judul: String,
    val poster: String,
    val nomorEp: Int,
    val indeksEp: Int,
    val berkas: String,
    val ukuran: Long,
    val waktu: Long,
)

fun fmtUkuran(b: Long): String =
    if (b >= 1_000_000_000L) {
        "%.1f GB".format(b / 1_000_000_000.0).replace('.', ',')
    } else {
        "${b / 1_000_000L} MB"
    }

// Mengunduh episode HLS: ambil semua segmen lalu gabung jadi satu berkas (.ts atau .mp4 untuk fMP4).
// Berjalan di lingkup aplikasi: tetap jalan selama proses aplikasi hidup.
object Pengunduh {
    fun kunci(
        idJudul: String,
        nomor: Int,
    ) = "${idJudul}__$nomor"

    private fun folder(ctx: Context): java.io.File {
        val dasar = ctx.applicationContext.getExternalFilesDir(null) ?: ctx.applicationContext.filesDir
        return java.io.File(dasar, "unduhan").also { it.mkdirs() }
    }

    fun daftar(ctx: Context): List<Unduhan> =
        try {
            val arr = org.json.JSONArray(prefs(ctx).getString("unduhan_meta", "[]") ?: "[]")
            val hasil = ArrayList<Unduhan>()
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                val u =
                    Unduhan(
                        o.getString("kunci"),
                        o.getString("id"),
                        o.optString("judul"),
                        o.optString("poster"),
                        o.optInt("nomor"),
                        o.optInt("indeks"),
                        o.getString("berkas"),
                        o.optLong("ukuran"),
                        o.optLong("waktu"),
                    )
                if (java.io.File(u.berkas).exists()) hasil.add(u)
            }
            hasil.sortedByDescending { it.waktu }
        } catch (e: Exception) {
            emptyList()
        }

    private fun simpanMeta(
        ctx: Context,
        daftar: List<Unduhan>,
    ) {
        val arr = org.json.JSONArray()
        daftar.forEach { u ->
            arr.put(
                JSONObject()
                    .put("kunci", u.kunci)
                    .put("id", u.idJudul)
                    .put("judul", u.judul)
                    .put("poster", u.poster)
                    .put("nomor", u.nomorEp)
                    .put("indeks", u.indeksEp)
                    .put("berkas", u.berkas)
                    .put("ukuran", u.ukuran)
                    .put("waktu", u.waktu),
            )
        }
        prefs(ctx).edit().putString("unduhan_meta", arr.toString()).apply()
    }

    fun berkas(
        ctx: Context,
        idJudul: String,
        nomor: Int,
    ): java.io.File? {
        val k = kunci(idJudul, nomor)
        return daftar(ctx).firstOrNull { it.kunci == k }?.let { java.io.File(it.berkas) }
    }

    @Synchronized
    fun hapus(
        ctx: Context,
        u: Unduhan,
    ) {
        try {
            val b = java.io.File(u.berkas)
            b.delete()
            java.io.File(b.path.substringBeforeLast('.') + ".vtt").delete()
        } catch (e: Exception) {
        }
        simpanMeta(ctx, daftar(ctx).filter { it.kunci != u.kunci })
    }

    // ---- Pengunduhan HLS (jalan di lingkup aplikasi; tetap hidup selama proses aplikasi hidup) ----
    private val lingkupUn = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // kunci -> persen (0..100) untuk unduhan yang sedang berjalan
    val aktif = mutableStateMapOf<String, Int>()

    // naik tiap unduhan selesai/gagal, supaya layar yang membaca daftar ikut segar
    var versi: Int by mutableStateOf(0)

    private fun aman(s: String) = s.replace(Regex("[^A-Za-z0-9_-]"), "_")

    // kunci unduhan yang diminta dibatalkan pengguna
    private val dibatalkan: MutableSet<String> = java.util.Collections.synchronizedSet(HashSet<String>())

    fun batal(
        idJudul: String,
        nomor: Int,
        kabar: (String) -> Unit,
    ) {
        val k = kunci(idJudul, nomor)
        if (aktif.containsKey(k)) {
            dibatalkan.add(k)
            kabar("Membatalkan unduhan Episode $nomor…")
        }
    }

    // Berkas .part sisa (proses dimatikan sistem / aplikasi ditutup saat mengunduh) tidak
    // dipakai lagi dan memakan ruang. Dipanggil sekali saat aplikasi mulai.
    fun bersihkanSisa(ctx: Context) {
        try {
            if (aktif.isNotEmpty()) return
            folder(ctx.applicationContext).listFiles { f -> f.name.endsWith(".part") }?.forEach { it.delete() }
        } catch (e: Exception) {
        }
    }

    @Synchronized
    private fun tambahMeta(
        ctx: Context,
        u: Unduhan,
    ) {
        simpanMeta(ctx, daftar(ctx).filter { it.kunci != u.kunci } + u)
    }

    private fun ambilBytes(url: String): ByteArray {
        var salah: Exception? = null
        for (percobaan in 1..3) {
            var conn: HttpURLConnection? = null
            try {
                conn = URL(url).openConnection() as HttpURLConnection
                conn.connectTimeout = 12000
                conn.readTimeout = 20000
                conn.setRequestProperty("User-Agent", "AnimeId/1.1")
                if (conn.responseCode !in 200..299) throw java.io.IOException("HTTP ${conn.responseCode}")
                return conn.inputStream.use { it.readBytes() }
            } catch (e: Exception) {
                salah = e
                if (percobaan < 3) {
                    try {
                        Thread.sleep(800L * percobaan)
                    } catch (x: InterruptedException) {
                    }
                }
            } finally {
                conn?.disconnect()
            }
        }
        throw salah ?: java.io.IOException("Gagal mengunduh")
    }

    private fun alamatTerhadap(
        dasar: String,
        rel: String,
    ): String = URL(URL(dasar), rel).toString()

    // Menulis semua segmen berurutan ke `tujuan`; mengembalikan ekstensi akhir ("ts" atau "mp4" bila fMP4).
    private fun unduhHls(
        url: String,
        tujuan: java.io.File,
        batal: () -> Boolean,
        lapor: (Int) -> Unit,
    ): String {
        var alamat = url
        var teks = ambilTeksUrl(alamat) ?: throw java.io.IOException("Playlist tidak terbaca")
        if (teks.contains("#EXT-X-STREAM-INF")) {
            val baris = teks.lines()
            if (baris.any { it.startsWith("#EXT-X-MEDIA") && it.contains("TYPE=AUDIO") && it.contains("URI=") }) {
                throw java.io.IOException("Episode ini memakai audio terpisah dan belum bisa diunduh")
            }
            // rendisi terbaik yang bitrate-nya <= 3 Mbps (kira-kira 720p); bila tak ada, yang terendah
            var terbaik: String? = null
            var bwTerbaik = -1L
            var terendah: String? = null
            var bwTerendah = Long.MAX_VALUE
            for (i in baris.indices) {
                val b = baris[i].trim()
                if (!b.startsWith("#EXT-X-STREAM-INF")) continue
                val bw =
                    Regex("(?<![A-Z-])BANDWIDTH=(\\d+)")
                        .find(b)
                        ?.groupValues
                        ?.get(1)
                        ?.toLongOrNull() ?: continue
                var j = i + 1
                while (j < baris.size && (baris[j].isBlank() || baris[j].startsWith("#"))) j++
                if (j >= baris.size) continue
                val u = baris[j].trim()
                if (bw <= 3_000_000L && bw > bwTerbaik) {
                    bwTerbaik = bw
                    terbaik = u
                }
                if (bw < bwTerendah) {
                    bwTerendah = bw
                    terendah = u
                }
            }
            val pilih = terbaik ?: terendah ?: throw java.io.IOException("Rendisi video tidak ditemukan")
            alamat = alamatTerhadap(alamat, pilih)
            teks = ambilTeksUrl(alamat) ?: throw java.io.IOException("Playlist video tidak terbaca")
        }
        val baris = teks.lines()
        if (baris.any { it.startsWith("#EXT-X-KEY") && !it.contains("METHOD=NONE") }) {
            throw java.io.IOException("Episode ini terenkripsi dan belum bisa diunduh")
        }
        if (teks.contains("#EXT-X-BYTERANGE")) throw java.io.IOException("Format segmen ini belum bisa diunduh")
        val peta = Regex("#EXT-X-MAP:[^\\n]*URI=\"([^\"]+)\"").find(teks)?.groupValues?.get(1)
        val segmen = baris.map { it.trim() }.filter { it.isNotEmpty() && !it.startsWith("#") }
        if (segmen.isEmpty()) throw java.io.IOException("Playlist kosong")
        val semua = (if (peta != null) listOf(peta) else emptyList()) + segmen
        java.io.FileOutputStream(tujuan).buffered().use { out ->
            for ((i, rel) in semua.withIndex()) {
                if (batal()) throw java.io.IOException("Dibatalkan")
                out.write(ambilBytes(alamatTerhadap(alamat, rel)))
                lapor(((i + 1) * 100) / semua.size)
            }
        }
        return if (peta != null) "mp4" else "ts"
    }

    fun mulai(
        ctx: Context,
        j: Judul,
        idx: Int,
        kabar: (String) -> Unit,
    ) {
        val app = ctx.applicationContext
        val ep = j.episodes.getOrNull(idx) ?: return
        val k = kunci(j.id, ep.number)
        if (aktif.containsKey(k)) {
            kabar("Episode ${ep.number} sedang diunduh.")
            return
        }
        if (berkas(app, j.id, ep.number) != null) {
            kabar("Episode ${ep.number} sudah terunduh.")
            return
        }
        val sumber = ep.playlist
        if (sumber == null || !sumber.startsWith("http")) {
            kabar("Episode ini belum punya sumber untuk diunduh.")
            return
        }
        // jangan mulai bila ruang penyimpanan hampir habis (satu episode bisa ratusan MB)
        if (folder(app).usableSpace < 300L * 1024 * 1024) {
            kabar("Ruang penyimpanan hampir penuh. Kosongkan dulu sebelum mengunduh.")
            return
        }
        aktif[k] = 0
        dibatalkan.remove(k)
        kabar("Mengunduh Episode ${ep.number}… tetap buka aplikasi sampai selesai. Ketuk lagi untuk membatalkan.")
        lingkupUn.launch {
            val sementara = java.io.File(folder(app), aman(k) + ".part")
            var pesanAkhir: String
            try {
                val ext = unduhHls(sumber, sementara, { dibatalkan.contains(k) }) { p -> aktif[k] = p }
                val akhir = java.io.File(folder(app), aman(k) + "." + ext)
                if (akhir.exists()) akhir.delete()
                if (!sementara.renameTo(akhir)) throw java.io.IOException("Gagal menyimpan berkas")
                tambahMeta(
                    app,
                    Unduhan(k, j.id, j.judul, j.poster, ep.number, idx, akhir.absolutePath, akhir.length(), System.currentTimeMillis()),
                )
                dibatalkan.remove(k)
                pesanAkhir = "Episode ${ep.number} selesai diunduh."
            } catch (e: Exception) {
                try {
                    sementara.delete()
                } catch (x: Exception) {
                }
                val sebab = (e as? java.io.IOException)?.message
                pesanAkhir =
                    if (dibatalkan.remove(k)) {
                        "Unduhan Episode ${ep.number} dibatalkan."
                    } else {
                        "Unduhan Episode ${ep.number} gagal" + (if (sebab.isNullOrBlank()) "." else ": $sebab.")
                    }
            }
            aktif.remove(k)
            withContext(Dispatchers.Main) {
                versi += 1
                kabar(pesanAkhir)
            }
        }
    }
}

@Composable
private fun Kerangka(modifier: Modifier = Modifier) {
    val t =
        androidx.compose.animation.core
            .rememberInfiniteTransition(label = "kerangka")
    val geser by t.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec =
            androidx.compose.animation.core.infiniteRepeatable(
                androidx.compose.animation.core
                    .tween(1300, easing = androidx.compose.animation.core.LinearEasing),
            ),
        label = "geser",
    )
    Box(
        modifier.background(
            Brush.horizontalGradient(
                colors = listOf(Kartu, Kartu2, Kartu),
                startX = -400f + 1200f * geser,
                endX = 200f + 1200f * geser,
            ),
        ),
    )
}

@Composable
fun Gambar(
    url: String,
    modifier: Modifier = Modifier,
    fokusX: Float = 0f,
) {
    val ctx = LocalContext.current
    var ulangKe by remember(url) { mutableStateOf(0) }
    var berhasil by remember(url) { mutableStateOf(false) }
    var gagalGambar by remember(url) { mutableStateOf(false) }
    // Coil mengurus cache memori + disk, pembatalan saat keluar layar, dan
    // downsampling sesuai ukuran tampilan. ulangKe mengganti permintaan supaya
    // ketukan coba-lagi memuat ulang dari jaringan (hasil gagal tidak disimpan Coil).
    val req =
        remember(url, ulangKe) {
            ImageRequest
                .Builder(ctx)
                .data(url.ifBlank { null })
                .httpHeaders(NetworkHeaders.Builder().add("User-Agent", "Anime.id Android").build())
                .build()
        }
    Box(modifier.background(Kartu)) {
        val alfa by androidx.compose.animation.core.animateFloatAsState(
            if (berhasil) 1f else 0f,
            androidx.compose.animation.core
                .tween(380),
            label = "muncul",
        )
        if (alfa < 1f) {
            // placeholder warna: rona tetap per gambar (dari url), shimmer tipis di atasnya
            val hue = (url.hashCode() and 0x7fffffff) % 360
            Box(
                Modifier.fillMaxSize().background(
                    Brush.linearGradient(
                        listOf(Color.hsv(hue.toFloat(), 0.38f, 0.28f), Color.hsv(((hue + 35) % 360).toFloat(), 0.42f, 0.12f)),
                    ),
                ),
            )
            Kerangka(Modifier.fillMaxSize().graphicsLayer { alpha = 0.3f * (1f - alfa) })
            if (gagalGambar) {
                // gagal: ketuk untuk mencoba lagi
                Box(
                    Modifier.fillMaxSize().clickable {
                        gagalGambar = false
                        ulangKe++
                    },
                    contentAlignment = Alignment.Center,
                ) {
                    Text("↻", color = Color.White.copy(alpha = 0.55f), fontSize = 22.sp)
                }
            }
        }
        AsyncImage(
            model = req,
            contentDescription = null,
            modifier =
                Modifier.fillMaxSize().graphicsLayer {
                    alpha = alfa
                    val sk = 1.06f - 0.06f * alfa
                    scaleX = sk
                    scaleY = sk
                },
            contentScale = ContentScale.Crop,
            alignment = androidx.compose.ui.BiasAlignment(fokusX, 0f),
            onState = { st ->
                berhasil = st is AsyncImagePainter.State.Success
                gagalGambar = st is AsyncImagePainter.State.Error
            },
        )
    }
}

// ================= Penyimpanan lokal =================
private fun prefs(ctx: Context) = ctx.getSharedPreferences("animeid", Context.MODE_PRIVATE)

// Kunci login Google (proyek Cloud "anime-id-login"; klien Web dipakai sebagai serverClientId)
private const val ID_KLIEN_WEB = "922679004228-j054jmvtb0gke90d8uuh35jntc9gnfg7.apps.googleusercontent.com"

data class AkunGoogle(
    val nama: String,
    val email: String,
    val foto: String,
)

private fun bacaFavorit(ctx: Context): Set<String> = prefs(ctx).getStringSet("favorit", emptySet())?.toSet() ?: emptySet()

private fun simpanFavorit(
    ctx: Context,
    fav: Set<String>,
) {
    prefs(ctx).edit().putStringSet("favorit", fav).apply()
    SinkronAkun.jadwalkan(ctx)
}

// ===== Kabar rilis baru (gaya aplikasi populer) =====
// Potret seluruh katalog (per judul: tanggal segar + jumlah episode) disimpan
// di prefs. Sesudah katalog selesai dimuat ulang (saat aplikasi dibuka/kembali
// ke depan DAN oleh PemeriksaKabarWorker tiap ±15 menit di latar, juga saat
// aplikasi tertutup), perubahan sejak potret terakhir memicu pemberitahuan setempat:
//   - judul yang BARU masuk katalog -> pemberitahuan "Judul baru rilis";
//   - judul APA PUN yang jumlah episodenya bertambah -> "Episode baru".
// Tanpa syarat favorit (perintah pengguna 2026-11-23 18:16 WIB: yang
// diberitahukan adalah judul/episode yang benar-benar baru rilis).
// Jalan pertama hanya mencatat potret — tidak ada kabar palsu.
// ===== Kotak masuk notifikasi (ikon lonceng di Beranda) =====
// Setiap kabar yang dikirim juga disimpan di sini agar tetap bisa dibaca
// setelah notifikasi sistemnya dihapus. Titik hijau di lonceng muncul hanya
// selama masih ada yang belum dibaca.
data class Kabar(
    val judul: String,
    val isi: String,
    val idJudul: String?,
    val waktu: Long,
    val dibaca: Boolean,
)

private var versiKabar by mutableStateOf(0)

private fun bacaKabar(ctx: Context): List<Kabar> =
    try {
        val arr = org.json.JSONArray(prefs(ctx).getString("kotak_notif", "[]"))
        (0 until arr.length()).map { i ->
            val o = arr.getJSONObject(i)
            Kabar(o.optString("t"), o.optString("i"), o.optString("id").ifEmpty { null }, o.optLong("w"), o.optBoolean("b"))
        }
    } catch (e: Exception) {
        emptyList()
    }

private fun tulisKabar(
    ctx: Context,
    daftar: List<Kabar>,
) {
    val arr = org.json.JSONArray()
    for (k in daftar.take(30)) {
        arr.put(
            org.json
                .JSONObject()
                .put("t", k.judul)
                .put("i", k.isi)
                .put("id", k.idJudul ?: "")
                .put("w", k.waktu)
                .put("b", k.dibaca),
        )
    }
    prefs(ctx).edit().putString("kotak_notif", arr.toString()).apply()
    versiKabar++
}

private fun simpanKabar(
    ctx: Context,
    judul: String,
    isi: String,
    idJudul: String?,
) {
    tulisKabar(ctx, listOf(Kabar(judul, isi, idJudul, System.currentTimeMillis(), false)) + bacaKabar(ctx))
}

private fun tandaiKabarDibaca(ctx: Context) {
    val d = bacaKabar(ctx)
    if (d.any { !it.dibaca }) tulisKabar(ctx, d.map { it.copy(dibaca = true) })
}

private fun waktuRelatif(w: Long): String {
    val m = (System.currentTimeMillis() - w) / 60000
    return when {
        m < 1 -> "Baru saja"
        m < 60 -> "$m menit lalu"
        m < 1440 -> "${m / 60} jam lalu"
        else -> "${m / 1440} hari lalu"
    }
}

private const val CHANNEL_KABAR = "kabar_rilis_v2"

// jenis: 7001 episode baru, 7002 judul baru, 7003 sudah tayang. ID notifikasi dibuat
// unik per kiriman (jenis + detik) supaya kabar yang belum dibuka tidak saling timpa.
private fun kirimKabar(
    ctx: Context,
    jenis: Int,
    judulNotif: String,
    isi: String,
    idJudul: String? = null,
) {
    val app = ctx.applicationContext
    simpanKabar(app, judulNotif, isi, idJudul) // kotak masuk selalu terisi, walau izin notifikasi ditolak
    try {
        val nm = app.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        // channel lama berprioritas biasa dan prioritasnya tidak bisa dinaikkan setelah dibuat -> ganti baru
        nm.deleteNotificationChannel("kabar_episode")
        nm.createNotificationChannel(
            android.app
                .NotificationChannel(
                    CHANNEL_KABAR,
                    "Rilis Baru",
                    android.app.NotificationManager.IMPORTANCE_HIGH,
                ).apply { description = "Kabar judul dan episode baru di Anime.id" },
        )
        val idUnik = jenis * 100000 + ((System.currentTimeMillis() / 1000) % 100000).toInt()
        val niat =
            android.content
                .Intent(app, MainActivity::class.java)
                .addFlags(
                    android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP,
                )
        if (idJudul != null) niat.putExtra(KeadaanBuka.EKSTRA, idJudul)
        val pi =
            android.app.PendingIntent.getActivity(
                app,
                idUnik,
                niat,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE,
            )
        val notif =
            android.app.Notification
                .Builder(app, CHANNEL_KABAR)
                .setSmallIcon(R.drawable.ic_stat_kabar)
                .setColor(Zamrud.toArgb()) // Zamrud aplikasi
                .setContentTitle(judulNotif)
                .setContentText(isi)
                .setStyle(
                    android.app.Notification
                        .BigTextStyle()
                        .bigText(isi),
                ).setContentIntent(pi)
                .setGroup("kabar_rilis")
                .setAutoCancel(true)
                .build()
        nm.notify(idUnik, notif)
    } catch (e: Exception) {
        // gagal menampilkan notifikasi tidak boleh menggagalkan pemeriksaan
    }
}

// Pemeriksaan bisa datang bersamaan dari layar (LaunchedEffect) dan dari Worker:
// dikunci agar potret tidak dibaca ganda dan kabar tidak terkirim dua kali.
private val kunciKabar = Any()

private fun periksaKabarEpisode(ctx: Context) {
    synchronized(kunciKabar) { periksaKabarEpisodeLangsung(ctx) }
}

private fun periksaKabarEpisodeLangsung(ctx: Context) {
    try {
        val p = prefs(ctx)
        val teksLama = p.getString("kabar_potret3", null)
        // potret lama: id -> (tanggal segar, jumlah episode)
        val lama = HashMap<String, Pair<String, Int>>()
        if (teksLama != null) {
            val o = org.json.JSONObject(teksLama)
            val kunci = o.keys()
            while (kunci.hasNext()) {
                val k = kunci.next()
                val bagian = o.optString(k).split("|")
                lama[k] = (bagian.getOrNull(0) ?: "") to (bagian.getOrNull(1)?.toIntOrNull() ?: 0)
            }
        }
        val judulBaru = ArrayList<Judul>()
        val epBaru = ArrayList<Judul>()
        // Potret baru = potret lama DIGABUNG katalog sekarang (jumlah episode diambil yang
        // terbesar, id yang hilang sementara tetap diingat). Katalog yang sesaat kurang
        // lengkap (mis. cermin lain) jadi tidak memicu "judul/episode baru" palsu sesudahnya.
        val oBaru = org.json.JSONObject()
        for ((id, v) in lama) oBaru.put(id, "${v.first}|${v.second}")
        for (j in KatalogSumber.daftar) {
            val dulu = lama[j.id]
            oBaru.put(j.id, "${j.segar}|${maxOf(j.jumlahTercatat, dulu?.second ?: 0)}")
            if (teksLama == null) continue
            if (dulu == null) {
                judulBaru.add(j)
            } else if (j.jumlahTercatat > dulu.second) {
                epBaru.add(j)
            }
        }
        if (judulBaru.isNotEmpty()) {
            val pertama = judulBaru.first()
            val isi =
                if (judulBaru.size == 1) {
                    "${rapikanJudul(pertama.judul)} — baru rilis dan sudah bisa ditonton."
                } else {
                    "${rapikanJudul(pertama.judul)} dan ${judulBaru.size - 1} judul baru lain baru saja rilis."
                }
            kirimKabar(ctx, 7002, "Judul baru rilis", isi, pertama.id)
        }
        if (epBaru.isNotEmpty()) {
            val pertama = epBaru.first()
            val isi =
                if (epBaru.size == 1) {
                    "${rapikanJudul(pertama.judul)} — episode baru sudah bisa ditonton."
                } else {
                    "${rapikanJudul(pertama.judul)} dan ${epBaru.size - 1} judul lain dapat episode baru."
                }
            kirimKabar(ctx, 7001, "Episode baru rilis", isi, pertama.id)
        }
        // Pengingat judul belum tayang: judul yang dipasangi lonceng dan kini
        // sudah punya episode -> kabari penunggunya, lalu lepas pengingatnya.
        val pengingat = p.getStringSet("pengingat", emptySet()) ?: emptySet()
        if (pengingat.isNotEmpty() && teksLama != null) {
            val tayang = KatalogSumber.daftar.filter { it.id in pengingat && it.jumlahEpisode > 0 }
            if (tayang.isNotEmpty()) {
                val pertama = tayang.first()
                val isi =
                    if (tayang.size == 1) {
                        "${rapikanJudul(pertama.judul)} yang kamu tunggu sudah tayang — episode pertamanya sudah bisa ditonton."
                    } else {
                        "${rapikanJudul(pertama.judul)} dan ${tayang.size - 1} judul lain yang kamu tunggu sudah tayang."
                    }
                kirimKabar(ctx, 7003, "Sudah tayang!", isi, pertama.id)
                p.edit().putStringSet("pengingat", pengingat - tayang.map { it.id }.toSet()).apply()
            }
        }
        // Potret disimpan PALING AKHIR, sesudah semua kabar terkirim/tersimpan di kotak masuk.
        p.edit().putString("kabar_potret3", oBaru.toString()).apply()
    } catch (e: Exception) {
        // kabar yang gagal tidak boleh mengganggu aplikasi
    }
}

// Pemeriksa latar (WorkManager): jalan tiap ±15 menit walau aplikasi tertutup, selama ada
// jaringan. Memuat katalog (ETag -> biasanya cukup 304), lalu memakai logika perbandingan yang sama.
class PemeriksaKabarWorker(
    ctx: Context,
    params: androidx.work.WorkerParameters,
) : androidx.work.CoroutineWorker(ctx, params) {
    override suspend fun doWork(): androidx.work.ListenableWorker.Result {
        val app = applicationContext
        return try {
            KonfigPabrik.berkas = java.io.File(app.filesDir, "pabrik_cache.json")
            KatalogSumber.muat()
            if (KatalogSumber.daftar.isEmpty()) {
                androidx.work.ListenableWorker.Result
                    .retry()
            } else {
                withContext(Dispatchers.IO) { periksaKabarEpisode(app) }
                androidx.work.ListenableWorker.Result
                    .success()
            }
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            androidx.work.ListenableWorker.Result
                .retry()
        }
    }
}

private fun jadwalkanPemeriksaanKabar(ctx: Context) {
    try {
        val syarat =
            androidx.work.Constraints
                .Builder()
                .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
                .build()
        val minta =
            androidx.work
                .PeriodicWorkRequestBuilder<PemeriksaKabarWorker>(
                    15,
                    java.util.concurrent.TimeUnit.MINUTES,
                ).setConstraints(syarat)
                .build()
        androidx.work.WorkManager.getInstance(ctx.applicationContext).enqueueUniquePeriodicWork(
            "kabar_berkala",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            minta,
        )
    } catch (e: Exception) {
        // penjadwalan gagal: kabar tetap diperiksa saat aplikasi dibuka
    }
}

data class Progres(
    val idJudul: String,
    val indeksEpisode: Int,
    val posisi: Int,
    val durasi: Int,
    val waktu: Long = 0L,
)

private fun bacaProgres(ctx: Context): List<Progres> {
    val set = prefs(ctx).getStringSet("lanjut", emptySet()) ?: emptySet()
    return set
        .mapNotNull { s ->
            val p = s.split("|")
            if (p.size >= 4) {
                val pos = p[2].toIntOrNull() ?: 0
                val dur = p[3].toIntOrNull() ?: 0
                if (dur > 0 && pos > 5000 && pos < dur - 3000) {
                    Progres(p[0], p[1].toIntOrNull() ?: 0, pos, dur, p.getOrNull(4)?.toLongOrNull() ?: 0L)
                } else {
                    null
                }
            } else {
                null
            }
        }.sortedByDescending { it.waktu }
}

// Posisi terakhir PER EPISODE (daftar "Lanjut Menonton" hanya mengingat satu episode per judul)
private fun simpanPosisiEp(
    ctx: Context,
    id: String,
    idx: Int,
    pos: Int,
    dur: Int,
) {
    val kunci = "pos_${id}_$idx"
    val e = prefs(ctx).edit()
    if (dur > 0 && pos > 5000 && pos < dur - 20000) {
        e.putInt(kunci, pos)
    } else if (dur > 0) {
        e.remove(kunci)
    }
    e.apply()
    SinkronAkun.jadwalkan(ctx)
}

private fun bacaPosisiEp(
    ctx: Context,
    id: String,
    idx: Int,
): Int = prefs(ctx).getInt("pos_${id}_$idx", 0)

private fun simpanProgres(
    ctx: Context,
    pr: Progres,
) {
    val lama = bacaProgres(ctx).filter { it.idJudul != pr.idJudul }
    val baru = (listOf(pr.copy(waktu = System.currentTimeMillis())) + lama).take(12)
    val set = baru.map { "${it.idJudul}|${it.indeksEpisode}|${it.posisi}|${it.durasi}|${it.waktu}" }.toSet()
    prefs(ctx).edit().putStringSet("lanjut", set).apply()
    catatRiwayat(ctx, pr.idJudul)
}

// ===== Riwayat tontonan (semua judul yang pernah diputar, gaya aplikasi populer) =====
private fun bacaRiwayat(ctx: Context): List<Pair<String, Long>> {
    val set = prefs(ctx).getStringSet("riwayat", emptySet()) ?: emptySet()
    return set
        .mapNotNull { s ->
            val p = s.split("|")
            if (p.size >= 2) p[0] to (p[1].toLongOrNull() ?: 0L) else null
        }.sortedByDescending { it.second }
}

private fun tulisRiwayat(
    ctx: Context,
    daftar: List<Pair<String, Long>>,
) {
    prefs(ctx)
        .edit()
        .putStringSet("riwayat", daftar.map { "${it.first}|${it.second}" }.toSet())
        .apply()
    SinkronAkun.jadwalkan(ctx)
}

private fun catatRiwayat(
    ctx: Context,
    id: String,
) {
    val lama = bacaRiwayat(ctx).filter { it.first != id }
    tulisRiwayat(ctx, (listOf(id to System.currentTimeMillis()) + lama).take(100))
}

private fun hapusRiwayat(
    ctx: Context,
    id: String,
) {
    tulisRiwayat(ctx, bacaRiwayat(ctx).filter { it.first != id })
    // menghapus dari riwayat juga membersihkan sisa progres judul itu
    val p = prefs(ctx)
    val lanjutBaru = bacaProgres(ctx).filter { it.idJudul != id }
    val e = p.edit()
    e.putStringSet(
        "lanjut",
        lanjutBaru.map { "${it.idJudul}|${it.indeksEpisode}|${it.posisi}|${it.durasi}|${it.waktu}" }.toSet(),
    )
    p.all.keys
        .filter { it.startsWith("pos_${id}_") }
        .forEach { e.remove(it) }
    e.apply()
}

private fun hapusSemuaRiwayat(ctx: Context) {
    val p = prefs(ctx)
    val e = p.edit()
    e.remove("riwayat")
    e.remove("lanjut")
    p.all.keys
        .filter { it.startsWith("pos_") }
        .forEach { e.remove(it) }
    e.apply()
    SinkronAkun.jadwalkan(ctx)
}

// ===== Pengingat judul belum tayang (lonceng "Ingatkan Saya", gaya Netflix) =====
private fun bacaPengingat(ctx: Context): Set<String> = prefs(ctx).getStringSet("pengingat", emptySet())?.toSet() ?: emptySet()

private fun simpanPengingat(
    ctx: Context,
    s: Set<String>,
) {
    prefs(ctx).edit().putStringSet("pengingat", s).apply()
    SinkronAkun.jadwalkan(ctx)
}

// ================= Sinkron akun (Firestore) =================
// Favorit, progres, posisi per episode, riwayat, dan pengingat disalin ke
// Firestore (proyek anime-id-login) supaya ganti HP tidak menghilangkan
// data penonton. Satu dokumen per pengguna: pengguna/{uid}, bidang
// "paket" berisi JSON kecil berbentuk sama dengan penyimpanan lokal.
// Gabung saat masuk: yang lebih baru menang per judul; favorit dan
// pengingat digabung utuh; posisi jauh hanya mengisi kunci yang belum
// ada setempat. Semua kegagalan jaringan diabaikan diam-diam — data
// lokal tetap raja dan aplikasi tidak pernah menunggu sinkron.
// Firebase disiapkan manual dari kode (tanpa berkas google-services),
// jadi satu-satunya syarat bangun adalah dependensi firebase-auth +
// firebase-firestore di modul aplikasi.
private object SinkronAkun {
    private const val APP_ID = "1:922679004228:android:1cec1f3495fa83ac17c011"
    private const val API_KEY = "AIzaSyBcGA4wx8ebOvHg05SRSI4tj0iw0EnoljM"
    private const val PROYEK = "anime-id-login"
    private val latar =
        java.util.concurrent.Executors
            .newSingleThreadExecutor()
    private val penunda = android.os.Handler(android.os.Looper.getMainLooper())
    private var tugasKirim: Runnable? = null

    @Volatile private var sibukMuat = false

    fun pastikanFirebase(ctx: Context) {
        try {
            val app = ctx.applicationContext
            if (com.google.firebase.FirebaseApp
                    .getApps(app)
                    .isEmpty()
            ) {
                val opsi =
                    com.google.firebase.FirebaseOptions
                        .Builder()
                        .setApplicationId(APP_ID)
                        .setApiKey(API_KEY)
                        .setProjectId(PROYEK)
                        .build()
                com.google.firebase.FirebaseApp
                    .initializeApp(app, opsi)
            }
        } catch (e: Exception) {
            android.util.Log.w("SinkronAkun", "Firebase gagal diinisialisasi", e)
        }
    }

    // uid pengguna ini di Firebase (dipakai juga KomentarBersama).
    fun uidSaya(): String? =
        try {
            com.google.firebase.auth.FirebaseAuth
                .getInstance()
                .currentUser
                ?.uid
        } catch (e: Exception) {
            null
        }

    // Dipanggil sesudah masuk Google berhasil: token id Google ditukar
    // menjadi sesi Firebase, lalu paket jauh digabung ke lokal.
    fun masukFirebase(
        ctx: Context,
        idToken: String,
    ) {
        pastikanFirebase(ctx)
        try {
            val kred =
                com.google.firebase.auth.GoogleAuthProvider
                    .getCredential(idToken, null)
            com.google.firebase.auth.FirebaseAuth
                .getInstance()
                .signInWithCredential(kred)
                .addOnSuccessListener { muatGabung(ctx) {} }
        } catch (e: Exception) {
        }
    }

    fun keluarFirebase() {
        try {
            com.google.firebase.auth.FirebaseAuth
                .getInstance()
                .signOut()
        } catch (e: Exception) {
        }
    }

    private fun paketLokal(ctx: Context): org.json.JSONObject {
        val p = prefs(ctx)
        val o = org.json.JSONObject()
        o.put("versi", 1)
        o.put("diperbarui", System.currentTimeMillis())
        o.put("favorit", org.json.JSONArray(bacaFavorit(ctx).toList()))
        o.put("pengingat", org.json.JSONArray(bacaPengingat(ctx).toList()))
        o.put("lanjut", org.json.JSONArray(p.getStringSet("lanjut", emptySet())?.toList() ?: emptyList<String>()))
        o.put("riwayat", org.json.JSONArray(p.getStringSet("riwayat", emptySet())?.toList() ?: emptyList<String>()))
        val posisi = org.json.JSONObject()
        for ((k, v) in p.all) {
            if (k.startsWith("pos_") && v is Int) posisi.put(k.removePrefix("pos_"), v)
        }
        o.put("posisi", posisi)
        return o
    }

    private fun gabungPaket(
        ctx: Context,
        jauh: org.json.JSONObject,
    ) {
        val p = prefs(ctx)
        val e = p.edit()
        val fav = bacaFavorit(ctx).toMutableSet()
        val arrF = jauh.optJSONArray("favorit")
        if (arrF != null) for (i in 0 until arrF.length()) fav.add(arrF.optString(i))
        e.putStringSet("favorit", fav)
        val ing = bacaPengingat(ctx).toMutableSet()
        val arrI = jauh.optJSONArray("pengingat")
        if (arrI != null) for (i in 0 until arrI.length()) ing.add(arrI.optString(i))
        e.putStringSet("pengingat", ing)

        fun petaLanjut(set: Set<String>): HashMap<String, String> {
            val m = HashMap<String, String>()
            for (s in set) {
                val b = s.split("|")
                val id = b.getOrNull(0) ?: continue
                val wBaru = b.getOrNull(4)?.toLongOrNull() ?: 0L
                val wLama = m[id]?.split("|")?.getOrNull(4)?.toLongOrNull() ?: -1L
                if (wBaru > wLama) m[id] = s
            }
            return m
        }
        val lanjut = petaLanjut(p.getStringSet("lanjut", emptySet())?.toSet() ?: emptySet())
        val arrL = jauh.optJSONArray("lanjut")
        if (arrL != null) {
            val setJauh = HashSet<String>()
            for (i in 0 until arrL.length()) setJauh.add(arrL.optString(i))
            for ((id, s) in petaLanjut(setJauh)) {
                val wLama = lanjut[id]?.split("|")?.getOrNull(4)?.toLongOrNull() ?: -1L
                val wBaru = s.split("|").getOrNull(4)?.toLongOrNull() ?: 0L
                if (wBaru > wLama) lanjut[id] = s
            }
        }
        e.putStringSet("lanjut", lanjut.values.toSet())

        fun petaRiwayat(set: Set<String>): HashMap<String, Long> {
            val m = HashMap<String, Long>()
            for (s in set) {
                val b = s.split("|")
                val id = b.getOrNull(0) ?: continue
                val w = b.getOrNull(1)?.toLongOrNull() ?: 0L
                if (w > (m[id] ?: -1L)) m[id] = w
            }
            return m
        }
        val riw = petaRiwayat(p.getStringSet("riwayat", emptySet())?.toSet() ?: emptySet())
        val arrR = jauh.optJSONArray("riwayat")
        if (arrR != null) {
            val setJauh = HashSet<String>()
            for (i in 0 until arrR.length()) setJauh.add(arrR.optString(i))
            for ((id, w) in petaRiwayat(setJauh)) if (w > (riw[id] ?: -1L)) riw[id] = w
        }
        e.putStringSet(
            "riwayat",
            riw.entries
                .sortedByDescending { it.value }
                .take(100)
                .map { "${it.key}|${it.value}" }
                .toSet(),
        )
        val posJauh = jauh.optJSONObject("posisi")
        if (posJauh != null) {
            val kunci = posJauh.keys()
            while (kunci.hasNext()) {
                val k = kunci.next()
                val penuh = "pos_$k"
                if (!p.contains(penuh)) e.putInt(penuh, posJauh.optInt(k))
            }
        }
        e.apply()
    }

    fun muatGabung(
        ctx: Context,
        selesai: () -> Unit,
    ) {
        pastikanFirebase(ctx)
        if (sibukMuat) {
            selesai()
            return
        }
        val app = ctx.applicationContext
        latar.execute {
            val auth =
                try {
                    com.google.firebase.auth.FirebaseAuth
                        .getInstance()
                } catch (e: Exception) {
                    null
                }
            if (auth == null) {
                selesai()
                return@execute
            }
            if (auth.currentUser == null) {
                // Sesi Firebase mungkin masih dipulihkan atau token Google
                // baru saja ditukar; tunggu sekali keadaan masuk berubah.
                val latch = java.util.concurrent.CountDownLatch(1)
                val dengar =
                    com.google.firebase.auth.FirebaseAuth.AuthStateListener { a ->
                        if (a.currentUser != null) latch.countDown()
                    }
                auth.addAuthStateListener(dengar)
                try {
                    latch.await(15, java.util.concurrent.TimeUnit.SECONDS)
                } catch (e: Exception) {
                }
                auth.removeAuthStateListener(dengar)
            }
            val uid = auth.currentUser?.uid
            if (uid == null) {
                selesai()
                return@execute
            }
            sibukMuat = true
            try {
                com.google.firebase.firestore.FirebaseFirestore
                    .getInstance()
                    .collection("pengguna")
                    .document(uid)
                    .get()
                    .addOnSuccessListener { dok ->
                        try {
                            val teks = dok.getString("paket")
                            if (teks != null) gabungPaket(app, org.json.JSONObject(teks))
                        } catch (e: Exception) {
                        }
                        sibukMuat = false
                        selesai()
                        jadwalkan(app)
                    }.addOnFailureListener {
                        sibukMuat = false
                        selesai()
                    }
            } catch (e: Exception) {
                sibukMuat = false
                selesai()
            }
        }
    }

    // Unggah paket terbaru, dijeda 5 detik dari perubahan terakhir.
    fun jadwalkan(ctx: Context) {
        val app = ctx.applicationContext
        tugasKirim?.let { penunda.removeCallbacks(it) }
        val t = Runnable { kirim(app) }
        tugasKirim = t
        penunda.postDelayed(t, 5000)
    }

    private fun kirim(ctx: Context) {
        pastikanFirebase(ctx)
        val uid = uidSaya() ?: return
        latar.execute {
            try {
                val paket = paketLokal(ctx)
                val data =
                    hashMapOf<String, Any>(
                        "paket" to paket.toString(),
                        "diperbarui" to paket.optLong("diperbarui"),
                    )
                com.google.firebase.firestore.FirebaseFirestore
                    .getInstance()
                    .collection("pengguna")
                    .document(uid)
                    .set(data)
            } catch (e: Exception) {
            }
        }
    }
}

private fun fmtWaktu(ms: Int): String {
    val total = (ms / 1000).coerceAtLeast(0)
    val j = total / 3600
    val m = (total % 3600) / 60
    val d = total % 60
    return if (j > 0) "%d:%02d:%02d".format(java.util.Locale.US, j, m, d) else "%d:%02d".format(java.util.Locale.US, m, d)
}

private fun fmtSkor(s: Double): String = if (s <= 0.0) "–" else "%.1f".format(java.util.Locale.US, s).replace('.', ',')

// ================= Ikon gambar sendiri (Canvas) =================
@Composable
fun Ikon(
    nama: String,
    modifier: Modifier = Modifier,
    warna: Color = Redup,
    terisi: Boolean = false,
    tebal: Float = 0.085f,
    solid: Boolean = false,
    potong: Color = Kartu,
) {
    Canvas(modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val s =
            Stroke(
                width = w * tebal,
                cap = androidx.compose.ui.graphics.StrokeCap.Round,
                join = androidx.compose.ui.graphics.StrokeJoin.Round,
            )

        fun titik(
            x: Float,
            y: Float,
        ) = Offset(w * x, h * y)
        // isi ikon: transparan (duotone) atau penuh (solid) untuk tab aktif
        val isi = warna.copy(alpha = if (solid) 1f else 0.30f)
        val warnaPotong = if (solid) potong else warna
        when (nama) {
            "putar" -> {
                val p = Path()
                p.moveTo(w * 0.34f, h * 0.24f)
                p.lineTo(w * 0.76f, h * 0.5f)
                p.lineTo(w * 0.34f, h * 0.76f)
                p.close()
                drawPath(p, warna, style = Fill)
            }
            "jeda" -> {
                drawRoundRect(
                    warna,
                    topLeft = titik(0.30f, 0.24f),
                    size = Size(w * 0.13f, h * 0.52f),
                    cornerRadius = CornerRadius(w * 0.05f),
                )
                drawRoundRect(
                    warna,
                    topLeft = titik(0.57f, 0.24f),
                    size = Size(w * 0.13f, h * 0.52f),
                    cornerRadius = CornerRadius(w * 0.05f),
                )
            }
            // Ikon navigasi gaya duotone: garis tipis + isi transparan saat aktif (terisi = true)
            "beranda" -> {
                fun X(v: Float) = w * v / 24f

                fun Y(v: Float) = h * v / 24f
                val rumah = Path()
                rumah.moveTo(X(4f), Y(10.6f))
                rumah.lineTo(X(10.5f), Y(4.7f))
                rumah.quadraticTo(X(12f), Y(3.4f), X(13.5f), Y(4.7f))
                rumah.lineTo(X(20f), Y(10.6f))
                rumah.lineTo(X(20f), Y(17.8f))
                rumah.quadraticTo(X(20f), Y(20.4f), X(17.4f), Y(20.4f))
                rumah.lineTo(X(6.6f), Y(20.4f))
                rumah.quadraticTo(X(4f), Y(20.4f), X(4f), Y(17.8f))
                rumah.close()
                if (terisi) drawPath(rumah, isi, style = Fill)
                drawPath(rumah, warna, style = s)
                val pintu = Path()
                pintu.moveTo(X(9.6f), Y(20.4f))
                pintu.lineTo(X(9.6f), Y(16.4f))
                pintu.quadraticTo(X(9.6f), Y(14.6f), X(12f), Y(14.6f))
                pintu.quadraticTo(X(14.4f), Y(14.6f), X(14.4f), Y(16.4f))
                pintu.lineTo(X(14.4f), Y(20.4f))
                drawPath(pintu, warnaPotong, style = s)
            }
            "jadwal" -> {
                fun X(v: Float) = w * v / 24f

                fun Y(v: Float) = h * v / 24f
                val tutup = androidx.compose.ui.graphics.StrokeCap.Round
                val kal = Path()
                kal.addRoundRect(RoundRect(X(4f), Y(5.2f), X(20f), Y(20.4f), CornerRadius(X(3.4f))))
                if (terisi) drawPath(kal, isi, style = Fill)
                drawPath(kal, warna, style = s)
                drawLine(warnaPotong, Offset(X(4f), Y(10f)), Offset(X(20f), Y(10f)), strokeWidth = s.width)
                drawLine(warna, Offset(X(8.2f), Y(2.9f)), Offset(X(8.2f), Y(6.6f)), strokeWidth = s.width, cap = tutup)
                drawLine(warna, Offset(X(15.8f), Y(2.9f)), Offset(X(15.8f), Y(6.6f)), strokeWidth = s.width, cap = tutup)
                for (tx in listOf(8.4f, 12f, 15.6f)) drawCircle(warnaPotong, radius = X(1f), center = Offset(X(tx), Y(14.8f)), style = Fill)
            }
            "markah" -> {
                fun X(v: Float) = w * v / 24f

                fun Y(v: Float) = h * v / 24f
                val p = Path()
                p.moveTo(X(7.2f), Y(3.6f))
                p.lineTo(X(16.8f), Y(3.6f))
                p.quadraticTo(X(18.4f), Y(3.6f), X(18.4f), Y(5.2f))
                p.lineTo(X(18.4f), Y(19.8f))
                p.quadraticTo(X(18.4f), Y(21f), X(17.4f), Y(20.3f))
                p.lineTo(X(12.7f), Y(16.9f))
                p.quadraticTo(X(12f), Y(16.4f), X(11.3f), Y(16.9f))
                p.lineTo(X(6.6f), Y(20.3f))
                p.quadraticTo(X(5.6f), Y(21f), X(5.6f), Y(19.8f))
                p.lineTo(X(5.6f), Y(5.2f))
                p.quadraticTo(X(5.6f), Y(3.6f), X(7.2f), Y(3.6f))
                p.close()
                if (terisi) drawPath(p, isi, style = Fill)
                drawPath(p, warna, style = s)
            }
            "unduhan" -> {
                fun X(v: Float) = w * v / 24f

                fun Y(v: Float) = h * v / 24f
                val tutup = androidx.compose.ui.graphics.StrokeCap.Round
                if (terisi) {
                    val wadah = Path()
                    wadah.moveTo(X(4.4f), Y(15.8f))
                    wadah.lineTo(X(4.4f), Y(17.6f))
                    wadah.quadraticTo(X(4.4f), Y(20.4f), X(7.2f), Y(20.4f))
                    wadah.lineTo(X(16.8f), Y(20.4f))
                    wadah.quadraticTo(X(19.6f), Y(20.4f), X(19.6f), Y(17.6f))
                    wadah.lineTo(X(19.6f), Y(15.8f))
                    wadah.close()
                    drawPath(wadah, isi, style = Fill)
                }
                val nampan = Path()
                nampan.moveTo(X(4.4f), Y(15.8f))
                nampan.lineTo(X(4.4f), Y(17.6f))
                nampan.quadraticTo(X(4.4f), Y(20.4f), X(7.2f), Y(20.4f))
                nampan.lineTo(X(16.8f), Y(20.4f))
                nampan.quadraticTo(X(19.6f), Y(20.4f), X(19.6f), Y(17.6f))
                nampan.lineTo(X(19.6f), Y(15.8f))
                drawPath(nampan, warna, style = s)
                drawLine(warna, Offset(X(12f), Y(3.8f)), Offset(X(12f), Y(14.2f)), strokeWidth = s.width, cap = tutup)
                drawLine(warna, Offset(X(7.9f), Y(10.2f)), Offset(X(12f), Y(14.3f)), strokeWidth = s.width, cap = tutup)
                drawLine(warna, Offset(X(16.1f), Y(10.2f)), Offset(X(12f), Y(14.3f)), strokeWidth = s.width, cap = tutup)
            }
            "akun" -> {
                fun X(v: Float) = w * v / 24f

                fun Y(v: Float) = h * v / 24f
                if (terisi) drawCircle(isi, radius = X(3.8f), center = Offset(X(12f), Y(7.8f)), style = Fill)
                drawCircle(warna, radius = X(3.8f), center = Offset(X(12f), Y(7.8f)), style = s)
                val p = Path()
                p.moveTo(X(4.8f), Y(19.4f))
                p.cubicTo(X(4.8f), Y(16.4f), X(7.8f), Y(14.8f), X(12f), Y(14.8f))
                p.cubicTo(X(16.2f), Y(14.8f), X(19.2f), Y(16.4f), X(19.2f), Y(19.4f))
                p.quadraticTo(X(19.2f), Y(20.6f), X(18f), Y(20.6f))
                p.lineTo(X(6f), Y(20.6f))
                p.quadraticTo(X(4.8f), Y(20.6f), X(4.8f), Y(19.4f))
                p.close()
                if (terisi) drawPath(p, isi, style = Fill)
                drawPath(p, warna, style = s)
            }
            "cari" -> {
                drawCircle(warna, radius = w * 0.26f, center = titik(0.44f, 0.44f), style = s)
                drawLine(
                    warna,
                    titik(0.63f, 0.63f),
                    titik(0.82f, 0.82f),
                    strokeWidth = w * 0.09f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
            }
            "lonceng" -> {
                val p = Path()
                p.moveTo(w * 0.50f, h * 0.16f)
                p.quadraticTo(w * 0.72f, h * 0.20f, w * 0.72f, h * 0.46f)
                p.lineTo(w * 0.78f, h * 0.66f)
                p.lineTo(w * 0.22f, h * 0.66f)
                p.lineTo(w * 0.28f, h * 0.46f)
                p.quadraticTo(w * 0.28f, h * 0.20f, w * 0.50f, h * 0.16f)
                drawPath(p, warna, style = if (terisi) Fill else s)
                drawCircle(warna, radius = w * 0.06f, center = titik(0.50f, h * 0.76f), style = Fill)
            }
            "kembali" -> {
                drawLine(
                    warna,
                    titik(0.66f, 0.20f),
                    titik(0.36f, 0.50f),
                    strokeWidth = w * 0.10f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.36f, 0.50f),
                    titik(0.66f, 0.80f),
                    strokeWidth = w * 0.10f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
            }
            "bintang" -> {
                val p = Path()
                val cx = w * 0.5f
                val cy = h * 0.52f
                val rLuar = w * 0.34f
                val rDalam = w * 0.15f
                for (i in 0 until 10) {
                    val r = if (i % 2 == 0) rLuar else rDalam
                    val sudut = Math.toRadians(-90.0 + i * 36.0)
                    val x = cx + (r * cos(sudut)).toFloat()
                    val y = cy + (r * sin(sudut)).toFloat()
                    if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
                }
                p.close()
                drawPath(p, warna, style = Fill)
            }
            "hati" -> {
                val p = Path()
                p.moveTo(w * 0.50f, h * 0.82f)
                p.cubicTo(w * 0.10f, h * 0.55f, w * 0.22f, h * 0.16f, w * 0.50f, h * 0.34f)
                p.cubicTo(w * 0.78f, h * 0.16f, w * 0.90f, h * 0.55f, w * 0.50f, h * 0.82f)
                drawPath(p, warna, style = if (terisi) Fill else s)
            }
            "bagikan" -> {
                drawCircle(warna, radius = w * 0.10f, center = titik(0.76f, 0.22f), style = s)
                drawCircle(warna, radius = w * 0.10f, center = titik(0.24f, 0.50f), style = s)
                drawCircle(warna, radius = w * 0.10f, center = titik(0.76f, 0.78f), style = s)
                drawLine(warna, titik(0.33f, 0.44f), titik(0.67f, 0.27f), strokeWidth = w * 0.07f)
                drawLine(warna, titik(0.33f, 0.56f), titik(0.67f, 0.73f), strokeWidth = w * 0.07f)
            }
            "kirim" -> {
                val p = Path()
                p.moveTo(w * 0.16f, h * 0.50f)
                p.lineTo(w * 0.86f, h * 0.20f)
                p.lineTo(w * 0.62f, h * 0.86f)
                p.lineTo(w * 0.48f, h * 0.60f)
                p.close()
                drawPath(p, warna, style = if (terisi) Fill else s)
            }
            "layarpenuh" -> {
                val c = w * 0.09f
                drawLine(
                    warna,
                    titik(0.16f, 0.34f),
                    titik(0.16f, 0.16f),
                    strokeWidth = c,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.16f, 0.16f),
                    titik(0.34f, 0.16f),
                    strokeWidth = c,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.66f, 0.16f),
                    titik(0.84f, 0.16f),
                    strokeWidth = c,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.84f, 0.16f),
                    titik(0.84f, 0.34f),
                    strokeWidth = c,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.84f, 0.66f),
                    titik(0.84f, 0.84f),
                    strokeWidth = c,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.84f, 0.84f),
                    titik(0.66f, 0.84f),
                    strokeWidth = c,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.34f, 0.84f),
                    titik(0.16f, 0.84f),
                    strokeWidth = c,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.16f, 0.84f),
                    titik(0.16f, 0.66f),
                    strokeWidth = c,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
            }
            "tambah" -> {
                drawLine(
                    warna,
                    titik(0.50f, 0.22f),
                    titik(0.50f, 0.78f),
                    strokeWidth = w * 0.10f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.22f, 0.50f),
                    titik(0.78f, 0.50f),
                    strokeWidth = w * 0.10f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
            }
            "centang" -> {
                drawLine(
                    warna,
                    titik(0.22f, 0.54f),
                    titik(0.44f, 0.74f),
                    strokeWidth = w * 0.11f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.44f, 0.74f),
                    titik(0.80f, 0.28f),
                    strokeWidth = w * 0.11f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
            }
            "sampah" -> {
                drawLine(
                    warna,
                    titik(0.20f, 0.30f),
                    titik(0.80f, 0.30f),
                    strokeWidth = w * 0.08f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.40f, 0.30f),
                    titik(0.40f, 0.18f),
                    strokeWidth = w * 0.08f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.60f, 0.30f),
                    titik(0.60f, 0.18f),
                    strokeWidth = w * 0.08f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                val p = Path()
                p.moveTo(w * 0.28f, h * 0.30f)
                p.lineTo(w * 0.34f, h * 0.82f)
                p.lineTo(w * 0.66f, h * 0.82f)
                p.lineTo(w * 0.72f, h * 0.30f)
                drawPath(p, warna, style = s)
            }
            "mahkota" -> {
                val p = Path()
                p.moveTo(w * 0.16f, h * 0.72f)
                p.lineTo(w * 0.20f, h * 0.34f)
                p.lineTo(w * 0.38f, h * 0.54f)
                p.lineTo(w * 0.50f, h * 0.30f)
                p.lineTo(w * 0.62f, h * 0.54f)
                p.lineTo(w * 0.80f, h * 0.34f)
                p.lineTo(w * 0.84f, h * 0.72f)
                p.close()
                drawPath(p, warna, style = Fill)
            }
            "subtitle" -> {
                drawRoundRect(
                    warna,
                    topLeft = titik(0.08f, 0.22f),
                    size = Size(w * 0.84f, h * 0.56f),
                    cornerRadius = CornerRadius(w * 0.12f),
                    style = s,
                )
                drawLine(
                    warna,
                    titik(0.24f, 0.42f),
                    titik(0.58f, 0.42f),
                    strokeWidth = w * 0.08f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.24f, 0.60f),
                    titik(0.72f, 0.60f),
                    strokeWidth = w * 0.08f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
            }
            "geser" -> {
                // ikon pengatur (sliders) untuk menu Pengaturan
                drawLine(
                    warna,
                    titik(0.18f, 0.32f),
                    titik(0.82f, 0.32f),
                    strokeWidth = w * 0.07f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.18f, 0.68f),
                    titik(0.82f, 0.68f),
                    strokeWidth = w * 0.07f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawCircle(warna, radius = w * 0.09f, center = titik(0.62f, 0.32f), style = Fill)
                drawCircle(warna, radius = w * 0.09f, center = titik(0.36f, 0.68f), style = Fill)
            }
            "keluar" -> {
                drawLine(
                    warna,
                    titik(0.30f, 0.20f),
                    titik(0.30f, 0.80f),
                    strokeWidth = w * 0.08f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.30f, 0.20f),
                    titik(0.62f, 0.20f),
                    strokeWidth = w * 0.08f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.30f, 0.80f),
                    titik(0.62f, 0.80f),
                    strokeWidth = w * 0.08f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.48f, 0.50f),
                    titik(0.84f, 0.50f),
                    strokeWidth = w * 0.09f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.70f, 0.36f),
                    titik(0.84f, 0.50f),
                    strokeWidth = w * 0.09f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawLine(
                    warna,
                    titik(0.70f, 0.64f),
                    titik(0.84f, 0.50f),
                    strokeWidth = w * 0.09f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
            }
            "mundur10", "maju10" -> {
                // panah melingkar: mundur = berlawanan arah jarum jam, maju = searah jarum jam
                val maju = nama == "maju10"
                val c = Offset(w * 0.5f, h * 0.54f)
                val r = w * 0.32f
                val sudut = if (maju) -120f else -60f
                drawArc(
                    color = warna,
                    startAngle = sudut,
                    sweepAngle = if (maju) -300f else 300f,
                    useCenter = false,
                    topLeft = Offset(c.x - r, c.y - r),
                    size = Size(r * 2f, r * 2f),
                    style = s,
                )
                val rad = sudut * Math.PI.toFloat() / 180f
                val cx = cos(rad)
                val sy = sin(rad)
                val ujung = Offset(c.x + r * cx, c.y + r * sy)
                val tx = if (maju) -sy else sy
                val ty = if (maju) cx else -cx
                val l = w * 0.17f
                val kepala = Path()
                kepala.moveTo(ujung.x + tx * l, ujung.y + ty * l)
                kepala.lineTo(ujung.x + cx * l * 0.85f, ujung.y + sy * l * 0.85f)
                kepala.lineTo(ujung.x - cx * l * 0.85f, ujung.y - sy * l * 0.85f)
                kepala.close()
                drawPath(kepala, warna, style = Fill)
            }
            "surat" -> {
                drawRoundRect(
                    warna,
                    topLeft = titik(0.14f, 0.24f),
                    size = Size(w * 0.72f, h * 0.52f),
                    cornerRadius =
                        androidx.compose.ui.geometry
                            .CornerRadius(w * 0.08f, w * 0.08f),
                    style = s,
                )
                val v = Path()
                v.moveTo(w * 0.16f, h * 0.30f)
                v.lineTo(w * 0.50f, h * 0.56f)
                v.lineTo(w * 0.84f, h * 0.30f)
                drawPath(v, warna, style = s)
            }
            "peringatan" -> {
                val t = Path()
                t.moveTo(w * 0.50f, h * 0.16f)
                t.lineTo(w * 0.88f, h * 0.80f)
                t.lineTo(w * 0.12f, h * 0.80f)
                t.close()
                drawPath(t, warna, style = s)
                drawLine(
                    warna,
                    titik(0.50f, 0.40f),
                    titik(0.50f, 0.58f),
                    strokeWidth = w * 0.085f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
                drawCircle(warna, radius = w * 0.04f, center = titik(0.50f, 0.69f))
            }
            "panah_bawah" -> {
                val c = Path()
                c.moveTo(w * 0.30f, h * 0.40f)
                c.lineTo(w * 0.50f, h * 0.60f)
                c.lineTo(w * 0.70f, h * 0.40f)
                drawPath(c, warna, style = s)
            }
            "berikutnya" -> {
                val seg = Path()
                seg.moveTo(w * 0.26f, h * 0.24f)
                seg.lineTo(w * 0.68f, h * 0.5f)
                seg.lineTo(w * 0.26f, h * 0.76f)
                seg.close()
                drawPath(seg, warna, style = Fill)
                drawLine(
                    warna,
                    titik(0.78f, 0.26f),
                    titik(0.78f, 0.74f),
                    strokeWidth = w * 0.11f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                )
            }
        }
    }
}

// ================= Komponen bersama =================
private val sudahMuncul = java.util.Collections.synchronizedSet(HashSet<String>())

// Kartu naik 28dp sambil memudar masuk, berjeda 45 ms per urutan; sekali per judul per sesi.
@Composable
fun Modifier.munculBertahap(
    kunci: String,
    urutan: Int,
): Modifier {
    val a =
        remember(kunci) {
            androidx.compose.animation.core
                .Animatable(if (sudahMuncul.contains(kunci)) 1f else 0f)
        }
    LaunchedEffect(kunci) {
        if (a.value < 1f) {
            delay(urutan.coerceIn(0, 8) * 45L)
            a.animateTo(
                1f,
                androidx.compose.animation.core
                    .tween(420, easing = androidx.compose.animation.core.FastOutSlowInEasing),
            )
            sudahMuncul.add(kunci)
        }
    }
    return this.graphicsLayer {
        alpha = a.value
        translationY = (1f - a.value) * 28.dp.toPx()
    }
}

@Composable
fun ChipSkor(
    skor: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Latar.copy(alpha = 0xCC / 255f))
            .padding(horizontal = 6.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Ikon("bintang", Modifier.size(11.dp), BintangW, terisi = true)
        Spacer(Modifier.width(3.dp))
        Text(fmtSkor(skor), fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Teks)
    }
}

@Composable
fun KartuPoster(
    j: Judul,
    onKlik: () -> Unit,
    modifier: Modifier = Modifier,
    peringkat: Int? = null,
    lencana: String? = null,
    urutan: Int = 0,
) {
    val sumber =
        remember {
            androidx.compose.foundation.interaction
                .MutableInteractionSource()
        }
    val ditekan by sumber.collectIsPressedAsState()
    val skala by androidx.compose.animation.core
        .animateFloatAsState(if (ditekan) 0.96f else 1f, label = "tekan")
    LaunchedEffect(ditekan) { if (ditekan) PramuatDetail.aksi?.invoke(j) }
    Column(
        modifier
            .munculBertahap(j.id, urutan)
            .graphicsLayer {
                scaleX = skala
                scaleY = skala
            }.clickable(interactionSource = sumber, indication = null) { onKlik() },
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(0.70f)
                .clip(RoundedCornerShape(14.dp)),
        ) {
            Gambar(j.poster, Modifier.fillMaxSize())
            ChipSkor(j.skor, Modifier.align(Alignment.TopEnd).padding(6.dp))
            if (lencana != null) {
                Text(
                    lencana,
                    modifier =
                        Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Zamrud)
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = DiAtasZamrud,
                )
            }
            // lencana tipe tayang (TV / ONA / OVA / Film): selalu di bawah kanan poster
            Text(
                tipeJudul(j),
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Latar.copy(alpha = 0xCC / 255f))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Teks,
            )
            val varian = lencanaVarian(j.judul)
            if (varian != null && peringkat == null) {
                Text(
                    varian,
                    modifier =
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Latar.copy(alpha = 0xCC / 255f))
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Zamrud,
                )
            }
            if (peringkat != null) {
                Box(
                    Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Latar.copy(alpha = 0xE6 / 255f)))),
                )
                Text(
                    "$peringkat",
                    modifier = Modifier.align(Alignment.BottomStart).padding(start = 8.dp, bottom = 2.dp),
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-1).sp,
                    color = Teks,
                )
            }
        }
        Spacer(Modifier.height(7.dp))
        Text(
            rapikanJudul(j.judul),
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Medium,
            color = Teks,
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 16.sp,
        )
        val ketEp = keteranganEpisode(j)
        if (ketEp != null) {
            Spacer(Modifier.height(2.dp))
            Text(
                ketEp,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Zamrud,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun JudulBagian(
    teks: String,
    modifier: Modifier = Modifier,
    onLihatSemua: (() -> Unit)? = null,
) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            teks,
            modifier = Modifier.weight(1f),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.2).sp,
            color = Teks,
        )
        if (onLihatSemua != null) {
            Text(
                "Lihat semua",
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onLihatSemua() }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = Zamrud,
            )
        }
    }
}

@Composable
fun TombolIkonBulat(
    nama: String,
    aktif: Boolean = false,
    onKlik: () -> Unit,
) {
    Box(
        Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(if (aktif) Zamrud.copy(alpha = 0.18f) else Kartu)
            .border(1.dp, if (aktif) Zamrud else Garis, CircleShape)
            .clickable { onKlik() },
        contentAlignment = Alignment.Center,
    ) {
        Ikon(nama, Modifier.size(21.dp), if (aktif) Zamrud else Redup, terisi = aktif)
    }
}

@Composable
fun BilahBawah(
    tab: Int,
    haze: HazeState,
    onTab: (Int) -> Unit,
) {
    val item =
        listOf(
            "beranda" to "Beranda",
            "jadwal" to "Jadwal",
            "markah" to "Favorit",
            "unduhan" to "Unduhan",
            "akun" to "Akun",
        )
    // navigationBarsPadding: navbar naik di atas gesture bar / tombol navigasi sistem
    Box(Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 14.dp, vertical = 8.dp)) {
        // kapsul penuh, ikon garis tipis, tab aktif = pil lebar yang membungkus ikon + label
        Row(
            Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .hazeEffect(haze) { blurRadius = 24.dp }
                .background(Kartu.copy(alpha = 0.58f))
                .border(1.dp, Garis.copy(alpha = 0.8f), CircleShape)
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            item.forEachIndexed { i, (ikon, label) ->
                val aktif = tab == i
                val pil by androidx.compose.animation.core.animateFloatAsState(
                    if (aktif) 1f else 0f,
                    androidx.compose.animation.core
                        .tween(220),
                    label = "pilTab",
                )
                val warna by animateColorAsState(
                    if (aktif) DiAtasZamrud else Redup,
                    androidx.compose.animation.core
                        .tween(220),
                    label = "warnaTab",
                )
                val skala by androidx.compose.animation.core.animateFloatAsState(
                    if (aktif) 1.08f else 1f,
                    androidx.compose.animation.core
                        .spring(dampingRatio = 0.5f, stiffness = 420f),
                    label = "skalaTab",
                )
                // tanpa teks: label hanya untuk pembaca layar (TalkBack)
                Box(
                    Modifier
                        .weight(1f)
                        .height(48.dp)
                        .then(
                            if (aktif) {
                                Modifier.shadow(8.dp, CircleShape, ambientColor = Zamrud, spotColor = Zamrud)
                            } else {
                                Modifier
                            },
                        ).clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(ZamrudTerang, Zamrud)),
                            alpha = pil,
                        ).selectable(selected = aktif, role = Role.Tab) { onTab(i) }
                        .semantics { contentDescription = label },
                    contentAlignment = Alignment.Center,
                ) {
                    Ikon(
                        ikon,
                        Modifier.size(26.dp).graphicsLayer {
                            scaleX = skala
                            scaleY = skala
                        },
                        warna,
                        terisi = aktif,
                        tebal = 0.07f,
                        solid = aktif,
                        potong = Zamrud,
                    )
                }
            }
        }
    }
}

// Keadaan kosong seragam di seluruh aplikasi: judul singkat tanpa titik + satu baris bantu
// (opsional, diakhiri titik). Dipakai Riwayat, Notifikasi, Jadwal, Favorit per segmen,
// Pencarian, dan Komentar.
@Composable
fun KeadaanKosong(
    judul: String,
    bantu: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.fillMaxWidth().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            judul,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Teks,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
        if (bantu != null) {
            Spacer(Modifier.height(6.dp))
            Text(
                bantu,
                fontSize = 12.5.sp,
                color = Samar,
                lineHeight = 18.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }
    }
}

@Composable
fun KeadaanMuatAtauGagal(
    gagal: Boolean = false,
    onCobaLagi: () -> Unit,
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (gagal) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Daftar tidak bisa dimuat.", color = Redup, fontSize = 14.sp)
                Spacer(Modifier.height(6.dp))
                Text("Periksa sambungan internetmu.", color = Samar, fontSize = 12.5.sp)
                Spacer(Modifier.height(14.dp))
                Button(
                    onClick = onCobaLagi,
                    colors = ButtonDefaults.buttonColors(containerColor = Zamrud, contentColor = DiAtasZamrud),
                    shape = RoundedCornerShape(14.dp),
                ) { Text("Coba Lagi", fontWeight = FontWeight.Bold) }
            }
        } else {
            KerangkaDaftar(Modifier.fillMaxSize())
        }
    }
}

// Kerangka halaman daftar: kisi poster 3 kolom, bentuknya sama dengan kartu aslinya
@Composable
fun KerangkaDaftar(modifier: Modifier = Modifier) {
    Column(modifier.padding(horizontal = 18.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        repeat(3) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(3) {
                    Column(Modifier.weight(1f)) {
                        Kerangka(Modifier.fillMaxWidth().aspectRatio(0.70f).clip(RoundedCornerShape(14.dp)))
                        Spacer(Modifier.height(8.dp))
                        Kerangka(Modifier.fillMaxWidth(0.85f).height(11.dp).clip(RoundedCornerShape(5.dp)))
                        Spacer(Modifier.height(5.dp))
                        Kerangka(Modifier.fillMaxWidth(0.55f).height(11.dp).clip(RoundedCornerShape(5.dp)))
                    }
                }
            }
        }
    }
}

// ================= Beranda =================
// Kerangka beranda: bentuknya sama dengan isi aslinya (header, banner, dua baris poster)
@Composable
fun KerangkaBeranda() {
    Column(Modifier.fillMaxSize().statusBarsPadding().padding(top = 14.dp)) {
        Kerangka(
            Modifier
                .padding(horizontal = 18.dp)
                .width(130.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(10.dp)),
        )
        Spacer(Modifier.height(18.dp))
        Kerangka(
            Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(262.dp)
                .clip(RoundedCornerShape(30.dp)),
        )
        Spacer(Modifier.height(26.dp))
        repeat(2) {
            Kerangka(
                Modifier
                    .padding(horizontal = 18.dp)
                    .width(160.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                repeat(3) {
                    Column(Modifier.width(118.dp)) {
                        Kerangka(Modifier.fillMaxWidth().aspectRatio(0.70f).clip(RoundedCornerShape(14.dp)))
                        Spacer(Modifier.height(8.dp))
                        Kerangka(Modifier.fillMaxWidth(0.85f).height(11.dp).clip(RoundedCornerShape(5.dp)))
                        Spacer(Modifier.height(5.dp))
                        Kerangka(Modifier.fillMaxWidth(0.55f).height(11.dp).clip(RoundedCornerShape(5.dp)))
                    }
                }
            }
            Spacer(Modifier.height(26.dp))
        }
    }
}

@Composable
fun LayarBeranda(
    onDetail: (Judul) -> Unit,
    onPutar: (Judul, Int) -> Unit,
    // false saat tab ini sedang dianimasikan keluar: tombol Kembali tidak boleh lagi
    // diurus oleh pencarian/halaman penuh Beranda yang sebentar lagi hilang.
    aktif: Boolean = true,
    // Tombol simpan di banner hero: ikut daftar Favorit milik aplikasi.
    favorit: Set<String> = emptySet(),
    onToggleFav: (Judul) -> Unit = {},
) {
    val ctx = LocalContext.current
    val daftar = KatalogSumber.daftar
    if (daftar.isEmpty()) {
        if (KatalogSumber.gagal) KeadaanMuatAtauGagal(gagal = true) { KatalogSumber.muatLatar() } else KerangkaBeranda()
        return
    }
    // Unggulan memakai urutan populer dari situs sumber itu sendiri.
    // Hero hanya memuat judul yang sudah bisa ditonton — judul Segera
    // Tayang (belum ada episode) tidak masuk hero.
    val unggulan = remember(daftar) { daftar.filter { it.jumlahEpisode > 0 }.take(5) }
    val idUnggulan = remember(unggulan) { unggulan.map { it.id }.toSet() }
    // "Sedang Trending" cara aplikasi populer (keputusan pengguna
    // 2026-11-23 20:32 WIB): semua judul tren tampil berurutan mengikuti
    // peringkat aslinya di indeks katalog ("sedangTren"), TERMASUK yang
    // jadi hero — nomor peringkat di kartu adalah nomor aslinya.
    val trending =
        remember(daftar, KatalogSumber.idTren) {
            KatalogSumber.idTren.mapNotNull { idt -> daftar.firstOrNull { it.id == idt } }.take(14)
        }
    // Judul yang SUDAH tayang (punya episode). Aturan pengguna 2026-11-23
    // 21:53 WIB: judul belum tayang HANYA boleh tampil di baris
    // "Segera Tayang" — tidak di baris lain, tidak di "Semua Anime",
    // tidak di baris genre halaman Cari.
    val sudahTayang = remember(daftar) { daftar.filter { it.jumlahEpisode > 0 } }
    val semua = remember(sudahTayang) { sudahTayang.sortedBy { rapikanJudul(it.judul).lowercase() } }
    // Genre asli saja untuk Jelajahi Genre di halaman Cari — data katalog penuh
    // label bukan-genre (Earth, Japan, Present, ...) yang harus disaring.
    val genreBersih =
        remember(daftar) {
            val sah =
                listOf(
                    "Action",
                    "Comedy",
                    "Drama",
                    "Fantasy",
                    "Adventure",
                    "Romance",
                    "Slice of Life",
                    "Science Fiction",
                    "Supernatural",
                    "Mystery",
                    "Horror",
                    "Psychological",
                    "Thriller",
                    "Super Power",
                    "Martial Arts",
                    "Historical",
                    "School Life",
                    "Sports",
                    "Music",
                    "Mecha",
                    "Isekai",
                )
            val hitung = daftar.flatMap { it.genres }.groupingBy { it }.eachCount()
            sah.filter { (hitung[it] ?: 0) > 0 }.sortedByDescending { hitung[it] ?: 0 }
        }
    var modeCari by rememberSaveable { mutableStateOf(false) }
    var kueri by rememberSaveable { mutableStateOf("") }
    var penuh by rememberSaveable { mutableStateOf<String?>(null) }
    // Saringan halaman "Semua Anime" (gaya aplikasi populer): saring genre
    // + pilihan urutan. Setelan disetel ulang tiap halaman penuh berganti.
    var saringGenre by rememberSaveable { mutableStateOf<String?>(null) }
    var urutanSemua by rememberSaveable { mutableStateOf("A–Z") }
    LaunchedEffect(penuh) {
        if (penuh != "Semua Anime") {
            saringGenre = null
            urutanSemua = "A–Z"
        }
    }
    val hasilCari =
        remember(kueri, daftar) {
            if (kueri.isBlank()) {
                emptyList()
            } else {
                daftar.filter { it.judul.contains(kueri.trim(), ignoreCase = true) }.take(60)
            }
        }
    val fokusCari = remember { FocusRequester() }
    // papan ketik hanya dibuka saat pencarian baru dimulai, bukan saat kembali dari Detail
    LaunchedEffect(modeCari) { if (modeCari && kueri.isBlank()) fokusCari.requestFocus() }
    var kotakNotif by rememberSaveable { mutableStateOf(false) }
    BackHandler(enabled = aktif && (penuh != null || modeCari || kotakNotif)) {
        if (kotakNotif) {
            kotakNotif = false
        } else if (penuh != null) {
            penuh = null
        } else {
            modeCari = false
            kueri = ""
        }
    }
    var progres by remember { mutableStateOf(bacaProgres(ctx)) }
    var riwayat by remember { mutableStateOf(bacaRiwayat(ctx)) }
    LaunchedEffect(daftar) {
        progres = bacaProgres(ctx)
        riwayat = bacaRiwayat(ctx)
    }
    val lanjut =
        remember(daftar, progres) {
            progres.mapNotNull { p -> daftar.firstOrNull { it.id == p.idJudul }?.let { it to p } }
        }
    // Rekomendasi belajar dari genre tontonan terakhir (bukan acak).
    val rekomendasiFull =
        remember(daftar, lanjut) {
            val selera = lanjut.flatMap { it.first.genres }.groupingBy { it }.eachCount()
            val sudah = lanjut.map { it.first.id }.toSet()
            val kandidat = sudahTayang.filter { it.id !in idUnggulan && it.id !in sudah }
            if (selera.isEmpty()) {
                kandidat.sortedByDescending { it.skor }
            } else {
                kandidat.sortedWith(
                    compareByDescending<Judul> { j -> j.genres.sumOf { g -> selera[g] ?: 0 } }
                        .thenByDescending { it.skor },
                )
            }
        }
    // "Baru Ditambahkan" = daftar "terbaru" dari situs sumber (yang paling
    // baru diperbarui di situsnya), tanpa judul unggulan hero. Dulu bagian ini
    // memakai selisih id terhadap kunjungan sebelumnya; cara itu rusak oleh
    // pengisian katalog halaman-demi-halaman (tiap halaman dianggap "baru"
    // dan isinya berganti terus). Daftar terbaru situs memang data aslinya.
    // Tahun berjalan — penentu "benar-benar baru" untuk baris
    // "Anime Baru Rilis" (aturan pengguna 2026-11-23 20:23 WIB: anime lama
    // tidak tampil di baris rilis baru; cukup di "Semua Anime").
    val tahunIni =
        remember {
            java.text
                .SimpleDateFormat("yyyy", java.util.Locale.US)
                .format(java.util.Date())
                .toIntOrNull() ?: 0
        }
    // "Baru Ditambahkan" diurutkan dari tanggal segar paling baru (judul yang
    // dapat episode baru ikut naik lagi ke depan lewat tanggal segarnya);
    // urutan "terbaru" katalog tetap jadi penentu bila tanggalnya sama/kosong.
    // Baris ini mengikuti cara aplikasi populer (keputusan pengguna
    // 2026-11-23 20:26 WIB): patokannya tanggal masuk katalog, BUKAN tahun
    // rilis — anime lama yang baru ditambahkan tetap tampil di sini.
    val baru =
        remember(KatalogSumber.terbaru, idUnggulan) {
            KatalogSumber.terbaru
                .filter { it.id !in idUnggulan && it.jumlahEpisode > 0 }
                .sortedByDescending { it.segar }
        }
    // "Anime Baru Rilis" = hanya judul rilis tahun berjalan, diurutkan dari
    // tahun rilis paling baru (tahun sama: yang skornya lebih tinggi dulu).
    // Judul tahun lama tidak ikut — cukup tampil di "Semua Anime".
    val baruRilis =
        remember(sudahTayang, tahunIni) {
            sudahTayang
                .filter { it.tahun >= tahunIni }
                .sortedWith(compareByDescending<Judul> { it.tahun }.thenByDescending { it.skor })
        }
    // "Donghua" = hanya judul berjenis donghua dari indeks katalog.
    // Katalog belum berisi donghua -> barisnya tidak ditampilkan sama sekali
    // (tidak ada baris kosong); muncul sendiri begitu ada donghua masuk.
    val donghua =
        remember(sudahTayang) {
            sudahTayang.filter { it.jenis.equals("donghua", ignoreCase = true) }
        }
    // "Segera Tayang" = judul yang belum rilis (status NOT_YET_RELEASED).
    // Barisnya hanya tampil bila katalog memuat judul semacam itu;
    // pengingat loncengnya ada di halaman detail judul itu.
    val segeraTayang =
        remember(daftar) {
            daftar.filter { labelStatus(it.status) == "Segera Tayang" }
        }
    // Riwayat tontonan: gabungan catatan riwayat + progres yang sedang
    // berjalan, yang paling akhir ditonton di depan.
    val riwayatJudul =
        remember(daftar, riwayat, progres) {
            val gabung = LinkedHashMap<String, Long>()
            for ((id, w) in riwayat) gabung[id] = maxOf(gabung[id] ?: 0L, w)
            for (pr in progres) gabung[pr.idJudul] = maxOf(gabung[pr.idJudul] ?: 0L, pr.waktu)
            gabung.entries
                .sortedByDescending { it.value }
                .mapNotNull { (id, w) -> daftar.firstOrNull { it.id == id }?.let { j -> j to w } }
        }
    val atas = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val pen = penuh
    // Daftar halaman penuh dihitung dari keadaan TERKINI (bukan salinan saat
    // tombol diketuk) — katalog yang masih terisi di latar ikut tampil.
    val daftarPen: List<Judul> =
        when (pen) {
            null -> emptyList()
            "Sedang Trending" -> trending
            "Baru Ditambahkan" -> baru
            "Rekomendasi Untukmu" -> rekomendasiFull.take(60)
            "Anime Baru Rilis" -> baruRilis
            "Donghua" -> donghua
            "Segera Tayang" -> segeraTayang
            "Riwayat Tontonan" -> emptyList()
            "Semua Anime" ->
                semua
                    .filter { saringGenre == null || saringGenre in it.genres }
                    .let { l ->
                        when (urutanSemua) {
                            "Nilai Tertinggi" -> l.sortedByDescending { it.skor }
                            "Tahun Terbaru" -> l.sortedByDescending { it.tahun }
                            "Baru Masuk" -> l.sortedByDescending { it.segar }
                            else -> l // "A–Z": daftar `semua` memang sudah urut abjad
                        }
                    }
            else -> sudahTayang.filter { x -> pen in x.genres }.sortedByDescending { x -> x.skor }
        }
    val normal = !modeCari && pen == null
    val kabar = remember(versiKabar) { bacaKabar(ctx) }
    val adaBaru = kabar.any { !it.dibaca }

    // Cahaya zamrud lembut di pojok atas (latar bilah atas + banner)
    Column(
        Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Zamrud.copy(alpha = 0.16f), Color.Transparent),
                    center = Offset(180f, 0f),
                    radius = 1100f,
                ),
            ),
    ) {
        // Box: hanya untuk menumpuk halaman Notifikasi di atas daftar. Bilah atas
        // (Anime.id, cari, lonceng) adalah item pertama daftar, jadi ikut tergulir
        // naik bersama poster sampai terpotong tepat di bawah status bar.
        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                // Normal: daftar dipotong tepat di bawah status bar.
                Modifier.fillMaxSize().then(if (normal) Modifier.padding(top = atas) else Modifier),
                contentPadding = PaddingValues(top = if (normal) 0.dp else atas, bottom = 118.dp),
            ) {
                if (normal) {
                    item {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp, start = 20.dp, end = 20.dp, bottom = 18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            // Logo kapsul kaca: huruf modern tanpa ikon, ".id" bergradien zamrud
                            Row(Modifier.weight(1f)) {
                                Row(
                                    Modifier
                                        .height(42.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(Color(0x2EFFFFFF), Color(0x0FFFFFFF)),
                                            ),
                                        ).border(
                                            1.dp,
                                            Brush.horizontalGradient(
                                                listOf(ZamrudTerang.copy(alpha = 0.55f), Color(0x1FFFFFFF)),
                                            ),
                                            RoundedCornerShape(50),
                                        ).padding(horizontal = 18.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        "anime",
                                        fontSize = 21.sp,
                                        fontWeight = FontWeight.Medium,
                                        letterSpacing = (-0.5).sp,
                                        color = Teks,
                                    )
                                    Text(
                                        ".id",
                                        style =
                                            TextStyle(
                                                brush = Brush.horizontalGradient(listOf(ZamrudTerang, Zamrud)),
                                                fontFamily = Inter,
                                                fontSize = 21.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                letterSpacing = (-0.5).sp,
                                            ),
                                    )
                                }
                            }
                            // Tombol kaca bulat: cari
                            Box(
                                Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x1AFFFFFF))
                                    .border(1.dp, Color(0x29FFFFFF), CircleShape)
                                    .clickable { modeCari = true },
                                contentAlignment = Alignment.Center,
                            ) { Ikon("cari", Modifier.size(22.dp), Teks) }
                            Spacer(Modifier.width(10.dp))
                            // Tombol kaca bulat: lonceng (notifikasi belum ada -> "Segera hadir", sama seperti menu Akun)
                            Box(
                                Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x1AFFFFFF))
                                    .border(1.dp, Color(0x29FFFFFF), CircleShape)
                                    .clickable {
                                        kotakNotif = true
                                        tandaiKabarDibaca(ctx)
                                    },
                                contentAlignment = Alignment.Center,
                            ) {
                                Ikon("lonceng", Modifier.size(22.dp), Teks)
                                if (adaBaru) {
                                    Box(
                                        Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(top = 7.dp, end = 8.dp)
                                            .size(12.dp)
                                            .background(Zamrud, CircleShape)
                                            .border(2.dp, Color(0xFF1B2B22), CircleShape),
                                    )
                                }
                            }
                        }
                    }
                }
                if (pen != null) {
                    item {
                        Row(
                            Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                Modifier.size(40.dp).clip(CircleShape).clickable { penuh = null },
                                contentAlignment = Alignment.Center,
                            ) { Ikon("kembali", Modifier.size(21.dp), Redup) }
                            Spacer(Modifier.width(6.dp))
                            Text(pen, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Teks)
                        }
                        Spacer(Modifier.height(10.dp))
                    }
                    if (pen == "Riwayat Tontonan") {
                        if (riwayatJudul.isEmpty()) {
                            item {
                                KeadaanKosong(
                                    "Belum ada riwayat",
                                    "Judul yang kamu tonton akan tercatat di sini.",
                                    Modifier.padding(vertical = 24.dp),
                                )
                            }
                        } else {
                            item {
                                Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 2.dp)) {
                                    Spacer(Modifier.weight(1f))
                                    Text(
                                        "Hapus Semua",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MerahLembut,
                                        modifier =
                                            Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable {
                                                    hapusSemuaRiwayat(ctx)
                                                    riwayat = emptyList()
                                                    progres = emptyList()
                                                }.padding(horizontal = 8.dp, vertical = 4.dp),
                                    )
                                }
                                Spacer(Modifier.height(6.dp))
                            }
                            items(riwayatJudul, key = { it.first.id }) { (j, w) ->
                                val p = progres.firstOrNull { it.idJudul == j.id }
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 18.dp, vertical = 5.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Kartu)
                                        .clickable { onDetail(j) }
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Box(Modifier.width(52.dp).aspectRatio(0.70f).clip(RoundedCornerShape(9.dp))) {
                                        Gambar(j.poster, Modifier.fillMaxSize())
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(
                                            rapikanJudul(j.judul),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Teks,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                        Spacer(Modifier.height(2.dp))
                                        val tgl =
                                            java.text
                                                .SimpleDateFormat(
                                                    "d MMM yyyy",
                                                    java.util.Locale("id", "ID"),
                                                ).format(java.util.Date(w))
                                        Text(
                                            if (p != null) "Episode ${p.indeksEpisode + 1} • $tgl" else tgl,
                                            fontSize = 12.sp,
                                            color = Redup,
                                        )
                                    }
                                    Box(
                                        Modifier.size(36.dp).clip(CircleShape).clickable {
                                            hapusRiwayat(ctx, j.id)
                                            riwayat = bacaRiwayat(ctx)
                                            progres = bacaProgres(ctx)
                                        },
                                        contentAlignment = Alignment.Center,
                                    ) { Ikon("sampah", Modifier.size(17.dp), Redup) }
                                }
                            }
                        }
                    }
                    if (pen == "Semua Anime") {
                        item {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 18.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                items(listOf("Semua Genre") + genreBersih, key = { it }) { g ->
                                    val aktif = (g == "Semua Genre" && saringGenre == null) || g == saringGenre
                                    Text(
                                        g,
                                        modifier =
                                            Modifier
                                                .clip(RoundedCornerShape(20.dp))
                                                .background(if (aktif) Zamrud else Kartu)
                                                .border(1.dp, if (aktif) Zamrud else Garis, RoundedCornerShape(20.dp))
                                                .clickable { saringGenre = if (g == "Semua Genre") null else g }
                                                .padding(horizontal = 14.dp, vertical = 8.dp),
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (aktif) DiAtasZamrud else Teks,
                                    )
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 18.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                items(listOf("A–Z", "Nilai Tertinggi", "Tahun Terbaru", "Baru Masuk"), key = { it }) { u ->
                                    val aktif = u == urutanSemua
                                    Text(
                                        u,
                                        modifier =
                                            Modifier
                                                .clip(RoundedCornerShape(20.dp))
                                                .background(if (aktif) Zamrud else Kartu)
                                                .border(1.dp, if (aktif) Zamrud else Garis, RoundedCornerShape(20.dp))
                                                .clickable { urutanSemua = u }
                                                .padding(horizontal = 14.dp, vertical = 8.dp),
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (aktif) DiAtasZamrud else Teks,
                                    )
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                    items(daftarPen.chunked(3), key = { baris -> baris.first().id }) { baris ->
                        Row(
                            Modifier.fillMaxWidth().padding(horizontal = 18.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            baris.forEach { j ->
                                KartuPoster(j, onKlik = { onDetail(j) }, modifier = Modifier.weight(1f))
                            }
                            repeat(3 - baris.size) { Spacer(Modifier.weight(1f)) }
                        }
                        Spacer(Modifier.height(14.dp))
                    }
                    if (pen == "Semua Anime" && KatalogSumber.mengisi) {
                        item {
                            Text(
                                "Masih memuat katalog… ${daftarPen.size} judul sejauh ini",
                                fontSize = 12.5.sp,
                                color = Samar,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 16.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            )
                        }
                    }
                }
                if (modeCari) {
                    item {
                        Row(
                            Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                Modifier.size(40.dp).clip(CircleShape).clickable {
                                    modeCari = false
                                    kueri = ""
                                },
                                contentAlignment = Alignment.Center,
                            ) { Ikon("kembali", Modifier.size(21.dp), Redup) }
                            Spacer(Modifier.width(6.dp))
                            Row(
                                Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Kartu)
                                    .border(1.dp, Garis, RoundedCornerShape(20.dp))
                                    .padding(horizontal = 14.dp, vertical = 11.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Ikon("cari", Modifier.size(18.dp), Samar)
                                Spacer(Modifier.width(9.dp))
                                Box(Modifier.weight(1f)) {
                                    if (kueri.isEmpty()) Text("Cari anime\u2026", fontSize = 14.sp, color = Samar)
                                    BasicTextField(
                                        value = kueri,
                                        onValueChange = { kueri = it },
                                        singleLine = true,
                                        textStyle = TextStyle(color = Teks, fontSize = 14.sp),
                                        cursorBrush = SolidColor(Zamrud),
                                        modifier = Modifier.fillMaxWidth().focusRequester(fokusCari),
                                    )
                                }
                            }
                        }
                    }
                    if (kueri.isBlank()) {
                        if (trending.isNotEmpty()) {
                            item {
                                Spacer(Modifier.height(10.dp))
                                JudulBagian("Pencarian Populer", Modifier.padding(horizontal = 18.dp))
                                Spacer(Modifier.height(6.dp))
                            }
                            items(trending.take(10).withIndex().toList(), key = { it.value.id }) { (i, j) ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 18.dp, vertical = 5.dp)
                                        .clickable { onDetail(j) },
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        "${i + 1}",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Zamrud,
                                        modifier = Modifier.width(30.dp),
                                    )
                                    Box(Modifier.width(46.dp).aspectRatio(0.70f).clip(RoundedCornerShape(8.dp))) {
                                        Gambar(j.poster, Modifier.fillMaxSize())
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(
                                            rapikanJudul(j.judul),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Teks,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                        val ket = keteranganEpisode(j)
                                        if (ket != null) {
                                            Spacer(Modifier.height(2.dp))
                                            Text(ket, fontSize = 12.sp, color = Redup)
                                        }
                                    }
                                    ChipSkor(j.skor)
                                }
                            }
                        }
                        item {
                            Spacer(Modifier.height(10.dp))
                            JudulBagian("Jelajahi Genre", Modifier.padding(horizontal = 18.dp))
                            Spacer(Modifier.height(12.dp))
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 18.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                items(listOf("Donghua") + genreBersih, key = { it }) { g ->
                                    Text(
                                        g,
                                        modifier =
                                            Modifier
                                                .clip(RoundedCornerShape(20.dp))
                                                .background(Kartu)
                                                .border(1.dp, Garis, RoundedCornerShape(20.dp))
                                                .clickable {
                                                    penuh = g
                                                    modeCari = false
                                                    kueri = ""
                                                }.padding(horizontal = 16.dp, vertical = 9.dp),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Teks,
                                    )
                                }
                            }
                        }
                        // Baris poster per genre (gaya aplikasi populer) — ditempatkan di
                        // halaman pencarian atas perintah pengguna 2026-11-23, bukan di beranda.
                        items(genreBersih.take(6), key = { "baris-$it" }) { g ->
                            Column {
                                Spacer(Modifier.height(20.dp))
                                JudulBagian(
                                    g,
                                    Modifier.padding(horizontal = 18.dp),
                                    onLihatSemua = {
                                        penuh = g
                                        modeCari = false
                                        kueri = ""
                                    },
                                )
                                Spacer(Modifier.height(12.dp))
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 18.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                ) {
                                    val isiGenre =
                                        sudahTayang
                                            .filter { g in it.genres }
                                            .sortedByDescending { it.skor }
                                            .take(10)
                                    items(isiGenre, key = { it.id }) { j ->
                                        KartuPoster(j, onKlik = { onDetail(j) }, modifier = Modifier.width(118.dp))
                                    }
                                }
                            }
                        }
                        if (trending.isNotEmpty()) {
                            item {
                                Spacer(Modifier.height(20.dp))
                                JudulBagian("Sedang Trending", Modifier.padding(horizontal = 18.dp))
                                Spacer(Modifier.height(4.dp))
                            }
                        }
                        items(trending.take(5), key = { it.id }) { j ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable { onDetail(j) }
                                    .padding(horizontal = 18.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box(Modifier.width(46.dp).height(62.dp).clip(RoundedCornerShape(9.dp))) {
                                    Gambar(j.poster, Modifier.fillMaxSize())
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        rapikanJudul(j.judul),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Teks,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        listOfNotNull(
                                            j.genres.firstOrNull(),
                                            if (j.tahun > 0) "${j.tahun}" else null,
                                        ).joinToString(" • "),
                                        fontSize = 12.sp,
                                        color = Redup,
                                    )
                                }
                                ChipSkor(j.skor)
                            }
                        }
                    } else if (hasilCari.isEmpty()) {
                        item {
                            KeadaanKosong(
                                "Tidak ada hasil",
                                "Tidak ada judul yang cocok dengan \u201C$kueri\u201D.",
                                Modifier.padding(vertical = 24.dp),
                            )
                        }
                    } else {
                        item {
                            Spacer(Modifier.height(8.dp))
                            JudulBagian("Hasil Pencarian", Modifier.padding(horizontal = 18.dp))
                            Spacer(Modifier.height(12.dp))
                        }
                        items(hasilCari.chunked(3), key = { baris -> baris.first().id }) { baris ->
                            Row(
                                Modifier.fillMaxWidth().padding(horizontal = 18.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                baris.forEach { j ->
                                    KartuPoster(j, onKlik = { onDetail(j) }, modifier = Modifier.weight(1f))
                                }
                                repeat(3 - baris.size) { Spacer(Modifier.weight(1f)) }
                            }
                            Spacer(Modifier.height(14.dp))
                        }
                    }
                }
                if (normal && unggulan.isNotEmpty()) {
                    item {
                        val n = unggulan.size
                        val pager =
                            androidx.compose.foundation.pager
                                .rememberPagerState(pageCount = { n })
                        LaunchedEffect(n) {
                            if (n > 1) {
                                while (true) {
                                    delay(5500)
                                    pager.animateScrollToPage((pager.currentPage + 1) % n)
                                }
                            }
                        }
                        androidx.compose.foundation.pager.HorizontalPager(
                            state = pager,
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            pageSpacing = 10.dp,
                        ) { halaman ->
                            val u = unggulan[halaman]
                            val meta =
                                listOfNotNull(
                                    u.genres
                                        .take(2)
                                        .joinToString(", ")
                                        .ifBlank { null },
                                    if (u.tahun > 0) "${u.tahun}" else null,
                                    if (u.film) "Film" else "${u.jumlahEpisode} episode",
                                ).joinToString(" \u2022 ")
                            val kaca = Latar.copy(alpha = 0x66 / 255f)
                            val garisKaca = Color(0x29FFFFFF)
                            val disimpan = favorit.contains(u.id)
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(262.dp)
                                    .graphicsLayer {
                                        val o =
                                            kotlin.math
                                                .abs(
                                                    (pager.currentPage - halaman) + pager.currentPageOffsetFraction,
                                                ).coerceIn(0f, 1f)
                                        val sk = 1f - 0.06f * o
                                        scaleX = sk
                                        scaleY = sk
                                        alpha = 1f - 0.45f * o
                                    }.clip(RoundedCornerShape(30.dp))
                                    .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(30.dp))
                                    .clickable { onDetail(u) },
                            ) {
                                Gambar(
                                    u.banner,
                                    Modifier.fillMaxSize().graphicsLayer {
                                        val o = (pager.currentPage - halaman) + pager.currentPageOffsetFraction
                                        scaleX = 1.16f
                                        scaleY = 1.16f
                                        translationX = o * size.width * 0.12f
                                    },
                                    fokusX = u.fokus,
                                )
                                Box(
                                    Modifier.fillMaxSize().background(
                                        Brush.verticalGradient(
                                            0.0f to Latar.copy(alpha = 0x4D / 255f),
                                            0.25f to Color.Transparent,
                                            0.5f to Latar.copy(alpha = 0x8C / 255f),
                                            1.0f to Latar.copy(alpha = 0xF2 / 255f),
                                        ),
                                    ),
                                )
                                // Label "Unggulan" (kiri atas)
                                Row(
                                    Modifier
                                        .align(Alignment.TopStart)
                                        .padding(14.dp)
                                        .clip(CircleShape)
                                        .background(kaca)
                                        .border(1.dp, garisKaca, CircleShape)
                                        .padding(horizontal = 11.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Box(Modifier.size(7.dp).background(Zamrud, CircleShape))
                                    Spacer(Modifier.width(6.dp))
                                    Text("Unggulan", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Teks)
                                }
                                // Skor (kanan atas), hanya bila datanya ada
                                if (u.skor > 0.0) {
                                    Row(
                                        Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(14.dp)
                                            .clip(CircleShape)
                                            .background(kaca)
                                            .border(1.dp, garisKaca, CircleShape)
                                            .padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Ikon("bintang", Modifier.size(13.dp), BintangW, terisi = true)
                                        Spacer(Modifier.width(4.dp))
                                        Text(fmtSkor(u.skor), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Teks)
                                    }
                                }
                                Column(Modifier.align(Alignment.BottomStart).padding(horizontal = 18.dp, vertical = 18.dp)) {
                                    Text(
                                        rapikanJudul(u.judul),
                                        fontSize = 25.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = (-0.7).sp,
                                        color = Teks,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        lineHeight = 28.sp,
                                    )
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        meta,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Teks.copy(alpha = 0.85f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Spacer(Modifier.height(14.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Row(
                                            Modifier
                                                .shadow(10.dp, RoundedCornerShape(22.dp), ambientColor = Zamrud, spotColor = Zamrud)
                                                .clip(RoundedCornerShape(22.dp))
                                                .background(Brush.horizontalGradient(listOf(ZamrudTerang, Zamrud, ZamrudGelap)))
                                                .clickable { onPutar(u, 0) }
                                                .height(44.dp)
                                                .padding(start = 18.dp, end = 24.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Ikon("putar", Modifier.size(18.dp), DiAtasZamrud, terisi = true)
                                            Spacer(Modifier.width(8.dp))
                                            Text("Putar", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = DiAtasZamrud)
                                        }
                                        Spacer(Modifier.width(10.dp))
                                        // Simpan ke Favorit
                                        Box(
                                            Modifier
                                                .size(44.dp)
                                                .clip(CircleShape)
                                                .background(kaca)
                                                .border(1.dp, garisKaca, CircleShape)
                                                .clickable { onToggleFav(u) }
                                                .semantics {
                                                    contentDescription =
                                                        if (disimpan) "Hapus dari favorit" else "Simpan ke favorit"
                                                },
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Ikon("markah", Modifier.size(22.dp), if (disimpan) Zamrud else Teks, terisi = disimpan)
                                        }
                                    }
                                }
                            }
                        }
                        if (n > 1) {
                            Spacer(Modifier.height(12.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                for (i in 0 until n) {
                                    val lebar by androidx.compose.animation.core.animateDpAsState(
                                        if (pager.currentPage == i) 22.dp else 6.dp,
                                        label = "titik",
                                    )
                                    Box(
                                        Modifier
                                            .padding(horizontal = 2.5.dp)
                                            .height(6.dp)
                                            .width(lebar)
                                            .clip(CircleShape)
                                            .background(if (pager.currentPage == i) Zamrud else Color(0x38FFFFFF)),
                                    )
                                }
                            }
                        }
                    }
                }
                if (normal && lanjut.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(24.dp))
                        JudulBagian(
                            "Lanjut Menonton",
                            Modifier.padding(horizontal = 18.dp),
                            onLihatSemua = { penuh = "Riwayat Tontonan" },
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                    items(lanjut.take(3), key = { it.first.id }) { (j, p) ->
                        val ep = j.episodes.getOrNull(p.indeksEpisode)
                        val frac = if (p.durasi > 0) p.posisi.toFloat() / p.durasi else 0f
                        val persen = if (p.durasi > 0) (p.posisi * 100 / p.durasi) else 0
                        val sisaMenit = ((p.durasi - p.posisi) / 60000).coerceAtLeast(0)
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 5.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Kartu)
                                .clickable { onPutar(j, p.indeksEpisode) }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(Modifier.width(128.dp).height(76.dp).clip(RoundedCornerShape(12.dp))) {
                                Gambar(j.banner, Modifier.fillMaxSize(), fokusX = j.fokus)
                                Box(Modifier.fillMaxSize().background(Latar.copy(alpha = 0x40 / 255f)))
                                Box(
                                    Modifier
                                        .align(Alignment.Center)
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(Latar.copy(alpha = 0xB3 / 255f)),
                                    contentAlignment = Alignment.Center,
                                ) { Ikon("putar", Modifier.size(16.dp), Teks, terisi = true) }
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    rapikanJudul(j.judul),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Teks,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Spacer(Modifier.height(3.dp))
                                Text(
                                    "Episode ${ep?.number ?: (p.indeksEpisode + 1)} \u2022 " +
                                        if (sisaMenit >= 1) "sisa $sisaMenit menit" else "hampir selesai",
                                    fontSize = 12.sp,
                                    color = Redup,
                                )
                                Spacer(Modifier.height(9.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        Modifier
                                            .weight(1f)
                                            .height(5.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(Garis),
                                    ) {
                                        Box(
                                            Modifier
                                                .fillMaxWidth(
                                                    frac.coerceIn(0f, 1f),
                                                ).height(5.dp)
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(Zamrud),
                                        )
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Text("$persen%", fontSize = 12.sp, color = Zamrud, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
                // "Segera Tayang" duduk tepat di atas "Sedang Trending"
                // (perintah pengguna 2026-11-23 21:16 WIB).
                if (normal && segeraTayang.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(26.dp))
                        JudulBagian(
                            "Segera Tayang",
                            Modifier.padding(horizontal = 18.dp),
                            onLihatSemua = {
                                penuh = "Segera Tayang"
                            },
                        )
                        Spacer(Modifier.height(12.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 18.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(segeraTayang.take(14), key = { it.id }) { j ->
                                KartuPoster(j, onKlik = { onDetail(j) }, modifier = Modifier.width(118.dp))
                            }
                        }
                    }
                }
                if (normal && trending.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(26.dp))
                        JudulBagian(
                            "Sedang Trending",
                            Modifier.padding(horizontal = 18.dp),
                            onLihatSemua = {
                                penuh = "Sedang Trending"
                            },
                        )
                        Spacer(Modifier.height(12.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 18.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            itemsIndexed(trending, key = { _, j -> j.id }) { idx, j ->
                                KartuPoster(
                                    j,
                                    onKlik = { onDetail(j) },
                                    modifier = Modifier.width(118.dp),
                                    peringkat = idx + 1,
                                    urutan = idx,
                                )
                            }
                        }
                    }
                    if (baru.isNotEmpty()) {
                        item {
                            Spacer(Modifier.height(26.dp))
                            JudulBagian(
                                "Baru Ditambahkan",
                                Modifier.padding(horizontal = 18.dp),
                                onLihatSemua = {
                                    penuh = "Baru Ditambahkan"
                                },
                            )
                            Spacer(Modifier.height(12.dp))
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 18.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                itemsIndexed(baru.take(14), key = { _, j -> j.id }) { idx, j ->
                                    KartuPoster(
                                        j,
                                        onKlik = { onDetail(j) },
                                        modifier = Modifier.width(118.dp),
                                        lencana = if (masihSegar(j)) "BARU" else null,
                                        urutan = idx,
                                    )
                                }
                            }
                        }
                    }
                    if (rekomendasiFull.isNotEmpty()) {
                        item {
                            Spacer(Modifier.height(26.dp))
                            JudulBagian(
                                "Rekomendasi Untukmu",
                                Modifier.padding(horizontal = 18.dp),
                                onLihatSemua = {
                                    penuh = "Rekomendasi Untukmu"
                                },
                            )
                            Spacer(Modifier.height(12.dp))
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 18.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                itemsIndexed(rekomendasiFull.take(14), key = { _, j -> j.id }) { idx, j ->
                                    KartuPoster(j, onKlik = { onDetail(j) }, modifier = Modifier.width(118.dp), urutan = idx)
                                }
                            }
                        }
                    }
                    if (baruRilis.isNotEmpty()) {
                        item {
                            Spacer(Modifier.height(26.dp))
                            JudulBagian(
                                "Anime Baru Rilis",
                                Modifier.padding(horizontal = 18.dp),
                                onLihatSemua = {
                                    penuh = "Anime Baru Rilis"
                                },
                            )
                            Spacer(Modifier.height(12.dp))
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 18.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                items(baruRilis.take(14), key = { it.id }) { j ->
                                    KartuPoster(j, onKlik = { onDetail(j) }, modifier = Modifier.width(118.dp))
                                }
                            }
                        }
                    }
                    if (donghua.isNotEmpty()) {
                        item {
                            Spacer(Modifier.height(26.dp))
                            JudulBagian(
                                "Donghua",
                                Modifier.padding(horizontal = 18.dp),
                                onLihatSemua = {
                                    penuh = "Donghua"
                                },
                            )
                            Spacer(Modifier.height(12.dp))
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 18.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                items(donghua.take(14), key = { it.id }) { j ->
                                    KartuPoster(j, onKlik = { onDetail(j) }, modifier = Modifier.width(118.dp))
                                }
                            }
                        }
                    }
                    item {
                        Spacer(Modifier.height(26.dp))
                        JudulBagian(
                            "Semua Anime",
                            Modifier.padding(horizontal = 18.dp),
                            onLihatSemua = {
                                penuh = "Semua Anime"
                            },
                        )
                        Spacer(Modifier.height(12.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 18.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(semua.take(20), key = { it.id }) { j ->
                                KartuPoster(j, onKlik = { onDetail(j) }, modifier = Modifier.width(118.dp))
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
            if (kotakNotif) {
                // izin/pengaturan notifikasi dicek tiap halaman dibuka dan tiap kembali dari Pengaturan
                var notifAktif by remember { mutableStateOf(true) }
                DisposableEffect(Unit) {
                    fun cek() {
                        notifAktif =
                            try {
                                (
                                    ctx.getSystemService(
                                        Context.NOTIFICATION_SERVICE,
                                    ) as android.app.NotificationManager
                                ).areNotificationsEnabled()
                            } catch (e: Exception) {
                                true
                            }
                    }
                    cek()
                    val pemilik = (ctx as? android.app.Activity) as? androidx.lifecycle.LifecycleOwner
                    val pengamat =
                        androidx.lifecycle.LifecycleEventObserver { _, kejadian ->
                            if (kejadian == androidx.lifecycle.Lifecycle.Event.ON_RESUME) cek()
                        }
                    pemilik?.lifecycle?.addObserver(pengamat)
                    onDispose { pemilik?.lifecycle?.removeObserver(pengamat) }
                }
                Column(
                    Modifier
                        .fillMaxSize()
                        .zIndex(2f)
                        .background(Latar)
                        .statusBarsPadding(),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            Modifier.size(40.dp).clip(CircleShape).clickable { kotakNotif = false },
                            contentAlignment = Alignment.Center,
                        ) { Ikon("kembali", Modifier.size(21.dp), Redup) }
                        Spacer(Modifier.width(6.dp))
                        Text("Notifikasi", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Teks)
                    }
                    if (!notifAktif) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 6.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Kartu)
                                .clickable {
                                    try {
                                        ctx.startActivity(
                                            Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                                                .putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, ctx.packageName),
                                        )
                                    } catch (e: Exception) {
                                    }
                                }.padding(14.dp),
                        ) {
                            Text("Notifikasi dimatikan", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Teks)
                            Spacer(Modifier.height(3.dp))
                            Text(
                                "Kabar episode baru tidak akan muncul di bilah notifikasi. Ketuk untuk mengaktifkannya.",
                                fontSize = 13.sp,
                                color = Redup,
                            )
                        }
                    }
                    if (kabar.isEmpty()) {
                        KeadaanKosong(
                            "Belum ada notifikasi",
                            "Kabar judul dan episode baru akan muncul di sini.",
                            Modifier.padding(vertical = 40.dp),
                        )
                    } else {
                        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 118.dp)) {
                            items(kabar.size) { n ->
                                val k = kabar[n]
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 18.dp, vertical = 5.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Kartu)
                                        .clickable {
                                            val j = k.idJudul?.let { id -> daftar.firstOrNull { it.id == id } }
                                            if (j != null) {
                                                kotakNotif = false
                                                onDetail(j)
                                            }
                                        }.padding(14.dp),
                                ) {
                                    Text(k.judul, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Teks)
                                    Spacer(Modifier.height(3.dp))
                                    Text(k.isi, fontSize = 13.sp, color = Redup)
                                    Spacer(Modifier.height(6.dp))
                                    Text(waktuRelatif(k.waktu), fontSize = 11.5.sp, color = Samar)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ================= Jadwal Rilis =================
// Jam tayang asli diambil dari AniList (airingSchedules), 7 hari ke depan mulai hari ini,
// lalu dicocokkan dengan katalog lewat judul. Tidak ada jam karangan: bila gagal diambil,
// layar menampilkan tombol Coba Lagi.
class SlotRilis(
    val waktu: Long, // detik epoch
    val judul: String,
    val judul2: String, // judul Inggris (boleh kosong), untuk pencocokan katalog
    val episode: Int,
    val format: String,
    val sampul: String, // URL gambar sampul AniList (boleh kosong); poster cadangan bila tak cocok katalog
)

private fun teksAmanJ(
    o: JSONObject?,
    k: String,
): String = if (o == null || o.isNull(k)) "" else o.optString(k)

private fun awalHariLokal(offset: Int): Long {
    val c = java.util.Calendar.getInstance()
    c.set(java.util.Calendar.HOUR_OF_DAY, 0)
    c.set(java.util.Calendar.MINUTE, 0)
    c.set(java.util.Calendar.SECOND, 0)
    c.set(java.util.Calendar.MILLISECOND, 0)
    c.add(java.util.Calendar.DAY_OF_YEAR, offset)
    return c.timeInMillis
}

private fun namaZona(): String {
    val tz = java.util.TimeZone.getDefault()
    return when (tz.id) {
        "Asia/Jakarta", "Asia/Pontianak" -> "WIB"
        "Asia/Makassar", "Asia/Ujung_Pandang" -> "WITA"
        "Asia/Jayapura" -> "WIT"
        else -> tz.getDisplayName(false, java.util.TimeZone.SHORT)
    }
}

private fun normalJudul(s: String): String = s.lowercase().filter { it.isLetterOrDigit() }

private fun cariKatalog(
    s: SlotRilis,
    katalog: List<Pair<String, Judul>>,
): Judul? {
    val a = normalJudul(s.judul)
    val b = normalJudul(s.judul2)
    if (a.isEmpty() && b.isEmpty()) return null
    katalog.firstOrNull { (n, _) -> n == a || (b.isNotEmpty() && n == b) }?.let { return it.second }
    return katalog
        .firstOrNull { (n, _) ->
            n.length >= 10 && (
                (a.length >= 10 && (a.contains(n) || n.contains(a))) ||
                    (b.length >= 10 && (b.contains(n) || n.contains(b)))
            )
        }?.second
}

object JadwalRilis {
    var slot: List<SlotRilis> by mutableStateOf(emptyList())
    var gagal: Boolean by mutableStateOf(false)
    private var jalan = false
    private var terakhir = 0L

    suspend fun muat(paksa: Boolean = false) {
        if (jalan) return
        if (!paksa && slot.isNotEmpty() && System.currentTimeMillis() - terakhir < 30 * 60_000L) return
        jalan = true
        try {
            gagal = false
            val hasil = withContext(Dispatchers.IO) { ambil() }
            if (hasil != null) {
                slot = hasil
                terakhir = System.currentTimeMillis()
            } else if (slot.isEmpty()) {
                gagal = true
            }
        } finally {
            jalan = false
        }
    }

    private fun ambil(): List<SlotRilis>? {
        val awal = awalHariLokal(0) / 1000L
        val akhir = awalHariLokal(7) / 1000L
        val semua = ArrayList<SlotRilis>()
        var halaman = 1
        while (halaman <= 8) {
            val kueri =
                "query{Page(page:" + halaman + ",perPage:50){pageInfo{hasNextPage} " +
                    "airingSchedules(airingAt_greater:" + (awal - 1) + ",airingAt_lesser:" + akhir + ",sort:TIME){" +
                    "airingAt episode media{format countryOfOrigin isAdult title{romaji english} coverImage{large}}}}}"
            val badan = JSONObject().put("query", kueri).toString()
            var conn: HttpURLConnection? = null
            try {
                conn = URL("https://graphql.anilist.co").openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.connectTimeout = 8000
                conn.readTimeout = 15000
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Accept", "application/json")
                conn.setRequestProperty("User-Agent", "AnimeId/1.1")
                conn.outputStream.use { it.write(badan.toByteArray()) }
                if (conn.responseCode !in 200..299) return if (halaman == 1) null else semua
                val teks = conn.inputStream.bufferedReader().readText()
                val page = JSONObject(teks).getJSONObject("data").getJSONObject("Page")
                val larik = page.getJSONArray("airingSchedules")
                for (i in 0 until larik.length()) {
                    val o = larik.getJSONObject(i)
                    val m = o.optJSONObject("media") ?: continue
                    if (m.optBoolean("isAdult")) continue
                    val negara = teksAmanJ(m, "countryOfOrigin")
                    if (negara != "JP" && negara != "CN") continue
                    val fmt = teksAmanJ(m, "format")
                    if (fmt == "MUSIC") continue
                    val t = m.optJSONObject("title")
                    val romaji = teksAmanJ(t, "romaji")
                    val inggris = teksAmanJ(t, "english")
                    val sampul = teksAmanJ(m.optJSONObject("coverImage"), "large")
                    val nama = romaji.ifBlank { inggris }
                    if (nama.isBlank()) continue
                    semua.add(
                        SlotRilis(
                            waktu = o.optLong("airingAt"),
                            judul = nama,
                            judul2 = inggris,
                            episode = o.optInt("episode"),
                            format =
                                when (fmt) {
                                    "TV", "TV_SHORT" -> "TV"
                                    "MOVIE" -> "Film"
                                    else -> fmt
                                },
                            sampul = sampul,
                        ),
                    )
                }
                if (!page.getJSONObject("pageInfo").optBoolean("hasNextPage")) break
            } catch (e: Exception) {
                return if (halaman == 1) null else semua
            } finally {
                conn?.disconnect()
            }
            halaman++
        }
        return semua
    }
}

private fun hitungMundur(
    waktuDetik: Long,
    sekarang: Long,
): String {
    val selisih = waktuDetik * 1000L - sekarang
    if (selisih <= 0L) return "Sudah tayang"
    val jam = selisih / 3_600_000L
    val menit = (selisih % 3_600_000L) / 60_000L
    return when {
        jam >= 24 -> "Dalam " + (jam / 24) + " h " + (jam % 24) + " j"
        jam >= 1 -> "Dalam " + jam + " j " + menit + " m"
        else -> "Dalam " + maxOf(menit, 1L) + " m"
    }
}

private fun jamTayang(detik: Long): String {
    val c =
        java.util.Calendar
            .getInstance()
            .apply { timeInMillis = detik * 1000L }
    return String.format("%02d.%02d", c.get(java.util.Calendar.HOUR_OF_DAY), c.get(java.util.Calendar.MINUTE))
}

@Composable
private fun HariMinggu(
    label: String,
    tgl: Int,
    ada: Boolean,
    aktif: Boolean,
    modifier: Modifier,
    onKlik: () -> Unit,
) {
    val latar by animateColorAsState(if (aktif) Zamrud else Color.Transparent, label = "hari")
    Column(
        modifier
            .clip(RoundedCornerShape(22.dp))
            .background(latar)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onKlik() }
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = if (aktif) DiAtasZamrud else Samar)
        Spacer(Modifier.height(3.dp))
        Text(tgl.toString(), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = if (aktif) DiAtasZamrud else Teks)
        Spacer(Modifier.height(5.dp))
        Box(Modifier.size(4.dp).clip(CircleShape).background(if (ada) (if (aktif) DiAtasZamrud else Zamrud) else Color.Transparent))
    }
}

@Composable
private fun JudulBagian(
    teks: String,
    jumlah: Int,
    aksi: String? = null,
    onAksi: () -> Unit = {},
) {
    Row(
        Modifier.fillMaxWidth().padding(start = 18.dp, end = 18.dp, top = 22.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(teks, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Teks)
        Spacer(Modifier.width(8.dp))
        Text(jumlah.toString(), fontSize = 13.sp, color = Samar, modifier = Modifier.weight(1f))
        if (aksi != null) {
            Text(
                aksi,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Zamrud,
                modifier = Modifier.clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onAksi() },
            )
        }
    }
}

@Composable
private fun HeroRilis(
    s: SlotRilis,
    j: Judul?,
    sekarang: Long,
    suka: Boolean,
    onKlik: () -> Unit,
    onSuka: () -> Unit,
) {
    val bentuk = RoundedCornerShape(28.dp)
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .height(184.dp)
            .clip(bentuk)
            .background(Kartu)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onKlik() },
    ) {
        val gambar = if (j != null) j.poster else s.sampul
        if (gambar.isNotBlank()) Gambar(gambar, Modifier.fillMaxSize())
        Box(
            Modifier.fillMaxSize().background(
                Brush.horizontalGradient(listOf(Latar.copy(alpha = 0.97f), Latar.copy(alpha = 0.82f), Color.Transparent)),
            ),
        )
        Column(Modifier.fillMaxHeight().fillMaxWidth(0.68f).padding(18.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Column {
                Box(Modifier.clip(RoundedCornerShape(50)).background(Zamrud).padding(horizontal = 10.dp, vertical = 3.dp)) {
                    Text("Berikutnya", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = DiAtasZamrud)
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    s.judul,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Teks,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 24.sp,
                )
                Spacer(Modifier.height(4.dp))
                Text("Episode " + s.episode + "  " + s.format, fontSize = 12.5.sp, color = Redup)
            }
            Column {
                Text(jamTayang(s.waktu), fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Teks)
                Text(hitungMundur(s.waktu, sekarang), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Zamrud)
            }
        }
        if (j != null) {
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Latar.copy(alpha = 0.55f))
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onSuka() },
                contentAlignment = Alignment.Center,
            ) { Ikon("hati", Modifier.size(20.dp), if (suka) MerahLembut else Teks, terisi = suka) }
        }
    }
}

@Composable
private fun BarisRilis(
    s: SlotRilis,
    j: Judul?,
    sekarang: Long,
    suka: Boolean,
    onKlik: () -> Unit,
    onSuka: () -> Unit,
) {
    val sudah = s.waktu * 1000L <= sekarang
    Column(Modifier.graphicsLayer { alpha = if (sudah || j == null) 0.62f else 1f }) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onKlik() }
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(jamTayang(s.waktu), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Teks, modifier = Modifier.width(52.dp))
            Box(
                Modifier
                    .width(52.dp)
                    .height(72.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Kartu),
            ) {
                val gambar = if (j != null) j.poster else s.sampul
                if (gambar.isNotBlank()) Gambar(gambar, Modifier.fillMaxSize())
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    s.judul,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Teks,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 19.sp,
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    "Episode " + s.episode + "  " + s.format +
                        if (j ==
                            null
                        ) {
                            "  Belum di katalog"
                        } else {
                            ""
                        },
                    fontSize = 12.sp,
                    color = Samar,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    hitungMundur(s.waktu, sekarang),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (sudah) Samar else Zamrud,
                )
            }
            if (j != null) {
                Box(
                    Modifier
                        .size(
                            36.dp,
                        ).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onSuka() },
                    contentAlignment = Alignment.Center,
                ) { Ikon("hati", Modifier.size(20.dp), if (suka) MerahLembut else Samar, terisi = suka) }
            }
        }
        Box(
            Modifier
                .padding(start = 84.dp, end = 18.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(Garis.copy(alpha = 0.6f)),
        )
    }
}

@Composable
private fun PilihanSegmen(
    label: String,
    aktif: Boolean,
    onKlik: () -> Unit,
) {
    Box(
        Modifier
            .clip(RoundedCornerShape(50))
            .background(if (aktif) Kartu2 else Color.Transparent)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onKlik() }
            .padding(horizontal = 14.dp, vertical = 7.dp),
    ) {
        Text(label, fontSize = 13.sp, fontWeight = if (aktif) FontWeight.Bold else FontWeight.Medium, color = if (aktif) Teks else Samar)
    }
}

@Composable
fun LayarJadwal(
    favorit: Set<String>,
    onToggleFav: (Judul) -> Unit,
    onDetail: (Judul) -> Unit,
) {
    val lingkupJadwal = rememberCoroutineScope()
    LaunchedEffect(Unit) { JadwalRilis.muat() }
    var hari by rememberSaveable { mutableStateOf(0) }
    var hanyaKatalog by rememberSaveable { mutableStateOf(false) }
    var lihatSudah by rememberSaveable { mutableStateOf(false) }
    var sekarang by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(30_000L)
            sekarang = System.currentTimeMillis()
        }
    }
    val katalog = KatalogSumber.daftar
    val normKatalog = remember(katalog) { katalog.map { normalJudul(it.judul) to it } }
    val slot = JadwalRilis.slot
    val cocok = remember(slot, normKatalog) { slot.map { cariKatalog(it, normKatalog) } }
    val zona = remember { namaZona() }
    val batas = remember(sekarang / 3_600_000L) { List(8) { awalHariLokal(it) } }
    val tiapHari =
        remember(slot, batas) {
            List(7) { i -> slot.indices.filter { slot[it].waktu * 1000L >= batas[i] && slot[it].waktu * 1000L < batas[i + 1] } }
        }
    val singkat = listOf("Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab")
    val lengkap = listOf("Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")
    val kalHari =
        remember(batas) {
            List(7) { i ->
                java.util.Calendar
                    .getInstance()
                    .apply { timeInMillis = batas[i] }
            }
        }

    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(ZamrudGelap.copy(alpha = 0.3f), Color.Transparent), startY = 0f, endY = 900f))
            .statusBarsPadding(),
    ) {
        Row(Modifier.fillMaxWidth().padding(start = 18.dp, end = 18.dp, top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Jadwal Rilis",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp,
                color = Teks,
                modifier = Modifier.weight(1f),
            )
            Text(zona, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Samar)
        }
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                ).clip(RoundedCornerShape(28.dp))
                .background(Kartu.copy(alpha = 0.85f))
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            for (i in 0 until 7) {
                val c = kalHari[i]
                HariMinggu(
                    label = singkat[c.get(java.util.Calendar.DAY_OF_WEEK) - 1],
                    tgl = c.get(java.util.Calendar.DAY_OF_MONTH),
                    ada = tiapHari[i].isNotEmpty(),
                    aktif = hari == i,
                    modifier = Modifier.weight(1f),
                    onKlik = { hari = i },
                )
            }
        }
        Spacer(Modifier.height(18.dp))
        val cH = kalHari[hari]
        val namaHari = lengkap[cH.get(java.util.Calendar.DAY_OF_WEEK) - 1] + ", " + cH.get(java.util.Calendar.DAY_OF_MONTH)
        val diKatalog = tiapHari[hari].count { cocok[it] != null }
        Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(if (hari == 0) "Hari ini" else namaHari, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Teks)
                Text(
                    if (slot.isEmpty()) namaHari else "${tiapHari[hari].size} rilis, $diKatalog di katalog",
                    fontSize = 12.5.sp,
                    color = Samar,
                )
            }
            Row(Modifier.clip(RoundedCornerShape(50)).background(Kartu).padding(3.dp)) {
                PilihanSegmen("Semua", !hanyaKatalog) { hanyaKatalog = false }
                PilihanSegmen("Di katalog", hanyaKatalog) { hanyaKatalog = true }
            }
        }
        Spacer(Modifier.height(14.dp))
        if (slot.isEmpty()) {
            KeadaanMuatAtauGagal(gagal = JadwalRilis.gagal) { lingkupJadwal.launch { JadwalRilis.muat(paksa = true) } }
        } else {
            val tampil = tiapHari[hari].filter { !hanyaKatalog || cocok[it] != null }
            val sudah = tampil.filter { slot[it].waktu * 1000L <= sekarang }
            val akan = tampil.filter { slot[it].waktu * 1000L > sekarang }
            val utama = if (hari == 0) akan.firstOrNull() else null
            val sisa = if (utama != null) akan.drop(1) else akan
            if (tampil.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(top = 48.dp), contentAlignment = Alignment.TopCenter) {
                    KeadaanKosong(
                        "Belum ada rilis",
                        if (hanyaKatalog) "Tidak ada rilis hari ini yang ada di katalog." else "Tidak ada jadwal tayang hari ini.",
                    )
                }
            } else {
                LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 120.dp)) {
                    if (utama != null) {
                        item(key = "utama") {
                            val j = cocok[utama]
                            HeroRilis(slot[utama], j, sekarang, j != null && favorit.contains(j.id), {
                                if (j !=
                                    null
                                ) {
                                    onDetail(j)
                                }
                            }, { if (j != null) onToggleFav(j) })
                        }
                    }
                    if (sisa.isNotEmpty()) {
                        item(key = "h-akan") {
                            JudulBagian(
                                if (utama !=
                                    null
                                ) {
                                    "Selanjutnya"
                                } else {
                                    "Akan tayang"
                                },
                                sisa.size,
                            )
                        }
                    }
                    items(sisa, key = { it }) { idx ->
                        val j = cocok[idx]
                        BarisRilis(slot[idx], j, sekarang, j != null && favorit.contains(j.id), {
                            if (j !=
                                null
                            ) {
                                onDetail(j)
                            }
                        }, { if (j != null) onToggleFav(j) })
                    }
                    if (sudah.isNotEmpty()) {
                        item(key = "h-sudah") {
                            JudulBagian("Sudah tayang", sudah.size, if (lihatSudah) "Sembunyikan" else "Tampilkan") {
                                lihatSudah =
                                    !lihatSudah
                            }
                        }
                    }
                    if (lihatSudah) {
                        items(sudah, key = { it }) { idx ->
                            val j = cocok[idx]
                            BarisRilis(slot[idx], j, sekarang, j != null && favorit.contains(j.id), {
                                if (j !=
                                    null
                                ) {
                                    onDetail(j)
                                }
                            }, { if (j != null) onToggleFav(j) })
                        }
                    }
                }
            }
        }
    }
}

private val HatiFavorit = Zamrud

@Composable
private fun KartuFavorit(
    j: Judul,
    urutan: Int,
    onKlik: () -> Unit,
    onHapus: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sumber =
        remember {
            androidx.compose.foundation.interaction
                .MutableInteractionSource()
        }
    val ditekan by sumber.collectIsPressedAsState()
    val skala by androidx.compose.animation.core
        .animateFloatAsState(if (ditekan) 0.97f else 1f, label = "tekanFav")
    Box(
        modifier
            .munculBertahap(j.id, urutan)
            .graphicsLayer {
                scaleX = skala
                scaleY = skala
            }.aspectRatio(0.74f)
            .clip(RoundedCornerShape(20.dp))
            .background(Kartu)
            .clickable(interactionSource = sumber, indication = null) { onKlik() },
    ) {
        Gambar(j.poster, Modifier.fillMaxSize())
        // scrim bawah supaya judul selalu terbaca di poster terang maupun gelap
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Latar.copy(alpha = 0.92f)))),
        )
        Column(Modifier.align(Alignment.BottomStart).padding(12.dp)) {
            Text(
                rapikanJudul(j.judul),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Teks,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp,
            )
            Spacer(Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Ikon("bintang", Modifier.size(12.dp), BintangW, terisi = true)
                Spacer(Modifier.width(3.dp))
                Text(fmtSkor(j.skor), fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Teks)
                Text("  •  ", fontSize = 12.sp, color = Redup)
                Text(
                    if (j.film) "Film" else "${j.jumlahEpisode} eps",
                    fontSize = 12.sp,
                    color = Redup,
                )
            }
        }
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(34.dp)
                .clip(CircleShape)
                .semantics { contentDescription = "Hapus dari favorit" }
                .clickable { onHapus() },
            contentAlignment = Alignment.Center,
        ) {
            Ikon("markah", Modifier.size(22.dp), Zamrud, terisi = true)
        }
    }
}

@Composable
fun LayarFavorit(
    favorit: Set<String>,
    onDetail: (Judul) -> Unit,
    onHapus: (Judul) -> Unit,
    onJelajah: () -> Unit = {},
) {
    val daftar = (KatalogSumber.daftar + KatalogSumber.terbaru).distinctBy { kunciJudul(it.id) }
    var segmen by rememberSaveable { mutableStateOf(0) }
    var urut by rememberSaveable { mutableStateOf(0) }
    var menuUrut by remember { mutableStateOf(false) }
    val statusList = rememberLazyListState()
    val segmenNama = listOf("Semua", "Serial", "Film")
    val urutNama = listOf("Urutan katalog", "Rating tertinggi", "Judul A–Z")
    val milik = remember(daftar, favorit) { daftar.filter { favorit.contains(it.id) } }
    val jumlah = remember(milik) { listOf(milik.size, milik.count { !it.film }, milik.count { it.film }) }
    val hasil =
        remember(milik, segmen, urut) {
            val disaring =
                when (segmen) {
                    1 -> milik.filter { !it.film }
                    2 -> milik.filter { it.film }
                    else -> milik
                }
            when (urut) {
                1 -> disaring.sortedByDescending { it.skor }
                2 -> disaring.sortedBy { it.judul.lowercase() }
                else -> disaring
            }
        }
    LaunchedEffect(segmen, urut) { statusList.scrollToItem(0) }
    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 14.dp)) {
            Text("Daftar Favorit", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Teks, letterSpacing = (-0.5).sp)
            if (milik.isNotEmpty()) {
                Spacer(Modifier.height(2.dp))
                Text("${milik.size} judul tersimpan", fontSize = 13.sp, color = Redup)
            }
        }
        if (favorit.isNotEmpty() && daftar.isEmpty()) {
            KeadaanMuatAtauGagal(gagal = KatalogSumber.gagal) { KatalogSumber.muatLatar() }
        } else if (milik.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(bottom = 118.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 40.dp)) {
                    Box(
                        Modifier.size(84.dp).clip(CircleShape).background(HatiFavorit.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center,
                    ) { Ikon("markah", Modifier.size(38.dp), HatiFavorit) }
                    Spacer(Modifier.height(20.dp))
                    Text("Belum ada favorit", color = Teks, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Ketuk Favorit di halaman detail untuk menyimpannya di sini.",
                        color = Samar,
                        fontSize = 12.5.sp,
                        lineHeight = 18.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                    Spacer(Modifier.height(22.dp))
                    Button(
                        onClick = onJelajah,
                        colors = ButtonDefaults.buttonColors(containerColor = Zamrud, contentColor = DiAtasZamrud),
                        shape = RoundedCornerShape(14.dp),
                    ) { Text("Jelajahi anime", fontWeight = FontWeight.Bold) }
                }
            }
        } else {
            // kontrol segmen: satu kapsul, segmen aktif berwarna penuh
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Kartu)
                    .border(1.dp, Garis, RoundedCornerShape(16.dp))
                    .padding(4.dp),
            ) {
                segmenNama.forEachIndexed { i, nama ->
                    val aktif = segmen == i
                    val latar by animateColorAsState(
                        if (aktif) Zamrud else Color.Transparent,
                        androidx.compose.animation.core
                            .tween(200),
                        label = "segLatar",
                    )
                    Box(
                        Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(latar)
                            .selectable(selected = aktif, role = Role.Tab) { segmen = i }
                            .padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "$nama  ${jumlah[i]}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (aktif) DiAtasZamrud else Redup,
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp), contentAlignment = Alignment.CenterEnd) {
                Row(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { menuUrut = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Urut: ", fontSize = 12.5.sp, color = Redup)
                    Text(urutNama[urut], fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = Zamrud)
                    Text("  ▾", fontSize = 12.sp, color = Zamrud)
                }
                androidx.compose.material3.DropdownMenu(
                    expanded = menuUrut,
                    onDismissRequest = { menuUrut = false },
                    containerColor = Kartu2,
                ) {
                    urutNama.forEachIndexed { i, nama ->
                        androidx.compose.material3.DropdownMenuItem(
                            text = {
                                Text(
                                    nama,
                                    fontSize = 14.sp,
                                    color = if (i == urut) Zamrud else Teks,
                                    fontWeight = if (i == urut) FontWeight.SemiBold else FontWeight.Normal,
                                )
                            },
                            onClick = {
                                urut = i
                                menuUrut = false
                            },
                        )
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
            if (hasil.isEmpty()) {
                Box(Modifier.fillMaxSize().padding(bottom = 118.dp), contentAlignment = Alignment.Center) {
                    KeadaanKosong(
                        if (segmen == 1) "Belum ada serial" else "Belum ada film",
                        "Ketuk Favorit di halaman detail untuk menyimpannya di sini.",
                    )
                }
            } else {
                LazyColumn(
                    Modifier.fillMaxSize(),
                    state = statusList,
                    contentPadding =
                        androidx.compose.foundation.layout.PaddingValues(
                            start = 20.dp,
                            end = 20.dp,
                            top = 6.dp,
                            bottom = 130.dp,
                        ),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    items(hasil.chunked(2), key = { baris -> baris.joinToString(",") { it.id } }) { baris ->
                        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            baris.forEachIndexed { k, j ->
                                KartuFavorit(
                                    j,
                                    urutan = k,
                                    onKlik = { onDetail(j) },
                                    onHapus = { onHapus(j) },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                            if (baris.size < 2) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

// ================= Unduhan =================
@Composable
fun LayarUnduhan(onPutar: (Judul, Int) -> Unit = { _, _ -> }) {
    val ctx = LocalContext.current
    var segarUn by remember { mutableStateOf(0) }
    var hapusU by remember { mutableStateOf<Unduhan?>(null) }
    val daftarUn = remember(segarUn) { Pengunduh.daftar(ctx) }
    val info =
        remember(segarUn) {
            try {
                val sf =
                    StatFs(
                        android.os.Environment
                            .getExternalStorageDirectory()
                            .path,
                    )
                val total = sf.totalBytes
                val bebas = sf.availableBytes
                val terpakai = total - bebas

                fun gb(b: Long) = "%.1f".format(b / 1_000_000_000.0).replace('.', ',')
                Triple(
                    "${gb(terpakai)} GB dari ${gb(total)} GB",
                    if (total >
                        0
                    ) {
                        terpakai.toFloat() / total
                    } else {
                        0f
                    },
                    "Penyimpanan perangkat",
                )
            } catch (e: Exception) {
                Triple("–", 0f, "Penyimpanan perangkat")
            }
        }
    Column(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 100.dp),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Text("Unduhan", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = Teks, modifier = Modifier.weight(1f))
            if (daftarUn.isNotEmpty()) {
                Text(
                    "${daftarUn.size} episode",
                    fontSize = 13.sp,
                    color = Samar,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Kartu)
                .padding(18.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(46.dp).clip(CircleShape).background(Zamrud),
                    contentAlignment = Alignment.Center,
                ) { Ikon("unduhan", Modifier.size(22.dp), DiAtasZamrud) }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(info.first, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Teks)
                    Text(info.third, fontSize = 12.sp, color = Samar)
                }
            }
            Spacer(Modifier.height(14.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Garis),
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(info.second.coerceIn(0f, 1f))
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Zamrud),
                )
            }
        }
        Spacer(Modifier.height(22.dp))
        if (daftarUn.isNotEmpty()) {
            Text("Sudah diunduh", fontSize = 13.sp, color = Redup, modifier = Modifier.padding(start = 20.dp, end = 18.dp, bottom = 6.dp))
            daftarUn.forEach { u ->
                val jj =
                    (KatalogSumber.daftar + KatalogSumber.terbaru)
                        .firstOrNull { it.id == u.idJudul }
                        ?.let { if (it.episodes.isEmpty()) it.copy(episodes = listOf(Episode(u.nomorEp, null))) else it }
                        ?: Judul(
                            u.idJudul,
                            rapikanJudul(u.judul),
                            u.poster,
                            u.poster,
                            "",
                            0,
                            0.0,
                            "",
                            emptyList(),
                            null,
                            null,
                            listOf(Episode(u.nomorEp, null)),
                        )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 5.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Kartu)
                        .clickable { onPutar(jj, u.indeksEp) }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.width(52.dp).height(72.dp).clip(RoundedCornerShape(10.dp))) {
                        Gambar(u.poster, Modifier.fillMaxSize())
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            rapikanJudul(u.judul),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Teks,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Spacer(Modifier.height(3.dp))
                        Text("Episode ${u.nomorEp} • ${fmtUkuran(u.ukuran)}", fontSize = 12.sp, color = Samar)
                    }
                    Box(
                        Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .border(1.dp, Garis, CircleShape)
                            .clickable { hapusU = u },
                        contentAlignment = Alignment.Center,
                    ) { Ikon("sampah", Modifier.size(18.dp), MerahLembut) }
                }
            }
        } else {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Kartu)
                            .border(1.dp, Garis, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) { Ikon("unduhan", Modifier.size(32.dp), Samar) }
                    Spacer(Modifier.height(16.dp))
                    Text("Belum ada unduhan", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Teks)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Episode yang kamu unduh akan muncul di sini dan bisa ditonton tanpa internet.",
                        fontSize = 12.5.sp,
                        color = Samar,
                        lineHeight = 18.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
            }
        }
        Spacer(Modifier.height(118.dp))
        // Konfirmasi hapus: satu ketukan tidak lagi langsung menghapus berkas
        hapusU?.let { u ->
            androidx.compose.ui.window.Dialog(onDismissRequest = { hapusU = null }) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(28.dp))
                        .background(Kartu)
                        .border(1.dp, Garis, RoundedCornerShape(28.dp))
                        .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        Modifier.size(54.dp).clip(CircleShape).background(MerahLembut.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center,
                    ) { Ikon("sampah", Modifier.size(26.dp), MerahLembut) }
                    Spacer(Modifier.height(14.dp))
                    Text("Hapus Episode ${u.nomorEp}?", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Teks)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Berkas akan dihapus dari perangkat. Kamu bisa mengunduhnya lagi nanti.",
                        fontSize = 13.5.sp,
                        lineHeight = 20.sp,
                        color = Redup,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Kartu2)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(Modifier.width(36.dp).height(50.dp).clip(RoundedCornerShape(8.dp))) {
                            Gambar(u.poster, Modifier.fillMaxSize())
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                rapikanJudul(u.judul),
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Teks,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Spacer(Modifier.height(3.dp))
                            Text("Episode ${u.nomorEp} • ${fmtUkuran(u.ukuran)}", fontSize = 12.sp, color = Samar)
                        }
                    }
                    Spacer(Modifier.height(18.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .background(Kartu2)
                                .border(1.dp, Garis, CircleShape)
                                .clickable { hapusU = null }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center,
                        ) { Text("Batal", fontSize = 14.5.sp, fontWeight = FontWeight.SemiBold, color = Teks) }
                        Box(
                            Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .background(MerahLembut)
                                .clickable {
                                    Pengunduh.hapus(ctx, u)
                                    hapusU = null
                                    segarUn++
                                }.padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center,
                        ) { Text("Hapus", fontSize = 14.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A0F0F)) }
                    }
                }
            }
        }
    }
}

// ================= Akun =================
@Composable
@UnstableApi
fun LayarAkun(
    akun: AkunGoogle?,
    onMasukBerhasil: (AkunGoogle) -> Unit,
    onKeluar: () -> Unit,
    onHalamanBuka: (Boolean) -> Unit = {},
) {
    val ctx = LocalContext.current
    val lingkup = rememberCoroutineScope()
    var sibuk by remember { mutableStateOf(false) }
    var pesan by remember { mutableStateOf<String?>(null) }
    val masuk = akun != null

    fun mulaiMasuk() {
        if (sibuk) return
        sibuk = true
        pesan = null
        lingkup.launch {
            try {
                val cm = CredentialManager.create(ctx)
                // Opsi "Masuk dengan Google" khusus untuk tombol: tetap berjalan walau belum ada akun
                // Google yang pernah dipakai di HP ini (pengguna bisa menambah akun dari sheet-nya).
                val opsi = GetSignInWithGoogleOption.Builder(ID_KLIEN_WEB).build()
                val req = GetCredentialRequest.Builder().addCredentialOption(opsi).build()
                val hasil = cm.getCredential(ctx, req)
                val k = GoogleIdTokenCredential.createFrom(hasil.credential.data)
                onMasukBerhasil(AkunGoogle(k.displayName ?: "", k.id, k.profilePictureUri?.toString() ?: ""))
                SinkronAkun.masukFirebase(ctx, k.idToken)
            } catch (e: GetCredentialCancellationException) {
                // pengguna membatalkan pilihan akun — tidak perlu pesan
            } catch (e: NoCredentialException) {
                pesan = "Tidak ada akun Google yang bisa dipakai di HP ini."
            } catch (e: GetCredentialException) {
                pesan = "Gagal masuk. Coba lagi ya."
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                pesan = "Gagal masuk. Coba lagi ya."
            } finally {
                sibuk = false
            }
        }
    }

    fun keluar() {
        lingkup.launch {
            try {
                CredentialManager.create(ctx).clearCredentialState(ClearCredentialStateRequest())
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                // abaikan — data lokal tetap dihapus oleh pemanggil
            }
            onKeluar()
        }
    }

    // Bersihkan cache gambar + cache video sementara. Unduhan offline TIDAK ikut terhapus.
    fun bersihkanCache() {
        lingkup.launch {
            val terhapus =
                withContext(Dispatchers.IO) {
                    var total = 0L
                    try {
                        val pemuat = SingletonImageLoader.get(ctx)
                        total += pemuat.diskCache?.size ?: 0L
                        pemuat.memoryCache?.clear()
                        pemuat.diskCache?.clear()
                    } catch (e: Exception) {
                    }
                    try {
                        val video = CachePemutar.ambil(ctx)
                        total += video.cacheSpace
                        for (k in video.keys.toList()) video.removeResource(k)
                    } catch (e: Exception) {
                    }
                    total
                }
            val mb = terhapus / (1024.0 * 1024.0)
            android.widget.Toast
                .makeText(
                    ctx,
                    "Cache dibersihkan (" + String.format(java.util.Locale.US, "%.1f", mb) + " MB)",
                    android.widget.Toast.LENGTH_SHORT,
                ).show()
        }
    }

    // Halaman turunan menu Akun (Tentang, Bantuan, Pengaturan). Pengaturan masih halaman
    // kosong; isinya menyusul.
    var halaman by rememberSaveable { mutableStateOf<String?>(null) }
    // Beri tahu induk supaya navbar bawah disembunyikan selama halaman terbuka
    LaunchedEffect(halaman) { onHalamanBuka(halaman != null) }
    DisposableEffect(Unit) { onDispose { onHalamanBuka(false) } }

    val a = akun
    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp),
        ) {
            Text(
                "Akun",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = Teks,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            )
            Spacer(Modifier.height(8.dp))
            // Kartu profil: susunan yang sama untuk kondisi masuk maupun belum masuk
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Kartu)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (masuk && a != null && a.foto.isNotBlank()) {
                    Gambar(a.foto, Modifier.size(56.dp).clip(CircleShape))
                } else if (masuk && a != null) {
                    Box(
                        Modifier.size(56.dp).clip(CircleShape).background(Zamrud),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            (a.nama.firstOrNull() ?: a.email.firstOrNull() ?: 'A').uppercase(),
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold,
                            color = DiAtasZamrud,
                        )
                    }
                } else {
                    Box(
                        Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Kartu2)
                            .border(1.dp, Garis, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) { Ikon("akun", Modifier.size(27.dp), Samar) }
                }
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    if (masuk && a != null) {
                        Text(
                            a.nama.ifBlank { a.email.substringBefore('@') },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Teks,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            a.email,
                            fontSize = 13.sp,
                            color = Redup,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    } else {
                        Text("Belum masuk", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Teks)
                    }
                }
            }
            if (!masuk) {
                Spacer(Modifier.height(12.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .clickable(enabled = !sibuk) { mulaiMasuk() }
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (sibuk) {
                        androidx.compose.material3.LoadingIndicator(color = Zamrud, modifier = Modifier.size(26.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("Menghubungi Google…", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1F1F1F))
                    } else {
                        LogoGoogle(Modifier.size(20.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("Masuk dengan Google", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1F1F1F))
                    }
                }
                val p = pesan
                if (p != null) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        p,
                        fontSize = 13.sp,
                        color = MerahLembut,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            // Kartu tawaran Premium
            val bentukPremium = RoundedCornerShape(22.dp)
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(bentukPremium)
                    .background(Brush.linearGradient(listOf(Kartu2, Kartu)))
                    .border(
                        1.dp,
                        Brush.linearGradient(listOf(Aksen.copy(alpha = 0.7f), Garis, Zamrud.copy(alpha = 0.4f))),
                        bentukPremium,
                    ).clickable { halaman = "Premium" },
            ) {
                // cahaya emas lembut di pojok kanan atas
                Box(
                    Modifier
                        .matchParentSize()
                        .background(
                            Brush.radialGradient(
                                listOf(Aksen.copy(alpha = 0.20f), Color.Transparent),
                                center = Offset(Float.POSITIVE_INFINITY, 0f),
                                radius = 520f,
                            ),
                        ),
                )
                Column(Modifier.fillMaxWidth().padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Brush.linearGradient(listOf(Aksen, AksenGelap))),
                            contentAlignment = Alignment.Center,
                        ) { Ikon("mahkota", Modifier.size(27.dp), DiAtasAksen) }
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Premium", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Teks, maxLines = 1)
                            Spacer(Modifier.height(2.dp))
                            Text("Nonton tanpa iklan", fontSize = 13.sp, color = Redup, maxLines = 1)
                        }
                        Spacer(Modifier.width(10.dp))
                        Text(
                            "Langganan",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DiAtasAksen,
                            modifier =
                                Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Brush.linearGradient(listOf(Aksen, AksenGelap)))
                                    .padding(horizontal = 18.dp, vertical = 9.dp),
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            JudulMenu("Dukungan")
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Kartu),
            ) {
                BarisMenu("cari", "Tentang") { halaman = "Tentang" }
                GarisMenu()
                BarisMenu("kirim", "Bantuan") { halaman = "Bantuan" }
            }
            if (masuk) {
                Spacer(Modifier.height(28.dp))
                Text(
                    "Keluar",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MerahLembut,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Kartu)
                            .clickable { keluar() }
                            .padding(vertical = 16.dp),
                )
            }
            // Pengaturan selalu paling bawah
            Spacer(Modifier.height(20.dp))
            JudulMenu("Lainnya")
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Kartu),
            ) {
                BarisMenu("sampah", "Cache") { bersihkanCache() }
                GarisMenu()
                BarisMenu("geser", "Pengaturan") { halaman = "Pengaturan" }
            }
        }
        val h = halaman
        if (h != null) {
            when (h) {
                "Bantuan" -> HalamanBantuan(onKembali = { halaman = null })
                "Tentang" -> HalamanTentang(onKembali = { halaman = null })
                "Premium" -> HalamanPremium(onKembali = { halaman = null })
                else -> HalamanKosong(h, onKembali = { halaman = null })
            }
        }
    }
}

// Bilah atas halaman turunan menu Akun: tombol kembali + judul.
@Composable
fun BilahHalaman(
    judul: String,
    onKembali: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(40.dp).clip(CircleShape).clickable { onKembali() },
            contentAlignment = Alignment.Center,
        ) { Ikon("kembali", Modifier.size(21.dp), Redup) }
        Spacer(Modifier.width(6.dp))
        Text(judul, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Teks)
    }
}

// Halaman kosong untuk menu Akun yang isinya belum dibuat (Notifikasi, Pengaturan).
@Composable
private fun HalamanKosong(
    judul: String,
    onKembali: () -> Unit,
) {
    BackHandler { onKembali() }
    Column(
        Modifier
            .fillMaxSize()
            .zIndex(2f)
            .background(Latar)
            .statusBarsPadding(),
    ) {
        BilahHalaman(judul, onKembali)
        Box(Modifier.fillMaxSize())
    }
}

// ================= Bantuan =================
// Isi alamat kontak di sini. Baris yang alamatnya masih kosong menampilkan "Segera hadir".
private const val EMAIL_BANTUAN = ""
private const val LINK_TELEGRAM = ""

private val DaftarFaq =
    listOf(
        "Video tidak mau diputar" to
            "Periksa koneksi internetmu, lalu tutup dan buka lagi episodenya. Jika masih gagal, coba lagi beberapa menit kemudian karena sumber video kadang sedang gangguan.",
        "Notifikasi tidak masuk" to
            "Pastikan izin notifikasi untuk Anime.id aktif di pengaturan HP, dan aplikasi tidak dibatasi oleh mode hemat baterai.",
        "Cara mengunduh episode" to
            "Buka halaman detail anime, lalu ketuk ikon unduh pada episode yang diinginkan. Hasilnya muncul di tab Unduhan.",
    )

@Composable
private fun HalamanBantuan(onKembali: () -> Unit) {
    val ctx = LocalContext.current
    BackHandler { onKembali() }
    var terbuka by rememberSaveable { mutableStateOf(-1) }

    fun segera() {
        android.widget.Toast
            .makeText(ctx, "Segera hadir", android.widget.Toast.LENGTH_SHORT)
            .show()
    }

    fun bukaEmail(
        subjek: String,
        isi: String,
    ) {
        if (EMAIL_BANTUAN.isBlank()) {
            segera()
            return
        }
        try {
            val i =
                android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
                    data = android.net.Uri.parse("mailto:")
                    putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf(EMAIL_BANTUAN))
                    putExtra(android.content.Intent.EXTRA_SUBJECT, subjek)
                    putExtra(android.content.Intent.EXTRA_TEXT, isi)
                }
            ctx.startActivity(i)
        } catch (e: Exception) {
            android.widget.Toast
                .makeText(ctx, "Tidak ada aplikasi email di HP ini.", android.widget.Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun bukaTelegram() {
        if (LINK_TELEGRAM.isBlank()) {
            segera()
            return
        }
        try {
            ctx.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(LINK_TELEGRAM)))
        } catch (e: Exception) {
            segera()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .zIndex(2f)
            .background(Latar)
            .statusBarsPadding(),
    ) {
        BilahHalaman("Bantuan", onKembali)
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 32.dp)) {
            Spacer(Modifier.height(8.dp))
            JudulMenu("Pertanyaan umum")
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Kartu),
            ) {
                DaftarFaq.forEachIndexed { idx, (tanya, jawab) ->
                    if (idx > 0) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp)
                                .height(1.dp)
                                .background(Garis),
                        )
                    }
                    val buka = terbuka == idx
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clickable { terbuka = if (buka) -1 else idx }
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(tanya, fontSize = 15.sp, color = Teks, modifier = Modifier.weight(1f))
                            Spacer(Modifier.width(12.dp))
                            Ikon("panah_bawah", Modifier.size(22.dp).rotate(if (buka) 180f else 0f), Redup)
                        }
                        if (buka) {
                            Spacer(Modifier.height(8.dp))
                            Text(jawab, fontSize = 13.sp, lineHeight = 20.sp, color = Redup)
                        }
                    }
                }
            }
            Spacer(Modifier.height(22.dp))
            JudulMenu("Hubungi kami")
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Kartu),
            ) {
                BarisMenu("surat", "Email") { bukaEmail("Bantuan Anime.id", "") }
                GarisMenu()
                BarisMenu("kirim", "Telegram") { bukaTelegram() }
                GarisMenu()
                BarisMenu("peringatan", "Laporkan masalah") {
                    val versi =
                        try {
                            ctx.packageManager.getPackageInfo(ctx.packageName, 0).versionName ?: "-"
                        } catch (
                            e: Exception,
                        ) {
                            "-"
                        }
                    bukaEmail(
                        "Laporan masalah Anime.id",
                        "\n\n---\nVersi aplikasi: $versi\nAndroid: ${android.os.Build.VERSION.RELEASE}\nPerangkat: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}",
                    )
                }
            }
        }
    }
}

// ================= Tentang =================
private val DaftarPustaka =
    listOf(
        "Jetpack Compose",
        "Media3 ExoPlayer",
        "Coil",
        "Haze",
        "Firebase",
        "Kotlinx Serialization",
        "WorkManager",
    )

@Composable
private fun HalamanTentang(onKembali: () -> Unit) {
    val ctx = LocalContext.current
    BackHandler { onKembali() }
    val versi =
        remember {
            try {
                ctx.packageManager.getPackageInfo(ctx.packageName, 0).versionName ?: "-"
            } catch (e: Exception) {
                "-"
            }
        }
    var dialogPustaka by remember { mutableStateOf(false) }
    var dialogSumber by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .zIndex(2f)
            .background(Latar)
            .statusBarsPadding(),
    ) {
        BilahHalaman("Tentang", onKembali)
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 32.dp)) {
            Spacer(Modifier.height(8.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Kartu)
                    .padding(vertical = 26.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row {
                    Text("Anime", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Teks)
                    Text(".id", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Zamrud)
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "Versi $versi",
                    fontSize = 13.sp,
                    color = Redup,
                    modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Latar).padding(horizontal = 14.dp, vertical = 5.dp),
                )
            }
            Spacer(Modifier.height(22.dp))
            JudulMenu("Informasi")
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Kartu),
            ) {
                BarisMenu("bintang", "Sumber data") { dialogSumber = true }
                GarisMenu()
                BarisMenu("markah", "Pustaka yang dipakai") { dialogPustaka = true }
            }
            Spacer(Modifier.height(20.dp))
            Text(
                "© 2026 Anime.id",
                fontSize = 13.sp,
                color = Redup,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
    if (dialogSumber) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { dialogSumber = false },
            containerColor = Kartu,
            title = { Text("Sumber data", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Teks) },
            text = {
                Column {
                    listOf(
                        "Katalog judul dan episode" to "Anime.id",
                        "Poster dan banner" to "Anime.id",
                        "Jadwal rilis" to "AniList",
                    ).forEachIndexed { i, (label, nilai) ->
                        if (i > 0) Spacer(Modifier.height(14.dp))
                        Text(label, fontSize = 13.sp, color = Samar)
                        Spacer(Modifier.height(2.dp))
                        Text(nilai, fontSize = 15.sp, color = Teks)
                    }
                }
            },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = { dialogSumber = false }) {
                    Text("Tutup", color = Zamrud)
                }
            },
        )
    }
    if (dialogPustaka) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { dialogPustaka = false },
            containerColor = Kartu,
            title = { Text("Pustaka yang dipakai", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Teks) },
            text = {
                Column {
                    DaftarPustaka.forEach {
                        Text(it, fontSize = 15.sp, color = Redup, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = { dialogPustaka = false }) {
                    Text("Tutup", color = Zamrud)
                }
            },
        )
    }
}

// Logo "G" Google asli (4 warna) digambar dengan Canvas, bukan huruf biasa.
@Composable
private fun LogoGoogle(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val tebal = size.width * 0.22f
        val pad = tebal / 2f
        val ukuran = Size(size.width - tebal, size.height - tebal)
        val awal = Offset(pad, pad)
        val st = Stroke(width = tebal)
        drawArc(Color(0xFFEA4335), 215f, 100f, false, awal, ukuran, style = st)
        drawArc(Color(0xFFFBBC05), 135f, 80f, false, awal, ukuran, style = st)
        drawArc(Color(0xFF34A853), 45f, 90f, false, awal, ukuran, style = st)
        drawArc(Color(0xFF4285F4), 0f, 45f, false, awal, ukuran, style = st)
        drawRect(
            Color(0xFF4285F4),
            topLeft = Offset(size.width * 0.5f, size.height / 2f - tebal / 2f),
            size = Size(size.width * 0.5f - pad + tebal / 2f, tebal),
        )
    }
}

@Composable
fun JudulMenu(teks: String) {
    Text(
        teks,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = Samar,
        modifier = Modifier.padding(start = 26.dp, bottom = 8.dp),
    )
}

@Composable
private fun BarisMenu(
    ikon: String,
    teks: String,
    onKlik: () -> Unit = {},
) {
    Row(
        Modifier.fillMaxWidth().clickable { onKlik() }.padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Ikon(ikon, Modifier.size(21.dp), Redup)
        Spacer(Modifier.width(16.dp))
        Text(teks, fontSize = 15.sp, color = Teks, modifier = Modifier.weight(1f))
        Text("›", fontSize = 22.sp, color = Samar)
    }
}

// ===== Jaringan cepat: balapan cermin + ETag =====
private const val TIDAK_BERUBAH = "\u0000304"

private class HasilAmbil(
    val url: String,
    val teks: String,
    val etag: String?,
)

// Pelaksana balapan: lepas dari lingkup pemanggil supaya pemanggil tidak ikut
// menunggu cermin yang lambat (HttpURLConnection tidak bisa dibatalkan).
private val lingkupBalap = CoroutineScope(SupervisorJob() + Dispatchers.IO)

private fun sidecarEtag(f: java.io.File?) = f?.let { java.io.File(it.path + ".etag") }

// (alamat, etag) hanya bila salinan di disk masih ada — 304 tanpa salinan tak berguna.
private fun bacaEtag(f: java.io.File?): Pair<String, String>? =
    try {
        if (f == null || !f.exists()) {
            null
        } else {
            val b = sidecarEtag(f)?.takeIf { it.exists() }?.readLines()
            if (b != null && b.size >= 2 && b[1].isNotBlank()) b[0] to b[1] else null
        }
    } catch (e: Exception) {
        null
    }

private fun tulisEtag(
    f: java.io.File?,
    url: String?,
    etag: String?,
) {
    try {
        val s = sidecarEtag(f) ?: return
        if (url != null && !etag.isNullOrBlank()) s.writeText(url + "\n" + etag) else s.delete()
    } catch (e: Exception) {
    }
}

private fun hapusSalinan(f: java.io.File?) {
    try {
        f?.delete()
        sidecarEtag(f)?.delete()
    } catch (e: Exception) {
    }
}

private fun ambilTeksCond(
    url: String,
    etag: String?,
): Pair<String?, String?> {
    var conn: HttpURLConnection? = null
    return try {
        conn = URL(url).openConnection() as HttpURLConnection
        conn.connectTimeout = 8000
        conn.readTimeout = 15000
        conn.setRequestProperty("User-Agent", "AnimeId/1.1")
        if (etag != null) conn.setRequestProperty("If-None-Match", etag)
        val kode = conn.responseCode
        when {
            kode == 304 -> TIDAK_BERUBAH to etag
            kode in 200..299 -> conn.inputStream.bufferedReader().readText() to conn.getHeaderField("ETag")
            else -> null to null
        }
    } catch (e: Exception) {
        null to null
    } finally {
        conn?.disconnect()
    }
}

// Minta semua alamat bersamaan, kembalikan yang PERTAMA sah (lolos `sah` atau 304).
// etag hanya dikirim ke alamat yang menghasilkan salinan di disk.
private suspend fun balapTeks(
    urls: List<String>,
    etagUrl: String?,
    etag: String?,
    sah: (String) -> Boolean,
): HasilAmbil? {
    if (urls.isEmpty()) return null
    val kanal = kotlinx.coroutines.channels.Channel<HasilAmbil?>(urls.size)
    val tugas =
        urls.map { u ->
            lingkupBalap.launch {
                // Hasil SELALU dikirim (null bila gagal), bahkan bila yang terlempar Error
                // seperti OutOfMemoryError. Tanpa ini receive() di bawah menunggu selamanya,
                // bendera `jalan` di KatalogSumber tak pernah turun, dan katalog tak bisa
                // dimuat ulang lagi sampai aplikasi dimatikan.
                var hasil: HasilAmbil? = null
                try {
                    val (b, e) = ambilTeksCond(u, if (u == etagUrl) etag else null)
                    if (b != null && (b == TIDAK_BERUBAH || sah(b))) hasil = HasilAmbil(u, b, e)
                } catch (t: Throwable) {
                    hasil = null
                } finally {
                    kanal.trySend(hasil)
                }
            }
        }
    var sisa = urls.size
    var menang: HasilAmbil? = null
    while (sisa > 0 && menang == null) {
        menang = kanal.receive()
        sisa--
    }
    tugas.forEach { it.cancel() }
    kanal.close()
    return menang
}

private fun ambilTeksUrl(url: String): String? {
    // Dua kali coba: satu paket hilang / sambungan putus sesaat tidak boleh membuat
    // daftar episode atau playlist unduhan langsung dianggap gagal. Kode 4xx tidak
    // diulang (alamatnya memang tidak ada).
    for (percobaan in 1..2) {
        var conn: HttpURLConnection? = null
        var ulang = true
        try {
            conn = URL(url).openConnection() as HttpURLConnection
            conn.connectTimeout = 12000
            conn.readTimeout = 15000
            conn.setRequestProperty("User-Agent", "AnimeId/1.1")
            val kode = conn.responseCode
            if (kode in 200..299) return conn.inputStream.bufferedReader().readText()
            if (kode in 400..499 && kode != 408 && kode != 429) ulang = false
        } catch (e: Exception) {
            // gangguan jaringan: coba lagi
        } finally {
            conn?.disconnect()
        }
        if (!ulang || percobaan == 2) break
        try {
            Thread.sleep(700L)
        } catch (x: InterruptedException) {
            break
        }
    }
    return null
}

@Composable
private fun GarisMenu() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(start = 55.dp, end = 18.dp)
            .height(1.dp)
            .background(Garis),
    )
}

// ================= Detail =================
// Warna aksen halaman detail harus GELAP: Palette bisa mengembalikan warna dominan yang terang
// (banner bernuansa putih/salju), dan itu membuat latar gradien + bagian bawah banner
// berpendar putih saat warnanya beranimasi masuk.
private fun gelapkanAksen(rgb: Int): Int {
    val hsl = FloatArray(3)
    androidx.core.graphics.ColorUtils
        .colorToHSL(rgb, hsl)
    hsl[1] = hsl[1].coerceAtMost(0.6f)
    hsl[2] = hsl[2].coerceAtMost(0.22f)
    return androidx.core.graphics.ColorUtils
        .HSLToColor(hsl)
}

@Composable
@UnstableApi
fun LayarDetail(
    j: Judul,
    favorit: Boolean,
    onToggleFav: () -> Unit,
    onKembali: () -> Unit,
    onPutar: (Int) -> Unit,
    // true selama berkas judul (daftar episode) sedang diambil dari jaringan
    memuatEpisode: Boolean = false,
    onCobaLagiEpisode: () -> Unit = {},
) {
    val ctx = LocalContext.current
    // keadaan per judul: tidak terbawa bila halaman detail berpindah ke judul lain
    var pesan by remember(j.id) { mutableStateOf<String?>(null) }
    var sinopsisPenuh by remember(j.id) { mutableStateOf(false) }
    var sinopsisTerpotong by remember(j.id) { mutableStateOf(false) }
    // pesan status hilang sendiri setelah beberapa detik
    LaunchedEffect(pesan) {
        if (pesan != null) {
            delay(3500)
            pesan = null
        }
    }
    // daftar unduhan dibaca sekali (bukan per baris episode), segar lagi tiap unduhan selesai
    val unduhanTersimpan = remember(Pengunduh.versi) { Pengunduh.daftar(ctx).map { it.kunci }.toSet() }
    // episode pertama yang punya sumber putar (lewati episode rusak)
    val idxMulai = remember(j.episodes) { j.episodes.indexOfFirst { it.playlist != null }.let { if (it < 0) 0 else it } }
    // Banner dikunci selama halaman ini terbuka. Saat berkas judul selesai dimuat, `j` diganti
    // dengan versi lengkap yang alamat banner/fokusnya bisa beda dari entri indeks; kalau
    // dipakai langsung, Gambar() mereset fade-nya (gambar hilang lalu muncul lagi) dan
    // gambar terang itu terlihat berkedip. Hanya diperbarui bila tadinya kosong.
    val bannerAsli = remember(j.id, j.banner.isBlank()) { j.banner }
    // Judul yang memang tak punya banner (setelah berkas judul selesai dimuat) memakai poster,
    // bukan placeholder berkilau selamanya. Selama masih memuat tetap kosong supaya tidak
    // berkedip dari poster ke banner.
    val bannerTampil = if (bannerAsli.isBlank() && !memuatEpisode) j.poster else bannerAsli
    val fokusTampil = remember(j.id, j.banner.isBlank()) { j.fokus }
    // Pemanasan awal: begitu halaman detail terbuka (Wi-Fi saja), playlist dan segmen pertama
    // episode yang akan diputar sudah masuk cache, jadi video mulai hampir instan saat diputar.
    LaunchedEffect(j.id, j.episodes.size, idxMulai) {
        val epMulai = j.episodes.getOrNull(idxMulai) ?: return@LaunchedEffect
        val url = epMulai.playlist ?: return@LaunchedEffect
        if (!url.startsWith("http")) return@LaunchedEffect
        if (Pengunduh.kunci(j.id, epMulai.number) in unduhanTersimpan) return@LaunchedEffect
        if (!PemanasanVideo.jaringanAman(ctx)) return@LaunchedEffect
        delay(800) // jangan bersaing dengan pemuatan banner
        withContext(Dispatchers.IO) { PemanasanVideo.panaskan(ctx, url) }
    }
    var pengingat by remember { mutableStateOf(bacaPengingat(ctx)) }
    val segeraTayang = j.episodes.isEmpty() && labelStatus(j.status) == "Segera Tayang"
    BackHandler { onKembali() }
    // Warna dinamis: ambil warna gelap dominan dari banner begitu gambarnya termuat
    var aksen by remember(j.id) { mutableStateOf<Color?>(null) }
    LaunchedEffect(bannerTampil) {
        if (bannerTampil.isBlank()) return@LaunchedEffect
        val hasil =
            SingletonImageLoader.get(ctx).execute(
                ImageRequest
                    .Builder(ctx)
                    .data(bannerTampil)
                    .size(400)
                    .httpHeaders(NetworkHeaders.Builder().add("User-Agent", "Anime.id Android").build())
                    .build(),
            )
        val bmpMentah = (hasil as? SuccessResult)?.image?.toBitmap()
        // Palette tidak bisa membaca bitmap hardware — salin ke perangkat lunak dulu
        val bmp =
            if (bmpMentah != null && bmpMentah.config == Bitmap.Config.HARDWARE) {
                bmpMentah.copy(Bitmap.Config.ARGB_8888, false)
            } else {
                bmpMentah
            }
        if (bmp != null) {
            val rgb =
                withContext(Dispatchers.Default) {
                    try {
                        val pal =
                            androidx.palette.graphics.Palette
                                .from(bmp)
                                .maximumColorCount(16)
                                .generate()
                        (pal.darkVibrantSwatch ?: pal.darkMutedSwatch ?: pal.dominantSwatch)?.rgb
                    } catch (e: Exception) {
                        null
                    }
                }
            if (rgb != null) aksen = Color(gelapkanAksen(rgb))
        }
    }
    val aksenAnim by animateColorAsState(
        aksen ?: Latar,
        androidx.compose.animation.core
            .tween(700),
        label = "aksen",
    )
    Box(Modifier.fillMaxSize().background(Latar)) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(520.dp)
                .background(Brush.verticalGradient(listOf(aksenAnim.copy(alpha = 0.55f), Latar))),
        )
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Box(Modifier.fillMaxWidth().height(252.dp)) {
                    Gambar(bannerTampil, Modifier.fillMaxSize(), fokusX = fokusTampil)
                    Box(
                        Modifier.fillMaxSize().background(
                            Brush.verticalGradient(
                                listOf(
                                    Latar.copy(alpha = 0x59 / 255f),
                                    Color.Transparent,
                                    aksenAnim.copy(alpha = 0.283f).compositeOver(Latar),
                                ),
                                startY = 0f,
                            ),
                        ),
                    )
                    Row(Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 14.dp, vertical = 8.dp)) {
                        Box(
                            Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Latar.copy(alpha = 0xB3 / 255f))
                                .clickable { onKembali() },
                            contentAlignment = Alignment.Center,
                        ) { Ikon("kembali", Modifier.size(20.dp), Teks) }
                        Spacer(Modifier.weight(1f))
                        Box(
                            Modifier.size(40.dp).clip(CircleShape).background(Latar.copy(alpha = 0xB3 / 255f)).clickable {
                                val kirim =
                                    Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, "Nonton ${j.judul} di Anime.id")
                                    }
                                ctx.startActivity(Intent.createChooser(kirim, "Bagikan"))
                            },
                            contentAlignment = Alignment.Center,
                        ) { Ikon("bagikan", Modifier.size(19.dp), Teks) }
                    }
                }
            }
            item {
                Column(Modifier.padding(horizontal = 18.dp)) {
                    Text(j.judul, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Teks, lineHeight = 30.sp)
                    Spacer(Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Ikon("bintang", Modifier.size(16.dp), BintangW, terisi = true)
                        Spacer(Modifier.width(5.dp))
                        Text(fmtSkor(j.skor), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Teks)
                        Text(
                            "  •  ${if (j.tahun > 0) j.tahun.toString() else "–"}" +
                                (if (j.jumlahEpisode > 0) "  •  ${j.jumlahEpisode} Episode" else ""),
                            fontSize = 13.5.sp,
                            color = Redup,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false),
                        )
                        Text("  •  ", fontSize = 13.5.sp, color = Redup)
                        Box(
                            Modifier
                                .clip(
                                    RoundedCornerShape(9.dp),
                                ).background(Zamrud.copy(alpha = 0.16f))
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                        ) {
                            Text(labelStatus(j.status), fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Zamrud)
                        }
                    }
                    if (j.genres.isNotEmpty()) {
                        Spacer(Modifier.height(14.dp))
                        // LazyRow dilebarkan ke tepi layar (keluar dari padding 18dp Column)
                        // supaya chip tidak terpotong sebelum tepi layar
                        LazyRow(
                            Modifier.layout { ukur, batas ->
                                val ekstra = 18.dp.roundToPx() * 2
                                val p = ukur.measure(batas.copy(maxWidth = batas.maxWidth + ekstra))
                                layout(batas.maxWidth, p.height) { p.place(-ekstra / 2, 0) }
                            },
                            contentPadding = PaddingValues(horizontal = 18.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(j.genres) { g ->
                                Box(
                                    Modifier
                                        .clip(
                                            RoundedCornerShape(16.dp),
                                        ).border(1.dp, ZamrudGelap, RoundedCornerShape(16.dp))
                                        .padding(horizontal = 13.dp, vertical = 7.dp),
                                ) { Text(g, fontSize = 12.5.sp, color = Zamrud) }
                            }
                        }
                    }
                    Spacer(Modifier.height(18.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Row(
                            Modifier
                                .weight(1f)
                                .shadow(8.dp, RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                                .background(Brush.horizontalGradient(listOf(Zamrud, ZamrudGelap)))
                                .clickable {
                                    if (segeraTayang) {
                                        pengingat = if (pengingat.contains(j.id)) pengingat - j.id else pengingat + j.id
                                        simpanPengingat(ctx, pengingat)
                                        pesan =
                                            if (pengingat.contains(j.id)) {
                                                "Pengingat dipasang — kamu akan dikabari begitu anime ini tayang."
                                            } else {
                                                "Pengingat dilepas."
                                            }
                                    } else if (j.episodes.isNotEmpty()) {
                                        onPutar(idxMulai)
                                    } else {
                                        pesan =
                                            if (memuatEpisode) {
                                                "Daftar episode masih dimuat…"
                                            } else {
                                                "Daftar episode belum tersedia. Coba lagi sebentar."
                                            }
                                    }
                                }.padding(vertical = 14.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (segeraTayang) {
                                Ikon("lonceng", Modifier.size(18.dp), DiAtasZamrud)
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    if (pengingat.contains(j.id)) "Pengingat Terpasang" else "Ingatkan Saya",
                                    fontSize = 15.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DiAtasZamrud,
                                )
                            } else {
                                Ikon("putar", Modifier.size(18.dp), DiAtasZamrud, terisi = true)
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Putar Episode ${j.episodes.getOrNull(idxMulai)?.number ?: 1}",
                                    fontSize = 15.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DiAtasZamrud,
                                )
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        TombolIkonBulat("markah", aktif = favorit) { onToggleFav() }
                        Spacer(Modifier.width(10.dp))
                        TombolIkonBulat("unduhan") {
                            pesan =
                                if (j.episodes.isNotEmpty()) {
                                    "Ketuk ikon unduh di samping episode untuk mengunduhnya."
                                } else if (memuatEpisode) {
                                    "Daftar episode masih dimuat…"
                                } else {
                                    "Daftar episode belum tersedia. Coba lagi sebentar."
                                }
                        }
                    }
                    if (pesan != null) {
                        Spacer(Modifier.height(12.dp))
                        Text(pesan!!, fontSize = 12.5.sp, color = BintangW)
                    }
                    Spacer(Modifier.height(20.dp))
                    JudulBagian("Sinopsis")
                    Spacer(Modifier.height(8.dp))
                    Text(
                        j.sinopsis.ifBlank { "Belum ada sinopsis." },
                        fontSize = 13.5.sp,
                        color = Redup,
                        lineHeight = 20.sp,
                        maxLines = if (sinopsisPenuh) Int.MAX_VALUE else 4,
                        overflow = TextOverflow.Ellipsis,
                        onTextLayout = { if (!sinopsisPenuh) sinopsisTerpotong = it.hasVisualOverflow },
                    )
                    if (sinopsisTerpotong || sinopsisPenuh) {
                        Text(
                            if (sinopsisPenuh) "Tutup" else "Lihat selengkapnya",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Zamrud,
                            modifier = Modifier.clickable { sinopsisPenuh = !sinopsisPenuh }.padding(vertical = 6.dp),
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    JudulBagian("Daftar Episode")
                    Spacer(Modifier.height(6.dp))
                }
            }
            if (j.episodes.isEmpty() && memuatEpisode && !segeraTayang) {
                // kerangka berkilau: bentuk baris episode sudah terlihat selagi daftar diambil
                items(4) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 5.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Kartu)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Kerangka(Modifier.width(34.dp).height(24.dp).clip(RoundedCornerShape(8.dp)))
                        Spacer(Modifier.width(10.dp))
                        Kerangka(Modifier.width(86.dp).height(52.dp).clip(RoundedCornerShape(9.dp)))
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Kerangka(Modifier.fillMaxWidth(0.5f).height(14.dp).clip(RoundedCornerShape(6.dp)))
                            Spacer(Modifier.height(8.dp))
                            Kerangka(Modifier.fillMaxWidth(0.8f).height(11.dp).clip(RoundedCornerShape(6.dp)))
                        }
                    }
                }
            } else if (j.episodes.isEmpty()) {
                item {
                    Column(Modifier.padding(horizontal = 18.dp, vertical = 6.dp)) {
                        Text(
                            when {
                                segeraTayang -> "Belum tayang. Pasang pengingat supaya tidak ketinggalan episode pertamanya."
                                memuatEpisode -> "Memuat daftar episode…"
                                else -> "Daftar episode belum tersedia. Periksa sambungan internetmu, lalu coba lagi."
                            },
                            fontSize = 13.sp,
                            color = Samar,
                        )
                        if (!segeraTayang && !memuatEpisode) {
                            Spacer(Modifier.height(10.dp))
                            Button(
                                onClick = onCobaLagiEpisode,
                                colors = ButtonDefaults.buttonColors(containerColor = Zamrud, contentColor = DiAtasZamrud),
                                shape = RoundedCornerShape(14.dp),
                            ) { Text("Coba Lagi", fontWeight = FontWeight.Bold) }
                        }
                    }
                }
            }
            items(j.episodes.indices.toList(), key = { it }) { idx ->
                val ep = j.episodes[idx]
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 5.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Kartu)
                        .clickable { onPutar(idx) }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // lebar tetap: angka berbeda lebar (E1 vs E2, E10) tak menggeser thumbnail dan teks
                    Box(
                        Modifier
                            .width(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Kartu2)
                            .padding(vertical = 5.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "E${ep.number}",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Redup,
                            maxLines = 1,
                            softWrap = false,
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Box(Modifier.width(86.dp).height(52.dp).clip(RoundedCornerShape(9.dp))) {
                        Gambar(bannerTampil, Modifier.fillMaxSize(), fokusX = fokusTampil)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Episode ${ep.number}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Teks)
                        Spacer(Modifier.height(3.dp))
                        Text(j.judul, fontSize = 11.5.sp, color = Samar, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    val persenUn = Pengunduh.aktif[Pengunduh.kunci(j.id, ep.number)]
                    val sudahUn = Pengunduh.kunci(j.id, ep.number) in unduhanTersimpan
                    Box(
                        Modifier
                            .height(36.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .clickable {
                                // ketuk saat sedang mengunduh = batalkan
                                if (persenUn != null) {
                                    Pengunduh.batal(j.id, ep.number) { pesan = it }
                                } else {
                                    Pengunduh.mulai(ctx, j, idx) { pesan = it }
                                }
                            }.padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (sudahUn) {
                            Ikon("centang", Modifier.size(18.dp), Zamrud)
                        } else if (persenUn != null) {
                            Text("$persenUn%", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Zamrud)
                        } else {
                            Ikon("unduhan", Modifier.size(18.dp), Redup)
                        }
                    }
                    Spacer(Modifier.width(6.dp))
                    Ikon("putar", Modifier.size(17.dp), Zamrud, terisi = true)
                }
            }
            // ruang di bawah daftar: di atas bilah navigasi sistem (edge-to-edge)
            item { Spacer(Modifier.navigationBarsPadding().height(18.dp)) }
        }
    }
}

class PeganganPosisi {
    var posisi: Int = 0
    var durasi: Int = 0
}

private fun labelKecepatan(k: Float): String =
    when (k) {
        0.5f -> "0,5x"
        0.75f -> "0,75x"
        1f -> "1x"
        1.25f -> "1,25x"
        1.5f -> "1,5x"
        1.75f -> "1,75x"
        2f -> "2x"
        else -> "${k}x"
    }

// Komentar ASLI: ditulis pengguna yang masuk dengan Google. Salinan kerja
// tersimpan di HP per judul+episode (prefs); sesudah paket komentar bersama
// (2026-11-25) komentar juga dibagikan lewat Firestore — lihat KomentarBersama.
// Tidak ada komentar contoh/palsu.
data class Komentar(
    val id: Long,
    val nama: String,
    val foto: String,
    val teks: String,
    val waktuMs: Long,
    val suka: Boolean = false,
    // uid penulis di Firebase. Kosong = komentar setempat warisan (ditulis
    // sebelum paket komentar bersama, atau saat sesi Firebase belum ada);
    // komentar seperti itu dianggap milik pengguna perangkat ini.
    val uid: String = "",
)

private fun kunciKomentar(
    idJudul: String,
    indeksEp: Int,
) = "komentar_${idJudul}_$indeksEp"

private fun bacaKomentar(
    ctx: Context,
    idJudul: String,
    indeksEp: Int,
): List<Komentar> {
    val mentah = prefs(ctx).getString(kunciKomentar(idJudul, indeksEp), null) ?: return emptyList()
    return try {
        val arr = org.json.JSONArray(mentah)
        val hasil = ArrayList<Komentar>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            hasil.add(
                Komentar(
                    o.getLong("id"),
                    o.getString("nama"),
                    o.optString("foto"),
                    o.getString("teks"),
                    o.getLong("waktu"),
                    o.optBoolean("suka"),
                    o.optString("uid"),
                ),
            )
        }
        hasil
    } catch (e: Exception) {
        emptyList()
    }
}

private fun simpanKomentar(
    ctx: Context,
    idJudul: String,
    indeksEp: Int,
    daftar: List<Komentar>,
) {
    val arr = org.json.JSONArray()
    for (k in daftar) {
        arr.put(
            org.json
                .JSONObject()
                .put("id", k.id)
                .put("nama", k.nama)
                .put("foto", k.foto)
                .put("teks", k.teks)
                .put("waktu", k.waktuMs)
                .put("suka", k.suka)
                .put("uid", k.uid),
        )
    }
    prefs(ctx).edit().putString(kunciKomentar(idJudul, indeksEp), arr.toString()).apply()
}

// ================= Komentar Bersama (Firestore) =================
// Paket komentar bersama (perintah pengguna 2026-11-25, "terapkan semua"):
// komentar per judul+episode juga disimpan di Firestore proyek
// anime-id-login — koleksi komentar/{idJudul}/episode/{indeksEp}/isi/{idKomentar} —
// supaya penonton yang masuk Google membaca tulisan yang sama di semua
// perangkat. Bentuk dokumen mengikuti firestore.rules yang sudah terbit:
// uid, nama, foto, teks, waktu, suka (JUMLAH suka bersama; aturan mengizinkan
// pengguna masuk mana pun mengubah bidang "suka" saja). Bidang suka setempat
// di Komentar tetap penanda "saya menyukai ini" di perangkat ini; jumlah
// bersama digeser +1/-1 mengikuti penanda itu.
// Pengguna yang belum masuk Firebase: semua jalan di sini diam saja dan
// komentar setempat (prefs) berlaku persis seperti sebelumnya — sama seperti
// filosofi SinkronAkun, kegagalan jaringan/aturan tidak pernah mengganggu.
private object KomentarBersama {
    private fun koleksiIsi(
        idJudul: String,
        indeksEp: Int,
    ) = com.google.firebase.firestore.FirebaseFirestore
        .getInstance()
        .collection("komentar")
        .document(idJudul)
        .collection("episode")
        .document(indeksEp.toString())
        .collection("isi")

    // Id dokumen ditentukan dari waktu tulis + uid penulis: unik per penulis
    // dan bisa dibangun ulang dari Komentar mana pun yang sudah bersama.
    private fun idDokumen(k: Komentar) = "${k.waktuMs}_${k.uid}"

    private fun dariDokumen(d: com.google.firebase.firestore.DocumentSnapshot): Komentar? {
        val waktu = d.getLong("waktu") ?: return null
        return Komentar(
            id = waktu,
            nama = d.getString("nama") ?: "",
            foto = d.getString("foto") ?: "",
            teks = d.getString("teks") ?: "",
            waktuMs = waktu,
            suka = false,
            uid = d.getString("uid") ?: "",
        )
    }

    // Ambil komentar bersama, gabung dengan daftar setempat, serahkan hasil
    // gabungan lewat selesai (utas utama). Gabung: komentar setempat yang
    // milik pengguna ini (uid kosong = warisan, atau uid saya) diunggah dulu
    // dengan gabung-tulis yang TIDAK menyentuh jumlah suka, supaya komentar
    // yang ditulis saat luring ikut tersinkron; penanda suka setempat
    // dipertahankan per komentar; urutan terbaru di depan. Bila belum masuk
    // atau gagal: selesai(null) dan pemanggil membiarkan daftar setempat.
    fun muat(
        ctx: Context,
        idJudul: String,
        indeksEp: Int,
        setempat: List<Komentar>,
        selesai: (List<Komentar>?) -> Unit,
    ) {
        SinkronAkun.pastikanFirebase(ctx)
        val uid = SinkronAkun.uidSaya()
        if (uid == null) {
            selesai(null)
            return
        }
        try {
            for (k in setempat) {
                if (k.uid.isBlank() || k.uid == uid) {
                    unggah(idJudul, indeksEp, if (k.uid.isBlank()) k.copy(uid = uid) else k, adopsi = true)
                }
            }
            koleksiIsi(idJudul, indeksEp)
                .orderBy("waktu", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(100)
                .get()
                .addOnSuccessListener { hasil ->
                    try {
                        val sukaSetempat = HashMap<String, Boolean>()
                        for (k in setempat) {
                            val u = if (k.uid.isBlank()) uid else k.uid
                            sukaSetempat["${k.waktuMs}|$u"] = k.suka
                        }
                        val gabung = LinkedHashMap<String, Komentar>()
                        for (d in hasil.documents) {
                            val k = dariDokumen(d) ?: continue
                            val kunci = "${k.waktuMs}|${k.uid}"
                            gabung[kunci] = k.copy(suka = sukaSetempat[kunci] ?: false)
                        }
                        for (k in setempat) {
                            val kk = if (k.uid.isBlank()) k.copy(uid = uid) else k
                            val kunci = "${kk.waktuMs}|${kk.uid}"
                            if (!gabung.containsKey(kunci)) gabung[kunci] = kk
                        }
                        selesai(gabung.values.sortedByDescending { it.waktuMs })
                    } catch (e: Exception) {
                        selesai(null)
                    }
                }.addOnFailureListener { selesai(null) }
        } catch (e: Exception) {
            selesai(null)
        }
    }

    // Tulis satu komentar ke Firestore (diam bila uid kosong / gagal).
    // adopsi=true: gabung-tulis tanpa bidang suka — mengunggah komentar lama
    // atau yang sempat luring tanpa menghapus jumlah suka yang sudah terkumpul.
    fun unggah(
        idJudul: String,
        indeksEp: Int,
        k: Komentar,
        adopsi: Boolean = false,
    ) {
        if (k.uid.isBlank()) return
        try {
            val data =
                hashMapOf<String, Any>(
                    "uid" to k.uid,
                    "nama" to k.nama,
                    "foto" to k.foto,
                    "teks" to k.teks,
                    "waktu" to k.waktuMs,
                )
            val dok = koleksiIsi(idJudul, indeksEp).document(idDokumen(k))
            if (adopsi) {
                dok.set(
                    data,
                    com.google.firebase.firestore.SetOptions
                        .merge(),
                )
            } else {
                data["suka"] = 0L
                dok.set(data)
            }
        } catch (e: Exception) {
        }
    }

    // Hapus komentar bersama (aturan Firestore hanya mengizinkan penulisnya).
    fun hapus(
        idJudul: String,
        indeksEp: Int,
        k: Komentar,
    ) {
        if (k.uid.isBlank()) return
        try {
            koleksiIsi(idJudul, indeksEp).document(idDokumen(k)).delete()
        } catch (e: Exception) {
        }
    }

    // Geser jumlah suka bersama mengikuti penanda suka setempat yang baru.
    fun suka(
        idJudul: String,
        indeksEp: Int,
        k: Komentar,
        menyukai: Boolean,
    ) {
        if (k.uid.isBlank()) return
        try {
            koleksiIsi(idJudul, indeksEp)
                .document(idDokumen(k))
                .update(
                    "suka",
                    com.google.firebase.firestore.FieldValue
                        .increment(if (menyukai) 1L else -1L),
                )
        } catch (e: Exception) {
        }
    }
}

private fun fmtRelatif(ms: Long): String {
    val selisih = System.currentTimeMillis() - ms
    val menit = selisih / 60000
    val jam = menit / 60
    val hari = jam / 24
    return when {
        selisih < 60000 -> "Baru saja"
        menit < 60 -> "$menit menit lalu"
        jam < 24 -> "$jam jam lalu"
        hari == 1L -> "Kemarin"
        hari < 30 -> "$hari hari lalu"
        else -> java.text.SimpleDateFormat("d MMM yyyy", java.util.Locale("id", "ID")).format(java.util.Date(ms))
    }
}

private fun warnaNama(nama: String): Color {
    val pilihan = listOf(0xFF7A5C3E, 0xFF3E4A7A, 0xFF3E7A5C, 0xFF6B3E7A, 0xFF7A3E4E, 0xFF3E6B7A)
    return Color(pilihan[nama.hashCode().mod(pilihan.size)])
}

@Composable
private fun TombolAksi(
    ikon: String,
    label: String,
    aktif: Boolean,
    onKlik: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (aktif) Zamrud.copy(alpha = 0.16f) else Kartu)
            .clickable { onKlik() }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Ikon(ikon, Modifier.size(20.dp), if (aktif) Zamrud else Teks, terisi = aktif)
        Spacer(Modifier.height(5.dp))
        Text(label, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = if (aktif) Zamrud else Redup)
    }
}

@Composable
private fun ChipInfo(
    teks: String,
    warna: Color = Redup,
) {
    Box(
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Kartu2)
            .padding(horizontal = 9.dp, vertical = 4.dp),
    ) { Text(teks, fontSize = 11.5.sp, fontWeight = FontWeight.Medium, color = warna) }
}

@Composable
@UnstableApi
fun LayarPutar(
    j: Judul,
    indeksEp: Int,
    favorit: Boolean,
    akun: AkunGoogle?,
    onToggleFav: () -> Unit,
    onKembali: () -> Unit,
    onPutarEp: (Int) -> Unit,
    keluar: Boolean = false,
) {
    val ctx = LocalContext.current
    val aktivitas = ctx as? Activity
    val ep = j.episodes.getOrNull(indeksEp) ?: j.episodes.firstOrNull()
    // episode berikutnya yang bisa diputar: lewati episode rusak (tanpa sumber)
    val idxBerikut =
        remember(j.episodes, indeksEp) {
            ((indeksEp + 1)..j.episodes.lastIndex).firstOrNull { j.episodes[it].playlist != null }
        }
    // bila episode sudah diunduh, putar dari berkas lokal (tanpa internet)
    val berkasLokal = remember(j.id, indeksEp) { ep?.let { Pengunduh.berkas(ctx, j.id, it.number) } }
    val pegangan =
        remember(j.id, indeksEp) {
            PeganganPosisi().also { p ->
                try {
                    val pr = bacaProgres(ctx).firstOrNull { it.idJudul == j.id && it.indeksEpisode == indeksEp }
                    if (pr != null) {
                        // episode yang hampir/sudah tamat dimulai dari awal, bukan dari detik terakhir
                        p.posisi = if (pr.durasi > 0 && pr.posisi >= pr.durasi * 0.95) 0 else pr.posisi
                    }
                    // posisi per episode lebih akurat; mundur 3 detik supaya adegan terakhir terulang sedikit
                    val tersimpan = bacaPosisiEp(ctx, j.id, indeksEp)
                    if (tersimpan > 0) p.posisi = maxOf(0, tersimpan - 3000)
                } catch (e: Exception) {
                    // abaikan
                }
            }
        }
    var layarPenuh by remember { mutableStateOf(KeadaanPutar.penuhLanjut) }
    LaunchedEffect(Unit) { KeadaanPutar.penuhLanjut = false }
    var pesan by remember { mutableStateOf<String?>(null) }
    var komentar by remember(j.id, indeksEp) { mutableStateOf(bacaKomentar(ctx, j.id, indeksEp)) }
    var isiKomentar by remember(j.id, indeksEp) { mutableStateOf("") }
    var pesanKomentar by remember(j.id, indeksEp) { mutableStateOf<String?>(null) }
    // uid Firebase saya, dibaca segar tiap gambar ulang: tombol Hapus hanya
    // untuk komentar sendiri / warisan setempat — komentar penonton lain
    // tidak bisa (dan tidak boleh) dihapus dari sini.
    val uidKu = SinkronAkun.uidSaya()
    val daftarKomentar = rememberLazyListState()
    // daftar episode langsung membuka di dekat episode yang sedang diputar
    val daftarEpisode = rememberLazyListState(initialFirstVisibleItemIndex = maxOf(0, indeksEp - 2))
    // Komentar bersama: sesudah layar terbuka, ambil versi Firestore dan
    // gabungkan dengan salinan setempat (diam saja bila belum masuk Firebase —
    // daftar setempat tetap tampil apa adanya).
    LaunchedEffect(j.id, indeksEp) {
        KomentarBersama.muat(ctx, j.id, indeksEp, komentar) { gabungan ->
            if (gabungan != null) {
                val kunciAda = gabungan.map { "${it.waktuMs}|${it.uid}" }.toSet()
                val tambahan = komentar.filter { "${it.waktuMs}|${it.uid}" !in kunciAda }
                val akhir = (tambahan + gabungan).sortedByDescending { it.waktuMs }
                komentar = akhir
                simpanKomentar(ctx, j.id, indeksEp, akhir)
            }
        }
    }
    var sinopsisTerbuka by remember(j.id) { mutableStateOf(false) }
    var putarOtomatis by remember { mutableStateOf(prefs(ctx).getBoolean("putar_otomatis", true)) }
    val tampilKomentar = komentar
    val lingkup = rememberCoroutineScope()
    val fokus = LocalFocusManager.current
    val kirimKomentar = {
        val teksBaru = isiKomentar.trim()
        val a = akun
        if (teksBaru.isNotEmpty()) {
            if (a == null) {
                pesanKomentar = "Masuk dengan Google dulu untuk berkomentar."
            } else {
                pesanKomentar = null
                val sekarang = System.currentTimeMillis()
                val baru =
                    Komentar(
                        id = sekarang,
                        nama = a.nama.ifBlank { a.email.substringBefore('@') },
                        foto = a.foto,
                        teks = teksBaru,
                        waktuMs = sekarang,
                        uid = SinkronAkun.uidSaya() ?: "",
                    )
                komentar = listOf(baru) + komentar
                simpanKomentar(ctx, j.id, indeksEp, komentar)
                KomentarBersama.unggah(j.id, indeksEp, baru)
                isiKomentar = ""
                fokus.clearFocus()
                lingkup.launch { daftarKomentar.animateScrollToItem(1) }
            }
        }
    }
    val toggleSuka: (Komentar) -> Unit = { k ->
        komentar = komentar.map { if (it.id == k.id) it.copy(suka = !it.suka) else it }
        simpanKomentar(ctx, j.id, indeksEp, komentar)
        KomentarBersama.suka(j.id, indeksEp, k, menyukai = !k.suka)
    }
    val hapusKomentar: (Komentar) -> Unit = { k ->
        komentar = komentar.filter { it.id != k.id }
        simpanKomentar(ctx, j.id, indeksEp, komentar)
        KomentarBersama.hapus(j.id, indeksEp, k)
    }

    fun simpanSekarang() {
        try {
            if (pegangan.durasi > 0 && pegangan.posisi > 0) {
                simpanProgres(ctx, Progres(j.id, indeksEp, pegangan.posisi, pegangan.durasi))
                simpanPosisiEp(ctx, j.id, indeksEp, pegangan.posisi, pegangan.durasi)
            }
        } catch (e: Exception) {
            // abaikan
        }
    }
    // Simpan progres berkala dan saat aplikasi ke latar, supaya tidak hilang bila proses dimatikan sistem.
    LaunchedEffect(j.id, indeksEp) {
        while (true) {
            delay(5000)
            simpanSekarang()
        }
    }
    val sedangKeluar by rememberUpdatedState(keluar)
    DisposableEffect(aktivitas) {
        val pemilik = aktivitas as? androidx.lifecycle.LifecycleOwner
        val pengamat =
            androidx.lifecycle.LifecycleEventObserver { _, kejadian ->
                if (kejadian == androidx.lifecycle.Lifecycle.Event.ON_PAUSE || kejadian == androidx.lifecycle.Lifecycle.Event.ON_STOP) {
                    simpanSekarang()
                }
                if (kejadian == androidx.lifecycle.Lifecycle.Event.ON_RESUME && !sedangKeluar) {
                    KeadaanPip.halamanPutarAktif = true
                }
            }
        pemilik?.lifecycle?.addObserver(pengamat)
        onDispose {
            pemilik?.lifecycle?.removeObserver(pengamat)
        }
    }
    // Setelah keluar dari layar penuh, lepas kunci orientasi supaya rotasi otomatis aktif lagi.
    var pernahPenuh by remember { mutableStateOf(false) }
    LaunchedEffect(layarPenuh) {
        if (layarPenuh) {
            pernahPenuh = true
        } else if (pernahPenuh) {
            delay(1500)
            aktivitas?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
    // Mode imersif: sembunyikan status bar dan navigation bar saat layar penuh / lanskap.
    val konfig = androidx.compose.ui.platform.LocalConfiguration.current
    val imersif = layarPenuh || konfig.screenWidthDp > konfig.screenHeightDp
    DisposableEffect(imersif) {
        val jendela = aktivitas?.window
        if (jendela != null) {
            try {
                val pengendali =
                    androidx.core.view.WindowCompat
                        .getInsetsController(jendela, jendela.decorView)
                if (imersif) {
                    pengendali.systemBarsBehavior = androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    pengendali.hide(
                        androidx.core.view.WindowInsetsCompat.Type
                            .systemBars(),
                    )
                } else {
                    pengendali.show(
                        androidx.core.view.WindowInsetsCompat.Type
                            .systemBars(),
                    )
                }
            } catch (e: Exception) {
                // abaikan
            }
        }
        onDispose {
            try {
                if (jendela != null) {
                    androidx.core.view.WindowCompat
                        .getInsetsController(jendela, jendela.decorView)
                        .show(
                            androidx.core.view.WindowInsetsCompat.Type
                                .systemBars(),
                        )
                }
            } catch (e: Exception) {
                // abaikan
            }
        }
    }
    // Selama halaman putar tampil, tandai supaya MainActivity bisa masuk
    // mode jendela kecil (PiP) saat pengguna menekan tombol Beranda.
    DisposableEffect(Unit) {
        KeadaanPip.halamanPutarAktif = true
        onDispose { KeadaanPip.halamanPutarAktif = false }
    }
    // Halaman ini sedang dianimasikan keluar (tombol Kembali sudah ditekan): composable-nya
    // masih hidup beberapa ratus milidetik, jadi simpan progres, matikan penanda PiP,
    // lepas penahan layar, dan tampilkan lagi bilah sistem SEKARANG, bukan menunggu animasi usai.
    // (pemutar dijeda lewat parameter `jeda` di PemutarExo)
    LaunchedEffect(keluar) {
        if (keluar) {
            simpanSekarang()
            KeadaanPip.halamanPutarAktif = false
            aktivitas?.window?.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            try {
                val jendela = aktivitas?.window
                if (jendela != null) {
                    androidx.core.view.WindowCompat
                        .getInsetsController(jendela, jendela.decorView)
                        .show(
                            androidx.core.view.WindowInsetsCompat.Type
                                .systemBars(),
                        )
                }
            } catch (e: Exception) {
                // abaikan
            }
            if (!KeadaanPutar.penuhLanjut) aktivitas?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
    DisposableEffect(Unit) {
        aktivitas?.window?.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            aktivitas?.window?.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            try {
                if (pegangan.durasi > 0) {
                    simpanProgres(ctx, Progres(j.id, indeksEp, pegangan.posisi, pegangan.durasi))
                    simpanPosisiEp(ctx, j.id, indeksEp, pegangan.posisi, pegangan.durasi)
                }
            } catch (e: Exception) {
                // abaikan
            }
            if (!KeadaanPutar.penuhLanjut) aktivitas?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
    // Kembali dari halaman putar: video mengecil jadi mini pemutar dan lanjut dari posisi terakhir.
    val kembaliKeMini: () -> Unit = {
        val alamat =
            when {
                berkasLokal != null -> "lokal:" + berkasLokal.absolutePath
                ep?.playlist?.startsWith("http") == true -> ep.playlist
                else -> null
            }
        if (alamat != null && pegangan.durasi > 0 && pegangan.posisi < pegangan.durasi - 5000) {
            KeadaanMini.siapkan(j, indeksEp, alamat, pegangan.posisi)
        } else {
            KeadaanMini.tutup()
        }
        onKembali()
    }
    BackHandler {
        if (layarPenuh) {
            layarPenuh = false
            aktivitas?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            kembaliKeMini()
        }
    }

    if (ep == null) {
        Box(Modifier.fillMaxSize().background(Latar), contentAlignment = Alignment.Center) {
            Text("Episode tidak tersedia.", color = Redup)
        }
        return
    }

    androidx.compose.foundation.layout.BoxWithConstraints(Modifier.fillMaxSize().background(Latar)) {
        val lanskap = maxWidth > maxHeight || layarPenuh
        Box(Modifier.fillMaxSize()) {
            if (!lanskap) {
                Column(Modifier.fillMaxSize().statusBarsPadding().imePadding()) {
                    Spacer(Modifier.fillMaxWidth().aspectRatio(16f / 9f))
                    // daftar mengalir sampai di belakang kolom komentar yang melayang
                    Box(Modifier.weight(1f)) {
                        LazyColumn(
                            Modifier.fillMaxSize(),
                            state = daftarKomentar,
                            contentPadding = PaddingValues(bottom = 96.dp),
                        ) {
                            item {
                                Column(Modifier.padding(horizontal = 18.dp)) {
                                    Spacer(Modifier.height(16.dp))
                                    Text(j.judul, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Teks, lineHeight = 25.sp)
                                    Spacer(Modifier.height(4.dp))
                                    Text("Episode ${ep.number}", fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = Zamrud)
                                    Spacer(Modifier.height(12.dp))
                                    Row(
                                        Modifier.horizontalScroll(rememberScrollState()),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    ) {
                                        if (j.skor > 0.0) ChipInfo("★ ${fmtSkor(j.skor)}", BintangW)
                                        if (j.tahun > 0) ChipInfo("${j.tahun}")
                                        ChipInfo(labelStatus(j.status))
                                        j.genres.take(2).forEach { ChipInfo(it) }
                                    }
                                    Spacer(Modifier.height(18.dp))
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        TombolAksi("markah", "Favorit", favorit, { onToggleFav() }, Modifier.weight(1f))
                                        run {
                                            val persenUn = Pengunduh.aktif[Pengunduh.kunci(j.id, ep.number)]
                                            val sudahUn = Pengunduh.versi.let { Pengunduh.berkas(ctx, j.id, ep.number) != null }
                                            TombolAksi(
                                                "unduhan",
                                                if (sudahUn) {
                                                    "Terunduh"
                                                } else if (persenUn != null) {
                                                    "$persenUn%"
                                                } else {
                                                    "Unduh"
                                                },
                                                sudahUn,
                                                { Pengunduh.mulai(ctx, j, indeksEp) { pesan = it } },
                                                Modifier.weight(1f),
                                            )
                                        }
                                        TombolAksi("bagikan", "Bagikan", false, {
                                            val kirim =
                                                Intent(Intent.ACTION_SEND).apply {
                                                    type = "text/plain"
                                                    putExtra(Intent.EXTRA_TEXT, "Nonton ${j.judul} Episode ${ep.number} di Anime.id")
                                                }
                                            // onUserLeaveHint ikut terpanggil saat aplikasi membuka activity lain:
                                            // matikan penanda PiP dulu supaya video tidak mengecil di belakang chooser.
                                            // (dinyalakan lagi di ON_RESUME)
                                            KeadaanPip.halamanPutarAktif = false
                                            ctx.startActivity(Intent.createChooser(kirim, "Bagikan"))
                                        }, Modifier.weight(1f))
                                    }
                                    if (idxBerikut != null) {
                                        Spacer(Modifier.height(12.dp))
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(Zamrud)
                                                .clickable { onPutarEp(idxBerikut) }
                                                .padding(horizontal = 18.dp, vertical = 14.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Column(Modifier.weight(1f)) {
                                                Text("Berikutnya", fontSize = 11.5.sp, fontWeight = FontWeight.Medium, color = DiAtasZamrud)
                                                Text(
                                                    "Episode ${j.episodes[idxBerikut].number}",
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DiAtasZamrud,
                                                )
                                            }
                                            Ikon("berikutnya", Modifier.size(22.dp), DiAtasZamrud, terisi = true)
                                        }
                                    }
                                    if (pesan != null) {
                                        Spacer(Modifier.height(10.dp))
                                        Text(pesan!!, fontSize = 12.5.sp, color = BintangW)
                                    }
                                    if (j.sinopsis.isNotBlank()) {
                                        Spacer(Modifier.height(18.dp))
                                        Text(
                                            j.sinopsis,
                                            fontSize = 13.5.sp,
                                            color = Redup,
                                            lineHeight = 20.sp,
                                            maxLines = if (sinopsisTerbuka) Int.MAX_VALUE else 3,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                        Text(
                                            if (sinopsisTerbuka) "Sembunyikan" else "Selengkapnya",
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Zamrud,
                                            modifier = Modifier.clickable { sinopsisTerbuka = !sinopsisTerbuka }.padding(vertical = 6.dp),
                                        )
                                    }
                                    if (j.episodes.size > 1) {
                                        Spacer(Modifier.height(16.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                "Episode",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Teks,
                                                modifier = Modifier.weight(1f),
                                            )
                                            Text("Putar otomatis", fontSize = 12.sp, color = Samar)
                                            Spacer(Modifier.width(8.dp))
                                            Box(
                                                Modifier
                                                    .width(38.dp)
                                                    .height(22.dp)
                                                    .clip(CircleShape)
                                                    .background(if (putarOtomatis) Zamrud else Garis)
                                                    .clickable {
                                                        putarOtomatis = !putarOtomatis
                                                        prefs(ctx).edit().putBoolean("putar_otomatis", putarOtomatis).apply()
                                                    }.padding(2.dp),
                                                contentAlignment = if (putarOtomatis) Alignment.CenterEnd else Alignment.CenterStart,
                                            ) { Box(Modifier.size(18.dp).clip(CircleShape).background(Teks)) }
                                        }
                                        Spacer(Modifier.height(12.dp))
                                        androidx.compose.foundation.lazy.LazyRow(
                                            state = daftarEpisode,
                                            contentPadding = PaddingValues(horizontal = 18.dp),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            // lepas dari padding 16dp induk: baris menggulir sampai tepi layar,
                                            // chip tidak terpotong di pinggir
                                            modifier =
                                                Modifier.layout { ukur, batas ->
                                                    val tambah = 36.dp.roundToPx()
                                                    val pl = ukur.measure(batas.copy(maxWidth = batas.maxWidth + tambah))
                                                    layout(batas.maxWidth, pl.height) { pl.place(-tambah / 2, 0) }
                                                },
                                        ) {
                                            itemsIndexed(j.episodes, key = { i, _ -> i }) { idx, e ->
                                                val aktif = idx == indeksEp
                                                Box(
                                                    Modifier
                                                        .width(56.dp)
                                                        .height(44.dp)
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(if (aktif) Zamrud else Kartu)
                                                        .clickable { if (!aktif) onPutarEp(idx) },
                                                    contentAlignment = Alignment.Center,
                                                ) {
                                                    Text(
                                                        "${e.number}",
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (aktif) DiAtasZamrud else Redup,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Spacer(Modifier.height(24.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Komentar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Teks)
                                        Spacer(Modifier.width(6.dp))
                                        Text("${komentar.size}", fontSize = 14.sp, color = Samar, modifier = Modifier.weight(1f))
                                    }
                                    Spacer(Modifier.height(6.dp))
                                }
                            }
                            if (tampilKomentar.isEmpty()) {
                                item {
                                    KeadaanKosong("Belum ada komentar", modifier = Modifier.padding(vertical = 24.dp))
                                }
                            }
                            items(tampilKomentar, key = { it.id }) { k ->
                                Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 10.dp)) {
                                    if (k.foto.isNotBlank()) {
                                        Gambar(k.foto, Modifier.size(36.dp).clip(CircleShape))
                                    } else {
                                        Box(
                                            Modifier.size(36.dp).clip(CircleShape).background(warnaNama(k.nama)),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Text(k.nama.take(1).uppercase(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Teks)
                                        }
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    Column(Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(k.nama, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Teks)
                                            Spacer(Modifier.width(8.dp))
                                            Text(fmtRelatif(k.waktuMs), fontSize = 11.5.sp, color = Samar)
                                        }
                                        Spacer(Modifier.height(4.dp))
                                        Text(k.teks, fontSize = 13.5.sp, color = Redup, lineHeight = 20.sp)
                                        Spacer(Modifier.height(8.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Row(
                                                Modifier
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .clickable { toggleSuka(k) }
                                                    .padding(end = 10.dp, top = 2.dp, bottom = 2.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Ikon("hati", Modifier.size(16.dp), if (k.suka) MerahLembut else Samar, terisi = k.suka)
                                                if (k.suka) {
                                                    Spacer(Modifier.width(6.dp))
                                                    Text("1", fontSize = 12.sp, color = Samar)
                                                }
                                            }
                                            Spacer(Modifier.width(14.dp))
                                            Text(
                                                "Balas",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Samar,
                                                modifier = Modifier.clickable { isiKomentar = k.nama + " " },
                                            )
                                            if (k.uid.isBlank() || k.uid == uidKu) {
                                                Spacer(Modifier.width(14.dp))
                                                Text(
                                                    "Hapus",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Samar,
                                                    modifier = Modifier.clickable { hapusKomentar(k) },
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            item { Spacer(Modifier.height(16.dp)) }
                        }
                        // scrim: daftar memudar ke warna latar, sama seperti navbar bawah
                        val insetBawah = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                        Box(
                            Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .height(110.dp + insetBawah)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Latar.copy(alpha = 0.9f), Latar),
                                    ),
                                ),
                        )
                        Column(Modifier.align(Alignment.BottomCenter)) {
                            val pk = pesanKomentar
                            if (pk != null) {
                                Text(
                                    pk,
                                    fontSize = 12.5.sp,
                                    color = MerahLembut,
                                    modifier = Modifier.padding(horizontal = 26.dp, vertical = 4.dp),
                                )
                            }
                            // kapsul melayang: gaya sama dengan BilahBawah (Kartu, garis tipis, bayangan)
                            Box(Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 14.dp, vertical = 8.dp)) {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .shadow(12.dp, RoundedCornerShape(28.dp))
                                        .clip(RoundedCornerShape(28.dp))
                                        .background(Kartu)
                                        .border(1.dp, Garis, RoundedCornerShape(28.dp))
                                        .padding(6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    val aKu = akun
                                    if (aKu != null && aKu.foto.isNotBlank()) {
                                        Gambar(aKu.foto, Modifier.size(40.dp).clip(CircleShape))
                                    } else if (aKu != null) {
                                        Box(
                                            Modifier
                                                .size(
                                                    40.dp,
                                                ).clip(CircleShape)
                                                .background(Brush.linearGradient(listOf(Zamrud, ZamrudGelap))),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Text(
                                                (aKu.nama.firstOrNull() ?: 'A').uppercase(),
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = DiAtasZamrud,
                                            )
                                        }
                                    } else {
                                        Box(
                                            Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(Kartu2)
                                                .border(1.dp, Garis, CircleShape),
                                            contentAlignment = Alignment.Center,
                                        ) { Ikon("akun", Modifier.size(20.dp), Samar) }
                                    }
                                    Box(
                                        Modifier.weight(1f).padding(horizontal = 12.dp),
                                        contentAlignment = Alignment.CenterStart,
                                    ) {
                                        if (isiKomentar.isEmpty()) Text("Tulis komentar...", fontSize = 14.sp, color = Samar)
                                        BasicTextField(
                                            value = isiKomentar,
                                            onValueChange = {
                                                isiKomentar = it
                                                pesanKomentar = null
                                            },
                                            textStyle = TextStyle(color = Teks, fontSize = 14.sp),
                                            cursorBrush = SolidColor(Zamrud),
                                            maxLines = 3,
                                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                            keyboardActions = KeyboardActions(onSend = { kirimKomentar() }),
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                    }
                                    // tombol kirim: pil hijau yang sama dengan tab aktif navbar
                                    Box(
                                        Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .then(
                                                if (isiKomentar.isNotBlank()) {
                                                    Modifier.background(Brush.linearGradient(listOf(ZamrudTerang, Zamrud)))
                                                } else {
                                                    Modifier.background(Kartu2)
                                                },
                                            ).clickable { kirimKomentar() },
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Ikon(
                                            "kirim",
                                            Modifier.size(19.dp),
                                            if (isiKomentar.isNotBlank()) DiAtasZamrud else Samar,
                                            terisi = true,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Kunci portrait hanya sementara; lepas lagi supaya rotasi otomatis tetap hidup
            // (termasuk saat lanskap berasal dari rotasi perangkat, bukan tombol layar penuh).
            val keluarLanskap: () -> Unit = {
                layarPenuh = false
                aktivitas?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                lingkup.launch {
                    delay(1500)
                    aktivitas?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }
            }
            val aksiKembali: () -> Unit = {
                if (lanskap) keluarLanskap() else kembaliKeMini()
            }
            val aksiPenuh: () -> Unit = {
                if (lanskap) {
                    keluarLanskap()
                } else {
                    layarPenuh = true
                    aktivitas?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
            }
            val modPutar =
                if (lanskap) {
                    Modifier.fillMaxSize()
                } else {
                    Modifier.statusBarsPadding().fillMaxWidth().aspectRatio(16f / 9f)
                }
            val jalurPutar =
                when {
                    berkasLokal != null -> "lokal:" + berkasLokal.absolutePath
                    ep.playlist?.startsWith("http") == true -> ep.playlist
                    else -> null
                }
            if (jalurPutar != null) {
                PemutarExo(
                    jalurEp = jalurPutar,
                    jalurBerikut = idxBerikut?.let { j.episodes[it].playlist },
                    judulTeks = if (lanskap) "${j.judul} — Episode ${ep.number}" else j.judul,
                    posisiAwal = pegangan.posisi,
                    pegangan = pegangan,
                    onSelesai = {
                        if (putarOtomatis && idxBerikut != null) {
                            KeadaanPutar.penuhLanjut = layarPenuh
                            onPutarEp(idxBerikut)
                        }
                    },
                    onKembali = aksiKembali,
                    onTogglePenuh = aksiPenuh,
                    modifier = modPutar,
                    urlPoster = j.banner.ifBlank { j.poster },
                    jeda = keluar,
                )
            } else {
                Box(modPutar.background(Latar), contentAlignment = Alignment.Center) {
                    Text("Sumber tidak tersedia untuk judul ini.", color = Redup, fontSize = 13.5.sp)
                }
            }
        }
    }
}

// ================= Pemutar Exo =================
// Pemutar Media3 untuk cara baru: memutar alamat langsung — berkas lokal
// hasil unduhan atau URL paket hasil pabrik (HLS adaptif oleh ExoPlayer).
// Posisi/durasi (ms) ditulis ke PeganganPosisi supaya simpan progres LayarPutar tetap bekerja.
private fun fmtWaktuExo(ms: Int): String {
    if (ms <= 0) return "0:00"
    val dtk = ms / 1000
    val jam = dtk / 3600
    val menit = (dtk % 3600) / 60
    val sisa = dtk % 60
    return if (jam > 0) {
        String.format("%d:%02d:%02d", jam, menit, sisa)
    } else {
        String.format("%d:%02d", menit, sisa)
    }
}

// Cache segmen HLS (maks 200 MB, buang yang terlama): putar ulang & geser mundur
// tidak mengunduh ulang, dan segmen yang sudah dimuat tetap lancar saat sinyal naik-turun.
@UnstableApi
object CachePemutar {
    // Satu klien OkHttp dipakai bersama oleh pemutar dan pemanasan: HTTP/2 (banyak permintaan
    // dalam satu koneksi), koneksi dipakai ulang antar segmen (tanpa handshake TCP/TLS baru
    // tiap potongan), dan kolam koneksi tetap hangat. Menggantikan HttpURLConnection (HTTP/1.1).
    private val klienHttp: okhttp3.OkHttpClient by lazy {
        okhttp3.OkHttpClient
            .Builder()
            .protocols(listOf(okhttp3.Protocol.HTTP_2, okhttp3.Protocol.HTTP_1_1))
            .connectionPool(okhttp3.ConnectionPool(8, 5, java.util.concurrent.TimeUnit.MINUTES))
            .connectTimeout(8, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(8, java.util.concurrent.TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    fun pabrikHttp(): androidx.media3.datasource.okhttp.OkHttpDataSource.Factory =
        androidx.media3.datasource.okhttp.OkHttpDataSource
            .Factory(klienHttp)

    @Volatile private var cache: androidx.media3.datasource.cache.SimpleCache? = null

    fun ambil(ctx: Context): androidx.media3.datasource.cache.SimpleCache =
        cache ?: synchronized(this) {
            cache ?: androidx.media3.datasource.cache
                .SimpleCache(
                    java.io.File(ctx.applicationContext.cacheDir, "media_cache"),
                    androidx.media3.datasource.cache
                        .LeastRecentlyUsedCacheEvictor(200L * 1024 * 1024),
                    androidx.media3.database.StandaloneDatabaseProvider(ctx.applicationContext),
                ).also { cache = it }
        }
}

// Pemanasan episode berikutnya (versi aman): hanya playlist + 2 segmen pertama
// rendisi terendah (3 segmen), ditulis ke cache yang sama dengan pemutar. Hanya dijalankan
// di jaringan tidak berbayar (Wi-Fi), saat episode berjalan >=70% dan penyangga
// sudah cukup. Semua kegagalan diabaikan; tidak pernah mengganggu pemutaran.
@UnstableApi
object PemanasanVideo {
    private val sudah = java.util.Collections.synchronizedSet(HashSet<String>())

    fun jaringanAman(ctx: Context): Boolean {
        return try {
            val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
            val cap = cm.getNetworkCapabilities(cm.activeNetwork ?: return false) ?: return false
            cap.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                cap.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
        } catch (e: Exception) {
            false
        }
    }

    private fun fabrik(ctx: Context): androidx.media3.datasource.cache.CacheDataSource.Factory {
        val http = CachePemutar.pabrikHttp()
        return androidx.media3.datasource.cache.CacheDataSource
            .Factory()
            .setCache(CachePemutar.ambil(ctx))
            .setUpstreamDataSourceFactory(http)
            .setFlags(androidx.media3.datasource.cache.CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    private fun bacaTeks(
        f: androidx.media3.datasource.cache.CacheDataSource.Factory,
        alamat: String,
    ): String {
        val sumber = f.createDataSource()
        val aliran =
            androidx.media3.datasource.DataSourceInputStream(
                sumber,
                androidx.media3.datasource.DataSpec(Uri.parse(alamat)),
            )
        return aliran.use { String(it.readBytes(), Charsets.UTF_8) }
    }

    // true = selesai / tak perlu dikerjakan; false = gagal (boleh dicoba lagi)
    fun panaskan(
        ctx: Context,
        url: String,
    ): Boolean {
        if (!sudah.add(url)) return true
        try {
            val f = fabrik(ctx)
            val master = bacaTeks(f, url)
            var alamatMedia = url
            var teksMedia = master
            if (master.contains("#EXT-X-STREAM-INF")) {
                var bwTerendah = Long.MAX_VALUE
                var uriTerendah: String? = null
                val baris = master.lines()
                for (i in baris.indices) {
                    val b = baris[i].trim()
                    if (!b.startsWith("#EXT-X-STREAM-INF")) continue
                    val bw =
                        Regex("(?<![A-Z-])BANDWIDTH=(\\d+)")
                            .find(b)
                            ?.groupValues
                            ?.get(1)
                            ?.toLongOrNull() ?: (Long.MAX_VALUE - 1)
                    var j = i + 1
                    while (j < baris.size && (baris[j].isBlank() || baris[j].startsWith("#"))) j++
                    if (j < baris.size && bw < bwTerendah) {
                        bwTerendah = bw
                        uriTerendah = baris[j].trim()
                    }
                }
                val u = uriTerendah ?: return true
                alamatMedia =
                    java.net
                        .URI(url)
                        .resolve(u)
                        .toString()
                teksMedia = bacaTeks(f, alamatMedia)
            }
            // segmen berbasis byte-range = satu berkas besar: jangan dipanaskan
            if (teksMedia.contains("#EXT-X-BYTERANGE")) return true
            val daftar = ArrayList<String>()
            Regex("#EXT-X-MAP:[^\\n]*URI=\"([^\"]+)\"")
                .find(teksMedia)
                ?.groupValues
                ?.get(1)
                ?.let { daftar.add(it) }
            var jumlah = 0
            for (b in teksMedia.lines()) {
                val t = b.trim()
                if (t.isNotEmpty() && !t.startsWith("#")) {
                    daftar.add(t)
                    jumlah++
                    if (jumlah >= 3) break
                }
            }
            val dasar = java.net.URI(alamatMedia)
            for (rel in daftar) {
                val spek = androidx.media3.datasource.DataSpec(Uri.parse(dasar.resolve(rel).toString()))
                androidx.media3.datasource.cache
                    .CacheWriter(f.createDataSource(), spek, null, null)
                    .cache()
            }
            return true
        } catch (e: Exception) {
            sudah.remove(url) // gagal: boleh dicoba lagi lain kali
            return false
        }
    }
}

@Composable
@UnstableApi
fun PemutarExo(
    jalurEp: String,
    jalurBerikut: String? = null,
    judulTeks: String,
    posisiAwal: Int,
    pegangan: PeganganPosisi,
    onKembali: () -> Unit,
    onTogglePenuh: () -> Unit,
    modifier: Modifier = Modifier,
    urlPoster: String = "",
    onSelesai: () -> Unit = {},
    jeda: Boolean = false,
) {
    val ctx = LocalContext.current
    val aksiSelesai by rememberUpdatedState(onSelesai)
    var gagalTotal by remember(jalurEp) { mutableStateOf(false) }
    var pesanGagal by remember(jalurEp) { mutableStateOf("") }
    var kunciMuat by remember(jalurEp) { mutableStateOf(0) }
    var main by remember { mutableStateOf(true) }
    var buffering by remember { mutableStateOf(true) }
    var memuatAwal by remember(jalurEp) { mutableStateOf(true) }
    var posisi by remember { mutableStateOf(posisiAwal) }
    var durasi by remember { mutableStateOf(0) }
    var tampil by remember { mutableStateOf(true) }
    var kecepatan by remember { mutableStateOf(prefs(ctx).getFloat("kecepatan", 1f)) }
    var menuKecepatan by remember { mutableStateOf(false) }
    var menuKualitas by remember { mutableStateOf(false) }
    var daftarKualitas by remember { mutableStateOf<List<Int>>(emptyList()) }
    var kualitasPilih by remember { mutableStateOf(prefs(ctx).getInt("kualitas", 0)) } // 0 = otomatis
    var tinggiAktif by remember { mutableStateOf(0) }
    // Pilihan kualitas yang BENAR-BENAR berlaku di episode ini: pilihan tersimpan (mis. 1080p)
    // bisa tidak ada di episode lain; saat itu pemutar berjalan Otomatis dan label harus jujur.
    val kualitasAktif = if (kualitasPilih > 0 && kualitasPilih in daftarKualitas) kualitasPilih else 0
    val percobaan = remember { intArrayOf(0) }
    var sudahTamat by remember(jalurEp) { mutableStateOf(false) }
    var muatPertama by remember(jalurEp) { mutableStateOf(true) }
    var paksaHls by remember(jalurEp) { mutableStateOf(false) }
    var ingin by remember { mutableStateOf(true) }
    var berakhir by remember { mutableStateOf(false) }
    var seret by remember { mutableStateOf(false) }
    // Lompat ketuk dua kali (gaya YouTube): kiri = mundur, kanan = maju, tengah = putar/jeda.
    // Setelah ketukan ganda pertama, ketukan tunggal berikutnya menumpuk (+10, +20, +30 ...)
    // selama jeda antar ketukan < 0,8 dtk.
    val detikLompat = 10
    var lompatAktif by remember { mutableStateOf(false) }
    var lompatArah by remember { mutableStateOf(1) } // -1 mundur, 1 maju
    var lompatTotal by remember { mutableStateOf(0) }
    var lompatKunci by remember { mutableStateOf(0) }
    var lompatTitik by remember { mutableStateOf(Offset.Zero) }
    var ketukTerakhir by remember { mutableStateOf(0L) }
    var zonaTerakhir by remember { mutableStateOf(-9) }
    // jendela kecil (PiP): kontrol disembunyikan supaya tidak menutupi video mungil
    val konfigPemutar = androidx.compose.ui.platform.LocalConfiguration.current
    val modePip =
        KeadaanPip.sedangPip || konfigPemutar.screenHeightDp < 260 ||
            (ctx as? android.app.Activity)?.isInPictureInPictureMode == true

    // Perkiraan kecepatan: mulai dari kecepatan terukur terakhir (disimpan di prefs),
    // bukan angka tetap, jadi episode baru langsung mulai di kualitas yang pantas.
    // Diambil 70% dan dijepit 0,4-8 Mbps supaya aman bila jaringan berganti.
    val meter =
        remember {
            val tersimpan = prefs(ctx).getLong("bw_terakhir", 0L)
            val awal = if (tersimpan > 0L) (tersimpan * 7 / 10).coerceIn(400_000L, 8_000_000L) else 900_000L
            androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
                .Builder(ctx)
                .setInitialBitrateEstimate(awal)
                .setSlidingWindowMaxWeight(1000)
                .build()
        }
    // bitrateEstimate sebelum ada sampel = angka awal tebakan, BUKAN hasil ukur. Menyimpannya
    // (mis. memutar berkas unduhan / segmen dari cache / gagal sebelum mengunduh) membuat
    // tebakan awal menyusut 30% di tiap sesi sampai terkunci di 0,4 Mbps -> video selalu
    // mulai di kualitas terendah. Maka hanya simpan bila meter pernah menerima sampel nyata.
    val adaSampelBw = remember { booleanArrayOf(false) }
    DisposableEffect(meter) {
        val pendengarBw =
            androidx.media3.exoplayer.upstream.BandwidthMeter.EventListener { _, byteDipindah, _ ->
                if (byteDipindah > 0L) adaSampelBw[0] = true
            }
        meter.addEventListener(android.os.Handler(android.os.Looper.getMainLooper()), pendengarBw)
        onDispose { meter.removeEventListener(pendengarBw) }
    }
    val simpanKecepatan: () -> Unit = {
        val b = meter.bitrateEstimate
        if (adaSampelBw[0] && b > 0L) prefs(ctx).edit().putLong("bw_terakhir", b).apply()
    }
    val exo =
        remember {
            // Pemutar diatur supaya LANCAR dan kualitas ADAPTIF bekerja:
            // - mulai dari kecepatan terukur terakhir lalu naik/turun sendiri;
            // - naik kualitas hanya setelah penyangga aman (>=15 dtk), turun cepat
            //   begitu penyangga menipis (<12 dtk) dan memakai 65% kecepatan terukur;
            // - penyangga 30-90 dtk DIBATASI 48 MB (memori aman di HP kecil) + cache
            //   segmen 200 MB di disk;
            // - macet diam-diam dianggap gagal setelah 8 dtk lalu dicoba ulang;
            // - gagal muat: dicoba ulang 6x per potongan, lalu 3x ulang total, baru pindah pintu.
            val kontrolMuat =
                androidx.media3.exoplayer.DefaultLoadControl
                    .Builder()
                    .setBufferDurationsMs(30_000, 90_000, 2_000, 6_000)
                    .setTargetBufferBytes(48 * 1024 * 1024)
                    .setBackBuffer(20_000, true)
                    .setPrioritizeTimeOverSizeThresholds(false)
                    .build()
            val http = CachePemutar.pabrikHttp()
            val lewatCache =
                androidx.media3.datasource.cache.CacheDataSource
                    .Factory()
                    .setCache(CachePemutar.ambil(ctx))
                    .setUpstreamDataSourceFactory(http)
                    .setFlags(androidx.media3.datasource.cache.CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
            // cache hanya membungkus jaringan; berkas lokal tetap dibaca langsung
            val sumberData =
                androidx.media3.datasource.DefaultDataSource
                    .Factory(ctx, lewatCache)
            val pabrikSumber =
                androidx.media3.exoplayer.source
                    .DefaultMediaSourceFactory(sumberData)
                    .setLoadErrorHandlingPolicy(
                        androidx.media3.exoplayer.upstream
                            .DefaultLoadErrorHandlingPolicy(6),
                    )
            val adaptif =
                androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection.Factory(
                    15_000,
                    12_000,
                    25_000,
                    0.65f,
                )
            val pembangunRender =
                androidx.media3.exoplayer
                    .DefaultRenderersFactory(ctx)
                    .setEnableDecoderFallback(true)
            ExoPlayer
                .Builder(ctx, pembangunRender)
                // Fokus audio: tanpa ini pemutar tidak pernah meminta fokus, jadi suara video
                // menimpa musik/aplikasi lain, tidak berhenti saat ada telepon masuk, dan tidak
                // mengecil saat ada notifikasi bersuara.
                .setAudioAttributes(
                    androidx.media3.common.AudioAttributes
                        .Builder()
                        .setUsage(androidx.media3.common.C.USAGE_MEDIA)
                        .setContentType(androidx.media3.common.C.AUDIO_CONTENT_TYPE_MOVIE)
                        .build(),
                    true,
                ).setBandwidthMeter(meter)
                .setLoadControl(kontrolMuat)
                .setMediaSourceFactory(pabrikSumber)
                .setTrackSelector(
                    androidx.media3.exoplayer.trackselection
                        .DefaultTrackSelector(ctx, adaptif),
                ).setHandleAudioBecomingNoisy(true)
                .build()
                .apply {
                    setPlaybackSpeed(prefs(ctx).getFloat("kecepatan", 1f))
                }
        }

    // Pilih kualitas: 0 = otomatis (ABR), selain itu kunci ke tinggi tertentu (mis. 720).
    val terapkanKualitas: (Int) -> Unit = { h ->
        val ubah =
            exo.trackSelectionParameters
                .buildUpon()
                .clearOverridesOfType(androidx.media3.common.C.TRACK_TYPE_VIDEO)
        if (h > 0) {
            val grup =
                exo.currentTracks.groups.firstOrNull {
                    it.type == androidx.media3.common.C.TRACK_TYPE_VIDEO
                }
            if (grup != null) {
                var terbaik = -1
                var bitrateTerbaik = -1
                for (i in 0 until grup.length) {
                    val f = grup.getTrackFormat(i)
                    if (grup.isTrackSupported(i) && f.height == h && f.bitrate >= bitrateTerbaik) {
                        terbaik = i
                        bitrateTerbaik = f.bitrate
                    }
                }
                if (terbaik >= 0) {
                    ubah.setOverrideForType(
                        androidx.media3.common.TrackSelectionOverride(grup.mediaTrackGroup, listOf(terbaik)),
                    )
                }
            }
        }
        exo.trackSelectionParameters = ubah.build()
    }

    // Muat alamat langsung tiap episode berubah / diminta ulang: berkas lokal
    // hasil unduhan, atau URL paket hasil pabrik (HLS — kualitas adaptif
    // dipilih ExoPlayer sendiri dari daftar rendisi di master playlist).
    LaunchedEffect(jalurEp, kunciMuat, paksaHls) {
        percobaan[0] = 0
        gagalTotal = false
        memuatAwal = true
        // Pintu putar kini SATU alamat (Pages proyek judulnya); mekanisme pintu
        // cadangan cermin lama dibuang 2026-11-25 — sumber selalu jalurEp.
        val sumber = jalurEp
        val uri =
            when {
                sumber.startsWith("lokal:") -> "file://" + sumber.removePrefix("lokal:")
                sumber.startsWith("http") -> sumber
                else -> null
            }
        if (uri == null) {
            gagalTotal = true
            memuatAwal = false
            return@LaunchedEffect
        }
        try {
            // alamat tanpa ekstensi .m3u8 (mis. lewat proksi) tidak dikenali ExoPlayer;
            // tandai HLS secara eksplisit bila alamatnya memuat m3u8 atau setelah gagal dikenali.
            val butir =
                MediaItem
                    .Builder()
                    .setUri(uri)
                    .apply {
                        if (paksaHls || uri.contains(".m3u8", ignoreCase = true)) {
                            setMimeType(androidx.media3.common.MimeTypes.APPLICATION_M3U8)
                        }
                    }.build()
            exo.setMediaItem(butir)
            exo.prepare()
            val pertama = muatPertama
            val target = if (pertama) posisiAwal else pegangan.posisi
            if (target > 0) exo.seekTo(target.toLong())
            muatPertama = false
            // Putar otomatis hanya pada muat pertama episode. Saat muat ulang / paksa HLS
            // keadaan jeda pengguna (atau jeda otomatis ON_STOP) dipertahankan, kalau tidak
            // gangguan jaringan di latar bisa menyalakan suara lagi padahal aplikasi tertutup.
            if (pertama) exo.playWhenReady = true
        } catch (e: Exception) {
            pesanGagal = "tangkap: " + e.toString()
            gagalTotal = true
            memuatAwal = false
        }
    }

    // daftarkan pemutar aktif untuk tombol putar/jeda di jendela PiP sistem
    DisposableEffect(exo) {
        KeadaanPip.pemutar = exo
        KeadaanPip.sedangMain = exo.playWhenReady
        onDispose { if (KeadaanPip.pemutar === exo) KeadaanPip.pemutar = null }
    }

    DisposableEffect(exo) {
        var dilepas = false
        val pendengar =
            object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    buffering = state == Player.STATE_BUFFERING
                    berakhir = state == Player.STATE_ENDED
                    if (state == Player.STATE_READY || state == Player.STATE_ENDED) memuatAwal = false
                    if (state == Player.STATE_READY) percobaan[0] = 0
                    if (state != Player.STATE_ENDED) sudahTamat = false
                    if (state == Player.STATE_ENDED && !sudahTamat) {
                        sudahTamat = true
                        aksiSelesai()
                    }
                }

                override fun onIsPlayingChanged(playing: Boolean) {
                    main = playing
                    KeadaanPip.sedangMain = playing
                }

                override fun onPlayWhenReadyChanged(
                    playWhenReady: Boolean,
                    reason: Int,
                ) {
                    ingin = playWhenReady
                }

                override fun onTracksChanged(tracks: androidx.media3.common.Tracks) {
                    val tinggi = ArrayList<Int>()
                    tracks.groups.forEach { g ->
                        if (g.type == androidx.media3.common.C.TRACK_TYPE_VIDEO) {
                            for (i in 0 until g.length) {
                                if (g.isTrackSupported(i)) {
                                    val h = g.getTrackFormat(i).height
                                    if (h > 0) tinggi.add(h)
                                }
                            }
                        }
                    }
                    daftarKualitas = tinggi.distinct().sortedDescending()
                    // Kunci kualitas lama menempel di trackSelectionParameters walau episode berganti.
                    // Dulu pilihan tersimpan hanya diterapkan bila overrides KOSONG, jadi pada episode
                    // berikutnya override basi (grup trek lama) menghalangi dan video diam-diam jalan
                    // Otomatis padahal menu menunjukkan 1080p. Sekarang yang dicek: apakah ada
                    // override yang cocok dengan grup trek episode INI.
                    val adaOverrideCocok =
                        tracks.groups.any { g ->
                            g.type == androidx.media3.common.C.TRACK_TYPE_VIDEO &&
                                exo.trackSelectionParameters.overrides.containsKey(g.mediaTrackGroup)
                        }
                    if (!adaOverrideCocok) {
                        if (kualitasPilih > 0 && kualitasPilih in daftarKualitas) {
                            terapkanKualitas(kualitasPilih)
                        } else if (exo.trackSelectionParameters.overrides.values
                                .any { it.type == androidx.media3.common.C.TRACK_TYPE_VIDEO }
                        ) {
                            // rendisi pilihan tak ada di episode ini: buang override basi, kembali Otomatis
                            terapkanKualitas(0)
                        }
                    }
                }

                override fun onVideoSizeChanged(videoSize: androidx.media3.common.VideoSize) {
                    tinggiAktif = videoSize.height
                }

                override fun onPlayerError(error: PlaybackException) {
                    // gangguan jaringan sesaat: coba lagi alamat yang sama (posisi dipertahankan)
                    // sebelum menyerah
                    // 4xx (selain 408/429) = alamatnya memang tidak ada / ditolak: diulang pun
                    // sama saja dan hanya membuang ~9 detik sebelum menyerah.
                    val kodeHttp = (error.cause as? androidx.media3.datasource.HttpDataSource.InvalidResponseCodeException)?.responseCode
                    val galatKlien = kodeHttp != null && kodeHttp in 400..499 && kodeHttp != 408 && kodeHttp != 429
                    val gangguanJaringan =
                        !galatKlien && (
                            error.errorCode == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED ||
                                error.errorCode == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT ||
                                error.errorCode == PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS
                        )
                    if (gangguanJaringan && percobaan[0] < 3) {
                        percobaan[0] += 1
                        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                            if (dilepas) return@postDelayed
                            try {
                                // prepare() saja: playWhenReady tetap sesuai pilihan pengguna
                                exo.prepare()
                            } catch (e: Exception) {
                            }
                        }, 1500L * percobaan[0])
                        return
                    }
                    // sumber memang mati (pintu putar kini satu alamat Pages — tidak ada
                    // pintu cadangan lagi): nyatakan gagal total
                    val sebab = error.errorCodeName + " | " + (error.cause?.toString() ?: error.message ?: "?")
                    pesanGagal = sebab
                    val tidakDikenal =
                        error.errorCode == PlaybackException.ERROR_CODE_PARSING_CONTAINER_UNSUPPORTED ||
                            error.cause is androidx.media3.exoplayer.source.UnrecognizedInputFormatException
                    if (tidakDikenal && !paksaHls) {
                        // format tidak terdeteksi dari alamat: coba lagi sebagai HLS sebelum menyerah
                        paksaHls = true
                    } else {
                        gagalTotal = true
                        memuatAwal = false
                    }
                }
            }
        exo.addListener(pendengar)
        onDispose {
            dilepas = true
            exo.removeListener(pendengar)
            simpanKecepatan()
            exo.release()
        }
    }

    // Jeda otomatis saat aplikasi tidak lagi tampil (Beranda tanpa PiP, atau jendela PiP ditutup),
    // supaya suara tidak terus berjalan di latar.
    DisposableEffect(exo) {
        val pemilik = (ctx as? android.app.Activity) as? androidx.lifecycle.LifecycleOwner
        val pengamat =
            androidx.lifecycle.LifecycleEventObserver { _, kejadian ->
                if (kejadian == androidx.lifecycle.Lifecycle.Event.ON_STOP) {
                    simpanKecepatan()
                    exo.pause()
                }
            }
        pemilik?.lifecycle?.addObserver(pengamat)
        onDispose { pemilik?.lifecycle?.removeObserver(pengamat) }
    }

    // Halaman putar sedang animasi keluar: suara/video berhenti seketika.
    LaunchedEffect(jeda) {
        if (jeda) {
            simpanKecepatan()
            exo.pause()
        }
    }

    // Polling posisi untuk bilah geser + pegangan progres
    LaunchedEffect(exo) {
        while (true) {
            val d = exo.duration
            durasi = if (d > 0) d.toInt() else 0
            // saat bilah digeser, waktu mengikuti jari, bukan posisi pemutar
            if (!seret) posisi = exo.currentPosition.toInt().coerceAtLeast(0)
            pegangan.posisi = posisi
            pegangan.durasi = durasi
            delay(500)
        }
    }

    // Sembunyikan kontrol sendiri saat sedang memutar
    LaunchedEffect(tampil, main, menuKecepatan, menuKualitas, seret) {
        if (tampil && main && !menuKecepatan && !menuKualitas && !seret) {
            delay(3500)
            tampil = false
        }
    }

    // Pemanasan episode berikutnya (sekali, hanya Wi-Fi/tidak berbayar)
    LaunchedEffect(jalurBerikut, jalurEp) {
        if (jalurBerikut == null || !jalurBerikut.startsWith("http")) return@LaunchedEffect
        var gagalPanas = 0
        while (gagalPanas < 3) {
            delay(5000)
            val hampirTamat = durasi > 0 && posisi.toLong() * 100 >= durasi.toLong() * 70
            if (hampirTamat && !buffering && exo.totalBufferedDuration >= 15_000 &&
                PemanasanVideo.jaringanAman(ctx)
            ) {
                // sebelumnya loop berhenti walau pemanasan gagal, jadi tidak pernah dicoba lagi
                val ok = withContext(Dispatchers.IO) { PemanasanVideo.panaskan(ctx, jalurBerikut) }
                if (ok) break
                gagalPanas++
            }
        }
    }

    fun lompat(arah: Int) {
        if (durasi <= 0) return
        if (!lompatAktif || arah != lompatArah) lompatTotal = 0
        lompatArah = arah
        lompatTotal += detikLompat
        lompatAktif = true
        val target = (exo.currentPosition + arah * detikLompat * 1000L).coerceIn(0L, durasi.toLong())
        sudahTamat = false
        exo.seekTo(target)
        posisi = target.toInt()
        lompatKunci += 1
    }
    // mode lompat berakhir bila tidak ada ketukan baru dalam 0,8 dtk
    LaunchedEffect(lompatKunci) {
        if (lompatKunci > 0) {
            delay(800)
            lompatAktif = false
        }
    }

    var pecahanGeser by remember { mutableStateOf(0f) }
    LaunchedEffect(posisi, durasi, seret) {
        if (!seret) pecahanGeser = if (durasi > 0) posisi.toFloat() / durasi else 0f
    }

    Box(
        modifier.background(Color.Black).onGloballyPositioned {
            KeadaanPip.rect = KeadaanPip.rectPip(it.boundsInWindow())
        },
    ) {
        AndroidView(
            factory = { c ->
                PlayerView(c).apply {
                    player = exo
                    useController = false
                    resizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT
                }
            },
            // lepas pemutar sebelum exo.release() supaya view tidak menyentuh pemutar yang sudah dilepas
            onRelease = { it.player = null },
            modifier = Modifier.fillMaxSize(),
        )
        // Lapisan ketuk: ketuk tunggal = tampil/sembunyi kontrol, ketuk dua kali di
        // kiri/kanan = mundur/maju 10 dtk (menumpuk), ketuk dua kali di tengah = putar/jeda.
        // Deteksi ganda dibuat manual (bukan onDoubleTap) agar ketuk tunggal tidak tertunda.
        Box(
            Modifier.fillMaxSize().pointerInput(modePip) {
                detectTapGestures { titik ->
                    val lebar = size.width.toFloat()
                    val zona =
                        when {
                            titik.x < lebar / 3f -> -1
                            titik.x > lebar * 2f / 3f -> 1
                            else -> 0
                        }
                    val sekarang = android.os.SystemClock.uptimeMillis()
                    if (lompatAktif && zona != 0) {
                        lompatTitik = titik
                        lompat(zona)
                    } else if (zona == zonaTerakhir && sekarang - ketukTerakhir < 300L) {
                        ketukTerakhir = 0L
                        zonaTerakhir = -9
                        tampil = false
                        menuKecepatan = false
                        menuKualitas = false
                        if (zona != 0) {
                            lompatTitik = titik
                            lompat(zona)
                        } else if (exo.playbackState == Player.STATE_ENDED) {
                            sudahTamat = false
                            exo.seekTo(0)
                            exo.play()
                        } else if (exo.playWhenReady) {
                            exo.pause()
                        } else {
                            exo.play()
                        }
                    } else {
                        ketukTerakhir = sekarang
                        zonaTerakhir = zona
                        tampil = !tampil
                        if (!tampil) {
                            menuKecepatan = false
                            menuKualitas = false
                        }
                    }
                }
            },
        )
        // Umpan balik lompat: setengah layar yang diketuk menyala + panah + "+20 detik"
        UmpanBalikLompat(
            aktif = lompatAktif && !gagalTotal && !modePip,
            arah = lompatArah,
            total = lompatTotal,
            kunci = lompatKunci,
            titik = lompatTitik,
            modifier = Modifier.matchParentSize(),
        )

        if (memuatAwal && !gagalTotal) {
            // poster episode meredup di belakang indikator, bukan layar hitam kosong
            if (urlPoster.isNotBlank()) {
                Gambar(urlPoster, Modifier.fillMaxSize().graphicsLayer { alpha = 0.35f })
                Box(
                    Modifier.fillMaxSize().background(
                        Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.35f), Color.Black.copy(alpha = 0.75f))),
                    ),
                )
            }
            Column(
                Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                androidx.compose.material3.LoadingIndicator(color = Zamrud, modifier = Modifier.size(if (modePip) 26.dp else 48.dp))
                if (!modePip) {
                    Spacer(Modifier.height(12.dp))
                    Text("Memuat video…", color = Color.White, fontSize = 14.sp)
                }
            }
        }

        if (gagalTotal) {
            Column(
                Modifier.align(Alignment.Center).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Sumber tidak bisa diputar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text("Periksa koneksi, lalu coba lagi.", color = Color.White.copy(alpha = 0.75f), fontSize = 13.sp)
                val bisaDebug = (ctx.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0
                if (bisaDebug && pesanGagal.isNotBlank()) {
                    Spacer(Modifier.height(6.dp))
                    Text(pesanGagal, color = Color.White.copy(alpha = 0.55f), fontSize = 10.sp)
                }
                Spacer(Modifier.height(14.dp))
                Row {
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Zamrud)
                            .clickable {
                                paksaHls = false
                                kunciMuat += 1
                                exo.playWhenReady = true
                            }.padding(horizontal = 18.dp, vertical = 9.dp),
                    ) { Text("Coba lagi", color = DiAtasZamrud, fontSize = 14.sp, fontWeight = FontWeight.Bold) }
                    Spacer(Modifier.width(10.dp))
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Kartu2)
                            .clickable { onKembali() }
                            .padding(horizontal = 18.dp, vertical = 9.dp),
                    ) { Text("Kembali", color = Teks, fontSize = 14.sp, fontWeight = FontWeight.Bold) }
                }
            }
        }

        if (buffering && !memuatAwal && !gagalTotal) {
            androidx.compose.material3.LoadingIndicator(
                color = Zamrud,
                modifier = Modifier.align(Alignment.Center).size(if (modePip) 26.dp else 44.dp),
            )
        }

        // PiP sistem: garis progres 2dp rata dasar, identik dengan kartu mini dalam aplikasi
        if (modePip && !gagalTotal) {
            Box(
                Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.White.copy(alpha = 0.25f)),
            ) {
                val frac = if (durasi > 0) (posisi.toFloat() / durasi).coerceIn(0f, 1f) else 0f
                Box(Modifier.fillMaxWidth(frac).fillMaxHeight().background(Zamrud))
            }
        }

        if (tampil && !gagalTotal && !modePip) {
            // Tombol putar/jeda tengah: disembunyikan selama memuat/buffering
            // (di tengah sudah ada indikator putar), supaya tidak bertumpuk.
            if (!buffering && !memuatAwal) {
                Box(
                    Modifier
                        .align(Alignment.Center)
                        .size(66.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.45f))
                        .clickable {
                            if (exo.playbackState == Player.STATE_ENDED) {
                                // sudah tamat: ulang dari awal
                                sudahTamat = false
                                exo.seekTo(0)
                                exo.play()
                            } else if (exo.playWhenReady) {
                                exo.pause()
                            } else {
                                exo.play()
                            }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Ikon(if (ingin && !berakhir) "jeda" else "putar", Modifier.size(32.dp), Color.White, terisi = true)
                }
            }
            // Bilah atas: kembali + judul
            Row(
                Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent)))
                    .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.35f))
                        .clickable { onKembali() },
                    contentAlignment = Alignment.Center,
                ) { Ikon("kembali", Modifier.size(20.dp), Color.White) }
                Spacer(Modifier.width(10.dp))
                Text(
                    judulTeks,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
            }
            // Bilah bawah: waktu + geser + kecepatan/layar penuh
            Column(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))))
                    .padding(start = 12.dp, end = 12.dp, top = 20.dp, bottom = 8.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(fmtWaktuExo(posisi), color = Color.White, fontSize = 12.sp)
                    // Bilah geser rancangan sendiri: area sentuh tinggi, seret
                    // mendatar untuk menggeser, ketuk untuk lompat.
                    BoxWithConstraints(
                        Modifier
                            .weight(1f)
                            .height(36.dp)
                            .padding(horizontal = 8.dp)
                            .pointerInput(durasi) {
                                detectTapGestures { titik ->
                                    if (durasi > 0 && size.width > 0) {
                                        val frac = (titik.x / size.width).coerceIn(0f, 1f)
                                        pecahanGeser = frac
                                        posisi = (frac * durasi).toInt()
                                        exo.seekTo((frac * durasi).toLong())
                                    }
                                }
                            }.pointerInput(durasi) {
                                detectHorizontalDragGestures(
                                    onDragStart = { awal ->
                                        seret = true
                                        if (size.width > 0 && durasi > 0) {
                                            val frac = (awal.x / size.width).coerceIn(0f, 1f)
                                            pecahanGeser = frac
                                            posisi = (frac * durasi).toInt()
                                        }
                                    },
                                    onDragEnd = {
                                        if (durasi > 0) exo.seekTo((pecahanGeser * durasi).toLong())
                                        seret = false
                                    },
                                    onDragCancel = { seret = false },
                                ) { change, _ ->
                                    change.consume()
                                    if (size.width > 0) {
                                        val frac = (change.position.x / size.width).coerceIn(0f, 1f)
                                        pecahanGeser = frac
                                        posisi = (frac * durasi).toInt()
                                    }
                                }
                            },
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(CircleShape)
                                .background(Color(0x55FFFFFF)),
                        ) {
                            Box(Modifier.fillMaxWidth(pecahanGeser).fillMaxHeight().background(Zamrud))
                        }
                        Box(
                            Modifier
                                .offset(x = maxWidth * pecahanGeser - 7.dp)
                                .size(14.dp)
                                .background(Zamrud, CircleShape),
                        )
                    }
                    Text(fmtWaktuExo(durasi), color = Color.White, fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .height(34.dp)
                            .clip(RoundedCornerShape(17.dp))
                            .background(Color.Black.copy(alpha = 0.5f))
                            .clickable {
                                menuKecepatan = !menuKecepatan
                                menuKualitas = false
                            }.padding(horizontal = 14.dp),
                        contentAlignment = Alignment.Center,
                    ) { Text(labelKecepatan(kecepatan), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                    if (daftarKualitas.size > 1) {
                        Spacer(Modifier.width(8.dp))
                        Box(
                            Modifier
                                .height(34.dp)
                                .clip(RoundedCornerShape(17.dp))
                                .background(Color.Black.copy(alpha = 0.5f))
                                .clickable {
                                    menuKualitas = !menuKualitas
                                    menuKecepatan = false
                                }.padding(horizontal = 14.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                when {
                                    kualitasAktif > 0 -> "${kualitasAktif}p"
                                    tinggiAktif > 0 -> "Auto · ${tinggiAktif}p"
                                    else -> "Auto"
                                },
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    Box(
                        Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .clickable { onTogglePenuh() },
                        contentAlignment = Alignment.Center,
                    ) { Ikon("layarpenuh", Modifier.size(19.dp), Color.White) }
                }
            }
            // Menu kualitas — jangkar bawah 8dp (bukan 104dp): kotak video potret
            // hanya ±202dp, jangkar lama menyisakan ±98dp sehingga baris 720p ke bawah
            // terpotong klip sudut bundar menu. Tinggi dibatasi + bisa digulir untuk berjaga.
            if (menuKualitas && daftarKualitas.size > 1) {
                Column(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 12.dp, bottom = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Kartu)
                        .heightIn(max = 188.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(6.dp),
                ) {
                    (listOf(0) + daftarKualitas).forEach { h ->
                        Text(
                            if (h == 0) "Otomatis" else "${h}p",
                            color = if (h == kualitasAktif) Zamrud else Teks,
                            fontSize = 14.sp,
                            modifier =
                                Modifier
                                    .clickable {
                                        kualitasPilih = h
                                        prefs(ctx).edit().putInt("kualitas", h).apply()
                                        terapkanKualitas(h)
                                        menuKualitas = false
                                    }.padding(horizontal = 16.dp, vertical = 6.dp),
                        )
                    }
                }
            }
            // Menu kecepatan — jangkar sama seperti menu kualitas (lihat catatan di atas).
            if (menuKecepatan) {
                Column(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 12.dp, bottom = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Kartu)
                        .heightIn(max = 188.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(6.dp),
                ) {
                    listOf(0.5f, 0.75f, 1f, 1.25f, 1.5f, 2f).forEach { k ->
                        Text(
                            labelKecepatan(k),
                            color = if (k == kecepatan) Zamrud else Teks,
                            fontSize = 14.sp,
                            modifier =
                                Modifier
                                    .clickable {
                                        kecepatan = k
                                        exo.setPlaybackSpeed(k)
                                        prefs(ctx).edit().putFloat("kecepatan", k).apply()
                                        menuKecepatan = false
                                    }.padding(horizontal = 16.dp, vertical = 6.dp),
                        )
                    }
                }
            }
        }
    }
}

// ================= Mini pemutar (PiP dalam aplikasi) =================
@Composable
@UnstableApi
private fun MiniPemutar(
    onBuka: (Judul, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val j = KeadaanMini.judul ?: return
    androidx.compose.runtime.key(KeadaanMini.versi) {
        MiniIsi(j, KeadaanMini.indeks, KeadaanMini.jalur, KeadaanMini.posisi, onBuka, modifier)
    }
}

@Composable
@UnstableApi
private fun MiniIsi(
    j: Judul,
    idx: Int,
    jalur: String,
    posAwal: Int,
    onBuka: (Judul, Int) -> Unit,
    modifier: Modifier,
) {
    val ctx = LocalContext.current
    val lingkup = rememberCoroutineScope()
    val kepadatan = LocalDensity.current
    var main by remember { mutableStateOf(true) }
    var buffering by remember { mutableStateOf(true) }
    var posisi by remember { mutableStateOf(posAwal) }
    var durasi by remember { mutableStateOf(0) }
    val paksaHls = remember { booleanArrayOf(false) }

    val exo =
        remember {
            val lewatCache =
                androidx.media3.datasource.cache.CacheDataSource
                    .Factory()
                    .setCache(CachePemutar.ambil(ctx))
                    .setUpstreamDataSourceFactory(CachePemutar.pabrikHttp())
                    .setFlags(androidx.media3.datasource.cache.CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
            val sumberData =
                androidx.media3.datasource.DefaultDataSource
                    .Factory(ctx, lewatCache)
            ExoPlayer
                .Builder(ctx)
                .setMediaSourceFactory(
                    androidx.media3.exoplayer.source
                        .DefaultMediaSourceFactory(sumberData),
                ).setAudioAttributes(
                    androidx.media3.common.AudioAttributes
                        .Builder()
                        .setUsage(androidx.media3.common.C.USAGE_MEDIA)
                        .setContentType(androidx.media3.common.C.AUDIO_CONTENT_TYPE_MOVIE)
                        .build(),
                    true,
                ).setHandleAudioBecomingNoisy(true)
                .build()
                .apply { setPlaybackSpeed(prefs(ctx).getFloat("kecepatan", 1f)) }
        }

    fun muat(
        paksa: Boolean,
        mulaiDari: Long,
    ) {
        val uri = if (jalur.startsWith("lokal:")) "file://" + jalur.removePrefix("lokal:") else jalur
        val butir =
            MediaItem
                .Builder()
                .setUri(uri)
                .apply {
                    if (paksa || uri.contains(".m3u8", ignoreCase = true)) {
                        setMimeType(androidx.media3.common.MimeTypes.APPLICATION_M3U8)
                    }
                }.build()
        exo.setMediaItem(butir)
        exo.prepare()
        if (mulaiDari > 0) exo.seekTo(mulaiDari)
    }

    fun simpan() {
        try {
            val d = exo.duration
            if (d > 0) {
                val pos = exo.currentPosition.toInt().coerceAtLeast(0)
                simpanProgres(ctx, Progres(j.id, idx, pos, d.toInt()))
                simpanPosisiEp(ctx, j.id, idx, pos, d.toInt())
            }
        } catch (e: Exception) {
            // abaikan
        }
    }

    LaunchedEffect(exo) {
        muat(false, posAwal.toLong())
        exo.playWhenReady = true
    }

    DisposableEffect(exo) {
        val pendengar =
            object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    buffering = state == Player.STATE_BUFFERING
                    if (state == Player.STATE_ENDED) KeadaanMini.tutup()
                }

                override fun onPlayWhenReadyChanged(
                    playWhenReady: Boolean,
                    reason: Int,
                ) {
                    main = playWhenReady
                    KeadaanPip.sedangMain = playWhenReady
                }

                override fun onPlayerError(error: PlaybackException) {
                    if (!paksaHls[0] && error.cause is androidx.media3.exoplayer.source.UnrecognizedInputFormatException) {
                        paksaHls[0] = true
                        muat(true, exo.currentPosition)
                    } else {
                        KeadaanMini.tutup()
                    }
                }
            }
        exo.addListener(pendengar)
        // jeda otomatis saat aplikasi tidak tampil (sama seperti pemutar utama)
        val pemilik = (ctx as? android.app.Activity) as? androidx.lifecycle.LifecycleOwner
        val pengamat =
            androidx.lifecycle.LifecycleEventObserver { _, kejadian ->
                if (kejadian == androidx.lifecycle.Lifecycle.Event.ON_STOP) {
                    simpan()
                    exo.pause()
                }
            }
        pemilik?.lifecycle?.addObserver(pengamat)
        onDispose {
            pemilik?.lifecycle?.removeObserver(pengamat)
            exo.removeListener(pendengar)
            simpan()
            exo.release()
        }
    }

    // daftarkan pemutar aktif untuk tombol putar/jeda di jendela PiP sistem
    DisposableEffect(exo) {
        KeadaanPip.pemutar = exo
        KeadaanPip.sedangMain = exo.playWhenReady
        onDispose { if (KeadaanPip.pemutar === exo) KeadaanPip.pemutar = null }
    }

    LaunchedEffect(exo) {
        var tik = 0
        while (true) {
            val d = exo.duration
            durasi = if (d > 0) d.toInt() else 0
            posisi = exo.currentPosition.toInt().coerceAtLeast(0)
            tik += 1
            if (tik % 20 == 0) simpan()
            delay(500)
        }
    }

    // Tombol Beranda saat mini tampil: jendela kecil (PiP) sistem hanya menampilkan videonya
    // (tanpa kontrol), sama seperti PiP dari halaman putar penuh. Pemutar tetap yang sama,
    // jadi video lanjut tanpa putus.
    if (KeadaanPip.sedangPip) {
        Box(modifier.fillMaxSize().background(Color.Black)) {
            AndroidView(
                factory = { c ->
                    PlayerView(c).apply {
                        player = exo
                        useController = false
                        resizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT
                    }
                },
                onRelease = { it.player = null },
                modifier = Modifier.fillMaxSize(),
            )
            if (buffering) {
                androidx.compose.material3.LoadingIndicator(
                    color = Zamrud,
                    modifier = Modifier.align(Alignment.Center).size(26.dp),
                )
            }
            Box(
                Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.White.copy(alpha = 0.25f)),
            ) {
                val frac = if (durasi > 0) (posisi.toFloat() / durasi).coerceIn(0f, 1f) else 0f
                Box(Modifier.fillMaxWidth(frac).fillMaxHeight().background(Zamrud))
            }
        }
        return
    }
    val insetBawah = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val insetAtas = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    BoxWithConstraints(modifier.fillMaxSize()) {
        val lebarKartu = 176.dp
        val tinggiKartu = 99.dp
        val tepi = 14.dp
        val jarakBawah = insetBawah + 96.dp // di atas kapsul navigasi
        val px = { d: androidx.compose.ui.unit.Dp -> with(kepadatan) { d.toPx() } }
        // batas geser: kiri-kanan dan atas-bawah, supaya kartu tidak keluar layar
        val lebih = px(90.dp) // geser melewati tepi sejauh ini = tutup
        val minX = -(px(maxWidth) - px(lebarKartu) - 2f * px(tepi))
        // batas atas: tepat di bawah header Beranda (logo, cari, lonceng), jadi tidak menutupinya
        val ruangHeader = insetAtas + 64.dp
        val minY =
            -(px(maxHeight) - px(tinggiKartu) - px(jarakBawah) - px(ruangHeader))
                .coerceAtLeast(0f)
        // batas bawah: posisi awal (tepat di atas kapsul navigasi), tidak boleh menutupinya
        val maksY = 0f
        val gx =
            remember {
                androidx.compose.animation.core
                    .Animatable(if (KeadaanMini.diKanan) 0f else minX)
            }
        val gy =
            remember {
                androidx.compose.animation.core
                    .Animatable(if (KeadaanMini.diBawah) 0f else minY)
            }
        // rotasi / split-screen / kembali dari PiP sistem: tempelkan ulang ke sudut tersimpan
        LaunchedEffect(minX, minY) {
            gx.snapTo(if (KeadaanMini.diKanan) 0f else minX)
            gy.snapTo(if (KeadaanMini.diBawah) 0f else minY)
        }
        val masuk =
            remember {
                androidx.compose.animation.core
                    .Animatable(0f)
            }
        val hilang =
            remember {
                androidx.compose.animation.core
                    .Animatable(0f)
            }
        LaunchedEffect(Unit) {
            masuk.animateTo(
                1f,
                androidx.compose.animation.core
                    .spring(dampingRatio = 0.7f, stiffness = 380f),
            )
        }
        // Kontrol (putar/jeda, tutup, judul) tampil 2,5 dtk saat kartu muncul atau disentuh, lalu
        // hilang supaya videonya bersih. Ketuk saat kontrol tersembunyi = tampilkan kontrol;
        // ketuk saat kontrol tampil = buka pemutar penuh.
        var kontrol by remember { mutableStateOf(true) }
        var kunciKontrol by remember { mutableStateOf(0) }
        LaunchedEffect(kunciKontrol, kontrol) {
            if (kontrol) {
                delay(2500)
                kontrol = false
            }
        }
        val alfaKontrol by androidx.compose.animation.core.animateFloatAsState(
            targetValue = if (kontrol) 1f else 0f,
            animationSpec =
                androidx.compose.animation.core
                    .tween(250),
            label = "miniKontrol",
        )
        val bentuk = RoundedCornerShape(16.dp)
        val nomor = j.episodes.getOrNull(idx)?.number ?: (idx + 1)
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(end = tepi, bottom = jarakBawah)
                .offset {
                    androidx.compose.ui.unit
                        .IntOffset(gx.value.roundToInt(), gy.value.roundToInt())
                }.graphicsLayer {
                    val skala = 0.8f + 0.2f * masuk.value
                    scaleX = skala
                    scaleY = skala
                    // memudar saat diseret melewati tepi layar (tanda akan ditutup)
                    val lewat =
                        if (gx.value > 0f) {
                            gx.value
                        } else if (gx.value < minX) {
                            minX - gx.value
                        } else {
                            0f
                        }
                    this.alpha = masuk.value.coerceIn(0f, 1f) *
                        (1f - minOf(lewat, lebih) / (lebih * 1.45f)) * (1f - hilang.value)
                    transformOrigin =
                        androidx.compose.ui.graphics
                            .TransformOrigin(1f, 1f)
                }.size(lebarKartu, tinggiKartu)
                .onGloballyPositioned { KeadaanPip.rect = KeadaanPip.rectPip(it.boundsInWindow()) }
                .shadow(14.dp, bentuk)
                .clip(bentuk)
                .background(Color.Black)
                .border(1.dp, Color.White.copy(alpha = 0.14f), bentuk)
                .pointerInput(Unit) {
                    detectTapGestures {
                        if (kontrol) {
                            simpan()
                            onBuka(j, idx)
                        } else {
                            kontrol = true
                            kunciKontrol += 1
                        }
                    }
                }.pointerInput(minX, minY, maksY) {
                    detectDragGestures(
                        onDragStart = {
                            kontrol = true
                            kunciKontrol += 1
                        },
                        onDragEnd = {
                            lingkup.launch {
                                val tutupKanan = gx.value > px(55.dp)
                                val tutupKiri = gx.value < minX - px(55.dp)
                                if (tutupKanan || tutupKiri) {
                                    // dilempar ke luar layar sambil memudar, lalu ditutup
                                    launch {
                                        gx.animateTo(
                                            gx.value + (if (tutupKanan) px(160.dp) else -px(160.dp)),
                                            androidx.compose.animation.core
                                                .tween(300),
                                        )
                                    }
                                    hilang.animateTo(
                                        1f,
                                        androidx.compose.animation.core
                                            .tween(300),
                                    )
                                    KeadaanMini.tutup()
                                } else {
                                    // menempel ke sudut terdekat (empat sudut), lalu diingat
                                    KeadaanMini.diKanan = gx.value >= minX / 2f
                                    KeadaanMini.diBawah = gy.value >= minY / 2f
                                    val pegas =
                                        androidx.compose.animation.core
                                            .spring<Float>(dampingRatio = 0.8f, stiffness = 400f)
                                    launch { gy.animateTo(if (KeadaanMini.diBawah) 0f else minY, pegas) }
                                    gx.animateTo(if (KeadaanMini.diKanan) 0f else minX, pegas)
                                }
                            }
                        },
                    ) { change, geser ->
                        change.consume()
                        lingkup.launch {
                            gx.snapTo((gx.value + geser.x).coerceIn(minX - lebih, lebih))
                            gy.snapTo((gy.value + geser.y).coerceIn(minY, maksY))
                        }
                    }
                },
        ) {
            AndroidView(
                factory = { c ->
                    PlayerView(c).apply {
                        player = exo
                        useController = false
                        resizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT
                    }
                },
                onRelease = { it.player = null },
                modifier = Modifier.fillMaxSize(),
            )
            // scrim atas/bawah: hanya terlihat bersama kontrol
            Box(
                Modifier.fillMaxSize().graphicsLayer { this.alpha = alfaKontrol }.background(
                    Brush.verticalGradient(
                        0f to Color.Black.copy(alpha = 0.5f),
                        0.42f to Color.Transparent,
                        1f to Color.Black.copy(alpha = 0.7f),
                    ),
                ),
            )
            if (buffering) {
                androidx.compose.material3.LoadingIndicator(
                    color = Zamrud,
                    modifier = Modifier.align(Alignment.Center).size(26.dp),
                )
            }
            // tutup (X) — area sentuh 44dp, tampilan kaca tetap 26dp (putih transparan + garis tepi tipis)
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .size(44.dp)
                    .clip(CircleShape)
                    .clickable(enabled = kontrol) { KeadaanMini.tutup() },
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    Modifier
                        .size(26.dp)
                        .graphicsLayer { this.alpha = alfaKontrol }
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .border(1.dp, Color.White.copy(alpha = 0.22f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Canvas(Modifier.size(10.dp)) {
                        val tebal = size.width * 0.26f
                        val ujung = androidx.compose.ui.graphics.StrokeCap.Round
                        drawLine(Color.White, Offset(0f, 0f), Offset(size.width, size.height), strokeWidth = tebal, cap = ujung)
                        drawLine(Color.White, Offset(size.width, 0f), Offset(0f, size.height), strokeWidth = tebal, cap = ujung)
                    }
                }
            }
            // putar / jeda: ikon berganti dengan animasi (mengecil + berputar, yang lain muncul membal)
            val pj by androidx.compose.animation.core.animateFloatAsState(
                targetValue = if (main) 1f else 0f,
                animationSpec =
                    androidx.compose.animation.core
                        .spring(dampingRatio = 0.55f, stiffness = 500f),
                label = "miniPutarJeda",
            )
            Box(
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 1.dp, bottom = 8.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .clickable(enabled = kontrol) {
                        kunciKontrol += 1
                        if (exo.playWhenReady) exo.pause() else exo.play()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    Modifier
                        .size(32.dp)
                        .graphicsLayer { this.alpha = alfaKontrol }
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .border(1.dp, Color.White.copy(alpha = 0.22f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    // ikon jeda (dua batang)
                    Canvas(
                        Modifier.size(14.dp).graphicsLayer {
                            val k = pj
                            this.alpha = k.coerceIn(0f, 1f)
                            scaleX = 0.3f + 0.7f * k
                            scaleY = 0.3f + 0.7f * k
                            rotationZ = (1f - k) * 90f
                        },
                    ) {
                        val lb = size.width * 3f / 14f
                        drawRoundRect(
                            Color.White,
                            Offset(size.width * 2.5f / 14f, size.height * 1.5f / 14f),
                            Size(lb, size.height * 11f / 14f),
                            CornerRadius(lb / 3f),
                        )
                        drawRoundRect(
                            Color.White,
                            Offset(size.width * 8.5f / 14f, size.height * 1.5f / 14f),
                            Size(lb, size.height * 11f / 14f),
                            CornerRadius(lb / 3f),
                        )
                    }
                    // ikon putar (segitiga)
                    Canvas(
                        Modifier.size(14.dp).graphicsLayer {
                            val k = 1f - pj
                            this.alpha = k.coerceIn(0f, 1f)
                            scaleX = 0.3f + 0.7f * k
                            scaleY = 0.3f + 0.7f * k
                            rotationZ = -(1f - k) * 90f
                        },
                    ) {
                        val tr = Path()
                        tr.moveTo(size.width * 3.5f / 14f, size.height * 1.8f / 14f)
                        tr.lineTo(size.width * 11.5f / 14f, size.height * 7f / 14f)
                        tr.lineTo(size.width * 3.5f / 14f, size.height * 12.2f / 14f)
                        tr.close()
                        drawPath(tr, Color.White, style = Fill)
                    }
                }
            }
            // judul: pil kaca gelap di samping tombol putar
            Text(
                "${j.judul} · Ep $nomor",
                color = Color.White,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier =
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 46.dp, end = 9.dp, bottom = 19.dp)
                        .graphicsLayer { this.alpha = alfaKontrol }
                        .clip(RoundedCornerShape(50))
                        .background(Color.Black.copy(alpha = 0.32f))
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
            )
            // progres: garis rata dasar kartu (ikut sudut membulat lewat clip kartu);
            // 2dp saat kontrol tersembunyi, 4dp saat tampil, supaya subtitle video tidak tertimpa
            val tinggiProgres by androidx.compose.animation.core.animateDpAsState(
                targetValue = if (kontrol) 4.dp else 2.dp,
                animationSpec =
                    androidx.compose.animation.core
                        .tween(200),
                label = "miniProgres",
            )
            Box(
                Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(tinggiProgres)
                    .background(Color.White.copy(alpha = 0.25f)),
            ) {
                val frac = if (durasi > 0) (posisi.toFloat() / durasi).coerceIn(0f, 1f) else 0f
                Box(Modifier.fillMaxWidth(frac).fillMaxHeight().background(Zamrud))
            }
        }
    }
}

// ================= Akar aplikasi =================
@Composable
@UnstableApi
fun Aplikasi() {
    val ctx = LocalContext.current
    LaunchedEffect(Unit) {
        KonfigPabrik.berkas = java.io.File(ctx.applicationContext.filesDir, "pabrik_cache.json")
        KatalogSumber.muat()
    }
    // Kabar episode baru: tiap daftar katalog selesai dimuat atau diperbarui,
    // bandingkan jumlah episode seluruh judul dengan potret terakhir.
    // (Saat aplikasi tertutup, PemeriksaKabarWorker yang melakukan hal yang sama.)
    LaunchedEffect(KatalogSumber.daftar) {
        if (KatalogSumber.siap && KatalogSumber.daftar.isNotEmpty()) {
            withContext(Dispatchers.IO) { periksaKabarEpisode(ctx) }
        }
    }
    // Pramuat ke cache disk: banner hero + poster baris pertama diunduh begitu
    // daftar tersedia, jadi saat digulir/ditampilkan sudah ada di disk.
    LaunchedEffect(KatalogSumber.daftar) {
        val d = KatalogSumber.daftar
        if (d.isEmpty()) return@LaunchedEffect
        val muatan =
            d.filter { it.jumlahEpisode > 0 }.take(5).map { it.banner } +
                KatalogSumber.terbaru.take(12).map { it.poster }
        val pemuat = SingletonImageLoader.get(ctx)
        for (u in muatan) {
            if (u.isBlank()) continue
            pemuat.enqueue(
                ImageRequest
                    .Builder(ctx)
                    .data(u)
                    .httpHeaders(NetworkHeaders.Builder().add("User-Agent", "Anime.id Android").build())
                    .build(),
            )
        }
    }
    // Pembaruan instan: daftar sumber diperiksa ulang tiap aplikasi kembali ke
    // depan (termasuk saat baru dibuka), BUKAN berkala 5 menit. Pemeriksaan
    // memakai ETag, jadi bila tidak ada perubahan cukup jawaban 304 tanpa unduh.
    // muat() sendiri menolak jalan ganda dan hanya menukar daftar bila berhasil.
    val lingkupSegar = rememberCoroutineScope()
    DisposableEffect(Unit) {
        val pemilik = (ctx as? android.app.Activity) as? androidx.lifecycle.LifecycleOwner
        val pengamat =
            androidx.lifecycle.LifecycleEventObserver { _, kejadian ->
                if (kejadian == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                    lingkupSegar.launch { KatalogSumber.muat() }
                }
            }
        pemilik?.lifecycle?.addObserver(pengamat)
        onDispose { pemilik?.lifecycle?.removeObserver(pengamat) }
    }
    var tab by rememberSaveable { mutableStateOf(0) }
    var halamanAkunBuka by remember { mutableStateOf(false) }
    val penyimpanTab = rememberSaveableStateHolder()
    // Detail/pemutar disimpan sebagai id (objek Judul tidak bisa disimpan) supaya
    // pulih bila sistem mematikan proses saat aplikasi di latar.
    var detailId by rememberSaveable { mutableStateOf<String?>(null) }
    var putarId by rememberSaveable { mutableStateOf<String?>(null) }
    var putarIdx by rememberSaveable { mutableStateOf(0) }
    var pulih by remember { mutableStateOf(detailId == null && putarId == null) }
    var detail by remember { mutableStateOf<Judul?>(null) }
    var memuatEp by remember { mutableStateOf(false) }
    var putar by remember { mutableStateOf<Pair<Judul, Int>?>(null) }
    val lingkupAplikasi = rememberCoroutineScope()
    // bersihkan sisa berkas unduhan yang tidak selesai (.part) dari sesi sebelumnya
    LaunchedEffect(Unit) { withContext(Dispatchers.IO) { Pengunduh.bersihkanSisa(ctx) } }
    var favorit by remember { mutableStateOf(bacaFavorit(ctx)) }
    var akun by remember {
        mutableStateOf(
            prefs(ctx).getString("akun_email", null)?.let { email ->
                AkunGoogle(
                    prefs(ctx).getString("akun_nama", "") ?: "",
                    email,
                    prefs(ctx).getString("akun_foto", "") ?: "",
                )
            },
        )
    }

    // Sinkron akun: tiap keadaan masuk berubah (termasuk pulih dari
    // penyimpanan saat aplikasi dibuka), gabung paket Firestore ke lokal
    // lalu segarkan keadaan favorit yang tampil.
    LaunchedEffect(akun) {
        if (akun != null) SinkronAkun.muatGabung(ctx) { favorit = bacaFavorit(ctx) }
    }

    val haptik = androidx.compose.ui.platform.LocalHapticFeedback.current
    val haze = remember { HazeState() }

    fun toggleFav(j: Judul) {
        haptik.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
        favorit = if (favorit.contains(j.id)) favorit - j.id else favorit + j.id
        simpanFavorit(ctx, favorit)
    }

    // Buka detail: halaman langsung terbuka dari data indeks, lalu daftar
    // episode diisi dari berkas judul di etalase pabrik (alamat putar HLS).
    // Bila berkas judul gagal diambil, halaman tetap tampil dengan data
    // indeks apa adanya.
    val tokenNav = remember { intArrayOf(0) }
    val sedangPramuat = remember { HashSet<String>() }
    androidx.compose.runtime.DisposableEffect(Unit) {
        PramuatDetail.aksi = { j ->
            if (j.banner.isNotBlank()) {
                SingletonImageLoader.get(ctx).enqueue(
                    ImageRequest
                        .Builder(ctx)
                        .data(j.banner)
                        .httpHeaders(NetworkHeaders.Builder().add("User-Agent", "Anime.id Android").build())
                        .build(),
                )
            }
            if (j.episodes.isEmpty() && sedangPramuat.add(j.id)) {
                lingkupAplikasi.launch {
                    try {
                        KatalogSumber.muatDetail(j)
                    } finally {
                        sedangPramuat.remove(j.id)
                    }
                }
            }
        }
        onDispose { PramuatDetail.aksi = null }
    }

    fun bukaDetail(j0: Judul) {
        // bila pramuat sudah selesai, pakai versi katalog yang sudah memuat episode
        val j =
            (KatalogSumber.daftar + KatalogSumber.terbaru)
                .firstOrNull { it.id == j0.id && it.episodes.isNotEmpty() } ?: j0
        tokenNav[0]++
        pulih = true
        detail = j
        if (j.episodes.isEmpty()) {
            memuatEp = true
            lingkupAplikasi.launch {
                val lengkap = KatalogSumber.muatDetail(j)
                if (lengkap != null && detail?.id == j.id) {
                    detail = lengkap
                }
                // hanya permintaan terakhir yang boleh mematikan penanda muat
                if (detail?.id == j.id || detail == null) memuatEp = false
            }
        } else {
            memuatEp = false
        }
    }

    // Putar dari Beranda ("Lanjut Menonton", tombol Putar hero): entri indeks
    // belum membawa daftar episode, jadi muat berkas judulnya dulu. Bila gagal
    // diambil, buka halaman detail supaya pengguna tidak melihat layar kosong.
    fun mulaiPutar(
        j: Judul,
        idx: Int,
    ) {
        val token = ++tokenNav[0]
        pulih = true
        if (j.episodes.isNotEmpty()) {
            putar = j to idx
        } else {
            lingkupAplikasi.launch {
                val lengkap = KatalogSumber.muatDetail(j)
                // pengguna sudah pindah halaman selama memuat -> jangan paksa buka pemutar/detail
                if (tokenNav[0] != token) return@launch
                if (lengkap != null && lengkap.episodes.isNotEmpty()) {
                    putar = lengkap to idx
                } else {
                    bukaDetail(j)
                }
            }
        }
    }

    // Tujuan dari notifikasi: tunggu katalog siap, lalu buka Detail judulnya.
    LaunchedEffect(KeadaanBuka.id, KatalogSumber.daftar, KatalogSumber.terbaru) {
        val tujuan = KeadaanBuka.id
        if (tujuan != null && KatalogSumber.daftar.isNotEmpty()) {
            val j = (KatalogSumber.daftar + KatalogSumber.terbaru).firstOrNull { it.id == tujuan }
            KeadaanBuka.id = null
            if (j != null) {
                putar = null
                tab = 0
                bukaDetail(j)
            }
        }
    }
    LaunchedEffect(detail, pulih) { if (pulih) detailId = detail?.id }
    LaunchedEffect(putar, pulih) {
        if (pulih) {
            putarId = putar?.first?.id
            putarIdx = putar?.second ?: 0
        }
    }
    LaunchedEffect(KatalogSumber.daftar, KatalogSumber.terbaru) {
        if (!pulih && KatalogSumber.daftar.isNotEmpty()) {
            val semuaJudul = KatalogSumber.daftar + KatalogSumber.terbaru
            val dj = detailId?.let { id -> semuaJudul.firstOrNull { it.id == id } }
            val pj = putarId?.let { id -> semuaJudul.firstOrNull { it.id == id } }
            if (dj != null) bukaDetail(dj)
            if (pj != null) mulaiPutar(pj, putarIdx)
            pulih = true
        }
    }

    // Membuka pemutar penuh (dari mana pun) menutup mini pemutar; videonya pindah ke halaman penuh.
    LaunchedEffect(putar) { if (putar != null) KeadaanMini.tutup() }

    Box(Modifier.fillMaxSize().background(Latar)) {
        AnimatedContent(
            targetState = Nav(putar, detail),
            contentKey = { it.jenis() },
            transitionSpec = {
                val dari = initialState.jenis()
                val ke = targetState.jenis()
                if (ke == 2 || dari == 2) {
                    // Detail <-> Pemutar: pemutar membesar halus dari tengah / menyusut kembali.
                    if (ke == 2) {
                        ContentTransform(
                            targetContentEnter =
                                fadeIn(tween(300, easing = LinearOutSlowInEasing)) +
                                    scaleIn(initialScale = 0.94f, animationSpec = tween(360, easing = FastOutSlowInEasing)),
                            initialContentExit = fadeOut(tween(200, easing = FastOutSlowInEasing)),
                            targetContentZIndex = 1f,
                        )
                    } else {
                        ContentTransform(
                            targetContentEnter = fadeIn(tween(300, delayMillis = 60, easing = LinearOutSlowInEasing)),
                            initialContentExit =
                                fadeOut(tween(220, easing = FastOutSlowInEasing)) +
                                    scaleOut(targetScale = 0.94f, animationSpec = tween(280, easing = FastOutSlowInEasing)),
                            targetContentZIndex = -1f,
                        )
                    }
                } else if (ke > dari) {
                    // Beranda -> Detail: tanpa geser. Detail muncul memudar sambil
                    // membesar tipis, Beranda memudar di bawahnya.
                    ContentTransform(
                        targetContentEnter =
                            fadeIn(tween(280, easing = LinearOutSlowInEasing)) +
                                scaleIn(initialScale = 0.96f, animationSpec = tween(320, easing = FastOutSlowInEasing)),
                        initialContentExit = fadeOut(tween(200, easing = FastOutSlowInEasing)),
                        targetContentZIndex = 1f,
                    )
                } else {
                    // Detail -> Beranda: kebalikannya; Detail memudar dan menyusut tipis.
                    ContentTransform(
                        targetContentEnter = fadeIn(tween(260, easing = LinearOutSlowInEasing)),
                        initialContentExit =
                            fadeOut(tween(220, easing = FastOutSlowInEasing)) +
                                scaleOut(targetScale = 0.96f, animationSpec = tween(260, easing = FastOutSlowInEasing)),
                        targetContentZIndex = -1f,
                    )
                }
            },
            label = "navigasi",
        ) { n ->
            val p = n.putar
            val d = n.detail
            Box(Modifier.fillMaxSize().background(Latar)) {
                // Selama transisi berjalan, halaman yang masuk maupun yang keluar tidak boleh diketuk
                // (mencegah ketukan ganda membuka detail lain di tengah animasi), dan halaman yang
                // sedang keluar ditandai supaya bisa menghentikan pemutar/penahan layar lebih dulu.
                val sibukNav = transition.isRunning
                val keluarNav = transition.targetState == EnterExitState.PostExit
                when {
                    p != null -> {
                        androidx.compose.runtime.key(p.first.id, p.second) {
                            LayarPutar(
                                j = p.first,
                                indeksEp = p.second,
                                favorit = favorit.contains(p.first.id),
                                akun = akun,
                                onToggleFav = { toggleFav(p.first) },
                                onKembali = { putar = null },
                                onPutarEp = { idx -> putar = p.first to idx },
                                keluar = keluarNav,
                            )
                        }
                    }
                    d != null -> {
                        LayarDetail(
                            j = d,
                            favorit = favorit.contains(d.id),
                            onToggleFav = { toggleFav(d) },
                            onKembali = {
                                tokenNav[0]++
                                detail = null
                            },
                            onPutar = { idx -> putar = d to idx },
                            memuatEpisode = memuatEp,
                            onCobaLagiEpisode = { bukaDetail(d) },
                        )
                    }
                    else -> {
                        // isi tab memenuhi layar; kapsul navbar melayang di atasnya dengan scrim pemudar
                        // Gaya aplikasi populer: kembali dari tab lain menuju Beranda dulu, baru keluar
                        BackHandler(enabled = tab != 0 && !sibukNav) { tab = 0 }
                        Box(Modifier.fillMaxSize()) {
                            Box(Modifier.fillMaxSize().hazeSource(haze)) {
                                // tiap tab menyimpan state-nya (posisi gulir, halaman penuh, pencarian)
                                // sehingga kembali dari Detail mendarat di tempat yang sama
                                AnimatedContent(
                                    targetState = tab,
                                    transitionSpec = {
                                        // pudar silang saja, tanpa geser
                                        fadeIn(tween(200, delayMillis = 40, easing = LinearOutSlowInEasing)) togetherWith
                                            fadeOut(tween(120, easing = FastOutSlowInEasing))
                                    },
                                    label = "tab",
                                ) { tabSasaran ->
                                    Box(Modifier.fillMaxSize().background(Latar)) {
                                        // tab yang sedang keluar tidak boleh menerima ketukan
                                        val tabKeluar = transition.targetState == EnterExitState.PostExit
                                        penyimpanTab.SaveableStateProvider(tabSasaran) {
                                            when (tabSasaran) {
                                                0 ->
                                                    LayarBeranda(
                                                        onDetail = { bukaDetail(it) },
                                                        onPutar = { j, i -> mulaiPutar(j, i) },
                                                        aktif = !tabKeluar,
                                                        favorit = favorit,
                                                        onToggleFav = { toggleFav(it) },
                                                    )
                                                1 ->
                                                    LayarJadwal(
                                                        favorit = favorit,
                                                        onToggleFav = { toggleFav(it) },
                                                        onDetail = { bukaDetail(it) },
                                                    )
                                                2 ->
                                                    LayarFavorit(favorit = favorit, onDetail = {
                                                        bukaDetail(
                                                            it,
                                                        )
                                                    }, onHapus = { toggleFav(it) }, onJelajah = {
                                                        tab =
                                                            0
                                                    })
                                                3 -> LayarUnduhan(onPutar = { j, i -> mulaiPutar(j, i) })
                                                else ->
                                                    LayarAkun(
                                                        akun = akun,
                                                        onHalamanBuka = { halamanAkunBuka = it },
                                                        onMasukBerhasil = { a ->
                                                            akun = a
                                                            prefs(ctx)
                                                                .edit()
                                                                .putString("akun_nama", a.nama)
                                                                .putString("akun_email", a.email)
                                                                .putString("akun_foto", a.foto)
                                                                .remove("masuk")
                                                                .apply()
                                                        },
                                                        onKeluar = {
                                                            akun = null
                                                            SinkronAkun.keluarFirebase()
                                                            prefs(ctx)
                                                                .edit()
                                                                .remove("akun_nama")
                                                                .remove("akun_email")
                                                                .remove("akun_foto")
                                                                .remove("masuk")
                                                                .apply()
                                                        },
                                                    )
                                            }
                                        }
                                        if (tabKeluar) {
                                            Box(
                                                Modifier.fillMaxSize().pointerInput(Unit) {
                                                    awaitPointerEventScope {
                                                        while (true) awaitPointerEvent().changes.forEach { it.consume() }
                                                    }
                                                },
                                            )
                                        }
                                    }
                                }
                            }
                            // scrim: poster memudar ke warna latar, bukan terpotong lurus di atas navbar
                            val insetBawah = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                            // halaman turunan menu Akun memenuhi layar: navbar dan scrim disembunyikan
                            val sembunyiBar = tab == 4 && halamanAkunBuka
                            if (!sembunyiBar) {
                                Box(
                                    Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .height(84.dp + insetBawah)
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color.Transparent, Latar.copy(alpha = 0.55f)),
                                            ),
                                        ),
                                )
                            }
                            if (!sembunyiBar) {
                                Box(Modifier.align(Alignment.BottomCenter)) {
                                    BilahBawah(tab, haze) {
                                        if (it !=
                                            tab
                                        ) {
                                            haptik.performHapticFeedback(
                                                androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove,
                                            )
                                        }
                                        tokenNav[0]++
                                        tab = it
                                    }
                                }
                            }
                        }
                    }
                }
                if (sibukNav) {
                    Box(
                        Modifier.fillMaxSize().pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) awaitPointerEvent().changes.forEach { it.consume() }
                            }
                        },
                    )
                }
            }
        }
        // Mini pemutar melayang di atas Beranda / Detail / tab mana pun
        if (putar == null && KeadaanMini.judul != null) {
            MiniPemutar(onBuka = { jd, i -> mulaiPutar(jd, i) })
        }
    }
}

class MainActivity : ComponentActivity() {
    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        // Layar pembuka gelap: ditahan sampai katalog siap (maks. 1 detik) lalu
        // dilepas tanpa animasi keluar bawaan sistem -> tidak ada kilatan putih.
        val splash = installSplashScreen()
        val mulai = android.os.SystemClock.uptimeMillis()
        splash.setKeepOnScreenCondition {
            !KatalogSumber.siap && android.os.SystemClock.uptimeMillis() - mulai < 1000L
        }
        splash.setOnExitAnimationListener { it.remove() }
        super.onCreate(savedInstanceState)
        // latar jendela disamakan dengan latar aplikasi sejak awal — membunuh kedip putih saat mulai
        window.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(Latar.toArgb()))
        // ikon status bar & nav bar terang di atas latar gelap
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )
        // Mulai muat katalog SEKARANG (sebelum Compose tampil): isi dari cache disk
        // langsung, jaringan jalan di latar. Aplikasi() memanggil muat() lagi,
        // tapi dijaga flag `jalan` jadi tidak dobel.
        KonfigPabrik.berkas = java.io.File(applicationContext.filesDir, "pabrik_cache.json")
        KatalogSumber.muatLatar()
        // izin pemberitahuan (Android 13+): untuk kabar episode baru judul favorit
        if (android.os.Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission("android.permission.POST_NOTIFICATIONS") != android.content.pm.PackageManager.PERMISSION_GRANTED &&
            !prefs(this).getBoolean("izin_notif_ditanya", false)
        ) {
            // ditanya sekali saja: tanpa penanda ini dialog muncul lagi di tiap pembukaan aplikasi
            prefs(this).edit().putBoolean("izin_notif_ditanya", true).apply()
            try {
                requestPermissions(arrayOf("android.permission.POST_NOTIFICATIONS"), 7101)
            } catch (e: Exception) {
                // abaikan
            }
        }
        // pemeriksa latar: kabar rilis tetap datang walau aplikasi tertutup
        jadwalkanPemeriksaanKabar(applicationContext)
        // dibuka dari notifikasi (bukan pemulihan setelah proses dimatikan/diputar ulang)
        if (savedInstanceState == null) KeadaanBuka.id = intent?.getStringExtra(KeadaanBuka.EKSTRA)
        setContent {
            // perbarui parameter PiP (masuk otomatis, area video, ikon putar/jeda) saat berubah
            LaunchedEffect(Unit) {
                androidx.compose.runtime
                    .snapshotFlow {
                        listOf<Any?>(layakPip(), KeadaanPip.rect, KeadaanPip.sedangMain)
                    }.collectLatest {
                        delay(150)
                        perbaruiPip()
                    }
            }
            TemaAnime { Aplikasi() }
        }
    }

    // Notifikasi diketuk saat aplikasi sudah terbuka (SINGLE_TOP): Activity dipakai ulang.
    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent.getStringExtra(KeadaanBuka.EKSTRA)?.let { KeadaanBuka.id = it }
    }

    // Nonton jendela kecil (PiP): meninggalkan aplikasi saat halaman putar atau mini pemutar
    // tampil -> video mengecil jadi jendela melayang 16:9 dan tetap jalan. Tampilan di dalam
    // jendela dibuat sama dengan kartu mini: video 16:9 penuh tanpa kontrol, garis progres 2dp,
    // latar hitam. Ukuran, sudut, dan bayangan jendela diatur sistem Android (bisa dicubit pengguna).
    private val aksiPip = "com.animeid.app.PIP_PUTAR_JEDA"
    private var penerimaAktif = false
    private val penerimaPip =
        object : android.content.BroadcastReceiver() {
            override fun onReceive(
                context: android.content.Context?,
                intent: android.content.Intent?,
            ) {
                if (intent?.action != aksiPip) return
                KeadaanPip.pemutar?.let { if (it.playWhenReady) it.pause() else it.play() }
                perbaruiPip()
            }
        }

    private fun layakPip() = KeadaanPip.halamanPutarAktif || KeadaanMini.judul != null

    private fun paramPip(): android.app.PictureInPictureParams {
        val b =
            android.app.PictureInPictureParams
                .Builder()
                .setAspectRatio(android.util.Rational(16, 9))
        KeadaanPip.rect?.let { if (!it.isEmpty) b.setSourceRectHint(it) }
        val main = KeadaanPip.pemutar?.playWhenReady ?: KeadaanPip.sedangMain
        val label = if (main) "Jeda" else "Putar"
        val ikon =
            android.graphics.drawable.Icon.createWithResource(
                this,
                if (main) R.drawable.ic_pip_jeda else R.drawable.ic_pip_putar,
            )
        val aksi =
            android.app.PendingIntent.getBroadcast(
                this,
                0,
                android.content.Intent(aksiPip).setPackage(packageName),
                android.app.PendingIntent.FLAG_IMMUTABLE or android.app.PendingIntent.FLAG_UPDATE_CURRENT,
            )
        b.setActions(listOf(android.app.RemoteAction(ikon, label, label, aksi)))
        if (android.os.Build.VERSION.SDK_INT >= 31) {
            // masuk PiP mulus saat geser Beranda (gestur), hanya bila ada video yang diputar
            b.setAutoEnterEnabled(layakPip())
            b.setSeamlessResizeEnabled(true)
        }
        return b.build()
    }

    private fun perbaruiPip() {
        try {
            setPictureInPictureParams(paramPip())
        } catch (e: Exception) {
            // abaikan
        }
    }

    private fun lepasPenerimaPip() {
        if (penerimaAktif) {
            try {
                unregisterReceiver(penerimaPip)
            } catch (e: Exception) {
                // abaikan
            }
            penerimaAktif = false
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: android.content.res.Configuration,
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        KeadaanPip.sedangPip = isInPictureInPictureMode
        if (isInPictureInPictureMode) {
            // tanpa celah warna di tepi jendela kecil: latar hitam seperti kartu mini
            window.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.BLACK))
            if (!penerimaAktif) {
                androidx.core.content.ContextCompat.registerReceiver(
                    this,
                    penerimaPip,
                    android.content.IntentFilter(aksiPip),
                    androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED,
                )
                penerimaAktif = true
            }
            perbaruiPip()
        } else {
            window.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(Latar.toArgb()))
            lepasPenerimaPip()
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (layakPip() && !isInPictureInPictureMode) {
            try {
                enterPictureInPictureMode(paramPip())
            } catch (e: Exception) {
                // abaikan
            }
        }
    }

    override fun onDestroy() {
        lepasPenerimaPip()
        super.onDestroy()
    }
}

