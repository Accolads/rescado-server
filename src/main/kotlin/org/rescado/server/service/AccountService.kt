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
            status = Account.Status.ANONYMOUS,
            shelter = null,
            avatar = null,
            favorites = mutableSetOf(),
            likes = mutableSetOf(),
            swipes = mutableSetOf(),
        )
        return accountRepository.save(account)
    }

    fun update(account: Account, name: String?, email: String?, password: String?): Account {
        name?.let { account.name = it }
        email?.let { account.email = it }
        password?.let { account.password = it }

        if (account.status == Account.Status.ANONYMOUS)
            account.status = Account.Status.ENROLLED

        return accountRepository.save(account)
    }
}
