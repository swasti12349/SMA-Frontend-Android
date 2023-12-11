package com.sro.schoolmanagementapp.Activity.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sro.schoolmanagementapp.Activity.Repository.Repository

class MainViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}