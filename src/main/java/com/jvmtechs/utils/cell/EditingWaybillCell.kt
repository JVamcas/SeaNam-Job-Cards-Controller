package com.jvmtechs.utils.cell

import com.jvmtechs.controllers.home.HomeController
import com.jvmtechs.model.JobCard
import tornadofx.*


import javafx.beans.value.ObservableValue
import javafx.scene.control.TableCell
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditingWaybillCell(private val tableView: HomeController) : TableCell<JobCard, String>() {
    private lateinit var textField: TextField

    override fun startEdit() {
        if (!isEmpty) {
            super.startEdit()
            createTextField()
            text = null
            graphic = textField
            textField.selectAll()
        }
    }

    override fun cancelEdit() {
        super.cancelEdit()
        text = item
        graphic = null
    }

    override fun updateItem(item: String?, empty: Boolean) {
        super.updateItem(item, empty)
        if (empty) {
            text = item
            graphic = null
        } else {
            if (isEditing) {
                textField.text = string
                text = null
                graphic = textField
            } else {
                text = string
                graphic = null
            }
        }
    }

    private fun createTextField() {
        textField = TextField(string)
        textField.minWidth = this.width - this.graphicTextGap * 2
        textField.setOnAction { commitEdit(textField.text) }
//        FuelTransactionRepo().let { repo->
//            textField.addEventHandler(KeyEvent.KEY_PRESSED) {
//                if (it.code == KeyCode.ENTER) {
//                    val oldValue = item
//                    val newValue = textField.text
//
//                    GlobalScope.launch {
//                        val results = repo.updateWayBill(rowItem,newValue)
//                        if(results is Results.Success<*>)
//                            tableView.onRefresh()
////                            tableView.modelList.asyncItems { results.data as List<FuelTransaction> }
//                        else {
//                            tableView.tableModel.rollbackSelected()
//                            tableView.parseResults(results)
//                        }
//                    }
//                }
//            }
//        }

        textField.focusedProperty()
            .addListener { _: ObservableValue<out Boolean?>?, _: Boolean?, newValue: Boolean? ->
                if (!newValue!!) {
                    commitEdit(textField.text)
                }
            }
    }

    private val string: String
        get() = item ?: ""
}
