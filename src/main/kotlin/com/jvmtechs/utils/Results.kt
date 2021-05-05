package com.jvmtechs.utils

import java.net.ConnectException
import java.net.SocketException


sealed class Results {

    companion object {
        fun loading() = Loading
    }

    object Loading : Results()

    class Success<T>(
            val data: T? = null,
            val code: CODE
    ) : Results() {
        enum class CODE {
            WRITE_SUCCESS,
            UPDATE_SUCCESS,
            LOAD_SUCCESS,
            DELETE_SUCCESS,
        }
    }

    class Error(e: Exception) : Results() {
        enum class CODE {
            DUPLICATE_VEHICLE,
            ODOMETER_LESS_PREVIOUS,
            INSUFFICIENT_FUEL,
            DUPLICATE_WAYBILL,
            NO_CONNECTION,
            UNKNOWN
        }

        val code: CODE = when (e) {
            is DuplicateVehicleException -> CODE.DUPLICATE_VEHICLE
            is InvalidOdoMeterException -> CODE.ODOMETER_LESS_PREVIOUS
            is InsufficientFuelException -> CODE.INSUFFICIENT_FUEL
            is DuplicateWaybillException -> CODE.DUPLICATE_WAYBILL
            is SocketException -> CODE.NO_CONNECTION
            is ConnectException -> CODE.NO_CONNECTION
            else -> {
                println("Error message was ${e.printStackTrace()}")
                CODE.UNKNOWN
            }
        }

        class DuplicateVehicleException : Exception()
        class InvalidOdoMeterException : Exception()
        class InsufficientFuelException: Exception()
        class DuplicateWaybillException: Exception()
    }
}