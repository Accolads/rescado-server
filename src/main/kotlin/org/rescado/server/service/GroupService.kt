package org.rescado.server.service

import liquibase.pro.packaged.it
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Group
import org.rescado.server.persistence.entity.Membership
import org.rescado.server.persistence.repository.GroupRepository
import org.rescado.server.persistence.repository.MembershipRepository
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
@Transactional
class GroupService(
    private val accountService: AccountService,
    private val groupRepository: GroupRepository,
    private val membershipRepository: MembershipRepository,
) {

    fun getConfirmedMembershipByAccount(account: Account): Membership? = membershipRepository.findAllByAccount(account).firstOrNull { it.status == Membership.Status.CONFIRMED }

    fun isValidInvite(inviterUuid: String, groupId: Long): Group? {
        val account = accountService.getByUuid(inviterUuid) ?: return null
        val group = getConfirmedMembershipByAccount(account)?.group ?: return null
        return if (group.id == groupId) group else null
    }

    fun create(account: Account): Membership {
        val membership = Membership(
            account = account,
            group = groupRepository.save(
                Group(
                    memberships = mutableSetOf(),
                    created = ZonedDateTime.now(),
                )
            ),
            status = Membership.Status.CONFIRMED,
        )
        return membershipRepository.save(membership)
    }

    fun invite(account: Account, group: Group): Membership {
        val membership = Membership(
            account = account,
            group = group,
            status = Membership.Status.INVITED,
        )
        return membershipRepository.save(membership)
    }

    fun join(account: Account, group: Group): Membership {
        val confirmedMembership = account.memberships.firstOrNull { it.status == Membership.Status.CONFIRMED }

        // If the account already has an active group
        if (confirmedMembership != null) {
            val confirmedGroup = confirmedMembership.group

            // Change the group of that membership, effectively changing the group the account belongs to
            confirmedMembership.group = group
            val membership = membershipRepository.save(confirmedMembership)

            // If the old group does not contain any confirmed members
            if (!confirmedGroup.memberships.any { it.account != account && it.status == Membership.Status.CONFIRMED }) {
                // Remove the group
                groupRepository.delete(confirmedGroup)
            }

            return membership
        }

        val membership = Membership(
            account = account,
            group = group,
            status = Membership.Status.CONFIRMED,
        )
        return membershipRepository.save(membership)
    }

    fun deleteMembership(membership: Membership) {
        membershipRepository.delete(membership)

        // If after leaving, the group has no confirmed members anymore
        if (membership.group.memberships.any { it.status == Membership.Status.CONFIRMED }) {
            // Remove the group
            groupRepository.delete(membership.group)
        }
    }
}
