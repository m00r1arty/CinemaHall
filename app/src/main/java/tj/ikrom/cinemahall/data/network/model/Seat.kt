package tj.ikrom.cinemahall.data.network.model

import com.google.gson.annotations.SerializedName

data class Seat(
    @SerializedName("booked_seats") val bookedSeats: Int? = null,
    @SerializedName("left") val left: Int? = null,
    @SerializedName("object_description") val objectDescription: String? = null,
    @SerializedName("object_title") val objectTitle: String? = null,
    @SerializedName("object_type") val objectType: String? = null,
    @SerializedName("place") val place: String? = null,
    @SerializedName("place_name") val placeName: Any? = null,
    @SerializedName("row_num") val rowNum: String? = null,
    @SerializedName("seat_id") val seatId: Int? = null,
    @SerializedName("seat_type") val seatType: String? = null,
    @SerializedName("seat_view") val seatView: String? = null,
    @SerializedName("sector") val sector: String? = null,
    @SerializedName("top") val top: Int? = null,
)