package com.paidaxing.security.filter;

import com.paidaxing.security.dto.SmsCodeAuthenticationToken;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "phone";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "code";
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/user/sms/login", "POST");
    private String phoneParameter = "phone";
    private String codeParameter = "code";
    private boolean postOnly = true;

    public SmsAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    public SmsAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String phone = this.obtainPhone(request);
            phone = phone != null ? phone : "";
            phone = phone.trim();
            String code = this.obtainCode(request);
            code = code != null ? code : "";
            SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(phone, code);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    @Nullable
    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter(this.codeParameter);
    }

    @Nullable
    protected String obtainPhone(HttpServletRequest request) {
        return request.getParameter(this.phoneParameter);
    }

    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setPhoneParameter(String phoneParameter) {
        Assert.hasText(phoneParameter, "phone parameter must not be empty or null");
        this.phoneParameter = phoneParameter;
    }

    public void setCodeParameter(String codeParameter) {
        Assert.hasText(codeParameter, "code parameter must not be empty or null");
        this.codeParameter = codeParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getPhoneParameter() {
        return this.phoneParameter;
    }

    public final String getCodeParameter() {
        return this.codeParameter;
    }
}
