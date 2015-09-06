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
                <ul class="ul-no-decoration">
                    <g:each in="${classifieds}" var="classified">
                        <li class="row clickable-row paddingtopbottom10 bottombordergrey"
                            data-href-blank="${raw(createLink(url: classified.url))}">
                            <div class="col-md-3 text-center ">
                                <g:if test="${classified.images}">
                                    <img src="${createLink(controller: "image", action: "get", id: classified.images.first().id)}"
                                         class="image-pola"/>
                                </g:if>
                                <g:else>
                                    <img src="holder.js/120x90/text:Pas de visuel" class="image-pola"/>
                                </g:else>
                            </div>

                            <div class="col-md-9">
                                <div>
                                    <span class="xlarge">
                                        ${classified.name}
                                    </span>
                                    <g:if test="${classified.price}">
                                        <span class="large label label-warning pull-right">
                                            ${classified.price} €
                                        </span>
                                    </g:if>
                                </div>

                                <div class="small">
                                    <g:getExtra extras="${classified.classifiedExtras}" name="postalCode"/>
                                    <g:getExtra extras="${classified.classifiedExtras}" name="addressLocality"/>
                                    -
                                    <g:formatDate date="${classified.date}" format="d MMM à HH:mm"/>
                                </div>

                                <div class="text-muted xsmall">
                                    ${raw(classified.description)}
                                </div>
                            </div>
                        </li>
                    </g:each>
                </ul>
            </div>
        </div>
    </div>

    <div class="col-md-2"></div>
</div>
</body>
</html>