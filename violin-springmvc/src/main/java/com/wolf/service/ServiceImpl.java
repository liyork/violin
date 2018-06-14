package com.wolf.service;

import com.wolf.companytcp.server.AbstractRemoteService;
import org.springframework.stereotype.Component;

/**
 * Description:
 * <br/> Created on 2017/4/6 10:03
 *
 * @author 李超
 * @since 1.0.0
 */
@Component
public class ServiceImpl extends AbstractRemoteService {

    public ServiceImpl() {
        System.out.println("xxxxxx");
    }

    public String test(String xxx) {
        return "1111test...." + xxx;
    }
}
