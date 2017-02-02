package com.marklogic.mgmt.admin;

import com.marklogic.rest.util.RestConfig;


/**
 * Defines the configuration data for talking to the Admin REST API.
 */
public class AdminConfig extends RestConfig {

    public AdminConfig() {
        super("obp-test-1.demo.marklogic.com", 8001, "tammy", "isSparta");
    }

    public AdminConfig(String host, String password) {
        super(host, 8001, "admin", password);
    }

    public AdminConfig(String host, int port, String username, String password) {
        super(host, port, username, password);
    }

}
