package com.jvmtechs.repos

import com.jvmtechs.model.JobCard
import com.jvmtechs.model.JobCardSearch
import com.jvmtechs.model.OrderNumber
import com.jvmtechs.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.Session
import tornadofx.*
import java.util.*

class JobCardRepo : AbstractRepo<JobCard>() {


    suspend fun loadAll(): Results {
        var session: Session? = null
        return try {
            withContext(Dispatchers.IO) {
                session = sessionFactory?.openSession()
                val qryStr = "SELECT * FROM jobcard where deleted=false order by create_date desc"

                val data = session?.createNativeQuery(qryStr, JobCard::class.java)?.resultList?.asObservable()
                    ?: observableListOf()

                Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)
            }
        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }

    suspend fun loadOrderNo(jobCard: JobCard): Results {
        var session: Session? = null
        return try {
            withContext(Dispatchers.IO) {
                session = sessionFactory?.openSession()
                val qryStr = "SELECT * FROM seanam_job_cards.ordernumber where deleted=false and job_card_id=:jobCardId"

                val data = session?.createNativeQuery(qryStr, OrderNumber::class.java)
                    ?.setParameter("jobCardId", jobCard.id)
                    ?.resultList
                    ?.asObservable()
                    ?: observableListOf()

                Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)
            }
        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }

    suspend fun deleteJobCard(item: JobCard): Results {
        item.deletedProperty.set(true)
        return updateModel(item)
    }

    /***
     * Filter the [JobCard] based on the query parameters encapsulated inside [JobCardSearch]
     * Return a list of the JobCards
     */
    suspend fun loadFilteredModel(search: JobCardSearch): Results {
        var session: Session? = null

        val mainBuilder =
            StringBuilder("SELECT DISTINCT j.* FROM jobcard j WHERE j.id IS NOT NULL AND deleted=false")

        if (!search.jobCardNoProperty.get().isNullOrEmpty())
            mainBuilder.append(" AND j.job_card_no = ${search.jobCardNoProperty.get()}")

        if (search.employeeProperty.get() != null)
            mainBuilder.append(" AND j.employee_id = ${search.employeeProperty.get().id}")

        if (search.jobClassProperty.get() != null)
            mainBuilder.append(" AND j.job_class_id=${search.jobClassProperty.get().id}")

        if (search.workAreaProperty.get() != null)
            mainBuilder.append(" AND j.work_area_id=${search.workAreaProperty.get().id}")

        val fromDate = search.fromDateProperty.get()
        val toDate = search.toDateProperty.get()

        if (fromDate != null && toDate != null)
            mainBuilder.append(" AND j.create_date BETWEEN \'$fromDate\' AND \'$toDate\'")

        mainBuilder.append(" order by j.create_date desc")

        return try {
            withContext(Dispatchers.Default) {
                session = sessionFactory?.openSession()
                val data = session
                    ?.createNativeQuery(mainBuilder.toString(), JobCard::class.java)
                    ?.resultList
                    ?.filterNotNull()
                Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Results.Error(e)
        } finally {
            session?.close()
        }
    }
}