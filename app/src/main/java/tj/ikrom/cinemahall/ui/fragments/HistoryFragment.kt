package tj.ikrom.cinemahall.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tj.ikrom.cinemahall.R
import tj.ikrom.cinemahall.data.database.entity.HistoryEntity
import tj.ikrom.cinemahall.ui.adapter.HistoryAdapter
import tj.ikrom.cinemahall.ui.viewmodel.HistoryViewModel

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history) {

    private val viewModel: HistoryViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var cinemaName: TextView
    private lateinit var hallName: TextView
    private lateinit var tranche: TextView
    private lateinit var clearTranche: TextView
    private lateinit var adapter: HistoryAdapter
    private lateinit var homeButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = HistoryAdapter()
        recyclerView.adapter = adapter
        cinemaName = view.findViewById(R.id.theaterName)
        hallName = view.findViewById(R.id.hallName)
        tranche = view.findViewById(R.id.tranche)
        clearTranche = view.findViewById(R.id.clearTranche)
        homeButton = view.findViewById(R.id.homeButton)

        tranche.text = "Транзакции"

        clearTranche.setOnClickListener {
            Toast.makeText(requireContext(), "Очищено", Toast.LENGTH_SHORT).show()
            viewModel.deletePayment(adapter.currentList) // удаляем актуальные
            adapter.submitList(emptyList()) // сразу очищаем UI
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allHistories.collect { payments: List<HistoryEntity> ->
                    if (payments.isNotEmpty()) {
                        // можно взять данные из первого платежа для заголовков
                        cinemaName.text = payments.first().cinemaName
                        hallName.text = payments.first().hall
                    }
                    Log.i("Payment", payments.toString())
                    adapter.submitList(payments)
                }
            }
        }
        homeButton.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_history_to_seats)
        }
    }
}
