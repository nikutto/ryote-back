package com.example.ryote.security

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoginFailureHandler : AuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        if (response.isCommitted()) {
            return
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value())
        clearAuthenticationAttributes(request)
    }

    fun clearAuthenticationAttributes(request: HttpServletRequest) {
        val session = request.getSession(false)
        if (session == null) return
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)
    }
}
