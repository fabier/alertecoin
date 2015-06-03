<g:if test="${flash?.message}">
    <div class="alert alert-${flash?.level} alert-dismissible" role="alert">
        <button type="button" class="close" data-dismiss="alert">
            <span aria-hidden="true">
            &times;
            </span>
            <span class="sr-only">
                Close
            </span>
        </button>
        <strong>
            <g:message code="alertecoin.flash.${flash?.level}"/>
        </strong>
        ${flash?.message}
    </div>
</g:if>