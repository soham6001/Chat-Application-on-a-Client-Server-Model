*********************************************************************************************************************************************
*********************************************************************************************************************************************
                                COMPUTER NETWORKS PROJECT ON CHAT APPLICATION IN A CLIENT SERVER MODEL
				Submitted by : Kingshuk Mukherjee(92820942) & Soham Panigrahi(13188847)
*********************************************************************************************************************************************
*********************************************************************************************************************************************

The server is run first and clients can then join in to it upon which they are asked to pick an username.
On joining, the server assigns a numerical value to each client to identify each client uniquely.
Irrespective of the set username the server uses the numerical value assigned to each  client no to send and receive messages and files.

SENDING A MESSAGE
---------------------------------------------------------------------------------------------------------------------------------------------
Broadcast Message Format : message broadcast MESSAGE_CONTENT
Unicast Message Format : message unicast TARGET_CLIENT_NO MESSAGE_CONTENT
Blockcast Message Format : message blockcast BLOCK_CLIENT_NO MESSAGE_CONTENT
---------------------------------------------------------------------------------------------------------------------------------------------

SENDING A FILE
---------------------------------------------------------------------------------------------------------------------------------------------
Broadcast File Format : file FILE_PATH/FILE_NAME broadcast 
Unicast File Format : file FILE_PATH/FILE_NAME unicast TARGET_CLIENT_NO 
Blockcast File Format : file FILE_PATH/FILE_NAME blockcast BLOCK_CLIENT_NO 

File of any format can be zipped and sent over the application to be unzipped at the client end. Incase we choose to send the file without 
unzipping we have to open the file in its respective format for it to work though sending of the the file would work as expected. We can 
send large files over this application. I tested it by transefering one of the computer networks course video lectures of about the size 
of 300 mb over the system
---------------------------------------------------------------------------------------------------------------------------------------------

SEE LIST OF ALL ACTIVE CLIENTS
---------------------------------------------------------------------------------------------------------------------------------------------
Following command from the client side will display all active clients and their numbers:

active

Clients are to use the following feature to keep track of other users who are logged in and those who left the application.

*********************************************************************************************************************************************
Group members : Kingshuk Mukherjee(92820942) & Soham Panigrahi(13188847)
*********************************************************************************************************************************************
Kingshuk Mukherjee(92820942): 
i) Implemented the multi threaded system required for message and file transfer and got the skeleton application working.
ii) Dealt with issues arising due to improper disconnection of client
iii) Implemented the active feature to find the current clients in the system
iv) Tested the application aggressively in the lookout for errors that arise on account of leaks in the implementation and corrected them.
v) Discussion with team mate on design choices that were to be implemented.


Soham Panigrahi(13188847):
i) Implemented separate functions for file broadcast, unicast and blockcast.
ii) Implemented the username feature
iii) Worked on making the client server application robust by implementing proper error handling.
iv) Followed up the implementation with phases of testing to check for improper behaviour in the application and made the changes when necessary.
v) Discussion with team mate on design choices that were to be implemented.
