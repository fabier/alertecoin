<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Liste des utilisateurs</title>
</head>

<body>

<div class="row">
    <div class="col-md-2">
    </div>

    <div class="col-md-8">

        <g:render template="/templates/flashMessage"/>

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
                <tr>
                    <td>
                        <g:link url="#">
                            ${user.id}
                        </g:link>
                    </td>
                    <td>
                        <g:link url="#">
                            ${user.username}
                        </g:link>
                    </td>
                    <td>
                        <g:link url="#">
                            ${user.email}
                        </g:link>
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>
    </div>

    <div class="col-md-2">
    </div>
</div>
</body>
</html>