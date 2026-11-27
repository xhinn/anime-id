package com.animeid.app

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject

// Pembaca JSON longgar berbasis kotlinx.serialization. Perilakunya meniru opt* milik
// org.json: kunci hilang / tipe salah -> nilai bawaan, bukan error. Pemanggil yang
// membaca teks mentah tetap membungkusnya dengan try/catch (teks rusak -> exception).
private val jsonLonggar =
    Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

internal fun String.keJsonObjek(): JsonObject = jsonLonggar.parseToJsonElement(this).jsonObject

private fun JsonObject.primitif(k: String): JsonPrimitive? = (this[k] as? JsonPrimitive)?.takeIf { it !is JsonNull }

internal fun JsonObject.jLarik(k: String): JsonArray? = this[k] as? JsonArray

internal fun JsonObject.jObjek(k: String): JsonObject? = this[k] as? JsonObject

internal fun JsonObject.jTeks(
    k: String,
    bawaan: String = "",
): String = primitif(k)?.takeIf { it.isString }?.content ?: bawaan

internal fun JsonObject.jInt(
    k: String,
    bawaan: Int = 0,
): Int = primitif(k)?.let { it.intOrNull ?: it.doubleOrNull?.toInt() } ?: bawaan

internal fun JsonObject.jDesimal(
    k: String,
    bawaan: Double = 0.0,
): Double = primitif(k)?.doubleOrNull ?: bawaan

internal fun JsonObject.jBenar(
    k: String,
    bawaan: Boolean = false,
): Boolean = primitif(k)?.booleanOrNull ?: bawaan

// Daftar teks tak kosong dari larik (elemen non-teks / null dilewati)
internal fun JsonArray.daftarTeks(): List<String> =
    mapNotNull {
        (it as? JsonPrimitive)?.takeIf { p -> p.isString }?.content?.ifBlank { null }
    }

