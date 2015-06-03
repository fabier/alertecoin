<html>

<head>
    <g:set var="hideLoginBlock" value="${true}" scope="request"/>
    <meta name='layout' content='main'/>
    <title>Mot de passe perdu</title>
</head>

<body>

<p/>

<div class="row">
    <div class="col-md-2"></div>

    <div class="col-md-8">

        <g:render template="/templates/flashMessage"/>

        <div class="panel panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title">
                    <strong><g:message code="alertecoin.forgotPassword.title"/></strong>
                </h3>
            </div>

            <div class="panel-body">
                <g:form controller="register" action="forgotPassword" class="form-horizontal" role="form" method="POST">
                    <g:if test='${emailSent}'>
                        <br/>
                        <g:message code='spring.security.ui.forgotPassword.sent'/>
                    </g:if>
                    <g:else>
                        <div class="form-group">
                            <label for="username" class="col-md-4 control-label">
                                <g:message code="alertecoin.email" default="Email"/>
                            </label>

                            <div class="col-md-4">
                                <input name="username" id="username" size="20" class="form-control"/>
                            </div>
                        </div>

                    %{--<br/>--}%
                    %{--<h4><g:message code='spring.security.ui.forgotPassword.description'/></h4>--}%

                    %{--<table>--}%
                    %{--<tr>--}%
                    %{--<td><label for="username"><g:message--}%
                    %{--code='spring.security.ui.forgotPassword.username'/></label>--}%
                    %{--</td>--}%
                    %{--<td><g:textField name="username" size="25"/></td>--}%
                    %{--</tr>--}%
                    %{--</table>--}%


                        <div class="form-group">
                            <div class="col-md-4">
                            </div>

                            <div class="col-md-8">
                                <button type="submit" class="btn btn-primary">
                                    Récupérer mon mot de passe
                                </button>
                            </div>
                        </div>
                    %{--<s2ui:submitButton elementId='reset' form='forgotPasswordForm'--}%
                    %{--messageCode='spring.security.ui.forgotPassword.submit'/>--}%

                    </g:else>
                </g:form>
            %{--<s2ui:form width='400' height='220' elementId='forgotPasswordFormContainer'--}%
            %{--titleCode='spring.security.ui.forgotPassword.header' center='true'>--}%
            %{----}%
            %{--<g:form action='forgotPassword' name="forgotPasswordForm" autocomplete='off'>--}%
            %{----}%
            %{--<g:if test='${emailSent}'>--}%
            %{--<br/>--}%
            %{--<g:message code='spring.security.ui.forgotPassword.sent'/>--}%
            %{--</g:if>--}%
            %{----}%
            %{----}%
            %{----}%
            %{--</g:form>--}%
            %{--</s2ui:form>--}%
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        $('#username').focus();
    });
</script>

</body>
</html>
