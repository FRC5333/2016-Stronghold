#include "socket.h"

typedef struct sockaddr_in SocketAddress;

int socket_init(void) {
  #ifdef _WIN32
    WSADATA wsa_data;
    return WSAStartup(MAKEWORD(1,1), &wsa_data);
  #else
    return 0;
  #endif
}

SOCKET socket_create() {
    SOCKET s;
    s = socket(AF_INET, SOCK_STREAM, 0);
    return s;
}

SOCKET socket_udp_create() {
    SOCKET s;
    s = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    return s;
}

int socket_connect(SOCKET s, char *host, int port) {
    SocketAddress host_addr;
    host_addr.sin_addr.s_addr = inet_addr(host);
    host_addr.sin_family = AF_INET;
    host_addr.sin_port = htons(port);
    return connect(s, (struct sockaddr *)&host_addr, sizeof(host_addr));
}

int socket_host(char *host, char *ip) {
    struct hostent *he;
    he = gethostbyname(host);
    struct in_addr **addr_list;
    if (h_errno != 0) return h_errno;
    addr_list = (struct in_addr **) he->h_addr_list;
    
    int i;
    for (i = 0; addr_list[i] != NULL; i++) {
        strcpy(ip, inet_ntoa(*addr_list[i]));
    }
    return 0;
}

int socket_quit(void) {
  #ifdef _WIN32
    return WSACleanup();
  #else
    return 0;
  #endif
}

int socket_close(SOCKET sock) {
  int status = 0;

  #ifdef _WIN32
    status = shutdown(sock, SD_BOTH);
    if (status == 0) { status = closesocket(sock); }
  #else
    status = shutdown(sock, SHUT_RDWR);
    if (status == 0) { status = close(sock); }
  #endif

  return status;
}

int hostname_to_ip(char * hostname , char* ip) {
    struct hostent *he;
    struct in_addr **addr_list;
    int i;
         
    if ( (he = gethostbyname( hostname ) ) == NULL) {
        herror("gethostbyname");
        return 1;
    }
 
    addr_list = (struct in_addr **) he->h_addr_list;
     
    for(i = 0; addr_list[i] != NULL; i++) {
        strcpy(ip , inet_ntoa(*addr_list[i]) );
        return 0;
    }
     
    return 1;
}