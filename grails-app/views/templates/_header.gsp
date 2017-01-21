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
                <g:img file="logo-text-header.png" class="header-image"/>
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
        </div>
    </div>
</div>