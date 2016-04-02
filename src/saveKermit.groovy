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

}
// the script
def hint = hint()
def url = saveKermit(hint)
Desktop.getDesktop().browse(new URI(url))

