package org.rescado.server.persistence.repository

import org.rescado.server.persistence.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Long> {

    fun findByUuid(uuid: String): Account?

    fun findByEmail(email: String): Account?
}
