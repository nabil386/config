<xsl:stylesheet version = '1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
  <xsl:output indent="yes" method="xml"/>
  <xsl:template match="/services">
    <project name="wsdl2java" default="main">
      <target name="main" >
        <xsl:comment>Generated Ant file to process each WSDL file specified in webservices_config.xml.</xsl:comment>
        <xsl:for-each select="/services/service">


          <xsl:variable name="outdir">
            <xsl:choose>
              <xsl:when test="normalize-space(@forcemodelimport)">
                <xsl:value-of select="'${dir.bld.svr.wsc.mdl}'"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="'${dir.bld.svr.wsc.jav}'"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>


          <xsl:element name="wsdl2java">
            <xsl:attribute name="output"><xsl:value-of select="$outdir"/></xsl:attribute>
            <xsl:attribute name="url">${base.dir}/<xsl:value-of select="@location"/></xsl:attribute>
            <xsl:attribute name="verbose">no</xsl:attribute>
            <xsl:attribute name="package">wsconnector</xsl:attribute>
          </xsl:element>

        </xsl:for-each>

        <xsl:if test="count(/services/service) > 0">

          <xsl:call-template name="compile">
            <xsl:with-param name="dir">${dir.bld.svr.wsc.jav}</xsl:with-param>
          </xsl:call-template>
          <xsl:call-template name="compile">
            <xsl:with-param name="dir">${dir.bld.svr.wsc.mdl}</xsl:with-param>
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

  </xsl:template>
  <!-- ******************************************************** -->

</xsl:stylesheet>


