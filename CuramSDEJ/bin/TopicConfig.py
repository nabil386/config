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
def configFactory ( factoryName, serverName, nodeName, SIBusName, J2CAlias ):

        #--------------------------------------------------------------------
        # Set up globals
        #--------------------------------------------------------------------
        global AdminConfig
        global AdminTask

        nodeId = AdminConfig.getid("/Node:"+nodeName+"/" )
        hostname = AdminConfig.showAttribute(nodeId, "hostName" )
        J2CAlias = hostname+"/"+J2CAlias

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
        params.append("The connection factory for all connections to curam topics.")
        AdminTask.createSIBJMSConnectionFactory(scope, params ) 

        jmsCF = AdminConfig.getid("/Node:"+nodeName+"/J2CResourceAdapter:SIB JMS Resource Adapter/J2CConnectionFactory:"+factoryName )
        AdminConfig.create("MappingModule", jmsCF, [["mappingConfigAlias", "DefaultPrincipalMapping"], ["authDataAlias", J2CAlias]] )

        print " - SIB JMS Connection Factory "+factoryName+" created."
#endDef 

def configTopics ( topicName, factoryName, serverName, nodeName, SIBusName, J2CAlias ):

        #--------------------------------------------------------------------
        # Set up globals
        #--------------------------------------------------------------------
        global AdminConfig
        global AdminTask

        nodeId = AdminConfig.getid("/Node:"+nodeName+"/" )
        hostname = AdminConfig.showAttribute(nodeId, "hostName" )
        J2CAlias = hostname+"/"+J2CAlias

        #--------------------------------------------------------------------
        # Create SIB JMS topic that references a SIB topic
        #--------------------------------------------------------------------
        scope = AdminConfig.getid("/Node:"+nodeName+"/" )

        topicList = AdminTask.listSIBJMSTopics(scope ).splitlines()
        for topic in topicList:
                name = AdminConfig.showAttribute(topic, "name" )
                if (name == topicName):
                        print "The "+topicName+" SIB JMS topic already exists."
                        AdminTask.deleteSIBJMSTopic(topic ) 
                #endIf 
        #endFor 

        print "Create a new SIB JMS topic named "+topicName+"."
        params = ["-name", topicName, "-jndiName", "jms/"+topicName, "-description", "Selfdescribing", "-topicName", "SIB_"+topicName, "-topicSpace", "Default.Topic.Space", "-busName", SIBusName, "-deliveryMode", "Persistent"]
        AdminTask.createSIBJMSTopic(scope, params ) 

        specList = AdminTask.listSIBJMSActivationSpecs(scope).splitlines()
        for spec in specList:
                name = AdminConfig.showAttribute(spec, "name" )
                if (name == topicName):
                        print "The "+topicName+" SIB JMS activation spec already exists - removing"
                        AdminTask.deleteSIBJMSActivationSpec(spec )
                #endIf 
        #endFor 

        messageEngine = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/SIBMessagingEngine:/" )
        messageEngineName = AdminConfig.showAttribute(messageEngine, "name" )

        print "Create a new SIB JMS activation spec named "+topicName+"."
        params = ["-name", topicName, "-jndiName", "eis/"+topicName+"AS", "-busName", SIBusName, "-destinationJndiName", "jms/"+topicName, "-destinationType", "javax.jms.Topic"]
        params.append("-authenticationAlias")
        params.append(J2CAlias)
        AdminTask.createSIBJMSActivationSpec(scope, params ) 

#endDef 

#-----------------------------------------------------------------
# Main
#-----------------------------------------------------------------
print ""
print "Curam Websphere Topic Configuration Script"
print ""

nodeName = sys.argv[0]
serverName = sys.argv[1]
factoryEntry = sys.argv[2]
topicEntry = sys.argv[3]
J2CAlias = sys.argv[4]
SIBusName = sys.argv[5]

configFactory(factoryEntry, serverName, nodeName, SIBusName, J2CAlias )

configTopics(topicEntry, factoryEntry, serverName, nodeName, SIBusName, J2CAlias )

#--------------------------------------------------------------
# Save all the changes
#--------------------------------------------------------------
AdminConfig.validate( )
AdminConfig.save( )

print ""
print "Topic Configuration Script executed successfully."
print ""

