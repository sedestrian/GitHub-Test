package com.gaboardi.githubtest.dao.stargazers

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gaboardi.githubtest.model.stargazers.Stargazer

@Dao
interface StargazersDao {
    @Query("SELECT * FROM stargazers ORDER BY callPosition ASC")
    fun queryStargazers(): DataSource.Factory<Int, Stargazer>

    @Query("SELECT MAX(callPosition) + 1 FROM stargazers")
    fun getNextIndex() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repos: List<Stargazer>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repo: Stargazer)

    @Query("SELECT COUNT(*) FROM stargazers")
    fun count(): Int

    @Query("DELETE FROM stargazers")
    fun clear()
}