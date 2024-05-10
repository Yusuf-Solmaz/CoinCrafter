package com.yusuf.cryptocurrencytrading.ui.mainScreens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusuf.cryptocurrencytrading.databinding.FragmentTransactionsBinding
import com.yusuf.cryptocurrencytrading.ui.mainScreens.adapter.TransactionsAdapter
import com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.TransactionsViewModel
import com.yusuf.cryptocurrencytrading.utils.gone
import com.yusuf.cryptocurrencytrading.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionsFragment : Fragment() {

    private lateinit var binding: FragmentTransactionsBinding
    private lateinit var viewModel: TransactionsViewModel
    private lateinit var adapter: TransactionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionsBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(TransactionsViewModel::class.java)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()
        viewModel.getAllTransactions()
    }

    private fun observeData(){

        viewModel.loading.observe(viewLifecycleOwner){
            if (it){
                binding.progressBarTransactions.visible()
                binding.transactionsRecyclerView.gone()
            }
            else{
                binding.progressBarTransactions.gone()
                binding.transactionsRecyclerView.visible()
            }
        }

        viewModel.transactions.observe(viewLifecycleOwner){
            transactions->
            if (transactions.isEmpty()){
                binding.noTransactionText.visible()
            }
            else{
                binding.noTransactionText.gone()
                transactions?.let {
                    adapter = TransactionsAdapter(requireContext(), transactions)
                    binding.transactionsRecyclerView.adapter = adapter
                }
            }


        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.transactionsRecyclerView.layoutManager = layoutManager
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllTransactions()
        observeData()
    }

}