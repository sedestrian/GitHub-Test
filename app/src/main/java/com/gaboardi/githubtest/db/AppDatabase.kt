package com.gaboardi.githubtest.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gaboardi.githubtest.dao.stargazers.StargazersDao
import com.gaboardi.githubtest.dao.userrepos.ReposDao
import com.gaboardi.githubtest.dao.usersquery.UsersQueryDao
import com.gaboardi.githubtest.model.users.User
import com.gaboardi.githubtest.model.stargazers.Stargazer
import com.gaboardi.githubtest.model.userrepos.Repo

@Database(entities = [User::class, Repo::class, Stargazer::class], exportSchema = false, version = 4)
abstract class AppDatabase: RoomDatabase() {
    abstract fun usersDao(): UsersQueryDao
    abstract fun reposDao(): ReposDao
    abstract fun stargazersDao(): StargazersDao
}