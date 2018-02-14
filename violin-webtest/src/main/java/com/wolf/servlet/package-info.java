package com.wolf.servlet;

//底层走的还是流，http请求到来时被tomcat封装成httpservletrequest和response，操作后，不论用forward或者output的write
//给浏览器的都是流，然后浏览器查看流解码，然后封装成response展示在页面中
//HTTP/1.1 200 OK
//        Server: Apache-Coyote/1.1
//        Set-Cookie: sessionId=123456789
//        Content-Length: 0
//        Date: Tue, 13 Feb 2018 08:28:12 GMT