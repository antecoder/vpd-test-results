package com.joeydalu.vpd.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.joeydalu.vpd.view.auth.LandingPageActivity
import com.joeydalu.vpd.view.theme.BorderlessTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition{splashViewModel.authState.value == SplashViewModel.AuthState.loading }
        lifecycleScope.launch {
            observeAuthState()
        }
    }

    private fun observeAuthState() {
        splashViewModel.authState.observe(this) { state ->
            when (state) {
                SplashViewModel.AuthState.loggedIn -> {

                }
                SplashViewModel.AuthState.loggedOut -> {
                    startActivity(Intent(this, LandingPageActivity::class.java))
                    finish()
                }

                SplashViewModel.AuthState.loading -> {

                }
            }
        }

    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BorderlessTheme {
        Greeting("Android")
    }
}
