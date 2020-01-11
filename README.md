# GlobalProvisioningService

GlobalProvisingService is used for provisioning VM to users registered with GPS.  Users can have two roles: USER and MASTER.  
MASTER user can delete or access the VM provisioned for themselves and other users.

A User can be created and deleted within GPS.

Additionally GPS also provides APIs to request a VM, get list of all VMs provisioned by GPS, get list of VMs based on a user
sorted by the max RAM size provisioned to the user.  Additionally, we can also get the list of all VMs available in GPS sorted
by max RAM size across all users.

All provisioning and delete user APIs are authenticated by JWT authentication mechanism.
