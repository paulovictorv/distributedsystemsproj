Ricart-Agrawala's Protocol
======================
Missouri University of Science and Technology
CS384 - Distributed Operating Systems - Programming Assignment

Julio Zynger
Paulo Victor Vieira de Melo
-------------------------------------

To compile and run this project, you will need to download and install the latest versions of:
 - Apache Maven (http://maven.apache.org/)
 - Java Development Kit (http://www.oracle.com/technetwork/java/javase/downloads/index.html)

After setting your environment correctly, run the following steps on the console:
1 - On the project's directory, run the command
    mvn package
    to compile the binary files.
2 - Go into the newly created 'target' folder and run the following command
    java -jar DistOpSysProject-0.0.1-SNAPSHOT-jar-with-dependencies <protocolparameter>
    where <protocolparameter> stands for false if you want to run the common Ricart-Agrawala Protocol or true if you want to run the modified protocol, that allows two processes at once in the critical section.
