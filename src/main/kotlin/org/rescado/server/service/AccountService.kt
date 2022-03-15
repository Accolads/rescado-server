package org.rescado.server.service

import org.hibernate.Hibernate
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Image
import org.rescado.server.persistence.entity.Shelter
import org.rescado.server.persistence.repository.AccountRepository
import org.rescado.server.service.exception.IllegalReferenceException
import org.rescado.server.service.exception.LastReferenceException
import org.rescado.server.util.checkPassword
import org.springframework.stereotype.Service
import java.util.Locale
import java.util.UUID
import javax.transaction.Transactional

@Service
@Transactional
class AccountService(
    private val accountRepository: AccountRepository,
    private val imageService: ImageService,
) {

    fun getAll(): List<Account> = accountRepository.findAll()

    fun getAllVolunteers(): List<Account> = accountRepository.findAllByShelterIsNotNull()

    fun getAllVolunteers(shelter: Shelter): List<Account> = accountRepository.findAllByShelter(shelter)

    fun getById(id: Long): Account? = accountRepository.findById(id).orElse(null)

    fun getByIdWithFollowing(id: Long) = getById(id)?.apply {
        Hibernate.initialize(
            following.onEach {
                Hibernate.initialize(it.animals)
            }
        )
    }

    fun getByUuid(uuid: String) = accountRepository.findByUuid(uuid)

    fun getByEmail(email: String) = accountRepository.findByEmail(email)

    fun getByEmailAndPassword(email: String, password: String): Account? {
        val account = this.getByEmail(email) ?: return null
        return if (!account.password.isNullOrBlank() && checkPassword(password, account.password!!)) account else null
    }

    fun create(): Account {
        val account = Account(
            uuid = UUID.randomUUID().toString(),
            name = null,
            appleReference = null,
            googleReference = null,
            facebookReference = null,
            twitterReference = null,
            email = null,
            password = null,
            status = Account.Status.ANONYMOUS,
            shelter = null,
            avatar = null,
            memberships = mutableSetOf(),
            following = mutableSetOf(),
            likes = mutableSetOf(),
            swipes = mutableSetOf(),
        )
        return accountRepository.save(account)
    }

    fun update(account: Account, name: String?, appleReference: String?, googleReference: String?, facebookReference: String?, twitterReference: String?, email: String?, password: String?, avatar: Image?): Account {
        name?.let { account.name = it }
        appleReference?.let { account.appleReference = it }
        googleReference?.let { account.googleReference = it }
        facebookReference?.let { account.facebookReference = it }
        twitterReference?.let { account.twitterReference = it }
        email?.let { account.email = it }
        password?.let { account.password = it }
        avatar?.let {
            account.avatar?.let { oldAvatar -> imageService.delete(oldAvatar) }
            account.avatar = it
        }

        if (account.status == Account.Status.ANONYMOUS)
            account.status = Account.Status.ENROLLED

        return accountRepository.save(account)
    }

    fun setVolunteer(account: Account, shelter: Shelter?): Account {
        account.shelter = shelter
        account.status = if (shelter == null) Account.Status.ENROLLED else Account.Status.VOLUNTEER
        return accountRepository.save(account)
    }

    fun removeReference(account: Account, referenceName: String): Account {
        when (referenceName.uppercase(Locale.getDefault())) {
            "APPLE" -> account.appleReference = null
            "GOOGLE" -> account.googleReference = null
            "FACEBOOK" -> account.facebookReference = null
            "TWITTER" -> account.twitterReference = null
            "EMAIL" -> {
                account.email = null
                account.password = null
            }
            else -> throw IllegalReferenceException(referenceName)
        }

        if (!account.hasAtLeastOneReference())
            throw LastReferenceException(referenceName)

        return accountRepository.save(account)
    }
}
