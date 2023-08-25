package com.test.booklistapp.data

import android.content.Context
import android.graphics.Color
import com.test.booklistapp.R
import com.test.booklistapp.model.Book
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream


class BooksRepository {

    fun getBooks(context: Context): MutableList<Book>? {
        return parseJSON(context)
    }

    //read Json as Byte Stream from res/raw
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

    //parse Json manually and create Book list
    // TODO : map with jackson
    private fun parseJSON(context: Context): MutableList<Book>? {
        val jsonString: String? = readJSONFromRaw(context)
        val obj :JSONObject? = JSONObject(jsonString)
        val booksArr: JSONArray? = obj?.getJSONArray("data")
        val list: MutableList<Book> = ArrayList()
        if (booksArr != null) {
            for (i in 0 until booksArr.length()) {
                val bookObject = booksArr.getJSONObject(i)
                val placeHolderObject = bookObject.getJSONObject("placeholderColor")
                val red = placeHolderObject.getString("red").toInt()
                val green = placeHolderObject.getString("green").toInt()
                val blue = placeHolderObject.getString("blue").toInt()
                val holderColor: Int = Color.rgb(red,green,blue)
                val book: Book = Book(
                    bookObject.getString("title"),
                    bookObject.getString("body"),
                    bookObject.getDouble("rating"), holderColor, bookObject.getString("url")
                )
                list.add(book)
            }
        }
        return list
    }
}