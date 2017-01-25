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
      <script src="scripts/jquery-1.7.2.min.js" language="javascript" type="text/javascript"></script>
      <script language="javascript" type="text/javascript" >
          $(function(){
              $("#btn").click(function(){
                  $.post("helloController/testJson",{},function(data){
                      alert(data);
                  });
              });
          });
      </script>
    <title>login</title>

  </head>
  <body>
    <input type="button" value="testjson" id="btn">
  </body>
</html>
