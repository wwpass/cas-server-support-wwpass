package com.wwpass.cas.support.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

/**
 * JdbcWwpassDao.java
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
public class JdbcWwpassDao implements WwpassDAO {
    
    private static final String GET_USERNAME_BY_PUID =  "select username " +
                                                        "from cas_users cas " +
                                                        "   inner join wwpass_users wwpass on cas.userid = wwpass.uid " +
                                                        "where wwpass.puid=?;";
    
    private String usernameByPuidQuery;
    
    @NotNull
    private JdbcTemplate jdbcTemplate;

    @NotNull
    private DataSource dataSource;
    
    @Override
    public WwpassUser getUserByPuid(String puid) {
        if (usernameByPuidQuery == null) {
            usernameByPuidQuery = GET_USERNAME_BY_PUID;
        }
        String username = null;
        try {
            username = getJdbcTemplate().queryForObject(usernameByPuidQuery, String.class, puid);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        if (username == null) {
            return null;
        }
        return new WwpassUser(puid, username);
    }

    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    private JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplate(dataSource);
        }
        return jdbcTemplate;
    }

    public void setUsernameByPuidQuery(String usernameByPuidQuery) {
        this.usernameByPuidQuery = usernameByPuidQuery;
    }

}
