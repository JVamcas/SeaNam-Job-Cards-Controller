package com.jvmtechs.utils


import com.jvmtechs.model.User
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

        fun String?.strip(): String {
            return this?.trim() ?: ""
        }

        fun User?.isInvalid() = this == null || this.id == null
    }
}