It is a project based on java, and the version is: java9
The detail version is below:
java version "9"
Java(TM) SE Runtime Environment (build 9+181)
Java HotSpot(TM) 64-Bit Server VM (build 9+181, mixed mode)

Please make sure that your java is new, or there may occur some problem

Run:

First open the terminal:

In linux(16.04), Mac(10.13) and windows(10) 
1. you can locate to the file where directory "bin" is, then input "cd bin" to get into the bin directory. 
2. then input "java VM_part1 "+ target_file/target_directory	
   target_file or target_directory is file or directory that you want to compile. 
   for example you can input "must.vm" as a target_file, or input "07/MemoryAccess/BasicTest" as the target_directory, but if you input more than one file or other kind of error, then Assembler will throw this error.
The whole commend will be "java VM_part1 must.vm"
   

After doing above, if you input a file, then there will be a file that names "XXX.asm"(if name of target_file is "XXX.vm")
                   But if you input a directory, there will be a new directory "target_directory_out(eg. Input A, build A_out)". In target_directory, there will be all asm files, created according to vm file in target_directory.
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
2. use "cd" to locate this file, for example if the address of this file in your computer is "/Users/shuaiwang/Desktop/project7", then input cd "/Users/shuaiwang/Desktop/project7"
3. run "compiler_mac.sh". That is, input "./compiler_mac.sh" (If it return "permission denies", input chmod 777 compiler_linux.sh, and turn again)
4. then bin2 was created. Enter it, the file VM_part1.java is the entrance of the program.

In linux environment(test in Ubuntu 16.04) 
1. open the terminal
2. use "cd" to locate this file, for example if the address of this file in your computer is "/Users/shuaiwang/Desktop/project7", then input cd "/Users/shuaiwang/Desktop/project7"
3. run "compiler_linux.sh". That is, input "./compiler_linux.sh" (If it return "permission denies", input chmod 777 compiler_linux.sh, and turn again)
4. then bin2 was created. Enter it, the file VM_part1.java is the entrance of the program.

In window environment(test in Window10)
1. open the terminal
2. use "cd" to locate this file, for example if the address of this file in your computer is "C:\Users\shuaiwang\Desktop\project7", then input cd "C:\Users\shuaiwang\Desktop\project7"
3. run "compile.bat". That is, input "compile.bat"
4. then bin2 was created. Enter it, the file VM_part1.java is the entrance of the program.

-----------------------------------------------------------------------------------------------


Function of this project:
1. It can change a vm language file into hack language.
2. It will check if the vm language is reasonable. That is, it contains error detection. If a certain line does not follow the vm language's rule, it will stop the compile and tell you which line it is and the error this line have.

-------------------------------------------------------------------------------------------------

For more detail about the project, see the code and API file.
Thank you!
