package com.gaboardi.githubtest.di

import androidx.room.Room
import com.gaboardi.githubtest.App
import com.gaboardi.githubtest.dao.userrepos.ReposDao
import com.gaboardi.githubtest.dao.usersquery.UsersQueryDao
import com.gaboardi.githubtest.datasource.userrepos.local.UserReposLocalDataSource
import com.gaboardi.githubtest.datasource.userrepos.local.UserReposLocalDataSourceImpl
import com.gaboardi.githubtest.datasource.userrepos.remote.UserReposRemoteDataSource
import com.gaboardi.githubtest.datasource.userrepos.remote.UserReposRemoteDataSourceImpl
import com.gaboardi.githubtest.datasource.usersquery.local.UsersQueryLocalDataSource
import com.gaboardi.githubtest.datasource.usersquery.local.UsersQueryLocalDataSourceImpl
import com.gaboardi.githubtest.datasource.usersquery.remote.UsersQueryRemoteDataSource
import com.gaboardi.githubtest.datasource.usersquery.remote.UsersQueryRemoteDataSourceImpl
import com.gaboardi.githubtest.db.AppDatabase
import com.gaboardi.githubtest.repository.userrepos.UserReposRepository
import com.gaboardi.githubtest.repository.userrepos.UserReposRepositoryImpl
import com.gaboardi.githubtest.repository.usersquery.UsersQueryRepository
import com.gaboardi.githubtest.repository.usersquery.UsersQueryRepositoryImpl
import com.gaboardi.githubtest.usecases.userrepos.UserReposUseCase
import com.gaboardi.githubtest.usecases.userrepos.UserReposUseCaseImpl
import com.gaboardi.githubtest.usecases.usersquery.QueryUsersUseCase
import com.gaboardi.githubtest.usecases.usersquery.QueryUsersUseCaseImpl
import com.gaboardi.githubtest.viewmodel.userrepos.UserReposViewModel
import com.gaboardi.githubtest.viewmodel.usersquery.UsersQueryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            App.INSTANCE,
            AppDatabase::class.java, "database-name"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    single<UsersQueryDao> { get<AppDatabase>().usersDao() }
    single<ReposDao> { get<AppDatabase>().reposDao() }
    single<UsersQueryLocalDataSource> { UsersQueryLocalDataSourceImpl(get()) }
    single<UserReposLocalDataSource> { UserReposLocalDataSourceImpl(get()) }
    single<UsersQueryRemoteDataSource> { UsersQueryRemoteDataSourceImpl(get()) }
    single<UserReposRemoteDataSource> { UserReposRemoteDataSourceImpl(get()) }
    single<UsersQueryRepository> { UsersQueryRepositoryImpl(get(), get(), get()) }
    single<UserReposRepository> { UserReposRepositoryImpl(get(), get(), get()) }
    single<QueryUsersUseCase> { QueryUsersUseCaseImpl(get()) }
    single<UserReposUseCase> { UserReposUseCaseImpl(get()) }
    viewModel { UsersQueryViewModel(get()) }
    viewModel { UserReposViewModel(get()) }
}