package com.example.food.util

import android.util.Log
import com.example.food.FoodDetector

class Dlog {

    companion object {
        const val tag = "Food"

        private fun buildLogMsg(message: String): String {
            val ste: StackTraceElement = Thread.currentThread().stackTrace[4]

            return "[${ste.fileName.replace(".kt", "")} :: ${ste.methodName}] $message"
        }

        fun e(message: String) {
            if (FoodDetector.debug) Log.e(tag, buildLogMsg(message))
        }

        fun w(message: String) {
            if (FoodDetector.debug) Log.w(tag, buildLogMsg(message))
        }

        fun i(message: String) {
            if (FoodDetector.debug) Log.i(tag, buildLogMsg(message))
        }

        fun d(message: String) {
            if (FoodDetector.debug) Log.d(tag, buildLogMsg(message))
        }

        fun v(message: String) {
            if (FoodDetector.debug) Log.v(tag, buildLogMsg(message))
        }
    }
}