package com.joeydalu.vpd.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.joeydalu.vpd.R
import com.joeydalu.vpd.databinding.ActivityLandingPageBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * The applications landing page.
 *
 * This page will place a video and be the first screen new users see,
 * allowing them to login or signup.
 *
 * @author Joseph Dalughut
 */
@AndroidEntryPoint
class LandingPageActivity : AppCompatActivity() {

    private var isPrepared = false
    lateinit var binding: ActivityLandingPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadVideoIntoView()
        attachActions()
    }

    private fun loadVideoIntoView() {
        try {
            binding.vwVideo.setRawData(R.raw.transaction2)
            binding.vwVideo.setVolume(0f, 0f)
            binding.vwVideo.isLooping = true
            binding.vwVideo.prepareAsync {
                binding.vwVideo.start()
                isPrepared = true
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    // attach ui actions
    private fun attachActions() {
        binding.btnLogin.setOnClickListener {
            startAuthActivity(true)
        }
        binding.btnSignup.setOnClickListener {
            startAuthActivity(false)
        }
    }

    private fun startAuthActivity(loginMode: Boolean) {
        val intent = Intent(this, AuthActivity::class.java)
        intent.putExtra(AuthActivity.ARG_LOGIN_MODE, loginMode)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if (isPrepared) {
            binding.vwVideo.start()
        } else {
            loadVideoIntoView()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isPrepared) {
            binding.vwVideo.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // release resources
        binding.vwVideo.stop()
        binding.vwVideo.reset()
        binding.vwVideo.release()
    }

}