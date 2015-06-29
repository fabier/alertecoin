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
    <asset:javascript src="application.js"/>
    <asset:stylesheet href="application.css"/>
    <g:layoutHead/>
    <ga:trackPageview/>
</head>

<body>

<div class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target=".navbar-collapse">
                <span class="sr-only">Basculer la navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <g:link class="navbar-brand" uri="/">
                AlerteCoin
            </g:link>
        </div>

        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li class="${controllerName == null || controllerName == "alert" ? "active" : ""}">
                    <g:link uri="/">
                        <sec:ifLoggedIn>
                            Mes alertes
                        </sec:ifLoggedIn>
                        <sec:ifNotLoggedIn>
                            Accueil
                        </sec:ifNotLoggedIn>
                    </g:link>
                </li>
                <li class="${controllerName == 'common' && actionName == "about" ? "active" : ""}">
                    <g:link controller="common" action="about">
                        A propos
                    </g:link>
                </li>
                <li class="${controllerName == 'common' && actionName == "contact" ? "active" : ""}">
                    <g:link controller="common" action="contact">
                        Contact
                    </g:link>
                </li>
            </ul>

            <sec:ifLoggedIn>
                <g:form controller="logout" action="index" method="POST" class="navbar-form navbar-right" role="form">
                    <span class="label label-success"><sec:username/></span>
                    <button type="submit" class="btn btn-link">
                        <span class="glyphicon glyphicon-log-out paddingright10"></span>
                        Déconnexion
                    </button>
                </g:form>
            </sec:ifLoggedIn>
            <sec:ifNotLoggedIn>
                <g:if test="${hideLoginBlock}">
                </g:if>
                <g:else>
                    <g:form controller="login" action="index" method="GET" class="navbar-form navbar-right" role="form">
                        <button type="submit" class="btn btn-warning">
                            <span class="glyphicon glyphicon-log-in paddingright10"></span>
                            <g:message code="alertecoin.login"/>
                        </button>
                    </g:form>
                </g:else>
            </sec:ifNotLoggedIn>
        </div>
    </div>
</div>

<div class="container">
    <g:layoutBody/>
</div>

<footer>
    <div class="container">
        <sec:ifAllGranted roles="ROLE_ADMIN">
            <div class="pull-left">
            </div>

            <div class="pull-right">
                <g:link controller="admin" action="index" class="btn btn-info">
                    Admin
                </g:link>
            </div>
        </sec:ifAllGranted>

        <div class="btn btn-link block">
            <g:link controller="common" action="contact">
                &copy; AlerteCoin 2014 - <g:getYear/>
            </g:link>
        </div>
    </div>
</footer>

</body>
</html>