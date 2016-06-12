<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Mes alertes</title>
</head>

<body>

<div class="row">
    <div class="col-md-12">
        <div class="panel panel-primary">
            <div class="panel-heading">
                <div class="row">
                    <div class="col-md-8">
                        <h1 class="panel-title">
                            <strong>Mes alertes</strong>
                        </h1>
                    </div>

                    <div class="col-md-4 alignRight">
                        <g:link controller="alert" action="new" class="btn btn-success"
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
                                    <div class="pull-right">
                                        <g:link action="refresh" id="${alert.id}" class="btn btn-default"
                                                title="Refresh">
                                            <span class="glyphicon glyphicon-repeat"></span>
                                        </g:link>

                                        <sec:ifAllGranted roles="ROLE_ADMIN">
                                            <g:link action="email" id="${alert.id}" class="btn btn-info"
                                                    title="Email" target="_blank">
                                                <span class="glyphicon glyphicon-envelope"></span>
                                            </g:link>
                                        </sec:ifAllGranted>

                                        <g:link action="edit" id="${alert.id}" class="btn btn-success"
                                                title="Editer">
                                            <span class="glyphicon glyphicon-edit"></span>
                                        </g:link>

                                        <g:link action="confirmDelete" id="${alert.id}" class="btn btn-danger"
                                                title="Supprimer">
                                            <span class="glyphicon glyphicon-trash"></span>
                                        </g:link>
                                    </div>

                                    <p>
                                        <span class="xlarge bold">
                                            ${alert.name}
                                        </span>
                                    </p>

                                    <p>
                                        <g:set var="classifiedCount" value="${alert.classifieds?.size()}"/>

                                        <span class="label label-default">
                                            <g:if test="${classifiedCount > 1}">
                                                ${alert.classifieds?.size()} annonces
                                            </g:if>
                                            <g:elseif test="${classifiedCount == 1}">
                                                ${alert.classifieds?.size()} annonce
                                            </g:elseif>
                                            <g:else>
                                                Aucune annonce
                                            </g:else>
                                        </span>

                                        &nbsp;

                                        <span class="xsmall text-muted">
                                            Dernière vérification :
                                            <g:formatDate date="${alert.lastCheckedDate}"
                                                          format="d MMM à HH:mm"/>
                                        </span>
                                    </p>

                                    <p class="url">
                                        <span class="xsmall text-muted">
                                            ${alert.url}
                                        </span>
                                    </p>
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
</div>
</body>
</html>