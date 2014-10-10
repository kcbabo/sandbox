
###Goals
* Create baseline application demonstrating transformation with current tooling and runtime
* Evaluate existing tools and runtime support for example application and determine what can be used moving forward
* Identify tooling and runtime gaps for transformation uses cases involving Java, XML, and JSON
* Create initial version of visual mapper which can produce Dozer mapping configuration
* Provide Java class generation from JSON Schema

###Baseline Application
The baseline application used as input for Milestone 1 can be found here:
(https://github.com/kcbabo/sandbox/tree/master/mapper/examples/map-0)

The application demonstrates transformation from XML to JSON using Camel.  Details on how the application was created and how milestone 1 will help can be found under  

###Perspectives
There are three perspectives to keep in mind with each milestone:
* *User View* : XML -> JSON
* *Tooling View* : XSD -> Java -> Java -> JSON Schema
* *Runtime View* : XML -> Java -> Java -> JSON

###Tooling Deliverables
* XSD viewer
* Java source generation from XSD
* JSON schema viewer
* Java source generation from JSON schema
* Dozer mapping
  * View input model
  * View output model
  * Assignment from input -> output
* Runtime configuration
  * Dozer bean configuration
  * JSON data format
  * JAXB data format
  * Transform sequence in Camel route

###Runtime Deliverables