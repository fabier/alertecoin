<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Mes alertes</title>
</head>

<body>

<div class="row">
    <div class="col-md-2">
    </div>

    <div class="col-md-8">

        <g:render template="/templates/flashMessage"/>

        <div class="panel panel-primary">
            <div class="panel-heading">
                <div class="row">
                    <div class="col-md-8">
                        <h1 class="panel-title">
                            <strong>Mes alertes</strong>
                        </h1>
                    </div>

                    <div class="col-md-4 alignRight">
                        <g:link controller="alert" action="create" class="btn btn-success"
                                title="Créer une alerte">
                            <span class="glyphicon glyphicon-plus-sign"></span>
                            &nbsp;Créer une alerte
                        </g:link>
                    </div>
                </div>
            </div>

            <div class="panel-body">
                <g:if test="${alerts?.size() > 0}">
                    <table class="table table-condensed table-alerts">
                        <thead>
                        <tr>
                            <th class="col-md-12">Titre</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${alerts}" var="alert">
                            <tr class="clickable-row"
                                data-href="${raw(createLink(controller: "alert", action: "show", id: alert.id))}">
                                <td>
                                    <div class="xlarge">
                                        ${alert.name}
                                    </div>

                                    <div class="url">
                                        <div class="xsmall text-muted">
                                            Dernière vérification :
                                            <g:formatDate date="${alert.lastCheckedDate}"
                                                          format="dd MMMM yyyy - HH:mm"/>
                                            <br/>
                                            ${alert.url}
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </g:if>
                <g:else>
                    <p>
                        Vous n'avez pas d'alerte. Utilisez le bouton "Créer une alerte" pour créer une nouvelle alerte.
                    </p>
                </g:else>
            </div>
        </div>
    </div>

    <div class="col-md-2">
    </div>
</div>
</body>
</html>