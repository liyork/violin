4.3.6
 * 一、为什么要用Http连接池
 *
 * 1、降低延迟：如果不采用连接池，每次连接发起Http请求的时候都会重新建立TCP连接(经历3次握手)，用完就会关闭连接(4次挥手)，如果采用连接池则减少了这部分时间损耗，别小看这几次握手，本人经过测试发现，基本上3倍的时间延迟
 *
 * 2、支持更大的并发：如果不采用连接池，每次连接都会打开一个端口，在大并发的情况下系统的端口资源很快就会被用完，导致无法建立新的连接
 * 仅仅使用response.close();结果就是连接会被关闭，并且不能被复用，这样就失去了采用连接池的意义。
 * 创建连接和销毁连接都是不小的开销
 *
 * PoolingClientConnectionManager
 * 默认情况下，每个router最多维护两个并发线程的connection连接，整个pool最多容纳20个并发的connections。当然可以通过设置来修改这些限制。
 
HttpGet必须releaseconnection，但不是abort。因为releaseconnection是归还连接到连接池，而abort是直接抛弃这个连接，而且占用连接池的数目。

httpclient连接池底层通过tcp的连接不释放发送多个http，减少创建和连接的开销。
由客户端发起Keep-Alive
httpclient判断连接是否可以保持？通过Conent-Length、Transfer-Encoding
很难保证tcp失效。因为是池化管理，没有实时侦测有效性。ClientConnectionManager提供了 closeExpiredConnections和closeIdleConnections两个方法。

httpclient是一个线程安全的类，没有必要由每个线程在每次使用时创建，全局保留一个即可。
tcp的三次握手与四次挥手两大裹脚布过程，对于高频次的请求来说，消耗实在太大。改成keep alive方式以实现连接复用！
String response = EntityUtils.toString(entity);
相当于额外复制了一份content到一个字符串response里，而原本的httpResponse仍然保留了一份content，需要被consume掉，
在高并发且content非常大的情况下，会消耗大量内存。

使用setStaleConnectionCheckEnabled方法来逐出已被关闭的链接(不被推荐)。因为在执行http前检查连接。
更好的方式是手动启用一个线程，定时运行closeExpiredConnections 和closeIdleConnections方法，如下所示。

关闭连接？关闭流？
entity.isStreaming应该不影响连接的继续处理。
Ensures that the entity content is fully consumed and the content stream, if exists, is closed.
关闭内容流HttpEntity#getContent()和关闭response之间的区别是，前者将尝试通过占用实体内容来保持底层连接，而后者会立即关闭并丢弃连接。
主要是前者并没有真正关闭，只不过是个代理对象。底层其实是对连接释放到连接池，而后者是真正关闭底层连接。

connectTimeout
连接超时时间是发起请求前建立连接的等待时间；
SocketTimeout
socket超时时间是等待数据的超时时间，即读取超时时间

Http协议中只有Post,Put和Patch三种方法支持实体内容的发送。

路由(HttpRoute)的概念，我们可以理解成一条连接的路线。如果多个线程访问的是同一个IP+端口，我们就认为他们实际上使用的是同一个HttpRoute。

HTTP连接不是一个线程安全的对象，因此一个HTTP连接一次只能由一个线程使用。HttpClient使用HttpClientConnectionManager接口来管理Http连接，它管理Http连接的生命周期和线程安全，保证一个连接至多有一个线程访问。

还是得看下httpclient的源码才知道内部原理！才知道怎么释放的。
CloseableHttpClient.execute时，触发EntityUtils.consume。这个entity是ResponseEntityProxy。
instream.close时触发ResponseEntityProxy的releaseConnection进而触发HttpClientConnectionManager的releaseConnection
将pool.release将leased移除。
看来只看代码，看不出来里面是什么意思。。。都是调用close。。

httpRequest.releaseConnection();内部会调用到ConnectionHolder的abortConnection，但是released已经在releaseConnection中被释放了。
所以不会managedConn.shutdown。这个释放就是上面说的EntityUtils.consume
所以就不需要再finnaly中使用httpRequest.releaseConnection();


安全机制稍后需要再研究
https://www.yeetrack.com/?p=782