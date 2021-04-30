package com.jvmtechs.controllers.user

import com.jfoenix.controls.JFXButton
import com.jvmtechs.controllers.AbstractModelTableController
import com.jvmtechs.model.JobTitle
import com.jvmtechs.model.JobTitleModel
import com.jvmtechs.repos.JobTitleRepo
import com.jvmtechs.utils.Results
import com.jvmtechs.utils.cell.EditingCell
import javafx.application.Platform
import javafx.collections.ObservableList
import javafx.scene.control.TableView
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.controlsfx.control.textfield.CustomTextField
import tornadofx.*

class JobTitleTableController : AbstractModelTableController<JobTitle>("Job Titles") {

    override val root: GridPane by fxml("/view/JobTitleTableView.fxml")
    private val jobTitleVBox: VBox by fxid("jobTitleVBox")
    private val titleName: CustomTextField by fxid("titleName")
    private val saveJobTitleBtn: JFXButton by fxid("saveJobTitleBtn")
    private val clearJobTitleBtn: JFXButton by fxid("clearJobTitleBtn")

    private val jobTitleRepo = JobTitleRepo()
    private val jobTitleModel = JobTitleModel()

    private var tableView: TableView<JobTitle>

    init {

        jobTitleModel.item = JobTitle()

        jobTitleVBox.apply {
            tableView = tableview(modelList) {

                columnResizePolicy = TableView.UNCONSTRAINED_RESIZE_POLICY
                smartResize()
                items = modelList

                placeholder = label("No data here.")

                columns.add(indexColumn)
                column("Employee Title", JobTitle::titleNameProperty) {
                    contentWidth(padding = 10.0, useAsMin = true)
                    setCellFactory { EditingCell(jobTitleRepo) }
                }

                enableCellEditing()

                contextmenu {
                    item("Delete Employee Title") {
                        action {
                            GlobalScope.launch {
                                val results = selectedItem?.let { jobTitleRepo.deleteJobTitle(it) }
                                if (results is Results.Success<*>)
                                    onRefresh()
                                else results?.let { parseResults(it) }
                            }
                        }
                    }
                }
            }
        }

        titleName.apply {
            bind(jobTitleModel.title)
            validator(ValidationTrigger.OnChange()) {
                if (text != null && text.toString().trim().length > 4)
                    null
                else error("Enter a valid employee title.")
            }
        }

        saveJobTitleBtn.apply {
            enableWhen { jobTitleModel.valid }

            action {
                jobTitleModel.commit()
                GlobalScope.launch {
                    val jobTitle = jobTitleModel.item
                    val results = jobTitleRepo.addNewModel(jobTitle)
                    if (results is Results.Success<*>) {
                        jobTitleModel.item = JobTitle()
                        onRefresh()
                        return@launch
                    }
                    parseResults(results)
                }
            }
        }

        clearJobTitleBtn.apply { action { jobTitleModel.item = JobTitle() } }
        jobTitleModel.validate(decorateErrors = false)
    }


    override fun onDock() {
        super.onDock()
        currentStage?.isResizable = false
        title = "Employee Titles"
    }

    override suspend fun loadModels(): ObservableList<JobTitle> {

        val loadResults = jobTitleRepo.loadAll()
        if (loadResults is Results.Success<*>)
            return loadResults.data as ObservableList<JobTitle>
        return observableListOf()
    }
}