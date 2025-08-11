package tj.ikrom.cinemahall.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tj.ikrom.cinemahall.R
import tj.ikrom.cinemahall.data.network.model.Seat
import tj.ikrom.cinemahall.ui.viewmodel.SeatsViewModel

@AndroidEntryPoint
class SeatsFragment : Fragment(R.layout.fragment_seats) {

    private val viewModel: SeatsViewModel by viewModels()

    private lateinit var totalPriceText: TextView
    private lateinit var payButton: Button
    private lateinit var bottomPanel: View
    private lateinit var seatsContainer: CardView
    private lateinit var theaterNameText: TextView
    private lateinit var hallNameText: TextView
    private lateinit var freeSeatsCountText: TextView

    private lateinit var vipPriceView: TextView
    private lateinit var comfortPriceView: TextView
    private lateinit var standardPriceView: TextView

    // Максимум выбранных мест
    private val maxSelection = 5

    // Выбранные места
    private val selectedSeats = mutableSetOf<Seat>() // Seat — модель места

    // Цены по типам (будут установлены из seats_type API)
    private val pricesMap = mutableMapOf(
        "VIP" to 0,
        "COMFORT" to 0,
        "STANDARD" to 0
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        theaterNameText = view.findViewById(R.id.theaterName)
        hallNameText = view.findViewById(R.id.hallName)
        freeSeatsCountText = view.findViewById(R.id.freeSeatsCount)
        totalPriceText = view.findViewById(R.id.totalPrice)
        payButton = view.findViewById(R.id.payButton)
        bottomPanel = view.findViewById(R.id.bottomPanel)
        seatsContainer = view.findViewById(R.id.listCardView)

        vipPriceView = view.findViewById(R.id.vip_price)
        comfortPriceView = view.findViewById(R.id.comfort_price)
        standardPriceView = view.findViewById(R.id.standard_price)

        // Пока скрываем нижнюю панель
        bottomPanel.visibility = View.GONE

        // Подписываемся на данные
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.seats.collect { seatsResponse ->
                seatsResponse?.let { response ->

                    theaterNameText.text = "NullPointer Кинотеатр"
                    hallNameText.text = response.hallName
                    freeSeatsCountText.text = "Свободных мест: ${
                        response.seats?.count { it.bookedSeats == 0 } ?: 0
                    }"

                    // Устанавливаем цены из seats_type
                    response.seatsType?.forEach { seatType ->
                        seatType.seatType?.let { type ->
                            seatType.price?.let { price ->
                                pricesMap[type] = price
                            }
                        }
                    }
                    updatePriceViews()

                    response.seats?.let { updateSeats(it) }
                }
            }
        }

        viewModel.loadSeats()

        payButton.setOnClickListener {
            openPaymentScreen()
        }
    }

    private fun updatePriceViews() {
        vipPriceView.text = pricesMap["VIP"]?.toString() ?: "0"
        comfortPriceView.text = pricesMap["COMFORT"]?.toString() ?: "0"
        standardPriceView.text = pricesMap["STANDARD"]?.toString() ?: "0"
    }

    private fun updateSeats(seats: List<Seat>) {
        seatsContainer.removeAllViews()

        seats.forEach { seat ->
            val seatView = Button(requireContext()).apply {
                text = seat.objectTitle ?: ""

                background = when (seat.seatType) {
                    "VIP" -> ContextCompat.getDrawable(context, R.drawable.bg_vip_seat)
                    "COMFORT" -> ContextCompat.getDrawable(context, R.drawable.bg_comfort_seat)
                    "STANDARD" -> ContextCompat.getDrawable(context, R.drawable.bg_standard_seat)
                    else -> ContextCompat.getDrawable(context, android.R.color.transparent)
                }

                isEnabled = seat.bookedSeats == 0

                // Позиционирование
                x = seat.left?.toFloat() ?: 0f
                y = seat.top?.toFloat() ?: 0f

                setOnClickListener {
                    onSeatClicked(seat, this)
                }
            }
            seatsContainer.addView(seatView, FrameLayout.LayoutParams(100, 100))
        }
    }

    private fun onSeatClicked(seat: Seat, seatView: View) {
        if (selectedSeats.contains(seat)) {
            selectedSeats.remove(seat)
            seatView.alpha = 1f
        } else {
            if (selectedSeats.size >= maxSelection) {
                Toast.makeText(requireContext(), "Можно выбрать не более $maxSelection мест", Toast.LENGTH_SHORT).show()
                return
            }
            selectedSeats.add(seat)
            seatView.alpha = 0.5f
        }
        updateBottomPanel()
    }

    private fun updateBottomPanel() {
        if (selectedSeats.isEmpty()) {
            bottomPanel.visibility = View.GONE
        } else {
            bottomPanel.visibility = View.VISIBLE
            val totalPrice = selectedSeats.sumOf { seat ->
                pricesMap[seat.seatType] ?: 0
            }
            totalPriceText.text = "Итого: $totalPrice somoni"
        }
    }

    private fun openPaymentScreen() {
        // Открыть экран оплаты, передать выбранные места
        Toast.makeText(requireContext(), "Открываем оплату", Toast.LENGTH_SHORT).show()
    }
}
