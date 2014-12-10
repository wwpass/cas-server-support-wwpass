CAS Example
============

An example app, which demonstrates authentication via CAS Single Sign-On Server, and registration of a new WWPass users.



Configuration
---------------

This example app uses the same database as CAS server to access the authorities (roles) of a user, once the user has been authenticated by CAS.

All configuration data for the example app are kept in **cas_example/src/main/resources/config.properties** file. Rename the file 
**cas_example/src/main/resources/config.properties.example** to **cas_example/src/main/resources/config.properties** and open it in a text editor you prefer:

Edit URLs of example app and CAS server:

``` properties
# CAS Server application URL
cas.url = https://cas.example.com:8443/cas

# This example application URL
app.url = https://example.com:8443/cas-example
```


Next, change paths to your WWPass certificate/key files and Service Provider's name on which the WWPass certificate was issued:

``` properties
# WWPass configuration
wwpass.sp.name = example.com
wwpass.sp.cert.path = /etc/ssl/certs/example.com.crt
wwpass.sp.key.path = /etc/ssl/certs/example.com.key
```



Build
-----

Now build the example app with Maven:

``` bat
cd cas_example
mvn clean package install
```
 
Basically this is all. 


Configuration details 
---------------------


Note that the following part of **cas-example/src/main/resources/config.properties** describes particular MySQL database connection.  
Modify this block according to your database parameters


``` properties
# JDBC configuration
db.driver.class.name = com.mysql.jdbc.Driver
db.url = jdbc:mysql://127.0.0.1/wwpass_cas
db.username = cas
db.password = changeit
```


Don't forget to replace JDBC driver dependency  for database type in file **cas_example/pom.xml**, if you are using Database Management System other than MySQL:
``` xml
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.6</version>
    </dependency>
```



####CAS Server configuration

Initial CAS server configuration allows any client to be serviced.
If the your list of CAS clients is restrictive, edit  

**simple-cas4-overlay-template-master/src/main/webapp/WEB-INF/deployerConfigContext.xml**:


Configure CAS Server to allow authentication requests from example app. Add bean below into `registeredServicesList` bean:

``` xml
<bean class="org.jasig.cas.services.RegexRegisteredService">
    <property name="id" value="1"/>
    <property name="name" value="cas-example"/>
    <property name="description" value="Example app using CAS authentication"/>
    <property name="serviceId" value="https://example.com:8443/cas-example"/>
    <property name="enabled" value="true"/>
    <property name="evaluationOrder" value="0"/>
    <property name="attributeFilter">
    <bean class="org.jasig.cas.services.support.RegisteredServiceRegexAttributeFilter" c:regex="^\w{3}$" />
    </property>
</bean>
```

Then build CAS Overlay:

``` bat
cd _your_cas_overlay_directory_
mvn clean package
```
