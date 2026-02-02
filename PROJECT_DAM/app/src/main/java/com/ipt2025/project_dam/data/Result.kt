package com.ipt2025.project_dam.data

/**
 * simple wrapper class to handle success or error states
 * holds a generic class
 */
sealed class Result<out T : Any> {

    /**
     * holds the success class data if everything went well
     */
    data class Success<out T : Any>(val data: T) : Result<T>()

    /**
     * holds the error class data if everything went well
     */
    data class Error(val exception: Exception) : Result<Nothing>()

    /**
     * string representation for debugging
     */
    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}