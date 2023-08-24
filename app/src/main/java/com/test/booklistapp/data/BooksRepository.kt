package com.test.booklistapp.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.test.booklistapp.R
import com.test.booklistapp.model.Book
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream


class BooksRepository {

    fun getBooks(context: Context): MutableList<Book>? {
        return parseJSON(context)
    }

    private fun readJSONFromRaw(context: Context): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.book_list)
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }


    // TODO : map with jackson
    private fun parseJSON(context: Context): MutableList<Book>? {
        val jsonString: String? = readJSONFromRaw(context)
        val obj = JSONObject(jsonString)
        val booksArr: JSONArray = obj.getJSONArray("data")
        val list: MutableList<Book> = ArrayList()
        for (i in 0 until booksArr.length()) {
            val bookObject = booksArr.getJSONObject(i)
            val book : Book = Book(bookObject.getString("title"),
                bookObject.getString("body"),
                bookObject.getDouble("rating"), bookObject.getString("url"))
            list.add(book)
        }

        return list
    }
}