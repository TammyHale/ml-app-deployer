package com.marklogic.appdeployer.command.partitions;

import com.marklogic.appdeployer.command.AbstractManageResourceTest;
import com.marklogic.appdeployer.command.Command;
import com.marklogic.mgmt.ResourceManager;
import com.marklogic.mgmt.partitions.PartitionManager;
import com.marklogic.rest.util.Fragment;

public class ManagePartitionsTest extends AbstractManageResourceTest {

    
    @Override
    protected ResourceManager newResourceManager() {
        return new PartitionManager(manageClient,  "my-content-db", null);
    }

    @Override
    protected Command newCommand() {
        return new DeployPartitionsCommand();
    }

    @Override
    protected String[] getResourceNames() {
        return new String[] { "sample-app-partitions" };
    }

    @Override
    protected void afterResourcesCreated() {
        PartitionManager mgr = new PartitionManager(manageClient, "my-content-db", null);
        Fragment f = mgr.getPropertiesAsXml("sample-app-partition");
    }

}
