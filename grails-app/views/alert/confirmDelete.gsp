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
                <h3 class="panel-title">
                    <strong>Alerte : ${alert.name}</strong>
                </h3>
            </div>

            <div class="panel-body">
                <g:form controller="alert" action="delete" id="${alert.id}" class="form form-horizontal" role="form">
                    <p>
                        La suppression d'une alerte est définitive, êtes vous sûr de vouloir supprimer cette alerte ?
                    </p>

                    <div class="form-group">
                        <div class="col-md-2 control-label">
                        </div>

                        <div class="col-md-10">
                            <button type="submit" class="btn btn-primary">
                                Confirmer la suppression
                            </button>
                            &nbsp;
                            <button type="button" class="btn btn-link" onclick="history.go(-1)">
                                Annuler
                            </button>
                        </div>
                    </div>
                </g:form>
            </div>
        </div>
    </div>

    <div class="col-md-2"></div>
</div>
</body>
</html>