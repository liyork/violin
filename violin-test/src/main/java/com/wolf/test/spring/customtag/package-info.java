package com.wolf.test.spring.customtag;

//自定义标签如下的思考：
//       1、自定义标签可以做到封装
//            把真正用户需要关心的东西提供出来，把用户不需要关心的内容隐示的实现掉。
//       2、自定义标签可以做到标签检查
//            对用户填写信息的控制做到一定的检查，对中间组件的配置信息有一定的规范可以帮助开发人员正常的使用，避免出现问题时难以定位问题所在。
//       3、自定义标签可以在标签处理时，做任何你想“预做”的事情，如一个数据库连接，可以尝试的该实例在创建时便尝试连接数据库看是否正常。

//XML Schema Definition(XSD)  对于XSD，简单的说是xml的一个标签的定义

// springtag.xsd及spring.schemas是为标签使用而定义的
// spring.handlers是为了让spring调用handler解析xml

//自定义标签的目的是为了更好的方便我们的开发，对一些繁琐而又固定的东西，封装节省劳动力

//标记（命名空间+元素名称）

//小结：
//要自定义Spring的配置标签，需要一下几个步骤：
//**使用XSD定义XML配置中标签元素的结构(myns.XSD)
//**提供该XSD命名空间的处理类，它可以处理多个标签定义(MyNamespaceHandler.java)
//**为每个标签元素的定义提供解析类。(SimpleDateFormatBeanDefinitionParser.java)
//**两个特殊文件通知Spring使用自定义标签元素（spring.handlers 和spring.schemas)