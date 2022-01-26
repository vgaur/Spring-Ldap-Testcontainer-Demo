# Spring-Ldap-Testcontainer-Demo

Displays the ldap based authentication with spring security ldap and inegration test with Testcontainer.

Ldap Container used for this demo
  <br> rroemhild/test-openldap ( https://github.com/rroemhild/docker-test-openldap )
  
There are two endpoint exposed.
<ul>
  <li> /public </li>
    <li>/private/info </li>
  </ul>

The protected url requires authentication. When user tries to login the user is redirected to a login form where user has to enter credentials.
On submission, the application connect to a running ldap server (default : localhost:10683 ) and login using pre set up admin creadentials.

Post login the user is seached in LDAP tree with the given uid and pre defined search filter. Once use if found the password is matched with the one provided by user.

<b> Testing </b> <p>
The test runs an ldap test container and expose the port. Test container maps the host port randomely so we need to get the mapped port and comppute the ldap url.

The computed URL is set in environment variable so the actual code will use the test container url.
