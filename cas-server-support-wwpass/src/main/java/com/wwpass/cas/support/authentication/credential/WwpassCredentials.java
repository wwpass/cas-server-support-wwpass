package com.wwpass.cas.support.authentication.credential;

import org.jasig.cas.authentication.Credential;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * WwpassCredentials.java
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
public class WwpassCredentials implements Credential, Serializable {

    private static final long serialVersionUID = 4214848023236594431L;

    @NotNull
    private String ticket;
    
    public WwpassCredentials() {}
    
    public WwpassCredentials(String ticket) {
        this.ticket = ticket;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    @Override
    public String getId() {
        return ticket;
    }
}
