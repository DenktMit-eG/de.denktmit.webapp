package de.denktmit.webapp.persistence.rbac

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

): HasIdOfType<Long>