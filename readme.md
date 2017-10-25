# Line Literal

Author: Christopher Wong

[Issues and Project Management](https://waffle.io/pkcwong/line-literal)

## Installation
This section describes the deployment procedure.

- Clone the project.
```bash
git clone https://github.com/pkcwong/line-literal.git
```
- Import the source as a Gradle project.
- Deploy on Heroku.
```bash
git push heroku [branch]:master
```

## Create a Service module
This section describes the procedure on creating new processing logic.

- Create a new class which extends class ```DefaultService```.
- Override methods ```payload``` and ```chain```

```Java
public class SampleService extends DefaultService {
	public SampleService(Service service) {
		super(service);
	}
	@Override
	public void payload() throws Exception {
		// payload
	}
	@Override
	public Service chain() throws Exception {
		// chaining
	}
}
```

## Payload
The ```payload``` method implements the processing service of the module. The method should alter contents in data member ```this.fulfillment```. Additional resolved parameters may also be read and written through methods ```getParams``` and ```setParams```.

## Chaining
The ```chain``` method passes the current state to the next processing module. To pass to the next ```Service``` module, return ```Service.resolve().get()```. To end the processing chain, return ```this```.

## Processing Entry
The entry point of the processing services is defined in ```DefaultService.java```. Change it accordingly.

```Java
@Override
public Service chain() throws Exception {
	return new SampleService(this).resolve().get();
}
```
