package com.jnyakush.rickymorty.di

import com.jnyakush.rickymorty.ui.viewmodel.CharacterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { CharacterViewModel(get()) }
}

