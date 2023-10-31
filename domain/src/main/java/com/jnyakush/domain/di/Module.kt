package com.jnyakush.domain.di

import org.koin.core.module.Module
import org.koin.dsl.module

private val doModule: Module = module {

}

val domainModule: List<Module> = listOf(doModule)