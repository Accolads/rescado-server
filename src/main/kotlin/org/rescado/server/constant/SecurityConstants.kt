package org.rescado.server.constant

object SecurityConstants {
    const val AUTHORIZATION_HEADER = "Authorization"

    const val BASIC_PREFIX = "Basic "

    const val TOKEN_PREFIX = "Bearer "
    const val TOKEN_TYPE = "JWT"
    const val TOKEN_TTL = 24L // 24 hours
    const val REFRESH_TTL = 2160L // 2160 hours, aka 90 days

    const val JWT_SECRET = "n2r5u8x/A%D*G-KaPdSgVkYp3s6v9y?/B&E(H+MbQeThWmZq4t7w!z%C*F-J@NcRf" // will change once we go prod, sorry hackers

    const val AUTH_ROUTE = "/auth"
    const val PUBLIC_ROUTE = "/public"

    val PASSWORD_BLACKLIST = listOf(
        "password", "pass1234", "12345678", "01234567", "baseball", "trustno1", "superman", "testtest", "computer",
        "michelle", "123456789", "0123456789", "012345678", "1234567890", "corvette", "00000000", "test1234",
    )

    val IMAGE_CONTENTTYPE_WHITELIST = listOf(
        "image/jpeg", "image/png",
    )
}
