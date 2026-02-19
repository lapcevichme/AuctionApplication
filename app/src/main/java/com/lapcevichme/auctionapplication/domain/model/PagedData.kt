package com.lapcevichme.auctionapplication.domain.model

data class PagedData<T> (
    val items: List<T>,
    val isLastPage : Boolean
)