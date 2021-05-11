package com.jvmtechs.model

import com.jvmtechs.utils.SimpleBooleanConvertor
import com.jvmtechs.utils.SimpleStringConvertor
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import org.hibernate.criterion.Order
import tornadofx.*
import javax.persistence.*

@Entity
@Table(name = "OrderNumber")
class OrderNumber(
    orderNumber: String? = null,
    deleted: Boolean = false,
    description: String? = null
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null

    @Column(name = "OrderNumber", nullable = false)
    @Convert(converter = SimpleStringConvertor::class)
    val orderNumberProperty = SimpleStringProperty(orderNumber)

    @Column(name = "deleted", nullable = false)
    @Convert(converter = SimpleBooleanConvertor::class)
    val deletedProperty = SimpleBooleanProperty(deleted)

    @Column(name = "description", nullable = false)
    @Convert(converter = SimpleStringConvertor::class)
    val descriptionProperty = SimpleStringProperty(description)
}

class OrderNoModel : ItemViewModel<OrderNumber>() {
    var orderNumber = bind(OrderNumber::orderNumberProperty)
    var description = bind(OrderNumber::descriptionProperty)
}