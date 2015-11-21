# Using Cookies with JAX-RS

## Table of Contents

* [Relevant Example Code](#relevant-example-code)
* [Demonstrate Using Browser](#demonstrate-using-browser)

## Relevant Example Code

The relevant sample code is in `morbrian.sandbox.j2ee.cookie.rest.CookieExampleRestApi`. There are two methods used to demonstrate
setting a cookie on the client.

1. `public Response pickGadgetById(@PathParam("gadgetId") String gadgetId)`

    This method sets the **Cookie** on the response with the `NewCookie` object. Of particular interest are
    the `path` and `domain` paramters to the constructor. Setting these to "/" and "" respectively will
    let all endpoints for the context see the **Cookie** contents, which is often desireable for
    REST API endpoints comprising the same web application.
    
    _The empty string for `domain` struck me as odd at first since it was not clear to me what the JAX-RS implementation
    would interpret that to mean. This turns out to be a reasonable default that does not jeopardize security concerns.
    Modern browsers will not share the `Cookie` contents with any server that has a different FQDN. For example,
    a `Cookie` set by `http://h2.winterfell.westeros.com:8080/jaxrs-cookies/rest/gogo/pick/A` will not
    be available in requests sent to http://h1.winterfell.westeros.com:8080/jaxrs-cookies/rest/gogo/gadget/go`.
    So the full hostname is trusted with the Cookie contents, but not other host names or parent domains._
    
    The last parameter **secure** means the `Cookie` will only be sent when using a secure SSL connection.
    
2. `public String goGoGadgetGo(@CookieParam(value = "Gadget") String gadget)`

    This very simple method uses the JAX-RS `@CookieParam` annotation to get the cookie value from the request,
    and simply displays it back to the user in a sentence.
    
## Demonstrate Using Browser

The use case for this behavior can be observed using any web browser. We have observed the use of Cookies in action
on classified networks previously, so there is reason to believe the target SIPR workstations used by our
operators are likely compatible with this strategy.

1. Open a browser to the following URL. (The "A" can be replaced with any letter _A..G_)

        http://localhost:8080/jaxrs-cookies/rest/gogo/pick/A

This will result in a message indicating a particular gadget value was set in the Cookie, such as `copter`.

        Picking Gadget: copter

2. Using the same browser, open a new window with the URL:

        http://localhost:8080/jaxrs-cookies/rest/gogo/gadget/go

This will result in a message showing the last picked gadget.

        Go, Go gadget copter
        
Notice that there was no information about the gadget specified in the request, and there is no data stored on the server.
This information is stored on the client in the browser. This information persists between browser sessions, but can
be set to be expired if desired when creating the cookie. The user may also delete their cookie database locally
from the browser which would reset the state.
