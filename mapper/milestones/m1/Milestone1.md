
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

###Workflow
Rough workflow for end user in milestone 1:

1.  New Fuse project - Spring DSL
2.  Import XSD and JSON schema
3. Generate Java classes for map input from XSD
4. Generate Java classes for map output from JSON schema
5. Create new Dozer mapping
6. Import input and output classes and assign fields to create mapping in visual mapper view.  Mapping stored as Dozer configuration.
7. Create Camel route to run through transformation sequence (unmarshall from XML, dozer converter, marshall to JSON)
8. Run route and verify output

###Touchpoints
This section details important elements of the tooling and runtime that are 'touched' during the course of creating and running the baseline application in milestone 1.

###Tooling Touchpoints
* XSD viewer : already present in Eclipse, so no work required.
* Java source generation from XSD : already present in JBDS (possibly from core Eclipse).
* JSON schema viewer : text editor for now.
* Java source generation from JSON schema : no tooling for this AFAICT, but building a UI action around the core library from [jsonschema2pojo](http://www.jsonschema2pojo.org/) should work.
* Visual mapper
  * Import and view for input model
  * Import and view for output model
  * Assignment from input -> output
  * Persisted as Dozer configuration
* Runtime configuration
  * Dozer bean configuration : added to Spring application context using text editor
  * JSON data format : added to Camel context definition using text editor
  * JAXB data format : no distinct data format definition required; can be done via EIP editor
  * Transform sequence in Camel route : created through EIP editor

###Runtime Deliverables
* No runtime changes for Camel or SwitchYard in milestone 1
