#/*
# * Licensed Materials - Property of IBM
# *
# * PID 5725-H26
# *
# * Copyright IBM Corporation 2012,2013. All rights reserved.
# *
# * US Government Users Restricted Rights - Use, duplication or disclosure
# * restricted by GSA ADP Schedule Contract with IBM Corp.
# */


import sys

def createSIBus ( nodeName, serverName, SIBusName, username ):

        global AdminConfig
        global AdminTask

        nodeId = AdminConfig.getid("/Node:"+nodeName+"/" )
        server = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/" )
        bus = AdminConfig.getid("/SIBus:"+SIBusName )

        if (len(bus) != 0):
                AdminConfig.remove(bus )
                print "The "+SIBusName+" already exists - removing."
        #endIf 
        print "Create a new SI bus named "+SIBusName+"."
        AdminTask.createSIBus(["-bus", SIBusName, "-description", "Messaging bus for Curam"] )
        busMember = AdminTask.listSIBusMembers(["-bus", SIBusName] )
        busMemberList = busMember.split()
        for busMember in busMemberList:
              busMemberNode = AdminConfig.showAttribute(busMember, "node" )
              busMemberServer = AdminConfig.showAttribute(busMember, "server" )
              if (( busMemberNode == nodeName )  and ( busMemberServer == serverName ) ):
                  print "The bus member already exists - removing"
                  AdminConfig.remove(busMember )
              #endIf 
        #endFor 
        listSIBMessagingEngine = AdminConfig.list("SIBMessagingEngine", server )
        listSIBMessagingEngine = listSIBMessagingEngine.splitlines()
        for messagingEngine in listSIBMessagingEngine:
                
                AdminConfig.remove(messagingEngine )
        #endFor 

        print "Add the server named "+serverName+" to the bus."
        AdminTask.addSIBusMember(["-bus", SIBusName, "-node", nodeName, "-server", serverName, "-createDefaultDatasource", "false", "-datasourceJndiName", "jdbc/curamsibdb"] ) 

        hostname = AdminConfig.showAttribute(nodeId, "hostName" )

        dataStore = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/SIBMessagingEngine:/SIBDatastore:/" )

        attr1 = ["schemaName", username]
        attr2 = ["createTables", "false"]
        attrs = [attr1, attr2]
        AdminConfig.modify(dataStore, attrs )

        services = AdminConfig.list("SIBService", server )
        services = services.split()

        if (len(services) == 0):
                print "The SIBService service could not be found."
                
        #endIf 

        serviceID = services[0]
        
        enable = "true"
        if (AdminConfig.showAttribute(serviceID, "enable" ) == enable):
                print "The SIBService service is already enabled."
        else:
                AdminConfig.modify(serviceID, [["enable", enable]] )
        #endElse 

        # Add necessary security information  
        AdminTask.addGroupToBusConnectorRole("-bus "+SIBusName+" -group AllAuthenticated" ) 
#endDef 

#-----------------------------------------------------------------
# Main
#-----------------------------------------------------------------
print ""
print "Curam Websphere SIBus Configuration Script"
print ""

nodeName = sys.argv[0]
serverName = sys.argv[1]
SIBusName = sys.argv[2]
username = sys.argv[3]

createSIBus(nodeName, serverName, SIBusName, username )

#--------------------------------------------------------------
# Save all the changes
#--------------------------------------------------------------
AdminConfig.validate( )
AdminConfig.save( )

print ""
print "SIBus Configuration Script executed successfully."
print ""
