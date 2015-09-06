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
                            <small class="text-white url">
                                ${alert.url}
                            </small>
                        </g:link>
                    </div>

                    <div class="col-md-4 alignRight">
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
                </div>
            </div>

            <div class="panel-body">

                <table class="table table-condensed table-classifieds">
                    <thead>
                    <tr>
                        <th class="col-md-2">Image</th>
                        <th class="col-md-10">Titre</th>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${classifieds}" var="classified">
                        <tr class="clickable-row"
                            data-href="${raw(createLink(url: classified.url, target: "_blank"))}">
                            <td class="text-center">
                                <g:if test="${classified.images}">
                                    <img src="${classified.images.first().url}" class="image-pola"/>
                                </g:if>
                                <g:else>
                                    <img src="holder.js/120x90/text:Pas de visuel" class="image-pola"/>
                                </g:else>
                            </td>
                            <td>
                                <p class="large">
                                    <span class="text-muted xxsmall pull-right">
                                        <g:formatDate date="${classified.date}" format="HH:mm - dd MMMM yyyy"/>
                                        <br/>
                                        <g:if test="${classified.price}">
                                            ${classified.price} €
                                        </g:if>
                                    </span>
                                    ${classified.name}
                                    <span class="text-muted xsmall">
                                        <g:set var="description" value="${classified.classifiedExtras.find {
                                            it.key.name == "description"
                                        }}"/>
                                        <g:if test="description">
                                            <br/>
                                            <small>${description.value}</small>
                                            <br/>
                                        </g:if>
                                    </span>

                                    <g:each in="${classified.classifiedExtras}" var="classifiedExtra">
                                        <br/>
                                        <span class="xxsmall">
                                            ${classifiedExtra.key.name} ${classifiedExtra.value}
                                        </span>
                                    </g:each>
                                </p>
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