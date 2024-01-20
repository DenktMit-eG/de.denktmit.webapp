package de.denktmit.webapp.persistence.users

import de.denktmit.webapp.persistence.HasIdOfType
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.NaturalId


@Entity
@Table(name = "authorities")
data class Authority(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "authority_id")
    override val id: Long = 0,

    @NaturalId
    @Column(length = 50)
    @NotBlank
    val authority: String = "",

    ) : HasIdOfType<Long> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Authority

        return authority == other.authority
    }

    override fun hashCode(): Int {
        return authority.hashCode()
    }

    override fun toString(): String {
        return "Authority(authority='$authority')"
    }

}