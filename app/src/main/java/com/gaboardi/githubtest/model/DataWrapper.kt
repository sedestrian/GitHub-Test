package com.gaboardi.githubtest.model

data class DataWrapper<T>(
    val data: T?,
    val error: String?,
    val errorCode: Int
)