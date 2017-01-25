<%--
  Created by IntelliJ IDEA.
  User: 20141022
  Date: 2016/5/17
  Time: 15:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>login</title>

  </head>
  <body>
      <form action="helloController/testUpload" method="post" enctype="multipart/form-data">
          <input type="file" name="file"><br>
          <input type="submit" value="submit">
      </form>
  </body>
</html>
