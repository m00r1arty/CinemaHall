package tj.ikrom.cinemahall.ui.adapter

import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import tj.ikrom.cinemahall.R
import tj.ikrom.cinemahall.data.network.model.Seat

class SeatsAdapter(
    private var seats: List<Seat>,
    private val onSeatClick: (Seat) -> Unit,
) : RecyclerView.Adapter<SeatsAdapter.SeatViewHolder>() {

    private val selectedSeats: MutableSet<Seat> = mutableSetOf()

    inner class SeatViewHolder(val button: Button) : RecyclerView.ViewHolder(button)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val button = Button(parent.context).apply {
            layoutParams = ViewGroup.MarginLayoutParams(60, 60).apply {
                val margin = (8 * resources.displayMetrics.density).toInt()
                setMargins(margin, margin, margin, margin)
            }
        }
        return SeatViewHolder(button)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        val seat = seats[position]
        holder.button.apply {
            textSize = 8f
            text = seat.objectTitle ?: ""

            if (seat.seatType == "label") {
                background = null
                setTextColor(ContextCompat.getColor(context, R.color.gray))
                isEnabled = false
                isClickable = false
            } else {
                val defaultBackground = when (seat.seatType) {
                    "VIP" -> ContextCompat.getDrawable(context, R.drawable.bg_vip_seat)
                    "COMFORT" -> ContextCompat.getDrawable(context, R.drawable.bg_comfort_seat)
                    "STANDARD" -> ContextCompat.getDrawable(context, R.drawable.bg_standard_seat)
                    else -> ContextCompat.getDrawable(context, android.R.color.transparent)
                }
                background = if (selectedSeats.contains(seat)) {
                    ContextCompat.getDrawable(context, R.drawable.bg_selected_seat)
                } else {
                    defaultBackground
                }

                setTextColor(
                    when (seat.seatType) {
                        "VIP", "COMFORT", "STANDARD" -> ContextCompat.getColor(context, R.color.black)
                        else -> ContextCompat.getColor(context, R.color.transparent)
                    }
                )

                val isSelectable = seat.seatType in listOf("VIP", "COMFORT", "STANDARD") && seat.bookedSeats == 0
                isEnabled = isSelectable
                isClickable = isSelectable

                if (isSelectable) {
                    setOnClickListener {
                        val isSelected = selectedSeats.contains(seat)

                        // Если уже выбрано 5 мест и это не текущее выбранное — не делаем ничего
                        if (!isSelected && selectedSeats.size >= 5) {
                            Toast.makeText(
                                context,
                                "Можно выбрать не более ${selectedSeats.size} мест",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }

                        if (isSelected) {
                            selectedSeats.remove(seat)
                            background = defaultBackground
                        } else {
                            selectedSeats.add(seat)
                            background = ContextCompat.getDrawable(context, R.drawable.bg_selected_seat)
                        }
                        onSeatClick(seat)
                    }
                } else {
                    setOnClickListener(null)
                }
            }
        }
    }
    override fun getItemCount(): Int = seats.size

    fun updateData(newSeats: List<Seat>) {
        seats = newSeats
        notifyDataSetChanged()
    }
}
