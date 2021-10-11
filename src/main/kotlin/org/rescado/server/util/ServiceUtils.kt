package org.rescado.server.util

import org.springframework.security.crypto.bcrypt.BCrypt

fun hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt(12))

fun checkPassword(password: String, encryptedPassword: String) = BCrypt.checkpw(password, encryptedPassword)
