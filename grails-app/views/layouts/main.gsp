<%@ page import="grails.plugin.springsecurity.SpringSecurityUtils" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">
    <title>AlerteCoin - <g:layoutTitle default="Accueil"/></title>
    %{--<r:require module="application"/>--}%
    <asset:stylesheet src="application"/>
    <asset:javascript src="application"/>
    <g:layoutHead/>
    <ga:trackPageview/>
</head>

<body>

<g:render template="/templates/header"/>

<div class="container">
    <g:render template="/templates/flashMessage"/>
    <g:layoutBody/>
</div>

<g:render template="/templates/footer"/>

</body>
</html>