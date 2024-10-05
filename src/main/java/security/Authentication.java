package security;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import security.config.HashAlgorithm;

import java.io.IOException;

/**
 The Authentication class provides methods for user authentication and authorization.
 It uses Jakarta Security API to authenticate and authorize the user based on user roles.
 */
@ApplicationScoped
@FormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.xhtml",
                errorPage = "/loginFail.xhtml"))
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:jboss/datasources/bikes5",
        callerQuery = "SELECT phone FROM staffs WHERE email = ?",
        groupsQuery = """
                        SELECT
                        CASE
                        WHEN manager_id IS NULL THEN 'ADMIN'
                        WHEN manager_id = 1 THEN 'USER1'
                        ELSE 'USER2'
                        END AS group_name
                        FROM staffs
                        WHERE email = ?;
                        """,
        hashAlgorithm = HashAlgorithm.class)
@Named
public class Authentication extends HttpServlet {

    Authentication() {}

    /**
     * Jakarta Security API's SecurityContext for authentication and authorization of the user.
     */
    @Inject
    SecurityContext securityContext;
    /**
     * Jakarta Faces API's ExternalContext for redirecting the user to the dashboard or login page after authentication.
     */
    @Inject
    ExternalContext externalContext;

    /**
     * Invalidates the user's session and redirects the user to the current page after the session is invalidated.
     * @throws IOException if the redirection fails.
     */
    public void invalidate() throws IOException {
        externalContext.invalidateSession();
        externalContext.redirect(((HttpServletRequest) externalContext.getRequest()).getRequestURI());
    }

    /**
     * Checks if the user is authenticated.
     * @return true if the user is authenticated, false otherwise.
     */
    public boolean isLoggedIn(){
        return securityContext.getCallerPrincipal() != null;
    }
}
