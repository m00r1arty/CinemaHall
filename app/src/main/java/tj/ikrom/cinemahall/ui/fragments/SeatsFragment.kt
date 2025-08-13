package tj.ikrom.cinemahall.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tj.ikrom.cinemahall.R
import tj.ikrom.cinemahall.data.network.model.Seat
import tj.ikrom.cinemahall.ui.SeatsAdapter
import tj.ikrom.cinemahall.ui.viewmodel.SeatsViewModel

@AndroidEntryPoint
class SeatsFragment : Fragment(R.layout.fragment_seats) {

    private val viewModel: SeatsViewModel by viewModels()

    private lateinit var totalPriceText: TextView
    private lateinit var payButton: Button
    private lateinit var bottomPanel: View
    private lateinit var theaterNameText: TextView
    private lateinit var hallNameText: TextView
    private lateinit var freeSeatsCountText: TextView
    private lateinit var seatsRecyclerView: RecyclerView

    private lateinit var vipPriceView: TextView
    private lateinit var comfortPriceView: TextView
    private lateinit var standardPriceView: TextView

    // Максимум выбранных мест
    private val maxSelectionPlace = 5
    private val selectedSeats = mutableSetOf<Seat>()

    private val pricesMap: MutableMap<String, Int> = mutableMapOf(
        "VIP" to 0,
        "COMFORT" to 0,
        "STANDARD" to 0
    )

    private lateinit var seatsAdapter: SeatsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        theaterNameText = view.findViewById(R.id.theaterName)
        hallNameText = view.findViewById(R.id.hallName)
        freeSeatsCountText = view.findViewById(R.id.freeSeatsCount)
        totalPriceText = view.findViewById(R.id.totalPrice)
        payButton = view.findViewById(R.id.payButton)
        bottomPanel = view.findViewById(R.id.bottomPanel)
        seatsRecyclerView = view.findViewById(R.id.seatsRecyclerView)

        vipPriceView = view.findViewById(R.id.vip_price)
        comfortPriceView = view.findViewById(R.id.comfort_price)
        standardPriceView = view.findViewById(R.id.standard_price)

        bottomPanel.visibility = View.GONE

        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.seats.collect { seatsResponse ->
                seatsResponse?.let { response ->

                    theaterNameText.text = "NullPointer Кинотеатр"
                    hallNameText.text = response.hallName
                    freeSeatsCountText.text = "Свободных мест: ${
                        response.seats?.count { it.bookedSeats == 0 } ?: 0
                    }"

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

    private fun setupRecyclerView() {
        seatsAdapter = SeatsAdapter(emptyList()) { seat ->
            onSeatClicked(seat)
        }
        seatsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 10)
        seatsRecyclerView.adapter = seatsAdapter
    }

    private fun updatePriceViews() {
        vipPriceView.text = "VIP: ${pricesMap["VIP"] ?: 0} с"
        comfortPriceView.text = "COMFORT: ${pricesMap["COMFORT"] ?: 0} с"
        standardPriceView.text = "STANDARD: ${pricesMap["STANDARD"] ?: 0} с"
    }

    private fun updateSeats(seats: List<Seat>) {
        val groupedSeats = seats.groupBy { it.rowNum }
            .toSortedMap(compareBy { it?.toIntOrNull() ?: 0 }) // ряды по порядку

        // Определяем максимальное количество мест в ряду
        val maxPlacesInRow = groupedSeats.maxOfOrNull { it.value.size } ?: 1
        (seatsRecyclerView.layoutManager as? GridLayoutManager)?.spanCount = maxPlacesInRow

        // Создаем список для адаптера с “фиктивными” пустыми местами, чтобы сетка не съезжала
        val sortedSeats = mutableListOf<Seat>()
        groupedSeats.forEach { (_, rowSeats) ->
            val sortedRow = rowSeats.sortedBy { it.place }.toMutableList()
            // Добавляем пустые места, если в ряду меньше, чем max
            while (sortedRow.size < maxPlacesInRow) {
                sortedRow.add(Seat(null, null, "", "")) // пустой объект Seat
            }
            sortedSeats.addAll(sortedRow)
        }

        seatsAdapter.updateData(sortedSeats)
    }

    private fun seatTypeOrder(type: String?): Int {
        return when (type) {
            "VIP" -> 0
            "COMFORT" -> 1
            "STANDARD" -> 2
            else -> 3
        }
    }

    private fun onSeatClicked(seat: Seat) {
        if (selectedSeats.contains(seat)) {
            selectedSeats.remove(seat)
        } else {
            if (selectedSeats.size >= maxSelectionPlace) {
                Toast.makeText(
                    requireContext(),
                    "Можно выбрать не более $maxSelectionPlace мест",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            selectedSeats.add(seat)
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
            totalPriceText.text = "Итого: $totalPrice сомони"
        }
    }

    private fun openPaymentScreen() {
        Toast.makeText(requireContext(), "Открываем оплату", Toast.LENGTH_SHORT).show()
    }
}