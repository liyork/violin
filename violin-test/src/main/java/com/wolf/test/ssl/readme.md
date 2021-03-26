Java实现SSL双向认证的方法

一. 常见的SSL验证较多的只是在客户端验证服务器是否是真实正确的。SSL单向认证
实际中，有可能还会在服务端验证客户端是否符合要求，也就是给我们每个用户颁发一个证书，并且每个数字证书都是唯一的，不公开的。这样就能通过这个数字证书保证当前访问我服务器的这个用户是经过服务器认可的，其他人不可访问
这样就确保了服务器与客户端都是互相认可的。 他们之间要进行通信，就会在通信协议上附加SSL协议，确保通信的内容是加密的

二. 场景： Server端和Client端通信，需要进行授权和身份的验证，即Client只能接受Server的消息，Server只能接受Client的消息。

三. 实现技术： JSSE（Java Security Socket Extension） 是Sun为了解决在Internet上的安全通讯而推出的解决方案。 它实现了SSL和TSL（传输层安全）协议。
在JSSE中包含了数据加密，服务器验证，消息完整性和客户端验证等技术。 通过使用JSSE，开发人员可以在客户机和服务器之间通过TCP/IP协议安全地传输数据

四. 推荐使用Java自带的keytool命令，去生成这样信息文件。
目前非常流行的开源的生成SSL证书的还有OpenSSL。OpenSSL用C语言编写，跨系统。但是考虑以后的过程中用java程序生成证书的方便性考虑，还是用JDK自带的keytool

五. 服务端:
1）KeyStore: 其中保存服务端的私钥 2）Trust KeyStore:其中保存客户端的授权证书

1. 服务端执行：生成服务端私钥(生成kserver.keystore文件,保存着自己的私钥)
   keytool -genkey -alias serverkey -keystore kserver.keystore
2. 服务端执行：根据私钥，导出服务端证书(server.crt是产生的服务端的证书)
   keytool -export -alias serverkey -keystore kserver.keystore -file server.crt
3. 客户端执行：将服务端证书，导入到客户端的Trust KeyStore中(tclient.keystore产生的文件其中保存着服务端的受信任的证书)
   keytool -import -alias serverkey -file server.crt -keystore tclient.keystore

五. 客户端:
1）KeyStore：其中保存客户端的私钥 2）Trust KeyStore：其中保存服务端的授权证书

1. keytool -genkey -alias clientkey -keystore kclient.keystore
2. keytool -export -alias clientkey -keystore kclient.keystore -file client.crt
3. keytool -import -alias clientkey -file client.crt -keystore tserver.keystore