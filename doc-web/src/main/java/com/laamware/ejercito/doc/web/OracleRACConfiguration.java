/**
 *
 */
package com.laamware.ejercito.doc.web;

import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Clase requerida para la configuracion de la conexion a la base de datos
 * Oracle RAC. Para mayor informacion consultar la documentacion del proveedor
 * (ver
 * <a href="https://docs.oracle.com/cd/E11882_01/java.112/e12265/toc.htm">Universal
 * Connection Pool for JDBC Developer's Guide</a>)
 * <p>
 * Lista de Propiedades:
 * <ul>
 * <li>user: Usuario para conexion a la base de datos
 * <li>password: Contraseña para conexion a la base de datos
 * <li>url: Cadena de conexion a la base de datos
 * <li>poolName: Nombre del pool de conexiones
 * <li>connectionFactoryClassName: Nombre del Connection Factory para obtener
 * las conexiones
 * <li>initialPoolSize: Numero de conexiones disponibles que se van a crear
 * cuando se inicie el pool
 * <li>maxPoolSize: Numero maximo de conexiones incluyendo las conexiones
 * activas y las disponibles
 * <li>validateConnectionOnBorrow: Validar o no las conexiones que son
 * utilizadas del pool
 * <li>fastConnectionFailoverEnabled: Habilitar el Fast Connection Failover
 * (FCF) para la conexion
 * <li>maxConnectionReuseTime: Tiempo maximo en segundos en el que que cualquier
 * conexion puede ser re-utilizada despues que el pool cierra y elimina la
 * conexion
 * <li>maxConnectionReuseCount: Numero maximo de veces en el que que cualquier
 * conexion puede ser re-utilizada despues que el pool cierra y elimina la
 * conexion
 * <li>abandonedConnectionTimeout: Tiempo maximo en segundos que puede tener una
 * conexion sin uso antes de ser considerada abandonada y sea reclamada por el
 * pool
 * <li>timeToLiveConnectionTimeout: Tiempo maximo en segundos de cualquier
 * conexion puede estar en uso
 * <li>connectionWaitTimeout: Tiempo maximo en segundos que cualquier conexion
 * debe esperar cuando no hay mas conexiones disponibles en el pool
 * <li>inactiveConnectionTimeout: Tiempo maximo en segundos que cualquier
 * conexion puede permanecer en el pool antes de ser cerrada
 * <li>timeoutCheckInterval: Tiempo en segundos para revisar/aplicar las
 * propiedades de timeout: InactiveConnectionTimeout,
 * AbandonedConnectionTimeout, TimeToLiveConnectionTimeout
 * </ul>
 *
 * @author David Alvarez
 * @version 11/11/2016
 *
 */
@ConfigurationProperties("datasource.ejedoc")
@Component
public class OracleRACConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(OracleRACConfiguration.class);

    @NotNull
    private String user;

    @NotNull
    private String password;

    @NotNull
    private String url;

    private String poolName;

    private String connectionFactoryClassName;

    private int initialPoolSize;

    private int minPoolSize;
    
    private int maxPoolSize;

    private Boolean validateConnectionOnBorrow;

    private Boolean fastConnectionFailoverEnabled;

    private int maxConnectionReuseTime;

    private int maxConnectionReuseCount;

    private int abandonedConnectionTimeout;

    private int timeToLiveConnectionTimeout;

    private int connectionWaitTimeout;

    private int inactiveConnectionTimeout;

    private int timeoutCheckInterval;

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the poolName
     */
    public String getPoolName() {
        return poolName;
    }

    /**
     * @param poolName the poolName to set
     */
    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    /**
     * @return the connectionFactoryClassName
     */
    public String getConnectionFactoryClassName() {
        return connectionFactoryClassName;
    }

    /**
     * @param connectionFactoryClassName the connectionFactoryClassName to set
     */
    public void setConnectionFactoryClassName(String connectionFactoryClassName) {
        this.connectionFactoryClassName = connectionFactoryClassName;
    }

    /**
     * @return the initialPoolSize
     */
    public int getInitialPoolSize() {
        return initialPoolSize;
    }

    /**
     * @param initialPoolSize the initialPoolSize to set
     */
    public void setInitialPoolSize(int initialPoolSize) {
        this.initialPoolSize = initialPoolSize;
    }

    /**
     * @return the minPoolSize
     */
    public int getMinPoolSize() {
        return minPoolSize;
    }

    /**
     * @param minPoolSize the minPoolSize to set
     */
    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }
    
    /**
     * @return the maxPoolSize
     */
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    /**
     * @param maxPoolSize the maxPoolSize to set
     */
    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    /**
     * @return the validateConnectionOnBorrow
     */
    public Boolean getValidateConnectionOnBorrow() {
        return validateConnectionOnBorrow;
    }

    /**
     * @param validateConnectionOnBorrow the validateConnectionOnBorrow to set
     */
    public void setValidateConnectionOnBorrow(Boolean validateConnectionOnBorrow) {
        this.validateConnectionOnBorrow = validateConnectionOnBorrow;
    }

    /**
     * @return the fastConnectionFailoverEnabled
     */
    public Boolean getFastConnectionFailoverEnabled() {
        return fastConnectionFailoverEnabled;
    }

    /**
     * @param fastConnectionFailoverEnabled the fastConnectionFailoverEnabled to
     * set
     */
    public void setFastConnectionFailoverEnabled(Boolean fastConnectionFailoverEnabled) {
        this.fastConnectionFailoverEnabled = fastConnectionFailoverEnabled;
    }

    /**
     * @return the maxConnectionReuseTime
     */
    public int getMaxConnectionReuseTime() {
        return maxConnectionReuseTime;
    }

    /**
     * @param maxConnectionReuseTime the maxConnectionReuseTime to set
     */
    public void setMaxConnectionReuseTime(int maxConnectionReuseTime) {
        this.maxConnectionReuseTime = maxConnectionReuseTime;
    }

    /**
     * @return the maxConnectionReuseCount
     */
    public int getMaxConnectionReuseCount() {
        return maxConnectionReuseCount;
    }

    /**
     * @param maxConnectionReuseCount the maxConnectionReuseCount to set
     */
    public void setMaxConnectionReuseCount(int maxConnectionReuseCount) {
        this.maxConnectionReuseCount = maxConnectionReuseCount;
    }

    /**
     * @return the abandonedConnectionTimeout
     */
    public int getAbandonedConnectionTimeout() {
        return abandonedConnectionTimeout;
    }

    /**
     * @param abandonedConnectionTimeout the abandonedConnectionTimeout to set
     */
    public void setAbandonedConnectionTimeout(int abandonedConnectionTimeout) {
        this.abandonedConnectionTimeout = abandonedConnectionTimeout;
    }

    /**
     * @return the timeToLiveConnectionTimeout
     */
    public int getTimeToLiveConnectionTimeout() {
        return timeToLiveConnectionTimeout;
    }

    /**
     * @param timeToLiveConnectionTimeout the timeToLiveConnectionTimeout to set
     */
    public void setTimeToLiveConnectionTimeout(int timeToLiveConnectionTimeout) {
        this.timeToLiveConnectionTimeout = timeToLiveConnectionTimeout;
    }

    /**
     * @return the connectionWaitTimeout
     */
    public int getConnectionWaitTimeout() {
        return connectionWaitTimeout;
    }

    /**
     * @param connectionWaitTimeout the connectionWaitTimeout to set
     */
    public void setConnectionWaitTimeout(int connectionWaitTimeout) {
        this.connectionWaitTimeout = connectionWaitTimeout;
    }

    /**
     * @return the inactiveConnectionTimeout
     */
    public int getInactiveConnectionTimeout() {
        return inactiveConnectionTimeout;
    }

    /**
     * @param inactiveConnectionTimeout the inactiveConnectionTimeout to set
     */
    public void setInactiveConnectionTimeout(int inactiveConnectionTimeout) {
        this.inactiveConnectionTimeout = inactiveConnectionTimeout;
    }

    /**
     * @return the timeoutCheckInterval
     */
    public int getTimeoutCheckInterval() {
        return timeoutCheckInterval;
    }

    /**
     * @param timeoutCheckInterval the timeoutCheckInterval to set
     */
    public void setTimeoutCheckInterval(int timeoutCheckInterval) {
        this.timeoutCheckInterval = timeoutCheckInterval;
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OracleRACConfiguration [user=");
        builder.append(user);
        builder.append(", password=");
        builder.append(password);
        builder.append(", url=");
        builder.append(url);
        builder.append(", poolName=");
        builder.append(poolName);
        builder.append(", connectionFactoryClassName=");
        builder.append(connectionFactoryClassName);
        builder.append(", initialPoolSize=");
        builder.append(initialPoolSize);
        builder.append(", minPoolSize=");
        builder.append(minPoolSize);
        builder.append(", maxPoolSize=");
        builder.append(maxPoolSize);
        builder.append(", validateConnectionOnBorrow=");
        builder.append(validateConnectionOnBorrow);
        builder.append(", fastConnectionFailoverEnabled=");
        builder.append(fastConnectionFailoverEnabled);
        builder.append(", maxConnectionReuseTime=");
        builder.append(maxConnectionReuseTime);
        builder.append(", maxConnectionReuseCount=");
        builder.append(maxConnectionReuseCount);
        builder.append(", abandonedConnectionTimeout=");
        builder.append(abandonedConnectionTimeout);
        builder.append(", timeToLiveConnectionTimeout=");
        builder.append(timeToLiveConnectionTimeout);
        builder.append(", connectionWaitTimeout=");
        builder.append(connectionWaitTimeout);
        builder.append(", inactiveConnectionTimeout=");
        builder.append(inactiveConnectionTimeout);
        builder.append(", timeoutCheckInterval=");
        builder.append(timeoutCheckInterval);
        builder.append("]");
        return builder.toString();
    }

}
