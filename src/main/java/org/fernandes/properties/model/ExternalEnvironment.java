/*
 OSSCUBE 2014
 */

package org.fernandes.properties.model;

/**
 * Represents environments from which we are reading variables.
 *
 * @author onepoint
 */
public enum ExternalEnvironment {

    /**
     * The normal environment where you have the path variable, etc.
     */
    ENV,

    /**
     * The Java system environment of the system properties.
     */
    SYS;
}
