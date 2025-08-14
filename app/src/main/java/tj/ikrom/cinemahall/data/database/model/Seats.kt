package tj.ikrom.cinemahall.data.database.model

import androidx.room.Entity

@Entity(tableName = "seats")
data class Seats(
    val seatType: String,
    val price: String,
)
