package it.txt.ldap;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.GeneralStringUtils;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import tpms.TpmsException;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 23-feb-2006
 * Time: 13.29.07
 * To change this template use File | Settings | File Templates.
 */
public class LdapAuthentication extends AfsCommonStaticClass {

    protected static final String LDAP_FULL_SERVER_NAME = tpmsConfiguration.getLdapFullServerName();

    protected static final String LDAP_SERVER_PORT = tpmsConfiguration.getLdapServerPort();

    protected static final String LDAP_DRIVER = tpmsConfiguration.getLdapDriver();

    protected static final String LDAP_SEARCH_BASE = tpmsConfiguration.getLdapSearchBase();

    /**
     * authenticate a user using ldap credentials
     *
     * @param ldapLogin    login inside of ldap
     * @param ldapPassword password inside ldap
     * @return true if the user was succesfully authenticated, false otherwise
     * @throws TpmsException in case of errors...
     */
    public static boolean authenticateUser(String ldapLogin, String ldapPassword) throws TpmsException {
        boolean authenticated = false;

        if (GeneralStringUtils.isEmptyString( ldapPassword )) {
        	return authenticated;
        }

        String FILTER = "uid=" + ldapLogin;

        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_DRIVER);
        env.put(Context.PROVIDER_URL, "ldap://" + LDAP_FULL_SERVER_NAME + ":" + LDAP_SERVER_PORT);
        DirContext ctx;
        try {

            ctx = new InitialDirContext(env);
        } catch (NamingException e) {
            errorLog("LdapAuthentication :: authenticateUser : a naming exception is encountered during context initialization", e);
            throw new TpmsException("NamingExeption", "LDAP authentication", e);
        }

        SearchControls constraint = new SearchControls();
        constraint.setSearchScope(SearchControls.SUBTREE_SCOPE);

        NamingEnumeration ldapQueryResult;
        try {
            ldapQueryResult = ctx.search(LDAP_SEARCH_BASE, FILTER, constraint);
        } catch (NamingException e) {
            errorLog("LdapAuthentication :: authenticateUser : a naming exception is encountered during context search", e);
            throw new TpmsException("NamingExeption", "LDAP authentication", e);
        }

        String dn = null;
        try {
            if ((ldapQueryResult != null) && (ldapQueryResult.hasMore())) {
                SearchResult sr = (SearchResult) ldapQueryResult.next();
                dn = sr.getName() + "," + LDAP_SEARCH_BASE;
            }
        } catch (NamingException e) {
            errorLog("LdapAuthentication :: authenticateUser : a naming exception is encountered during retrieving search ldapQueryResult", e);
            throw new TpmsException("NamingExeption", "LDAP authentication", e);
        }

        if (dn != null) {
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, dn);
            env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
            DirContext ctx2 = null;
            try {
                ctx2 = new InitialDirContext(env);
                authenticated = true;
            } catch (AuthenticationException e) {
                // in this case the user do not give valid credentials....
                //the following line is not needed... but it's there in order to clarify the execution...
                //(remember that we have only to set authenticated to true, because it's default value is false!)
                authenticated = false;
            } catch (NamingException e) {
                errorLog("LdapAuthentication :: authenticateUser : a naming exception is encountered during authentication context initialization", e);
                throw new TpmsException("NamingExeption", "LDAP authentication", e);
            } finally {
                try {
                    ctx.close();
                } catch (NamingException e) {
                    errorLog("LdapAuthentication :: authenticateUser : NamingException error during ctx.close()", e);
                }
                try {
                    if (ctx2 != null) ctx2.close();
                } catch (NamingException e) {
                    errorLog("LdapAuthentication :: authenticateUser : NamingException error during ctx2.close()", e);
                }
            }

        }
        return authenticated;
    }

}
