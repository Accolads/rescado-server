package org.rescado.server.service

import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Group
import org.rescado.server.persistence.entity.Membership
import org.rescado.server.persistence.repository.MembershipRepository
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
@Transactional
class GroupService(
    private val accountService: AccountService,
    private val membershipRepository: MembershipRepository,
) {

    fun getConfirmedGroupByAccount(account: Account): Group? = membershipRepository.findAllByAccount(account).firstOrNull { it.status == Membership.Status.CONFIRMED }?.group

    fun isValidInvite(inviterUuid: String, groupId: Long): Group? {
        val account = accountService.getByUuid(inviterUuid) ?: return null
        val group = getConfirmedGroupByAccount(account) ?: return null
        return if (group.id == groupId) group else null
    }

    fun create(account: Account): Membership {
        val membership = Membership(
            account = account,
            group = Group(
                memberships = mutableSetOf(),
                created = ZonedDateTime.now(),
            ),
            status = Membership.Status.CONFIRMED,
        )
        return membershipRepository.save(membership)
    }

    fun join(account: Account, group: Group): Membership {
        val confirmedMembership = account.memberships.firstOrNull { it.status == Membership.Status.CONFIRMED }

        if (confirmedMembership != null) {
            confirmedMembership.group = group
            return membershipRepository.save(confirmedMembership)
        }

        val membership = Membership(
            account = account,
            group = group,
            status = Membership.Status.CONFIRMED,
        )
        return membershipRepository.save(membership)
    }

    fun deleteMembership(membership: Membership) = membershipRepository.delete(membership)

    // TODO implement
// @Scheduled(cron = daily?)
// fun cleanup(){
//     remove groups that have no members and were created >1 month ago
// }
}
