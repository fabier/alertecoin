grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'

grails.project.fork = [
        test   : false, // [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon: true], // configure settings for the test-app JVM
        run    : false, // [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256], // configure settings for the run-app JVM
        war    : false, // [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256], // configure settings for the run-war JVM
        console: false // [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]// configure settings for the Console UI JVM
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
        runtime 'org.postgresql:postgresql:9.2-1003-jdbc4'
    }

    plugins {
        build(':release:3.0.1', ':rest-client-builder:1.0.3') {
            export = false
        }
        // Resources
        compile ":asset-pipeline:1.9.9"

        // Bootstrap
        runtime ':twitter-bootstrap:3.3.0'
        runtime ":jquery:1.11.1"
        compile ":jquery-ui:1.10.4"

        // Others
        runtime ':hibernate4:4.3.5.2'
        build ':tomcat:8.0.14.1'

        // Gestion utilisateurs
        compile ":spring-security-core:2.0-RC4"
        compile ":spring-security-ui:1.0-RC2"

        // Gestion des emails
        compile ":mail:1.0.7"

        // Gestion des taches recurrentes
        compile ":quartz:1.0.2"
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