package com.jvmtechs.controllers.user

import com.jvmtechs.controllers.AbstractModelTableController
import com.jvmtechs.model.*
import com.jvmtechs.repos.JobTitleRepo
import com.jvmtechs.repos.UserRepo
import com.jvmtechs.utils.ParseUtil.Companion.generalTxtFieldValidation
import com.jvmtechs.utils.ParseUtil.Companion.isOldId
import com.jvmtechs.utils.Results
import javafx.collections.ObservableList
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tornadofx.*

class UserController : AbstractModelTableController<User>(title = "") {

    override val root: BorderPane by fxml("/view/UsersView.fxml")
    private val jobTitle: ComboBox<JobTitle> by fxid("jobTitle")
    private val userGroup: ComboBox<String> by fxid("userGroup")
    private val firstName: TextField by fxid("firstName")
    private val lastName: TextField by fxid("lastName")
    private val userName: TextField by fxid("userName")
    private val password: TextField by fxid("password")
    private val saveUserBtn: Button by fxid("saveUserBtn")
    private val clearUserBtn: Button by fxid("clearUserBtn")

    private val userModel = UserModel()
    private val userRepo = UserRepo()
    private lateinit var tableView: TableView<User>

    init {
        userModel.item = User()
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
                    column("Job Title", User::jobTitle) {
                        contentWidth(padding = 10.0, useAsMin = true)
                    }

                    contextmenu {
                        item("View Employee Titles") {
                            action {
                                find(JobTitleTableController::class).openModal()
                            }
                        }
                    }

                    enableCellEditing()
                    onUserSelect {
                        userModel.item = it
                    }
                }
            }

            jobTitle.apply {
                tooltip = Tooltip("Select employee title.")
                bindCombo(userModel.jobTitle)
                GlobalScope.launch {
                    val loadResults = JobTitleRepo().loadAll()
                    val titles = if (loadResults is Results.Success<*>)
                        loadResults.data as ObservableList<JobTitle>
                    else observableListOf()
                    items = titles
                }
            }
            userGroup.apply {
                bindCombo(userModel.userGroup)
                tooltip = Tooltip("Select user group.")
                val groups = UserGroup.values().map { it.name }.toList().asObservable()
                items = groups
            }
            firstName.apply {
                bind(userModel.firstName)
                generalTxtFieldValidation("First name must at least have 4 chars.", 4)
            }

            lastName.apply {
                bind(userModel.lastName)
                generalTxtFieldValidation("Last name must at least have 4 chars.", 4)
            }

            userName.apply {
                bind(userModel.username)
                generalTxtFieldValidation("Username must at least have 5 chars.", 5)
            }

            password.apply {
                bind(userModel.password)
                generalTxtFieldValidation("Password must at least have 4 chars.", 4)
            }

            saveUserBtn.apply {
                enableWhen { userModel.valid }
                action {
                    userModel.commit()
                    GlobalScope.launch {
                        val driver = userModel.item
                        val results =
                            if (driver.id.isOldId()) userRepo.updateModel(model = driver) else userRepo.addNewModel(
                                driver
                            )
                        if (results is Results.Success<*>) {
                            userModel.item = User()
                            onRefresh()
                            return@launch
                        }
                        parseResults(results)
                    }
                }
            }
            clearUserBtn.apply {
                action { userModel.item = User() }
            }

            userModel.validate(decorateErrors = false)
        }
    }

    override fun onDelete() {
        super.onDelete()
        GlobalScope.launch {
            if (userModel.item.id.isOldId()) {
                val delResults = userRepo.deleteUser(userModel.item)
                if (delResults is Results.Error)
                    parseResults(delResults)
                else {
                    userModel.item = User()
                    onRefresh()
                }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        currentStage?.isMaximized = true
    }


    override suspend fun loadModels(): ObservableList<User> {

        val loadResults = userRepo.loadAll()
        if (loadResults is Results.Success<*>)
            return loadResults.data as ObservableList<User>
        return observableListOf()
    }
}