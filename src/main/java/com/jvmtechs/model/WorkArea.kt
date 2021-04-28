package com.jvmtechs.model

import com.jvmtechs.utils.SimpleStringConvertor
import javafx.beans.property.SimpleStringProperty
import javax.persistence.*

@Entity
@Table(name = "WorkArea")
class WorkArea(
    areaName: String? = null
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null

    @Column(name = "AreaName", nullable = false)
    @Convert(converter = SimpleStringConvertor::class)
    val areaNameProperty = SimpleStringProperty(areaName)

}