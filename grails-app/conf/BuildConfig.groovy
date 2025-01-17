grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'
grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.fork = [
        // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
        //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

        // configure settings for the test-app JVM, uses the daemon by default
        test   : [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon: true],
        // configure settings for the run-app JVM
        run    : [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve: false],
        // configure settings for the run-war JVM
        war    : [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve: false],
        // configure settings for the Console UI JVM
        console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = 'maven' // or ivy
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits('global') {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log 'warn' // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenRepo 'http://repository.codehaus.org'
        //mavenRepo 'http://download.java.net/maven/2/'
        //mavenRepo 'http://repository.jboss.com/maven2/'
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        // runtime 'mysql:mysql-connector-java:5.1.27'

        runtime 'org.jsoup:jsoup:1.7.2'
        runtime 'org.postgresql:postgresql:42.0.0.jre7'

        // Cache entities
        compile 'net.sf.ehcache:ehcache:2.8.5'

        // HTTP Client Apache
        compile 'org.apache.httpcomponents:httpclient:4.5'
    }

    plugins {
        // Conteneur Tomcat
        build ':tomcat:8.0.14.1'

        // Resources
        compile ':cache:1.1.8'
        compile ":asset-pipeline:2.1.5"
        compile ":less-asset-pipeline:2.0.8"

        build(':release:3.0.1', ':rest-client-builder:1.0.3') {
            export = false
        }

        // Bootstrap
        compile ":twitter-bootstrap:3.3.4"
        runtime ":jquery:1.11.1"
        compile ":jquery-ui:1.10.4"

        // Others
        compile ':hibernate4:5.0.0.RC1'

        // Gestion utilisateurs
        compile ":spring-security-core:2.0-RC4"
        compile ":spring-security-ui:1.0-RC2"

        // Gestion des emails
        compile ":mail:1.0.7"

        // Gestion des taches recurrentes
        compile ":quartz:1.0.2"

        // Statistiques et monitoring
        compile ":grails-melody:1.55.0"

        // Pour Google Analytics
        compile ":google-analytics:2.3.3"
    }
}


environments {
    development {
    }

    test {
    }

    production {
    }
}