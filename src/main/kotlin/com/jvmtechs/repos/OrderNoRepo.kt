package com.jvmtechs.repos

import com.jvmtechs.model.JobCard
import com.jvmtechs.model.OrderNumber
import com.jvmtechs.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.Session
import tornadofx.*

class OrderNoRepo: AbstractRepo<OrderNumber>() {

    suspend fun loadAll(jobCard: JobCard):Results{
        var session: Session? = null
        return try {
            withContext(Dispatchers.IO) {
                session = sessionFactory?.openSession()
                val qryStr = "SELECT * FROM seanam_job_cards.ordernumber where deleted=false and job_card_id=:jobCardId"

                val data = session?.createNativeQuery(qryStr, OrderNumber::class.java)
                    ?.setParameter("jobCardId",jobCard.id)
                    ?.resultList
                    ?.asObservable()
                    ?:observableListOf()

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