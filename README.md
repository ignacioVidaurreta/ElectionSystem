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

_Open Elections_
```
./run-management.sh -Daction=open -DserverAddress="localhost"
```

_Submit Votes in /tmp/votes.csv_
```
./run-vote.sh -DvotesPath="/tmp/votes.csv" -DserverAddress="localhost"
```

_Query for votes and save result in /tmp/result.csv_
```
./run-query.sh -DserverAddress="localhost" -DoutPath="/tmp/result.csv"
```


## Authors
* Clara Guzzetti
* Francisco Delgado
* Nicolás Paganini
* Ignacio Vidaurreta