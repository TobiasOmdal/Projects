# Chad Shell

## Requirements
Uses POSIX standards so the <unistd.h> header is required. 

## Build
```bash
cmake -B build
cmake --build build
```

## Running 
```bash 
cd build/
./chadshell
```

## Example Use
### Running the shell
<div>
	<img src="./documentation/resources/shell_pics/Capture.PNG" alt="image" width=500/>
</div>

### Clearing and listing current directory
<div>
	<img src="./documentation/resources/shell_pics/clear.PNG" alt="image" width=500/>
	<img src="./documentation/resources/shell_pics/ls.PNG" alt="image" width=500/>
</div>

### Changing directory and opening text editor
<div>
	<img src="./documentation/resources/shell_pics/cd.PNG" alt="image" width=500/>
	<img src="./documentation/resources/shell_pics/vim.PNG" alt="image" width=500/>
	<img src="./documentation/resources/shell_pics/vim_inside.PNG" alt="image" width=500/>
</div>

### Exiting the shell
<div>
	<img src="./documentation/resources/shell_pics/exit.PNG" alt="image" width=500/>
</div>
Note: can also just signal an interrupt or kill the process
