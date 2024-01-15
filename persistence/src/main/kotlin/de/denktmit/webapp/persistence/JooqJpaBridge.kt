package de.denktmit.webapp.persistence

import jakarta.persistence.EntityManager
import jakarta.persistence.Query
import org.hibernate.Session
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL


fun <T> EntityManager.jooqWrite(fn: (DSLContext) -> T) {
    val session = this.unwrap(Session::class.java)
    session.flush()
    return session.doWork {
            connection -> fn(DSL.using(connection, SQLDialect.POSTGRES))
    }
}


fun <T> EntityManager.jooqRead(fn: (DSLContext) -> T): T {
    val session = this.unwrap(Session::class.java)
    session.flush()
    return session.doReturningWork {
            connection -> fn(DSL.using(connection, SQLDialect.POSTGRES))
    }
}

fun <T> EntityManager.createQueryFrom(jooqQuery: org.jooq.Query, clazz: Class<T>): Query {
    val queryString: String = jooqQuery.sql
    return createNativeQuery(queryString, clazz)
        .setBindParameterValues(jooqQuery)
}

fun EntityManager.createQueryFrom(jooqQuery: org.jooq.Query): Query {
    val queryString: String = jooqQuery.sql
    return createNativeQuery(queryString)
        .setBindParameterValues(jooqQuery)
}

fun Query.setBindParameterValues(jooqQuery: org.jooq.Query): Query {
    val values = jooqQuery.bindValues
    for (i in values.indices) {
        setParameter(i + 1, values[i])
    }
    return this
}