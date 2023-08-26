package com.test.booklistapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.test.booklistapp.data.BooksRepository
import com.test.booklistapp.model.Book
import kotlinx.coroutines.launch

class BooksViewModel(
    private val application: Application,
    private val booksRepository: BooksRepository
) : AndroidViewModel(application) {
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

    //launch coroutine to get book list and post change when complete
    private fun populateBookList() {
        viewModelScope.launch {
            val bookList: MutableList<Book>? = booksRepository.getBooks(application)
            bookListLive.postValue(bookList)
            progressBarVisibility.postValue(false)
        }
    }
}