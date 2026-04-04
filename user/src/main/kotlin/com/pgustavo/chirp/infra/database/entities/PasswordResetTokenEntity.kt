package com.pgustavo.chirp.infra.database.entities

import com.pgustavo.chirp.infra.security.TokenGenerator
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(
    name = "password_reset_token",
    schema = "user_service",
    indexes = [
        Index("idx_password_reset_token_token", columnList = "token")
    ]
)
class PasswordResetTokenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column(nullable = false, unique = true)
    var token: String = TokenGenerator.generateSecureToken(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity,
    @Column(name= "expires_at", nullable = false)
    var expiresAt: Instant,
    @Column(name= "used_at")
    var usedAt: Instant? = null,
    @Column(name = "created_at")
    var createdAt: Instant = Instant.now(),

) {
    val isUsed: Boolean
        get() = usedAt != null

    val isExpired: Boolean
        get() = Instant.now() > expiresAt
}