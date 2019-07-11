package com.gaboardi.githubtest.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gaboardi.githubtest.dao.usersquery.UsersQueryDao
import com.gaboardi.githubtest.model.User

@Database(entities = [User::class], exportSchema = false, version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun usersDao(): UsersQueryDao
}