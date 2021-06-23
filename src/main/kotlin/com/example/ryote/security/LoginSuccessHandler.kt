package com.example.ryote.security

import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoginSuccessHandler : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        auth: Authentication
    ) {
        if (response.isCommitted()) {
            return
        }
        response.setStatus(HttpStatus.OK.value())
        clearAuthenticationAttributes(request)
    }

    fun clearAuthenticationAttributes(request: HttpServletRequest) {
        val session = request.getSession(false)
        if (session == null) return
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)
    }
}
