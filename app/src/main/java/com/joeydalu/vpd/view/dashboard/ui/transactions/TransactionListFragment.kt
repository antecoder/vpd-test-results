package com.joeydalu.vpd.view.dashboard.ui.transactions

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joeydalu.vpd.databinding.FragmentTransactionListBinding
import com.joeydalu.vpd.view.dashboard.ui.transactions.data.TransactionListAdapter
import dagger.hilt.android.AndroidEntryPoint


/**
 * A [Fragment] which displays the list of [Transaction]'s in the local database
 */
@AndroidEntryPoint
class TransactionListFragment : Fragment() {

    companion object {
        fun newInstance() = TransactionListFragment()
    }

    private lateinit var binding: FragmentTransactionListBinding
    private val viewModel: TransactionListViewModel by viewModels()

    private lateinit var adapter: TransactionListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTransactionListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeModel()
    }

    private fun setupAdapter() {
        adapter = TransactionListAdapter { account ->

        }
        binding.recycler.adapter = adapter
    }

    private fun observeModel() {
        viewModel.transactions.observe(viewLifecycleOwner) {accounts ->
            adapter.items = accounts
        }
    }

}