
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:h="http://java.sun.com/jsf/html" xmlns:f="http://java.sun.com/jsf/core" xmlns:tds="http://airast.org/jsfcustom"
      xmlns:p="http://java.sun.com/jsf/passthrough">
<h:head>
    <title>Content</title>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Shared/CSS/items.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Shared/CSS/elpa.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Shared/CSS/accommodations.css">

    <style type="text/css">
        /* Hide all position #'s on this frame */
        .bigTable .questionCell h2 {
            display: none;
        }
    </style>

</h:head>
<h:body >
    <h:form id="aspnetForm">
        <div id="passageContent">
            ${content}
        </div>
    </h:form>
</h:body>
</html>
