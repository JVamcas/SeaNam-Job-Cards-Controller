package com.jvmtechs.controllers.home

import com.jvmtechs.controllers.AbstractView
import javafx.scene.layout.BorderPane

class HomeController: AbstractView("") {

    override val root: BorderPane by fxml("/view/HomeView.fxml")
}