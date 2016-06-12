<footer>
    <div class="container">
        <sec:ifAllGranted roles="ROLE_ADMIN">
            <div class="pull-left">
            </div>

            <div class="pull-right">
                <g:link controller="admin" action="index" class="btn btn-info">
                    Admin
                </g:link>
            </div>
        </sec:ifAllGranted>

        <div class="btn btn-link block">
            <g:link controller="common" action="contact">
                &copy; AlerteCoin 2014 - <g:getYear/>
            </g:link>
        </div>
    </div>
</footer>