//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.03 at 06:52:28 PM IST 
//


package curam.ca.gc.bdm.ws.address.gen.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for securityContextType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="securityContextType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TransactionID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Authentication">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Client" type="{}genericEntityType"/>
 *                   &lt;element name="Employee" type="{}genericEntityType"/>
 *                   &lt;element name="Application" type="{}applicationEntityType"/>
 *                   &lt;element name="AssuranceLevel" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Authorization">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="RequestDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="Request" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="TransactionID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="TimeStamp">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="BeginDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="EndDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ProxySettings" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="SecurityContext" type="{}securityContextType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "securityContextType", propOrder = {
    "transactionID",
    "authentication",
    "authorization",
    "proxySettings",
    "securityContext"
})
public class SecurityContextType {

    @XmlElement(name = "TransactionID", required = true)
    protected String transactionID;
    @XmlElement(name = "Authentication", required = true)
    protected SecurityContextType.Authentication authentication;
    @XmlElement(name = "Authorization", required = true)
    protected SecurityContextType.Authorization authorization;
    @XmlElement(name = "ProxySettings")
    protected Object proxySettings;
    @XmlElement(name = "SecurityContext")
    protected SecurityContextType securityContext;

    /**
     * Gets the value of the transactionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionID() {
        return transactionID;
    }

    /**
     * Sets the value of the transactionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionID(String value) {
        this.transactionID = value;
    }

    /**
     * Gets the value of the authentication property.
     * 
     * @return
     *     possible object is
     *     {@link SecurityContextType.Authentication }
     *     
     */
    public SecurityContextType.Authentication getAuthentication() {
        return authentication;
    }

    /**
     * Sets the value of the authentication property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecurityContextType.Authentication }
     *     
     */
    public void setAuthentication(SecurityContextType.Authentication value) {
        this.authentication = value;
    }

    /**
     * Gets the value of the authorization property.
     * 
     * @return
     *     possible object is
     *     {@link SecurityContextType.Authorization }
     *     
     */
    public SecurityContextType.Authorization getAuthorization() {
        return authorization;
    }

    /**
     * Sets the value of the authorization property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecurityContextType.Authorization }
     *     
     */
    public void setAuthorization(SecurityContextType.Authorization value) {
        this.authorization = value;
    }

    /**
     * Gets the value of the proxySettings property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getProxySettings() {
        return proxySettings;
    }

    /**
     * Sets the value of the proxySettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setProxySettings(Object value) {
        this.proxySettings = value;
    }

    /**
     * Gets the value of the securityContext property.
     * 
     * @return
     *     possible object is
     *     {@link SecurityContextType }
     *     
     */
    public SecurityContextType getSecurityContext() {
        return securityContext;
    }

    /**
     * Sets the value of the securityContext property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecurityContextType }
     *     
     */
    public void setSecurityContext(SecurityContextType value) {
        this.securityContext = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Client" type="{}genericEntityType"/>
     *         &lt;element name="Employee" type="{}genericEntityType"/>
     *         &lt;element name="Application" type="{}applicationEntityType"/>
     *         &lt;element name="AssuranceLevel" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "client",
        "employee",
        "application",
        "assuranceLevel"
    })
    public static class Authentication {

        @XmlElement(name = "Client", required = true)
        protected GenericEntityType client;
        @XmlElement(name = "Employee", required = true)
        protected GenericEntityType employee;
        @XmlElement(name = "Application", required = true)
        protected ApplicationEntityType application;
        @XmlElement(name = "AssuranceLevel", required = true)
        protected String assuranceLevel;

        /**
         * Gets the value of the client property.
         * 
         * @return
         *     possible object is
         *     {@link GenericEntityType }
         *     
         */
        public GenericEntityType getClient() {
            return client;
        }

        /**
         * Sets the value of the client property.
         * 
         * @param value
         *     allowed object is
         *     {@link GenericEntityType }
         *     
         */
        public void setClient(GenericEntityType value) {
            this.client = value;
        }

        /**
         * Gets the value of the employee property.
         * 
         * @return
         *     possible object is
         *     {@link GenericEntityType }
         *     
         */
        public GenericEntityType getEmployee() {
            return employee;
        }

        /**
         * Sets the value of the employee property.
         * 
         * @param value
         *     allowed object is
         *     {@link GenericEntityType }
         *     
         */
        public void setEmployee(GenericEntityType value) {
            this.employee = value;
        }

        /**
         * Gets the value of the application property.
         * 
         * @return
         *     possible object is
         *     {@link ApplicationEntityType }
         *     
         */
        public ApplicationEntityType getApplication() {
            return application;
        }

        /**
         * Sets the value of the application property.
         * 
         * @param value
         *     allowed object is
         *     {@link ApplicationEntityType }
         *     
         */
        public void setApplication(ApplicationEntityType value) {
            this.application = value;
        }

        /**
         * Gets the value of the assuranceLevel property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAssuranceLevel() {
            return assuranceLevel;
        }

        /**
         * Sets the value of the assuranceLevel property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAssuranceLevel(String value) {
            this.assuranceLevel = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="RequestDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="Request" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="TransactionID" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="TimeStamp">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="BeginDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="EndDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "requestDescription",
        "request",
        "transactionID",
        "timeStamp"
    })
    public static class Authorization {

        @XmlElement(name = "RequestDescription", required = true)
        protected String requestDescription;
        @XmlElement(name = "Request", required = true)
        protected String request;
        @XmlElement(name = "TransactionID", required = true)
        protected String transactionID;
        @XmlElement(name = "TimeStamp", required = true)
        protected SecurityContextType.Authorization.TimeStamp timeStamp;

        /**
         * Gets the value of the requestDescription property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRequestDescription() {
            return requestDescription;
        }

        /**
         * Sets the value of the requestDescription property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRequestDescription(String value) {
            this.requestDescription = value;
        }

        /**
         * Gets the value of the request property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRequest() {
            return request;
        }

        /**
         * Sets the value of the request property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRequest(String value) {
            this.request = value;
        }

        /**
         * Gets the value of the transactionID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTransactionID() {
            return transactionID;
        }

        /**
         * Sets the value of the transactionID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTransactionID(String value) {
            this.transactionID = value;
        }

        /**
         * Gets the value of the timeStamp property.
         * 
         * @return
         *     possible object is
         *     {@link SecurityContextType.Authorization.TimeStamp }
         *     
         */
        public SecurityContextType.Authorization.TimeStamp getTimeStamp() {
            return timeStamp;
        }

        /**
         * Sets the value of the timeStamp property.
         * 
         * @param value
         *     allowed object is
         *     {@link SecurityContextType.Authorization.TimeStamp }
         *     
         */
        public void setTimeStamp(SecurityContextType.Authorization.TimeStamp value) {
            this.timeStamp = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="BeginDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="EndDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "beginDateTime",
            "endDateTime"
        })
        public static class TimeStamp {

            @XmlElement(name = "BeginDateTime", required = true)
            protected String beginDateTime;
            @XmlElement(name = "EndDateTime", required = true)
            protected String endDateTime;

            /**
             * Gets the value of the beginDateTime property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getBeginDateTime() {
                return beginDateTime;
            }

            /**
             * Sets the value of the beginDateTime property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setBeginDateTime(String value) {
                this.beginDateTime = value;
            }

            /**
             * Gets the value of the endDateTime property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getEndDateTime() {
                return endDateTime;
            }

            /**
             * Sets the value of the endDateTime property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setEndDateTime(String value) {
                this.endDateTime = value;
            }

        }

    }

}
