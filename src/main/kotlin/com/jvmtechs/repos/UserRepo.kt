package com.jvmtechs.repos

import com.jvmtechs.model.JobTitle
import com.jvmtechs.model.User
import com.jvmtechs.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.Session
import tornadofx.*

class UserRepo : AbstractRepo<User>() {


    suspend fun authenticate(userName: String, password: String): Results {
        var session: Session? = null
        return try {
            withContext(Dispatchers.Default) {
                session = sessionFactory!!.openSession()
                val strQry = "SELECT DISTINCT * FROM users u WHERE LOWER(u.username)=:username AND u.password=:password AND not u.deleted limit 1"
                val data = session!!.createNativeQuery(strQry, User::class.java)
                    .setParameter("username", userName)
                    .setParameter("password", password)
                    .resultList.filterNotNull()
                Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Results.Error(e)
        }
        finally {
            session?.close()
        }
    }

    suspend fun loadAll(): Results {
        var session: Session? = null
        return try {
            withContext(Dispatchers.IO) {
                session = sessionFactory?.openSession()
                val qryStr = "SELECT * FROM users where deleted=false"
                val data = session?.createNativeQuery(qryStr, User::class.java)?.resultList?.asObservable()
                    ?: observableListOf()

                Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)
            }
        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }

    suspend fun deleteUser(it: User): Results {
        it.deletedProperty.set(true)
        return updateModel(it)
    }
}