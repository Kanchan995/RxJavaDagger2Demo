package com.example.rxjavademo.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rxjavademo.di.util.ViewModelKey
import com.example.rxjavademo.ui.detail.DetailsViewModel
import com.example.rxjavademo.ui.list.NewListViewModel
import com.example.rxjavademo.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NewListViewModel::class)
    abstract fun bindListViewModel(listViewModel: NewListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun bindDetailViewModel(detailsViewModel: DetailsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
