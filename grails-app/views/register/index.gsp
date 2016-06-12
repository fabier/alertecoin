<html>

<head>
    <meta name='layout' content='main'/>
    <title><g:message code='spring.security.ui.register.title'/></title>
</head>

<body>

<div class="row">
    <div class="col-md-12">
        <div class="panel panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title">
                    <strong><g:message code="alertecoin.createAccount"/></strong>
                </h3>
            </div>

            <div class="panel-body">
                <g:form action='register' name='registerForm' role="form" class="form-horizontal">

                    <g:if test='${emailSent}'>
                        <p>
                            <g:message code='alertecoin.emailSent'/>
                        </p>
                    </g:if>
                    <g:else>
                        <div class="row">
                            <div class="col-md-4"></div>

                            <div class="col-md-8">
                                <p>
                                    Remplissez le formulaire, puis cliquez sur "Créer un compte".
                                </p>
                            </div>
                        </div>

                        <br/>

                        <div class="form-group ${hasErrors(bean: command, field: "username") {
                            "has-error has-feedback"
                        }}">
                            <label for="username" class="col-md-4 control-label">
                                <g:message code="alertecoin.username" default="Nom"/>
                            </label>

                            <div class="col-md-4">
                                <input type="text" class="form-control" id="username" name="username"
                                       placeholder="Prénom Nom" value="${command.username}"/>
                                <g:hasErrors bean="${command}" field="username">
                                    <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                </g:hasErrors>
                            </div>

                            <div class="col-md-5">
                                <g:hasErrors bean="${command}" field="username">
                                    <span class="text-danger">
                                        Vous devez spécifier un nom d'utilisateur
                                    </span>
                                </g:hasErrors>
                            </div>
                        </div>

                        <div class="form-group ${hasErrors(bean: command, field: "email") {
                            "has-error has-feedback"
                        }}">
                            <label for="email" class="col-md-4 control-label">
                                <g:message code="alertecoin.email" default="E-mail"/>
                            </label>

                            <div class="col-md-4">
                                <input type="email" class="form-control" id="email" name="email"
                                       placeholder="Votre adresse email" value="${command.email}"/>
                                <g:hasErrors bean="${command}" field="email">
                                    <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                </g:hasErrors>
                            </div>

                            <div class="col-md-6">
                                <g:hasErrors bean="${command}" field="email">
                                    <span class="text-danger">
                                        Saisissez un email valide
                                    </span>
                                </g:hasErrors>
                            </div>
                        </div>

                        <div class="form-group ${hasErrors(bean: command, field: "password") {
                            "has-error has-feedback"
                        }}">
                            <label for="password" class="col-md-4 control-label">
                                <g:message code="alertecoin.password" default="Mot de passe"/>
                            </label>

                            <div class="col-md-4">
                                <input type="password" class="form-control" id="password" name="password"
                                       placeholder="Votre mot de passe" value="${command.password}"/>
                                <g:hasErrors bean="${command}" field="password">
                                    <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                </g:hasErrors>
                            </div>

                            <div class="col-md-6">
                                <g:hasErrors bean="${command}" field="password">
                                    <span class="text-danger">
                                        Saisissez un mot de passe (6 caractères min.)
                                    </span>
                                </g:hasErrors>
                            </div>
                        </div>

                        <div class="form-group ${hasErrors(bean: command, field: "password2") {
                            "has-error has-feedback"
                        }}">
                            <label for="password2" class="col-md-4 control-label">
                                <g:message code="alertecoin.password2" default="Mot de passe\n(répéter)"/>
                            </label>

                            <div class="col-md-4">
                                <input type="password" class="form-control" id="password2" name="password2"
                                       placeholder="Répéter votre mot de passe" value="${command.password2}"/>
                                <g:hasErrors bean="${command}" field="password2">
                                    <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                                </g:hasErrors>
                            </div>

                            <div class="col-md-6">
                                <g:hasErrors bean="${command}" field="password2">
                                    <span class="text-danger">
                                        Les mots de passe ne correspondent pas
                                    </span>
                                </g:hasErrors>
                            </div>
                        </div>


                        <div class="form-group">
                            <label for="password2" class="col-md-4 control-label">
                            </label>

                            <div class="col-md-8">
                                <button type="submit" class="btn btn-primary">
                                    Créer un compte
                                </button>
                            </div>
                        </div>
                    </g:else>
                </g:form>
            </div>
        </div>
    </div>

    <div class="col-md-4"></div>
</div>

<script>
    $(document).ready(function () {
        $('#username').focus();
    });
</script>

</body>
</html>
