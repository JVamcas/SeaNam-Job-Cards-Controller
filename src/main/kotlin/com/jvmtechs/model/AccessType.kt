package com.jvmtechs.model

import com.azul.crs.com.fasterxml.jackson.annotation.JsonProperty
import com.jvmtechs.utils.SimpleBooleanConvertor
import javafx.beans.property.SimpleBooleanProperty
import tornadofx.*
import javax.persistence.*

@Entity
@Table(name = "accessType")
class AccessType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null

    @Column(name = "updateOffice")
    @Convert(converter = SimpleBooleanConvertor::class)
    val updateOfficeFieldProp = SimpleBooleanProperty(false)

    @Column(name = "addUser")
    @Convert(converter = SimpleBooleanConvertor::class)
    val addUserProp = SimpleBooleanProperty(false)

    @Column(name = "deleteUser")
    @Convert(converter = SimpleBooleanConvertor::class)
    val deleteUserProp = SimpleBooleanProperty(false)

    @Column(name = "addJobCard")
    @Convert(converter = SimpleBooleanConvertor::class)
    val addJobCardProp = SimpleBooleanProperty(false)

    @Column(name = "deleteJobCard")
    @Convert(converter = SimpleBooleanConvertor::class)
    val deleteJobCardProp = SimpleBooleanProperty(false)

    @Column(name = "addWorkArea")
    @Convert(converter = SimpleBooleanConvertor::class)
    val addWorkAreaProp = SimpleBooleanProperty(false)

    @Column(name = "deleteWorkArea")
    @Convert(converter = SimpleBooleanConvertor::class)
    val deleteWorkAreaProp = SimpleBooleanProperty(false)

    @Column(name = "addJobClass")
    @Convert(converter = SimpleBooleanConvertor::class)
    val addJobClassProp = SimpleBooleanProperty(false)

    @Column(name = "deleteJobClass")
    @Convert(converter = SimpleBooleanConvertor::class)
    val deleteJobClassProp = SimpleBooleanProperty(false)

    @Column(name = "addOrderNumber")
    @Convert(converter = SimpleBooleanConvertor::class)
    val addOrderNumberProp = SimpleBooleanProperty(false)

    @Column(name = "deleteOrderNumber")
    @Convert(converter = SimpleBooleanConvertor::class)
    val deleteOrderNumberProp = SimpleBooleanProperty(false)
}

class AccessTypeModel: ItemViewModel<AccessType>() {

    val updateOfficeField = bind(AccessType::updateOfficeFieldProp)
    val addUser = bind(AccessType::addUserProp)
    val deleteUser = bind(AccessType::deleteUserProp)
    val addJobCard = bind(AccessType::addJobCardProp)
    val deleteJobCard = bind(AccessType::deleteJobCardProp)
    val addWorkArea = bind(AccessType::addWorkAreaProp)
    val deleteWorkArea = bind(AccessType::deleteWorkAreaProp)
    val addJobClass = bind(AccessType::addJobClassProp)
    val deleteJobClass = bind(AccessType::deleteJobClassProp)
    val addOrderNo = bind(AccessType::addOrderNumberProp)
    val deleteOrderNo = bind(AccessType::deleteOrderNumberProp)
}
