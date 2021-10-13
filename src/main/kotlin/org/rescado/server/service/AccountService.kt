package org.rescado.server.service

import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.repository.AccountRepository
import org.rescado.server.util.checkPassword
import org.rescado.server.util.hashPassword
import org.springframework.stereotype.Service
import java.util.UUID
import javax.transaction.Transactional

@Service
@Transactional
class AccountService(private val accountRepository: AccountRepository) {

    fun getByUuid(uuid: String) = accountRepository.findByUuid(uuid)

    fun getByEmail(email: String) = accountRepository.findByEmail(email)

    fun getByEmailAndPassword(email: String, password: String): Account? {
        val account = this.getByEmail(email) ?: return null
        return if (!account.password.isNullOrBlank() && checkPassword(password, account.password!!)) account else null
    }

    fun create(email: String? = null, password: String? = null, name: String? = null): Account {
        val account = Account(
            uuid = UUID.randomUUID().toString(),
            email = email,
            password = password?.let { hashPassword(it) },
            name = name,
            status = Account.Status.ENABLED,
            shelter = null,
            avatar = null,
            favorites = mutableSetOf(),
            likes = mutableSetOf(),
            swipes = mutableSetOf(),
        )
        return accountRepository.save(account)
    }

    fun update(account: Account, email: String?, password: String?, name: String?): Account {
        email?.let { account.email = it }
        password?.let { account.password = it }
        name?.let { account.name = it }

        return accountRepository.save(account)
    }
}
