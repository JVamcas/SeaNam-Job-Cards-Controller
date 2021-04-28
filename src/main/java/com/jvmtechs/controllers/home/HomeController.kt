package com.jvmtechs.controllers.home

import com.jvmtechs.controllers.AbstractModelTableController
import com.jvmtechs.controllers.AbstractView
import com.jvmtechs.model.JobCard
import javafx.collections.ObservableList
import javafx.scene.control.ScrollPane
import javafx.scene.control.TableView
import javafx.scene.control.TitledPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import kotlinx.coroutines.Job
import tornadofx.*

class HomeController: AbstractModelTableController<JobCard>("") {

    override val root: BorderPane by fxml("/view/HomeView.fxml")
    private lateinit var scrollPane: ScrollPane
    private val jobCardView : GridPane by fxml("/view/JobCardView.fxml")
    private val jobCardSearch: GridPane by fxml("/view/jobCardSearch.fxml")


    private lateinit var tableView: TableView<JobCard>

    init {
        root.apply {
            scrollPane = root.center as ScrollPane
            left{
                vbox (10.0){
                    style = "-fx-background-color: #045A6B"
                   add(jobCardView)
                    add(jobCardSearch)
                }
            }

            center{
                tableView = tableview(modelList){
                    smartResize()
                    prefWidthProperty().bind(scrollPane.widthProperty())
                    prefHeightProperty().bind(scrollPane.heightProperty())

                    items = modelList

                    placeholder = label("No Job Cards here yet.")

                    columns.add(indexColumn)

                }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        currentStage?.scene?.stylesheets?.add("style/style.css")
    }

    override suspend fun loadModels(): ObservableList<JobCard> {
        return observableListOf()
    }
}