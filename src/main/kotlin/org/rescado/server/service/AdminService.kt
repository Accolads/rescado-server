package org.rescado.server.service

import org.rescado.server.constant.SecurityConstants
import org.rescado.server.persistence.entity.Admin
import org.rescado.server.persistence.repository.AdminRepository
import org.rescado.server.util.checkPassword
import org.rescado.server.util.hashPassword
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class AdminService(
    private val adminRepository: AdminRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @EventListener
    fun onApplicationEvent(event: ContextRefreshedEvent?) {
        if (adminRepository.count() == 0L) {
            logger.warn("No admins found in the database. Creating admin with default credentials...")
            create(SecurityConstants.DEFAULT_ADMIN_USERNAME, SecurityConstants.DEFAULT_ADMIN_PASSWORD)
        }
    }

    fun getByUsername(username: String) = adminRepository.findByUsername(username)

    fun getByUsernameAndPassword(username: String, password: String): Admin? {
        val admin = this.getByUsername(username) ?: return null
        return if (checkPassword(password, admin.password)) admin else null
    }

    fun create(username: String, password: String): Admin {
        val admin = Admin(
            username = username,
            password = hashPassword(password),
        )
        return adminRepository.save(admin)
    }

    fun update(admin: Admin, username: String?, password: String?): Admin {
        username?.let { admin.username = it }
        password?.let { admin.password = it }

        return adminRepository.save(admin)
    }

    fun delete(admin: Admin) = adminRepository.delete(admin)
}
