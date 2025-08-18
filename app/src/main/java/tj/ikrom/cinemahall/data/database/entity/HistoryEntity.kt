package tj.ikrom.cinemahall.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import tj.ikrom.cinemahall.data.database.entity.converter.PaymentConverter
import tj.ikrom.cinemahall.data.network.model.Seat

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cinemaName: String,
    val hall: String,
    @TypeConverters(PaymentConverter::class)
    val seats: List<Seat>,
    val totalPrice: String,
)
