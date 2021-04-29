package com.jvmtechs.controllers.user

import com.jvmtechs.controllers.AbstractModelTableController
import com.jvmtechs.model.JobCard
import com.jvmtechs.model.User
import com.jvmtechs.utils.cell.DateEditingCell
import javafx.collections.ObservableList
import javafx.scene.control.TableView
import javafx.scene.layout.BorderPane
import tornadofx.*

class UserController : AbstractModelTableController<User>(title = "") {

    override val root: BorderPane by fxml("/view/UsersView.fxml")

    private lateinit var tableView: TableView<User>

    init {
        root.apply {
            center {
                tableView = tableview(modelList) {

                    columnResizePolicy = TableView.UNCONSTRAINED_RESIZE_POLICY
                    smartResize()
                    items = modelList

                    placeholder = label("No users here yet.")

                    columns.add(indexColumn)

                    column("First Name", User::firstNameProperty) {
                        contentWidth(padding = 10.0, useAsMin = true)

                    }
                    column("Last Name", User::lastNameProperty) {
                        contentWidth(padding = 10.0, useAsMin = true)
                    }

                    column("Username", User::usernameProperty) {
                        contentWidth(padding = 10.0, useAsMin = true)
                    }
                    column("UserGroup", User::userGroupProperty) {
                        contentWidth(padding = 10.0, useAsMin = true)
                    }
                    column("Job Title", User::userGroupProperty) {
                        contentWidth(padding = 10.0, useAsMin = true)
                    }
                }
            }
        }
    }


    override suspend fun loadModels(): ObservableList<User> {


        return observableListOf()
    }

}