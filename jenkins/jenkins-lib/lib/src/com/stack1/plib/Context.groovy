package com.stack1.plib

class Context implements Serializable{

	final HashMap config
	final String application

	BuildContext(String configuration) {
		this.application = 'default'
		this.config = (new HashMap(
				new groovy.json.JsonSlurperClassic().
				parseText(configuration)
				)
				).asImmutable()
	}
	BuildContext(String application, String configuration) {
		this.application = application
		this.config = (new HashMap(
				new groovy.json.JsonSlurperClassic().
				parseText(configuration)
				)
				).asImmutable()
	}
}
