package com.stack1.plib

class ConfigurableHandlers implements Serializable,Handlers{

	String builder
	String deployer 
	List<String> unitTests
	List<String> staticAnalysis 
	List<String> integrationTests 

	public ConfigurableBuildHandlers(String builder, 
						String deployer, 
						List<String> unitTests, 
						List<String> staticAnalysis, 
						List<String> integrationTests) { 

		this.builder = builder
		this.deployer = deployer
		this.unitTests = unitTests
		this.staticAnalysis = staticAnalysis
		this.integrationTests = integrationTests
	}
}

