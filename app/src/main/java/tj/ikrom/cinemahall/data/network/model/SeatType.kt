package tj.ikrom.cinemahall.data.network.model

import com.google.gson.annotations.SerializedName

data class SeatType(
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int,
    @SerializedName("seat_type") val seatType: String,
    @SerializedName("ticket_id") val ticketId: Int,
    @SerializedName("ticket_type") val ticketType: String
)