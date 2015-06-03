<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Nouvelle alerte</title>
</head>

<body>
<div class="row">

    <div class="col-md-2"></div>

    <div class="col-md-8">

        <g:render template="/templates/flashMessage"/>

        <div class="panel panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title">
                    <strong>Nouvelle alerte</strong>
                </h3>
            </div>

            <div class="panel-body">
                <g:form controller="alert" action="create" class="form form-horizontal" role="form">
                    <div class="form-group">
                        <label for="name" class="col-md-2 control-label">
                            <g:message code="alertecoin.name" default="Titre"/>
                        </label>

                        <div class="col-md-6">
                            <input type="text" name="name" id="name" class="form-control"
                                   placeholder="Titre de votre alerte"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="url" class="col-md-2 control-label">
                            <g:message code="alertecoin.url" default="URL"/>
                        </label>

                        <div class="col-md-10">
                            <input type="text" name="url" id="url" class="form-control"
                                   placeholder="Copier-coller ici l'adresse de la page"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="checkIntervalInMinutes" class="col-md-2 control-label">
                            <g:message code="alertecoin.checkIntervalInMinutes" default="Fréquence"/>
                        </label>

                        <div class="col-md-10">
                            <g:radioGroup name="checkIntervalInMinutes"
                                          value="5"
                                          id="checkIntervalInMinutes"
                                          labels="['Dès que possible', 'Toutes les heures', 'Une fois par jour']"
                                          values="['5', '60', '1440']">
                                <p>
                                    <label>
                                        <span class="radioSpan">${it.radio}</span>
                                        ${it.label}
                                    </label>
                                </p>
                            </g:radioGroup>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-md-2 control-label">
                        </div>

                        <div class="col-md-10">
                            <button type="submit" class="btn btn-primary">
                                Créer une nouvelle alerte
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