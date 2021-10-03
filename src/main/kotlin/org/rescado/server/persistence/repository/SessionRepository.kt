package org.rescado.server.persistence.repository

import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

@Repository
interface SessionRepository : JpaRepository<Session, Long> {

    fun findByAccount(account: Account): List<Session>

    fun findByToken(token: String): Session?

    fun findAllByLastLoginBefore(lastLogin: ZonedDateTime): List<Session>

    fun findAllByLastLoginAfter(lastLogin: ZonedDateTime): List<Session>
}
