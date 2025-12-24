package com.pgustavo.chirp.api.config

import com.pgustavo.chirp.domain.exception.RateLimitException
import com.pgustavo.chirp.infra.rate_limiting.IpRateLimiter
import com.pgustavo.chirp.infra.rate_limiting.IpResolver
import org.springframework.beans.factory.annotation.Value
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Duration
import kotlin.jvm.java

@Component
class IpRateLimitInterceptor(
    private val ipRateLimiter: IpRateLimiter,
    private val ipResolver: IpResolver,
    @param:Value("\${chirp.rate-limit.ip.apply-limit}")
    private val applyLimit: Boolean
): HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if(handler is HandlerMethod && applyLimit) {
            val annotation = handler.getMethodAnnotation(IpRateLimit::class.java)
            if(annotation != null) {
                val clientIp = ipResolver.getClientIp(request)

                return try {
                    ipRateLimiter.withIpRateLimit(
                        ipAddress = clientIp,
                        resetsIn = Duration.of(
                            annotation.duration,
                            annotation.unit.toChronoUnit()
                        ),
                        maxRequestsPerIp = annotation.requests,
                        action = { true }
                    )
                } catch(e: RateLimitException) {
                    response.sendError(429)
                    false
                }
            }
        }

        return true
    }
}