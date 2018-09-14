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
              $("#btn1").click(function(){
                  $.post("helloController/testAjax",{name:"a1"},function(data){
                      alert(data);
                  });
              });
              $("#btn2").click(function(){
                  var a = {name:"a2",age:1};
                  $.ajax({
                      type: "post",
                      contentType:"application/json;charset=utf-8",
                      url: "helloController/testajaxRequestBody",
                      data: JSON.stringify(a),
                      dataType: "json",
                      success: function(data){
                          if(!data.success){
                              alert("失败");
                          }else{
                              alert(data);
                          }
                      }})
              });
          });
      </script>
    <title>login</title>

  </head>
  <body>
    <input type="button" value="testajax" id="btn1">
    <input type="button" value="testajaxRequestBody" id="btn2">
  </body>
</html>
