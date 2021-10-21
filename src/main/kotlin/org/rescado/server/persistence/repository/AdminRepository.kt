package org.rescado.server.persistence.repository

import org.rescado.server.persistence.entity.Admin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdminRepository : JpaRepository<Admin, Long> {

    fun findByUsername(username: String): Admin?
}
