<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <script type="text/javascript">
        i = 1;
        j = 1;
        $(document).ready(function () {

            $("#btn_add1").click(function () {
                document.getElementById("newUpload1").innerHTML += '<div id="div_' + i + '"><input  name="file" type="file"  /><input type="button" value="删除"  onclick="del_1(' + i + ')"/></div>';
                i = i + 1;
            });

            $("#btn_add2").click(function () {
                document.getElementById("newUpload2").innerHTML += '<div id="div_' + j + '"><input  name="file_' + j + '" type="file"  /><input type="button" value="删除"  onclick="del_2(' + j + ')"/></div>';
                j = j + 1;
            });

            $("#btn_add3").click(function () {
                document.getElementById("newUpload3").innerHTML += '<div id="div_' + i + '"><input  name="file" type="file"  /><input type="button" value="删除"  onclick="del_1(' + i + ')"/></div>';
                i = i + 1;
            });
        });

        function del_1(o) {
            document.getElementById("newUpload1").removeChild(document.getElementById("div_" + o));
        }

        function del_2(o) {
            document.getElementById("newUpload2").removeChild(document.getElementById("div_" + o));
        }

    </script>
</head>
<body>

<h1>springMVC字节流输入上传文件</h1>
<form name="userForm1" action="${pageContext.request.contextPath}/file/upload1" enctype="multipart/form-data"
      method="post">
    <input type="text" name="xx" value="xx1">
    <div id="newUpload1">
        <input type="file" name="file">
    </div>

    <input type="button" id="btn_add1" value="增加一行">
    <input type="submit" value="上传"></form>
<br/>
<br/>
<hr align="left" width="60%" color="#FF0000" size="3">
<br/>
<br/>
<h1>springMVC包装类上传文件,name不一样</h1>
<form name="userForm2" action="${pageContext.request.contextPath}/file/upload2" enctype="multipart/form-data"
      method="post">
    <div id="newUpload2">
        <input type="file" name="file">
    </div>
    <input type="button" id="btn_add2" value="增加一行">
    <input type="submit" value="上传"></form>
<br/>
<br/>
<h1>springMVC包装类上传文件,name都一样</h1>
<form name="userForm3" action="${pageContext.request.contextPath}/file/upload3" enctype="multipart/form-data"
      method="post">
    <div id="newUpload3">
        <input type="file" name="file">
    </div>
    <input type="button" id="btn_add3" value="增加一行">
    <input type="submit" value="上传"></form>
</body>
</html>