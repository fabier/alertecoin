<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Alerte</title>
</head>

<body>
<div class="row">

    <div class="col-md-2"></div>

    <div class="col-md-8">

        <g:render template="/templates/flashMessage"/>

        <div class="panel panel-primary">
            <div class="panel-heading">
                <div class="row">
                    <div class="col-md-8">
                        <h3 class="panel-title">
                            <strong>Alerte : ${alert.name}</strong>
                        </h3>
                        <g:link url="${alert.url}" target="_blank">
                            <small class="text-white">
                                ${alert.url}
                            </small>
                        </g:link>
                    </div>

                    <div class="col-md-4 alignRight">
                        <g:link action="refresh" id="${alert.id}" class="btn btn-default"
                                title="Refresh">
                            <span class="glyphicon glyphicon-repeat"></span>
                        </g:link>

                        <g:link action="edit" id="${alert.id}" class="btn btn-success"
                                title="Editer">
                            <span class="glyphicon glyphicon-edit"></span>
                        </g:link>

                        <g:link action="confirmDelete" id="${alert.id}" class="btn btn-danger"
                                title="Supprimer">
                            <span class="glyphicon glyphicon-trash"></span>
                        </g:link>
                    </div>
                </div>
            </div>

            <div class="panel-body">

                <table class="table table-condensed table-classifieds">
                    <thead>
                    <tr>
                        <th class="image">Image</th>
                        <th class="title">Titre</th>
                        <th class="price">Prix</th>
                        <th class="date">Date</th>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${classifieds}" var="classified">
                        <tr>
                            <td class="text-center">
                                <g:link url="${classified.url}" target="_blank">
                                    <g:if test="${classified.images}">
                                        <img src="${classified.images.first().url}" class="image-pola"/>
                                    </g:if>
                                    <g:else>
                                        <img src="holder.js/120x90/text:Pas de visuel" class="image-pola"/>
                                    </g:else>
                                </g:link>
                            </td>
                            <td>
                                <g:link url="${classified.url}" target="_blank">
                                    ${classified.name}
                                    <g:if test="${classified.description}">
                                        <p class="text-muted">
                                            <small>${classified.description}</small>
                                        </p>
                                    </g:if>
                                </g:link>
                            </td>
                            <td>
                                <g:link url="${classified.url}" target="_blank">
                                    ${classified.price} €
                                </g:link>
                            </td>
                            <td>
                                <g:link url="${classified.url}" target="_blank">
                                    <g:formatDate date="${classified.date}" format="HH:mm"/>
                                    <br/>
                                    <small>
                                        <g:formatDate date="${classified.date}" format="dd MMM yyyy"/>
                                    </small>
                                </g:link>
                            </td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="col-md-2"></div>
</div>
</body>
</html>