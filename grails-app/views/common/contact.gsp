<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Contact</title>
</head>

<body>
<div class="row">
    <div class="col-md-12">
        <div class="panel panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title">
                    <strong>Contact</strong>
                </h3>
            </div>

            <div class="panel-body">
                <div class="row">
                    <div class="col-md-2"></div>

                    <div class="col-md-8">
                        <p>
                            Envoyez moi un message avec le formulaire ci dessous !
                        </p>
                    </div>
                </div>

                <br/>

                <g:form controller="common" action="sendMail" role="form" class="form-horizontal">
                    <div class="form-group">
                        <label for="email" class="col-md-2 control-label">Email</label>

                        <div class="col-md-6">
                            <sec:ifLoggedIn>
                                <input type="email" class="form-control" id="email" name="email"
                                       placeholder="Votre adresse email"
                                       value="${sec.loggedInUserInfo(field: "username")}"/>
                            </sec:ifLoggedIn>
                            <sec:ifNotLoggedIn>
                                <input type="email" class="form-control" id="email" name="email"
                                       placeholder="Votre adresse email"/>
                            </sec:ifNotLoggedIn>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="message" class="col-md-2 control-label">Message</label>

                        <div class="col-md-10">
                            <textarea class="form-control" id="message" name="message" rows="5"
                                      placeholder="Merci de dire quelquechose de gentil !"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-md-4">
                        </div>

                        <div class="col-md-8">
                            <button type="submit" class="btn btn-primary">
                                Envoyer le message
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