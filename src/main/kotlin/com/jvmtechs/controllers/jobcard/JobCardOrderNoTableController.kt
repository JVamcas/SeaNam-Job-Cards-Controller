package com.jvmtechs.controllers.jobcard

import com.jvmtechs.controllers.AbstractModelTableController
import com.jvmtechs.controllers.home.HomeController
import com.jvmtechs.model.*
import com.jvmtechs.repos.JobCardRepo
import com.jvmtechs.repos.JobClassRepo
import com.jvmtechs.repos.OrderNoRepo
import com.jvmtechs.utils.ParseUtil.Companion.authorized
import com.jvmtechs.utils.ParseUtil.Companion.generalTxtFieldValidation
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

class JobCardOrderNoTableController : AbstractModelTableController<OrderNumber>("") {

    override val root: GridPane by fxml("/view/JobCardOrderNoTableView.fxml")
    private val jobClassVBox: VBox by fxid("orderNoVBox")
    private val orderNo: TextField by fxid("orderNo")
    private val description: TextField by fxid("description")
    private val saveOrderNoBtn: Button by fxid("saveOrderNoBtn")
    private val clearOrderNoBtn: Button by fxid("clearOrderNoBtn")
    private val jobCardTableScope = super.scope as AbstractModelTableController<JobCard>.ModelEditScope
    private val jobCardModel = jobCardTableScope.viewModel as JobCardModel

    private val orderNoRepo = OrderNoRepo()
    private val jobCardRepo = JobCardRepo()
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
                column("Description", OrderNumber::descriptionProperty) {
                    contentWidth(padding = 10.0, useAsMin = true)
                    setCellFactory { EditingCell(orderNoRepo) }
                }

                enableCellEditing()

                contextmenu {

                    item("Delete Order") {
                        enableWhen {
                            val user = Account.currentUser.get()
                            user.permission!!.deleteOrderNumberProp.authorized(user)
                        }
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

        description.apply {
            bind(orderNoModel.description)
            generalTxtFieldValidation("Enter a short description.", 20)
        }


        saveOrderNoBtn.apply {
            enableWhen { orderNoModel.valid }

            action {
                orderNoModel.commit()
                GlobalScope.launch {
                    val jobCard = jobCardModel.item
                    jobCard.orderList = jobCard.orderList
                        .map { it }
                        .toMutableList()
                        .also { it.add(orderNoModel.item) }

                    val results = jobCardRepo.updateModel(jobCard)
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
        title = "Order Numbers"
    }

    override suspend fun loadModels(): ObservableList<OrderNumber> {
        val loadResults = orderNoRepo.loadAll(jobCard = jobCardModel.item)
        if (loadResults is Results.Success<*>)
            return loadResults.data as ObservableList<OrderNumber>
        return observableListOf()
    }
}