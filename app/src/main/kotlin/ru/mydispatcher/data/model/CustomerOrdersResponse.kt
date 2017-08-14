package ru.mydispatcher.data.model


data class CustomerOrdersResponse(val orders: List<CustomerOrder>, val total: Int)