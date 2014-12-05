package com.wwpass.cas.support.authentication.credential;

import com.wwpass.connection.WWPassConnection;
import com.wwpass.connection.exceptions.WWPassProtocolException;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.PrincipalResolver;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * WwpassCredentialsToPrincipalResolver.java
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
public class WwpassPrincipalResolver implements PrincipalResolver {

    SimplePrincipal simplePrincipal;
    WWPassConnection wwpassConnection;
    
    
    private static final Logger LOG = LoggerFactory.getLogger(WwpassPrincipalResolver.class);
    
    public Principal resolve(Credential credential) {
        if (credential instanceof WwpassCredentials) {
            final WwpassCredentials wwpassCredentials = (WwpassCredentials) credential;
            
            simplePrincipal = new SimplePrincipal(wwpassCredentials.getId());
        } else {
            final WwpassWithUsernamePasswordCredentials c = 
                    (WwpassWithUsernamePasswordCredentials) credential;
            simplePrincipal = new SimplePrincipal(c.getTicket());
        }
        
        return simplePrincipal;
    }

    private String getPuid(String ticket) {
        String puid = null;
        try {
            ticket = wwpassConnection.putTicket(ticket);
            puid = wwpassConnection.getPUID(ticket);
        } catch (WWPassProtocolException e) {
            LOG.error("Error occurred while WWPass authentication process: ", e);
        } catch (IOException e) {
            LOG.error("Error occurred while WWPass authentication process: ", e);
        }
        return puid;
    }

    public boolean supports(Credential credential) {
        return (credential instanceof WwpassCredentials) 
                || (credential instanceof WwpassWithUsernamePasswordCredentials);
    }
}
