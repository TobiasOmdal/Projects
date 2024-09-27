#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include "builtins.h"

#define TOK_BUFFERSIZE 64
#define TOK_DELIMITERS " \n\t\r\a"
#define BUFFERSIZE 512

/*
 * @param
 * @return character buffer
 */
static char* read_shell(void) {
	int buffersize = BUFFERSIZE; 
	int position = 0; 
	char* buffer = malloc(sizeof(char) * buffersize);
	int c; 

	if (!buffer) {
		fprintf(stderr, "Could not allocate input buffer\n"); 
		exit(EXIT_FAILURE);
	}

	while(1) {
		c = getchar(); //Alternatively grap the whole line instead of character by character
		if (c == EOF || c == '\n') { //Check for new line or end of file to null terminate string(line based)
			buffer[position] = '\0';
			return buffer;
		} else {
			buffer[position] = c;
		}
		position++;

		//Exceeds buffersize and needs to reallocate
		if (position >= buffersize) {
			buffersize += BUFFERSIZE;
			buffer = realloc(buffer, buffersize);
			if (!buffer) {
				fprintf(stderr, "Could not reallocate buffer\n");
				exit(EXIT_FAILURE);
			}
		}
	}
}
/*
 * @param stdin line
 * @return tokenized line
 */
static char** parse_line(char* line) {
	int buffersize = TOK_BUFFERSIZE;
	int position = 0; 
	char** tokens = malloc(buffersize * sizeof(char));
	char* token;

	if (!tokens) {
		fprintf(stderr,"Could not allocate tokens\n");
		exit(EXIT_FAILURE);
	}
	
	token = strtok(line, TOK_DELIMITERS); //tokenizes the string on whitespace
	while (token != NULL) {
		tokens[position++] = token;
		if (position >= buffersize) {
			buffersize += TOK_BUFFERSIZE; //increases buffer
			tokens = realloc(tokens, buffersize * sizeof(char*)); //allocates more buffer if exceeded
			if (!tokens) {
				fprintf(stderr, "Could not allocate token\n");
				exit(EXIT_FAILURE);
			}
		}
		token = strtok(NULL, TOK_DELIMITERS); //continues parsing the rest of the tokens
	}
	tokens[position] = NULL;
	return tokens;
}

/*
 * @param args/tokens
 * @return: 1 -> prompt more input, 0 -> exit()
 */
static int exec_cmd(char** args) {
	pid_t pid; 
	pid_t wpid;
	int status, builtin_status;
	
	/*
	 * Checks if input is an builtin command and returns iff it is.
	 * status: 0 -> no args, 1 -> args, -1 -> not builtin
	 */
	if ((builtin_status = builtin_execute(args)) != -1) {
		if (builtin_status == 0) return 0;
		return 1;
	}

	/*
	 * forks new child process, 
	 * checks if pid == child
	 * executes command iff (if and only if)
	 * if pid < 0 something went wrong
	 * if != 0 then its the parent and we have to wait for child
	 */
	pid = fork();
	if (pid == 0) {
		if (execvp(args[0], args) == -1) {
			perror("chad shell");
		}
		exit(EXIT_FAILURE);
	} else if (pid < 0) {
		perror("chad shell");
	} else {
		do {
			wpid = waitpid(pid, &status, WUNTRACED); 
		} while(!WIFEXITED(status) && !WIFSIGNALED(status));
	}
	return 1;
}

/*
 * [Main loop]
 * 1. Reads input from stdin
 * 2. Parses input
 * 3. Executes input 
 */
static void shell_loop(void) {
	char* line; 
	char** args; 
	int status;

	do {
		printf("~ ");
		line = read_shell(); 
		args = parse_line(line);
		status = exec_cmd(args);

		free(line);
		free(args);	
	} while(status);	
}

int main(int argc, char** argv) {
	shell_loop(); 
	return EXIT_SUCCESS; 
}
