package com.jvmtechs.controllers.home

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXCheckBox
import com.jfoenix.controls.JFXComboBox
import com.jvmtechs.controllers.AbstractModelTableController
import com.jvmtechs.model.*
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
import de.jensd.fx.glyphs.GlyphsBuilder


class HomeController : AbstractModelTableController<JobCard>("") {

    override val root: BorderPane by fxml("/view/HomeView.fxml")

    /** Start of [JobCard]**/
    private val startDateHBox: HBox by fxid("startTimeHbox")
    private val endTimeHBox: HBox by fxid("endTimeHbox")
    private val jobClassCombo: JFXComboBox<JobClass> by fxid("jobClassCombo")
    private val orderNoCombo: JFXComboBox<OrderNumber> by fxid("orderNoCombo")
    private val jobAreaCombo: JFXComboBox<WorkArea> by fxid("jobAreaCombo")
    private val jobCardNo: TextField by fxid("jobCardNo")
    private val jobDescription: TextArea by fxid("jobDescription")
    private val otherExplanations: TextArea by fxid("otherExplanations")
    private val workDoneSats: JFXCheckBox by fxid("workDoneSats")
    private val needReplacement: JFXCheckBox by fxid("needReplacement")
    private val recurringJob: JFXCheckBox by fxid("recurringJob")
    private val timeSatisfactory: JFXCheckBox by fxid("timeSatisfactory")
    private val doneToSpecs: JFXCheckBox by fxid("doneToSpecs")
    private val saveJobCardBtn: JFXButton by fxid("saveJobCardBtn")
    private val clearJobCardBtn: JFXButton by fxid("clearJobCardBtn")

    /** End of [JobCard]**/

    /** Start of [JobCardQuery]**/
    private val qrEmployeeCombo: JFXComboBox<User> by fxid("qrEmployeeCombo")
    private val qrJobClassCombo: JFXComboBox<JobClass> by fxid("qrJobClassCombo")
    private val qrWorkAreaCombo: JFXComboBox<WorkArea> by fxid("qrWorkAreaCombo")
    private val qrJobCardNo: TextField by fxid("qrJobCardNo")
    private val fromDateHBox: HBox by fxid("fromDateHBox")
    private val toDateHBox: HBox by fxid("toDateHBox")
    private val qrSearchJobCardBtn: JFXButton by fxid("qrSearchJobCardBtn")
    private val qrClearJobCardBtn: JFXButton by fxid("qrClearJobCardBtn")

    /** End of [JobCardQuery]**/

    private lateinit var tableView: TableView<JobCard>

    init {
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

            }
            orderNoCombo.apply {

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
                            val loadResults = UserRepo().loadEmployees()
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
                add(tableView)
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