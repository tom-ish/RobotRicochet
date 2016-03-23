#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define __USE_GNU 
#include <pthread.h>     /* pthread functions and data structures     */

/*MUTEX & CONDITIONS*/
#ifdef _WIN32
    // Windows (x64 and x86)
    pthread_mutex_t client_mutex = PTHREAD_RECURSIVE_MUTEX_INITIALIZER;
#elif __unix__ // all unices, not all compilers
    // Unix
    pthread_mutex_t client_mutex = PTHREAD_RECURSIVE_MUTEX_INITIALIZER_NP;
#elif __linux__
    // linux
    pthread_mutex_t client_mutex = PTHREAD_RECURSIVE_MUTEX_INITIALIZER_NP;
#elif __APPLE__
    // Mac OS
    pthread_mutex_t client_mutex = PTHREAD_RECURSIVE_MUTEX_INITIALIZER;
#endif

typedef struct client {
    int socket;
    int isConnected; //0 if true
    char *name;
    int score;
    struct client * next;
}client_t;

client_t * clients = NULL;
client_t * last_client = NULL;

int nbClients = 0;
int nbClientsConnecte = 0;

void addClient(int socket, char *name, pthread_mutex_t* p_mutex);
void rmClient(int socket, pthread_mutex_t* p_mutex);
client_t *findClient(int socket, char * name);
void printClientsState(pthread_mutex_t* p_mutex);