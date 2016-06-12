<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Liste des utilisateurs</title>
</head>

<body>

<div class="row">
    <div class="col-md-12">
        <table class="table table-condensed table-users">
            <thead>
            <tr>
                <th class="col-md-2">Id</th>
                <th class="col-md-4">Nom</th>
                <th class="col-md-6">Email</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${users}" var="user">
                <tr class="clickable-row"
                    data-href="${raw(createLink(controller: "admin", action: "userAlerts", id: user.id))}">
                    <td>
                        ${user.id}
                    </td>
                    <td>
                        ${user.username}
                    </td>
                    <td>
                        ${user.email}
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>