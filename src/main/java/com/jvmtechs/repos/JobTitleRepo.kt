package com.jvmtechs.repos

import com.jvmtechs.model.JobTitle
import com.jvmtechs.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.Session
import tornadofx.*

class JobTitleRepo : AbstractRepo<JobTitle>() {

    suspend fun loadAll(): Results {
        var session: Session? = null
        return try {
            withContext(Dispatchers.IO) {
                session = sessionFactory?.openSession()
                val qryStr = "SELECT * FROM jobtitle where deleted=false"
                val data = session?.createNativeQuery(qryStr, JobTitle::class.java)?.resultList?.asObservable()
                    ?: observableListOf()

                Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)
            }
        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }

    suspend fun deleteJobTitle(it: JobTitle): Results {
        it.deletedProperty.set(true)
        return updateModel(it)
    }
}