package com.groww.myapplication.presenter.di.module

import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.groww.myapplication.domain.repository.Repository
import com.groww.myapplication.domain.repository.RepositoryImpl
import com.groww.myapplication.domain.usecase.AddNewItemUseCase
import com.groww.myapplication.domain.usecase.GetAllPresentItemUseCase
import com.groww.myapplication.domain.usecase.RemoveCompletedItemUseCase
import com.groww.myapplication.presenter.ui.main.RecyclerViewAdapter
import com.groww.myapplication.presenter.viewModel.BaseViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Modules {

    @Singleton
    @Provides
    fun provideRepository(): Repository {
        return RepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideAddNewItemUseCase(repository: Repository): AddNewItemUseCase {
        return AddNewItemUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetAllPresentItemUseCase(repository: Repository): GetAllPresentItemUseCase {
        return GetAllPresentItemUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideRemoveCompletedItemUseCase(repository: Repository): RemoveCompletedItemUseCase {
        return RemoveCompletedItemUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideRecyclerViewAdapter(databaseReference: DatabaseReference): RecyclerViewAdapter {
        return RecyclerViewAdapter(databaseReference)
    }

    @Singleton
    @Provides
    fun provideDatabaseReference(): DatabaseReference{
        return Firebase.database.reference
    }

    @Provides
    fun baseViewModelFactoryProvider(
        addNewItemUseCase: AddNewItemUseCase,
        removeCompletedItemUseCase: RemoveCompletedItemUseCase,
        getAllPresentItemUseCase: GetAllPresentItemUseCase,
    ): BaseViewModelFactory {
        return BaseViewModelFactory(
            addNewItemUseCase, removeCompletedItemUseCase, getAllPresentItemUseCase
        )
    }

}