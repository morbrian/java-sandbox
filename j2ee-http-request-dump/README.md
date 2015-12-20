To view the output, deploy the WAR artifact and view this URL:

        http://localhost:8080/http-request-dump/inspect

Similar information should also be available in the server logs.

## JAAS

Explore setting the Principal object and enforcing policies with JAAS using a container,
such as TomEE. [TomEE JASS Reference](https://tomee.apache.org/tomee-jaas.html)

### PropertiesLoginModule TomEE JAAS Configuration

Add to your CATALINA_OPTS the java.security.auth.login.config system property:

``
-Djava.security.auth.login.config=$CATALINA_BASE/conf/login.config
``

Configure your realm in server.xml file


        <?xml version='1.0' encoding='utf-8'?>
        <Server port="8005" shutdown="SHUTDOWN">
          <Listener className="org.apache.tomee.loader.OpenEJBListener" />
          <Listener className="org.apache.catalina.security.SecurityListener" />
        
          <Service name="Catalina">
            <Connector port="8080" protocol="HTTP/1.1" 
                       connectionTimeout="20000" 
                       redirectPort="8443" />
            <Connector port="8009" protocol="AJP/1.3" redirectPort="8443" />
            <Engine name="Catalina" defaultHost="localhost">
              <!-- here is the magic -->
              <Realm className="org.apache.catalina.realm.JAASRealm" appName="PropertiesLoginModule"
                  userClassNames="org.apache.openejb.core.security.AbstractSecurityService$User"
                  roleClassNames="org.apache.openejb.core.security.AbstractSecurityService$Group">
              </Realm>
        
              <Host name="localhost"  appBase="webapps"
                    unpackWARs="true" autoDeploy="true" />
            </Engine>
          </Service>
        </Server>


Configure your login.config file

        PropertiesLogin {
            org.apache.openejb.core.security.jaas.PropertiesLoginModule required
            Debug=false
            UsersFile="users.properties"
            GroupsFile="groups.properties";
        };


Configure your login module specifically (users.properties for snippets of this page for instance)

Example group.properties:

        admin=dev_moore
        reader=cristina,dev_sanders,dev_clinton
        
Example users.properties:

        cristina=changeme
        dev_clinton=changeme
        dev_sanders=changeme
        dev_moore=changeme
