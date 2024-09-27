#ifndef BUILTINS_H
#define BUILTINS_H

#include <unistd.h>
#include <string.h>
#include <stdio.h>

int chad_cd(char** args);
int chad_help(char** args);
int chad_exit(char** args);
int builtin_execute(char** args);
static int chad_num_builtins(void);

//Array of available builtin functions
static const char* builtin_string[] = {
	"cd",
	"help",
	"exit"
};

//Function pointer to builtin functions
static const int (*builtin_func[])(char**) = {
	&chad_cd,
	&chad_help,
	&chad_exit
};
#endif
