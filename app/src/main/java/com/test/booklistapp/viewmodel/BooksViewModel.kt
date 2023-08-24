package com.test.booklistapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.test.booklistapp.data.BooksRepository
import com.test.booklistapp.model.Book
import kotlinx.coroutines.launch

class BooksViewModel(application: Application) : AndroidViewModel(application) {
    private val booksRepository: BooksRepository = BooksRepository()
    private var progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private var bookListLive: MutableLiveData<MutableList<Book>?> = MutableLiveData()
    init {
        populateBookList();
    }


    fun isProgressBarVisible(): LiveData<Boolean> {
        return progressBarVisibility
    }

    fun getBookList(): MutableLiveData<MutableList<Book>?> {
        return bookListLive
    }


    private fun populateBookList() {
        viewModelScope.launch {
            val bookList: MutableList<Book>? = booksRepository.getBooks(getApplication())
            bookListLive.postValue(bookList)
            progressBarVisibility.postValue(false)
        }
    }
}