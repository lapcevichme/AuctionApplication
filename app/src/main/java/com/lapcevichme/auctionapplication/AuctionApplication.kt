package com.lapcevichme.auctionapplication

import android.app.Application
import com.lapcevichme.auctionapplication.di.Dependencies

class AuctionApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Dependencies.init(this)
    }
}