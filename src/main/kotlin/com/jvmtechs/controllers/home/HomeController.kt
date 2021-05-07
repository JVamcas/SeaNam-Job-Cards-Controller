package com.jvmtechs.controllers.home

import com.jvmtechs.controllers.AbstractModelTableController
import com.jvmtechs.controllers.jobcard.JobClassTableController
import com.jvmtechs.controllers.jobcard.WorkAreaTableController
import com.jvmtechs.model.*
import com.jvmtechs.repos.*
import com.jvmtechs.utils.DateTimePicker
import com.jvmtechs.utils.DateUtil
import com.jvmtechs.utils.ParseUtil.Companion.generalTxtFieldValidation
import com.jvmtechs.utils.ParseUtil.Companion.isOldId
import com.jvmtechs.utils.ParseUtil.Companion.numberValidation
import com.jvmtechs.utils.ParseUtil.Companion.pickerBind
import com.jvmtechs.utils.Results
import com.jvmtechs.utils.TMDateTimePicker
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.collections.ObservableList
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.text.Text
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tornadofx.*


class HomeController : AbstractModelTableController<JobCard>("") {

    override val root: BorderPane by fxml("/view/HomeView.fxml")

    /** Start of [JobCard]**/
    private val startDateHBox: HBox by fxid("startTimeHbox")
    private val endTimeHBox: HBox by fxid("endTimeHbox")
    private val jobClassCombo: ComboBox<JobClass> by fxid("jobClassCombo")
    private val orderNoMenuBtn: MenuButton by fxid("orderNoMenuBtn")
    private val jobAreaCombo: ComboBox<WorkArea> by fxid("jobAreaCombo")
    private val jobCardNo: TextField by fxid("jobCardNo")
    private val jobDescription: TextArea by fxid("jobDescription")
    private val otherExplanations: TextArea by fxid("otherExplanations")
    private val workDoneSats: CheckBox by fxid("workDoneSats")
    private val needReplacement: CheckBox by fxid("needReplacement")
    private val recurringJob: CheckBox by fxid("recurringJob")
    private val timeSatisfactory: CheckBox by fxid("timeSatisfactory")
    private val doneToSpecs: CheckBox by fxid("doneToSpecs")
    private val saveJobCardBtn: Button by fxid("saveJobCardBtn")
    private val clearJobCardBtn: Button by fxid("clearJobCardBtn")

    /** End of [JobCard]**/

    /** Start of [JobCardQuery]**/
    private val qrEmployeeCombo: ComboBox<User> by fxid("qrEmployeeCombo")
    private val qrJobClassCombo: ComboBox<JobClass> by fxid("qrJobClassCombo")
    private val qrWorkAreaCombo: ComboBox<WorkArea> by fxid("qrWorkAreaCombo")
    private val qrJobCardNo: TextField by fxid("qrJobCardNo")
    private val fromDateHBox: HBox by fxid("fromDateHBox")
    private val toDateHBox: HBox by fxid("toDateHBox")
    private val qrSearchJobCardBtn: Button by fxid("qrSearchJobCardBtn")
    private val qrClearJobCardBtn: Button by fxid("qrClearJobCardBtn")

    /** End of [JobCardQuery]**/

    private lateinit var tableView: TableView<JobCard>

    private val jobCardModel = JobCardModel()
    private val repo = JobCardRepo()

    init {

        jobCardModel.item = JobCard(employee = Account.currentUser.get())

        root.apply {
            /** Start of [JobCard] view init **/

            workDoneSats.apply {
                bind(jobCardModel.isWorkDoneSats)
            }
            needReplacement.apply {
                bind(jobCardModel.isNeedReplacement)
            }
            recurringJob.apply {
                bind(jobCardModel.isRecurring)
            }
            timeSatisfactory.apply {
                bind(jobCardModel.isTimeFrameSatisfactory)
            }
            doneToSpecs.apply {
                bind(jobCardModel.isJobDoneToExpectations)
            }

            startDateHBox.let { container ->
                container.children.add(
                    TMDateTimePicker().apply {
                        jobCardModel.startDate.pickerBind(dateTimeValue)
                        prefWidthProperty().bind(container.prefWidthProperty())
                        minWidthProperty().bind(container.minWidthProperty())
                    })
            }

            endTimeHBox.let { container ->
                container.children.add(
                    TMDateTimePicker().apply {
                        jobCardModel.endDate.pickerBind(dateTimeValue)
                        prefWidthProperty().bind(container.prefWidthProperty())
                        minWidthProperty().bind(container.minWidthProperty())
                    })
            }

            jobCardNo.apply {
                bind(jobCardModel.jobCardNo)
                numberValidation("Please enter a valid job card no.")
            }
            jobDescription.apply {
                bind(jobCardModel.jobDescription)
                generalTxtFieldValidation("Enter at least 20 characters.", 20)
            }
            otherExplanations.apply {
                bind(jobCardModel.otherExplanation)
            }

            jobClassCombo.apply {
                tooltip = Tooltip("Select job class.")
                bindCombo(jobCardModel.jobClass)
                GlobalScope.launch {
                    val loadResults = JobClassRepo().loadAll()
                    val classList = if (loadResults is Results.Success<*>)
                        loadResults.data as ObservableList<JobClass>
                    else observableListOf()
                    items = classList
                }
            }
            orderNoMenuBtn.apply {
                item("New") {
                    action {

                    }
                }
                label("")
                item("View") {
                    action {

                    }
                }
            }

            jobAreaCombo.apply {
                tooltip = Tooltip("Select work area.")
                bindCombo(jobCardModel.workArea)
                GlobalScope.launch {
                    val loadResults = WorkAreaRepo().loadAll()
                    val areaList = if (loadResults is Results.Success<*>)
                        loadResults.data as ObservableList<WorkArea>
                    else observableListOf()
                    items = areaList
                }
            }

            saveJobCardBtn.apply {
                enableWhen { jobCardModel.dirty }
                graphic = FontAwesomeIconView(FontAwesomeIcon.SAVE).apply {
                    style {
                        fill = c("#109865")
                    }
                }
                action {
                    jobCardModel.commit()
                    GlobalScope.launch {
                        val jobCard = jobCardModel.item
                        jobCard.createDateProperty.set(DateUtil.today())
                        val results =
                            if (jobCard.id.isOldId()) repo.updateModel(model = jobCard) else repo.addNewModel(
                                jobCard
                            )
                        if (results is Results.Success<*>) {
                            jobCardModel.item = JobCard(employee = Account.currentUser.get())
                            onRefresh()
                            return@launch
                        }
                        parseResults(results)
                    }
                }
            }

            clearJobCardBtn.apply {
                graphic = FontAwesomeIconView(FontAwesomeIcon.CLOSE).apply {
                    style {
                        fill = c("#F50A43")
                    }
                }
                action {
                    jobCardModel.item = JobCard(employee = Account.currentUser.get())
                }
            }

            /** End of [JobCard] view init **/

            /** Start of [JobCardQuery] view init **/
            fromDateHBox.let { container ->
                container.children.add(
                    DateTimePicker().apply {
                        prefWidthProperty().bind(container.prefWidthProperty())
                        minWidthProperty().bind(container.minWidthProperty())
                    })
            }

            toDateHBox.let { container ->

                container.children.add(
                    DateTimePicker().apply {
                        prefWidthProperty().bind(container.prefWidthProperty())
                        minWidthProperty().bind(container.minWidthProperty())
                    })
            }
            qrSearchJobCardBtn.apply {
                graphic = FontAwesomeIconView(FontAwesomeIcon.SEARCH).apply {
                    style {
                        fill = c("#109865")
                    }
                }
            }

            qrClearJobCardBtn.apply {
                graphic = FontAwesomeIconView(FontAwesomeIcon.CLOSE).apply {
                    style {
                        fill = c("#b9162d")
                    }
                }
            }

            contextmenu {
                item("Work Areas") { action { find(WorkAreaTableController::class).openModal() } }
                item("Job Class") { action { find(JobClassTableController::class).openModal() } }
            }

            /** End of [JobCardQuery] view init **/


            center {
                tableView = tableview(modelList) {

                    columnResizePolicy = TableView.UNCONSTRAINED_RESIZE_POLICY
//                    smartResize()
                    items = modelList

                    placeholder = label("No Job Cards here yet.")

                    columns.add(indexColumn)
                    column("Date Created", JobCard::createDateProperty) {
                        contentWidth(padding = 10.0, useAsMin = true)
                    }
                    column("Job Card No", JobCard::jobCardNoProperty) {
                        contentWidth(padding = 10.0, useAsMin = true)
                    }
                    column("Work Area", JobCard::workArea) {
                        contentWidth(padding = 10.0, useAsMin = true)
                    }
                    column("Job Class", JobCard::jobClass) {
                        contentWidth(padding = 10.0, useAsMin = true)
                    }
                    column("Job Description", JobCard::jobDescriptionProperty) {
                        prefWidth = 300.0
                        minWidth = prefWidth

                        setCellFactory { col ->
                            val cell: TableCell<JobCard, String?> = TableCell()
                            val text = Text(col.text.toString())
                            cell.graphic = text
                            cell.prefHeight = Control.USE_COMPUTED_SIZE
                            text.wrappingWidthProperty().bind(widthProperty())
                            text.textProperty().bind(cell.itemProperty())
                            cell
                        }
                    }
                    column("Employee", JobCard::employee).apply {
                        contentWidth(padding = 20.0, useAsMin = true)

                    }

                    column("Done Satisfactory", JobCard::isWorkDoneSatisfactoryProperty) {
                        contentWidth(padding = 10.0, useAsMin = true)
                        prefWidth = 120.0
                        minWidth = prefWidth
                    }
                    column("Time Sufficient", JobCard::isTimeFrameSatisfactoryProperty) {
                        prefWidth = 120.0
                        minWidth = prefWidth
                        contentWidth(padding = 10.0, useAsMin = true)
                    }
                    column("Other Comments", JobCard::otherExplanationProperty) {
                        prefWidth = 300.0
                        minWidth = prefWidth

                        setCellFactory { col ->
                            val cell: TableCell<JobCard, String?> = TableCell()
                            val text = Text(col.text.toString())
                            cell.graphic = text
                            cell.prefHeight = Control.USE_COMPUTED_SIZE
                            text.wrappingWidthProperty().bind(widthProperty())
                            text.textProperty().bind(cell.itemProperty())
                            cell
                        }
                    }
                    onUserSelect {
                        jobCardModel.item = it
                    }
                }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        currentStage?.scene?.stylesheets?.add("style/style.css")
    }

    override suspend fun loadModels(): ObservableList<JobCard> {
        val loadResults = repo.loadAll()
        if (loadResults is Results.Success<*>)
            return loadResults.data as ObservableList<JobCard>
        return observableListOf()
    }

    override fun onDelete() {
        super.onDelete()
        GlobalScope.launch {
            if (jobCardModel.item.id.isOldId()) {
                val delResults = repo.deleteJobCard(jobCardModel.item)
                if (delResults is Results.Error)
                    parseResults(delResults)
                else {
                    jobCardModel.item = JobCard()
                    onRefresh()
                }
            }
        }
    }
}