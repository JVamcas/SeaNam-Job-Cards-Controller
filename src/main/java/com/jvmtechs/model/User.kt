package com.jvmtechs.model

import com.jvmtechs.utils.SimpleBooleanConvertor
import com.jvmtechs.utils.SimpleStringConvertor
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.persistence.*


enum class UserGroup{Employee, Supervisor, Admin}

@Entity
@Table(name="Users")
class User(
     firstName: String? = null,
     lastName: String? = null,
     userGroup: UserGroup = UserGroup.Employee,
     jobTitle: JobTitle? = null,
     deleted: Boolean = false,
     username: String? = null,
     password: String? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null

    @Column(name = "firstName", nullable = false)
    @Convert(converter = SimpleStringConvertor::class)
    val firstNameProperty = SimpleStringProperty(firstName)

    @Column(name = "lastName", nullable = false)
    @Convert(converter = SimpleStringConvertor::class)
    val lastNameProperty = SimpleStringProperty(lastName)

    @Column(name = "userGroup", nullable = false)
    @Convert(converter = SimpleStringConvertor::class)
    val userGroupProperty = SimpleStringProperty(userGroup.name)

    @Column(name = "deleted", nullable = false)
    @Convert(converter = SimpleBooleanConvertor::class)
    val deletedProperty = SimpleBooleanProperty(deleted)

    @Column(name = "username", nullable = true)
    @Convert(converter = SimpleStringConvertor::class)
    val usernameProperty = SimpleStringProperty(username)

    @Column(name = "password", nullable = true)
    @Convert(converter = SimpleStringConvertor::class)
    val passwordProperty = SimpleStringProperty(password)

    @Transient
    val jobTitleProperty = SimpleObjectProperty(jobTitle)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="job_title_id")
    var jobTitle: JobTitle? = null
        set(value) {
            field = value
            jobTitleProperty.set(value)
        }

    override fun toString(): String {
        return "${firstNameProperty.get()} ${lastNameProperty.get()}"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is User)
            return false
        return other.id == id
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + firstNameProperty.hashCode()
        result = 31 * result + lastNameProperty.hashCode()
        return result
    }
}

class UserModel : ItemViewModel<User>() {
    var firstName = bind(User::firstNameProperty)
    var lastName = bind(User::lastNameProperty)
    var jobTitle = bind(User::jobTitle)
    var userGroup = bind(User::userGroupProperty)
    var username = bind(User::usernameProperty)
    var password = bind(User::passwordProperty)
}