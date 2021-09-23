package com.wolf.test.win;

import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.OverthereProcess;
import com.xebialabs.overthere.cifs.CifsProcessConnection;
import com.xebialabs.overthere.spi.AddressPortMapper;

/**
 * Description:
 * Created on 2021/9/7 1:42 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class CifsWinrsConnection  extends CifsProcessConnection {

    public CifsWinrsConnection(String type, ConnectionOptions options, AddressPortMapper mapper) {
        super(type, options, mapper);
    }

    @Override
    public void connect() {
        super.connect();
    }

    @Override
    public void doClose() {
        super.doClose();
    }

    @Override
    public OverthereProcess startProcess(final CmdLine cmd) {
        return super.startProcess(cmd);
    }
}

