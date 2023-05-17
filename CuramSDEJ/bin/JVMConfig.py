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

def configureJvm ( nodeName, serverName, os, changeType, name, value ):

        #--------------------------------------------------------------
        # set up globals
        #--------------------------------------------------------------
        global AdminConfig
        global AdminControl
        global AdminApp

        #--------------------------------------------------------------
        # do some sanity checking
        #     -- do we have a node and server by this name?
        #--------------------------------------------------------------
        node = AdminConfig.getid("/Node:"+nodeName+"/" )
        print "Checking for existence of Node "+nodeName
        if (len(node) == 0):
                print "Error -- Node not found for name "+nodeName
                return 1
        #endIf 

        print "Checking for existence of Server "+serverName
        server = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/" )
        if (len(server) == 0):
                print "Error -- Server not found for name "+serverName
                return 1
        #endIf 

        #---------------------------------------------------------
        # JVM configuration
        #---------------------------------------------------------
        jvm = AdminConfig.list("JavaVirtualMachine", server )

        if (changeType == "argument"):
            #---------------------------------------------------------
            # JVM argument
            #---------------------------------------------------------
            print "Setting the JVM Entry to "+name

            if (os == "zos"):
                jvm = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/" )
                jvmList = AdminConfig.list("JavaVirtualMachine", jvm ).splitlines()
                for jvm in jvmList:
                    oldArgs = AdminConfig.showAttribute(jvm,"genericJvmArguments")
                    newArgs = oldArgs+" "+name
                    attr = ["genericJvmArguments", newArgs]
                    attrs = [attr]
                    AdminConfig.modify(jvm, attrs )
                #endFor 
            else:
                oldArgs = AdminConfig.showAttribute(jvm,"genericJvmArguments")
                newArgs = oldArgs+" "+name
                attr = ["genericJvmArguments", newArgs]
                attrs = [attr]
                AdminConfig.modify(jvm, attrs )
            #endElse
        else:
            #---------------------------------------------------------
            # Custom property
            #---------------------------------------------------------
            systemProperty = []
            systemProperty.append(["name", name])
            systemProperty.append(["value", value])
            print "Setting "+name+" property to: "+value

            if (os == "zos"):
               jvm = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/" )
               jvmList = AdminConfig.list("JavaVirtualMachine", jvm ).splitlines()
               for jvm in jvmList:
                   AdminConfig.modify(jvm, [["systemProperties", [systemProperty]]] )
               #endFor 
            else:
               AdminConfig.modify(jvm, [["systemProperties", [systemProperty]]] )
            #endElse
        #endIf


        #--------------------------------------------------------------
        # Save all the changes
        #--------------------------------------------------------------
        AdminConfig.save( )
#endDef 

#-----------------------------------------------------------------
# Main
#-----------------------------------------------------------------
print ""
print "Curam Websphere JVM Configuration Script"
print ""

# 
# Two cases are catered for:
#  1. Generic JVM arguments (e.g. -Xsomething)
#  2. Custom WebSphere JVM properties (e.g. com.ibm.websphere.something=true)
#
#  This behavior is determined by the changeType argument: "argument" or any other
#  value for a custom property.  In the first case only the name argument is used
#  and in the second case name and value are both used.
#
nodeName = sys.argv[0]
serverName = sys.argv[1]
os = sys.argv[2]
changeType = sys.argv[3]
name = sys.argv[4]
if (len(sys.argv)>5 and sys.argv[5] != "dummy"):
    value = sys.argv[5]
else:
    value = ""
# endIf
    
# Check input arguments
if (changeType != "argument"):
    if (value == ""):
        print "You have specified a JVM modification of a custom property and value, but no value has been specified for property: "+name
        sys.exit(1)
    # endIf
# endIf
    

configureJvm(nodeName, serverName, os, changeType, name, value )

#--------------------------------------------------------------
# Save all the changes
#--------------------------------------------------------------
AdminConfig.validate( )
AdminConfig.save( )

print ""
print "JVM Configuration Script execution completed."
print ""
