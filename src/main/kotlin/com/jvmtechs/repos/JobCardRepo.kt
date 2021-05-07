package com.jvmtechs.repos

import com.jvmtechs.model.JobCard
import com.jvmtechs.model.User
import com.jvmtechs.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.Session
import tornadofx.*

class JobCardRepo: AbstractRepo<JobCard>() {


    suspend fun loadAll(): Results {
        var session: Session? = null
        return try {
            withContext(Dispatchers.IO) {
                session = sessionFactory?.openSession()
                val qryStr = "SELECT * FROM jobcard where deleted=false"

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

    suspend fun deleteJobCard(item: JobCard): Results {
        item.deletedProperty.set(true)
        return updateModel(item)
    }
}