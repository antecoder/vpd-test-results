package com.joeydalu.vpd.view.dashboard.ui.accounts

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joeydalu.vpd.databinding.FragmentAccountListBinding
import com.joeydalu.vpd.view.dashboard.ui.accounts.data.AccountListAdapter
import dagger.hilt.android.AndroidEntryPoint


/**
 * A [Fragment] which displays the list of [Account] objects in the local database
 *
 * @author Joseph Dalughut
 */
@AndroidEntryPoint
class AccountListFragment : Fragment() {

    companion object {

        /**
         * Use this to create a new instance of the [AccountListFragment]
         *
         * @return an [AccountListFragment] instance
         */
        fun newInstance() = AccountListFragment()
    }

    private lateinit var binding: FragmentAccountListBinding
    private val viewModel: AccountListViewModel by viewModels()

    private lateinit var adapter: AccountListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeModel()
    }

    private fun setupAdapter() {
        adapter = AccountListAdapter { account ->
            // Do something like display the account here
        }
        binding.recycler.adapter = adapter
    }

    private fun observeModel() {
        // Display accounts we receive.
        viewModel.accounts.observe(viewLifecycleOwner) {accounts ->

            // hide or show empty state
            if  (accounts.isNotEmpty()) {
                binding.vwEmptyAccounts.visibility = View.GONE
            } else {
                binding.vwEmptyAccounts.visibility = View.VISIBLE
            }
            adapter.items = accounts
        }
    }

}