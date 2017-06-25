package ru.mydispatcher.util.pagination

import java.lang.RuntimeException


class PagingException(detailMessage: String) : RuntimeException(detailMessage)