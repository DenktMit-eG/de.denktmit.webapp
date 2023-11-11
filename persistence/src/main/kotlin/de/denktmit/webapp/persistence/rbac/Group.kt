package de.denktmit.webapp.persistence.rbac

import de.denktmit.webapp.persistence.HasIdOfType
import de.denktmit.webapp.persistence.users.User
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

    ) : HasIdOfType<Long>