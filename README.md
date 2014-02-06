tsp-client
==========

TSA TimeStamp Client

Simple RFC3161 console mode client. 

System requirements
-------------------
Any Java compatible machine and operating system, supported Java versions are 1.5, 1.6 and 1.7 booth 64 and 32 bit version.

Client was tested using 1.6 32/64 Apple JDK and Oracle 1.7 64 bit JDK on Mac OS X 10.9. It should work on most modern operating systems and hardware provided there is a Java instalation

Usage
-----

Linux/Unix clients: run tsp-client.sh or java -jar tsp-client.jar

./tsp-client.sh -h

Usage ./tsp-client.sh -a <algorithm> -s <source-file> -d <directory> -g -q -r -u <url> -h -v
  -a - hash algorithm, can be one of the following MD5 SHA1 SHA256
  -s - file to be hashed
  -d - (optional) output directory to store intermediate files
  -g - (optional) store digest file (<directory>/digest.<algorithm>)
  -q - (optional) store RFC3161 query (<directory>/query.<algorithm>.tsq)
  -r - (optional) store RFC3161 reply (<directory>/reply.<algorithm>.tsr)
  -u - URL to execute RFC3161 request to
  -h - (optional) this help
  -v - (optional) verbosity level, can be V1 or V2, the higher the number more verbose output
  
All other operating systems 

java -jar tsp-client.jar -h
 -a (--alg, --algorithm) [MD5 | SHA1 |  : TSP digest algorithm [MD5, SHA1,
 SHA256]                                : SHA256]
 -c (--cert-req)                        : Set certReq in RFC3161 request to
                                          TRUE.
 -d (--data) VAL                        : Path to file containing data
 -g (--digest) VAL                      : Store digest to this file
 -h (--help, -H)                        : Prints help
 -q (--query) VAL                       : Timestamp Query file (.tsq) path. If
                                          not set it will not be stored.
 -r (--resp) VAL                        : Timestamp Response file (.tsr) path.
                                          If not set it will not be stored.
 -u (--url) VAL                         : TSA URL for used to request Timestamp
                                          token. If not set it will not be used
 -v (--verbosity) [V1 | V2]             : Prints verbose output.
