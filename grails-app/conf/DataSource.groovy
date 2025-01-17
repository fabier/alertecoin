import alertecoin.TableNameSequencePostgreSQLDialect

dataSource {
    pooled = true
//    dbCreate = "validate"
    dbCreate = "update"
    driverClassName = "org.postgresql.Driver"
    url = "jdbc:postgresql://localhost:5432/alertecoin"
    dialect = TableNameSequencePostgreSQLDialect.class.getName()
    username = "dev"
    password = "dev"
    properties {
        initialSize = 1
        maxActive = 16
        minIdle = 1
        maxIdle = 4
        maxWait = 10000
    }
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'org.hibernate.cache.SingletonEhCacheRegionFactory'
}

// environment specific settings
environments {
    development {
        dataSource {
        }
    }
    test {
        dataSource {
        }
    }
    production {
        dataSource {
            username = "grails"
            password = "P639rb98n4H3YD4E7q"
        }
    }
}