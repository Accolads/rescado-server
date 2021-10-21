package org.rescado.server.constant

object SecurityConstants {

    const val AUTH_ROUTE = "/auth"
    const val PUBLIC_ROUTE = "/public"

    const val DEFAULT_ADMIN_USERNAME = "rescado-superadmin"
    const val DEFAULT_ADMIN_PASSWORD = "rescado-password"

    const val SALT = "\$2a\$12\$J5FICwPCWcyOH/5oUKq9qO" // will change once we go prod, sorry hackers
    const val SECRET = "n2r5u8x/A%D*G-KaPdSgVkYp3s6v9y?/B&E(H+MbQeThWmZq4t7w!z%C*F-J@NcRf" // sorry hackers

    const val BASIC_PREFIX = "Basic "
    const val TOKEN_PREFIX = "Bearer "
    const val TOKEN_TYPE = "JWT"
    const val TOKEN_TTL = 24L // 24 hours
    const val REFRESH_TTL = 2160L // 2160 hours, aka 90 days

    val PASSWORD_BLACKLIST = listOf(
        "password", "pass1234", "12345678", "01234567", "baseball", "trustno1", "superman", "testtest", "computer",
        "michelle", "123456789", "0123456789", "012345678", "1234567890", "corvette", "00000000", "test1234",
    )

    const val IMAGE_MAX_REFERENCES = 10

    val IMAGE_CONTENTTYPE_WHITELIST = listOf(
        "image/jpeg", "image/png",
    )
}
