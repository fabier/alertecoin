<head>
    <meta name='layout' content='main'/>
    <title><g:message code="springSecurity.denied.title"/></title>
</head>

<body>
<div class='body'>
    <div class="col-md-2"></div>

    <div class="col-md-8">

        <g:render template="/templates/flashMessage"/>

        <div class="panel panel-danger">
            <div class="panel-heading">
                <h3 class="panel-title">
                    Erreur
                </h3>
            </div>

            <div class="panel-body">
                <div class='errors'><g:message code="springSecurity.denied.message"/></div>
            </div>
        </div>
    </div>

    <div class="col-md-2"></div>
</div>
</body>
