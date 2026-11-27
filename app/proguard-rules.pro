# Add project-specific ProGuard/R8 keep rules here.
# These are applied on top of the bundled defaults (proguard-android-optimize.txt) when the
# build type has minifyEnabled = true.

# Tidak perlu keep rule untuk @Serializable: aplikasi hanya memakai API pohon
# kotlinx.serialization (JsonObject/JsonElement), bukan data class @Serializable,
# jadi tidak ada serializer berbasis refleksi yang bisa terhapus R8.
# Pustaka (Media3, Coil, Firebase, WorkManager) membawa consumer rule sendiri.

# Pertahankan nomor baris supaya stack trace crash tetap bisa dibaca
# (penting saat Crashlytics dipasang), tapi sembunyikan nama berkas asli.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
