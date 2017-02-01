<tr class="clickable-row"
    data-href="${raw(createLink(controller: "alert", action: "show", id: alert.id))}">
    <td class="text-vertical-align-center">
        <span class="xlarge bold">
            ${alert.name}
        </span>
    </td>
    <td class="text-vertical-align-center" align="center">
        <g:set var="classifiedCount" value="${alert.classifieds?.size()}"/>

        <span class="label label-default width-100">
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
    </td>
    <td>
        <span class="url xsmall text-muted">
            ${alert.url}
        </span>

        <br/>

        <span class="xsmall text-muted">
            Dernière vérification :
            <g:formatDate date="${alert.lastCheckedDate}" format="d MMM à HH:mm"/>
        </span>

        <br/>

        <span class="xsmall text-muted">
            Prochaine vérification :
            <g:formatDate date="${alert.nextCheckDate}" format="d MMM à HH:mm"/>
        </span>
    </td>
    <td class="text-vertical-align-center">
        <div class="pull-right">
            <g:link controller="alert" action="refresh" id="${alert.id}" class="btn btn-default" title="Refresh">
                <span class="glyphicon glyphicon-repeat"></span>
            </g:link>

            <sec:ifAllGranted roles="ROLE_ADMIN">
                <g:link controller="alert" action="email" id="${alert.id}" class="btn btn-info" title="Email"
                        target="_blank">
                    <span class="glyphicon glyphicon-envelope"></span>
                </g:link>
            </sec:ifAllGranted>

            <g:link controller="alert" action="edit" id="${alert.id}" class="btn btn-success" title="Editer">
                <span class="glyphicon glyphicon-edit"></span>
            </g:link>

            <g:link controller="alert" action="confirmDelete" id="${alert.id}" class="btn btn-danger" title="Supprimer">
                <span class="glyphicon glyphicon-trash"></span>
            </g:link>
        </div>
    </td>
</tr>