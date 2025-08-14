package tj.ikrom.cinemahall.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import tj.ikrom.cinemahall.data.database.entity.PaymentEntity

@Dao
interface PaymentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(salomEntity: PaymentEntity)

    @Delete
    fun deletePayment(salomEntity: PaymentEntity)

    @Query("SELECT * FROM payment;")
    fun getAllPayment(): List<PaymentEntity>
}