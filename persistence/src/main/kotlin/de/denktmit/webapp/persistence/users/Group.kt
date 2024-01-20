package de.denktmit.webapp.persistence.users

import de.denktmit.webapp.persistence.HasIdOfType
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.NaturalId


@SqlResultSetMapping(
    name = "groupauthorities",
    entities = [
        EntityResult(
            entityClass = Group::class,
            fields = [
                FieldResult(name = "id", column = "group_id"),
                FieldResult(name = "groupName", column = "group_name")
            ]
        ),
        EntityResult(
            entityClass = Authority::class,
            fields = [
                FieldResult(name = "id", column = "authority_id"),
                FieldResult(name = "authority", column = "authority")
            ]
        ),

    ]
)
@Entity
@Table(name = "groups")
data class Group(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "group_id")
    override val id: Long = 0,

    @NaturalId
    @Column(length = 50)
    @NotBlank
    val groupName: String = "",
) : HasIdOfType<Long> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Group

        return groupName == other.groupName
    }

    override fun hashCode(): Int {
        return groupName.hashCode()
    }

    override fun toString(): String {
        return "Group(groupName='$groupName')"
    }

}

const val GROUP_NAME_ADMINS = "admins"
const val GROUP_NAME_USERS = "users"