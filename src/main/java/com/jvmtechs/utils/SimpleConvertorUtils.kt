package com.jvmtechs.utils


import javafx.beans.property.*
import java.sql.Driver
import java.sql.Timestamp
import javax.persistence.AttributeConverter

class SimpleStringConvertor : AttributeConverter<SimpleStringProperty, String> {
    override fun convertToDatabaseColumn(p0: SimpleStringProperty?): String {
        return p0?.value ?: ""
    }

    override fun convertToEntityAttribute(p0: String?): SimpleStringProperty {
        return SimpleStringProperty(p0)
    }
}

class SimpleBooleanConvertor : AttributeConverter<SimpleBooleanProperty, Boolean> {
    override fun convertToDatabaseColumn(p0: SimpleBooleanProperty): Boolean {
        return p0.value
    }

    override fun convertToEntityAttribute(p0: Boolean): SimpleBooleanProperty {
        return SimpleBooleanProperty(p0)
    }
}

//class SimpleStringConvertor : AttributeConverter<SimpleStringProperty, String> {
//    override fun convertToDatabaseColumn(p0: SimpleStringProperty?): String {
//        return p0?.value ?: ""
//    }
//
//    override fun convertToEntityAttribute(p0: String?): SimpleStringProperty {
//        return SimpleStringProperty(p0)
//    }
//}
//
//class SimpleFloatConvertor : AttributeConverter<SimpleFloatProperty, Float> {
//    override fun convertToDatabaseColumn(p0: SimpleFloatProperty?): Float {
//        return p0?.value ?: 0.0f
//    }
//
//    override fun convertToEntityAttribute(p0: Float): SimpleFloatProperty {
//        return SimpleFloatProperty(p0)
//    }
//}
//
//class SimpleBooleanConvertor : AttributeConverter<SimpleBooleanProperty, Boolean> {
//    override fun convertToDatabaseColumn(p0: SimpleBooleanProperty): Boolean {
//        return p0.value
//    }
//
//    override fun convertToEntityAttribute(p0: Boolean): SimpleBooleanProperty {
//        return SimpleBooleanProperty(p0)
//    }
//}
//
//class SimpleIntegerConvertor : AttributeConverter<SimpleIntegerProperty, Int> {
//    override fun convertToDatabaseColumn(p0: SimpleIntegerProperty?): Int {
//        return p0?.value ?: 0
//    }
//
//    override fun convertToEntityAttribute(p0: Int): SimpleIntegerProperty {
//        return SimpleIntegerProperty(p0)
//    }
//}
//
//class SimpleDriverConvertor : AttributeConverter<SimpleObjectProperty<Driver>, Driver> {
//    override fun convertToDatabaseColumn(p0: SimpleObjectProperty<Driver>): Driver {
//        return p0.get()
//    }
//
//    override fun convertToEntityAttribute(p0: Driver): SimpleObjectProperty<Driver> {
//        return SimpleObjectProperty(p0)
//    }
//}
//
//class SimpleFishConvertor : AttributeConverter<SimpleObjectProperty<Fish>, Fish> {
//    override fun convertToDatabaseColumn(p0: SimpleObjectProperty<Fish>): Fish {
//        return p0.get()
//    }
//
//    override fun convertToEntityAttribute(p0: Fish): SimpleObjectProperty<Fish> {
//        return SimpleObjectProperty(p0)
//    }
//}
//
//class SimpleFactoryConvertor : AttributeConverter<SimpleObjectProperty<Factory>, Factory> {
//    override fun convertToDatabaseColumn(p0: SimpleObjectProperty<Factory>): Factory {
//        return p0.get()
//    }
//
//    override fun convertToEntityAttribute(p0: Factory): SimpleObjectProperty<Factory> {
//        return SimpleObjectProperty(p0)
//    }
//}
//
class SimpleDateConvertor : AttributeConverter<SimpleObjectProperty<Timestamp>, Timestamp> {
    override fun convertToDatabaseColumn(p0: SimpleObjectProperty<Timestamp>): Timestamp {
        return p0.get()
    }

    override fun convertToEntityAttribute(p0: Timestamp): SimpleObjectProperty<Timestamp> {
        return SimpleObjectProperty(p0)
    }
}
