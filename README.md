# ElectionSystem
Distributed election system

## Build
The build process is pretty straight-forward: go to the scripts directory
```
cd scripts
```
And then run the script called `generate_sources.sh`
```
./generate_sources.sh
```

Once this is done, you will need to open three terminals, one for the registry,
one for the server and one for the client. The server and the registry
must be on the same computer.

For the sake of this README we are assuming you are running everything on one computer.
Should this not be the case you just need to replace the `-DserverAddress`
argument with the IP of the server host.

**Note: you should still be standing in the scripts directory on every terminal**

On terminal 1, run the registry:
```
./run-registry
```
On terminal 2, run the server:
```
./run-server
```
Finally, you will use terminal 3 to run the different clients according to the 
instructions given. Example usage:

_Manage election status_
```
./run-management -Daction=[open|close|status] -DserverAddress=<ip>
```

_Submit Votes in the path_
```
./run-vote -DvotesPath=<path_to_csv> -DserverAddress=<ip>
```
  We mostly tested this using /tmp but you can also use relative paths starting from `client/src/main/assembly/overlay`

_Register fiscal_
```
./run-fiscal.sh -DserverAddress=<ip> -Did=<booth> -Dparty=<PartyName>"
```
(Remember that fiscals must be registered before the election is opened)

_Query for votes and save result in outPath_
```
./run-query -DserverAddress=<ip> -DoutPath=<filename> [ -Dstate=<stateName> | -Did=<pollingPlaceNumber> ]
```

## Testing 
### Unit Tests
Run 
```
mvn test
```

### E2E Tests
Run:
```
./integration-tests.sh (vote_bulk|concurrency|fiscal_test) [repetitions]
```

**Important**
For the three tests you need to run on your own terminal the server and the registry.
And for `vote_bulk` and `concurrency` you must also open the elections yourself.


## Authors
* Clara Guzzetti
* Francisco Delgado
* Nicolás Paganini
* Ignacio Vidaurreta
