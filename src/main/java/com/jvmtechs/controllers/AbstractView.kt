package com.jvmtechs.controllers

import com.jvmtechs.controllers.AbstractView.Account.currentUser
import com.jvmtechs.controllers.home.HomeController
import com.jvmtechs.controllers.user.LoginController
import com.jvmtechs.model.User
import com.jvmtechs.utils.ParseUtil.Companion.isInvalid
import com.jvmtechs.utils.Results
import javafx.application.Platform
import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.Alert
import javafx.scene.control.ComboBox
import tornadofx.*

abstract class AbstractView(private val viewTitle: String) : View(viewTitle) {


    object Account{
        val currentUser = SimpleObjectProperty<User>()
    }


    init {

        currentUser.addListener { _, _, newUser ->
            with(workspace) {
                if (!newUser.isInvalid()) {
                    header.show()
                    dock<HomeController>()
                    if(!find(HomeMenu::class.java).isDocked)
                        add(HomeMenu::class)

                    currentStage?.isResizable = true
                }
                else {
                    find(HomeMenu::class.java).removeFromParent()
                    dock<LoginController>()
                }
            }
        }
    }

    fun UIComponent.closeView() {
        Platform.runLater {
            close()
        }
    }


    fun showError(header: String, msg: String) {
        Platform.runLater {
            Alert(Alert.AlertType.ERROR).apply {
                title = "Error"
                headerText = header
                contentText = msg
                showAndWait()
            }
        }
    }

    fun parseResults(results: Results) {
        if (results is Results.Success<*>) {

        } else if (results is Results.Error) {

            when (results.code) {

                Results.Error.CODE.UNKNOWN -> {
                    showError(
                        header = "Unknown Error", msg = "An unknown error has occurred. What to do:\n" +
                                "1.  Restart the program.\n" +
                                "2. If the error persists, please contact the Petrus Kambala @ 081 326 4666."
                    )
                }
            }
        }
    }

    fun <T> ComboBox<T>.bindCombo(property: Property<T>) {
        bind(property)
        bindSelected(property)
    }

    override fun onDock() {
        super.onDock()
        title = "SeaNam Task Manager                  ${if (currentUser.get().isInvalid())"" else currentUser.get()?.toString()}"
        heading = viewTitle
    }

    fun isLoggedIn() = currentUser.get().isInvalid()

    fun logout() {
        currentUser.set(User())
    }
}