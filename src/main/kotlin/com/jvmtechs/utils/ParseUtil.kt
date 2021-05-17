package com.jvmtechs.utils

import com.jvmtechs.controllers.AbstractView
import com.jvmtechs.model.User
import com.jvmtechs.model.UserGroup
import javafx.beans.property.Property
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.ComboBox
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import tornadofx.*
import java.lang.Double.parseDouble
import java.sql.Timestamp
import java.time.LocalDateTime

class ParseUtil {

    companion object {

        fun User?.isAdmin(): SimpleBooleanProperty {
            return SimpleBooleanProperty(this != null && UserGroup.Admin.name == this.userGroupProperty.get())
        }
        fun User?.isOwnProfile():Boolean{
            return this!=null && this.id == AbstractView.Account.currentUser.get().id
        }

        fun SimpleBooleanProperty.authorized(user: User?): SimpleBooleanProperty {
            return SimpleBooleanProperty(this.get() || user.isAdmin().get())
        }

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


