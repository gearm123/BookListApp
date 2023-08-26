package com.test.booklistapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.booklistapp.data.BooksRepository

class ViewModelFactory constructor(
    private val application: Application,
    private val repository: BooksRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(BooksViewModel::class.java!!)) {
            BooksViewModel(application, repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}