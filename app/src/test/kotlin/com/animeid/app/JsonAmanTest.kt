package com.animeid.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

// Test unit untuk pembaca JSON longgar (JsonAman.kt).
// Fungsi-fungsi ini murni (tanpa Android), jadi bisa diuji tanpa emulator.
class JsonAmanTest {

    @Test
    fun keJsonObjek_menguraiObjekSederhana() {
        val o = """{"judul":"One Piece","episode":5}""".keJsonObjek()
        assertEquals("One Piece", o.jTeks("judul"))
        assertEquals(5, o.jInt("episode"))
    }

    @Test
    fun jTeks_kunciHilang_kembaliBawaan() {
        val o = """{"judul":"Naruto"}""".keJsonObjek()
        assertEquals("", o.jTeks("tidakAda"))
        assertEquals("isi", o.jTeks("tidakAda", "isi"))
    }

    @Test
    fun jTeks_nilaiBukanTeks_kembaliBawaan() {
        val o = """{"angka":42}""".keJsonObjek()
        assertEquals("", o.jTeks("angka"))
    }

    @Test
    fun jInt_kunciHilang_kembaliBawaan() {
        val o = """{}""".keJsonObjek()
        assertEquals(0, o.jInt("tidakAda"))
        assertEquals(7, o.jInt("tidakAda", 7))
    }

    @Test
    fun jInt_nilaiDesimal_dibulatkanKeBawah() {
        val o = """{"nilai":3.9}""".keJsonObjek()
        assertEquals(3, o.jInt("nilai"))
    }

    @Test
    fun jDesimal_kunciHilang_kembaliBawaan() {
        val o = """{}""".keJsonObjek()
        assertEquals(0.0, o.jDesimal("tidakAda"), 0.0)
        assertEquals(1.5, o.jDesimal("tidakAda", 1.5), 0.0)
    }

    @Test
    fun jBenar_kunciHilang_kembaliBawaan() {
        val o = """{}""".keJsonObjek()
        assertEquals(false, o.jBenar("tidakAda"))
        assertEquals(true, o.jBenar("tidakAda", true))
    }

    @Test
    fun jBenar_nilaiBenar_dibaca() {
        val o = """{"aktif":true,"mati":false}""".keJsonObjek()
        assertTrue(o.jBenar("aktif"))
        assertEquals(false, o.jBenar("mati"))
    }

    @Test
    fun jLarik_kunciHilang_kembaliNull() {
        val o = """{}""".keJsonObjek()
        assertNull(o.jLarik("tidakAda"))
    }

    @Test
    fun jObjek_kunciHilang_kembaliNull() {
        val o = """{}""".keJsonObjek()
        assertNull(o.jObjek("tidakAda"))
    }

    @Test
    fun jObjek_objekBersarang_dibaca() {
        val o = """{"luar":{"dalam":"isi"}}""".keJsonObjek()
        val luar = o.jObjek("luar")
        assertEquals("isi", luar?.jTeks("dalam"))
    }

    @Test
    fun daftarTeks_melewatiElemenNonTeksDanKosong() {
        val o = """{"daftar":["a","",null,42,"b"]}""".keJsonObjek()
        val daftar = o.jLarik("daftar")?.daftarTeks()
        assertEquals(listOf("a", "b"), daftar)
    }

    @Test
    fun daftarTeks_larikKosong_kembaliKosong() {
        val o = """{"daftar":[]}""".keJsonObjek()
        assertEquals(emptyList<String>(), o.jLarik("daftar")?.daftarTeks())
    }

    @Test
    fun keJsonObjek_teksRusak_melemparException() {
        var gagal = false
        try {
            """{bukan json""".keJsonObjek()
        } catch (e: Exception) {
            gagal = true
        }
        assertTrue(gagal)
    }
}
