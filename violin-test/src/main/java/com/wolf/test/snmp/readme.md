一、SNMP4J介绍

SNMP4J是一个用Java来实现SNMP(简单网络管理协议)协议的开源项目.它支持以命令行的形式进行管理与响应。SNMP4J是纯面向对象设计

SNMP4J API 提供以下下特性：
支持MD5和SHA验证，DES，3DES,AES128、AES192和AES256加密的SNMPv3。
支持MPv1,MPv2C和MPv3，带执行的可阻塞的信息处理模块。
全部PDU格式。
可阻塞的传输拓扑。支持UPD、TCP、TLS 。
可阻塞的超时模块。
同步和异步请求。
命令发生器以及命令应答器的支持。
基于Apache license的开源免费。
JAVA 1.4.1或更高版本(2.0或更高版本需要jdk1.6及以上的支持)。
基于LOG4J记录日志。
使用GETBULK实现Row-based的有效的异步表格获取。
支持多线程。


二、SNMP4J重要的类和接口介绍
2.1、Snmp类
负责SNMP报文的接受和发送。它提供了发送和接收PDU的方法，所有的PDU类型都可以采用同步或者异步的方式被发送

2.2、PDU类和ScopedPDU类
是SNMP报文单元的抽象，其中PDU类适用于SNMPv1和SNMPv2c。ScopedPDU类继承于PDU类，适用于SNMPv3。

2.3、Target接口和CommunityTarget类以及UserTarget类
对应于SNMP代理的地址信息，包括IP地址和端口号（161）。其中Target接口适用于SNMPv1和SNMPv2c。CommunityTarget类实现了Target接口，用于SNMPv1和SNMPv2c这两个版本，UserTarget类实现了Target接口，适用于SNMPv3。

2.4、TransportMapping接口
代表了SNMP4J所使用的传输层协议。这也是SNMP4J一大特色的地方。按照RFC的规定，SNMP是只使用UDP作为传输层协议的。而SNMP4J支持管理端和代理端使用UDP或者TCP进行传输。该接口有两个子接口

2.5、Snmp、Target、PDU三者的关系
Target代表远程设备或者远程实体、PDU代表管理端同Target通信的数据，Snmp就代表管理者管理功能（其实就是数据的收发）的具体执行者。


三、SNMP4J的两种消息发送模式
SNMP4J支持两种消息发送模式：同步发送模式和异步发送模式。
同步发送模式也称阻塞模式。当管理端发送出一条消息之后，线程会被阻塞，直到收到对方的回应或者时间超时。同步发送模式编程较为简单，但是不适用于发送广播消息
异步发送模式也称非阻塞模式。当程序发送一条消息之后，线程将会继续执行，当收到消息的回应的时候，程序会对消息作出相应的处理。要实现异步发送模式，需要实例化一个实现了ResponseListener接口的类的对象。ResponseListener接口中有一个名为onResponse的函数。这是一个回调函数，当程序收到响应的时候，会自动调用该函数。由该函数完成对响应的处理。

四、使用SNMP4J实现管理端的步骤
4.1 、初始化
①、明确SNMP在传输层所使用的协议
一般情况下，我们都使用使用UDP协议作为SNMP的传输层协议，所以我们需要实例化的是一个DefaultUdpTransportMapping接口对象；
②、实例化一个snmp对象
需要将1中实例化的DefaultUdpTransportMapping接口的对象作为参数。传snmp类的构造函数中。另外，如果实现的SNMPv3协议，我们还需要设置安全机制，添加安全用户等等；
③、监听snmp消息
可以调用刚刚实例化的DefaultUdpTransportMapping的接口对象的listen方法，让程序监听snmp消息

4.2、 构造发送目标
如果实现的是SNMPv2c或者说SNMPv1，需要实例化一个CommunityTarget对象。如果实现的是SNMPv3程序，则需要实例化一个UserTarget对象
需要对实例化的对象做一些设置。如果是CommunityTarget的对象，则需要设置使用的Snmp版本，重传时间和等待时延。如果是UserTarget对象，我们不仅需要设置版本、重传时间、等待时延，还需要设置安全级别和安全名称

4.3、 构造发送报文
如果发送的是SNMPv2c或者说SNMPv1的报文，我们需要实例化一个PDU类的对象。如果发送的是SNMPv3的报文，我们则需要实例化一个ScopedPDU类的对象。之后，我们还需要生成一个OID对象，其中包含了我们所需要获取的SNMP对象在MIB库中的ID。然后我们需要将OID和之前生成的PDU对象或者是ScopedPDU对象绑定，并且设置PDU的报文类型（五种SNMP报文类型之一）。

4.4、 构造响应监听对象（异步模式）
当使用异步模式的时候，我们需要实例化一个实现了ResponseListener接口的对象，作为响应消息的监听对象。在构造该对象的过程中，我们需要重写ResponseListener的OnResponse函数，该函数是一个回调函数，用来处理程序收到响应后的一些操作。

4.5、 发送消息
同步模式和异步模式发送消息调用的函数名字均为send，但是两个函数所需参数不一样。同步模式的参数仅为4.2和4.3中构造的目标对象和报文对象，而异步模式还需要4.4中构造的监听对象。
同步模式发送消息后便等待响应的到达，到达之后会返回一个ResponseEvent对象，该对象中包含了响应的相应信息。
异步模式发送消息之后便会继续执行，当收到响应消息时便会调用监听对象的OnResponse函数。该函数中的语句便是我们对响应的处理


五、使用SNMP4J实现管理端的编程实现
①、设定远程实体
snmp4j中，用CommunityTarget对象来表示远程实体（要进行snmp消息通信的远程主机，使用snmp的v2版本）
②、指定远程实体的地址
snmp4j中使用Address接口对象来表示，Address对象需要通过实现该接口的类的对象向上转型来实例化
③、通过CommunityTarget以及其父接口中提供的setXX方法来设定远程实体的属性，如设定远程实体的snmp共同体属性、远程实体的地址、超时时间、重传次数、snmp版本等
④、设定使用的传输协议
snmp4j中，用TransportMapping接口的对象来表示传输协议（tcp/udp）
⑤、调用TransportMapping中的listen()方法，启动监听进程，接收消息，由于该监听进程是守护进程，最后应调用close()方法来释放该进程
⑥、创建SNMP对象，用于发送请求PDU
a、创建请求pdu，即创建PDU类的对象，调用PDU类中的add()方法绑定要查询的OID，调用PDU中的setType()方法来确定该pdu的类型（与snmp中五种操作想对应）
b、通过PDU的构造方法  public SNMP(TransportMapping transportingMapping),或者其他构造方法来生成pdu,之后调用 ResopnseEvent send(PDU pdu,Target target)发送pdu，该方法返回一个ResponseEvent对象
⑦、通过ResponseEvent对象来获得SNMP请求的应答pdu，方法：public PDU getResponse()
⑧、通过应答pdu获得mib信息（之前绑定的OID的值），方法：VaribleBinding get（int index）