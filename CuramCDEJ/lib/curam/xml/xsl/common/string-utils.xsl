<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
  Copyright © 2003-2006 Curam Software Ltd.
  All rights reserved.
 
  This software is the confidential and proprietary information of Curam
  Software, Ltd. ("Confidential Information"). You shall not disclose
  such Confidential Information and shall use it only in accordance with the
  terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <!--
    Extracts a localized message from a string of locale codes and
    messages. The string format is "locale1|message1|locale2|message2|...".
    The templates produces the message that matches the locale code
    parameter or, if the string contains no "|" characters, the entire
    string is returned. If no matching locale code is found, the message for
    locale language code is attempted to find, or, failing that, the message
    matching the locale code "en", or failing that, the first message
    is produced.
    
    $locale-code  The locale to use to extract the message. Defaults to "en".
    $messages     The string containing the messages.
  -->
  <xsl:template name="get-localized-message">
    <xsl:param name="locale-code" select="'en'" />
    <xsl:param name="messages" select="''" />

    <xsl:variable name="code-at-start"
                  select="concat($locale-code, '|')" />
    <xsl:variable name="code-in-middle"
                  select="concat('|', $code-at-start)" />

     <xsl:variable name="language-code">
       <xsl:choose>
        <xsl:when test="contains($locale-code, '_')">
         <xsl:value-of select="substring-before($locale-code, '_')"/>
        </xsl:when>
        <xsl:otherwise>''</xsl:otherwise>
        </xsl:choose>
     </xsl:variable>
          
   <xsl:choose>
      <xsl:when test="not(contains($messages, '|'))">
        <xsl:value-of select="$messages" />
      </xsl:when>
      <xsl:when test="starts-with($messages, $code-at-start)">
        <xsl:call-template name="get-first-localized-message">
          <xsl:with-param name="messages" select="$messages" />
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains($messages, $code-in-middle)">
        <xsl:call-template name="get-first-localized-message">
          <xsl:with-param
              name="messages"
              select="concat($code-at-start,
                             substring-after($messages, $code-in-middle))" />
        </xsl:call-template>
      </xsl:when>          
      <xsl:otherwise>   
            
        <!-- Use "en" locale or first message. -->
        <xsl:variable name="en-message">
          <!--
            If the locale is not "en" and we haven't found a message,
            try to shorten locale to language code only and search again.
            If we already were using language code, then try the "en" locale.
            If the locale is "en", there is no point looking (we would have 
            found it above and we might already be recursing).
          -->        
          <xsl:if test="not($locale-code = 'en')">
          <xsl:choose>
             <xsl:when test="contains($locale-code, '_')">
                <xsl:call-template name="get-localized-message">
                  <xsl:with-param name="locale-code" select="$language-code" />
                  <xsl:with-param name="messages" select="$messages" />
                </xsl:call-template>
              </xsl:when>
             <xsl:otherwise>
               <xsl:call-template name="get-localized-message">
                 <xsl:with-param name="messages" select="$messages" />
               </xsl:call-template> 
             </xsl:otherwise>
           </xsl:choose>
           </xsl:if>
         </xsl:variable>

        <xsl:choose>
          <xsl:when test="$en-message
                          and not(normalize-space($en-message) = '')">
            <xsl:value-of select="$en-message" />
          </xsl:when>
          <xsl:otherwise>
            <xsl:call-template name="get-first-localized-message">
              <xsl:with-param name="messages" select="$messages" />
            </xsl:call-template>
          </xsl:otherwise>
        </xsl:choose>
      
      </xsl:otherwise>
      </xsl:choose>
  </xsl:template>

  <!--
    Extracts the first localized message from a string of locale codes and
    messages. The string format is "locale1|message1|locale2|message2|...".
    The templates produces the message that follows the first locale code.
    
    $messages     The string containing the messages.
  -->
  <xsl:template name="get-first-localized-message">
    <xsl:param name="messages" select="''" />

    <xsl:variable name="message-part"
                  select="substring-after($messages, '|')" />

    <xsl:choose>
      <xsl:when test="contains($message-part, '|')">
        <xsl:value-of select="substring-before($message-part, '|')" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$message-part" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!--
    Replaces the characters specified by the chars-in parameter with the 
    charsOut parameter.
    
    $string-in     The string to replace characters for.
    $chars-in      The character sequence to replace.
    $chars-out     The replacement characters.
  -->
  <xsl:template name="replace-chars-in-string">
    <xsl:param name="string-in"/>
    <xsl:param name="chars-in"/>
    <xsl:param name="chars-out"/>

    <xsl:choose>
      <xsl:when test="contains($string-in, $chars-in)">
        <xsl:value-of select="concat(substring-before($string-in, $chars-in),
                                     $chars-out)"/>
        <xsl:call-template name="replace-chars-in-string">
          <xsl:with-param name="string-in"
                          select="substring-after($string-in, $chars-in)"/>
          <xsl:with-param name="chars-in" select="$chars-in"/>
          <xsl:with-param name="chars-out" select="$chars-out"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$string-in"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!--
    Inserts a backslash character before any characters in the string that
    are present in "chars-to-escape". Any backslash characters will be escaped
    even if a backslash is not explicitly defined to be a character to escape.
    
    $string          The string to escape.
    $chars-to-escape The characters to be escaped with a backslash.
  -->
  <xsl:template name="escape-string">
    <xsl:param name="string"/>
    <xsl:param name="chars-to-escape"/>

    <xsl:variable name="first-char" select="substring($string, 1, 1)" />
    <xsl:if test="string-length($first-char) = 1">
      <xsl:if test="contains($chars-to-escape, $first-char)
                    or $first-char = '\'">
        <xsl:text>\</xsl:text>
      </xsl:if>
      <xsl:value-of select="$first-char" />
      <xsl:call-template name="escape-string">
        <xsl:with-param name="string" select="substring($string, 2)" />
        <xsl:with-param name="chars-to-escape" select="$chars-to-escape" />
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
