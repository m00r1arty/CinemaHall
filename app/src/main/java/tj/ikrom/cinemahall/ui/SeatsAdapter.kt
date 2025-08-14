package tj.ikrom.cinemahall.ui

import android.view.ViewGroup
import android.widget.Button
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import tj.ikrom.cinemahall.R
import tj.ikrom.cinemahall.data.network.model.Seat

class SeatsAdapter(
    private var seats: List<Seat>,
    private val onSeatClick: (Seat) -> Unit
) : RecyclerView.Adapter<SeatsAdapter.SeatViewHolder>() {

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
                // Метка ряда
                background = null
                setTextColor(ContextCompat.getColor(context, R.color.black))
                isEnabled = false
                isClickable = false
            } else {
                // Обычное место
                background = when (seat.seatType) {
                    "VIP" -> ContextCompat.getDrawable(context, R.drawable.bg_vip_seat)
                    "COMFORT" -> ContextCompat.getDrawable(context, R.drawable.bg_comfort_seat)
                    "STANDARD" -> ContextCompat.getDrawable(context, R.drawable.bg_standard_seat)
                    else -> ContextCompat.getDrawable(context, android.R.color.transparent)
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
                    setOnClickListener { onSeatClick(seat) }
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
