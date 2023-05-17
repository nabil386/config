#!/bin/sh

# Sample UNIX script for Curam XMLServer printing.

# This script illustrates printing on UNIX or UNIX-like
# environments.
# In general printing capabilities vary widely
# by OS distribution, version, installed software,
# physical printer capabilities, etc.
# Review your local environment for requirements
# and how to best implement printing support.
#

echo ---------------------------------------------------- >> XMLServer.log
# log output
echo File:         $1             >> XMLServer.log
echo Print Server: $2             >> XMLServer.log
Platform=`uname`
echo Platform:     $Platform      >> XMLServer.log

# The following illustrates some possible print solutions
# for various platforms:

case $Platform in
  # z/OS:
  OS/390)
    # On OS/390 (z/OS) use of the lop command as
    # illustrated would be dependent on the Infoprint
    # Server installation and configuration, related
    # software, and a printer with direct PDF support
    # and sufficient memory.
    echo Starting print...        >> XMLServer.log
    lp -d $2 $1
    echo Printing Completed       >> XMLServer.log
  ;;

  AIX)
    # AIX has no native print support for PDF files,
    # so you would need to implement functionality such as
    # pdf2ps to convert the generated PDF file to
    # PostScript for printing with lpr; e.g.:
    # see the IBM Redbook SG24-6018-00
    #   pdf2ps $1 $1.ps
    #   lpr -P $2 $1.ps
    echo $Platform printing implementation is TBD.        >> XMLServer.log
  ;;

  # Other platforms:
  *)
    # Your local print functionality to be implemented here ...
    echo $Platform printing implementation is TBD.        >> XMLServer.log
  ;;
esac

echo ---------------------------------------------------- >> XMLServer.log
