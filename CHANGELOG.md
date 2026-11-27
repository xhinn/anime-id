# Changelog

Semua perubahan penting proyek ini dicatat di sini.
Format mengikuti [Keep a Changelog](https://keepachangelog.com/id/1.1.0/).

## [1.0.0] - 2026-11-27

### Ditambahkan
- Build hijau pertama di GitHub Actions (JDK 17, Gradle 9.9.2, AGP 9.1.0).
- Pemutar HLS (Media3 ExoPlayer) dengan katalog dari Cloudflare Pages.
- Build APK debug dan release otomatis tiap push, plus build malam harian.
- Keystore debug tetap supaya tanda tangan APK konsisten antar build.
- versionCode otomatis dari nomor run GitHub.

### Diperbaiki
- Perilaku Picture-in-Picture (PiP): video tidak lagi mengecil saat membuka chooser Bagikan.
- Kunci orientasi portrait hanya sementara supaya rotasi otomatis tetap hidup.
- Status tamat pemutar di-reset saat state berubah, supaya aksi selesai bisa jalan lagi saat memutar ulang.
