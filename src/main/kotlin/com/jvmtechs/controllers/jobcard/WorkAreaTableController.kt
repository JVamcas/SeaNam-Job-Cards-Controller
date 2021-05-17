package com.jvmtechs.controllers.jobcard

import com.jvmtechs.controllers.AbstractModelTableController
import com.jvmtechs.model.WorkArea
import com.jvmtechs.model.WorkAreaModel
import com.jvmtechs.repos.WorkAreaRepo
import com.jvmtechs.utils.ParseUtil.Companion.authorized
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

class WorkAreaTableController : AbstractModelTableController<WorkArea>("Work Areas") {

    override val root: GridPane by fxml("/view/WorkAreaTableView.fxml")
    private val workAreaVBox: VBox by fxid("workAreaVBox")
    private val areaName: TextField by fxid("areaName")
    private val saveJobTitleBtn: Button by fxid("saveJobTitleBtn")
    private val clearJobTitleBtn: Button by fxid("clearJobTitleBtn")

    private val workAreaRepo = WorkAreaRepo()
    private val workAreaModel = WorkAreaModel()

    private var tableView: TableView<WorkArea>

    init {

        workAreaModel.item = WorkArea()

        workAreaVBox.apply {
            tableView = tableview(modelList) {

                columnResizePolicy = TableView.UNCONSTRAINED_RESIZE_POLICY
                smartResize()
                items = modelList

                placeholder = label("No data here.")

                columns.add(indexColumn)
                column("Area Name", WorkArea::areaNameProperty) {
                    contentWidth(padding = 10.0, useAsMin = true)
                    setCellFactory { EditingCell(workAreaRepo) }
                }

                enableCellEditing()

                contextmenu {
                    item("Delete Area") {
                        enableWhen {
                            val user = Account.currentUser.get()
                            user.permission!!.deleteWorkAreaProp.authorized(user)
                        }
                        action {
                            GlobalScope.launch {
                                val results = selectedItem?.let { workAreaRepo.deleteWorkArea(it) }
                                if (results is Results.Success<*>)
                                    onRefresh()
                                else results?.let { parseResults(it) }
                            }
                        }
                    }
                }
            }
        }

        areaName.apply {
            bind(workAreaModel.areaName)
            validator(ValidationTrigger.OnChange()) {
                if (text != null && text.toString().trim().length > 4)
                    null
                else error("Enter a valid name.")
            }
        }

        saveJobTitleBtn.apply {
            enableWhen { workAreaModel.valid }

            action {
                workAreaModel.commit()
                GlobalScope.launch {
                    val workArea = workAreaModel.item
                    val results = workAreaRepo.addNewModel(workArea)
                    if (results is Results.Success<*>) {
                        workAreaModel.item = WorkArea()
                        onRefresh()
                        return@launch
                    }
                    parseResults(results)
                }
            }
        }

        clearJobTitleBtn.apply { action { workAreaModel.item = WorkArea() } }
        workAreaModel.validate(decorateErrors = false)
    }


    override fun onDock() {
        super.onDock()
        currentStage?.isResizable = false
        title = "Work Areas"
    }

    override suspend fun loadModels(): ObservableList<WorkArea> {

        val loadResults = workAreaRepo.loadAll()
        if (loadResults is Results.Success<*>)
            return loadResults.data as ObservableList<WorkArea>
        return observableListOf()
    }
}