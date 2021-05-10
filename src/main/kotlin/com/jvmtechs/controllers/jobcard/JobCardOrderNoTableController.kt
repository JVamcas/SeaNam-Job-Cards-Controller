package com.jvmtechs.controllers.jobcard

import com.jvmtechs.controllers.AbstractModelTableController
import com.jvmtechs.model.*
import com.jvmtechs.repos.JobCardRepo
import com.jvmtechs.repos.JobClassRepo
import com.jvmtechs.repos.OrderNoRepo
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

class JobCardOrderNoTableController: AbstractModelTableController<OrderNumber>("") {

    override val root: GridPane by fxml("/view/JobCardOrderNoTableView.fxml")
    private val jobClassVBox: VBox by fxid("orderNoVBox")
    private val orderNo: TextField by fxid("orderNo")
    private val saveOrderNoBtn: Button by fxid("saveOrderNoBtn")
    private val clearOrderNoBtn: Button by fxid("clearOrderNoBtn")
    private val jobCard: JobCard by inject()

    private val orderNoRepo = OrderNoRepo()
    private val orderNoModel = OrderNoModel()

    private var tableView: TableView<OrderNumber>

    init {

        orderNoModel.item = OrderNumber()

        jobClassVBox.apply {
            tableView = tableview(modelList) {

                columnResizePolicy = TableView.UNCONSTRAINED_RESIZE_POLICY
                smartResize()
                items = modelList

                placeholder = label("No data here.")

                columns.add(indexColumn)
                column("Order No", OrderNumber::orderNumberProperty) {
                    contentWidth(padding = 10.0, useAsMin = true)
                    setCellFactory { EditingCell(orderNoRepo) }
                }

                enableCellEditing()

                contextmenu {
                    item("Delete Order") {
                        action {
                            GlobalScope.launch {
                                val results = selectedItem?.let { orderNoRepo.deleteOrderNumber(it) }
                                if (results is Results.Success<*>)
                                    onRefresh()
                                else results?.let { parseResults(it) }
                            }
                        }
                    }
                }
            }
        }

        orderNo.apply {
            bind(orderNoModel.orderNumber)
            validator(ValidationTrigger.OnChange()) {
                if (text.isNumber())
                    null
                else error("Enter an integer value.")
            }
        }


        saveOrderNoBtn.apply {
            enableWhen { orderNoModel.valid }

            action {
                orderNoModel.commit()
                GlobalScope.launch {
                    val orderNo = orderNoModel.item.also { }
                    val results = orderNoRepo.addNewModel(orderNo)
                    if (results is Results.Success<*>) {
                        orderNoModel.item = OrderNumber()
                        onRefresh()
                        return@launch
                    }
                    parseResults(results)
                }
            }
        }

        clearOrderNoBtn.apply { action { orderNoModel.item = OrderNumber() } }
        orderNoModel.validate(decorateErrors = false)
    }


    override fun onDock() {
        super.onDock()
        currentStage?.isResizable = false
        title = "Job Class"
    }

    override suspend fun loadModels(): ObservableList<OrderNumber> {

        val loadResults = orderNoRepo.loadAll()
        if (loadResults is Results.Success<*>)
            return loadResults.data as ObservableList<OrderNumber>
        return observableListOf()
    }
}