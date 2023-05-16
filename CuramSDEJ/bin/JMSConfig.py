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


#----------------------------------------------
# Creates new JMS Connection factory
#----------------------------------------------
def configFactory ( factoryName, serverName, nodeName, SIBusName ):

        #--------------------------------------------------------------------
        # Set up globals
        #--------------------------------------------------------------------

        global J2CAlias
        global AdminConfig
        global AdminTask

        #--------------------------------------------------------------------
        # Create SIB JMS connection factory
        #--------------------------------------------------------------------
        jmsCF = AdminConfig.getid("/Node:"+nodeName+"/J2CResourceAdapter:SIB JMS Resource Adapter/J2CConnectionFactory:"+factoryName )
        if (len(jmsCF) != 0):
                print "The "+factoryName+" JMS connection factory already exists - removing."
                AdminConfig.remove(jmsCF )
        #endIf 

        #--------------------------------------------------------------------
        # Create the SIB JMS connection factory
        #--------------------------------------------------------------------

        scope = AdminConfig.getid("/Node:"+nodeName+"/" )

        print "Create a new SIB JMS connection factory named "+factoryName+"."
        params = ["-name", factoryName, "-jndiName", "jms/"+factoryName, "-busName", SIBusName, "-authDataAlias", J2CAlias, "-xaRecoveryAuthAlias", J2CAlias]
        params.append("-description")
        params.append("The connection factory for all connections to curam queues.")
        
        AdminTask.createSIBJMSConnectionFactory(scope, params )
        jmsCF = AdminConfig.getid("/Node:"+nodeName+"/J2CResourceAdapter:SIB JMS Resource Adapter/J2CConnectionFactory:"+factoryName )
        AdminConfig.create("MappingModule", jmsCF, [["mappingConfigAlias", "DefaultPrincipalMapping"], ["authDataAlias", J2CAlias]] )

        print " - SIB JMS Connection Factory "+factoryName+" created."
#endDef 

def configQueues ( queueName, factoryName, serverName, nodeName, SIBusName ):

        global J2CAlias
        global AdminConfig
        global AdminTask

        #--------------------------------------------------------------------
        # Create SIB JMS queue that references a SIB queue
        #--------------------------------------------------------------------

        scope = AdminConfig.getid("/Node:"+nodeName+"/" )
        queue = AdminTask.listSIBJMSQueues(scope )
        queueList = queue.splitlines()

        for queue in queueList:
                name = AdminConfig.showAttribute(queue, "name" )
                if (name == queueName):
                        print "The "+queueName+" SIB JMS queue already exists - removing"
                        AdminTask.deleteSIBJMSQueue(queue )
                #endIf 
        #endFor 

        print "Create a new SIB JMS queue named "+queueName
        params = ["-name", queueName, "-jndiName", "jms/"+queueName, "-description", "Selfdescribing", "-queueName", "SIB_" + queueName, "-busName", SIBusName, "-deliveryMode", "Persistent"]
        AdminTask.createSIBJMSQueue(scope, params )
        spec = AdminTask.listSIBJMSActivationSpecs(scope )
        specList = spec.splitlines()

        for spec in specList:                
                name = AdminConfig.showAttribute(spec, "name" )
                if (name == queueName):
                        print "The "+queueName+" SIB JMS activation spec already exists - removing"
                        AdminTask.deleteSIBJMSActivationSpec(spec )
                #endIf 
        #endFor 

        print "Create a new SIB JMS activation spec named "+queueName+"."
        params = ["-name", queueName, "-jndiName", "eis/"+queueName+"AS", "-busName", SIBusName, "-destinationJndiName", "jms/"+queueName, "-destinationType", "javax.jms.Queue"]
        params.append("-authenticationAlias")
        params.append(J2CAlias)
        AdminTask.createSIBJMSActivationSpec(scope, params )

#endDef 

def setup ( serverName, nodeName, j2c_alias ):

        global AdminConfig, J2CAlias, provider

        #Set up node Name
        nodeId = AdminConfig.getid("/Node:"+nodeName+"/" )
        hostname = AdminConfig.showAttribute(nodeId, "hostName" )
        J2CAlias = hostname+"/"+j2c_alias

#endDef 

#-----------------------------------------------------------------
# Main
#-----------------------------------------------------------------
print ""
print "Curam Websphere JMS Configuration Script"
print ""

nodeName = sys.argv[0]
serverName = sys.argv[1]
factoryList = sys.argv[2]
queueList = sys.argv[3]
J2CAlias = sys.argv[4]
SIBusName = sys.argv[5]

setup(serverName, nodeName, J2CAlias )
factoryList = factoryList.split(" ")
for factoryEntry in factoryList:
        configFactory(factoryEntry, serverName, nodeName, SIBusName )
#endFor 

queueList = queueList.split(" ")
for queueEntry in queueList:
        configQueues(queueEntry, factoryEntry, serverName, nodeName, SIBusName )
#endFor 

#--------------------------------------------------------------
# Save all the changes
#--------------------------------------------------------------
AdminConfig.validate( )
AdminConfig.save( )

print ""
print "JMS Configuration Script executed successfully."
print ""

