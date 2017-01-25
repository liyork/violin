<%--
  Created by IntelliJ IDEA.
  User: 20141022
  Date: 2016/5/17
  Time: 15:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<html>
  <head>
    <title>121212</title>中文
  </head>
  <body>
    <form action="${pageContext.request.contextPath}/xx" method="post" enctype="application/x-www-form-urlencoded">
      <input type="text" name="username"/>
      <input type="submit" value="提交">
    </form>

    <%--超链接中带有中文的参数 在IE中 是自动转换的，但是在火狐中是正常显示的！！！--%>
    <a href="${pageContext.request.contextPath}/xx?username=中">一样的方式1</a>
    <a href="${pageContext.request.contextPath}/xx?username=<%=java.net.URLEncoder.encode("中","utf-8") %>">一样的方式2</a>
  </body>
</html>
