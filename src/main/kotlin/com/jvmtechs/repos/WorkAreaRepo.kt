package com.jvmtechs.repos

import com.jvmtechs.model.WorkArea
import com.jvmtechs.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.Session
import tornadofx.*

class WorkAreaRepo: AbstractRepo<WorkArea> (){

    suspend fun loadAll(): Results {
        var session: Session? = null
        return try {
            withContext(Dispatchers.IO) {
                session = sessionFactory?.openSession()
                val qryStr = "SELECT * FROM workArea where deleted=false"
                val data = session?.createNativeQuery(qryStr, WorkArea::class.java)?.resultList?.asObservable()
                    ?: observableListOf()
                Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)
            }
        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }

    suspend fun deleteWorkArea(it: WorkArea): Results {
        it.deletedProperty.set(true)
        return updateModel(it)
    }
}