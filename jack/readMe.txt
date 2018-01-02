It is a project based on java, and the version is: java9
The detail version is below:
java version "9"
Java(TM) SE Runtime Environment (build 9+181)
Java HotSpot(TM) 64-Bit Server VM (build 9+181, mixed mode)

Please make sure that your java is new, or there may occur some problem

Run:

First open the terminal:

In linux(16.04) and Mac(10.13) 
1. you can locate to the file where directory "bin" is, then input "cd bin" to get into the bin directory. 
2. then input "java Compiler "+ target_file/target_directory	
   target_file or target_directory is file or directory that you want to compile. 
   for example you can input "Main.jack" as a target_file, or input "10/ArrayTest" as the target_directory, but if you input more than one file or other kind of error, then Assembler will throw this error.
The whole commend will be "java Compiler Main.jack"
   

After doing above, there will be files that names "XXX.vm"(if there is XXX.jack)
--------------------------------------------------------------------------------------------
Warning:
   If you use a target_directory, this project can only find the files that directly in it, but will not recursive find files.
   So, if you have the target_file like this:
target_file
      |
       ---file1.vm
      |
       ---file2.txt
      |
       --- dir
            |
             ---- file3.vm
   The project will only get file1.vm

   If there is some problem when you run the project, you can compile the source code and try again, but get into bin2 rather than bin. The rest operation is same as run part.
-----------------------------------------------------------------------------------------------
Compile
In MacOS environment (test in macOS10.13) are same:
1. open the terminal
2. use "cd" to locate this file, for example if the address of this file in your computer is "/Users/shuaiwang/Desktop/project10", then input cd "/Users/shuaiwang/Desktop/project11"
3. run "compiler_mac.sh". That is, input "./compiler_mac.sh" (If it return "permission denies", input chmod 777 compiler_linux.sh, and turn again)
4. then enter bin, the file Compile.class is the entrance of the program.

In linux environment(test in Ubuntu 16.04) 
1. open the terminal
2. use "cd" to locate this file, for example if the address of this file in your computer is "/Users/shuaiwang/Desktop/project10", then input cd "/Users/shuaiwang/Desktop/project11"
3. run "compiler_linux.sh". That is, input "./compiler_linux.sh" (If it return "permission denies", input chmod 777 compiler_linux.sh, and turn again)
4. then enter bin, the file Compile.class is the entrance of the program.


-----------------------------------------------------------------------------------------------


Function of this project:
1. It can change a jack language file into vm file.

-------------------------------------------------------------------------------------------------

For more detail about the project, see the code and API file.
Thank you!
