package morbrian.j2eesandbox.jpabasics.digest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * Simple client class for generating random data strings.
 */
public class DummyClient {

    private static final String USER_AGENT = "JPABASICS - " + DummyClient.class.getName();

    private Logger log = LoggerFactory.getLogger(DummyClient.class);

    /**
     *
     * @return response content data
     * @throws IOException when errors fetching data
     */
    public byte[] requestData() throws IOException {
        return fetchData();
    }

    // connects to remote web service for endpoint type "web"
    // or reads data from local filesystem for endpoint type "file"
    // otherwise exception thrown.
    private byte[] fetchData() throws IOException {
        return UUID.randomUUID().toString().getBytes();
    }

}
