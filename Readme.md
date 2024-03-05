# Load Balancer Application

This is a simple load balancer application that distributes incoming requests to a pool of servers. 

## Getting Started
### Running the application
To run the application, you need to have java 17 installed in the machine.
Run the following command to start the application:
```shell
java -jar lb-0.0.1.jar 
```
Application Parameters
- `--server.port` : The port on which the load balancer will listen for incoming requests. Default is 8080
- `--algos` : The CSV list of fully qualified class names of the load balancing algorithms to use. Default is `com.example.lb.algo.RoundRobin`
- `--serverGroupFile` : The file path to the server group file. Default is `server-group.json` . The file holds the config for the routing and server details.


### Extending the Application
#### Adding a new Load Balancing Algorithm
To add a new load balancing algorithm, you need to implement the `com.example.lb.algo.ILbRouting` interface.
The interface has 2 methods:
- `String getAlgorithmName()` - which should return a static name for the algorithm
- `String getServer(String path)` - which should return the server to route the request to. path is the root path for the api call.

#### Server Group Configuration
The server group configuration is a json file that holds the routing and server details. The file should be in the following format:
```json
[
  {
    "rootPath": "profile",
    "routingAlgorithm": "RoundRobin",
    "retryStrategy": {
      "strategy": "Fixed",
      "maxRetries": 2,
      "duration": 1
    },
    "healthCheck" : {
      "path": ""
    },
    "servers": [
      {
        "url": "http://localhost:8081",
        "healthy": true
      },
      {
        "url": "http://localhost:8082",
        "healthy": true
      }
    ]
  }
]
```
- `rootPath` : The root path for the api call. 
- `routingAlgorithm` : The algorithm name of the Routing Algorithm to use for the root path.
- `retryStrategy` : The retry strategy to use when a server is unhealthy. The strategy can be `Fixed` or `Exponential`. 
  - `Fixed` : The maxRetries and duration in seconds are used to determine the number of retries and the duration between retries.
  - `Exponential` : The maxRetries and duration in seconds are used to determine the number of retries and the duration between retries. The duration is exponentially increased for each retry.
- `servers` : The list of servers to route the request to. The url is the server url and the healthy flag is used to determine if the server is healthy or not.
- `healthCheck` : The path to use for the health check. Provide a path for health check.

