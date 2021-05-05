package com.jvmtechs.controllers.home

import com.jvmtechs.controllers.AbstractModelTableController
import com.jvmtechs.controllers.jobcard.JobClassTableController
import com.jvmtechs.controllers.jobcard.WorkAreaTableController
import com.jvmtechs.model.*
import com.jvmtechs.repos.JobCardRepo
import com.jvmtechs.repos.JobTitleRepo
import com.jvmtechs.repos.UserRepo
import com.jvmtechs.utils.DateTimePicker
import com.jvmtechs.utils.Results
import com.jvmtechs.utils.cell.ComboBoxEditingCell
import com.jvmtechs.utils.cell.DateEditingCell
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
    val jobCardRepo = JobCardRepo()

    init {

        jobCardModel.item = JobCard()

        root.apply {


            startDateHBox.let { container ->

                container.children.add(
                    DateTimePicker().apply {
                        prefWidthProperty().bind(container.prefWidthProperty())
                        minWidthProperty().bind(container.minWidthProperty())
                    })
            }

            endTimeHBox.let { container ->
                container.children.add(
                    DateTimePicker().apply {
                        prefWidthProperty().bind(container.prefWidthProperty())
                        minWidthProperty().bind(container.minWidthProperty())
                    })
            }

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

            jobClassCombo.apply {
//                tooltip = Tooltip("Select job class.")
//                bindCombo(.jobTitle)
//                GlobalScope.launch {
//                    val loadResults = JobTitleRepo().loadAll()
//                    val titles = if (loadResults is Results.Success<*>)
//                        loadResults.data as ObservableList<JobTitle>
//                    else observableListOf()
//                    items = titles
//                }
            }
            orderNoMenuBtn.apply {
                enableWhen { jobCardModel.dirty }
                item("New") {
                    action {

                    }
                }
                item("View") {
                    action {

                    }
                }
            }

            jobAreaCombo.apply {

            }

            /** Start of [JobCard] view init **/

            saveJobCardBtn.apply {

            }

            clearJobCardBtn.apply {
//                graphic = FontAwesomeIconView(FontAwesomeIcon.CLOSE).apply {
//                    style {
//                        fill = c("#FFFFFF")
//                    }
//                }
            }

            /** End of [JobCard] view init **/

            /** Start of [JobCardQuery] view init **/

            qrSearchJobCardBtn.apply {
//                graphic = FontAwesomeIconView(FontAwesomeIcon.SEARCH).apply {
//                    style {
//                        fill = c("#FFFFFF")
//                    }
//                }
            }

            qrClearJobCardBtn.apply {
//                graphic = FontAwesomeIconView(FontAwesomeIcon.CLOSE).apply {
//                    style {
//                        fill = c("#FFFFFF")
//                    }
//                }
            }

            contextmenu {
                item("Work Areas") { action { find(WorkAreaTableController::class).openModal() } }
                item("Job Class") {action { find(JobClassTableController::class).openModal() }}
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
                        setCellFactory { DateEditingCell<JobCard>() }
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
                        GlobalScope.launch {
                            val loadResults = UserRepo().loadAll()
                            setCellFactory {
                                val employees = if (loadResults is Results.Success<*>)
                                    loadResults.data as ObservableList<User>
                                else observableListOf()

                                ComboBoxEditingCell(employees)
                            }
                        }
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
                }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        currentStage?.scene?.stylesheets?.add("style/style.css")
    }

    override suspend fun loadModels(): ObservableList<JobCard> {
        return observableListOf()
    }
}