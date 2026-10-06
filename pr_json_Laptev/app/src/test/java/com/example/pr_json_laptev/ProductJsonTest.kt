package com.example.pr_json_laptev

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductJsonTest {

    private val gson = Gson()

    @Test
    fun testManualSerialization() {
        val product = Product("Программирование на Kotlin", 1500.0, listOf("учебник", "программирование", "android"))
        val json = gson.toJson(product)
        assertTrue(json.contains("\"name\":\"Программирование на Kotlin\""))
        assertTrue(json.contains("\"price\":1500.0"))
        assertTrue(json.contains("\"tags\":[\"учебник\",\"программирование\",\"android\"]"))
    }

    @Test
    fun testDeserialization() {
        val json = """{"name":"Книга","price":1200.0,"tags":["it","study"]}"""
        val product = gson.fromJson(json, Product::class.java)
        assertEquals("Книга", product.name)
        assertEquals(1200.0, product.price, 0.001)
        assertEquals(2, product.tags.size)
    }

    @Test
    fun testMissingPriceDefaultsToZero() {
        val json = """{"name":"Электронная книга","tags":["ридер"]}"""
        val product = gson.fromJson(json, Product::class.java)
        assertEquals("Электронная книга", product.name)
        assertEquals(0.0, product.price, 0.001)
    }

    @Test
    fun testExtraFieldIgnored() {
        val json = """{"name":"Телефон","price":30000.0,"tags":["гаджет"],"discount":10,"brand":"Samsung"}"""
        val product = gson.fromJson(json, Product::class.java)
        assertEquals("Телефон", product.name)
        assertEquals(30000.0, product.price, 0.001)
        assertEquals(1, product.tags.size)
    }
}
