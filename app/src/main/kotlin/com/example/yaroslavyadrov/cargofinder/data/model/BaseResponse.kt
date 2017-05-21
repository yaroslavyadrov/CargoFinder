package com.example.yaroslavyadrov.cargofinder.data.model


data class BaseResponse<out T>(val code: Int, val message: String, val data: T)