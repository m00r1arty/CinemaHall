package tj.ikrom.cinemahall.domain.repositories

import kotlinx.coroutines.flow.Flow
import tj.ikrom.cinemahall.data.database.entity.PaymentEntity
import tj.ikrom.cinemahall.data.network.model.SeatsResponse

interface SeatsRep {

    suspend fun getSeats() : Flow<SeatsResponse?>

    suspend fun insertPayment(paymentEntity: PaymentEntity)


}