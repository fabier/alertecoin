<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Admin</title>
</head>

<body>

<div class="row">
    <div class="col-md-12">

        <g:render template="/templates/flashMessage"/>

        <div class="row">
            <div class="col-md-6">
                <g:link controller="user" action="index" class="btn btn-lg btn-primary block">
                    Voir la liste des utilisateurs
                </g:link>
            </div>

            <div class="col-md-6">

            </div>
        </div>
    </div>
</div>
</body>
</html>