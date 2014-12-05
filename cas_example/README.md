CAS Example
============
An example app, which demonstrates authentication via CAS Single Sign-On Server, and registration to of new WWPass users.

Configuration
=============
This example app uses the same database as CAS server to load the authorities for a user, once they have been authenticated by CAS.

All configuration for example app are separated in file **cas_example/src/main/resources/config.properties**. Rename file 
**cas_example/src/main/resources/config.properties.example** to **cas_example/src/main/resources/config.properties** and open it in a text editor you prefer:

* Edit URLs of example app and CAS server:

``` properties
# CAS Server application URL
cas.url = https://cas.example.com:8443/cas

# This example application URL
app.url = https://example.com:8443/cas-example
```


* Next, change paths to your WWPass certificate/key files and Service Provider's name on which the WWPass certificate was issued:

``` properties
# WWPass configuration
wwpass.sp.name = example.com
wwpass.sp.cert.path = /etc/ssl/certs/example.com.crt
wwpass.sp.key.path = /etc/ssl/certs/example.com.key
```

* Modify `JDBC configuration` section to configure connection to CAS database:

``` properties
# JDBC configuration
db.driver.class.name = com.mysql.jdbc.Driver
db.url = jdbc:mysql://127.0.0.1/wwpass_cas
db.username = cas
db.password = changeit
```
Don't forget to replace dependency of JDBC driver for database type in file **cas_example/pom.xml**, if you using other than MySQL:
``` xml
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.6</version>
    </dependency>
```

Build
==============
When you finish configuration, build app with Maven:
``` bat
cd cas_example
mvn clean package
```

CAS Server configuration
========================
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
