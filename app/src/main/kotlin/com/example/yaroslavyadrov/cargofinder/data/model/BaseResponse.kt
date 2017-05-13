package com.example.yaroslavyadrov.cargofinder.data.model


data class BaseResponse<out T>(val type: String, val value: List<T>)