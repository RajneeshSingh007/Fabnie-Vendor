package com.fabnie.vendor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabnie.vendor.model.category.CategoryResponseModel
import com.fabnie.vendor.model.subcategory.SubCategoryResponseModel
import com.fabnie.vendor.repository.CategoryRepository
import com.fabnie.vendor.repository.Response
import com.fabnie.vendor.repository.SubCategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubCategoryViewModel
@Inject
constructor(private val subCategoryRepository: SubCategoryRepository): ViewModel() {

    val subCategories: StateFlow<Response<SubCategoryResponseModel>>
    get() = subCategoryRepository.subCategoriesData

    fun getCategories(id:Int) = viewModelScope.launch {
        subCategoryRepository.getSubCategories(id)
    }
}