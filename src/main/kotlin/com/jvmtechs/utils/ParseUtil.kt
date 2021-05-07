package com.jvmtechs.utils


import com.jvmtechs.model.User
import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.control.ComboBox
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import tornadofx.*
import java.lang.Double.parseDouble
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ParseUtil {

    companion object {


        fun String?.isValidPassword() = this != null && this.length >= 4

        fun TextField.numberValidation(msg: String) =
            validator(ValidationTrigger.OnChange()) {
                if (it.isNumber())
                    null else error(msg)
            }

        fun SimpleObjectProperty<Timestamp>.pickerBind(other: SimpleObjectProperty<LocalDateTime?>) {
            other.addListener { _, _, newValue ->
                when (newValue) {
                    null -> this.set(null)
                    else -> this.set(Timestamp.valueOf(other.get()))
                }
            }

            this.addListener { _, _, newValue ->
                when (newValue) {
                    null -> other.set(null)
                    else -> other.set(this.get().toLocalDateTime())
                }
            }
        }

        fun TextArea.numberValidation(msg: String) =
            validator(ValidationTrigger.OnChange()) {
                if (it.isNumber())
                    null else error(msg)
            }

        fun TextField.generalTxtFieldValidation(msg: String, len: Int) =
            validator(ValidationTrigger.OnChange()) {
                if (!text.isNullOrEmpty() && text.toString().length >= len)
                    null else error(msg)
            }

        fun TextArea.generalTxtFieldValidation(msg: String, len: Int) =
            validator(ValidationTrigger.OnChange()) {
                if (!text.isNullOrEmpty() && text.toString().length >= len)
                    null else error(msg)
            }

        fun Any?.isNumber(): Boolean {
            val value = this.toString()
            return value != "null" &&
                    try {
                        parseDouble(value)
                        true
                    } catch (e: Exception) {
                        false
                    }
        }

        fun Int?.isOldId() = this != null && this > 0

        fun String?.strip(): String {
            return this?.trim() ?: ""
        }

        fun User?.isInvalid() = this == null || this.id == null

        fun <T> ComboBox<T>.bindCombo(property: Property<T>) {
            bind(property)
            bindSelected(property)
        }
    }
}