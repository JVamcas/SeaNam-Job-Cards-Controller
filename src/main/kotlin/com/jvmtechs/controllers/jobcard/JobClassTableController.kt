package com.jvmtechs.controllers.jobcard

import com.jvmtechs.controllers.AbstractModelTableController
import com.jvmtechs.model.JobClass
import com.jvmtechs.model.JobClassModel
import com.jvmtechs.repos.JobClassRepo
import com.jvmtechs.utils.ParseUtil.Companion.isNumber
import com.jvmtechs.utils.Results
import com.jvmtechs.utils.cell.EditingCell
import javafx.collections.ObservableList
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tornadofx.*

class JobClassTableController: AbstractModelTableController<JobClass>("") {

    override val root: GridPane by fxml("/view/JobClassTableView.fxml")
    private val jobClassVBox: VBox by fxid("jobClassVBox")
    private val className: TextField by fxid("className")
    private val classNo: TextField by fxid("classNo")
    private val saveJobClassBtn: Button by fxid("saveJobClassBtn")
    private val clearJobClassBtn: Button by fxid("clearJobClassBtn")

    private val jobClassRepo = JobClassRepo()
    private val jobClassModel = JobClassModel()

    private var tableView: TableView<JobClass>

    init {

        jobClassModel.item = JobClass()

        jobClassVBox.apply {
            tableView = tableview(modelList) {

                columnResizePolicy = TableView.UNCONSTRAINED_RESIZE_POLICY
                smartResize()
                items = modelList

                placeholder = label("No data here.")

                columns.add(indexColumn)
                column("Class No", JobClass::classNoProperty) {
                    contentWidth(padding = 10.0, useAsMin = true)
                    setCellFactory { EditingCell(jobClassRepo) }
                }
                column("Class Name", JobClass::classNameProperty) {
                    contentWidth(padding = 10.0, useAsMin = true)
                    setCellFactory { EditingCell(jobClassRepo) }
                }

                enableCellEditing()

                contextmenu {
                    item("Delete class") {
                        action {
                            GlobalScope.launch {
                                val results = selectedItem?.let { jobClassRepo.deleteJobClass(it) }
                                if (results is Results.Success<*>)
                                    onRefresh()
                                else results?.let { parseResults(it) }
                            }
                        }
                    }
                }
            }
        }

        classNo.apply {
            bind(jobClassModel.classNo)
            validator(ValidationTrigger.OnChange()) {
                if (text.isNumber())
                    null
                else error("Enter an integer value.")
            }
        }
        className.apply {
            bind(jobClassModel.className)
            validator(ValidationTrigger.OnChange()) {
                if (text != null && text.toString().trim().length > 4)
                    null
                else error("Enter a valid name.")
            }
        }

        saveJobClassBtn.apply {
            enableWhen { jobClassModel.valid }

            action {
                jobClassModel.commit()
                GlobalScope.launch {
                    val jobClass = jobClassModel.item
                    val results = jobClassRepo.addNewModel(jobClass)
                    if (results is Results.Success<*>) {
                        jobClassModel.item = JobClass()
                        onRefresh()
                        return@launch
                    }
                    parseResults(results)
                }
            }
        }

        clearJobClassBtn.apply { action { jobClassModel.item = JobClass() } }
        jobClassModel.validate(decorateErrors = false)
    }


    override fun onDock() {
        super.onDock()
        currentStage?.isResizable = false
        title = "Job Class"
    }

    override suspend fun loadModels(): ObservableList<JobClass> {

        val loadResults = jobClassRepo.loadAll()
        if (loadResults is Results.Success<*>)
            return loadResults.data as ObservableList<JobClass>
        return observableListOf()
    }
}