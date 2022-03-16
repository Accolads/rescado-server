package org.rescado.server.persistence.repository

import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Membership
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MembershipRepository : JpaRepository<Membership, Long> {

    fun findAllByAccount(account: Account): List<Membership>
}
