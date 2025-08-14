package tj.ikrom.cinemahall.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tj.ikrom.cinemahall.data.database.entity.PaymentEntity
import tj.ikrom.cinemahall.domain.repositories.SeatsRep
import javax.inject.Inject


@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val seatsRep: SeatsRep
) : ViewModel() {

    private val _allPayments = MutableStateFlow<List<PaymentEntity>>(emptyList())
    val allPayments: StateFlow<List<PaymentEntity>> = _allPayments

    init {
        viewModelScope.launch {
            seatsRep.getAllPayment()
                .collect { payments ->
                    _allPayments.value = payments
                }
        }
    }

    fun deletePayment(paymentEntity: List<PaymentEntity>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                seatsRep.deletePayment(paymentEntity)
            }
        }
    }
}
