package tj.ikrom.cinemahall.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tj.ikrom.cinemahall.R
import tj.ikrom.cinemahall.data.database.entity.HistoryEntity
import tj.ikrom.cinemahall.ui.adapter.PaymentAdapter
import tj.ikrom.cinemahall.ui.viewmodel.SeatsViewModel

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private val viewModel: SeatsViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var cinemaName: TextView
    private lateinit var hallName: TextView
    private lateinit var payment: TextView
    private lateinit var adapter: PaymentAdapter
    private lateinit var payButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.selectPayment(null)
            findNavController().navigateUp()
        }

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PaymentAdapter()
        recyclerView.adapter = adapter
        cinemaName = view.findViewById(R.id.theaterName)
        hallName = view.findViewById(R.id.hallName)
        payment = view.findViewById(R.id.payment)
        payButton = view.findViewById(R.id.payButton)

        payment.text = "Оплата"
        payButton.text = "Забронировать"

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedPayment.collect { payments: HistoryEntity? ->
                    payments?.let { payment ->
                        // можно взять данные из первого платежа для заголовков
                        cinemaName.text = payment.cinemaName
                        hallName.text = payment.hall

                        Log.i("Payment", payment.toString())
                        adapter.submitList(payment.seats)

                        val historyEntity = HistoryEntity(
                            cinemaName = payment.cinemaName,
                            hall = payment.hall,
                            seats = payment.seats.toList(),
                            totalPrice = payment.totalPrice,
                        )

                        payButton.setOnClickListener {
                            viewModel.insertPayment(historyEntity)

                            val navController = Navigation.findNavController(view)
                            navController.navigate(R.id.action_payment_to_history)
                        }
                    }
                }
            }
        }
    }
}
