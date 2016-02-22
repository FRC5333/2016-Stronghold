#ifndef SOCKET_H
#define SOCKET_H

#ifdef _WIN32
  #ifndef _WIN32_WINNT
    #define _WIN32_WINNT 0x0501
  #endif
  #include <winsock2.h>
  #include <ws2tcpip.h>
  #pragma comment(lib, "@s2_32.lib")
#else
  #include <sys/socket.h>
  #include <arpa/inet.h>
  #include <netdb.h>
  #include <unistd.h>
  #include <string.h>
  
  typedef int SOCKET;
#endif

typedef struct sockaddr_in SocketAddress;

int socket_init(void);
SOCKET socket_create();
SOCKET socket_udp_create();
int socket_connect(SOCKET s, char *host, int port);
int socket_host(char *host, char *ip);
int socket_quit(void);
int socket_close(SOCKET sock);
int hostname_to_ip(char * hostname , char* ip);

#endif