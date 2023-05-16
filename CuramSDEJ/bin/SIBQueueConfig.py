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


def configSIBQueue ( nodeName, serverName, SIBusName, queueName, errorQueue ):

        global AdminConfig
        global AdminTask
        queue = AdminTask.listSIBDestinations(["-bus", SIBusName, "-type", "Queue"] )
        queueList = queue.splitlines()
        for queue in queueList:                
                identifier = AdminConfig.showAttribute(queue, "identifier" )
                if (identifier == "SIB_"+queueName):
                        print "The SIB_"+queueName+" SIB queue already exists - removing"
                        AdminConfig.remove(queue )
                #endIf 
        #endFor 

        #--------------------------------------------------------------------
        # Create SIB queue
        #--------------------------------------------------------------------

        print "Create a new SIB queue named SIB_"+queueName+"."
        params = ["-bus", SIBusName, "-name", "SIB_" + queueName, "-type", "Queue", "-node", nodeName, "-server", serverName]
        params.append("-maxFailedDeliveries")
        params.append(5)
        if (len(errorQueue) != 0):
                params.append("-exceptionDestination")
                params.append("SIB_" + errorQueue)
        #endIf 
        AdminTask.createSIBDestination(params )

#endDef 

#-----------------------------------------------------------------
# Main
#-----------------------------------------------------------------
print ""
print "Curam Websphere SIBQueue Configuration Script"
print ""

nodeName = sys.argv[0]
serverName = sys.argv[1]
SIBusName = sys.argv[2]
queue = sys.argv[3]
errorQueue = ""
if (len(sys.argv) == 5):
        errorQueue = sys.argv[4]		
        print "errorQueue = " + sys.argv[4]
#endIf

configSIBQueue(nodeName, serverName, SIBusName, queue, errorQueue )

#--------------------------------------------------------------
# Save all the changes
#--------------------------------------------------------------
AdminConfig.validate( )
AdminConfig.save( )

print ""
print "SIBQueue Configuration Script executed successfully."
print ""

