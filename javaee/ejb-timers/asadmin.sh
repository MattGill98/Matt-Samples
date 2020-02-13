# Start fresh domain
asadmin start-domain

# Create instance config
asadmin copy-config default-config test-config
asadmin set configs.config.test-config.ejb-container.ejb-timer-service.ejb-timer-service=DataGrid
asadmin set configs.config.server-config.ejb-container.ejb-timer-service.ejb-timer-service=DataGrid

# Create deployment group and instances
asadmin create-instance --node localhost-domain1 --config test-config instance1
asadmin create-instance --node localhost-domain1 --config test-config instance2
asadmin create-deployment-group test-dg
asadmin add-instance-to-deployment-group --deploymentgroup test-dg --instance instance1
asadmin add-instance-to-deployment-group --deploymentgroup test-dg --instance instance2

# Restart domain
asadmin restart-domain

# Start instances
asadmin start-deployment-group test-dg

# Build and deploy application to deployment group
mvn package
asadmin deploy --target test-dg target/ejb-timers-1.0-SNAPSHOT.war