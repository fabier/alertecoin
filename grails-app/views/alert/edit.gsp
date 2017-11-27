<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Alerte</title>
    <script>
        $(function () {
            $("input[type=radio]").change(function () {
                if (this.value == '1440') {
                    $("#hourOfDay").removeClass("display-none");
                } else {
                    $("#hourOfDay").addClass("display-none");
                }
            });
            if ($("input[type=radio]:checked").val() == '1440') {
                $("#hourOfDay").removeClass("display-none");
            }
        });
    </script>
</head>

<body>
<div class="row">
    <div class="col-md-12">
        <div class="panel panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title">
                    <strong>Alerte : ${alert.name}</strong>
                </h3>
            </div>

            <div class="panel-body">
                <g:form controller="alert" action="update" id="${alert.id}"
                        class="form form-horizontal" role="form">
                %{--<input type="hidden" id="id" name="id" value="${alert.id}"/>--}%

                    <div class="form-group">
                        <label for="name" class="col-md-2 control-label">
                            <g:message code="alertecoin.name" default="Titre"/>
                        </label>

                        <div class="col-md-4">
                            <input type="text" name="name" id="name" class="form-control"
                                   placeholder="Titre de votre alerte"
                                   value="${alert?.name}"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="url" class="col-md-2 control-label">
                            <g:message code="alertecoin.url" default="URL"/>
                        </label>

                        <div class="col-md-10">
                            <input type="text" name="url" id="url" class="form-control"
                                   placeholder="Copier-coller ici l'adresse de la page"
                                   value="${alert?.url}"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="checkIntervalInMinutes" class="col-md-2 control-label">
                            <g:message code="alertecoin.checkIntervalInMinutes" default="Fréquence"/>
                        </label>

                        <div class="col-md-10">
                            <g:radioGroup name="checkIntervalInMinutes"
                                          value="${alert?.checkIntervalInMinutes ?: 5}"
                                          id="checkIntervalInMinutes"
                                          labels="${labels}"
                                          values="${values}">
                                <p>
                                    <label>
                                        <span class="radioSpan">${it.radio}</span>
                                        ${it.label}
                                        <g:if test="${it.label.equals('Une fois par jour')}">
                                            <g:select id="hourOfDay" name="hourOfDay" from="${0..23}"
                                                      value="${alert.hourOfDay ?: 7}"
                                                      class="display-none"
                                                      optionValue="${{
                                                          formatNumber(number: it, minIntegerDigits: 2) + ":00"
                                                      }}"/>
                                        </g:if>
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
                                Mettre à jour
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
</div>
</body>
</html>