package com.wolf.test.win;

import org.apache.http.auth.AuthScheme;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * Created on 2021/9/3 5:13 PM
 *
 * @author 李超
 * @version 0.0.1
 */
class WsmanSPNegoSchemeFactory extends SPNegoSchemeFactory {
    private final String spnServiceClass;
    private final String spnHost;
    private final int spnPort;
    private final boolean useCanonicalHostname;
    private Logger logger;

    public WsmanSPNegoSchemeFactory(boolean stripPort, String spnServiceClass, String spnHost, int spnPort) {
        this(stripPort, spnServiceClass, spnHost, spnPort, false);
    }

    public WsmanSPNegoSchemeFactory(boolean stripPort, String spnServiceClass, String spnHost, int spnPort, boolean useCanonicalHostname) {
        super(stripPort, useCanonicalHostname);
        this.logger = LoggerFactory.getLogger(WsmanSPNegoSchemeFactory.class);
        this.spnServiceClass = spnServiceClass;
        this.spnHost = spnHost;
        this.spnPort = spnPort;
        this.useCanonicalHostname = useCanonicalHostname;
    }

    public AuthScheme newInstance(HttpParams params) {
        this.logger.trace("WsmanSPNegoSchemeFactory.newInstance invoked for SPN {}/{} (spnPort = {}, stripPort = {})", new Object[]{this.spnServiceClass, this.spnHost, this.spnPort, this.isStripPort()});
        return new WsmanSPNegoScheme(this.isStripPort(), this.spnServiceClass, this.spnHost, this.spnPort, this.useCanonicalHostname);
    }

    public AuthScheme create(HttpContext context) {
        this.logger.trace("WsmanSPNegoSchemeFactory.create invoked for SPN {}/{} (spnPort = {}, stripPort = {})", new Object[]{this.spnServiceClass, this.spnHost, this.spnPort, this.isStripPort()});
        return new WsmanSPNegoScheme(this.isStripPort(), this.spnServiceClass, this.spnHost, this.spnPort, this.useCanonicalHostname);
    }
}
