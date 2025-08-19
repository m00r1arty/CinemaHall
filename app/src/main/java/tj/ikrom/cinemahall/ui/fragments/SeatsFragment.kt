package tj.ikrom.cinemahall.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.zoom.ZoomLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tj.ikrom.cinemahall.R
import tj.ikrom.cinemahall.data.database.entity.HistoryEntity
import tj.ikrom.cinemahall.data.network.model.Seat
import tj.ikrom.cinemahall.ui.adapter.SeatsAdapter
import tj.ikrom.cinemahall.ui.viewmodel.SeatsViewModel

@AndroidEntryPoint
class SeatsFragment : Fragment(R.layout.fragment_seats) {

    private val viewModel: SeatsViewModel by activityViewModels()

    private lateinit var seatsRecyclerView: RecyclerView
    private lateinit var seatsAdapter: SeatsAdapter
    private lateinit var zoomLayout: ZoomLayout

    private val maxSelectionPlace = 5
    private val selectedSeats = mutableSetOf<Seat>()
    private val pricesMap = mutableMapOf("VIP" to 0, "COMFORT" to 0, "STANDARD" to 0)

    private lateinit var bottomPanel: View
    private lateinit var totalPriceText: TextView
    private lateinit var payButton: Button
    private lateinit var historyButton: Button

    private lateinit var theaterNameText: TextView
    private lateinit var hallNameText: TextView
    private lateinit var freeSeatsCountText: TextView
    private lateinit var statusCinemaText: TextView

    private lateinit var vipPriceView: TextView
    private lateinit var comfortPriceView: TextView
    private lateinit var standardPriceView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        zoomLayout = view.findViewById(R.id.zoom)
        zoomLayout.setHasClickableChildren(true) // ВАЖНО

        seatsRecyclerView = view.findViewById(R.id.seatsRecyclerView)
        bottomPanel = view.findViewById(R.id.bottomPanel)
        totalPriceText = view.findViewById(R.id.totalPrice)
        payButton = view.findViewById(R.id.payButton)
        historyButton = view.findViewById(R.id.historyButton)

        theaterNameText = view.findViewById(R.id.theaterName)
        hallNameText = view.findViewById(R.id.hallName)
        freeSeatsCountText = view.findViewById(R.id.freeSeatsCount)
        statusCinemaText = view.findViewById(R.id.statusCinema)

        vipPriceView = view.findViewById(R.id.vip_price)
        comfortPriceView = view.findViewById(R.id.comfort_price)
        standardPriceView = view.findViewById(R.id.standard_price)

        bottomPanel.visibility = View.GONE

        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.seats.collect { seatsResponse ->
                seatsResponse?.let { response ->

                    theaterNameText.text = "Кинотеатр \"NullPointer\""
                    hallNameText.text = response.hallName
                    freeSeatsCountText.text =
                        "Свободных мест: ${response.seats?.count { it.bookedSeats == 0 } ?: 0}"
                    statusCinemaText.text = response.hasStartedText

                    response.seatsType?.forEach { seatType ->
                        pricesMap[seatType.seatType ?: ""] = seatType.price ?: 0
                    }
                    updatePriceViews()
                    response.seats?.let { updateSeats(it) }
                }
            }
        }

        viewModel.loadSeats()

        historyButton.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_seats_to_history)
        }

        payButton.setOnClickListener {
            if (selectedSeats.isEmpty()) {
                Toast.makeText(requireContext(), "Сначала выберите места", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val historyEntity = HistoryEntity(
                cinemaName = theaterNameText.text.toString(),
                hall = hallNameText.text.toString(),
                seats = selectedSeats.toList(),
                totalPrice = totalPriceText.text.toString(),
            )
            viewModel.selectPayment(historyEntity)
            Navigation.findNavController(view)
                .navigate(R.id.action_seats_to_payment)
        }
    }

    private fun setupRecyclerView() {
        seatsAdapter = SeatsAdapter(emptyList()) { seat ->
            onSeatClicked(seat)
        }
        seatsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        seatsRecyclerView.adapter = seatsAdapter
    }

    private fun updatePriceViews() {
        vipPriceView.text = "VIP: ${pricesMap["VIP"] ?: 0} TJS"
        comfortPriceView.text = "COMFORT: ${pricesMap["COMFORT"] ?: 0} TJS"
        standardPriceView.text = "STANDARD: ${pricesMap["STANDARD"] ?: 0} TJS"
    }

    private fun updateSeats(seats: List<Seat>) {
        val groupedSeats = seats.groupBy { it.rowNum }
            .toSortedMap(compareBy { it?.toIntOrNull() ?: 0 })

        val maxPlacesInRow = groupedSeats.maxOfOrNull { it.value.size + 1 } ?: 1
        (seatsRecyclerView.layoutManager as? GridLayoutManager)?.spanCount = maxPlacesInRow

        val sortedSeats = mutableListOf<Seat>()
        groupedSeats.forEach { (_, rowSeats) ->
            val sortedRow = rowSeats.sortedBy { it.place }.toMutableList()
            while (sortedRow.size < maxPlacesInRow) {
                sortedRow.add(Seat(null, null, "", ""))
            }
            sortedSeats.addAll(sortedRow)
        }
        seatsAdapter.updateData(sortedSeats)
    }

    private fun onSeatClicked(seat: Seat) {
        if (seat.rowNum == null) return // пустая ячейка
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
            val totalPrice = selectedSeats.sumOf { pricesMap[it.seatType] ?: 0 }
            totalPriceText.text = "Итого: $totalPrice сомони"
        }
    }
}
