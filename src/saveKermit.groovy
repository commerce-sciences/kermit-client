import groovy.json.JsonSlurper

@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1')
import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.GET

import java.awt.Desktop
import java.net.URI

http = new HTTPBuilder('http://localhost:8080')

def hint() {
    def hint
    http.request( GET, TEXT ) { req ->
        uri.path = '/hint'
        headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4"

        response.success = { resp, reader ->
            hint = reader.text
        }
    }
    hint
}

def saveKermit(street) {
    def url
    http.request( GET, TEXT ) { req ->
        uri.path = '/whereIsKermit'
        headers.'User-Agent' = "Mozilla/5.0 Firefox/3.0.4"
        headers.Accept = 'application/json'

        response.success = { resp, reader ->
            def result = reader.text

            def secrets = new JsonSlurper().parseText(result).secrets
            url = kermitUrl(secrets, street)
            println "You can see Kermit here: $url"
        }
        response.'404' = {
            println 'Not found'
        }
    }
    url
}



def kermitUrl(secrets, street) {
    def decryptedSecrets = decrypt(secrets)
    def buildings = buildingsOnStreet(decryptedSecrets, street)
    def theBuilding = tallestBuilding(buildings)
    theBuilding.url
}

def decrypt(secrets) {
    secrets.collect { secret ->
        secret.street = secret.street.reverse()
        secret
    }
    secrets
}

def buildingsOnStreet(buildings, street) {
    buildings.findAll { it.street == street}
}

def tallestBuilding(buildings) {
    buildings.max { it.floors }
}

// the script
def hint = hint()
def url = saveKermit(hint)
Desktop.getDesktop().browse(new URI(url))

