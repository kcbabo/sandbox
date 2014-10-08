
###Proposal
Camel has traditionally followed a "type-less" model for integration, where endpoints do not have contracts and processors do not have knowledge of the type of a message being processed.  We propose several additions to Camel to provide information on message types and to declare contracts within Camel configuration to support statically-typed interactions.  Adding these capabilities to Camel will provide the following benefits to Camel users:
* Static evaluation and validation of data types and interactions in an application.
* Runtime evaluation of data types and interactions
* The ability to start with a weak/dynamically typed application and move to a statically typed application
* Support for declarative transformation / automatic type conversion
* Support for declarative validation of data types

###Contract Metadata
There are two important components of contract metadata:
* Message type identifier
* Message exchange pattern

A message type identifier is a string which unambiguously identifies the type of a message.  In the case of a business domain object in Java, the fully-qualified Java class name may be sufficient to identify the message type (e.g. "org.example.abc.Order").  This approach does not cover cases where a message with a non-Java content model (XML, CSV, EDI, etc.) is carried inside a Java object (all of the former examples could be held in "java.lang.String", for example).  As a result, message type identifiers should not be tied exclusively to the Java object representing the message payload and the Camel runtime should allow arbitrary string values to be used as a message identifier.

Message exchange patterns serve as a way to correlate expected inputs and outputs.  These are typically defined via operation/method definition and 
identify the message types being used.  The exchange pattern can be declared in a free-form nature by identifying the input and optional output type or it can be defined within a contract (Java interface, WSDL) and referenced by name.

###Contract Definition
Contract information can be associated with a Camel application statically or dynamically.  The static option involves the user defining contract metadata directly in their application configuration - through a DSL extension, for example. Dynamic definition, on the other hand, is supplied at runtime through a Camel component based on its inherent knowledge of the message.  It's important to note that these approaches are not mutually exclusive.

####Static Definition

1) *Extend the DSL - add methods that allow input and output types to be specified directly via the DSL.* 
```
class ProcessorDefinition {
	// Set the input type for the current processor
	public Type inputType(String inputType);
	// Set the output type returned from the current processor
	public Type outputType(String outputType);
	
	// Alternative form which allows contract to be used instead of 
	// directly specifying input/output types.  Assumes there is a 
	// way to produce Contract instances off of common interfaces (WSDL, Java).
	public Type contract(Contract contract);

	// ... other methods omitted 
}
```
```
from("direct:abc")
		.inputType("java:org.example.Order")
	.to("direct:d")
   		.inputType("{org.example}xmlOrder")
   		.outputType("urn:acme:orderAck")
   	.to("direct:e");
```

Putting aside the crappy syntax, the above example shows how static type definition can be specified directly within the DSL.  A positive with this approach is that is provides a fine-grained approach to specifying type metadata that applies to all types of processors.  A negative is that it might be too "chatty" in the DSL; it's also unclear we even need to specify contract metadata at the processor level.

2) *Provide a standard set of endpoint parameters that specify the contract metadata.*

```
   component-name : <hierarchical-part> ? [ <component-config> ] [ <contract> ]
   contract : [inputType outputType contract]
```

```
from("direct:abc?inputType=java:org.example.Order")
	.to("direct:d?inputType={org.example}xmlOrder;outputType=urn:acme:orderAck")
   	.to("direct:e");
```

Specifying this at the endpoint level is a bit more coarse-grained than the processor approach, but still allows the data types to change within a route.  It does conflate the endpoint URI a bit and introduces a situation where component endpoint configuration could overlap with names of the contract parameters.

3) *Specify contract information at the route level.*

```
<route id="myRoute" inputType="java:org.example.Order" outputType="urn:acme:orderAck">
   <from uri="direct:abc"/>
   <to uri="direct:d"/>
   <to uri="direct:e"/>
</route>
```

This is fairly clean as it does not pollute the routing logic at all, but the approach is very coarse-grained.  Specifying contract metadata at the route level assumes that the type only changes at the edges of the route, which is not the case even in our simple examples above.

####Dynamic Definition
Contract metadata can also be set at runtime through the use of well-known properties or headers which specify the types being used.  The metadata can be set in component code, user code, or both.

Certain components are aware of the types of messages being exchanged based on information present on the wire (e.g. HTTP header) or passed in via endpoint configuration (e.g. WSDL definition).  Each component that is aware of message types would attach relevant message type information to messages as they are processed. 

User code could attach well-known properties/headers through custom processors or beans invoked during a route.  This approach serves as an alternative to the metadata in the configuration and requires no changes to the DSL, endpoint syntax, or Camel schema.  If none of the options for static definition are desirable/achievable, this dynamic approach could be a hacky workaround.

###Additional Considerations
Specification of contract metadata must be optional, allowing for existing Camel applications to continue 'as is' and for people writing new applications to ignore this capability if it doesn't matter to them.

Since this is optional, the runtime and tooling should not fail in the event that contract metadata is absent.  Any features which rely on contract metadata should treat the absence of required information as a NOP instead of a failure.

While the name of a data type can be arbitrary, it may make sense to establish conventions for common types.  In SwitchYard, we use the following:

```
Java =>  java:[fully qualified class name]
XML  =>  {namespace}element-name
```
