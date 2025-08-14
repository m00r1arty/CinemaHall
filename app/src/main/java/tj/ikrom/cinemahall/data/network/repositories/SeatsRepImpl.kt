package tj.ikrom.cinemahall.data.network.repositories

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import tj.ikrom.cinemahall.data.database.dao.PaymentDao
import tj.ikrom.cinemahall.data.database.entity.PaymentEntity
import tj.ikrom.cinemahall.data.network.api.Api
import tj.ikrom.cinemahall.data.network.model.SeatsResponse
import tj.ikrom.cinemahall.data.network.service.NetworkService.Companion.handleCall
import tj.ikrom.cinemahall.domain.repositories.SeatsRep
import javax.inject.Inject

class SeatsRepImpl @Inject constructor(
    private val api: Api,
    private val paymentDao: PaymentDao,
) : SeatsRep {

    override suspend fun getSeats(): Flow<SeatsResponse?> = flow {
        val call = api.getSeats()
        val result = handleCall(call)
        emit(result)
    }.flowOn(IO)

    override suspend fun insertPayment(paymentEntity: PaymentEntity) {
        try {
            withContext(IO) {
                paymentDao.insertPayment(paymentEntity)
            }
        } catch (e: SQLiteConstraintException) {
            val message = "Invalid saving: ${e.localizedMessage}"
            Log.e("InsertInvoice", message)
        }
    }

    override fun getAllPayment(): Flow<List<PaymentEntity>> = flow {
        emit(paymentDao.getAllPayment())
    }.flowOn(IO)
}
