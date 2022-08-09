package com.it.openmechanic.data

sealed class Result<out S, out E>
class Success<S>(val data: S) : Result<S, Nothing>()
class Failure<E>(val error: E) : Result<Nothing, E>()
