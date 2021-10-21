package org.rescado.server.service

import org.rescado.server.persistence.entity.Admin
import org.rescado.server.persistence.repository.AdminRepository
import org.rescado.server.util.checkPassword
import org.rescado.server.util.hashPassword
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class AdminService(private val adminRepository: AdminRepository) {

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
