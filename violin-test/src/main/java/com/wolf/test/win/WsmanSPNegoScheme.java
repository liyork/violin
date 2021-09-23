package com.wolf.test.win;

import org.apache.http.auth.Credentials;
import org.apache.http.auth.KerberosCredentials;
import org.apache.http.impl.auth.SPNegoScheme;
import org.ietf.jgss.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * Created on 2021/9/3 5:13 PM
 *
 * @author 李超
 * @version 0.0.1
 */
class WsmanSPNegoScheme extends SPNegoScheme {
    private final String spnServiceClass;
    private final String spnAddress;
    private final int spnPort;
    private static final Logger logger = LoggerFactory.getLogger(WsmanSPNegoScheme.class);

    public WsmanSPNegoScheme(boolean stripPort, String spnServiceClass, String spnAddress, int spnPort) {
        this(stripPort, spnServiceClass, spnAddress, spnPort, false);
    }

    public WsmanSPNegoScheme(boolean stripPort, String spnServiceClass, String spnAddress, int spnPort, boolean useCanonicalHostname) {
        super(stripPort, useCanonicalHostname);
        this.spnServiceClass = spnServiceClass;
        this.spnAddress = spnAddress;
        this.spnPort = spnPort;
    }

    protected byte[] generateGSSToken(byte[] input, Oid oid, String authServer) throws GSSException {
        logger.trace("WsmanSPNegoScheme.generateGSSToken invoked for authServer = {} without credentials", authServer);
        return this.doGenerateGSSToken(input, oid, authServer, (Credentials)null);
    }

    protected byte[] generateGSSToken(byte[] input, Oid oid, String authServer, Credentials credentials) throws GSSException {
        logger.trace("WsmanSPNegoScheme.generateGSSToken invoked for authServer = {} with credentials", authServer);
        return this.doGenerateGSSToken(input, oid, authServer, credentials);
    }

    private byte[] doGenerateGSSToken(byte[] input, Oid oid, String authServer, Credentials credentials) throws GSSException {
        byte[] token = input;
        if (input == null) {
            token = new byte[0];
        }

        String gssAuthServer;
        if (authServer.equals("localhost")) {
            if (authServer.indexOf(58) > 0) {
                gssAuthServer = this.spnAddress + ":" + this.spnPort;
            } else {
                gssAuthServer = this.spnAddress;
            }
        } else {
            gssAuthServer = authServer;
        }

        String spn = this.spnServiceClass + "@" + gssAuthServer;
        GSSCredential gssCredential;
        if (credentials instanceof KerberosCredentials) {
            gssCredential = ((KerberosCredentials)credentials).getGSSCredential();
        } else {
            gssCredential = null;
        }

        logger.debug("Canonicalizing SPN {}", spn);
        GSSManager manager = this.getManager();
        GSSName serverName = manager.createName(spn, GSSName.NT_HOSTBASED_SERVICE);
        GSSName canonicalizedName = serverName.canonicalize(oid);
        logger.debug("Requesting SPNego ticket for canonicalized SPN {}", canonicalizedName);
        GSSContext gssContext = manager.createContext(canonicalizedName, oid, gssCredential, JavaVendor.getSpnegoLifetime());
        gssContext.requestMutualAuth(true);
        gssContext.requestCredDeleg(true);
        return gssContext.initSecContext(token, 0, token.length);
    }
}

