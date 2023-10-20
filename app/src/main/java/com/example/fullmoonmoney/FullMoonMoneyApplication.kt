package com.example.fullmoonmoney

import android.app.Application

class FullMoonMoneyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}