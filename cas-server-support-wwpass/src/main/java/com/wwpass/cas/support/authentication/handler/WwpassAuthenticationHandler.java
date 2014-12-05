package com.wwpass.cas.support.authentication.handler;

import com.wwpass.cas.support.authentication.credential.WwpassWithUsernamePasswordCredentials;
import com.wwpass.cas.support.dao.WwpassDAO;
import com.wwpass.cas.support.dao.WwpassUser;
import com.wwpass.connection.WWPassConnection;
import com.wwpass.connection.exceptions.WWPassProtocolException;
import org.jasig.cas.authentication.*;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * WwpassAuthenticationHandler.java
 *
 * @author Stanislav Panyushkin <s.panyushkin@wwpass.com>
 *         <p/>
 *         Licensed under the Apache License, Version 2.0 (the "License");
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <p/>
 *         http://www.apache.org/licenses/LICENSE-2.0
 *         <p/>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 * @copyright (c) WWPass Corporation, 2014
 */
public class WwpassAuthenticationHandler extends AbstractAuthenticationHandler {
    
    private static final Logger LOG = LoggerFactory.getLogger(WwpassAuthenticationHandler.class);
    
    private WWPassConnection wwpassConnection;
    private WwpassDAO wwpassDAO;

    
    @Override
    public HandlerResult authenticate(Credential credential) throws GeneralSecurityException, PreventedException {
        final WwpassWithUsernamePasswordCredentials c = (WwpassWithUsernamePasswordCredentials) credential;
        //String ticket = c.getId();
        String ticket = c.getTicket();
        
        String puid = null;
        try {
            ticket = wwpassConnection.putTicket(ticket);
            puid = wwpassConnection.getPUID(ticket);
        } catch (WWPassProtocolException e) {
            LOG.error("Error occurred while WWPass authentication process: ", e);
            throw new FailedLoginException("Error in WWPassConnection library: " + e.getMessage());
        } catch (IOException e) {
            LOG.error("Error occurred while WWPass authentication process: ", e);
            throw new FailedLoginException("Error occurred while WWPass authentication process: " + e.getMessage());
        }
        
        LOG.debug("PUID: " + puid);
        
        if (puid == null) {
            throw new IllegalArgumentException("PUID cannot be null.");
        }

        WwpassUser user = null;
        try {
            user = wwpassDAO.getUserByPuid(puid);
        } catch (Exception e) {
            LOG.error("Exception querying database: " + e);
        }

        if (user == null) {
            LOG.info("There is no such user exception.");
            throw new AccountNotFoundException("There is no such user!");
        }
        return new HandlerResult(this, new BasicCredentialMetaData(c), new SimplePrincipal(user.getName()));

    }

    @Override
    public boolean supports(Credential credential) {
        //return (credential instanceof WwpassCredentials);
        return (credential instanceof WwpassWithUsernamePasswordCredentials) && 
                (((WwpassWithUsernamePasswordCredentials) credential).getTicket() != null);
    }

    @Override
    public String getName() {
        return "WwpassAuthenticationHandler";
    }

    public void setWwpassConnection(WWPassConnection wwpassConnection) {
        this.wwpassConnection = wwpassConnection;
    }

    public void setWwpassDAO(WwpassDAO wwpassDAO) {
        this.wwpassDAO = wwpassDAO;
    }
}
