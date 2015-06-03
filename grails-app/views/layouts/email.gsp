<%@ page import="grails.plugin.springsecurity.SpringSecurityUtils" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">
    <title><g:layoutTitle default="AlerteCoin"/></title>
    <asset:javascript src="application.js"/>
    <asset:stylesheet href="application.css"/>
    <g:layoutHead/>
</head>

<body>

<div class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <g:link class="navbar-brand" uri="/">AlerteCoin</g:link>
        </div>
    </div>
</div>

<div class="container">
    <g:layoutBody/>
</div>

<footer class="text-center">
    <g:link controller="common" action="contact">
        &copy; AlerteCoin 2013 - <g:getYear/>
    </g:link>
</footer>

</body>
</html>