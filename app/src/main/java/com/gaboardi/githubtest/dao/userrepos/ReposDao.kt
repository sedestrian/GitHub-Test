package com.gaboardi.githubtest.dao.userrepos

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gaboardi.githubtest.model.userrepos.Repo

@Dao
interface ReposDao {
    @Query("SELECT * FROM repos ORDER BY fullName ASC")
    fun queryRepo(): DataSource.Factory<Int, Repo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repos: List<Repo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repo: Repo)

    @Query("SELECT COUNT(*) FROM repos")
    fun count(): Int

    @Query("DELETE FROM repos")
    fun clear()
}