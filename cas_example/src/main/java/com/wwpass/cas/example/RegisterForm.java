package com.wwpass.cas.example;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * RegisterForm.java
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
public class RegisterForm {

    @NotEmpty(message="Username is required")
    private String username;
    @NotEmpty(message="Password is required")
    private String password;
    @NotEmpty(message="Hidden field \"ticket\" is required")
    private String ticket;
    
    private boolean adminRole;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public boolean isAdminRole() {
        return adminRole;
    }

    public void setAdminRole(boolean adminRole) {
        this.adminRole = adminRole;
    }
}
