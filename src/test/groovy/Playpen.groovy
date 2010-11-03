import groovyx.remote.client.RemoteControl
import groovyx.remote.server.Receiver
import groovyx.remote.transport.local.LocalTransport

class Playpen extends GroovyTestCase {

	def remote
	
	void setUp() {
		// we need to create a classloader for the "server" side that cannot access
		// classes defined in this file.
		def thisClassLoader = getClass().classLoader
		def neededURLsForServer = thisClassLoader.getURLs().findAll { it.path.contains("groovy-all") }
		def serverClassLoader = new URLClassLoader(neededURLsForServer as URL[], thisClassLoader.parent)
		
		def receiver = new Receiver(serverClassLoader, [num: 2])
		def transport = new LocalTransport(receiver, thisClassLoader)
		
		remote = new RemoteControl(transport, thisClassLoader)
	}
	
	void testIt() {
		assert remote.exec { num + 2 } == 4
	}
	
}