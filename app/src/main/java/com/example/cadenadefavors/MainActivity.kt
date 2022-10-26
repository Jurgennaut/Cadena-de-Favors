package com.example.cadenadefavors

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

enum class ProviderType {
    BASIC
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}