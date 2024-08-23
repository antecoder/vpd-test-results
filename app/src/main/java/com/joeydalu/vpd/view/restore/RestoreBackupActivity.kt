package com.joeydalu.vpd.view.restore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joeydalu.vpd.databinding.ActivityRestoreBackupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestoreBackupActivity : AppCompatActivity() {

    lateinit var binding: ActivityRestoreBackupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestoreBackupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupProgress()
    }

    private fun setupProgress() {
        binding.progress.progress = 30
    }

}