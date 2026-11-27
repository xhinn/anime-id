# Anime.id

![Build](https://github.com/xhinn/anime-id/actions/workflows/build.yml/badge.svg)
![License](https://img.shields.io/badge/license-MIT-green.svg)

Aplikasi nonton anime untuk Android.

Tonton anime dengan subtitle Indonesia, unduh episode untuk ditonton offline,
simpan favorit, dan lanjutkan menonton dari posisi terakhir.

## Tangkapan layar

_(Tangkapan layar menyusul.)_

## Fitur

- Jelajah katalog anime dan jadwal rilis
- Pemutar video (1080p / 720p / 480p / 360p) dengan subtitle hardsub
- Picture-in-Picture (PiP) — video tetap jalan saat membuka aplikasi lain
- Unduhan episode untuk ditonton offline
- Favorit dan lanjut menonton
- Komentar episode
- Mode layar penuh dengan rotasi otomatis
- Masuk dengan akun Google untuk sinkronisasi progres dan komentar

## Cara memakai

1. Buka aplikasi, jelajahi katalog atau cari judul.
2. Pilih episode, lalu putar.
3. Ketuk layar untuk menampilkan kontrol (putar/jeda, layar penuh, kecepatan).
4. Untuk menonton offline, unduh episode dari halaman detail.
5. Masuk dengan Google untuk menyimpan favorit, progres, dan komentar.

## Cara mendapatkan aplikasi

- Bangun sendiri dari kode sumber (lihat "Cara build" di bawah).
- APK debug terbaru tersedia sebagai artifact di halaman Actions GitHub.
- Distribusi lewat GitHub Release sedang disiapkan.

## Teknologi

| Bagian | Teknologi |
|---|---|
| Bahasa | Kotlin 2.4.21 |
| UI | Jetpack Compose + Material3 Expressive 1.5.0 |
| Pemutar | Media3 ExoPlayer 1.11.1 (HLS) |
| Gambar | Coil 3.6.3 |
| Efek blur | Haze 1.6.10 |
| Backend | Firebase Auth + Firestore |
| Build | AGP 9.1.0, Gradle 9.9.2, JDK 17 |

Versi dependensi terpusat di `gradle/libs.versions.toml`.

## Arsitektur

Aplikasi memakai satu Activity (`MainActivity`) + Jetpack Compose.
Saat ini belum ada ViewModel/Repository dan kode masih dalam satu berkas besar
— pemecahan struktur bertahap sedang dikerjakan (lihat `PANDUAN-PROYEK.md`
untuk peta kode).

## Cara build

Butuh **JDK 17** dan Android SDK (compileSdk 37).
Tidak perlu menyiapkan `google-services.json` atau keystore debug secara manual
— keduanya sudah ada di repo.

```bash
./gradlew assembleDebug
```

APK debug ada di `app/build/outputs/apk/debug/app-debug.apk`.

Build release (unsigned):

```bash
./gradlew assembleRelease
```

## Test

```bash
./gradlew testDebugUnitTest
./gradlew lintDebug
```

Catatan: proyek saat ini belum punya test unit — penambahan test sedang dikerjakan.
Lint dijalankan di CI dan laporannya diunggah sebagai artifact.

## CI

Setiap push ke `main` menjalankan workflow GitHub Actions (`.github/workflows/build.yml`):
unit test, Android Lint, build APK debug + release, dan unggah artifact.
Ada juga build malam harian (03:00 WIB), pemindaian keamanan CodeQL
(`.github/workflows/codeql.yml`), dan pembaruan dependensi otomatis lewat Dependabot.

## Struktur proyek

- `app/src/main/kotlin/com/animeid/app/MainActivity.kt` — seluruh layar dan pemutar
- `app/src/main/kotlin/com/animeid/app/JsonAman.kt` — pembaca JSON katalog yang aman
- `app/build.gradle.kts` — konfigurasi modul aplikasi
- `build.gradle.kts` — konfigurasi root (AGP, Kotlin)
- `gradle/libs.versions.toml` — katalog versi dependensi
- `.github/workflows/` — workflow CI (build, CodeQL)
- `PANDUAN-PROYEK.md` — peta kode dan panduan perbaikan
- `PRIVACY.md` — kebijakan privasi
- `CHANGELOG.md` — perubahan per rilis

## Rilis

- `versionName` saat ini `1.0.0`; `versionCode` diisi otomatis dari nomor run GitHub.
- APK debug ditandatangani dengan keystore debug tetap di repo (`app/debug.keystore`)
  sehingga setiap build bisa dipasang menimpa versi sebelumnya.
- APK release masih unsigned; signing release dan distribusi via GitHub Release
  sedang disiapkan.

## Berkontribusi

- Buat branch dari `main`, kirim perubahan lewat pull request.
- Ikuti gaya kode yang sudah ada; jalankan `./gradlew lintDebug` sebelum mengirim.
- Jangan ubah `app/debug.keystore`, `google-services.json`, dan kunci Firebase
  di kode tanpa persetujuan pemilik repo.

## Tautan

- Repo: https://github.com/xhinn/anime-id
- Lisensi: MIT (lihat `LICENSE`)
- Kebijakan privasi: `PRIVACY.md`
- Perubahan per rilis: `CHANGELOG.md`
