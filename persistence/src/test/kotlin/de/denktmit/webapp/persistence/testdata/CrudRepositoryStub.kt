package de.denktmit.webapp.persistence.testdata

import de.denktmit.webapp.persistence.HasIdOfType
import org.springframework.data.repository.CrudRepository
import java.util.*

open class CrudRepositoryStub<T: HasIdOfType<ID>, ID : Any>(
    private var data: MutableList<T>
): CrudRepository<T, ID> {

    override fun <S : T> save(entity: S): S {
        deleteById(entity.id)
        data.add(entity)
        return entity
    }

    override fun <S : T> saveAll(entities: Iterable<S>): Iterable<S> {
        deleteAllById(entities.map { it.id })
        data.addAll(entities)
        return entities
    }

    override fun findById(id: ID): Optional<T> {
        return Optional.ofNullable(data.find { it.id == id })
    }

    override fun existsById(id: ID): Boolean {
        return data.find { e -> e.id == id } != null
    }

    override fun findAll(): Iterable<T> {
        return data.toList()
    }

    override fun findAllById(ids: Iterable<ID>): Iterable<T> {
        return data.filter { ids.contains(it.id) }
    }

    override fun count(): Long {
        return data.size.toLong()
    }

    override fun deleteById(id: ID) {
        deleteAllById(listOf(id))
    }

    override fun delete(entity: T) {
        deleteById(entity.id)
    }

    override fun deleteAllById(ids: Iterable<ID>) {
        data.removeIf { ids.contains(it.id) }
    }

    override fun deleteAll(entities: Iterable<T>) {
        deleteAllById(entities.map { it.id })
    }

    override fun deleteAll() {
        data.clear()
    }

}