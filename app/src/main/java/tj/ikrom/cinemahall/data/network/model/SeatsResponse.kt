package tj.ikrom.cinemahall.data.network.model

import com.google.gson.annotations.SerializedName

data class SeatsResponse(
    @SerializedName("hall_name") val hallName: String? = null,
    @SerializedName("has_orzu") val hasOrzu: Boolean? = null,
    @SerializedName("has_started") val hasStarted: Boolean? = null,
    @SerializedName("has_started_text") val hasStartedText: String? = null,
    @SerializedName("map_height") val mapHeight: Int? = null,
    @SerializedName("map_width") val mapWidth: Int? = null,
    @SerializedName("merchant_id") val merchantId: Int? = null,
    @SerializedName("seats") val seats: List<Seat>? = null,
    @SerializedName("seats_type") val seatsType: List<SeatType>? = null,
    @SerializedName("session_date") val sessionDate: String? = null,
    @SerializedName("session_time") val sessionTime: String? = null,
)