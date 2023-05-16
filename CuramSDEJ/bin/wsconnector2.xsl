<xsl:stylesheet version = '1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
  <xsl:output indent="yes" method="xml"/>
  <xsl:template match="/services">
    <project name="wsdl2java" default="main">
      <target name="main" >
        <xsl:comment>Generated Ant file to process each WSDL file specified in merged ws_outbound.xml.</xsl:comment>
        <xsl:for-each select="/services/service">

         <xsl:comment>Invoke Axis2 tooling to generate Java code for service <xsl:value-of select="@name"/></xsl:comment>
         <xsl:element name="echo"/>
         <xsl:element name="echo">
             Generating Java code for service <xsl:value-of select="@name"/>
         </xsl:element>

         <xsl:element name="java">
           <xsl:attribute name="classname">org.apache.axis2.wsdl.WSDL2Code</xsl:attribute>
           <xsl:attribute name="classpathref">connector.classpath</xsl:attribute>
           <xsl:attribute name="fork">${java.fork}</xsl:attribute>
           <xsl:attribute name="maxmemory">${java.maxmemory}</xsl:attribute>
           <xsl:attribute name="taskname">JavaFromWSDL</xsl:attribute>
           <xsl:attribute name="failonerror">${java.failonerror}</xsl:attribute>
           <xsl:element name="jvmarg">
             <xsl:attribute name="value">${java.jvmargs}</xsl:attribute>
           </xsl:element>        
           <xsl:element name="jvmarg">
             <xsl:attribute name="value">-Dfile.encoding=UTF-8</xsl:attribute>
           </xsl:element>

           <xsl:element name="arg">
             <xsl:attribute name="line">-o ${axis2.java.outdir}</xsl:attribute>
           </xsl:element>
         <xsl:choose>
           <xsl:when test="@extraWsdl2javaArgs != ''">
           <xsl:element name="arg">
             <xsl:attribute name="line">${axis2.extra.wsdl2java.args} <xsl:value-of select="@extraWsdl2javaArgs"/></xsl:attribute>
           </xsl:element>
           </xsl:when>
                        <xsl:otherwise>
           <xsl:element name="arg">
             <xsl:attribute name="line">${axis2.extra.wsdl2java.args}</xsl:attribute>
           </xsl:element>
   </xsl:otherwise>
           </xsl:choose>
           <xsl:element name="arg">
             <xsl:attribute name="value">-or</xsl:attribute>
           </xsl:element>
           <xsl:element name="arg">
             <xsl:attribute name="line">-uri ${SERVER_DIR}/<xsl:value-of select="@location"/></xsl:attribute>
           </xsl:element>
         </xsl:element>

        </xsl:for-each>

        <xsl:if test="count(/services/service) > 0">
          <xsl:call-template name="compile">
            <xsl:with-param name="dir">${dir.bld.svr.wsc2.jav}</xsl:with-param>
          </xsl:call-template>
        </xsl:if>

        <xsl:if test="count(/services/service) = 0">
          <echo> No web services connector WSDL files found. </echo>
        </xsl:if>

      </target>
    </project>
  </xsl:template>

  <!-- ******************************************************** -->
  <xsl:template name="compile">
    <xsl:param name="dir"></xsl:param>

    <xsl:comment>Compile</xsl:comment>
    <xsl:element name="javac">
      <xsl:attribute name="srcdir"> <xsl:value-of select="$dir" /></xsl:attribute>
      <xsl:attribute name="destdir"> <xsl:value-of select="$dir" /></xsl:attribute>
      <xsl:attribute name="debug">${cmp.debug}</xsl:attribute>
      <xsl:attribute name="fork">${cmp.fork}</xsl:attribute>
      <xsl:attribute name="memoryMaximumSize">${cmp.maxmemory}</xsl:attribute>
      <xsl:attribute name="memoryInitialSize">${cmp.maxmemory}</xsl:attribute>
      <xsl:attribute name="nowarn">${cmp.nowarn}</xsl:attribute>
      <xsl:attribute name="optimize">${cmp.optimize}</xsl:attribute>
      <xsl:attribute name="deprecation">${cmp.deprecation}</xsl:attribute>
      <xsl:attribute name="verbose">${cmp.verbose}</xsl:attribute>
      <xsl:attribute name="includeAntRuntime">${cmp.includeAntRuntime}</xsl:attribute>
      <xsl:attribute name="includeJavaRuntime">${cmp.includeJavaRuntime}</xsl:attribute>
      <xsl:attribute name="failonerror">${cmp.failonerror}</xsl:attribute>
      <xsl:attribute name="listfiles">${cmp.listfiles}</xsl:attribute>
      <classpath refid="connector.classpath" />
      <include name="**/*.java" />
    </xsl:element>

   <xsl:element name="copy">
      <xsl:attribute name="toDir"><xsl:value-of select="$dir" /></xsl:attribute>
        <xsl:call-template name="fileset">
            <xsl:with-param name="dirtest"><xsl:value-of select="$dir" /></xsl:with-param>
          </xsl:call-template>
     </xsl:element>
  </xsl:template>
  <xsl:template name="fileset">
    <xsl:param name="dirtest"></xsl:param>
    <xsl:element name="fileset">
          <xsl:attribute name="dir"><xsl:value-of select="$dirtest" />/resources</xsl:attribute>
          <xsl:attribute name="erroronmissingdir">false</xsl:attribute>
          <xsl:attribute name="includes">**/*.*</xsl:attribute>


    </xsl:element>


  </xsl:template>
  <!-- ******************************************************** -->

</xsl:stylesheet>
