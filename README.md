# Anime.id

Aplikasi nonton anime untuk Android.

Dibangun dengan Kotlin + Jetpack Compose + Media3 ExoPlayer (pemutaran HLS).
Katalog anime diambil dari Cloudflare Pages, video diputar lewat HLS per judul.

## Fitur

- Jelajah katalog anime dan jadwal rilis
- Pemutar video HLS (1080p / 720p / 480p / 360p) dengan subtitle hardsub
- Picture-in-Picture (PiP)
- Unduhan episode
- Favorit dan lanjut menonton
- Komentar episode (Firebase Auth + Firestore)
- Mode layar penuh dengan rotasi otomatis

## Cara build

Butuh **JDK 17** dan Android SDK (compileSdk 37).

```bash
./gradlew assembleDebug
```

APK debug ada di `app/build/outputs/apk/debug/app-debug.apk`.

Build release (unsigned):

```bash
./gradlew assembleRelease
```

## CI

Setiap push ke `main` menjalankan workflow GitHub Actions (`.github/workflows/build.yml`):
unit test, Android Lint, build APK debug + release, dan unggah artifact.
Ada juga pemindaian keamanan CodeQL (`.github/workflows/codeql.yml`) dan
pembaruan dependensi otomatis lewat Dependabot.

## Struktur singkat

- `app/src/main/kotlin/com/animeid/app/MainActivity.kt` — seluruh layar dan pemutar
- `app/src/main/kotlin/com/animeid/app/JsonAman.kt` — pembaca JSON katalog yang aman
- `app/build.gradle.kts` — konfigurasi modul aplikasi
- `build.gradle.kts` — konfigurasi root (AGP, Kotlin)

## Tautan

- Repo: https://github.com/xhinn/anime-id
- Lisensi: MIT (lihat `LICENSE`)
- Perubahan per rilis: `CHANGELOG.md`
