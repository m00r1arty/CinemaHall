package tj.ikrom.cinemahall.data.network.model

import com.google.gson.annotations.SerializedName

data class SeatsResponse(
    @SerializedName("hall_name") val hallName: String,
    @SerializedName("has_orzu") val hasOrzu: Boolean,
    @SerializedName("has_started") val hasStarted: Boolean,
    @SerializedName("has_started_text") val hasStartedText: String,
    @SerializedName("map_height") val mapHeight: Int,
    @SerializedName("map_width") val mapWidth: Int,
    @SerializedName("merchant_id") val merchantId: Int,
    @SerializedName("seats") val seats: List<Seat>,
    @SerializedName("seats_type") val seatsType: List<SeatType>,
    @SerializedName("session_date") val sessionDate: String,
    @SerializedName("session_time") val sessionTime: String
)