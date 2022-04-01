package com.fabnie.vendor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabnie.vendor.model.category.CategoryResponseModel
import com.fabnie.vendor.repository.CategoryRepository
import com.fabnie.vendor.repository.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel
@Inject
constructor(private val categoryRepository: CategoryRepository): ViewModel() {

    init {
        getCategories()
    }

    val categories: StateFlow<Response<CategoryResponseModel>>
        get() = categoryRepository.categoriesData

    fun getCategories() = viewModelScope.launch {
        categoryRepository.getCategories();
    }
}