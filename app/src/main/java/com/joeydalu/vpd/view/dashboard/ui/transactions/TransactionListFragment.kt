package com.joeydalu.vpd.view.dashboard.ui.transactions

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joeydalu.vpd.databinding.FragmentTransactionListBinding
import com.joeydalu.vpd.view.dashboard.ui.transactions.data.TransactionListAdapter
import com.joeydalu.vpd.view.transfers.TransferActivity
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTransactionListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeModel()

        // Start the transfer activity
        binding.btnNewTransfer.setOnClickListener {
            val intent = Intent(requireActivity(), TransferActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupAdapter() {
        adapter = TransactionListAdapter { account ->

        }
        binding.recycler.adapter = adapter
    }

    private fun observeModel() {
        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->

            // hide or show empty state
            if  (transactions.isNotEmpty()) {
                binding.vwEmptyTransfers.visibility = View.GONE
            } else {
                binding.vwEmptyTransfers.visibility = View.VISIBLE
            }
            adapter.items = transactions
        }
    }

}