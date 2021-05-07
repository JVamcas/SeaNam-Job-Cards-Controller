package com.jvmtechs.model

import com.jvmtechs.utils.SimpleBooleanConvertor
import com.jvmtechs.utils.SimpleStringConvertor
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.persistence.*

@Entity
@Table(name = "WorkArea")
class WorkArea(
    areaName: String? = null,
    deleted: Boolean = false
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null

    @Column(name = "AreaName", nullable = false)
    @Convert(converter = SimpleStringConvertor::class)
    val areaNameProperty = SimpleStringProperty(areaName)

    @Column(name = "deleted", nullable = false)
    @Convert(converter = SimpleBooleanConvertor::class)
    val deletedProperty = SimpleBooleanProperty(deleted)

    override fun toString():String = areaNameProperty.get()

}
class WorkAreaModel : ItemViewModel<WorkArea>() {
    var areaName = bind(WorkArea::areaNameProperty)
}