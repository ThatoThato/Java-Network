Audio downloader:
 a networked client application and server application. This application allows for the transfer of audio files over a network by using the ZEDEM protocol. The
 ZEDEM protocol requires clients to login to the server before other commands can be processed. The server keeps track of the available audio files by storing
 each audio file with its corresponding ID in a text file. ZEDEM runs on port 2021.
 

 File Sharing system:
 A UDP-based peer-to-peer file sharing system which
 consists of a Client that can either send files (Seeder mode) or receive files (Leecher mode).
 Once a client starts, it asks the user for which mode the client will be making use of. The
 user can then continue sending or receiving binary files depending on what mode it is in, until
 the user terminates the connection by closing the client.
