package com.gaboardi.githubtest.dao.usersquery

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gaboardi.githubtest.model.User

@Dao
interface UsersQueryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("SELECT * FROM users WHERE login LIKE '%' || :q || '%'")
    fun query(q: String): DataSource.Factory<Int, User>

    @Query("SELECT COUNT(*) FROM users")
    fun count(): Int
}