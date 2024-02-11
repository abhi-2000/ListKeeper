package com.groww.myapplication.presenter.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.groww.myapplication.domain.usecase.AddNewItemUseCase
import com.groww.myapplication.domain.usecase.GetAllPresentItemUseCase
import com.groww.myapplication.domain.usecase.RemoveCompletedItemUseCase

class BaseViewModelFactory(
    private val addNewItemUseCase: AddNewItemUseCase,
    private val removeCompletedItemUseCase: RemoveCompletedItemUseCase,
    private val getAllPresentItemUseCase: GetAllPresentItemUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BaseViewModel(
            addNewItemUseCase,removeCompletedItemUseCase,getAllPresentItemUseCase
        ) as T
    }
}