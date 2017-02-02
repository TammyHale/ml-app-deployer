package com.marklogic.appdeployer.command.partitions;

import java.io.File;

import com.marklogic.appdeployer.command.AbstractResourceCommand;
import com.marklogic.appdeployer.command.CommandContext;
import com.marklogic.appdeployer.command.SortOrderConstants;
import com.marklogic.mgmt.ResourceManager;
import com.marklogic.mgmt.partitions.PartitionManager;

public class DeployPartitionsCommand extends AbstractResourceCommand  {

    private String databaseIdOrName;
	private File[] payload;
    
	public DeployPartitionsCommand() {
		setExecuteSortOrder(SortOrderConstants.DEPLOY_PARTITIONS);
	}
	
	@Override
	protected File[] getResourceDirs(CommandContext context) {
		return new File[] { new File(context.getAppConfig().getConfigDir().getBaseDir(), "partitions") };
	}


    public void setDatabaseIdOrName(String databaseIdOrName) {
        this.databaseIdOrName = databaseIdOrName;
    }

	@Override
	protected ResourceManager getResourceManager(CommandContext context) {
		//TODO: this needs to support mltiple directories beneath partitions dir but right now
		// only uses the first and assumes structure of partitions/{database-name}/{payload}.json.
		File[] configDir = getResourceDirs(context);
        for (int i = 0;   i < configDir.length ; i++) {
        	File f = configDir[i];
             if (f.isDirectory()) {
            	 File[] payloadDirs = f.listFiles();
                 for (int j = 0;   j < payloadDirs.length ; j++) {
                	 if (f.isDirectory()){
                		 databaseIdOrName = f.getName();
                		 //get children as payload
                		 payload = f.listFiles();
                		 //hack to get out of loop. Fix later.
                		 i = configDir.length;
                	 }
                    
                 }
              }
            }
        return new PartitionManager(context.getManageClient(), databaseIdOrName, payload);
	}
}
