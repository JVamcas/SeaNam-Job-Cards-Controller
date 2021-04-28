package com.jvmtechs.model

import com.jvmtechs.utils.SimpleBooleanConvertor
import com.jvmtechs.utils.SimpleStringConvertor
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javax.persistence.*

@Entity
@Table(name = "JobClass")
class JobClass(
    classNo: String? = null,
    classname: String? = null,
    deleted: Boolean = false
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null

    @Column(name = "classNo", nullable = false)
    @Convert(converter = SimpleStringConvertor::class)
    val classNoProperty = SimpleStringProperty(classNo)

    @Column(name = "className", nullable = false)
    @Convert(converter = SimpleStringConvertor::class)
    val classNameProperty = SimpleStringProperty(classname)

    @Column(name = "deleted", nullable = false)
    @Convert(converter = SimpleBooleanConvertor::class)
    val deletedProperty = SimpleBooleanProperty(deleted)

}