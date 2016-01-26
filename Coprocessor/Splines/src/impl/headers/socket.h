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