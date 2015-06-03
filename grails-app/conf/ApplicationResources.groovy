modules = {

    base {
        resource url: 'css/application.css'
        resource url: 'js/application.js'
    }

    holder {
        resource url: 'js/holder.js'
    }

    application {
        dependsOn 'base, holder, bootstrap, jquery'
    }
}