package com.wolf.test.win;

import javax.security.auth.callback.*;
import java.io.IOException;

/**
 * Description:
 * Created on 2021/9/3 5:07 PM
 *
 * @author 李超
 * @version 0.0.1
 */
class ProvidedAuthCallback implements CallbackHandler {
    private String username;
    private String password;

    public ProvidedAuthCallback(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        Callback[] var2 = callbacks;
        int var3 = callbacks.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Callback callback = var2[var4];
            if (callback instanceof NameCallback) {
                NameCallback nc = (NameCallback)callback;
                nc.setName(this.username);
            } else {
                if (!(callback instanceof PasswordCallback)) {
                    throw new UnsupportedCallbackException(callback, "Unrecognized Callback");
                }

                PasswordCallback pc = (PasswordCallback)callback;
                pc.setPassword(this.password.toCharArray());
            }
        }

    }
}

