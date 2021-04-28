package com.jvmtechs.controllers


import com.jvmtechs.app.Styles
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

                }
            }
        }
        menu {
            graphic = Label("Users").apply {
                onMouseClicked = EventHandler {

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