package com.joeydalu.vpd.view.accounts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joeydalu.vpd.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_list)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AccountListFragment.newInstance())
                .commitNow()
        }
    }
}