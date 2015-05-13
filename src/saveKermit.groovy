import groovy.json.JsonSlurper
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1')
import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.GET

http = new HTTPBuilder('http://localhost:8080')

hint = hint()
saveKermit hint

def hint() {
    def hint
    http.request( GET, TEXT ) { req ->
        uri.path = '/hint'
        headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4"
        headers.Accept = 'application/json'

        response.success = { resp, reader ->
            hint = reader.text
        }
    }
    hint
}

def saveKermit(street) {
    http.request( GET, TEXT ) { req ->
        uri.path = '/whereIsKermit'
        headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4"
        headers.Accept = 'application/json'

        response.success = { resp, reader ->
            def result = reader.text

            def secrets = new JsonSlurper().parseText(result).secrets
            def url = kermitUrl(secrets, street)
            println "You can see Kermit here: $url"
        }
        response.'404' = {
            println 'Not found'
        }
    }
}



def kermitUrl(secrets, street) {
    secrets.collect { secret ->
        secret.street = secret.street.reverse()
        secret
    }.
    findAll { it.street == street}.
    max { it.floors }.
    url
}

