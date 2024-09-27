#include "builtins.h"
/*
 * @return total number of builtin functions
 */
static int chad_num_builtins(void) {
	return sizeof(builtin_string) / sizeof(char*);
}

/*
 * @param args/tokens
 * @return 1 -> continue stdin
 * calls chdir to change parent's dir change, not process
 */
int chad_cd(char** args) {
	if (args[1] == NULL) {
		fprintf(stderr, "Could not change directory using \"cd\"");
	} else if (chdir(args[1]) != 0) {
		perror("chad shell");
	}
	return 1;
}

/*
 * @param args/tokens
 * @return 1
 */
int chad_help(char** args) {
	printf("Chad Shell [Tobias Omdal]\n");
	printf("Supports rudimentary UNIX shell commands\n");
	printf("To use, type program names followed by white-space seperated arguments\n");
	printf("Apart from normal commands, the following builtins are also available:\n");

	for (size_t i = 0; i < chad_num_builtins(); ++i) {
		printf("\t%s\n", builtin_string[i]);
	}

	printf("The [man] command will provide information on other programs\n");
	return 1;
}
/*
 * @param args/tokens
 * @return 0 -> signals not to continue reading
 */
int chad_exit(char** exit) {
	return 0;
}

/*
 * @param args/tokens
 * @return -1 -> not valid program, 0/1 -> return values of builtins
 * Compares first argument to the list of builtin functions and executes that 
 * function using the function pointer [builtin_func]
 */
int builtin_execute(char** args) {
	if (args[0] == NULL) {
		return -1;
	}

	for (size_t i = 0; i < chad_num_builtins(); ++i) {
		if (strcmp(args[0], builtin_string[i]) == 0) {
			return (*builtin_func[i])(args);
		}	
	}
	return -1;
}
