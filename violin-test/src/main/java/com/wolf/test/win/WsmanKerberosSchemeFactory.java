package com.wolf.test.win;

import org.apache.http.auth.AuthScheme;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * Created on 2021/9/3 5:10 PM
 *
 * @author 李超
 * @version 0.0.1
 */
class WsmanKerberosSchemeFactory extends KerberosSchemeFactory {
    private final String spnServiceClass;
    private final String spnHost;
    private final int spnPort;
    private final boolean useCanonicalHostname;
    private Logger logger;

    public WsmanKerberosSchemeFactory(boolean stripPort, String spnServiceClass, String spnHost, int spnPort) {
        this(stripPort, spnServiceClass, spnHost, spnPort, false);
    }

    public WsmanKerberosSchemeFactory(boolean stripPort, String spnServiceClass, String spnHost, int spnPort, boolean useCanonicalHostname) {
        super(stripPort, useCanonicalHostname);
        this.logger = LoggerFactory.getLogger(WsmanKerberosSchemeFactory.class);
        this.spnServiceClass = spnServiceClass;
        this.spnHost = spnHost;
        this.spnPort = spnPort;
        this.useCanonicalHostname = useCanonicalHostname;
    }

    public AuthScheme newInstance(HttpParams params) {
        this.logger.trace("WsmanKerberosSchemeFactory.newInstance invoked for SPN {}/{} (spnPort = {}, stripPort = {})", new Object[]{this.spnServiceClass, this.spnHost, this.spnPort, this.isStripPort()});
        return new WsmanKerberosScheme(this.isStripPort(), this.spnServiceClass, this.spnHost, this.spnPort);
    }

    public AuthScheme create(HttpContext context) {
        this.logger.trace("WsmanKerberosSchemeFactory.create invoked for SPN {}/{} (spnPort = {}, stripPort = {})", new Object[]{this.spnServiceClass, this.spnHost, this.spnPort, this.isStripPort()});
        return new WsmanKerberosScheme(this.isStripPort(), this.spnServiceClass, this.spnHost, this.spnPort);
    }
}


