package com.jvmtechs.controllers.home

import com.jvmtechs.controllers.AbstractView
import javafx.scene.control.TitledPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import tornadofx.*

class HomeController: AbstractView("") {

    override val root: BorderPane by fxml("/view/HomeView.fxml")
    private val jobCardView : GridPane by fxml("/view/JobCardView.fxml")
    private val jobCardSearch: GridPane by fxml("/view/jobCardSearch.fxml")

    init {
        root.apply {

            left{
                vbox (10.0){
                    style = "-fx-background-color: #045A6B"
                   add(jobCardView)
                    add(jobCardSearch)
                }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        currentStage?.scene?.stylesheets?.add("style/style.css")
    }
}