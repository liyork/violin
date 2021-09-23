package com.wolf.test.win;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import java.util.HashMap;

/**
 * Description:
 * Created on 2021/9/3 5:08 PM
 *
 * @author 李超
 * @version 0.0.1
 */
class KerberosJaasConfiguration extends Configuration {
    private boolean debug;
    private boolean ticketCache;

    KerberosJaasConfiguration(boolean debug) {
        this.debug = debug;
        this.ticketCache = false;
    }

    KerberosJaasConfiguration(boolean debug, boolean ticketCache) {
        this.debug = debug;
        this.ticketCache = ticketCache;
    }

    public AppConfigurationEntry[] getAppConfigurationEntry(String s) {
        HashMap<String, String> options = new HashMap();
        if (this.debug) {
            options.put("debug", "true");
        }

        options.put("refreshKrb5Config", "true");
        if (JavaVendor.isIBM()) {
            options.put("credsType", "initiator");
            options.put("useDefaultCcache", String.valueOf(this.ticketCache));
        } else {
            options.put("client", "true");
            options.put("useKeyTab", "false");
            options.put("doNotPrompt", "false");
            options.put("useTicketCache", String.valueOf(this.ticketCache));
        }

        return new AppConfigurationEntry[]{new AppConfigurationEntry(JavaVendor.getKrb5LoginModuleName(), AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options)};
    }
}
