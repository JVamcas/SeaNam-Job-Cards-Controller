package com.jvmtechs.repos

import com.jvmtechs.model.JobCard
import com.jvmtechs.model.OrderNumber
import com.jvmtechs.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.Session
import tornadofx.*
import java.util.*

class OrderNoRepo: AbstractRepo<OrderNumber>() {

    suspend fun loadAll():Results{
        var session: Session? = null
        return try {
            withContext(Dispatchers.IO) {
                session = sessionFactory?.openSession()
                val qryStr = "SELECT * FROM seanam_job_cards.ordernumber where deleted=false"

                val data = session?.createNativeQuery(qryStr, OrderNumber::class.java)?.resultList?.asObservable()
                    ?: observableListOf()

                Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)
            }
        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }

    suspend fun deleteOrderNumber(item: OrderNumber): Results {
        item.deletedProperty.set(true)
        return updateModel(item)
    }
}