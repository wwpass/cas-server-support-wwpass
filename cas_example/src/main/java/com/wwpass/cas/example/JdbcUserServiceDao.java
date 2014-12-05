package com.wwpass.cas.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * JdbcUserServiceDao.java
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
@Repository
public class JdbcUserServiceDao {

    private static final String GET_USERNAME_BY_PUID =  "select cas.username " +
            "from cas_users cas " +
            "   inner join wwpass_users wwpass on cas.userid = wwpass.uid " +
            "where wwpass.puid=?;";

    private static final String GET_PASSWORD = "select password from cas_users where active=1 and username=?;";
    private static final String CREATE_WWPASS_ASSOCIATION = "insert into wwpass_users (puid,uid) values (?,?);";
    private static final String CREATE_USER = "insert into cas_users (username,password,active) values (?,?,?);";
    private static final String ADD_ROLE = "insert into user_roles (uid,ROLE) values (?,?); ";

    private String usernameByPuidQuery;
    private String createWwpassAssociationQuery;
    private String passwordForUsernameQuery;

    @NotNull
    private JdbcTemplate jdbcTemplate;

    @NotNull
    @Autowired
    private DataSource dataSource;

    @Transactional(readOnly = true)
    public String getUsernameByPuid(String puid) {
        if (usernameByPuidQuery == null) {
            usernameByPuidQuery = GET_USERNAME_BY_PUID;
        }
        String username = getJdbcTemplate().queryForObject(usernameByPuidQuery, String.class, puid);

        return username;
    }

    @Transactional(readOnly = true)
    public String getPasswordForUsername(String username) {
        if (passwordForUsernameQuery == null) {
            passwordForUsernameQuery = GET_PASSWORD;
        }
        String password = getJdbcTemplate().queryForObject(passwordForUsernameQuery, String.class, username);
        return password;
    }
    
    public void addRole(final int uid, String[] roles) {
        for (final String role : roles) {
            getJdbcTemplate().update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(ADD_ROLE);
                    ps.setInt(1, uid);
                    ps.setString(2, role);
                    return ps;
                }
            });
        }
    }

    public void createWwpass(final String puid, final int uid) {
        if (createWwpassAssociationQuery == null) {
            createWwpassAssociationQuery = CREATE_WWPASS_ASSOCIATION;
        }
        getJdbcTemplate().update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(createWwpassAssociationQuery);
                ps.setString(1, puid);
                ps.setInt(2, uid);
                return ps;
            }
        });
    }
    
    public int createUser(final String username, final String password) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(
                        CREATE_USER,
                        new String[] { "userid" });
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setInt(3, 1);
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }
        return jdbcTemplate;
    }

    public void setUsernameByPuidQuery(String usernameByPuidQuery) {
        this.usernameByPuidQuery = usernameByPuidQuery;
    }

    public void setCreateWwpassAssociationQuery(String createWwpassAssociationQuery) {
        this.createWwpassAssociationQuery = createWwpassAssociationQuery;
    }

    public void setPasswordForUsernameQuery(String passwordForUsernameQuery) {
        this.passwordForUsernameQuery = passwordForUsernameQuery;
    }
}
