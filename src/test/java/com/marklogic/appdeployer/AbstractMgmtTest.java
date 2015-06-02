package com.marklogic.appdeployer;

import java.io.File;
import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.marklogic.appdeployer.AppConfig;
import com.marklogic.appdeployer.manager.SimpleAppManager;
import com.marklogic.appdeployer.manager.spring.SpringAppManager;
import com.marklogic.appdeployer.plugin.RestApiPlugin;
import com.marklogic.junit.spring.LoggingTestExecutionListener;
import com.marklogic.rest.mgmt.ManageClient;
import com.marklogic.rest.mgmt.ManageConfig;
import com.marklogic.rest.mgmt.admin.AdminConfig;
import com.marklogic.rest.mgmt.admin.AdminManager;

/**
 * Base class for tests that run against the new management API in ML8. Main purpose is to provide convenience methods
 * for quickly creating and deleting a sample application.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MgmtTestConfig.class })
@TestExecutionListeners({ LoggingTestExecutionListener.class, DependencyInjectionTestExecutionListener.class })
public abstract class AbstractMgmtTest extends Assert {

    public final static String SAMPLE_APP_NAME = "sample-app";

    protected final static Integer SAMPLE_APP_REST_PORT = 8540;
    protected final static Integer SAMPLE_APP_TEST_REST_PORT = 8541;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected ManageConfig manageConfig;

    @Autowired
    private AdminConfig adminConfig;

    protected ConfigDir configDir;
    protected ManageClient manageClient;
    protected AppManager appManager;
    protected AdminManager adminManager;
    protected ConfigurableApplicationContext appManagerContext;

    protected AppConfig appConfig;

    @Before
    public void initialize() {
        initializeAppConfig();

        configDir = new ConfigDir(new File("src/test/resources/sample-app/src/main/ml-config"));
        manageClient = new ManageClient(manageConfig);
        adminManager = new AdminManager(adminConfig);
    }

    protected void initializeAppConfig() {
        appConfig = new AppConfig();
        appConfig.setName(SAMPLE_APP_NAME);
        appConfig.setRestPort(SAMPLE_APP_REST_PORT);
    }

    protected void initializeAppManager() {
        initializeAppManager(new RestApiPlugin());
    }

    /**
     * Initialize an AppManager with the given set of plugins. Avoids having to create a Spring configuration.
     * 
     * @param plugins
     */
    protected void initializeAppManager(AppPlugin... plugins) {
        SimpleAppManager m = new SimpleAppManager(manageClient, adminManager);
        m.setAppPlugins(Arrays.asList(plugins));
        appManager = m;
    }

    /**
     * Initialize AppManager with a Spring Configuration class.
     * 
     * @param configurationClass
     */
    protected void initializeAppManager(Class<?> configurationClass) {
        appManagerContext = new AnnotationConfigApplicationContext(configurationClass);
        appManager = new SpringAppManager(appManagerContext, manageClient, adminManager);
    }

    @After
    public void closeAppContext() {
        if (appManagerContext != null) {
            appManagerContext.close();
        }
    }

    /**
     * Useful for when your test only needs a REST API and not full the sample app created.
     */
    protected void createSampleAppRestApi() {
        new RestApiPlugin().onCreate(new AppPluginContext(appConfig, configDir, manageClient, adminManager));
    }

    protected void deleteSampleApp() {
        try {
            appManager.deleteApp(appConfig, configDir);
        } catch (Exception e) {
            logger.warn("Error while waiting for MarkLogic to restart: " + e.getMessage());
        }
    }
}