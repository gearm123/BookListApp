package com.test.booklistapp

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.booklistapp.adapters.BookAdapter
import com.test.booklistapp.data.BooksRepository
import com.test.booklistapp.model.Book
import com.test.booklistapp.viewmodel.BooksViewModel
import com.test.booklistapp.viewmodel.ViewModelFactory


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var booksAdapter: BookAdapter
    private lateinit var booksRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpViews()
        doObserveWork()
    }

    private fun setUpViews() {
        //setup RecyclerAiew
        booksRecyclerView = findViewById<View>(R.id.rv_design) as RecyclerView
        booksRecyclerView.addItemDecoration(
            DividerItemDecoration(
                booksRecyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        booksRecyclerView.setHasFixedSize(true)

        booksRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )

        //setup SearchView
        searchView = findViewById<View>(R.id.search_view) as SearchView
        searchView
            .setOnQueryTextListener(this)

        booksAdapter = BookAdapter()
        booksRecyclerView.adapter = booksAdapter
    }

    private fun doObserveWork() {
        //provide view model with dp injection
        var booksViewModel: BooksViewModel = ViewModelProvider(
            this,
            ViewModelFactory(application, BooksRepository())
        )[BooksViewModel::class.java]

        //for future use loading large data set
        booksViewModel.isProgressBarVisible().observe(this, Observer {

        })

        //render book list whenever change in list is triggered
        booksViewModel.getBookList().observe(this, Observer {
            if (it != null) {
                renderBookList(it)
            }
        })
    }

    private fun renderBookList(bookList: MutableList<Book>) {
        booksAdapter.addData(bookList)
        booksAdapter.notifyDataSetChanged()

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        booksAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        booksAdapter.filter.filter(newText)
        return false
    }
}