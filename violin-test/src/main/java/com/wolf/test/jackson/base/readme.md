Jackson常用注解

1. @JacksonXmlRootElement

+ namespace属性：用于指定XML根元素命名空间的名称。
+ localname属性：用于指定XML根元素节点标签的名称,默认是首字母大写。

2. @JacksonXmlProperty

+ namespace和localname属性用于指定XML命名空间的名称
+ isAttribute指定该属性作为XML的属性还是作为子标签

3. @JacksonXmlText

+ 将属性直接作为未被标签包裹的普通文本表现

4. @JacksonXmlCData

+ 将属性包裹在CDATA标签中
+ 所有 XML 文档中的文本均会被解析器解析。 只有 CDATA 区段（CDATA section）中的文本会被解析器忽略。CDATA 指的是不应由 XML 解析器进行解析的文本数据（Unparsed Character Data

5. @JsonPropertyOrder

+ 和@JsonProperty的index属性类似，指定属性序列化时的顺序

6. @JsonIgnore

+ 用于排除某个属性，这样该属性就不会被Jackson序列化和反序列化

7. @JsonIgnoreProperties

+ 注解是类注解。在序列化为JSON的时候，@JsonIgnoreProperties({“prop1”, “prop2”})会忽略pro1和pro2两个属性。
+ 在从JSON反序列化为Java类的时候，@JsonIgnoreProperties(ignoreUnknown=true)会忽略所有没有Getter和Setter的属性。该注解在Java类和JSON不完全匹配的时候很有用

8. 设置集合外围标签名@JacksonXmlElementWrapper

+ 可以将列表数据转为XML子节点
   
