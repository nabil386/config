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

def configureServer ( serverName, nodeName, bootStrap, os, mode, initialHeapSize, maximumHeapSize):

        #--------------------------------------------------------------
        # set up globals
        #--------------------------------------------------------------
        global AdminConfig
        global AdminControl
        global AdminApp
        global AdminTask

        #--------------------------------------------------------------
        # do some sanity checking
        #     -- do we have a node by this name?
        #--------------------------------------------------------------
        node = AdminConfig.getid("/Node:"+nodeName+"/" )
        print "Checking for existence of node "+nodeName
        if (len(node) == 0):
                print "Error -- node not found for name "+nodeName
                return 1
        #endIf 

        server = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/" )
        print "Checking for existence of Server "+serverName
        if (len(server) == 0):
                print "Error -- node not found for name "+serverName
                return 1
        #endIf 

        #---------------------------------------------------------
        # Set the BOOTSTRAP_ADDRESS port
        #
        # This is an attribute on the NameServer object inside the Server.
        # To modify this endpoint, you need to get the id of the NameServer
        # and invoke modify:
        #---------------------------------------------------------
        print "Setting the BOOTSTRAP_ADDRESS port to "+bootStrap
        ns = AdminConfig.list("NameServer", server )
        port_attr = ["port", bootStrap]
        boot_attrs = ["BOOTSTRAP_ADDRESS", [port_attr]]
        attrs = [boot_attrs]
        AdminConfig.modify(ns, attrs )

        if (os == "zos"):
                serverEntriesList = AdminConfig.list("ServerEntry", node ).splitlines()
                for serverEntry in serverEntriesList:
                        sName = AdminConfig.showAttribute(serverEntry, "serverName" )
                        if (sName == serverName):
                                specialEndPoints = AdminConfig.showAttribute(serverEntry, "specialEndpoints" )
                                specialEndPoints = specialEndPoints.replace('[','')
                                specialEndPoints = specialEndPoints.replace(']','')
                                specialEndPointList = specialEndPoints.split(' ')
                                for specialEndPoint in specialEndPointList:
                                        endPointNm = AdminConfig.showAttribute(specialEndPoint, "endPointName" )
                                        if (endPointNm == "ORB_LISTENER_ADDRESS"):
                                                ePoint = AdminConfig.showAttribute(specialEndPoint, "endPoint" )
                                                AdminConfig.modify(ePoint, [["port", bootStrap]] )
                                                break 
                                        #endIf 
                                #endFor 
                        #endIf 
                #endFor 

                # Process z/OS 64-bit mode
                if (mode == "true"):
                        print "Setting 64-bit mode"
                        AdminTask.setJVMMode (["-nodeName", nodeName, "-serverName", serverName, "-mode", "64bit"])
                #endIf    
        #endIf 

        print "Setting the Application ClassLoader policy to Single"
        print "Setting the Application Class Loading mode to PARENT_LAST"
        app = AdminConfig.list("ApplicationServer", server )
        attr1 = ["applicationClassLoaderPolicy", "MULTIPLE"]
        attrs = [attr1]
        AdminConfig.modify(app, attrs )

        print "Setting the ORB Pass by Reference to true"
        orb = AdminConfig.list("ObjectRequestBroker", server )
        attr = ["noLocalCopies", "true"]
        attrs = [attr]
        AdminConfig.modify(orb, attrs )

        print "Setting the JVM initial heap size to " + initialHeapSize + "MB and maximum heap size to " + maximumHeapSize + "MB."
        if (os == "zos"):
            AdminTask.setJVMInitialHeapSize(["-nodeName", nodeName, "-serverName", serverName, "-processType", "Adjunct", "-initialHeapSize", initialHeapSize])
            AdminTask.setJVMMaxHeapSize(["-nodeName", nodeName, "-serverName", serverName, "-processType", "Adjunct", "-maximumHeapSize", maximumHeapSize])
            AdminTask.setJVMInitialHeapSize(["-nodeName", nodeName, "-serverName", serverName, "-processType", "Control", "-initialHeapSize", initialHeapSize])
            AdminTask.setJVMMaxHeapSize(["-nodeName", nodeName, "-serverName", serverName, "-processType", "Control", "-maximumHeapSize", maximumHeapSize])
            AdminTask.setJVMInitialHeapSize(["-nodeName", nodeName, "-serverName", serverName, "-processType", "Servant", "-initialHeapSize", initialHeapSize])
            AdminTask.setJVMMaxHeapSize(["-nodeName", nodeName, "-serverName", serverName, "-processType", "Servant", "-maximumHeapSize", maximumHeapSize])
        else:
            AdminTask.setJVMInitialHeapSize(["-nodeName", nodeName, "-serverName", serverName, "-initialHeapSize", initialHeapSize])
            AdminTask.setJVMMaxHeapSize(["-nodeName", nodeName, "-serverName", serverName, "-maximumHeapSize", maximumHeapSize])
        #endElse 

        jvm = AdminConfig.list("JavaVirtualMachine", server )
        
        if (os == "unix"):
                print "Setting the JVM Entry to -Djava.awt.headless=true"
                attr = ["genericJvmArguments", "-Djava.awt.headless=true"]
                attrs = [attr]
                AdminConfig.modify(jvm, attrs )
        #endIf 

        systemProperty = []
        systemProperty.append(["name", "com.ibm.websphere.security.util.authCacheCustomKeySupport"])
        systemProperty.append(["value", "false"])
        print "Setting com.ibm.websphere.security.util.authCacheCustomKeySupport JVM property"

        if (os == "zos"):
                jvm = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/" )
                jvmList = AdminConfig.list("JavaVirtualMachine", jvm ).splitlines()
                for jvm in jvmList:
                        AdminConfig.modify(jvm, [["systemProperties", [systemProperty]]] )
                #endFor 
        else:
                AdminConfig.modify(jvm, [["systemProperties", [systemProperty]]] )
        #endElse 

        streamRedirectList = AdminConfig.list("StreamRedirect", server ).splitlines()
        for streamRedirect in streamRedirectList:
                attr = ["maxNumberOfBackupFiles", 30]
                attrs = [attr]
                AdminConfig.modify(streamRedirect, attrs )
        #endFor 
        #serid = AdminConfig.getid( "/Server:CuramServer" )        
        serid = AdminConfig.getid( "/Server:"+serverName+"/" )
        print serid
        webid = AdminConfig.list( "WebContainer",serid )
        print webid
	attr = [ ['name','com.ibm.ws.webcontainer.suppressLoggingFileNotFoundExceptions'],['value','true'] ]
        AdminConfig.create('Property', webid, attr)
        modifySessionManagerAttribute( server, "enableSecurityIntegration", "false" )


        #--------------------------------------------------------------
        # Save all the changes
        #--------------------------------------------------------------
        AdminConfig.save()
#endDef 


def modifySessionManagerAttribute ( server, attributeName, attribValToSet ):

        #--------------------------------------------------------------
        # set up globals
        #--------------------------------------------------------------
        global AdminConfig
        serversmList = AdminConfig.list("SessionManager", server )
        if (len(serversmList) > 0):
                attribVal = AdminConfig.showAttribute(serversmList, attributeName )
                print "Checking Session Manager: " + attributeName + " :" + attribVal
                if (len(attribVal) > 0 and attribVal != attribValToSet):
                  AdminConfig.modify(serversmList, [[attributeName, attribValToSet]] )
                  print "modify " + attributeName + " to "  + attribValToSet
                #endIf 
         #endIf 
        
#endDef 

#-----------------------------------------------------------------
# Main
#-----------------------------------------------------------------
print ""
print "Curam Websphere Server Configuration Script"
print ""

serverName = sys.argv[0]
nodeName = sys.argv[1]
bootStrap = sys.argv[2]
os = sys.argv[3]
mode = sys.argv[4]
initialHeapSize = sys.argv[5]
maximumHeapSize = sys.argv[6]

configureServer(serverName, nodeName, bootStrap, os, mode, initialHeapSize, maximumHeapSize)

#--------------------------------------------------------------
# Save all the changes
#--------------------------------------------------------------
AdminConfig.validate( )
AdminConfig.save( )

print ""
print "Server Configuration Script executed successfully."
print ""
