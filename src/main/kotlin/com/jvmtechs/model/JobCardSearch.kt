package com.jvmtechs.model

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.sql.Timestamp

class JobCardSearch {

    val jobCardNoProperty = SimpleStringProperty()
    val employeeProperty = SimpleObjectProperty<User>()
    val jobClassProperty = SimpleObjectProperty<JobClass>()
    val workAreaProperty = SimpleObjectProperty<WorkArea>()
    val fromDateProperty = SimpleObjectProperty<Timestamp>()
    val toDateProperty = SimpleObjectProperty<Timestamp>()

}

class JobCardSearchModel (search: JobCardSearch): ItemViewModel<JobCardSearch>(){

    val jobCardNo = bind(JobCardSearch::jobCardNoProperty)
    val employee = bind(JobCardSearch::employeeProperty)
    val jobClass = bind(JobCardSearch::jobClassProperty)
    val workArea = bind(JobCardSearch::workAreaProperty)
    val fromDate = bind(JobCardSearch::fromDateProperty)
    val toDate = bind(JobCardSearch::toDateProperty)

    init {
        item = search
    }
}