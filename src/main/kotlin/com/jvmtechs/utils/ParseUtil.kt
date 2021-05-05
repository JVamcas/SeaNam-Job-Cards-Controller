package com.jvmtechs.utils


import com.jvmtechs.model.User
import javafx.beans.property.Property
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import tornadofx.*
import java.lang.Double.parseDouble

class ParseUtil {

    companion object {


        fun String?.isValidPassword() = this != null && this.length >= 4

        fun TextField.numberValidation(msg: String) =
            validator(ValidationTrigger.OnChange()) {
                if (it.isNumber())
                    null else error(msg)
            }

        fun TextField.generalTxtFieldValidation(msg: String, len: Int) =
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
        fun Int?.isOldId()  = this != null && this > 0

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