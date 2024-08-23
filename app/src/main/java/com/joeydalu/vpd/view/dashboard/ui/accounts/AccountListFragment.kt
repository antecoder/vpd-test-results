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
 * A [Fragment] which displays the list of accounts in the local database
 */
@AndroidEntryPoint
class AccountListFragment : Fragment() {

    companion object {
        fun newInstance() = AccountListFragment()
    }

    private lateinit var binding: FragmentAccountListBinding
    private val viewModel: AccountListViewModel by viewModels()

    private lateinit var adapter: AccountListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        }
        binding.recycler.adapter = adapter
    }

    private fun observeModel() {
        viewModel.accounts.observe(viewLifecycleOwner) {accounts ->
            adapter.items = accounts
        }
    }

}