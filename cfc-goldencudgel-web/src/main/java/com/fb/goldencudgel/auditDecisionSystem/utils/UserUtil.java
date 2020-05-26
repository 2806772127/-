package com.fb.goldencudgel.auditDecisionSystem.utils;

import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.service.IAppSessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {
    /** The session context. */
    private static IAppSessionContext sessionContext;

    /** The Constant SESSION_USER. */
    public static final String SESSION_USER = "_session_user_";

    @Autowired
    public void setSessionContext(IAppSessionContext sessionContext) {
        UserUtil.sessionContext = sessionContext;
    }

    public static User getCurrUser() {
        if (sessionContext != null) {
            User user = sessionContext.getUser();
            if (user != null) {
                return user;
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        User user =
                (User) (authentication.getPrincipal() == null ? null : authentication.getPrincipal());
        return user;
    }

    public boolean hasFunction(String functionCode) {
        return getCurrUser().hasFunctionCode(functionCode);
    }
}
