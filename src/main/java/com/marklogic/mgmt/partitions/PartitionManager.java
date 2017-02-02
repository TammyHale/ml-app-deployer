package com.marklogic.mgmt.partitions;

import java.io.File;

import com.marklogic.mgmt.AbstractResourceManager;
import com.marklogic.mgmt.DeleteReceipt;
import com.marklogic.mgmt.ManageClient;

public class PartitionManager  extends AbstractResourceManager {

    private String databaseIdOrName;
    
	public PartitionManager(ManageClient manageClient, String databaseIdOrName, File[] payload) {
        super(manageClient);
        this.databaseIdOrName = databaseIdOrName;
    }

    @Override
    protected boolean useAdminUser() {
        return true;
    }

    @Override
    public String getResourcesPath() {
        return format("/manage/v2/databases/%s/partitions", databaseIdOrName);
    }

    
    
    @Override
    public DeleteReceipt delete(String payload, String... resourceUrlParams) {
        String resourceId = getResourceId(payload);
        if (resourceId != null && resourceId.toUpperCase().equals("DEFAULT")) {
            return new DeleteReceipt(resourceId, null, false);
        }
        return super.delete(payload);
    }
}
