package org.rescado.server.constant

object SecurityConstants {

    const val AUTH_ROUTE = "/auth"
    const val PUBLIC_ROUTE = "/public"
    const val DEFAULT_ADMIN_USERNAME = "rescado-superadmin"

    const val DEFAULT_ADMIN_PASSWORD = "rescado-password"

    const val DEFAULT_LIMIT = 25
    const val MAX_LIMIT = 150
    const val CARDS_LIMIT = 50

    const val SALT = "\$2a\$12\$J5FICwPCWcyOH/5oUKq9qO" // will change once we go prod, sorry hackers
    const val SECRET = "n2r5u8x/A%D*G-KaPdSgVkYp3s6v9y?/B&E(H+MbQeThWmZq4t7w!z%C*F-J@NcRf" // sorry hackers

    const val DEVICE_HEADER = "User-Device"
    const val USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36"
    const val TIMEOUT = 5000

    const val BASIC_PREFIX = "Basic "
    const val TOKEN_PREFIX = "Bearer "
    const val TOKEN_TYPE = "JWT"
    const val TOKEN_TTL = 24L // 24 hours
    const val REFRESH_TTL = 2160L // 2160 hours, aka 90 days

    val PASSWORD_BLACKLIST = listOf(
        "password", "pass1234", "12345678", "01234567", "baseball", "trustno1", "superman", "testtest", "computer",
        "michelle", "123456789", "0123456789", "012345678", "1234567890", "corvette", "00000000", "test1234", "rescado",
    )

    const val IMAGE_MAX_REFERENCES = 10

    val IMAGE_CONTENTTYPE_WHITELIST = listOf(
        "image/jpeg", "image/png",
    )
}
