package com.animeid.app

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
