package com.example.smartfootapp.di.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartfootapp.base.BaseViewModel
import com.example.smartfootapp.viewmodels.UserScreenViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(UserScreenViewModel::class)
    abstract fun bindUserScreenViewModel(userScreenViewModel: UserScreenViewModel): ViewModel
}