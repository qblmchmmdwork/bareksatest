package com.bareksa.bareksatest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bareksa.bareksatest.R
import com.bareksa.bareksatest.databinding.ActivityPerbandinganBinding

class PerbandinganActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityPerbandinganBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}