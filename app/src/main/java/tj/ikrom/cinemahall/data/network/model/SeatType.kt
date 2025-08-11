package tj.ikrom.cinemahall.data.network.model

import com.google.gson.annotations.SerializedName

data class SeatType(
    @SerializedName("name") val name: String? = null,
    @SerializedName("price") val price: Int? = null,
    @SerializedName("seat_type") val seatType: String? = null,
    @SerializedName("ticket_id") val ticketId: Int? = null,
    @SerializedName("ticket_type") val ticketType: String? = null,
)