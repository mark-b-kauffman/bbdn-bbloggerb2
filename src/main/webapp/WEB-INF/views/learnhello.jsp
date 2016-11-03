<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/bbNG" prefix="bbNG"%>

<bbNG:learningSystemPage ctxId="ctx" title="Hello From Blackboard Learn!" hideCourseMenu="false">
<meta http-equiv="refresh" content="20" />

<%@ page session="false" %>
<html>
<head>
	<title>Hello From Blackboard Learn!</title>
</head>
<body>
<h1>
	Hello from Blackboard Learn!  
</h1>

<P>  The time on the server is ${serverTime}. </P>
</body>
</html>

</bbNG:learningSystemPage>
