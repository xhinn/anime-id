package com.animeid.app

fun hitungMundur(
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

fun jamTayang(detik: Long): String {
    val c =
        java.util.Calendar
            .getInstance()
            .apply { timeInMillis = detik * 1000L }
    return String.format("%02d.%02d", c.get(java.util.Calendar.HOUR_OF_DAY), c.get(java.util.Calendar.MINUTE))
}
