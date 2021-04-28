package com.jvmtechs.model

import com.jvmtechs.utils.SimpleBooleanConvertor
import com.jvmtechs.utils.SimpleStringConvertor
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javax.persistence.*

@Entity
@Table(name = "JobTitle")
class JobTitle(
    titleName: String? = null,
    deleted: Boolean = false
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null

    @Column(name = "titleName", nullable = false)
    @Convert(converter = SimpleStringConvertor::class)
    val titleNameProperty = SimpleStringProperty(titleName)

    @Column(name = "deleted", nullable = false)
    @Convert(converter = SimpleBooleanConvertor::class)
    val deletedProperty = SimpleBooleanProperty(deleted)

}