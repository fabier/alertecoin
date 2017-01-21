<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Accueil</title>
</head>

<body>
<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="jumbotron">
    <div class="container">
        <h1 class="font-museoslab alertecoin-color text-huge text-center">
            <span style="font-size: 60px;">AlerteCoin</span>
        </h1>

        <p class="text-center padding-top-20">Bienvenue sur le site qui vous permet de suivre les annonces de votre site préféré.</p>

        <p class="text-center">
            <g:link controller="register" action="index" class="btn btn-primary">
                <i class="glyphicon glyphicon-plus-sign"></i>
                Créer un compte
            </g:link>
            <g:link controller="login" action="index" class="btn btn-warning">
                <i class="glyphicon glyphicon-log-in"></i>
                <g:message code="alertecoin.login"/>
            </g:link>
        </p>
    </div>
</div>

<!-- Example row of columns -->
<div class="row">
    <div class="col-md-4">
        <h2>
            <g:link controller="register" action="index" class="label label-success block">
                1. Créer un compte
            </g:link>
        </h2>

        <p>Ca prend 1 minute !</p>
    </div>

    <div class="col-md-4">
        <h2>
            <g:link controller="register" action="index" class="label label-success block">
                2. Créer une alerte
            </g:link>
        </h2>

        <p>C'est facile, il suffit de copier coller une recherche provenant de votre site de petites annonces préféré.</p>
    </div>

    <div class="col-md-4">
        <h2>
            <g:link controller="register" action="index" class="label label-success block">
                3. Recevez des alertes !
            </g:link>
        </h2>

        <p>
            Vous pourrez recevoir des alertes toutes les 10 minutes !
        </p>
    </div>
</div>
</body>
</html>