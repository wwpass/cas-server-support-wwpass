package com.wwpass.cas.support.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * WwpassUser.java
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
public class WwpassUser {
    
    private String puid;
    private String name;
    
    public WwpassUser(@JsonProperty("puid") String puid, 
                      @JsonProperty("name") String name) {
        this.puid = puid;
        this.name = name;
    }

    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return "PUID: " + puid + "\n" +
                "Name: " + name;
    }
}
