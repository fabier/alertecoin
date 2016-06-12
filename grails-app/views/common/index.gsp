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
        <h1>Bienvenue !</h1>

        <p>Bienvenue sur le site qui vous permet de suivre les annonces de votre site préféré.</p>

        <p>
            <g:link controller="register" class="btn btn-primary" action="index">
                Créer un compte &raquo;
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
            Vous pourrez recevoir des alertes toutes les 5 minutes !
            <br/>
            On est particulièrement réactifs, vous ne trouverez pas mieux sur internet c'est assuré !
        </p>
    </div>
</div>
</body>
</html>