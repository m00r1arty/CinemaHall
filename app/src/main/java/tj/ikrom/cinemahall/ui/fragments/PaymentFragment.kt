package tj.ikrom.cinemahall.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tj.ikrom.cinemahall.R
import tj.ikrom.cinemahall.ui.PaymentAdapter
import tj.ikrom.cinemahall.ui.viewmodel.PaymentViewModel

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private val viewModel: PaymentViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PaymentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PaymentAdapter()
        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allPayments.collect { payments ->
                    Log.i("Payment", payments.toString())
                    adapter.submitList(payments)
                }
            }
        }
    }
}
