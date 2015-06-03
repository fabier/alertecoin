<html>

<head>
    <g:set var="hideLoginBlock" value="${true}" scope="request"/>
    <title><g:message code='spring.security.ui.login.title'/></title>
    <meta name='layout' content='main'/>
</head>

<body>
<div class="row">
    <div class="col-md-2"></div>

    <div class="col-md-8">

        <g:render template="/templates/flashMessage"/>

        <div class="panel panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title">
                    <strong><g:message code="alertecoin.connexion"/></strong>
                </h3>
            </div>

            <div class="panel-body">
                <form action='${postUrl}' method='POST' id="loginForm" name="loginForm" autocomplete='off'
                      class="form-horizontal">
                    <div class="form-group">
                        <label for="username" class="col-md-4 control-label">
                            <g:message code="alertecoin.email" default="Email"/>
                        </label>

                        <div class="col-md-4">
                            <input name="j_username" id="username" size="20" class="form-control"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="password" class="col-md-4 control-label">
                            <g:message code="alertecoin.password" default="Mot de passe"/>
                        </label>

                        <div class="col-md-4">
                            <input type="password" name="j_password" id="password" size="20" class="form-control"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-md-4">
                        </div>

                        <div class="col-md-4">
                            <input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me'
                                   <g:if test='${hasCookie}'>checked='checked'</g:if>/>
                            <label for='remember_me'>
                                <g:message code="alertecoin.rememberMe" default="Rester connecté"/>
                            </label>
                        </div>

                    </div>

                    <div class="form-group">
                        <div class="col-md-4">
                        </div>

                        <div class="col-md-8">
                            <button type="submit" class="btn btn-primary">
                                <g:message code="alertecoin.login" default="Se connecter"/>
                            </button>
                            <g:link controller='register' action='forgotPassword' class="paddingleft10">
                                <g:message code="alertecoin.forgotPassword" default="Mot de passe oublié ?"/>
                            </g:link>
                            <g:link controller="register" action="index" class="btn btn-success toRight">
                                <g:message code="alertecoin.createAccount"/>
                            </g:link>
                        </div>
                    </div>

                </form>
            </div>
        </div>

        <div class="panel-body">
        </div>
    </div>

    <div class="col-md-2"></div>
</div>

%{--<div class="login s2ui_center ui-corner-all" style='text-align:center;'>--}%
%{--<div class="login-inner">--}%
%{--<div class="sign-in">--}%
%{----}%
%{--<table>--}%
%{--<tr>--}%
%{--<td><label for="username"><g:message code='spring.security.ui.login.username'/></label></td>--}%
%{--<td><input name="j_username" id="username" size="20"/></td>--}%
%{--</tr>--}%
%{--<tr>--}%
%{--<td><label for="password"><g:message code='spring.security.ui.login.password'/></label></td>--}%
%{--<td><input type="password" name="j_password" id="password" size="20"/></td>--}%
%{--</tr>--}%
%{--<tr>--}%
%{--<td colspan='2'>--}%
%{--<input type="checkbox" class="checkbox" name="${rememberMeParameter}" id="remember_me"--}%
%{--checked="checked"/>--}%
%{--<label for='remember_me'><g:message code='spring.security.ui.login.rememberme'/></label> |--}%
%{--<span class="forgot-link">--}%
%{--<g:link controller='register' action='forgotPassword'><g:message--}%
%{--code='spring.security.ui.login.forgotPassword'/></g:link>--}%
%{--</span>--}%
%{--</td>--}%
%{--</tr>--}%
%{--<tr>--}%
%{--<td colspan='2'>--}%
%{--<s2ui:linkButton elementId='register' controller='register'--}%
%{--messageCode='spring.security.ui.login.register'/>--}%
%{--<s2ui:submitButton elementId='loginButton' form='loginForm'--}%
%{--messageCode='spring.security.ui.login.login'/>--}%
%{--</td>--}%
%{--</tr>--}%
%{--</table>--}%
%{--</div>--}%
%{--</div>--}%
%{--</div>--}%

<script>
    $(document).ready(function () {
        $('#username').focus();
    });

    <s2ui:initCheckboxes/>

</script>

</body>
</html>
