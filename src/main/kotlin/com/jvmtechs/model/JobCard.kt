package com.jvmtechs.model

import com.jvmtechs.utils.SimpleBooleanConvertor
import com.jvmtechs.utils.SimpleDateConvertor
import com.jvmtechs.utils.SimpleLocalDateTimeConvertor
import com.jvmtechs.utils.SimpleStringConvertor
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.sql.Timestamp
import java.time.LocalDateTime
import javax.persistence.*


enum class JobType{
    Proactive,
    Production,
    Safety,
    Planned,
    Breakdown,
    Other
}

@Entity
@Table(name = "Jobcard")
class JobCard(

    deleted: Boolean = false,
    jobDescription: String? = null,
    workArea: WorkArea? = null,
    jobClass: JobClass? = null,
    employee: User? = null,
    createDate: Timestamp? = null,
    startDate: Timestamp? = null,
    endDate: Timestamp? = null,
    IsWorkDoneSatisfactory: Boolean = false,
    IsNeedReplacement: Boolean = false,
    IsRecurringJob: Boolean = false,
    IsTimeFrameSatisfactory: Boolean = false,
    IsJobDoneToExpectations: Boolean = false,
    otherExplanation: String? = null,
    supervisor: User? = null,
    jobCardNo: String? = null,
    jobType: JobType = JobType.Planned
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null

    @Column(name = "deleted", nullable = false)
    @Convert(converter = SimpleBooleanConvertor::class)
    val deletedProperty = SimpleBooleanProperty(deleted)

    @Column(name = "job_card_no")
    @Convert(converter = SimpleStringConvertor::class)
    val jobCardNoProperty = SimpleStringProperty(jobCardNo)

    @Column(name = "job_description")
    @Convert(converter = SimpleStringConvertor::class)
    val jobDescriptionProperty = SimpleStringProperty(jobDescription)

    @Transient
    val workAreaProperty = SimpleObjectProperty(workArea)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "work_area_id")
    var workArea: WorkArea? = null
        set(value) {
            field = value
            workAreaProperty.set(value)
        }

    @Transient
    val supervisorProperty = SimpleObjectProperty(supervisor)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supervisor_id")
    var supervisor: User? = null
        set(value) {
            field = value
            supervisorProperty.set(value)
        }

    @Transient
    val employeeProperty = SimpleObjectProperty(employee)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    var employee: User? = null
        set(value) {
            field = value
            employeeProperty.set(value)
        }

    @Transient
    val jobClassProperty = SimpleObjectProperty(jobClass)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_class_id")
    var jobClass: JobClass? = null
        set(value) {
            field = value
            jobClassProperty.set(value)
        }

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "job_card_id")
    var orderList = listOf<OrderNumber>()

    @Column(name = "create_date", nullable = true)
    @Convert(converter = SimpleDateConvertor::class)
    val createDateProperty = SimpleObjectProperty<Timestamp>(createDate)

    @Column(name = "start_date", nullable = true)
    @Convert(converter = SimpleDateConvertor::class)
    val startDateProperty = SimpleObjectProperty<Timestamp>(startDate)

    @Column(name = "end_date", nullable = false)
    @Convert(converter = SimpleDateConvertor::class)
    val endDateProperty = SimpleObjectProperty<Timestamp>(endDate)

    @Column(name = "is_Work_Done_Satisfactory", nullable = false)
    @Convert(converter = SimpleBooleanConvertor::class)
    val isWorkDoneSatisfactoryProperty = SimpleBooleanProperty(IsWorkDoneSatisfactory)

    @Column(name = "Is_Need_Replacement", nullable = false)
    @Convert(converter = SimpleBooleanConvertor::class)
    val isNeedReplacementProperty = SimpleBooleanProperty(IsNeedReplacement)

    @Column(name = "Is_RecurringJob", nullable = false)
    @Convert(converter = SimpleBooleanConvertor::class)
    val isRecurringJobProperty = SimpleBooleanProperty(IsRecurringJob)

    @Column(name = "Is_Time_Frame_Satisfactory", nullable = false)
    @Convert(converter = SimpleBooleanConvertor::class)
    val isTimeFrameSatisfactoryProperty = SimpleBooleanProperty(IsTimeFrameSatisfactory)

    @Column(name = "Is_Job_Done_To_Expectations", nullable = false)
    @Convert(converter = SimpleBooleanConvertor::class)
    val isJobDoneToExpectationsProperty = SimpleBooleanProperty(IsJobDoneToExpectations)

    @Column(name = "other_explanation")
    @Convert(converter = SimpleStringConvertor::class)
    val otherExplanationProperty = SimpleStringProperty(otherExplanation)

    @Column(name = "job_Type")
    @Convert(converter = SimpleStringConvertor::class)
    val jobTypeProperty = SimpleStringProperty(jobType.name)

}

class JobCardModel : ItemViewModel<JobCard>() {

    val jobCardNo = bind(JobCard::jobCardNoProperty)
    val jobDescription = bind(JobCard::jobDescriptionProperty)
    val jobType = bind(JobCard::jobTypeProperty)
    val workArea = bind(JobCard::workArea)
    val supervisor = bind(JobCard::supervisor)
    val employee = bind(JobCard::employee)
    val jobClass = bind(JobCard::jobClass)
    val createDate = bind(JobCard::createDateProperty)
    val startDate = bind(JobCard::startDateProperty)
    val endDate = bind(JobCard::endDateProperty)
    val isWorkDoneSats = bind(JobCard::isWorkDoneSatisfactoryProperty)
    val isNeedReplacement = bind(JobCard::isNeedReplacementProperty)
    val isRecurring = bind(JobCard::isRecurringJobProperty)
    val isTimeFrameSatisfactory = bind(JobCard::isTimeFrameSatisfactoryProperty)
    val isJobDoneToExpectations = bind(JobCard::isJobDoneToExpectationsProperty)
    val otherExplanation = bind(JobCard::otherExplanationProperty)
}