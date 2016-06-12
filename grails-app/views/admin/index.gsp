<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Admin</title>
</head>

<body>
<div class="row">
    <div class="col-md-12">
        <div class="row">

            <div class="col-md-2">
            </div>

            <div class="col-md-4">
                <g:link controller="admin" action="users" class="btn btn-lg btn-primary block">
                    <i class="glyphicon glyphicon-user"></i>
                    Voir la liste des utilisateurs
                </g:link>
            </div>

            <div class="col-md-4">
                <g:link controller="admin" action="allAlerts" class="btn btn-lg btn-primary block">
                    <i class="glyphicon glyphicon-bell"></i>
                    Voir toutes les alertes
                </g:link>
            </div>

            <div class="col-md-2">
            </div>
        </div>
    </div>
</div>
</body>
</html>