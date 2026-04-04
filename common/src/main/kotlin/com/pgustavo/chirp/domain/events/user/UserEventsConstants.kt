package com.pgustavo.chirp.domain.events.user

object UserEventsConstants {

    const val USER_EXCHANGE = "user.events"

    const val USER_CREATED_KEY = "user.created"
    const val USER_VERIFIED = "user.verified"
    const val USER_REQUEST_RESEND_VERIFICATION = "user.request_resend_varification"
    const val USER_REQUEST_RESET_PASSWORD = "user.request_reset_password"
}