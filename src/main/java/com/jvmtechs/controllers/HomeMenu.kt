package com.jvmtechs.controllers


import com.jvmtechs.app.Styles
import com.jvmtechs.controllers.home.HomeController
import com.jvmtechs.controllers.user.UserController
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import tornadofx.*


class HomeMenu : AbstractView("") {

    override val root: MenuBar = menubar {
        addClass(Styles.cmenu)
        style {
            backgroundColor += c("#BDC3C7")
        }
        menu {
            graphic = Label("Job Cards").apply {
                onMouseClicked = EventHandler {
                    workspace.dock<HomeController>()
                }
            }
        }
        menu {
            graphic = Label("Users").apply {
                onMouseClicked = EventHandler {
                    workspace.dock<UserController>()
                }
            }
        }

        menu {
            graphic = Label("Logout").apply {
                onMouseClicked = EventHandler {
                    logout()
                }
            }
        }
    }
}