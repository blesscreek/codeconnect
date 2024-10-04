



# C/C++

## C语言main函数参数

​	C语言规定main函数的参数只能有两个，习惯上这两个参数写为argc和argv。100

​	C语言还规定argc(第一个形参)必须是整型变量，argv(第二个形参)必须是指向字符串的指针数组。

​	所以main函数的函数头应写成

```c
main(int argc,char *argv[])
```

​	由于main函数不能被其他函数调用，因此不可能在程序内部取得实际值，然而，在命令行模式下(如DOS和BASH)，一般形式为

`./可执行文件名 参数 参数`

​	main的两个形参和命令行中的参数在位置上不是一一对应的。argc表示了命令行中参数的个数(**注**：文件名本身也算是一个参数)，argc的值是在输入命令行时由系统按实际参数的个数自动赋予的。

事实上argv[]中最后一个元素恒存放一个空指针，作为argv数组的结束标志。

注：在不同的实现中可以有三个：

```c
main(int argc,char * argv[],char **envp);
```

​	第三个参数envp为环境变量。

## 变长参数

​	变长参数，即函数的参数长度(数量)是可变的，比如C语言的printf系列的(格式化输入输出等)函数，都是参数可变的。

​	在C/C++中，为了通知编译器函数的参数个数和类型是可变的(即是不定的、未知的)，就必须以三个点结束该函数的声明。

​	下面是printf函数的声明：

```c
int printf(const char * format,...)
```

​	在C/C++中，任何使用变长参数声明的函数都必须至少有一个指定的参数(又称强制参数)，即至少有一个参数的类型是已知的，且已知的指定参数必须声明在函数最左端

​	C语言可变参数通过三个宏(**va_start**  **va_end**  **va_arg**)和一个类型(**va_list**)实现。(宏定义在stdarg.h中，不要忘了添加头文件)

- **void va_start**(va_list ap, paramN);

​	参数： ap：可变参数列表地址    paramN:确定的参数

​	功能：初始化可变参数列表(把函数在paramN之后的参数地址放到ap中)

- **void va_end**(va_list ap);

​	参数：ap：可变参数列表地址

​	功能：关闭初始化列表(将ap置空)

- **type va_arg**(va_list ap,type);

​	参数：ap：可变参数列表地址  type：下一个参数的类型

​	功能：返回下一个参数的值

- **va_list**：存储参数的类型信息

​	用va_start获取参数列表(的地址)存储到ap中，用va_arg逐个获取值，最后用va_arg将ap置空

​	示例：

```c
#include <stdio.h>
#include <stdarg.h>

#define END -1

int va_sum (int first_num, ...)
{
    // (1) 定义参数列表
    va_list ap;
    // (2) 初始化参数列表
    va_start(ap, first_num);

    int result = first_num;
    int temp = 0;
    // 获取参数值
    while ((temp = va_arg(ap, int)) != END)
    {
        result += temp;
    }

    // 关闭参数列表
    va_end(ap);

    return result;
}

int main ()
{
    int sum_val = va_sum(1, 2, 3, 4, 5, END);
    printf ("%d", sum_val);
    return 0;
}
```

## 函数调用约定

​	在C语言中，假设有这样的一个函数：

```c
int function(int a,int b)
```

​	调用时只要用result = function(1,2)这样的方式就可以使用这个函数。
​	但是，当高级语言被编译成计算机可以识别的机器码时，有一个问题就凸现出来：在CPU中，计算机没有办法知道一个函数调用需要多少个、什么样的参数，也没有硬件可以保存这些参数。也就是说，计算机不知道怎么给这个函数传递参数，传递参数的工作必须由函数调用者和函数本身来协调。为此，计算机提供了**一种被称为栈的数据结构来支持参数传递**。

函数调用约定，主要解决两个问题：

​	1.当参数个数多于一个时，按照什么顺序把参数压入堆栈

​	2.函数调用后，由谁来把堆栈恢复原状

### 约束事件

参数传递顺序：

**1.从右到左依次入栈**：_stdcall   _cdecl  _thiscall  _fastcall

**2.从左到右依次入栈**：

调用堆栈清理：

**1.调用者清除栈**

**2.被调用函数返回后清除栈**

### 常用描述

#### stdcall(pascal)  

是Standard Call的缩写，C++的标准调用方式

在Microsoft C++系列的C/C++编译器中，常常用PASCAL宏来声明这个调用约定，类似的宏还有WINAPI和CALLBACK

一般WIN32的函数都是_stdcall

声明的语法为：

```c
int _stdcall function(int a, int b)
```

**stdcall**的调用约定意味着：

1.参数从右向左压入堆栈

2.函数自身清理堆栈

3.函数名自动加前导的下划线，后面紧跟一个@符号，其后紧跟着参数的尺寸

#### _cdecl  C (Declaration)

C语言缺省的调用约定

声明的语法为：

```c
int function (int a,int b)//不加修饰就是C调用约定
int _cdecl function(int a,int b)//明确指出C调用约定
```

cdecl调用约定意味着：

1.参数是从右向左压入堆栈

2.调用者负责清理堆栈

3.C调用约定允许函数的参数的个数是不固定的，这也是C语言的一大特色

4.仅在函数名前面加上一个下划线前缀，格式为_functionname

#### fastcall

声明的语法为:

```c
int fastcall function(int a,int b)
```

fastcall调用约定意味着：

1.函数的第一个和第二个DWORD(或者尺寸更小的)通过ecx和edx传递，其他参数通过从右向左的顺序压栈

2.函数自身清理堆栈

3.函数名修改规则同stdcall：函数名自动加前导的下划线，后面紧跟一个@符号，其后紧跟着参数的尺寸

## 以_t为后缀的数据类型

​	size_t是通过typedef声明的 unsigned int类型。对ssize_t来说，size_t前面多加的s代表signed，即ssize_t是通过typedef声明的signed int类型。

​	如size_t和ssize_t这种数据类型，都是**元数据类型(primitive)**，在sys/types.h头文件中一般由typedef声明定义。

​	使用这些数据类型而非基本数据类型的意义是：人们目前普遍认为int是32位的，目前主流操作系统和计算机采用32位，所而在过去16位操作系统时代，int类型是16位的。如果需要修改数据类型时，只需要修改typedef处ssize_t等数据类型的变量即可，大大减少了代码改动。在项目中，一般会大量使用typedef为基本数据类型赋予别名，一般会添加大量typedef声明。而**为了与程序员定义的新数据类型区分，操作系统定义的数据类型会添加后缀-t**。

# gcc

## 程序的编译过程

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/20181222142935800.png)

- 预处理(pre-processing)  -E

编译器将C源代码中的包含的头文件如stdio.h编译进来，替换宏

- 编译(Compiling)  -S

gcc首先要检查代码的规范性、是否由语法错误等，以确定代码的实际要做的工作，在检查无误后，gcc把代码翻译成汇编语言

- 汇编(Assembling)  -c

把编译阶段生成的".s"文件转成二进制目标代码

- 链接(Linking)  什么都不写默认为链接

链接到库中，生成可执行文件

​	这个过程说明未经过链接的.o文件不能被执行，那为什么汇编生成的二进制文件需要链接后才能执行？

​	编译生成.o文件时，它是一个可重定位文件，编译器还不清楚一些外部函数(变量)的地址，当链接器将.o文件链接成为可执行文件时，必须确定那些函数(变量)的性质，如果是静态目标模块提供的按照静态链接的规则，如果是动态共享对象提供的按照动态链接规则。动态链接时，并不是真正的链接，而是对符号的引用标记为动态链接符号，不重定位，在运行时再进行。所以目标文件需要先链接成为可执行文件。动态链接也不是完全在运行时执行链接这个过程，而是链接对符号进行处理，在运行时重定位。

```bash
#一次性完成
gcc hello.c -o hello
```

常用选项：

**E**：激活预处理；头文件、宏等展开(i文件)

**S**：激活预处理、编译；生成汇编代码(.s文件)

**c**：激活预处理、编译、汇编；生成目标文件(.o文件)

**o**：指定输出文件，如果不使用 -o 选项，那么将采用默认的输出文件。例如默认情况下，生成的可执行文件的名字默认为 a.out。

**Wall**：打开编译警告(所有)

**g**：嵌入调试信息，方便gdb调试

**llib**:链接lib库，相当于C++ #pragma comment(lib,"xxx lib)

**Idir**:增加include目录头文件路径

**LDir**：增加lib目录(编译静态库和动态库)

**-std**：声明C或C++的标准版本，如 gcc -std=c89 main.o -o main.exe

**注**：不添加任何参数就是直接将指定文件(不论是.c .i .s .o一步编译成可以执行文件)

```bash
#一次性编译
gcc -Wall fun.c main_fun.c -o main_fun
```

```bash
#独立编译
gcc -Wall -c main_fun.c -o main_fun.o
gcc -Wall -c fun.c -o fun.o
gcc -Wall main_fun.o fun.o -o main_fun
```

## gcc和g++

​	只要是 GCC 支持编译的程序代码，都可以使用 gcc 命令完成编译。可以这样理解，gcc 是 GCC 编译器的通用编译指令，因为根据程序文件的后缀名，gcc 指令可以自行判断出当前程序所用编程语言的类别，比如。

**xxx.c**：默认以编译 C 语言程序的方式编译此文件；

**xxx.cpp**：默认以编译 C++ 程序的方式编译此文件。

**xxx.m**：默认以编译 Objective-C 程序的方式编译此文件；

**xxx.go**：默认以编译 Go 语言程序的方式编译此文件；

​	gcc 指令也为用户提供了“手动指定代表编译方式”的接口，即使用 -x 选项。例如，gcc -xc xxx 表示以编译 C 语言代码的方式编译 xxx 文件；而 gcc -xc++ xxx 则表示以编译 C++ 代码的方式编译 xxx 文件。

C++ 程序都会调用某些标准库中现有的函数或者类对象，而单纯的 gcc 命令是无法自动链接这些标准库文件的

可以这样认为，g++ 指令就等同于`gcc -xc++ -lstdc++ -shared-libgcc`指令。

所以在编译C语言程序时，使用gcc指令，在编译C++程序时，使用g++指令就行了

使用c++11标准：

```bash
g++ -std=c++11
```

# 调试器gdb

对于gcc编译的可运行程序，直接运行方法是./name，发现运行结果和逻辑不一样时就需要用gdb来调试

**若要使用gdb，要在gcc编译时加上-g选项**

用法为：gdb 程序文件名

## 显示和查找程序源代码

list：显示10行代码，若再次运行该命令则显示接下来的10行代码

list 5， 10：显示第5行到第10行的代码

list test.c 5，10：显示源文件test.c中的第5行到第10行的代码，在调试含有多个源文件的程序时使用

list get_sum：显示get_sum函数周围的代码

list test.c:get_sum：显示源文件test.c中get_sum函数周围的代码，在调试含有多个源代码文件的程序时使用

如果要在调试过程中运行linux命令，则可以在gdb的提示符下输入shell命令。

## 设置和管理断点

以行号设置断点:

(gdb)break  7

以函数名设置断点：

(gdb) break get_sum

表达式设置断点，当程序运行中当表达式的值发生改变时程序就会停下来

watch 条件表达式

### 查看当前设置的断点

info breakpoints

Num表示断点的标号，Type指明类型，类型为break points说明是中断，Disp指示断点再生效一次后是否是去作用，dis为是，keep为不是，Enb表明当前当前断点是否有效，是为y，不是为n，Address表示断点所处的内存地址，What列出中断发生在哪个函数的第几行。

### 使断点失效或有效

disable 断点编号可以使该断点失效，enable 断点编号则可以恢复。

### 删除断点

disable只是让某个断点暂时失效，但它扔存在于程序中，想要彻底删除某个断点，可以使用clear或delete命令

clear：删除程序中所有的断点

clear行号：删除此处的断点

clear函数名：删除此函数的断点

delete断点编号：删除指定编号的断点。如果一次要删除多个断点，则需要用空格隔开

### 查看和设置变量的值

#### print命令

print命令一般用来打印变量或表达式的值，使用格式为：

print 变量或表达式：打印变量或表达式当前的值

print 变量=值：对变量进行赋值

print 表达式@要打印的值的个数n：打印以表达式值开始的n个数

#### whatis命令

whatis命令用来显示某个变量或表达式值的数据类型，格式如下：

whatis 变量或表达式

#### set命令

set命令可以用来给变量赋值，使用格式是：

set variable 变量=值

### 控制程序的执行

#### continue命令

让程序继续运行，知道下一个断点或运行完为止，该命令的格式是：

continue

#### kill命令

该命令用于结束当前程序的调试，在gdb提示符下输入kill，gdb会询问是否退出当前程序的调试，输入y为结束，输入n为继续

#### next和step命令

​	如果怀疑程序的错误可能出现在某个地方，可以使用next或step命令一次一条执行该段代码，其区别是，如果遇到函数调用，next会把该函数调用当作一条语句来执行，再次输入next会执行函数调用后的语句，而step则会跟踪进入函数，一次一条地执行函数内的代码，直到函数内的代码执行完，才执行函数调用后的语句。

#### nexti和stepi命令

​	nexti和stepi命令用来单步执行一条机器指令。通常一条语句由多条机器指令构成，nexti和next类似，不会跟踪进入函数内部去执行，stepi和step类似。

# Makefile

​	在linux中，有一个用来维护程序模块关系和生成可执行程序的工具——make，它可以根据程序模块的修改情况重新编译链接生成中间代码或最终的可执行程序。执行该命令需要一个名为"Makefile"或"makefile"的文本文件，这个文件定义了整个项目的编译规则，它定义了模块间的依赖关系，指定文件的编译顺序，以及编译所使用的命令。

## make的一般使用

​	make从Makefile文件中获取模块中的依赖关系，判断哪些文件过时了，根据这些信息make确定哪些文件需要重新编译，然后使用Makefile中的编译命令进行编译。所谓过时，是指一个文件生成后，用来生成该文件的源文件或头文件被修改了，导致生产该文件所需要的源文件或头文件的修改时间比生成该文件的时间晚。

下面是一个简单的Makefile文件：

```makefile
main:main.o module1.o module2.o
	gcc main.o module1.o module2.o -o main
main.o:main.c head1.h head2.h common_head.h
	gcc -c main.c
module1.o:module1.c head1.h
	gcc -c module1.c
module2.o:module2.c head2.h
	gcc -c module2.c
#This is a makefile
```

## Makefile的基本构成

​	Makefile文件的基本单元是规则。一条规则指定一个或多个目标文件，目标文件后面跟的是编译生成该目标文件所依赖的文件或模块，最后是生成或更新目标文件所使用的命令。规则的格式如下：

```
目标文件列表 分隔符  依赖文件列表  [; 命令]
	[命令]
	[命令]
#[]中的内容是可选的
#命令行之间可以插入任意多个空行，空行也要按Tab键开头
```

​	在上面的Makefile文件中，第一、二行就构成了一条规则。目标文件列表中只有一个目标文件即main。main后面的冒号是分隔符(一般分隔符都是冒号)，其他分隔符很少使用。依赖文件列表是main.o module1.o module2.o,也就是为了生存可执行程序main，需要先生成这些依赖文件，这些依赖文件是以.o结尾的，说明他们是一些只经过编译和汇编，没有进行过链接的中间代码。

​	第二行是生存目标文件所使用的指令。如果某一行是指令，那么它必须以一个Tab键开头。如果某一行以Tab开头，make就认为这是一条命令。第一行用于指明模块间的依赖关系，不是命令，因此不以Tab键开头。

在依赖文件列表后面加上一个分号可以跟上命令，如

```makefile
main:main.o module1.o module2.o ;gcc main.o module1.o module2.o -o main
main.o:main.c head1.h head2.h common_head.h
```

### make如何解释执行Makefile

​	make先从第一行开始寻找目标文件，如果没有就去寻找所依赖的文件，也没有的话就跳过编译命令，开始寻找下一个目标文件，如果找到了所依赖的源文件和头文件，则执行编译命令，生成该目标文件，于是再向下，再执行完所有行后，再回溯到第一行。

​	如果仅修改其中的几个文件，make会像之前一样开始检测，如果目标和依赖文件都存在，则检查修改时间，如果目标文件比依赖文件修改时间晚则make认为目标文件是最新的，便没有必要执行命令来生成新的目标文件。从头到尾扫描完一边Makefile文件后，make开始回溯，在第一行时，发现依赖文件修改时间比目标文件晚，则执行编译命令，生成一个新的目标文件

### 说明

​	Makefile也可以命名为makefile，也可以命名为其他任意合法文件名，但如果命名为其他名字，则需要在执行make的时候告知哪个是Makefile文件

```makefile
make -f othername
make --file=othername
```

如果所有文件都不需要更新，make不会执行任何编译操作，他会在屏幕上输出类似以下的提示信息

```bash
make: "main"是最新的
```

Makefile中的符号$有特殊的含义(表示使用变量或调用函数)，在规则中需要使用符号$的地方，需要书写两个连续的$,即$$。

## Makefile文件的构成

一个完整的Makefile文件由5部分构成：显式规则、隐含规则、使用变量、文件指示和注释。

**显示规则**：一条显示规则指明了目标文件、目标文件的依赖文件、生成或更新目标文件所使用的命令。有些规则没有命令，这样的规则只是描述了文件之间的依赖关系

**隐含规则**：由make根据目标文件(典型的是根据文件名的后缀)而自动推导出的规则。make根据目标文件的文件名，自动产生目标的依赖文件和生成目标的命令。例如，在Makefile中有一个规则：

```makefile
module1.o: head1.h
```

make根据目标文件名module1.o的后缀o，自动产生目标的依赖文件module1.c和生成目标所使用的命令gcc -c module1.c -o module.o

因此它等价于：

```makefile
module1.o:module1.c head1.h
	gcc -c module1.c -o module.o
```

因此上面整个Makefile文件可以简写为

```
main:main.o module1.o module2.o
	gcc main.o module1.o module2.o -o main
main.o:head1.h head2.h common_head.h
module1.o:head1.h
module2.o:head1.h
```

**使用变量**：可以使用一个字符串代表一个文本串，当定义了一个变量以后，在Makefile被make解释执行时，其中的字符串都会替换成相应的文本串。Makefile中的变量类似于C语言的宏

**文本指示**：它包括了3个部分，一是在一个Makefile中包含另一个Makefile，就像C语言中的include一样；二是指根据某些情况指定Makefile中的有效部分，就像C语言中的预编译#if一样；三是定义一个多行的命令

**注释**：在Makefile中#字符后的内容被当作注释处理。如果某行以#字符开头，那么此行就是注释行。一般在书写Makefile时建议将注释作为独立的一行。在Makefile中需要使用字符#时，需要用反斜线\加上#来表示，它表示一个字符#而不是注释的开始标志

### 依赖的类型

Makefile中有两种依赖，一种时显示规则中提到的依赖。对于这种依赖类型，依赖文件列表中任何一个文件的时间如果比目标文件新都将导致规则中的命令被执行，从而产生新的目标文件。还有一种依赖：

```makefile
foo : foo.c | somelib
	gcc -o foo foo.c somelib
```

|前面的文件是普通依赖文件，如果foo.c过时，将导致foo重新生成。而如果"|"后面的文件过时，foo不会被重新生成，也就是第二行的命令不会被执行。

### 命令行属性

​	一条规则中可以有一个或多个命令行，每个命令行以Tab键开头。可以在Tab键后先写上一个加号+，减号-或@，任何再写上命令。如果在Tab键后面没有加上这3个符号中的一个，make使用缺省的命令行属性：执行该命令行的命令时打印出命令，命令执行遇到错误就退出make。

符号意义如下：

**-**：执行本命令行的命令时如果遇到错误，继续执行而不推出make

**+**:本行命令始终被执行，即使运行make命令时使用了-n、-q、-t选项(前提时本行命令所在规则的目标文件已经过时，需要执行命令以更新目标文件)

**@**：执行本行命令时不在屏幕上打印命令的内容

例如，在某Makefile文件中有如下规则：

```makefile
file.o:file.c head1.h head2.h
	-mv file.o /tmp
	gcc -c file.c
```

​	如果发现file.o过时，先将过时的file.o文件备份到/tmp下，然后再生成新的，在第一次执行Makefile文件中的命令时，由于file.o不存在，mv命令就会出错，默认情况下一旦出错make就会停止运行，但在mv命令前加上-后，make就会忽略它。

### 伪目标

​	在Makefile文件中，目标文件可以分为两类：实目标文件和伪目标。实目标文件是真正要生成的、以文件的形式存放在硬盘上的目标。伪目标不要求生成实际文件，而是为了让make执行一些辅助命令，如打印一些信息，删除无用的中间文件等。

例：

```makefile
	clean:
	-rm -f *.o
```

​	这里的clean后面没有依赖文件，规则中的命令部分也不是用来生成目标文件的，而是用来删除目录下所有以.o结尾的文件。减号为了使忽略错误继续执行	

​	当make扫描到时，因为clean没有依赖文件，因此总认为它是最新的，从而不会去执行第二行的命令，为了让make执行后面的语句，可以在shell提示符后面使用命令"make clean".

​	如果在当前工作目录下存在一个名为clean的文件，情况就不一样了，由于没有任何依赖文件，所以目标文件被认为是最新的而不去执行规则中的命令，为了解决这个问题，我们需要将目标"clean"声明为伪目标，如下：

```makefile
.PHONY : clean
```

​	这样目标"clean"就被声明为一个伪目标，无论在当前目录下是否存在"clean"这个文件，输入"make clean"后，"rm"命令都会被执行。而且当一个目标被声明为伪目标后，make在执行此规则时不会试图去查找该目标的依赖文件，也提高了make的执行效率，在书写伪目标规则时，首先需要声明目标是一个伪目标，之后才是伪目标的规则定义。如下：

```makefile
.PHONY : clean
clean:
	-rm -f *.o
```

​	在Makefile中，一个伪目标可以有自己的依赖。在一个目录下如果需要生成多个可执行程序，可以在一个Makefile中完成。因为Makefile中第一个目标时最终目标，通常的做法是使用一个称为"all"的伪目标来作为最终目标，它的依赖文件就是那些需要创建的可执行程序，如下：

```makefile
all : program1 program2 program3
.PHONY : all
program1 : program1.c utils.h
	gcc -o program1 program1.c
program2 :program2.c
	gcc -o program2 program2.c
program3 : program3.c comm.h utils.h
	gcc -o program3 program3.c
```

​	执行make时，目标"all"被作为终极目标，为了完成对它的更新，make会生成目标"all"的所有依赖文件(program1、program2、program3)。当需要单独更新某一个程序时，我们可以通过make的命令行选项来指定需要生成的程序。(例如："make program1").

### 特殊目标

在Makefile中，有一些名字(通常以"."开头)，当它们作为规则的目标时，具有特殊含义，常用的有：

**.PHONY**:目标".PHONY"的所有 依赖被作为伪目标。伪目标时这样一个对象：当使用make命令指定此目标时，这个目标所在规则中的命令无论目标文件是否存在都会被无条件执行

**.IGNORE**：对于目标".IGNORE"后面跟的依赖文件，生成这些依赖文件的命令在执行时如果遇到错误，make将忽略所有错误继续执行。它的功能类似命令行属性的减号，当此目标没有依赖文件时，将忽略所有命令执行时的错误

**.SUFFIXES**：该目标的依赖被认为是一个后缀列表，在检查后缀规则时使用

**.SILENT**：对于该目标的依赖文件，执行生成依赖文件的命令时，make不会打印出所执行的命令。如果".SILENT"后面没有依赖文件，则表示执行Makefile中的所有命令都不会打印

例：

```makefile
.SILENT:module2.o
module2.o:module2.c head2.h
	gcc -c module2.c
```

module2.o最为目标".SILENT"的依赖文件。在执行第三行的命令时，不会打印出第三行命令，等价于

```makefile
module2.o:module2.c head2.h
	@gcc -c module2.c
```

**.PRECIOUS**：该目标的依赖文件会受到特别对待。如果make被kill命令终止或遇到意外而被终止，这些依赖并不会被删除，而且如果这些依赖文件是中间文件，在不需要是也不会被删除。

**.INTERMEDIATE**：目标".INTERMEDIATE"的依赖文件在make执行时被当作中间文件对待。如果目标".INTERMEDIATE"后面没有依赖文件，那么该规则时没有意义的。

### 含有多个目标的规则

​	一个规则中可以有多个目标，规则所定义的命令对所有的目标都有效。一个具有多目标的规则相当于多个规则。多目标规则意味着所有的目标具有相同的依赖文件。在规则的命令中可以使用自动变量，从而可以把几条类似的规则归为一条，例：

```makefile
module1.o module2.o module3.o: command.h
#这个规则实现了同时给3个目标文件指定一个依赖文件。由于隐含规则的作用，它等价于3条规则
module1.o:module1.c command.h
	gcc -c module1.c -o module1.h
module2.o:module2.c command.h
	gcc -c module2.c -o module2.h
module1.o:module3.c command.h
	gcc -c module3.c -o module3.h
```

### 搜索目录

​	在多文件编程中，有可能要让make到不同目录去寻找依赖文件，如果文件所在的目录发生变化，可以不用更改Makefile中的规则，只改变依赖文件的**搜索目录**。

​	make可以识别一个特殊变量"VPATH"。通过该变量可以指定依赖文件的搜索目录。当规则的依赖文件当前目录不存在时，make会在此变量指定的目录下去寻找。"VPATH"变量所指定的是Makefile中所有文件的搜索路径，包括了规则的依赖文件和目标文件。

​	定义变量"VPATH"时，使用空格或者冒号(:)将多个需要搜索的目录分开。make按照VPATH中目录的顺序进行搜索(当前目录永远是第一目录)，例：

```makefile
VPATH = /usr/src:../headers
#为所有规则指定了两个搜索路径"/usr/src"  "../headers"
```

​	另一个设置文件搜索目录的方法是使用make的关键字"**vpath**"，它不是变量，和VPATH类似，但它更为灵活。可以 为不同的文件指定不同的搜索目录。使用方法有：

**vpath <pattern> <directories>**

为符合模式的<pattern>的文件指定搜索目录<directories>

**vpath <pattern>**

清除符合模式<pattern>的文件的搜索目录

**vpath**

清除所有已被设置好了的文件搜索目录

​	vpath使用方法中的<pattern>需要包含"%"字符。"%"的意思是匹配零或若干字符，例如，"%.h"表示所有以".h"结尾的文件。<pattern>指定了要搜索的文件集，而<directories>则指定了文件集的搜索目录，例：

```makefile
vpath %.h ../headers
```

该语句表示，要求make在"../headers"目录下搜索所有以".h"结尾的文件(如果某文件在当前目录没有找到的话)

​	可以连续的使用vpath语句，以指定不同搜索策略。如果连续的vpath语句中出现了相同的<pattern>或者是重复了的<pattern>，则make会按照vpath语句的先后顺序来执行搜索，例：

```makefile
vpath %.c src1
vpath % src2
vpath %.c src3
```

它表示已.c结尾的文件，现在"src1"目录找，然后是"src2"，然后是"src3"

```makefile
vpath %.h headers:src
vpath % usr
```

上面的语句则表示".h"结尾的文件，现在"headers"目录找，然后在"src"，最后到"usr"

## 使用变量

​	一般在一定规模的项目中，编写Makefile文件时通常会使用到变量。Makefile中定义变量的一般形式是：

**变量名    赋值符    变量值**

​	变量名习惯上只使用字母、数字和下划线，而且不以数字开头，也可以时其他字符，但不能使用这些字符：":"、"#"、"="和空白符。

​	赋值符主要有4个=、:=、+=、?=。不同的赋值符有不同的意义。变量值是一个文本字符串

​	在含有变量的Makefile中，make执行时把变量名出现的地方用对应的变量值来替换。Makefile中的变量类似于C语言中的宏。Makefile中的变量时区分大小写的，即"var1"和"Var1"是两个不同变量。

​	有一些变量是系统预定义的，如自动变量："$@"、"$?"、"$<"、"$*"等。

### 引用变量

​	在定义了一个变量后，就可以在Makefile中使用这个变量。变量的引用方式是：$(变量名或者${变量名}。例如：$(foo)或者${foo}就是取变量"foo"的值。

​	如果要使用字符，就要用"$$"来表示

### 定义变量

​	在Makefile中，有两种类型的变量：递归展开变量和立即展开变量。通过"="赋值的变量是递归展开变量，通过":="赋值的变量是立即展开变量。

例：

```makefile
foo = $(bar)
bar = $(ugh)
ugh = Huh
all:
 echo $(foo)
```

​	执行make将会打印出Huh。整个变量的替换过程是这样的：在make执行echo时，首先将$(foo)替换成$(bar)，再将$(bar)替换成$(ugh)，再将$(ugh)替换成$(Huh)。整个替换过程实在执行"echo$(foo)时完成的"

​	这种定义的好处是，在变量未定义的时候就可以使用该变量，例如在"foo = $(bar)"中，提前引用了变量bar。如果bar在整个Makefile中都没有定义，则$(bar)的值为空。这种定义的缺点是可能造成死循环。如

```makefile
CFLAGS = $(CFLAGS)-O
#导致了死循环
```

使用赋值符":="赋值的变量是立即展开变量，例：

```makefile
x := foo
y := $(x) bar
x := later
```

它等价于：

```makefile
y := foo bar
x := later
```

这种类型的变量在定义时立即展开，而不是在引用该变量时才展开。例：

```makefile
CFLAGS := $(include_dirs) -O
include_dirs := -ifoo -lbar
```

CFLAGS的值是"-O"，而不是"-lfoo -lbar -O"因为CFLAGS在定义时立即展开，而此时的变量include_dirs还未定义，那么$(include_dirs)的值为空。

Makefile中，还有一个条件赋值符"?="。它被称为条件赋值符是因为：只有此变量在之前没有赋值的情况下才会对这个变量赋值，例：

```makefile
FOO ?= bar
```

含义是，如变量"FOO"之前没有定义，就给它赋值"bar"，否则不改变它的值。

变量赋值实例：

```makefile
objects = main.o foo.o bar.o utils.o
```

这个语句定义了一个变量"objects"，其值为一个.o文件的列表。变量名两边的空格和"="之后的空格在make处理时被忽略。

​	通常，一个变量在定义之后，可以对其值进行追加，这是非常有用的。可以在定义(也可以不定义直接追加)给他赋一个基本值，而后根据需要可随时对其值进行追加(增加它的值)。

在Makefile中使用"+="(追加赋值符)来实现对一个变量值的追加操作。例：

```makefile
objects += another.o
```

这个操作把字符串"another.o"添加到变量"objects"原有值得末尾，并用空格和原有值分开。

### 预定义变量

在Makefile中，预定义了许多变量，可以直接使用。在隐含规则中通常会使用预定义变量

常用的预定义变量如下

|    宏名    | 初始值 |           说明           |
| :--------: | :----: | :----------------------: |
|     CC     |   cc   |     默认使用的编译器     |
|   CFLAGS   |   -o   |     编译器使用的选项     |
|    MAKE    |  make  |         make命令         |
| MAKEFLAGS  |   空   |      make命令的选项      |
|   SHELL    |        |   默认使用的Shell类型    |
|    PWD     |        | 运行make命令时的当前目录 |
|     AR     |   ar   |        库管理命令        |
|  ARFLADS   |  -ruv  |      库管理命令选项      |
| LIBSUFFIXE |   .a   |         库的后缀         |
|     A      |   a    |        库的扩展名        |

例如，假如有一个规则

```makefile
module1.o:module1.c head1.h
	gcc -c module1.c
```

改为

```makefile
moduele1.o:module1.c head1.h
	$(CC) = -c module1.c
```

变量CC是系统预先定义的变量，可以直接使用

使用默认编译器cc来编译moduel1.c，我们可以直接改变预定义变量的值，假设仍然使用gcc来编译程序：

```makefile
module1.o:module1.c head1.h
	$(CC) = gcc
		$(CC) -c module1.c
```

现在这个规则等于第一个规则

Makefile还预定义了一组变量，它们的值在make运行过程中可以动态改变，它们是隐含规则所必需的变量，这类变量称为自动变量。常用的自动变量有： **$@  $%  $<  $>  $?  $^  $+  $***。

**$@**：表示一个规划中的目标文件名。如果目标是一个文档文件(Linux中，一般称.a文件为文档文件，也称为静态库文件)，那么它代表这个文件名。

假如有一个规则：

```makefile
file1.o file2.o: header.h
	cp $@  /backup
```

这条规则的功能是当目标文件过时时，将原来的目标文件备份到/backup目录下，然后重新生成新的目标文件。当需要更新的时file1.o时(此时file1.c比file1.o新)，$@等于file1.o。当需要更新的时file2.o时(此时file2.c比file2.o新)，$@等于file2.o。$@用于指代目标文件名。

这个规则是一个多目标规则。由于隐含规则的作用，它相当于以下两条规则：

```makefile
file1.o : file1.c header.h
	cp $@ /backup
	gcc -c file1.c -o file1.o
file2.o : file2.c header.h
	cp $@ /backup
	gcc -c file2.c -o file2.o
```

**$%**：当规则的目标文件是一个静态库文件时，$%代表静态库的一个成员名。例如，某条规则的目标是"foo.a(bar.o)"，那么"$%"的值就位"bar.o"，而"$@"的值为"foo.a"。如果目标不是静态库文件，$%的值为空。

**$<**：规则中的第一个依赖文件名。但如果规则中使用了隐含规则，那么$<的值是由隐含规则引入的第一个依赖文件名。例：

```makefile
file1.o file2.o header.h
	cp $@ /backup
```

当file1.o过时时，该规则相当于

```makefile
file1.o :file1.c header.h
	cp $@ /backup
	gcc -c file1.c -o file1.o
```

规则中第一个依赖文件名是file1.c，即$<的值是file1.c，而不是header.h。

**$>**：它和$%一样也只适用于库文件，它的值是库名。例如，某条规则的目标是"foo.a(bar.o)"，那么，"$%"的值为"bar.o"，"$@"的值是库名即"foo.a"，"$>"的值也是库名即"foo.a"。如果目标不是静态库文件，$%的值为空。

**$?**：所有比目标文件新的依赖文件列表，以空格分隔。如果目标是静态库文件名，代表的是库成员(.o文件)。

**$^**：规则的所有依赖文件列表，使用空格分隔。如果目标是静态库文件，它所代表的只是所有库成员(.o文件)名。一个文件可重复的出现在目标的依赖中，变量$^只记录它的一次引用情况。也就是说变量"$^"会去掉重复的依赖文件

**$+**：类似"$^"，但是它保留了依赖文件中重复出现的文件。主要用在程序链接时库的交叉引用场合。

**$***：它的值时目标文件去掉后缀后的名称。例如，如果某个规则的目标文件是module1.o，则$*的值时去掉后缀的后的名称即module1.例如：

```makefile
file1.o file2.o: header.h
	gcc -c $*.c -o $@
```

**注意**：以上自动变量只能用在规则的命令中，不能出现在规则的目标文件列表或依赖文件列表中，如果要想让他们出现在目标文件列表或依赖文件列表中，要在它们前加上一个$。例如：$$*可以出现在目标文件列表或依赖文件列表中，它代表目标文件名，其值等同于$**，如：

```makefile
file1.o file2.o: $$*.c header.h
	gcc -c $*.c -o $@
```

运行make，解释执行含有该规则的Makefile文件时，当处理的目标文件为file1.o时，$$*和

$*代表file1；当处理的目标文件为file2.o时

$$*和

$*代表file2.因此它等价于：

```makefile
file1.o : $$*.c header.h
	gcc -c $*.c -o $@
file2.o : $$*.c header.h
	gcc -c $*.c -o $@
```

或者等价于：

```makefile
file1.o : file1.c header.h
	gcc -c file1.c -o file1.o
file2.o : file2.c header.h
	gcc -c file2.c -o file1.o
```

## 隐含规则

隐含规则是make或用户自己预先定义的一些规则。如果Makefile文件中没有显式的给出某个目标文件的依赖文件和编译命令，make会根据目标文件的扩展名使用隐含规则，自动建立关于这个目标文件的依赖文件和编译所使用的命令。

隐含规则的基础是目标文件的扩展名，例如：

```makefile
program : header1.h header2.h
```

那么make会认为program是一个可执行文件，并依据隐含规则建立如下规则：

```makefile
program : program.c header1.h header2.h
	gcc -o program program.c
```

事实上，make处理时并不如此简单，make不仅可以识别C语言的源文件，它也可以识别其他语言，因此隐含规则是很复杂的。

与C语言有关的扩展名有

**.c**：C语言源程序

.h：头文件

.o：目标文件

这里的.o目标文件是指使用编译器gcc 的-c选项生成的中间文件，不是Makefile里介绍的目标文件。

在make处理Makefile文件时，涉及C语言德隐含规则主要有两条：一条是，对于一个没有后缀的目标文件(如上面的program)，如果Makefile文件中没有明确指明对应的依赖文件和编译命令并且可以在当前目录下找到同名.c源文件，make会为它建立一个规则以生成.o目标文件。比如，对于规则：

```makefile
program.o : header1.h header2.h
```

make自动建立一个类似如下方式的规则：

```makefile
program.h : program.c header1.h header2.h
	gcc -c program.c -o program.o
```

对于上面介绍的第一天隐含规则，在make中它表示为

```makefile
$(CC) ${CFLAGS} $@ $<
```

CC是make的预定义变量，它的默认值是cc。CFLAGS也是一个预定义变量，它的默认值是-o。$@指代目标文件，$<指代第一个依赖文件

而第二条，则表示为

```makefile
$(CC) -c $< $@
```

可以通过改变预定义变量的值来改变隐含规则，如：

```makefile
CC =gcc
program.o : header1.h header2.h
```

## 使用条件语句

通过在Makefile中使用条件语句，使得可以根据条件来指定要参与编译的部分，如根据用户所使用的硬件平台来选择适合的库

以下为一个Makefile中条件语句的片段

```makefile
...
libs_for_gcc = -lgnu
normal_libs = 
...
foo : foo.c
ifeq ($(CC) , gcc)
	$(CC) -o foo foo.c $(libs_for_gcc)
else
	$(CC) -o foo foo.c $(normal_libs)
endif
...
```

例子中，条件语句用到了三个关键字："**ifeq**"、"**else**"、"**endif**"。其中，"**ifeq**"表示条件语句的开始，并指定了一个比较条件(相等)。之后是用圆括号包围的，使用逗号分隔的两个参数，圆括号和关键字"**ifeq**"用空格分开。"**ifeq**"之后就是当条件满足，make需要执行的，条件不满足时忽略，"**else**"之后就是当条件不满足时要执行的部分，并不是必要的。"**endif**"表示一个条件语句的结束，任何一个条件表达式都必须以"**endif**"结束。

条件的判断和解析是由make来完成的，上面的例子，一种更简洁的实现方式时：

```makefile
libs_for_gcc = -lgnu
normal_libs = 
ifeq ($(CC),gcc)
libs=$(libs_for_gcc)
else
libs=$(normal_libs)
endif
foo : foo.c
	$(CC) -o foo foo.c $(libs)
```

条件语句还可以使用关键字"**ifneq**"，它代表的意思时"如果两个参数不相等"

此外，还有两个关键字："**ifdef**"和**"ifndef**"

关键字"**ifdef**"用来判断一个变量是否已经被定义，ifndef反之，其使用格式：

```makefile
ifdef 变量名
ifndef 变量名
```

如果变量的值为非空(Makefile中没有定义的变量的值为空)，那么表达式为真，则将其后的语句作为make要执行的一部分。

示例1：

```makefile
bar =
foo = $(bar)
ifdef foo
frobozz = yes
else
frobozz = no
endif
```

示例2：

```makefile
foo = 
ifdef foo
frobozz = yes
else
frobozz = no
endif
```

示例一中的结果是"frobozz = yes",而示例二的结果是"frobozz = no"。其原因在于：示例1中，变量"foo"的比定义是"foo = $(bar)"。虽然变量"bar"的值为空，但是"ifdef"判断的结果是真。当需要判断一个变量的值是否为空时，最好使用"ifeq"(或者"ifneq")而不是"ifdef"。使用ifeq($(foo), )来判断foo是否为空。

## 使用库

大型软件开发项目中，通常把编译好的模块按照功能的不同放在不同的库中。在Linux中，最后链接生成可执行文件时，如果链接的时一般的.o文件，是把整个.o文件的内容插入到可执行文件中。而如果链接的是库，则只从库中找出程序需要的变量和函数，把它们装入到可执行文件中，使用库可以节省空间，所以系统提供的标准函数一般都是以库的形式提供。

- 库中成员是.o(目标文件)的形式，是因为使用了gcc -c ，-c是阻止创建一个完整的程序，因为其中不包含main函数。

库中的文件一般被称为库的成员，成员的表示形式为：

库名(成员名)

其中成员名一般就是文件名，例如

```makefile
mylib.a (file.o)
```

表示静态库mylib.a(以so结尾的是动态库)中有一个名为file.o的文件。静态库也被称为文档文件，它是一些.o结尾的文件，通常使用ar命令对他进行维护和管理。

建立和维护一个库时，将库名作为目标文件，把希望放到库中的文件作为依赖文件，其格式为：

```makefile
库名: 库名(成员1) 库名(成员2) ...
#或者
库名: 库名(成员1 成员2 ...)
```

例如：

```bash
mylib.a: mylib.a(file1.o) mylib.a(file2.o) mylib.a(file3.o)
#或者
mylib.a: mylib.a(file1.o file2.o file3.o)
```

接着，就可以输入命令了，一般为：

```bash
ar -ruv 库名 目标文件名
```

例如，下面将file1.o file2.o file3.o三个文件加入到库mylib中：

```bash
mylib:mylib(file1.o)
	gcc -c file1.c
	ar -ruv mylib file1.o
	rm -f file1.o
mylib:mylib(file2.o)
	gcc- c file1.c
	ar -ruv mylib file2.o
	...
```

上面规则中，没有给出库mylib的后缀a，但make通过它的依赖文件(依赖文件放在一个圆括号中)可以识别出mylib时一个库而不是一个可执行文件或其他类型的目标。通过使用自动变量可以将上面的规则简化为：

```makefile
mylib:mylib(file1.o file2.o file3.o)
	ar -ruv $@ $?
	rm -f $?
file1.o:file1.c
	gcc -c file1.c
file2.o:file2.c
	gcc -c file2.c
file3.o:file3.c
	gcc -c file3.c
```

$?指代所有比目标文件新的依赖文件列表。还可以利用隐含规则简化为：

```makefile
mylib:mylib(file1.o file2.o file3.o)
	ar -ruv $@ $?
	rm -f $?
```

make在建立库mylib时，搜索它的依赖文件file1.o  file2.o  file3.o，如果当前目录下没有这些依赖文件或者这些依赖文件的依赖文件(即相应的.c源文件)更新，make会按照隐含规则生成或重新生成库的各个依赖文件，因此可以简化为上面3行语句。

事实上，make也建立了相应的针对库操作的隐含规则，隐含规则类似于：

```makefile
$(AR) $(ARFLAGS) $@ $?
rm -f $
```

有了隐含规则，可以将上面的规则进一步简化为

```makefile
mylib:mylib(file1.o file2.o file3.o)
```

make在处理这个语句时，，先检查库mylib的各个依赖文件。从依赖文件列表中的file1.o开始，如果成员文件file1.o的建立时间晚于库的建立时间，就使用针对库操作的隐含规则更新库mylib。如果file1.o的建立时间早于相应的.c源文件，即file1.o过时，那么使用隐含规则生成新的file1.o文件，再使用针对库操作的隐含规则更新库mylib。第一个成员文件处理完毕，接着处理file2.o，知道处理完所有依赖文件。

## make命令参数

**-C dir 或者 --directory=DIR**

在读取Makefile文件前，先切换到"dir"目录下，即把dir作为目前目录。如果存在多个-C选项，make的最终当前目录时第一个目录的相对路径，如"make -C/home/root -C src"，等价于"make -C /home/root/src"

**-d**

make执行时打印出所有的调试信息。包括：make认为那些需要重新生成的文件；那些需要比较它们的最后修改时间的文件，比较的结果；重新生成目标所要执行的命令；使用的隐含规则等。

**-e 或者  --environment-overrides**

不允许在Makefile中对系统环境变量进行重新赋值

**-f filename 或者 --file=FILE 或者 --makefile= FILE**

使用指定文件作为Makefile文件

**-i 或者  --ignore-errors**

忽略执行Makefile中命令时产生的错误，不退出make。

**-h 或者 -help**

打印出帮助信息。

**-k或者 --keep-going**

执行命令遇到错误时不终止make的执行，make尽最大可能执行所有的命令，直到出现致命错误才终止

**-n或者--just-print或者 --dry-run**

只打印出要执行的命令，但不执行命令

**-o filename或者--old-file=FILE**

指定文件"filename"不需要重建，即使相较于它的依赖已经过时，同时也不重建依赖于此文件的任何目标文件

**-p或者  --print-data-base**

命令执行之前，打印出make读取的Makefile的所有数据(包括规则和变量的值)，同时打印出make的版本信息。如果只要打印这些数据信息而不执行命令，可以使用"make -qp"命令。查看make执行前的隐含规则和预定义变量，可使用命令"make -p-f /dev/null"

**-q 或者 -question**

称为"询问模式"，不执行任何命令。make只是返回一个查询状态值，返回的状态值为0表示没有目标需要重建，1表示存在需要重建的目标，2表示有错误发生。

**-r 或者 --no-builtin-rules**

忽略隐含规则，使之不起作用，该选项不会取消make内嵌的预定义变量

**-R 或者 --no-builtin-variabes**

取消make内嵌的预定义变量，不过我们在Makefile中明确定义某些变量。**注**：-R选项同时打开-r选项，因为没有了预定义变量，隐含规则将失去意义(隐含规则是以内嵌的预定义变量为基础的)

**-s 或者 -silent**

执行但不显示所执行的命令

**-t或者-touch**

把所有目标文件的最后修改时间设置为当前系统时间

**-v或者-version**

打印出make的版本信息

# 系统编程

## 获取帮助

在Shell命令行下输入man命令可以查看函数原型。例如输入man lseek可以获取lseek函数的原型和所属头文件。有些函数名比如mkdir既是Linux命令，也是系统调用，这时可以输入man 2 mkdir获取该函数的原型，只输入man mkdir只是命令mkdir的帮助信息。对于库函数，输入man 3 <库函数名>可以获得帮助信息 例如 man 3 opendir

## 文件描述符

### 引入

​	在C语言中，打开文件调用的是C语言的库函数接口，如fopen、flcose、fputs、fgets，在不允许使用库函数接口时，就要使用系统调用相关接口。

- C语言接口和操作系统接口是上下级关系

​	任何一个语言(C C++ java Python)都有自己打开文件关闭文件读写文件的库函数，但这些库函数的使用都是在Linux和Windows系统下进行的，所以任何语言的接口和系统接口都是一种上下级关系，即语言的库文件接口是上层，系统调用接口是底层。

​	可以理解为C中调用的库函数底层封装了系统调用接口，即fopen底层调用open，flcose底层调用close，fread底层调用read，fwrite底层调用write。

​	在Windows中打开文件，windows底层也有一套自己的windows相关的api系统接口，当在windows环境下使用C的库函数时，C调用的就是windows下的系统接口，这样就在语言层面上实现了跨平台性。

​	在C中打开文件，库函数的类型为FILE*，而系统调用接口的类型为int。	

​	FILE*是文件指针，在C中打开一个文件，打开成功后会返回一个文件指针，该指针指向文件内容的起始地址，文件指针是C语言级别的概念；int fd本质是new file descriptor——文件描述符，是系统级别的概念。

文件描述符是一个非负的**索引值**(一般从3开始，0、1、2已经被使用)，指向内核中的"文件记录表"。(如果文件打开失败的话则为-1)

- 当打开一个现存文件或创建一个新文件时，内核就向进程返回一个**文件描述符**（内核记录表某一栏的索引）；
- 当需要读写文件时，也需要把文件描述符作为参数传递给相应的函数。
- Linux 下所有对设备和文件的操作都使用**文件描述符**来进行。

​	C语言程序会默认打开3个输入输出流，其中这三个输入输出流对应的名称为stdin  stdout  stderr，文件类型为FILE*，底层对应着文件描述符。**所谓的文件描述符，本质其实就是数组下标**

### 常见的文件描述符类型

一个进程启动时，会默认打开三个文件-标准输入、标准输出和标准出错处理

0：表示标准输入，对应宏为：STDIN_FILENO，函数 scanf() 使用的是标准输入；
1：表示标准输出，对应宏为：STDOUT_FILENO， 函数 printf() 使用的是标准输出；
2：表示标准出错处理，对应的宏为：STDERR_NO；
也可以使用函数 fscanf() 和 fprintf() 使用不同的 文件描述符 重定向进程的 I/O 到不同的文件

### 需要使用文件描述符的函数

若要访问文件，而且调用的函数又是write、read、open和close时，就必须用到**文件描述符**(一般从3开始)

若调用的函数为fwrite、fread、fopen和fclose时，就可以绕过直接控制**文件描述符**，使用的则是与**文件描述符**对应的**文件流**

### 底层原理

​	底层中描述文件的数据结构叫做struct file，一个文件对应一个struct file，大量的文件则需要大量的struct file，将这些数据结构用双链表链接起来，所以对文件的管理就变成了对双链表的增删改查。这些已经被打开的文件属于某个特定的进程，这就需要建立进程和文件的对应关系。

​	每一个**进程**都有一个task_struct，这个task_struct会指向一个struct files_struct结构体，这个结构体里会有一个指针数组struct file* fd_array[32]，而这个指针数组就是文件描述符对应的数组。

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/323bb0430bde42359decae96a5abedc6.png)

​	既然是数组就有下标，下标从0开始依次递增，task_struct结构里会有一个指针变量指向这个struct file_struct结构体，这个指针数组里的每个数据都是一个指针变量。默认的3个被打开的文件，每个都对应一个struct file结构体，结构体里有描述该文件属性的相关信息，而这些struct文件结构体之间，是通过双链表的形式链接起来的。

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/e9b3c520ced94c849a8d578fab9d5b4b.png)

​	对于输入、输出、错误，将下表012分配给他们，自己打开的文件从3开始分配，当我们将下标和struct file结构体的指向关系表明清楚后，open函数返回时，就会将下标数字直接返回给调用方，至此，在应用层我们就拿到了文件描述符，完成了文件和进程的对应关系。所以，**所谓的文件描述符实际就是数组的下标**。

### STDIN_FILENO和stdin的区别

STDIN_FILENO属于系统API接口库，声明类型为int型，是一个打开文件句柄，对应的函数主要包括open/read/write/close等**系统等级调用**。

操作系统一级提供的文件都是以文件描述符来表示文件。STDIN_FILENO就是标准输入设备(一般是键盘)的文件描述符。

而stdin的类型为FILE*，使用stdin的函数主要有：fread、fwtrite、fclose等，基本都以f开头

stdin等属于标准I/O(标准库处理的输入流)，高级的输入输出函数。在<stdio.h>。

STDIN_FILENO等是文件描述符，属于没有buffer的I/O，直接调用系统调用，在<unistd.h>

标准库内封装了系统API调用，如fread内部实现调用read。

## Linux的文件结构

### 文件的访问权限控制

#### chmod函数

在Shell下输入man 2 chmod可查看chmod/fchmod函数对文件访问权限进行修改，在Shell下输入man 2 chmod可查看chmod/fchmod的函数原型如下

```c
#include <sys/types.h>
#include <sys/stat.h>
int chmod(const char *path,mode_t mode);
int fchmod(int fildes,mode_t mode);
```

参数mode的参数如下：

**S_IRWXU** 00700所有者拥有读、写和运行权限

**S_IRUSR**  00400所有者拥有读权限

**S_IWUSR** 00200所有者拥有写权限

**S_IXUSR** 00100所有者拥有执行权限

**S_IRWXG** 00070群组拥有读、写和运行权限

**S_IRGRP** 00040群组拥有读权限

...

**S_IRWXO** 00007其他用户拥有所有权限

**S_IROTH** 00004其他用户拥有读权限

...

chmod和fchmod的区别在于chmod以文件名作为第一个参数，fchmod以文件描述符作为第一个参数。

## 文件的输入输出

### 文件的创建、打开与关闭

#### open函数

open系统调用用来打开或创建一个文件，在Shell下输入"man open"可获取函数原型：

```c
#include <sys/types.h>
#incldue <sys/stat.h>
#include<fcntl.h>
int open(const char *pathname,int flags);
int open(const char *pathname,int flags, mode_t mode);
```

其中第一个参数pathname是要打开或创建的含路径的文件名，第二个参数flags表示打开文件的方式。

**O_RDONLY**：以只读方式打开文件

**O_WRONGLY**：以只写方式打开文件

**O_RDWR**：以只读只写的方式打开文件

这三种打开方式是互斥的，不能同时以两种或3种方式打开文件，但是它们可以分别于下列标志进行或运算。

**O_CREAT**：若文件不存在则自动建立该文件，**只有在此时**，才需要用到第三个参数mode，以说明新文件的存取权限。

**O_EXCL**：如果**O_CREAT**也被设置，此指令去检查文件是否存在。文件若不存在则创建该文件，若文件已存在则将导致打开文件出错。

**O_TRUNC**：若文件存在并且以可写的方式打开时，此标志会将文件长度清为0，即原文件中保存的数据将丢失，但文件的属性不变。

**O_APPEND**：所写入的数据会以追加的方式加入到文件后面。

**O_ASYNC**：以同步的方式打开文件，任何对文件的修改都会阻塞直到物理磁盘上的数据同步后才返回。

**O_NOFOLLOW**：如果参数pathname所指的文件为一符号连接，则会令打开文件失败，Linux内核版本在2.1.126以上才有这个标志。

**O_DIRECTORY**：如果参数pathname所指的文件并非为一目录，则会打开文件失败，Linux内核版本在2.1.126以上才会有这个标准。

**O_NONBLOCK或O_NDELAY**：以非阻塞的方式打开文件，对于open及随后的对该文件的操作，都会立即返回。

当且仅当第二参数使用了**O_CREAT**时，需要使用第三个参数mode，以说明新文件的存取权限。新文件的实际存取权限时mode于umask按照(mode**^~**umask)(异或再取反)运算后的结果。例如，umask为045，mode为0740，则新建立的文件实际存取权限是0700，即-rwx------。

参数mode的使用方法和chmod相同。

成功调用open函数会返回一个文件描述符，若有错误发生则返回-1，并把错误代码赋给errno。

#### creat函数

文件的创建可以通过creat系统调用来完成，在Shell下输入"man 2 creat"可以获取函数原型：

```c
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
int creat(const char *pathname,mode_t mode)
```

第一个参数是要打开或创建的文件名如果pathname指向的文件不存在，则创建一个新文件；如果pathname指向的文件存在，则原文件将被新文件覆盖。第二个参数mode与open函数相同，为指定该文件的权限。creat()相当于这样使用open()：

```c
open(const char * pathname,(O_CREAT|O_WRONGLY|O_TRUNC));
```

成功调用creat会返回一个文件描述符，若有错误发生则会返回-1，并把错误代码付给errno。

**注**：creat只能以只写的方式打开创建的文件，creat无法创建设备文件，设备文件的创建要使用mknod函数

#### close函数

close系统调用用来关闭一个已经打开的文件，在Shell下输入"man 2 close"可获取其函数原型为：

```c
#include<unistd.h>
	int close(int fd);
```

close函数只有一个参数，此参数表示需要关闭的文件的文件描述符。该文件描述符是由open或creat函数得到的。当close调用成功时，返回值为0，发生错误时返回-1并设置错误代码。

**注**：close函数调用成功时并不保证数据能全部写回硬盘。

用户程序也可以不必调用close函数关闭已打开的文件，因为在进程结束时，内核会自动关闭所有已打开的文件，但这不是一个良好的习惯，建议在程序中显式地调用close函数。

my_create.c

```c
#include<stdio.h>
#include<sys/types.h>
#include<sys/stat.h>
#include<fcntl.h>
#include<unistd.h>
#include<errno.h>

int main()
{
	int fd;
	
	if((fd = open("example.c",O_CREAT|O_EXCL, S_IRUSR| S_IWUSR))== -1))
    {
        perror("open");
        exit(1);
    }
    else
    {
        printf("create file success\n");
    }
    close(fd);
}
```

该程序使用open系统调用在当前目录下创建一个名为example.c的文件，且新文件的存取权限为所有者可读可写。

第一次执行该程序成功创建了文件，再次执行时，则会出错

```bash
./mycreate
open: File exists
```

这是因为在调用open时，同时设置了O_CREAT和O_EXCL标志，则当文件存在时，open调用失败，系统将错误代码设置成EEXIST，表示文件已存在。

### 文件的读写

#### read函数

read系统调用用来从打开的文件中读取数据，在Shell下输入"man 2 read"可获取其函数原型如下

```c
#include <unistd.h>
ssize_t read(int fd,void *buf,size_t count);
```

函数中各参数的含义是：从文件描述符fd所指向的文件中读取count个字节的数据到buf所指向的缓存中。若参数count为0，则read()不会读取数据，只返回0.返回值表示实际读取到的字节数，如果返回0，表示已到达文件末尾或是无可读取的数据，此外文件读写指针会随读取到的字节移动。如果read()顺序返回实际读到的字节数，最好能将返回值与参数count作比较，若返回的字节数比要求的字节数少，则有可能读到了文件尾或者read()被信号中断了读取过程，或者是其他原因。当有错误发生的时候，返回-1，错误代码存入errno中。

#### write函数

write函数调用用来将数据写入已打开的文件中，在Shell下输入"man 2 write"可获取该函数原型：

```c
#include <unistd.h>
ssize_t write(int fd,const void *buf,size_t count);
```

函数中各参数意义是：将buf所指向的缓冲区的count个字节数据写入到由文件描述符fd所指示的文件中。当然，文件读写指针也会随之移动。如果调用成功，write()会返回写入的字节数。当有错误发生则返回-1，错误代码存入errno中。

### 文件读写指针的移动

#### lseek

lssek系统调用用来移动文件读写指针的位置，在Shell下输入"man 2 lseeek"可获取其原型：

```c
#include<sys/types.h>
#include<unistd.h>
off_t lseek(int fildes,off_t offset,int whence);
```

每一个已打开的文件都有一个读写位置，当打开文件时通常读写位置时指向文件开头，若是以添加的方式打开文件(调用open函数时使用了O_APPEND)，则读写位置会指向文件尾。当调用read()或write()时，读写位置会随之增加，lseek()便用来控制该文件的读写位置。参数flides为已打开的文件描述符，参数offset为根据参数whence来移动读写位置的位移数。

**参数whence有以下3种取值。**

**SEEK_SET** 从文件开始处计算偏移量，文件指针到文件开始处的距离为offset。

**SEEK_CUR**从文件指针的当前位置开始计算偏移量，文件指针值等于当前指针值加上offset的值，offset值允许取负数。

**SEEK_END**从文件结尾处开始计算偏移量，文件指针值等于当前指针值加上offset的值，offset值允许取负数。

由于历史原因，新旧值对应如图：

|  旧值  |   新值   |
| :----: | :------: |
|   0    | SEEK_SET |
|   1    | SEEK_CUR |
|   2    | SEEK_END |
| L_SET  | SEEK_SET |
| L_INCR | SEEK_CUR |
| L_XTND | SEEK_END |

lseek允许文件指针的值设置到文件结束符(EOF)之后，但这样做并不改变文件的大小。如果使用write对EOF之后的位置写入了数据，之前的EOF处与后面写入的数据之间将会存在一个间隔，此时，如果使用read读取这个间隔的数据，得到的数据为0.

当调用成功时返回当前的读写位置，也就是距离文件开始处多少个字节。若有错误则返回-1，errno会存放错误代码。

以下是lseek的几种常用方法：

将文件读写指针移动到文件开头：

```c
lseek(int fildes ,0,SEEK_SET);
```

将文件读写指针移动到文件结尾：

```c
lseek(int fildes,0,SEEK_END);
```

获取文件读写指针当前的位置(相对于文件开头的偏离)：

```c
lseek(int fildes,0,SEEK_CUR);
```

**注：**有些设备(或者说设备文件)不能使用lseek，Linux系统不允许lseek()对tty设备进行操作，此项操作会使用lseek()返回错误代码ESPIPE。

文件读写和文件读写指针的操作移动过程：

```c
#include<stdio.h>
#include<sys/types.h>
#include<sys/stat.h>
#include<fcntl.h>
#include<unistd.h>
#include<errno.h>
//自定义的错误处理函数
void my_err(const char * err_string,int line)
{
    fprintf(stderr,"line:%d ",line);
    perror(err_string);
    exit(1);
}

//自定义的读数据函数
int my_read(int fd)
{
    int len;
    int ret;
    int i;
    char read_buf[64];
    
    //获取文件长度并保持文件读写指针在文件开始处
    if(lseek(fd,0,SEEK_END)==-1)
    {
        my_err("lseek",__LINE__);
    }
    if((len = lseek(fd,0,SEEK_CUR)) == -1)
    {
        my_err("lseek",__LINE__);
    }
    if((lseek(fd,0,SEEK_SET)) == -1)
    {
        my_err("lseek", __LINE__);
    }
    
    printf("len:%d\n",len);
    
    if((ret = read(fd, read_buf, len)) < 0)
    {
        my_err=("read",__LINE__);
    }
    printf("\n");
    return ret;
} 
int main()
{
    int fd;
    char write_buf[32] = "Hello World!";
    //在当前目录下创建文件example.c
    if((fd = open("example.c", O_RDWR|O_CREAT|O_TRUNC, S_IRWXU)) == -1)
    {
        my_err("open", __LINE__);
    }
    else
    {
        printf("create file success\n");
    }
    
    if(write(fd,write_buf,strlen(write_buf))!= strlen(write_buf))
    {
        my_err("write",__LINE__);
    }
    my_read(fd);
    
    printf("/*————————————————*/\n");
    if(lseek(fd,10,SEEK_END)==-1)
    {
        my_err("lseek",__LINE__);
    }
    if(write(fd,write_buf,strlen(write_buf)!=strlen(write_buf))
       {
           my_err("write",__LINE__);
       }
       my_read(fd);
       
       close(fd);
       return 0;
}
```

程序中首先在当前目录下创建了文件example.c，open的参数为O_RDWR|O_CREAT|O_TRUNC表示以可读可写方式创建一个文件，若该文件名存在，则覆盖，然后使用write系统调用，向新创建的文件内写入数据。函数my_read先测试由参数fd传入的文件描述符对应的文件长度，然后读出全部数据，最后打印出阿里。

```
__LINE__是预编译器内置宏，表示行数，类似的宏还有_TIME_v   _FUNCTION  _FILE_等，分别表示时间、函数名。文件名，程序调试时在适当位置加入这些提示，方便定位错误
```

### dup、dup2、fcntl、ioctl系统调用

#### dup和dup2函数

dup和dup2系统调用都可以用来复制文件描述符，在Shell下输入"man 2 dup"可获取它们的函数原型如下：

```c
#include <unistd.h>
int dup(int oldfd);
int dup2(int oldfd, int newfd);
```

​	dup用来复制参数oldfd所指的文件描述符。当复制成功时，返回最小的尚未被使用的文件描述符。若有错误则返回1，错误代码存入errno中。返回的新文件描述符和参数oldfd指向同一个文件，共享所有的锁定、读写指针和各项权限或标志位。例如，当利用lseek()对某个文件描述符操作时，另一个文件描述符的读写位置也会随着改变。

​	dup2与dup的区别是dup2可以用参数newfd制定新文件描述符的数值。若newfd已经被程序使用，系统就会将其关闭以释放该文件描述符；若oldfd与newfd相等，则dup2返回newfd，而不关闭它。dup2调用成功，返回新的描述符，出错返回-1。

​	Shell中的重定向功能(输入重定向"<"和输出重定向">")就是通过dup或dup2函数对标准输入和标准输出的操作来实现的。

#### fcntl函数

fcntl即file control

fcntl系统调用可以用来对已打开的文件描述符进行各种控制操作以改变已打开文件的各种属性，在Shell下输入"man fcntl"可获取其原型如下

```c
#include<unistd.h>
#include<fcntl.h>
int fcntl(int fd,int cmd);
int fcntl(int fd,int cmd,long arg);
int fcntl(int fd,int cmd,struct flock *lock);
```

fcntl()针对(文件)描述符提供控制。参数fd是被参数cmd操作的描述符。针对cmd的值fcntl能接受第三个参数。

fcntl函数有5种功能：

- 复制一个现有的描述符(cmd = F_DUPFD)
- 获得/设置文件描述符标记(cmd = F_GETFD或F_SETFD)
- 获得/设置文件状态标记(cmd=F_GETFL或F_SETFL)
- 获得/设置异步I/O所有权(cmd= F_GETOWN或F_SETOWN)
- 获得/设置记录锁(cmd = F_GETLK，F_SETLK或F_SETLKW)

cmd选项：

**F_DUPFD**

返回一个如下描述的(文件)描述符：

1.最小的大于或等于arg的一个可用的描述符

2.与原始操作符一样的某对象的引用

3.如果对象是文件(file)的话，返回一个新的描述符，这个描述符与arg共享相同的偏移量(offset)]

4.相同的访问模式(读，写或读/写)

5.相同的文字状态标志(如：两个文件描述符共享相同的状态标志)

6.与新的文件描述符结合在一起的close-on-exec标志被设置成交叉式访问execve(2)的系统调用

**F_GETFD**

此时，fcntl用来获取文件描述符的close-on-exec标志。调用成功返回标志值，若此标志值的最后一位是0，则该标志没有设置，即意味着在执行exec相关函数后文件描述符仍保持打开。否则在执行exec相关函数时将关闭该文件描述符。失败返回-1。

**F_SETFD**

此时，fcntl用来设置文件描述符的close-on-exec标志为第三个参数arg的最后一位。成功返回0，失败返回-1。

**F_GETFL**

此时，fcntl用来获得文件打开的方式。成功返回标志值，失败返回-1.标志值的含义同open系统调用一致。

**F_SETFL**

此时，fcntl用来设置文件打开的方式为第三个参数arg指定的方式。但是Linux系统只能设置O_APPEND、O_NONBLOCK、O_ASYNC标志，也和open系统调用一致。

fcntl_access.c

```c
#include<stdio.h>
#include<unistd.h>
#include<fcntl.h>
#include<sys/types.h>
#include<sys/stat.h>

//自定义的错误处理函数
void my_err(const char *err_string,int line)
{
    fprintf(stderr,"line:%d ", line);
    perror(err_string);
    exit(1);
}

int main()
{
    int ret;
    int access_mode;
    int fd;
    if((fd = open("example", O_CREAT|O_TRUNC|O_RDWR,S_IRWXU)) == -1)
    {
        my_err("open,__LINE__");
    }
    //设置文件打开方式
    if((ret = fcntl(fd,F_SETFL, O_APPEND)) < 0)
    {
        my_err("fcntl",__LINE__);
    }
    //获取文件打开方式
    if((ret = fcntl(fd, F_GETFL, 0)) < 0)
    {
        my_err("fcntl", __LINE__);
    }
    access_mode = ret& O_ACCMODE;//O_ACCMODE是取得文件打开方式的掩码，实际上它的值就是3，做与运算为了取得ret的最后两位的值
    if(access_mode == O_RDONLY)
    {
        printf("example access mode: read only");
    }
    else if(access_mdoe == O_WRONLY)
    {
        printf("example access mode: write only");
    }
    else if(access_mode == O_RDWR)
    {
        printf("example access mode:read+write");
    }
    
    if(ret & O_APPEND)
    {
        printf(" ,append");
    }
    if(ret & O_NONBLOCK)
    {
        printf(" ,nonblock");
    }
    if(ret & O_SYNC)
    {
        printf(" ,sync");
    }
    printf("\n");
    return 0;
}
```

##### 文件记录锁

​	当有多个进程同时对某一文件进行操作时，就有可能发生数据的不同步，从而引起错误，该文件的最后状态取决于写该文件的最后一个程序。但对于某些应用程序，如数据库，有时进程需要确保它正在单独写一个文件。为了向进程提供这种功能，Linux系统提供了记录锁机制。

​	Linux的文件记录锁能提供非常详细的控制，它能对文件的某一区域进行文件记录锁的控制。当fcntl用于管理文件记录锁的操作时，第三个参数指向一个**struct flock *lock**的结构：

```c
struct flock
{
    short_1_type;			//锁的类型
    short_1_whence;			//偏移量的起始位置:SEEK_SET, SEEK_CUR,SEKK_END
    off_t_1_start;			//Starting offset for lock
    off_t_1_len;			//Number of bytes to lock
    pid_t_1_pid;			//锁的属主进程ID
};
```

​	l_type用来指定是设置共享锁(F_RDLCK,读锁)还是互斥锁(F_WDLCK,写锁)。多个进程在一个给定的字节上可以有一把共享的读锁，但是在一个给定字节上的写锁只能由一个进程单独使用。进一步而言，如果在一个给定字节上已经有一把或多把读锁，则不能在该字节上再加写锁；如果再一个字节上已经有一把独占性的写锁，则不能再对它加任何读锁(**锁的不兼容规则**)

​	一个进程只能设置某一文件区域上的一种锁。如果某一文件区域已经存在文件记录所了，则如果此时再设置新的锁在该区域的话，旧的锁将会被新的锁取代。

​	l_whence、l_start和l_len用来确定需要进行文件记录锁操作的区域，如l_len是0，则表示锁的区域从其起点(由l_start和l_whence决定)开始直至最大可能位置为止。也就是不管添加到该文件里多少数据，它都处于锁的范围。为了锁整个文件，通常的方法是将l_start说明为0，l_whence说明为SEEK_SET，l_len说明为0。

**F_SETLK**

​	fcntl系统调用被用来设置或解放锁，当l_type取F_RDLCK或F_WDLCK时，在由l_whence、l_start和l_len指定的区域上设置锁；当l_type取F_UNLCK时则释放锁。如果锁被其他进程占用，则返回-1并设置errno为EACCES或EAGAIN

​	需要注意的是，当设置一个共享锁(读锁)时，第一个参数fd所指向的文件必须以可读方式打开；当设置一个互斥锁(写锁)时，第一个参数fd所指向的文件必须以可写方式打开；当设置两种锁时，第一个参数fd所指向的文件必须以可读可写方式打开。当进程结束或文件描述符fd被close系统调用时，锁会自动释放。

**F_SETLKW**

fcntl的功能与cmd取F_SETLK时类似，不同的是当希望设置的锁因为存在其他锁而被阻止设置时，该命令会等待相冲突的锁被释放。

**F_GETLK**

​	第三个参数lock指向一个希望设置的锁的属性的结构，如果锁能被设置，该命令并不真的设置锁，而是只修改lock的l_type域为F_UNLCK，然后返回。如果存在一个或多个锁与希望设置的锁互相冲突，则fcntl只返回其中一个锁的flock结构。

​	cmd取(6)、(7)、(8)时，执行成功返回-，有错误时返回-1，错误代码存入errno。

​	Linux系统的文件记录锁默认是建议性的。我们考虑数据库存取例程库，如果数据库中所有函数都以一致的方法处理记录锁。则称这些函数存取数据库的任何进程集为合作进程。如果这些函数是唯一的用来存取数据库的函数，那么它们使用建议性锁是可行的。但是建议性锁并不能阻止对数据库文件有写许可权的任何其他进程写数据库文件。不使用协同一致的方法(数据库存取例程库)来存取数据库的进程是一个非合作进程。强制性锁机制中，内核对每一个open、read和write都要检查调用进程对正在存取的文件是否违背了一把锁的作用

以下是对锁的应用实例程序：

```c
#include<stdio.h>
#include<unistd.h>
#include<fcntl.h>
#include<sys/types.h>
#include<sys/stat.h>
#include<string.h>

//自定义的错误处理函数
void my_err(const char * err_string, int line)
{
    fprintf(stderr, "line:%D ", line);
    perror(err_string);
    exit (1);
}

//锁的设置或释放函数
int lock_set(int fd, struct flock * lock)
{
    if(fcntl(fd,F_SETLK, lock) == 0)
    {
        //执行成功
        if(lock->l_type == F_RDLCK)
        {
            printf("set read lock,pid:%d\n", getpid());
        }
        else if(lock -> l_type == F_WRLCK)
        {
            printf("set write lock,pid:%d\n",getpid());
        }
        else if(lock-l_type == F_UNLCK)
        {
            printf("release lock,pid:%d\n",getpid());
        }
    }
    else
    {
        //执行失败
        perror("lock operation fail\n");
        return -1;
    }
    return 0;
}

//测试锁，只有当测试发现参数lock指定的锁能被设置时，返回0
int lock_test(int fd,struct flock * lock)
{
    if(fcntl(fd, F_GETLK, lock) == 0)
    {
        if(lock->l_type == F_UNLCK)
        {
            printf("lock can be set in fd\n");
            return 0;
        }
        else
        {
            if(lock->l_type == F_RDLCK)
            {
                printf("can't set lock ,read lock has been set by:%d\n",lock->l_pid);
            }
            else if(lock->l_type == F_WRLCK)
            {
                printf("can't set lock, write lock has been set by:%d\n",lock->l_pid)''
            }
            return -2;
        }
    }
    else
    {
        //执行失败
        perror("get incompatible locks fail");
        return -1;
    }
}

int main()
{
    int fd;
    int ret;
    struct flock lock;
    char  read_buf[32];
    //打开或创建文件文件
    if((fd = open("example", O_CREAT|O_TRUNC|O_RDWR, S_IRWXU)) == -1)
    {
        my_err("open",__LINE__);
    }
    if(write(fd,"test lcok", 10)!=10)
    {
        my_err("write",__LINE__);
    }
    //初始化lock结构
    memset(&lock, 0,sizeof (struct flock));
    lock.l_start = SEEK_SET;
    lock.l_whence = 0;
    lock.l_len = 0;
    //设置读锁
    lcok.l_type = F_RDLCK
        if(lock_test(fd,&lcok) == 0)
        {
            //测试可以设置锁
            lock.l_type = F_RDLCK;
            lock_set(fd,&lock);
        }
    //读数据
    lseek(fd,0,SEEK_SET);
    if((ret = read(fd,read_buf, 10)) <0)
    {
        my_err("read",__LINE__);
    }
    read_buf[ret]='\0';
    printf("%s\n",read_buf);
    //等待按键
    getchar();
    //设置写锁
    lock.l_type = F_WRLCK;
    if(lock_test(fd,&lock) == 0)
    {
        //测试可以设置锁
        lock.l_type=F_WRLCK;
        lock_set(fd,&lock);
    }
    //释放锁
    lock.i_type = F_UNLCK;
    lock_set(fd,&lock);
    close(fd);
    return 0;
}
```

​	本程序将锁的设置与测试都写成单独的子函数，程序首先在当前目录下建立了文件example，并写入了一个字符串test lock到example文件中，然后对其进行文件记录锁的操作。

执行结果如下：

```bash
./fcntl_lock
lock can be set in fd
set read lock,pid:16681
test lock

lock can be set in fd
set write lock,pid:16681
release lock,pid:16681
```

​	因为本程序只是一个进程，单个进程在同一字节上只能设置一种锁，新的锁会取代旧的锁。锁的不兼容规则时针对于多个进程之间的。

​	先在某一终端执行程序(假设为进程1)，结果如下：

```bash
./fcntl_lock
lock can be set in fd
set read lock,pid:16700
test lock
```

​	这时不进行任何操作，程序一直等待用户的按键输入。再打开要给新的终端执行程序(假设为进程2)，结果如下：

```bash
./fcntl_lock
lock can be set in fd
set read lock,pid:16701
test lcok
```

​	从进程2的结果可以看出，虽然进程1已经在指定文件上设置读锁，但是进程2仍能从文件上正确读出数据，可见Linux的文件记录锁默认是建议性锁而不是强制性锁。

​	切换到第二个终端，输入任何按键，结果如下：

```bash
can't set lock,read lock has been set by:16700
release lock,pid:16701
```

由结果可见，由于进程1在文件上设置了读锁，按照锁的不兼容规则，进程2只设置成功了读锁，而不能设置写锁了。

**F_GETOWN**

返回当前接收SIGIO或SIGURG信号的进程ID或进程组，进程组ID以复制返回

**F_SETOWN**

设置进程或进程组接收SIGIO信号和SIGURG信号，进程组ID以负值指定，进程ID用正值指定。

**F_GETSIG**

可以在输入输出时，获得发送的信号

**F_SETSIG**

设置在输入输出时发送的信号

##### ioctl函数

ioctl系统调用通常用来控制设备，不能用本章其他函数进行的操作都可以用ioctl来进行，在Shell下输入"man ioctl"可获得函数原型如下

```c
#include<sys/ioctl.h>
int ioctl(int fd,int request,...);
```

ioctl用来控制特殊设备文件的属性，第一个参数fd必须是一个已经打开的文件描述符，第三个参数一般为char *argp，它随第二个参数request的不同而不同。参数request决定了参数argp是向ioctl传递数据还是从ioctl获取数据。

## 文件属性操作

 	在Linux系统中，每个文件、目录都属于某一个用户，而用户又属于某一个组。每个用户有一个对应得uid，uid为0表示该用户具有root权限。默认情况下，uid与用户名是一一对应的，但实际上也可以多个用户名共享一下uid。组名和组id与此类似，每个用户都属于某个用户组，一个组中可以有多个用户。一个用户还可以属于不同的组。可以通过查看/etc/passwd和/etc/group这两个文件的内容了解系统的用户、组的配置情况。

### 获取文件属性

#### stat/fstat/lstat

在Shell下直接使用命令ls就可以获取文件的属性，在程序中，需要用到stat/fstat/lstat函数。这3个函数的功能类似，它们的函数原型可以在Shell下输入命令"man 2 stat"获得

```c#
#include<sys/types.h>
#include<sys/stat.h>
#include<unistd.h>
int stat(const char *file_name,struct stat *buf);
int fstat(int filedes, struct stat *buf);
int lstat(const char *file_name, struct stat *buf);
```

​	这些函数执行成功都返回0，当有错误发生时则返回-1，错误代码存入errno中。

​	这3个函数的区别是：stat用于获取由参数file_name指定的文件名的状态信息 ，保存在参数struct stat *buf中。fstat与stat的区别在于fstat是通过文件描述符来指定文件的。lstat和stat的区别是，对于符号链接文件，lstat返回的是符号链接文件**本身**的状态信息，而stat返回的是符号链接**指向**的文件状态信息。

​	参数struct stat *buf是一个保存文件状态信息的结构体，其类型如下：

```c
struct stat
{
	dev_t		st_dev;
	ino_t		st_ino;
	mode_t		st_mode;
	nlink_t		st_nlink;
	uid_t		st_uid;
	gid_t		st_gid;
	dev_t		st_rdev;
	off_t		st_size;
	blksize_t	st_blksze;
	blkcnt_t	st_blocks;
	time_t		st_atime;
	time_t		st_mtime;
	time_t		st_ctime;
};
```

​	其中各个域的含义如下：

- st_dev：文件的设备编号。
- st_ino：文件的i-node(i节点编号)
- st_mode：文件的类型和存取权限，它的含义与chmod、open函数的mode参数相同。
- st_nlink：连到该文件的硬链接数目，刚建立的文件值为1.
- st_uid：文件所有者的用户id。
- st_gid：文件所有者的组id。
- st_rdev：若此文件为设备文件，则为其设备编号
- st_size：文件大小，以字节计算，对符号链接，该大小是其所指向的文件名的长度。
- st_blksize：文件系统的I/O缓冲区大小
- st_blocks：占用文件区块的数目，每一区块大小通常为512个字节
- st_atime：文件最近一次被访问的时间。
- st_mtime：文件最后一次被修改的时间，一般只有调用utime和write函数时才会改变
- st_ctime：文件最后一次被更改的时间，此参数在文件所有者、所属组、文件权限被更改时更新。

若某一目录具有sticky位(S_ISVTX)，则表示在此目录下的文件只能被该文件所有者、此目录所有者或者root来删除或改名。

对于st_mode包含的文件类型信息，POSIX标准定义了一系列的宏。 

#### st_mode

- **S_ISLKN**(st_mode)：判断是否为符号链接
- **S_ISREG**(st_mode)：判断是否为一般文件
- **S_ISDIR**(st_mode)：判断是否为目录文件
- **S_ISCHR**(st_mode)：判断是否为字符设备文件
- **S_ISBLK**(st_mode)：判断是否为块设备文件
- **S_ISFIFO**(st_mode)：判断是否为FIFO
- **S_ISSOCK**(st_mode)：判断是否为socket。

struct stat结构体的成员比较多，但有的很少使用，常用的有：**st_mode**  **st_uid**  **st_gid**  **st_size**  **st_atime**  **st_mtime**

```c
#include<stdio.h>
#include<time.h>
#include<sys/stat.h>
#include<unistd.h>
#include<sys/types.h>
#include<errno.h>

int main(int argc, char *argv[])
{
	struct stat buf;
	//检查参数个数
    if(argc != 2)
    {
        printf("Usage: my_stat <filename>\n");
        exit(0);
    }
    //获取文件属性
    if(stat(argv[1], &buf) == -1)
    {
        perror("stat:");
        exit(1);
    }
    //打印出文件属性
    printf("device is : %d\n",buf.st_dev);
    printf("inode is : %d\n",buf.st_ino);
    printf("mode is: %o\n",buf.st_mode);
    printf("number of hard links is:%d\n",buf.st_nlink);
    printf("user ID of owner is:%d\n",buf.st_uid);
    printf("group ID of onwer is:%D\n",buf.st_gid);
    printf("device type (if inode device) is:%d\n",buf.st_rdev);
    
    printf("total size, in bytes is:%d\n",buf.st_size);
    printf("blocksize for filesystem I/O is :%d\n",buf.st_blksize);
    printf("number of blocks allocated is: %d\n",buf.st_blocks);
    
    printf("time of last access is: %s",ctime(&buf.st_atime));
    printf("time of last modification is:%s",ctime(&buf.st_mtime));
    printf("time of last change is:%s",ctime(&buf.st_ctime));
}
```

​	该程序先检查参数个数是否为2，如果不是则退出。然后根据输入的文件名调用stat函数获取文件属性，如果该函数返回-1(有可能文件不存在)，则推出。最后将获得的文件属性信息打印出来。

### 设置文件属性

​	对文件属性的修改，在Shell下可以使用一些命令来完成，如chmod。在程序中同样可以通过一系列的函数来修改文件的属性，如chmod/fchmod，chown/fchown/lchown，truncate/ftruncate，utime，umask等

#### hmod/fchmod

用于修改文件的存取权限

#### chown/fchown/lchown

用于修改文件的用户id和组id，在Shell下输入"man 2 chown"可获取其函数原型如下

```c
#include<sys/types.h>
#include<unistd.h>
int chown(const char* path,uid_t owner,gid_t group);
int fchown(int fd,uid_t owner,gid_t group);
int lchown(const char *path,uid_t owner,gid_t group);
```

​	chown会将参数path锁指定的文件所有者id变更为参数owner代表的用户id，而将该文件所有者的组id变更为参数group组id。fchown与chown类似，但它是以文件描述符作为参数的。除了所引用的文件是符号链接以外，lchown与chown功能一样。在某个文件是符号链接的情况下，lchown更改符号链接本身的所有者id，而不是该符号链接所指向的文件。

​	文件的所有者只能改变文件的组id为其所属组中的一个，超级用户才能修改文件的所有者id，并且超级用户可以任意修改文件的用户组id。

​	**如果参数owner或group指定为-1，那么文件的用户id或组id不会被改变**

​	函数执行成功返回0，当有错误发生时返回-1，错误代码存入errno中

#### truncate/ftruncate

用于改变文件的大小，在Shell下输入"man truncate"可获取其函数原型如下

```c
#include<unistd.h>
#include<sys/types.h>
int truncate(const char *path,off_t length);
int ftruncate(int fd,off_t length);
```

​	truncate将参数path指定的文件大小改为参数length指定的大小。如果原来的文件大小比参数length大，则超过的部分会被删除；如果原来的文件大小比参数length小，则文件将被扩展，与lseek系统调用类似，文件扩展的部分将以0填充。如果文件的大小被改变了，则文件的st_mtime域和st_ctime域将会被更新。**注：**ctime在更改文件任何属性时会变化，mtime在修改内容时才会变化

​	执行成功返回0，有错误发生时返回-1，错误代码存入errno中。

#### utime

用于改变任何文件的st_mtime域和st_ctime域，即存取时间和修改时间，在Shell下输入"man utime"可获取其函数原型

```c
#include<sys/types.h>
#include<utime.h>
int utime(const char *filename, struct utimbuf *buf);

#include<sys/time.h>
int utimes(char *filename, struct timeval *tvp);
```

参数struct utimbuf *buf的定义如下：

```c
struct utimbuf
{
	time_t actime; //access time
	time_t modtime; //modification time (mtime)
};
```

​	utime系统调用会把由第一个参数filename指定的文件的存取时间改为第二个参数buf的actime域，把修改时间改为第二个参数buf的modtime域，如果buf是一个空指针，拿奖存取时间和修改时间都改为当前时间。

​	函数执行成功返回0，当有错误发生时返回-1，错误代码存入errno中。

#### umask

​	用于设置文件创建时使用的屏蔽字，并返回以前的值(注：umask没有出错返回)，在Shell下输入"man 2 umask"可获取其函数原型如下：

```c
#include<sys/types.h>
#include<sys/stat.h>
mode_t umask(mode_t mask);
```

​	在进程创建一个新文件或目录时，如调用oepn函数创建一个新文件，新文件的实际存取权限是mode域umask按照(mode&~umask)运算后的结果，umask函数用来修改进程的umask。参数mask可以直接取数值也可以为open系统调用的第三个参数mode的11个宏或它们的组合。

```c
#include<stdio.h>
#include<sys/types.h>
#include<sys/stat.h>
#include<fcntl.h>

int main()
{
	umask(0);
	if( creat("example1",S_IRWXU|S_IRWXG|S_IRWXO) < 0)
	{
		perror("creat");
		exit(1);
	}
	umask(S_IRWXO);
	if(creat("example2",S_IRWXU|S_IRWXG|S_IRWXO) < 0)
	{
		perror("creat");
		exit(1);
	}
	return 0;
}
```

​	程序中creat以所有者、组、其他用户可读可写可执行的权限创建了两个文件，只不过创建后用umask设置了不同的屏蔽字，执行完本程序后，在当前目录下生成了两个文件：example1和example2，example1的权限为00777，example2的权限为00770.。

## 文件的移动和删除

### 文件的移动

​	rename系统调用可以用来修改文件名或文件的位置，在Shell下输入"man 2 rename"可获取其函数原型如下：

```c
#include<stdio.h>
int rename(const char *oldpath,const char * newpath);
```

​	rename会将参数oldpath所指定的文件名称改为参数newpath所指定的文件名称。若newpath所指定的文件已存在，则原文件会被删除。

​	执行成功返回0，当有错误发生时则返回-1，错误代码存入errno中。

```c
#include<stdio.h>

int main(int argc,char ** argv)
{
    //检查参数个数的合法性
    if(argc < 3)
    {
        printf("my_mv < old file>  <new file>\n");
        exit(0);
    }
    
    if(rename(argv[1],argv[2]) < 0)
    {
        perror("rename");
        exit(1);
    }
    return 0;
}
```

​	本程序实现了简单的mv命令功能，程序先判断参数的合法性，然后利用rename系统调用，把命令行第二个参数所指定的文件名更改为命令行第三个参数指定的文件名。

### 文件的删除

​	文件的删除可以使用unlink系统调用，目录文件的删除则需要使用rmdir系统调用。而一个通用的既能删除文件又能删除目录的系统调用时remove，remove系统调用实际上是在其**内部封装**了unlink和rmdir，当需要删除的是文件时，调用unlink；当需要删除的是目录时，调用rmdir。

​	在Shell下输入"man 2 unlink"和"man remove"可获得它们的函数原型如下：

```c
#include<unistd.h>
int unlink(const char *pathname);
int remove(const char *pathname);
```

#### unlink

​	unlink系统调用从文件系统中删除一个文件，如果文件的链接数为0且没有进程打开这个文件，则文件被删除且占用的磁盘空间释放。如果文件的链接数为0，但是由进程打开了这个文件，则文件暂时不删除，直到所有打开该文件的进程都结束时文件才被删除，利用这一点可以确保即使程序崩溃时，它所创建的临时文件也不会遗留下来。

​	参数pathname若指向一个符号链接，则该链接被删除。若参数pathname指向一个socket(套接字文件)、FIFO(命名管道)或设备文件时，该名字被删除，但已经打开这些文件的进程仍然可以使用这些特殊文件。

​	函数执行成功返回0，由错误发生时返回-1，错误代码存入errno中。

​	下面时函数unlink的具体应用。

```c
#include<stdio.h>
#include<sys/types.h>
#include<sys/stat.h>
#include<fcntl.h>
#include<unistd.h>

//自定义的错误处理函数
void my_err(const char * err_string, int line)
{
    	fprintf(stderr,"line:%d ",line);
    	perror(err_string);
    	exit(1);
}
int main()
{
    int fd;
    char buf[32];
    
    if((fd=open("temp", O_RDWR|O_CREAT|O_TRUNC, S_IRWXU)) < 0 )
    {
        my_err("open",__LINE__);
    }
    if(unlink("temp") < 0 )
    {
        my_err("unlink",_LINK_);
    }
    printf("file unlinked\n");
    if(write(fd,"temp",5) < 0)
    {
        my_err("write",__LINE__);
    }
    if((lseek(fd,0,SEEK_SET)) == -1)
    {
        my_err("lseek",__LINE__);
    }
    if(read(fd,buf,5) < 0)
    {
        my_err("read",__LINE__);
    }
    printf("%s\n",buf);
    
    return 0;
}
```

​	程序在当前目录创建了一个名为temp的文件，然后调用unlink，之后再对文件进行读写操作，程序在unlink之后如果出现了崩溃，则在程序结束时，temp也不会留下来。

## 目录操作

### 目录的创建和删除

#### 目录的创建

目录的创建可以由mkdir系统调用来实现，在Shell下输入"man 2 mkdir"可获取其函数原型如下：

```c
#include<sys/stat.h>
#include<sys/types.h>
int mkdir(const char *pathname, mode_t mode);
```

​	mkdir创建一个新的空目录。空目录中会自动创建**.**和**..**目录项，所创建的目录的存取许可权由mode(mode&~umask)指定。

​	新创建目录的uid(所有者)与创建该目录的进程的uid一致。如果父目录设置了st_gid位，则新创建的目录也设置st_gid位(目录被设置该位后，任何用户在此目录下创建的文件的组id与该目录的组id相同)

​	函数执行成功返回0，有错误发生时返回-1，错误代码存入errno中

#### 目录的删除

目录的删除可以由rmdir系统调用来完成，在Shell下输入"man 2 rmdir"可获取其函数原型如下：

```c
#include<unistd.h>
int rmdir(const char *pathname);
```

**注：**rmdir只能删除由参数pathname指定的空目录

​	执行成功后返回0，当有错误发生时则返回-1，错误代码存入errno中。

### 获取当前目录

​	每个进程(可以理解为运行中的程序)都有一个当前工作目录，此目录是搜索所有相对路径名的起点(不以斜线开始的路径名为相对路径名)。当用户登录到Linux系统时，其当前工作目录通常是口令文件(/etc/passwd)中该用户登录项的第6个字段，可以使用cat /etc/passwd命令查看该文件的内容。当前工作目录是进程的一股属性。

getcwd系统调用可以获取进程当前工作目录，在Shell下输入"man getcwd"可获取其函数原型如下：

```c
#include<unistd.h>
char *getcwd(char *buf,size_t size);
char *get_current_dir_name(void);
char *getwd(char *buf);
```

​	getcwd会将当前的工作目录的绝对路径复制到参数buf所指的内存空间，参数size为buf的空间大小。在调用此函数时，buf所指的内存空间要足够大，若工作目录绝对路径的字符串长度超过参数size大小，则返回值为NULL，errno的值为ERANGE。倘若参数buf为NULL，getwd()会根据参数size的大小自动分配内存(使用malloc())，如果参数size也为0，则getcwd()会根据工作目录绝对路径的字符串长度来决定配置的内存大小。进程可以在使用完此字符串后利用free()来释放此空间。

​	执行成功则将结果复制到参数buf所指的内存空间，或是返回自动配置的字符串指针。失败返回NULL，错误代码存于errno

​	如果定义了_GNU_SOURCE，则也可以使用函数get_current_dir_name获取当前工作目录。该函数使用malloc分配空间来保存工作目录的绝对路径字符串，如果设置饿了环境变量PWD，则返回PWD的值

​	如果定义了_BSD_SOURCE或_XOPEN_SOURCE_EXTENDED，也可以使用getwd获取当前工作目录。该函数不使用malloc分配任何空间，参数buf指向的空间的大小至少为PATH_MAX，getwd仅返回工作目录绝对路径字符串的前PATH_MAX个字符。

### 设置工作目录

使用chdir可以更改当前工作目录，在Shell下输入"man chdir"可获取其函数原型如下：

```c
#include<unistd.h>
int chdir(const char *path);
int fchdir(int fd);
```

​	chdir用来将当前工作目录改为由参数path指定的目录，fchdir用来将当前工作目录改为由参数fd(文件描述符)指定的目录。const char *path中的const的含义是，在chdir函数的实现代码中不允许对path所指向的字符串的内容进行改变。由于chdir是系统调用，不是自己定义和实现的函数，所以可以不去理会const

​	函数执行成功返回0，当有错误发生时则返回-1，错误代码存入errno中。

​	利用chdir编写的cd命令：

```c
#include<stdio.h>
#include<unistd.h>
#include<Linux.limits.h>

//自定义错误处理函数
void my_err(const char *err_string, int line)
{
    fprintf(stderr,"line:%d ", line);
    perror(err_string);
    exit(1);
}    

int main(int argc,char **argv)
{
    char buf[PATH_MAX+1];
    
    if(argc<2)
    {
        printf("my_cd <target path>\n");
        exit(1);
    }
    
    if(chdir(argv[1]<0))
    {
        my_err("chdir",__LINE__);
    }
    if(getcwd(buf,512) < 0)
    {
        my_err("getcwd",__LINE__);
    }
    printf("%s\n",buf);
    return 0;
}
```

​		程序使用chdir系统调用将当前工作目录改变为命令行参数所指定的目录，然后利用getcwd获取新的工作目录并打印出来。，运行结果如下：

```bash
[root@......   test]#./my_cd /home
/home
[root@.....    test]#
```

​	从结果可以看出，在Shell运行程序后并不能像cd命令一样进行目录切换，这是因为chdir只影响该函数的进程，对其他进程，如其父进程的当前工作目录，则修改不了。这也是cd命令作为少数几个Shell内置命令的原因。

### 获取目录信息

​	只要对目录具有读权限，就可以获取目录信息。函数opendir用来打开一个目录，readdir用来读取目录中的内容，closedir关闭一个已经打开的目录。在Shell下可查看到它们的函数原型。

#### opendir

```c
#include <sys/types.h>
#include<dirent.h>
DIR *opendir(const char * name);
```

​	opendir用来打开参数name指定的目录，并返回DIR*形态的目录流，类似于文件操作中的文件描述符，接下来对目录的读取和搜索都要使用此返回值。

​	成功则返回DIR*形态的目录流，失败则返回NULL，错误代码存入errno中。

#### readdir

```c
#include<sys/types.h>
#include<dirent.h>
struct dirent *readdir(DIR *dir);
```

​	readdir用来从参数dir所指向的目录中读取出目录项信息，返回一个struct dirent结构的指针。

struct dirent的定义如下：

```c
struct dirent
{
	long d_ino;		//inode number
	off_t d_off;	//offset to this dirent
	unsigned short d_reclen;	//length of this d_name
	char d_name [NAME_MAX+1];	//file name null_terminated
}
```

​	其中，d_ino是指此目录i节点编号，不必理会；d_off是指目录文件开头至此目录进入点的位移；d_reclen是指d_name的长度；d_name是指以NULL结尾的文件名。

​	函数执行成功返回该目录下一个文件的信息(存储与dirent结构体中)，如果调用opendir打开某个目录之后，第一次调用该函数，则返回的是该目录下第一个文件的信息，第二次调用该函数返回该目录下第二个文件的信息，依次类推。如果该目录下已经没有文件信息可供读取，则返回NULL。调用该函数时如果由错误发生或读取到目录文件尾，则返回NULL，错误代码存入erno中。

#### closedir

```c
#include <sys/types.h>
#include <dirent.h>
int closedir(DIR *dir);
```

​	closedir用来关闭参数dir指向的目录，执行成功返回0，当有错误发生时返回-1，错误代码存入errno中。

这个程序程序实现了上述三个函数的功能：

```c
#include<sys/types.h>
#include<dirent.h>
#include<unistd.h>
#include<stdio.h>

int my_readir(const char *path)
{
    DIR		*dir;
    struct dirent *ptr;
    
    if((dir = opendir(path)) == NULL)
    {
        perror("opendir");
        return -1;
    }
    while ((ptr = readdir(dir)) != NULL)
    {
        printf("file name: %s\n",ptr->d_name)
    }
    closedir(dir);
    return 0;
}

int main(int argc, char ** argv)
{
    if(argc < 2)
    {
        printf("listfile <target path>\n");
        exit(1);
    }
    if(my_readir(argv[1]) < 0)
    {
        exit(1);
    }
    return 0;
}
```

#### 代码实现ls

my_ls.c：

```c
#include <stdio.h>
#include <stdlib.h>
#include <string,h>
#include <time.h>
#include <sys/stat.h>
#include <unistd.h>
#include <sys/types.h>
#include <Linux/limits.h>
#include <dirent,h>
#include <grp.h>
#include <pwd.h>
#include <errno,h>

#define PARAM_NONE	0	//无参数
#define PARAM_A	1	//-a:显示所有文件
#define	PARAM_L	2	//-l:一行只显示一个文件的详细信息
#define MAXROWLEN	80	//一行显示的最多字符数

int	g_leave_len = MAXROWLEN	//一行剩余长度，用于输出对齐
int	g_maxlen;   //存放某目录下最长文件名的长度

//错误处理函数
void my_err(const char *err_string, int line)
{
    fprintf(stderr, "line:%d ", line);
    perror(err_string);
    exit(1);
}

//获取文件属性并打印
void display_attribute(struct stat buf, char *name)
{
    char	buf_time[32];
    struct passwd	*psd;//从该结构体中获取文件所有者的用户名
    struct group *grp;//从该结构体中获取文件所有者所属的组名
    
    //获取并打印文件类型
    if (S_ISLINK(buf.st_mode))
    {
        printf("l");
    }
    else if(S_ISREG(buf.st_mode))
    {
        printf("-");
    }
    else if(S_ISDIR(buf.st_mode))
    {
        printf("d")l
    }
    else if(S_ISCHR(buf.st_mode))
    {
        printf("c");
    }
    else if(S_ISBLK(buf.st_mode))
    {
        printf("b");
    }
    else if(S_ISFIFO(buf.st_mode))
    {
        printf("f");
    }
    else if(S_ISSOCK(buf.st_mode))
    {
        printf("s");
    }
    //获取并打印文件所有者的权限
    if(buf.st_mode & S_IRUSR)
        printf("r");
    else printf("-");
    if(buf.st_mode & S_IWUSR)
        printf("w");
    else printf("-");
    if(buf.st_mode & S_IXUSR)
        printf("x");
    else printf("-");

    //获取并打印文件所有者同组的用户对该文件的权限
    if(buf.st_mode & S_IRGRP)
        printf("r");
    else printf("-");
    if(buf.st_mode & S_IWGRP)
        printf("w");
    else printf("-");
    if(buf.st_mode & S_IXGRP)
        printf("x");
    else printf("-");

    //获取并打印其他用户对该文件的权限
    if(buf.st_mode & S_IROTH)
        printf("r");
    else printf("-");
    if(buf.st_mode & S_IWOTH)
        printf("w");
    else printf("-");
    if(buf.st_mode & S_IOTH)
        printf("x");
    else printf("-");

    //根据uid与gid获取文件所有者的用户名与组名
    psd = getpwuid(buf.st_uid);
    grp = getgrgid(buf.st_gid);
    printf("%4d",buf.st_nlink);//打印文件链接数
    printf("%-8s",psd->pw_name);
    printf("%-8s",grp->gr_name);

    printf("%6d",buf.st_size);//打印文件的大小
    strcpy(buf_time,ctime(&buf.st_mtime));
    buf_time(strlen(buf_time)-1) = '\0';//去掉换行符
    printf(" %s",buf_time);//打印时间的文件信息
}

//在没有使用-l选项时，打印一个文件名，打印时上下行对齐
void display_single(char *name)
{
    int i,len;
    //如果本行不足以打印一个文件名则换行
    if(g_leave_len < g_maxlen)
    {
        printf("\n");
        g_leave_len = MAXRPWLEN;
    }
    len=strlen(name);
    len = g_maxlen-len;
    printf("%-s",name);
    for(i=0;i<len;i++)
    {
        printf(" ");
    }
    printf(" ");
    //下面的2指示空格
    g_leave_len -=(g_maxlen + 2);
}
/*
*根据命令行参数和完整路径名显示目标文件
*参数flag:命令行参数
*参数pathname：包含了文件名的路径名
*/
void display(int flag,char *pathname)
{
    int	i,j;
    struct stat	buf;
    char name[NAME_MAX + 1];
    //从路径中解析出文件名
    for(i=0,j=0;i<strlen(pathnmae)l i++)
    {
        if(pathname[i] == '/')
        {
            j=0;
            continue;
        }
        name[j++] pathname[i];
    }
    name[j] = '\0';
    
    //用lstat而不是stat以方便解析链接文件
    if(lstat(pathname,&buf) == -1)
    {
        my_err("stat",__LINE__);
    }
    
    switch(flag)
    {
        case PARAM_NONE://没有-l和-a选项
            if(name[0] != '.')
            {
                fisplay_single(name);
            }
            break;
        case PARAM_A:	//-a:显示包括隐藏文件在内的所有文件
            dispaly_single(name);
            break;
            
        case PARAM_L:	//-l：每个文件单独占一行，显示文件的详细属性信息
           	if(name[0] != '.')
            {
                dispaly_attribute(buf.name);
                printf(" %-s\n",name);
            }
            break;
            
        case PARAM_A + PARAM_L:	//同时有-a和-l选项的情况
            display_attribute(buf,name);
            printf(" %-s\n", name);
            breakl
            
        default:
            break;
    }
}

void display_dir(int flag_param,char *path)
{
    DIR		*dir;
    struct dirent	*ptr;
    int	count=0;
    char	filenames[256][PATH_MAX+1],temp[PATH_MAX+1];
    //获取该目录下文件总和最长的文件名
    dir = opendir(path);
    if(dir == NULL)
    {
        my_err("opendir",_LINE);
    }
    while((ptr = readdir(dir)) != NULL)
    {
        if(g_maxlen < strlen(ptr->d_name))
            g_maxlen = strlen(ptr->d_name);
       	count++;
    }
    closedir(dir);
    
    if(count>256)
        my_err("too many files under this dir",__LINE__);
    
    int i,j,len = strlen(path);
    //获取该目录下所有的文件名
    dir = opendir(path);
    for(i = 0;i < count;i++)
    {
        ptr = readdir(dir);
        if(ptr == NULL)
        {
            my_err("readdir",_LINE);
        }
        strncpy(filename[i],path,len);
        filenames[1][len] = '\0';
        strcat(filename[i],ptr->d_name);
        filenames[i][len+strlen(ptr->d_name)] = '\0';
    }
    //使用冒泡法对文件名进行排序，排序后文件名按字母顺序存储于filenames
    for(i=0;i<count-1;i++)
        for(j=0;j<count-1-i;i++)
        {
            if(strcmp(filenames[j],filenames[j+1])>0)
            {
                strcpy(temp,filenames[j+1]);
                temp[strlen(filenames[j+1])] = '\0';
                strcpy(filenames[j+1],filenames[j]);
                filenames[j+1][strlen(filenames[j])] = '\0';
                strcpy(filenames[j],temp);
                filenames[j][strlen(temp)] = '\0';
            }
        }
    for(i=0;i<count;i++)
        	display(flag_param,filenames[i]);
    closedir(dir);
    //如果命令行中没有-l选项，打印一个换行符
    if((flag_param & PARAM_L) == 0)
        printf("\n");
}

int main(int argc,char ** argv)
{
    int i,j,k,num;
    char path[PATH_MAX+1];
    char param[32];//保存命令行参数，目标文件名和目录名不在此列
    int flag_param = PARAM_NONE;//参数种类，即是否有-l和-a选项
    struct stat buf;
    //命令行参数的解析，分析-l、-a、-al、-la选项
    j=0;
    num  =0;
    for(i=1;i<argc;i++)
    {
        if(argv[i][0] == '-')
        {
            for(k=1;k<strlen(argv[i]);k++,j++)
            {
                param[j] = argv[i][k];
            }
            num++//保存"-"的个数
        }
    }
    //只支持参数a和l，如果含有其他选项就报错
    for(i=0,i<j;i++)
    {
        if(param[i] == 'a')
        {
            flag_param += PARAM_A;
            continue;
        }
        else if(param[i] == 'l')
        {
            flag_param +=PARAM_L;
            continue;
        }else
        {
            printf("my_ls:invalid option -%c\n",param[i]);
            exit(1);
        }
    }
    param[j] = '\0';
    //如果没有输入文件名或目录，就显示当前目录
    if((num +1 ) == argc)
    {
        strcpy(path,"./");
        path[2] = '\0';
        display_dir(flag_param,path);
        return 0;
    }
    
    i=1;
    do
    {
        //如果不是目标文件名或目录，解析下一个命令行参数
        if(argv[i][0] == '-')
        {
            i++;
            continue;
        }
        else
        {
            strcpy(path,argv[i]);
            
            //如果文件或目录不存在，报错并退出程序
            if(stat(path,&buf) == -1)
                my_err("stat",__LINE__);
            
            if(S_ISDIR(buf.st_mode))//argv[i]是一个目录
            {
                //如果目录的最后一个字符不是'/'，就加上'/'
                if(path( strlen(argv[i]) -1 ) ! = '/')
                {
                    path[strlen(argv[i])] = '/';
                    path[strlen(argv[i])+1] ='\0';
                }
                else
                    path[ strlen(argv[i])] ='\0';
                
                display_dir(flag_param,path);
                i++;
            }
            else
            {
                //argv[i]是一个文件
                display(flag_param,path);
                i++;
            }
        }
    } while(i<argc);
    return 0;
}
```

my_ls(带-d版本)

```c
#include <stdio.h>
#include <stdlib.h>
#include <string,h>
#include <time.h>
#include <sys/stat.h>
#include <unistd.h>
#include <sys/types.h>
#include <Linux/limits.h>
#include <dirent,h>
#include <grp.h>
#include <pwd.h>
#include <errno,h>

#define PARAM_NONE	0	//无参数
#define PARAM_A	1	//-a:显示所有文件
#define	PARAM_L	2	//-l:一行只显示一个文件的详细信息
#define PARAM_d 4	//-d:将目录名像其他文件一样列出，而不是列出它们的内容
#define MAXROWLEN	80	//一行显示的最多字符数
#define ll ls -l

int	g_leave_len = MAXROWLEN	//一行剩余长度，用于输出对齐
int	g_maxlen;   //存放某目录下最长文件名的长度

//错误处理函数
void my_err(const char *err_string, int line)
{
    fprintf(stderr, "line:%d ", line);
    perror(err_string);
    exit(1);
}

//获取文件属性并打印
void display_attribute(struct stat buf, char *name)
{
    char	buf_time[32];
    struct passwd	*psd;//从该结构体中获取文件所有者的用户名
    struct group *grp;//从该结构体中获取文件所有者所属的组名
    
    //获取并打印文件类型
    if (S_ISLINK(buf.st_mode))
    {
        printf("l");
    }
    else if(S_ISREG(buf.st_mode))
    {
        printf("-");
    }
    else if(S_ISDIR(buf.st_mode))
    {
        printf("d")l
    }
    else if(S_ISCHR(buf.st_mode))
    {
        printf("c");
    }
    else if(S_ISBLK(buf.st_mode))
    {
        printf("b");
    }
    else if(S_ISFIFO(buf.st_mode))
    {
        printf("f");
    }
    else if(S_ISSOCK(buf.st_mode))
    {
        printf("s");
    }
    //获取并打印文件所有者的权限
    if(buf.st_mode & S_IRUSR)
        printf("r");
    else printf("-");
    if(buf.st_mode & S_IWUSR)
        printf("w");
    else printf("-");
    if(buf.st_mode & S_IXUSR)
        printf("x");
    else printf("-");

    //获取并打印文件所有者同组的用户对该文件的权限
    if(buf.st_mode & S_IRGRP)
        printf("r");
    else printf("-");
    if(buf.st_mode & S_IWGRP)
        printf("w");
    else printf("-");
    if(buf.st_mode & S_IXGRP)
        printf("x");
    else printf("-");

    //获取并打印其他用户对该文件的权限
    if(buf.st_mode & S_IROTH)
        printf("r");
    else printf("-");
    if(buf.st_mode & S_IWOTH)
        printf("w");
    else printf("-");
    if(buf.st_mode & S_IOTH)
        printf("x");
    else printf("-");

    //根据uid与gid获取文件所有者的用户名与组名
    psd = getpwuid(buf.st_uid);
    grp = getgrgid(buf.st_gid);
    printf("%4d",buf.st_nlink);//打印文件链接数
    printf("%-8s",psd->pw_name);
    printf("%-8s",grp->gr_name);

    printf("%6d",buf.st_size);//打印文件的大小
    strcpy(buf_time,ctime(&buf.st_mtime));
    buf_time(strlen(buf_time)-1) = '\0';//去掉换行符
    printf(" %s",buf_time);//打印时间的文件信息
}

//在没有使用-l选项时，打印一个文件名，打印时上下行对齐
void display_single(char *name)
{
    int i,len;
    //如果本行不足以打印一个文件名则换行
    if(g_leave_len < g_maxlen)
    {
        printf("\n");
        g_leave_len = MAXRPWLEN;
    }
    len=strlen(name);
    len = g_maxlen-len;
    printf("%-s",name);
    for(i=0;i<len;i++)
    {
        printf(" ");
    }
    printf(" ");
    //下面的2指示空格
    g_leave_len -=(g_maxlen + 2);
}
/*
*根据命令行参数和完整路径名显示目标文件
*参数flag:命令行参数
*参数pathname：包含了文件名的路径名
*/
void display(int flag,char *pathname)
{
    int	i,j;
    struct stat	buf;
    char name[NAME_MAX + 1];
    //从路径中解析出文件名
    for(i=0,j=0;i<strlen(pathnmae)l i++)
    {
        if(pathname[i] == '/')
        {
            j=0;
            continue;
        }
        name[j++] pathname[i];
    }
    name[j] = '\0';
    
    //用lstat而不是stat以方便解析链接文件
    if(lstat(pathname,&buf) == -1)
    {
        my_err("stat",__LINE__);
    }
    
    switch(flag)
    {
        case PARAM_NONE://没有-l和-a选项
            if(name[0] != '.')
            {
                fisplay_single(name);
            }
            break;
        case PARAM_A:	//-a:显示包括隐藏文件在内的所有文件
            dispaly_single(name);
            break;
            
        case PARAM_L:	//-l：每个文件单独占一行，显示文件的详细属性信息
           	if(name[0] != '.')
            {
                dispaly_attribute(buf.name);
                printf(" %-s\n",name);
            }
            break;
            
        case PARAM_A + PARAM_L:	//同时有-a和-l选项的情况
            display_attribute(buf,name);
            printf(" %-s\n", name);
            breakl
            
        default:
            break;
    }
}

void display_dir(int flag_param,char *path)
{
    DIR		*dir;
    struct dirent	*ptr;
    int	count=0;
    char	filenames[256][PATH_MAX+1],temp[PATH_MAX+1];
	if(flag_param == 1||flag_param == 2|| flag_param == 3)   
    {
         //获取该目录下文件总和最长的文件名
        dir = opendir(path);
        if(dir == NULL)
        {
            my_err("opendir",_LINE);
        }
        while((ptr = readdir(dir)) != NULL)
        {
            if(g_maxlen < strlen(ptr->d_name))
                g_maxlen = strlen(ptr->d_name);
            count++;
        }
        closedir(dir);

        if(count>256)
            my_err("too many files under this dir",__LINE__);

        int i,j,len = strlen(path);
        //获取该目录下所有的文件名
        dir = opendir(path);
        for(i = 0;i < count;i++)
        {
            ptr = readdir(dir);
            if(ptr == NULL)
            {
                my_err("readdir",_LINE);
            }
            strncpy(filename[i],path,len);
            filenames[1][len] = '\0';
            strcat(filename[i],ptr->d_name);
            filenames[i][len+strlen(ptr->d_name)] = '\0';
        }
       //使用冒泡法对文件名进行排序，排序后文件名按字母顺序存储于filenames
       for(i=0;i<count-1;i++)
            for(j=0;j<count-1-i;i++)
            {
                if(strcmp(filenames[j],filenames[j+1])>0)
                {
                    strcpy(temp,filenames[j+1]);
                    temp[strlen(filenames[j+1])] = '\0';
                    strcpy(filenames[j+1],filenames[j]);
                    filenames[j+1][strlen(filenames[j])] = '\0';
                    strcpy(filenames[j],temp);
                    filenames[j][strlen(temp)] = '\0';
                }
            }
        for(i=0;i<count;i++)
                display(flag_param,filenames[i]);
        closedir(dir);
        //如果命令行中没有-l选项，打印一个换行符
        if((flag_param & PARAM_L) == 0)
            printf("\n");
    }
    else
    {
        printf("%s",path);
    }
    
}

int main(int argc,char ** argv)
{
    int i,j,k,num;
    char path[PATH_MAX+1];
    char param[32];//保存命令行参数，目标文件名和目录名不在此列
    int flag_param = PARAM_NONE;//参数种类，即是否有-l和-a和-d选项
    struct stat buf;
    //命令行参数的解析，分析-l、-a、-al、-la选项
    j=0;
    num  =0;
    for(i=1;i<argc;i++)
    {
        if(argv[i][0] == '-')
        {
            for(k=1;k<strlen(argv[i]);k++,j++)
            {
                param[j] = argv[i][k];
            }
            num++//保存"-"的个数
        }
    }
    //只支持参数a和l，如果含有其他选项就报错
    for(i=0,i<j;i++)
    {
        if(param[i] == 'a')
        {
            flag_param += PARAM_A;
            continue;
        }
        else if(param[i] == 'l')
        {
            flag_param +=PARAM_L;
            continue;
        }
        else if(param[i] == 'd')
        {
            flag_param +=PARAM_D
        }
        else
        {
            printf("my_ls:invalid option -%c\n",param[i]);
            exit(1);
        }
    }
    param[j] = '\0';
    //如果没有输入文件名或目录，就显示当前目录
    if((num +1 ) == argc)
    {
        strcpy(path,"./");
        path[2] = '\0';
        display_dir(flag_param,path);
        return 0;
    }
    
    i=1;
    do
    {
        //如果不是目标文件名或目录，解析下一个命令行参数
        if(argv[i][0] == '-')
        {
            i++;
            continue;
        }
        else
        {
            strcpy(path,argv[i]);
            
            //如果文件或目录不存在，报错并退出程序
            if(stat(path,&buf) == -1)
                my_err("stat",__LINE__);
            
            if(S_ISDIR(buf.st_mode))//argv[i]是一个目录
            {
                if(flag_param == 1||flag_param == 2|| flag_param == 3)
                {
                    //如果目录的最后一个字符不是'/'，就加上'/'
                    if(path( strlen(argv[i]) -1 ) ! = '/')
                    {
                        path[strlen(argv[i])] = '/';
                        path[strlen(argv[i])+1] ='\0';
                    }
                    else
                        path[ strlen(argv[i])] ='\0';

                    display_dir(flag_param,path);
                    i++;
                }
                else
                {
                    dispaly_dir(flag_param,path);
                    i++
                }
            }
            else
            {
                //argv[i]是一个文件
                display(flag_param,path);
                i++;
            }
        }
    } while(i<argc);
    return 0;
}
```

# 进程控制

## 进程概述

​	操作系统的主要任务是管理计算机的软、硬件资源。现代操作系统的主要特点在于程序的并行执行，Linux操作系统也是如此。操作系统借助于进程来管理计算机的软、硬件资源，支持多任务的并行执行。**操作系统最核心的概念就是进程**。

### Linux进程

​	Linux是一个多用户多任务的操作系统。多用户是指多个用户可以在同一时间使用计算机；多任务是指Linux可以同时执行几个任务，可以在还未执行完一个任务时又执行另一个任务。进程简单地讲就是运行中的程序，Linux系统的一个重要特点是可以同时启动多个进程。

​	根据操作系统的定义：进程是操作系统资源管理的最小单位。

#### 进程概念

​	进程是一个动态的实体，是程序的一次执行过程。进程是操作系统资源分配的基本单位。**要掌握进程的概念就要将其与程序、线程两个概念区别开。**进程和程序的区别在于进程是动态地，程序是静态的；进程是运行中的程序，程序是一些保存在硬盘上的可执行的代码。

​	为了让计算机在同一时间内执行更多任务，在进程内部又划分了许多线程。线程在进程内部，它是比进程更小的**能独立运行的基本单位**。线程基本上不拥有系统资源，它与同属一个进程的其他线程共享进程拥有的全部资源。进程在执行过程中拥有独立的内存单元，其内部的线程共享这些内存。一个线程可以创建和撤销另一个线程，同一个进程中的多个线程可以并行执行。

​	Linux下可通过命令ps或pstree查看当前系统中的进程。

#### 进程标识 getpid

​	Linux操作系统中，每个进程都是通过惟一的进程ID标识的。进程ID是一个非负数。每个进程出了进程ID外还有一些其他标识信息，它们都可以通过相应的函数获得。这些函数的声明在unistd.h头文件中。

|     函数声明      |         功能         |
| :---------------: | :------------------: |
| pid_t getpid(id)  |      获得进程ID      |
| pid_t getppid(id) |  获得进程父进程的ID  |
|  pid_t getuid()   | 获得进程的实际用户ID |
|  pid_t geteuid()  | 获得进程得有效用户ID |
|  pid_t getgid()   |  获得进程的实际组ID  |
| pid_t getegid(id) |  获得进程的有效组ID  |

#### ID

用户ID和组ID的概念如下所示：

- 实际用户ID(uid)：标识运行该进程的用户，
- 有效用户ID(euid)：标识以声明用户身份来运行程序。例如，某个普通用户A，运行了一个程序，而这个程序是以root身份来运行的，这程序运行时就具有root权限。此时实际用户ID是A用户的ID，而有效用户ID是root用户ID。
- 实际组ID(gid)：它是实际用户所属的组的ID
- 有效组ID(egid)：有效组ID是有效用户所属的组的组ID

#### Linux进程的结构

​	Linux中一个进程由3部分组成：代码段、数据段和堆栈段，如下：

| 代码段 | 数据段 | 堆栈段 |
| :----: | :----: | :----: |

​	代码段存放程序的可执行代码。数据段存放程序的全局变量、常量、静态变量。堆栈段中的堆用于存放动态分配的内存变量；堆栈段中的栈用于函数调用，它存放着函数的参数、函数内部定义的局部变量。

#### Linux进程状态

##### ps命令

Linux ps(process status) 命令用于显示当前进程的状态，类似于windows的任务管理器。

ps [options]  [--help]

参数：

- -A：列出所有的进程，跟-e的效果相同
- -a：显示现行终端机下的所有进程，包括其他用户的进程
- -u：显示当前用户的进程状态
- -x：通常与a这个参数一起使用，可列出较完整的信息
- -l：较长、较详细的将该PID的信息列出
- -j：工作的格式(jobs format)
- -f：把进程的所有信息都显示出来
- -e：表示显示所有继承

由于ps命令支持的系统类型相当的多，所以它的参数多到离谱，而且有没有加上 -差很多，所以只需要记住几个常用的命令即可

Linux系统中的进程有以下几种状态·：

- 运行状态：进程正在运行或在运行队列中等待运行。
- 可中断等待状态：进程正在等待某个事件完成(如等待数据到达)。等待过程中可以被信号或定时器唤醒。
- 不可中断等待状态：进程也正在等待某个事件完成，在等待中不可以被信号或定时器唤醒，必须等待直到等待的事件发生
- 僵死状态：进程已终止，但进程描述符依然存在，直到父进程调用wait()函数后释放。
- 停止状态：进程因为收到SIGSTOP、SIGSTP、SIGTIN、SIGTOU信号后停止运行或者该进程正在被追踪(调试程序时，进程处于被跟踪状态)。

用ps命令可以查看进程的当前状态。

- R(runnable)，运行状态
- S(sleeping)，可中断等待状态
- D(uninterruptible sleep)，不可中断等待状态
- Z(zombile)，僵死状态
- T(traced or stopped)停止状态

下面显示了一部分ps的执行结果

```bash
[root&....]# ps -eo pid,stat
PID STAT
	1 S
	2 SN
	3 S<
	120 S<s
	2628 Ss
	2816 Ssl
	...
```

​	在运行结果中有一些后缀字符，其意义分别为：

- **<**(高优先级进程)、
- **N**(低优先级进程)、
- **L**(内存锁页，即页不可以被换出内存)，
- **s**(该进程为会话首进程)、
- **l**(多线程进程)、
- **+**(进程位于前台程序组)。

例如，Ssl说明该进程处于可中断等待状态，且该进程为会话首进程，而且是一个多线程的进程。

### 进程控制

​	Linux进程控制包括创建进程、执行新程序、退出进程以及改变进程优先级等。Linux系统为了对进程进行控制而提供了一系列的系统调用，用户可以使用这些系统调用来完成创建一个新进程、终止一个进程、改变进程的优先级别条件。

在Linux系统中，用于对进程进行控制的主要系统调用如下：

- **fork**：用于创建一个新进程
- **exit**：用于终止进程。
- **exec**：用于执行一个应用程序
- **wait**：将父进程挂起，等待子进程终止
- **getpid**：获取当前进程的进程ID
- **nice**：改变进程的优先级

### 进程的内存映像

#### Linux下程序转化成进程

​	Linux下C程序的生成分为4个阶段：预编译、编译、汇编、链接。编译器gcc经过预编译、编译、汇编3个步骤将源程序文件转换为目标文件。如果程序有多个目标文件或者程序中使用了库函数，编译器还要将所有的目标文件或所需的库链接起来，最后生成可执行程序。当程序执行时，操作系统将可执行程序复制到内存中。

程序转化为进程通常要经过以下步骤

- 内核将程序读入内存，为程序分配内存空间
- 内核为该进程分配进程标识符(PID)和其他所需资源。
- 内核为该进程保存PID及相应的状态信息，把进程放到运行队列中等待执行。程序转化为进程后就可以被操作系统的调度程序调度执行了。

#### 进程的内存映像

​	进程的内存映像是指内核在内存中如何存放可执行程序文件。在将程序转化为程序的过程中，操作系统将可执行程序由硬盘复制到内存中。

​	从内存的低地址到高地址依次如下：

- 代码段：即二进制机器代码，代码段是只读的，可被多个进程共享。如一个进程创建了一个子程序，父子程序共享代码段，此外子进程还将获得父进程数据段、堆、栈的复制。
- 数据段：存储已被初始化的变量，包括全局变量和已被初始化的静态变量。
- 未初始化数据段：存储未被初始化的静态变量，它也被称为bss段
- 堆：用于存放程序运行中动态分配的变量
- 栈：用于函数调用，保存函数的返回地址、函数的参数、函数内部定义的局部变量。

另外，高地址还存储了命令行参数和环境变量

| 高地址                            命令行参数和环境变量 |
| ------------------------------------------------------ |
| 栈↓                                                    |
| 堆↑                                                    |
| 未被初始化数据段                                       |
| 数据段                                                 |
| 低地址                             代码段              |

​	可执行程序和内存映像的区别在于：可执行程序位于磁盘中而内存映像位于内存中；可执行程序没有堆栈；因为程序被加载到内存中才会分配堆栈；可执行程序虽然也有未初始化数据段但它并不被储存在位于磁盘中的可执行文件中；可执行程序是静态的、不变的，而内存映像随着程序的执行是在动态变化的。如，数据段随着程序的执行要存储新的变量值，栈在函数调用时也是不断在变化中。

## 进程操作

### 创建进程

​	每个进程由ID标识，进程被创建时系统会为其分配一个惟一的进程ID。当一个进程向其父进程(创建该进程的进程)传递其终止消息时，意味这个程序的整个生命周期的结束。此时，该进程占用的所有资源包括进程ID全部被释放。

​	创建进程有两种方式，一是由操作系统创建；二是由父进程创建。由操作系统创建的进程之间是平等的，一般不存在资源继承关系。而对于由父进程创建的进程(通常称为子进程)，它们和父进程存在隶属关系。子进程又可以创建进程，这样会形成一个进程家族。子进程可以继承其父进程几乎所有的资源。在系统启动时，操作系统会创建一些进程，它们承担着管理和分配系统资源的任务，这些进程通常被称为系统进程。

​	系统调用fork是创建一个新进程的惟一方法(创建一个进程通常也称为fork一个进程)，除了极少数以特殊方式创建的进程，如init进程，它是内核启动时以特殊方式创建的。进程调用fork函数就创建了一个子进程。

​	创建了一个子进程之后，父进程和子进程争夺CPU，抢到CPU者执行，另外一个挂起等待。如果想要父进程等待子进程执行完毕后再继续执行，可以在fork操作之后调用wait或waitpid。一个刚刚被fork的子进程会和它的父进程一样，继续执行当前的程序。几个进程同时执行一个应用程序通常用处不大。更常见的使用方法是子进程在被fork后可以通过调用exec函数执行其他程序。

#### fork函数

​	在命令行下输入man 2 forl获得该函数声明：

```c
#include <sys/types.h>
#include <unistd.h>
pid_t fork(void);
```

​	一般情况下，函数最多有一个返回值，但fork函数非常特殊，**它有两个返回值**，即调用一次返回两次。成功调用fork函数后，当前进程实际上已经分裂为两个进程，一个是原来的父进程，另一个是刚刚创建的子进程。父子进程在调用fork函数的地方分开，fork函数有两个返回值，一个是父进程调用fork函数后的返回值，该返回值是刚刚创建的子进程的ID；另一个是子进程中fork函数的返回值，该返回值是0.fork函数返回两次的前提是进程创建成功，如果进程创建失败，则只返回-1.两次返回不同的值，子进程返回值为0，而父进程的返回值为新创建的子进程的进程ID，这样可以用返回值来区别父、子进程。

fork.c

```c
#include<stdio.h>
#include<sys.types.h>
#include<unistd.h>
int main(void)
{
	pid_t pid;
    printf("Process Creation Study\n");
    pid = fork();
    switch(pid)
    {
        case 0:
            printf("Child process is running,CurPid is %d, ParentPid is %d\n",pid,getppid());
            break;
        case -1:
            perror("Process Creation failed\n");
            break;
        default:
            printf("Parent process if running,ChildPid is %d,ParentPid is %d\n",pid,getpid());
            break;
    }
    exit(0);
}
```

程序的一次运行结果如下：

```
Process Creation Study
Child process is running,CurPid is0,Parent Pid is 5585
Parent process is runnin, ChildPid is 5586,ParentPid is 5585
```

​	从运行结果可以看出，进程创建成功后，fork函数返回了两次：一次返回值是0，代表子进程在运行，通过函数getppid得到其父进程ID为5585；另外一次返回值为5586，即子进程的进程ID，代表当前父进程在运行，通过函数getpid得到父进程的ID为5585，刚好与前面得到的父进程ID一致。

​	switch语句被执行了两次，一次是在子进程中执行，另一次是在父进程中执行。

​	一般来叔，fork之后是父进程先执行还是子进程先执行是不确定的 ，这取决于内核所使用的调度算法。操作系统一般让所有进程都享有同等执行权，除非某进程的优先级比其他的高，下面这个例子可以看出父子进程的交替执行

```c
#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>

int main(void)
{
	pid_t	pid;
    char *	msg;
    int		k;
    
    printf("Process Creation Study\n");
    pid = fork();
    switch(pid)
    {
        case 0:
            msg = "Child process is running";
            k = 3;
            break;
        case -1:
            perror("Process creation failed\n");
            break;
        default:
            msg = "Parent process is running";
            k = 5;
            break;
    }
    while(k>0)
    {
        puts(msg);
        sleep(l);
        k--;
    }
    exit(0);
}
```

运行结果如下：

```
Process Creation Study
Child process is running
Parent process is running
Parent process is running
Child process is running
Parent process is running
Child process is running
Parent process is running
Parent process is running
```

​	该程序子进程输出3条消息，父进程输出5条消息。执行子进程和执行父进程时打印出的消息不拥有。从输出上看出父子进程交替进行。

​	fork在创建进程失败时，返回-1.失败的原因通常是父进程拥有的子进程的个数超过了规定的限制，此时errno值为EAGAIN。如果可供使用的内存不足也会导致进程创建失败，此时errno值为ENOMEM。

​	子进程会继承父进程的很多属性，主要包括用户ID、组ID、当前工作目录、根目录、打开的文件、创建文件时使用的屏蔽字、信号屏蔽字、上下文环境、共享的存储段、资源限制等。

​	子进程和父进程有一些不同的属性，主要有下面这些：

- 子进程有它唯一的进程ID
- fork的返回值不同，父进程返回子进程的ID，子进程的则为0.
- 不同的父进程ID。子进程的父进程ID为创建它的父进程ID。
- 子进程共享父进程打开的文件描述符，但父进程对文件描述符的改变不会影响子进程中的文件描述符。
- 子进程不继承父进程设置的文件锁。
- 子进程不继承父进程设置的警告。
- 子进程的未决信号集被清空。

#### 孤儿进程

​	如果一个子进程的父进程先于子进程结束，子进程就成为一个孤儿进程，它由init进程收养，成为init进程的子进程。

```c
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
int main(void)
{
    pid_t pid;
    pid = fork();
    switch(pid)
    {
            case0:
            whilie(1)
            {
                printf("A background process,PID:%d\n,ParentID:%d\n",getpid(),getppid);
                sleep(3);
            }
        case -1:
            perror("Process creation failed\n");
            exit(-1);
        default:
            printf("Parent here ,pid:%d\n",getpid());
            exit(0);
    }
    return 0;
}
```

一次运行结果如下：

```
[root...]# ./fork3
A background process,PID:4973\n,ParentID:4972
Parent here ,pid:4972
Parent here ,pid:1
Parent here ,pid:1
Parent here ,pid:1
...
```

​	调用fork函数后，子进程先执行，打印出自己的ID号和父进程ID号。之后父进程执行，打印出一行消息后，就执行完毕了。此后子进程就成了孤儿进程，由init进程收养。可以看到此时子进程的父进程ID变为1.

#### vfork函数

​	vfork也可以用来创建一个新进程(实现调用了fork)，以下是vfork和fork的异同：

- vfork和fork一样都是调用一次，返回两次
- 使用fork创建一个子进程时，子进程只是完全复制父进程的资源。这样得到的**子进程独立于父进程**，具有良好的并发性。而使用vfork创建一个子进程时，操作系统并不会将父进程的地址空间完全复制到子进程，用vfork创建的**子进程共享父进程的空间地址**，也就是说子进程完全运行在父进程的地址空间上。子进程对该地址空间中任何数据的修改同样为父进程所见。
- 使用fork创建一个子进程时，哪个进程先运行取决于系统的调度算法。而vfork一个子进程时，vfork保证子进程先运行，当它调用exec或exit之后，父进程才可能被调度运行。如果在调用exec或exit之前子进程要依赖父进程的某个行为，就会导致死锁。

​	因为使用fork创建一个子进程时，子进程需要将父进程几乎每种资源都复制，所以fork是一个开销很大的系统调用，这些开销并不是所有情况都需要的。比如fork一个进程后，立即调用exec执行另外一个应用程序，那么fork过程中子进程对父进程地址空间的复制将是一个多余的过程。vfork不会拷贝父进程的地址空间，减少了系统开销。

​	fork和vfork区别主要在于父子进程的执行顺序和对父进程变量的修改，例如：

```c
#include<stdio.h>
#include <sys/types.h>
#include <unistd.h>

int globVar = 5;

int main(void)
{
    pid_t pid;
    int var = 1;
    printf("fork is diffirent with vfork \n");
    
    pid = fork();
    //pid = vfork()
    switch(pid)
    {
        case 0:
            i=3;
            while(i-- > 0)
            {
                printf("Child process is running\n");
                globVar++;
                var++;
                sleep(1);
            }
            printf("Child's globVar = %d,var = %d\n,globVar,var");
            break;
        case -1:
            perror("Process creation failed\n");
            exit(0);
        default:
            i=5;
            while(i-- > 0)
            {
                printf("Parent process is running\n");
                globVar++;
                var++;
                sleep(1);
            }
            printf("Parent's globVar = %d,var = %d\n",globVar,var);
            exit(0);
    }
}
```

当使用fork创建子进程的时候，运行结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/fork.png)

使用vfork创建子进程时，结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/vfork.png)

​	使用fork创建子进程时，子进程继承了父进程的全局变量和局部变量。子进程中，最后全局变量globVar和局部变量var的值均递增3，分别为8和4，而父进程中两者分别递增5，最后结果为10和6这证明了fork的子进程有自己独立的地址空间。不管是全局变量还是局部变量，子进程与父进程对它们的修改互不影响。vfork创建子进程后，父进程中globVar和var最后递增了8，这是因为vfork的子进程共享父进程的地址空间，子进程修改变量对父进程是可见的。

### 创建守护进程

​	守护进程(daemon)是指在后台运行的、没有控制终端与之相连的进程。它独立于控制终端，通常周期性地执行某种任务。Linux大多数服务器就是用守护进程的方式实现的，如Internet服务器进程inetd，Web服务器进程http等，守护进程在后台运行，类似于Windows中的系统服务

​	守护进程的启动方式有多种：它可以在Linux系统启动时从启动脚本/etc/rc.d中启动：可以由作业规化进程crond启动；还可以由用户终端(通常是Shell)执行。

为尽量避免产生不必要的交互，编写创建守护进程程序有如下要点：

- 让进程在后台执行。方法是调用fork产生一个子进程，然后使得父进程退出。
- 调用setsid创建一个新对话期。控制终端，登录会话和进程组通常都是从父进程继承下来的，守护进程要摆脱他们，不受它们的影响，其方法是调用setsid使进程成为一个会话组长。

**注：**当进程是会话组长时，调用setsid会失败。但第一点已经保证进程不会是会话组长。setsid调用成功后，进程成为新的会话组长和进程组长，并与原来的登录会话和进程组隔离。由于会话过程对控制终端的独占性，进程同时与控制终端脱离

- 禁止进程重新打开控制终端。经过以上步骤，进程已经成为一个无终端的会话组长，但是它可以重新申请打开一个终端。为了避免这种情况的发生，可以通过使进程不再是会话组长来实现。再一次通过fork创建新的子程序，使调用fork的程序退出。
- 关闭不再需要的文件描述符。创建的子进程从父进程继承打开的文件描述符。如不关闭，将会浪费系统资源，造成进程所在的文件系统无法卸下已经引起无法预料的错误。先得到最高文件描述符值，然后用一个循环程序，关闭0倒最高文件描述符值的所有文件描述符。
- 将当前目录更改为根目录。当守护进程当前工作目录在一个装配文件系统中时，该文件系统不能被拆卸。一般需要将工作目录改为根目录。
- 将文件创建时使用的屏蔽字设置为0。进程从创建它的父进程那里继承的文件创建屏蔽字可能会拒绝某些许可权。为防止这一点，用umask(0)将屏蔽字清零。
- 处理SIGCHLD信号。这一步不是必须的。但对于某些进程，特别是服务器进程往往在请求到来时生成子进程处理请求。如果父进程不等待子进程结束，子进程将成为僵尸进程(zombie)，从而占用系统资源。如果父进程等待子进程结束，将增加父进程的负担，影响服务器进程的并发性能。在Linux下可以简单地将SIGCHLD信号地操作设为SIG_IGN。这样，子进程结束时不会产生僵尸进程。

例：

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <signal.h>
#include <sys/param.h>
#incldue <Sys/stat.h>
#include <time.h>
#include <syslog.h>

int init_daemon(void)
{
    int pid;
    int i;
    
    //忽略I/O信号，STOP信号
    signal(SIGTTOU,SIG_IGN);
    signal(SIGTTIN,SIG_IGN);
    signal(SIGTSTP,SIG_IGN);
    signal(SIGHUP,SIG_IGN);
    
    pid = fork();
    if(pid > 0)
    {
        exit(0);//结束父进程，使得子进程成为后台程序
    }
    else if(pid < 0 )
    {
        return -1;
    }
    //建立一个新的进程组，在这个新的进程组中，子进程成为这个进程组的首进程，以使该进程脱离所有终端
    setsid();
    
    //再次创建一个子进程，退出父进程，保证该进程不是进程组长，同时让该进程无法再打开一个新的终端
    pid = fork();
    if( pid > 0)
        exit(0);
    else if(pid < 0)
        	return -1;
    //关闭所有从父进程继承的不再需要的文件描述符
    for(i=0;i< NOFILE;close(i++));
    //改变工作目录，使得进程不与任何文件系统联系
    chdir("/");
    
    //将文字屏蔽字设置为0
    umask(0);
    //忽略SIGCHLD信号
    signal(SIGCHLD,SIG_IGN);
    
    return 0;
}

int main()
{
    time_t now;
    init_daemon();
    syslog(LOG_USER|LOG_INFO,"测试\n");
	while(1)
    {
        sleep(8);
        time(&now);
        syslog(LOG_USER|LOG_INFO,"系统时间：\t%s\t\t\n",ctime(&now));
    }
}
```

编译运行此程序。然后用ps -ef查看进程状态，该进程的状态如下：

```
UID		PID		PPID		C			STIME		TTY		TIME		CMD
zzy		30074	1			0			20:37		?		00:00:00	./daemon
```

### 进程退出

​	进程退出表示进程即将结束运行。在Linux系统中进程退出的方法分为正常退出和异常退出两种。其中正常退出的方法有3种，异常退出的方法有2种

**正常退出：**

- 在main函数中执行return
- 调用exit函数
- 调用_exit函数

**异常退出：**

- 调用about函数
- 进程收到某个信号，而该信号使程序终止

​	不管是那种退出方式，最终都会执行内核中的同一段代码。这段代码用来关闭进程所有已打开的文件描述符，释放它所占用的内存和其他资源。

以下是各种退出方式的比较：

- exit和return的区别：exit是一个函数，有参数；而return是函数执行完后的返回。exit把控制权交给系统，而return将控制权交给调用函数
- exit和abort的区别：exit是正常终止进程，而about是异常终止
- exit(int exit_code)：exit中的参数exit_code为0代表进程正常终止，若为 其他值表示程序执行过程中有错误发生，比如溢出、除数为0。
- exit()和)_exit()的区别：exit在头文件stdlib.h中声明，而

_exit()声明在头文件unistd.h中。两个函数均能正常终止进程，但是

_exit()会执行后立即返回给内核，而exit()要先执行一些清楚操作，然后将控制器交给内核。

​	父子进程终止的先后顺序不同会产生不同的结果。在子进程退出前父进程先退出，则系统会让init进程接管子进程。当子进程先于父进程终止，而父进程又没有调用wait函数等待子进程结束，子进程进入僵死状态，并且会一直持续下去除非系统重启。子进程处于僵死状态时，内核只保存该进程的一些必要信息以备父进程所需。此时子进程始终占用着资源，同时也减少了系统可以创建的最大进程数。如果子进程先于父进程终止，且父进程调用了wait或waitpid函数，则父进程会等待子进程结束。

### 执行新程序

#### exec函数族

​	使用fork或vfork创建子进程后，子进程通常会调用exec函数来执行另一个程序。系统调用exec用于执行一个可执行程序以代替当前进程的执行映像。

​	**注：**exec调用并没有生成新进程。一个进程一旦调用exec函数，它本身就"死亡"了，系统把代码段替换成新的程序的代码，废弃原有的数据段和堆栈段，并为新程序分配新的数据段与堆栈段，惟一保留的就是进程ID，也就是说，对系统而言，还是同一个进程，不过执行的已经是另外一个程序了。

​	Linux下，exec函数族有6种不同的调用的形式，它们的声明在头文件<unistd.h>中，格式如下：

```c
#include <unistd.h>
int execve(const char *path,char * const argv[],char * const envp[]);
int execv(const char * path,char * const envp[]);
int execle(const char *path,const char *arg,...);
int execl(const char *path,const char *arg,...);
int exevp(const char *file,char * const argv[]);
int execlp(const char * file,const  char * arg,...);
```

​	为了便于用户灵活地使用Shell，Linux引入了环境变量地概念，包括用户的主目录，终端类型、当前目录等，它们定义了用户的工作环境，所以称为环境变量。可以使用env命令查看环境变量值，用户也可以修改这些变量值以定制自己的工作环境。

env.c

```c
#include <stdio.h>
#include <malloc.h>

extern char **environ;

int main(int argc,char *argv[])
{
    int i;
    printf("Argument:\n");
    for(i=0;i<argc;i++)
    {
        printf("argv[%d] is %s\n",i,argv[i]);
    }
    printf("Environment:\n");
    for(i=0;environ[i]!=NULL,i++)
        printf("%s\n",environ[i]);
    
   	return 0;
}
```

执行结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/env.png)

​	Argument后显示的是程序的命令行参数。Environment后显示的是当前系统中各个环境变量的值，可以将其与命令env的输出结果作比较 ，将会发现两者的结果是一样的。

​	程序中通过系统预定义的全局变量environ显示各个环境变量的值。还可以通过另一个方法得到环境变量

​	事实上，main函数的完整形式应该是

```c
int main(int argc,char * argv[],char **envp);
```

​	通过打印main的参数envp，同样可以得到环境变量。事实上无论是哪个exec函数，都是将可执行程序的路径、命令行参数和环境变量3个参数传递给可执行程序的main函数。

​	各exec函数如何将main函数需要的参数传递给它：

- **execv函数**：execv函数通过路径名方式调用可执行文件作为新的进程映像。它的argv参数用来提供给main函数的argv参数。argv参数是一个以NULL结尾(最后一个元素必须是一个空指针)的字符串数组。
- **execve函数**：在该系统调用中，参数path是将要执行的程序的路径名，参数argv、envp与main函数的argv、envp对应。

**注：**参数argv和参数envp的大小都是受限制的。Linux操作系统通过宏ARG_MAX来限制它们的大小，如果它们的容量之和超过ARG）MAX定义的值将会发生错误，这个宏定义在<linux/limits.h>中

- **execl函数**：该函数与execv函数用法类似 。只是在传递argv参数的时候，每个命令行参数都声明为一个单独的参数(参数中使用"..."说明参数的个数是不确定的)，要注意的是这些参数要以一个空指针作为结束。
- **execle函数**：该函数与execl函数用法类似，只是要显式指定环境变量。环境变量位于命令行参数最后一个参数后面，也就是位于空指针之后。
- **execvp函数**：该函数和execv函数用法类似，不同的是参数filename。该参数如果包含 "/"，就相当于路径名；如果不包含"/"，函数就到PATH环境定义的目录中寻找可执行文件。
- **execlp函数**：该函数类似于execl函数，它们的区别和execvp和execv一样。

exec函数族的6各函数中只有execve是系统调用。其他5各都是库函数，它们实现时都调用了execve。

​	正常情况下，这些函数不会返回，因为进程的执行映像已经被替换，没有接受返回值的地方了。如果有一个错误的时间，将会返回-1.这些错误通常是由文件名或参数错误引起的。

exec函数错误表：

|   erno   |                           错误描述                           |
| :------: | :----------------------------------------------------------: |
|  EACCES  | 指向的文件或脚本文件没有设置可执行位，即指定的文件是不可执行的 |
|  E2BIG   |       新程序的命令行 参数与环境变量容量之和超过ARG_MAX       |
| ENOEXEC  |            由于没有正确的格式，指定的文件无法执行            |
|  ENOMEM  |              没有足够的内存空间来执行指定的程序              |
| ETXTBUSY |           指定文件被一个或多个进程以可写的方式打开           |
|   EIO    |               从文件系统读入文件时发生I/O错误                |

​	在Linux操作系统下，exec函数族可以执行二进制的可执行文件，也可以执行Shell脚本程序，但Shell脚本必须以下面的格式开头：

第一行必须为#!interpretername[arg]。其中interpretername可以是Shell或其他解释器，例如/bin/sh或/usr/bin/perl，arg是传递给解释器的参数。

​	processingmage.c和execve.c

```c
//程序一：用来替换进程映像的程序
#include <stdio.h>
#include <sys.types.h>
#include <unistd.h>

int main(int argc, char ** argv,char ** environ)
{
    int i;
    pritnf("process image here\n");
    printf("pid = %d,parentpid =%d\n",getpid(),getppid());
    print("uid = %d,gid = %d\n",getuid(),getgid());
    
    for(i=0;i<argc;i++)
    {
        printf("argv[%d]: %s\n",i,argv[i]);
    }
}

//程序二：exec函数实例，使用execve函数
#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
int main(int argc,char **  argv,char ** environ)
{
    pid_t pid;
    int stat_val;
    printf("Exec here\n");
    pid = fork();
    switch(pid)
    {
        case -1:
            perror("Process Creation failed\n");
            exit(1);
        case 0:
            printf("Child here\n");
            printf("pid = %d,parentpid = %d\n",getpid(),getppid());
            printf("uid = %d,gid = %d",getuid(),getgid());
            execve("processimage",argv,environ);
            printf("process never go here\n");
            exit(0);
        default:
            printf("Parent process is running");
            break;
	}
    wait(&stat_val);
    exit(0);
}
```

​	先编译第一个程序，再编译第二个程序，运行结果为：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/exec.png)





​	从执行结果可以看到执行新程序的进程保持了原来的进程的进程ID、父进程ID、实际用户ID和实际组ID、同时我们还可以看到当调用新的可执行程序后，原有的子程序的映像被替代，不再被执行。子程序永远不会执行到printf("process never go to here\n")，因为子进程已经被新的执行映像所替代。

​	执行新程序后的进程除了保持了原来的进程ID、父进程ID、实际用户ID和实际组ID之外，进程还保持了许多原有特征，主要有：

- 当前工作目录
- 根目录
- 创建文件时使用的屏蔽字
- 进程信号屏蔽字
- 未决警告
- 和进程相关的使用处理器的时间
- 控制终端
- 文件锁

### 等待进程结束

当子进程先于父进程退出时，如果父进程没有调用wait和waitpid函数，子进程就会进入僵死状态，如果父进程调用了wait或waipid函数，就不会使子进程变为僵尸进程。

#### wait和waitpid

这两个函数的声明如下：

```c
#include <sys/types.h>
#include <sys/wait.h>
pid_t wait(int *statloc);
pid_t waitpid(pid_t pid,int *statloc, int options);
```

​	wait函数使父进程暂停执行，直到它的一个子进程结束为止。该函数的返回值是终止运行的子进程的PID。参数statloc所指向的变量存放子程序的退出码，即从子进程的main函数返回的值或子进程中exit函数的参数。如果statloc不是一个空指针，状态信息将被写入它指向的变量。

​	头文件sys/wait.h中定义了解读进程退出状态的宏，以下为各个宏的说明：

|        宏定义         |                             说明                             |
| :-------------------: | :----------------------------------------------------------: |
|  WIFEXITED(stat_val)  | 若子进程是正常结束的，该宏返回一个非零值，表示真。若子进程异常结束，返回零。表示假 |
| WEXITSTTUS(stat_val)  | 若WIFEXITED返回值非零，它返回子进程中exit或_exit参数的低8位  |
| WIFSIGNALED(stat_val) |         若子进程异常终止，它就取得一个非零值，表示真         |
|  WTERMSIG(stat_val)   | 如果宏WIFSIGNALED的值为零，该宏返回使子进程异常终止的信号编号 |
| WIFSTOPPED(stat_val)  |           若子进程暂停，它就取得一个非零值，表示真           |
|  WSTOPSIG(stat_val)   |        若WIFSTOPPED非零，它返回使子进程暂停的信号编号        |

​	waitpid也用来等待子进程的结束，但它用于等待某个特定进程结束。参数pid指明要等待的子进程的PID。pid值的意义如下：

|  取值   |                   意义                   |
| :-----: | :--------------------------------------: |
| pid > 0 |     等待其进程ID等于pid的子进程退出      |
| pid = 0 | 等待其组ID等于调用进程的组ID的任一子进程 |
| pid <-1 |   等待其组ID等于pid绝对值的任一子进程    |
| pid =-1 |              等待任一子进程              |

​	waitpid参数statloc的含义与wait函数中的相同。options参数运行用户改变waitpid的行为，若将该参数赋值为WNOHANG，则使父进程不被挂起而立即返回并执行其后的代码。

​	如果想让父进程周期性地检查某个特定的子进程是否已经退出，可按如下方式调用waitpid

```c
waitpid(child_pid,(int*)0,WNOHANG);
```

​	如果子程序尚未退出，它将返回0；如果子程序已经结束，则返回child_pid。调用失败时返回-1.失败的原因包括没有该子程序、参数不合法等。

**注：**wait等待第一个终止的子进程，而waitpid则可以指定等待特定的子进程，waitpid提供了一个wait的非阻塞版本。有时希望取得一个子进程的状态，但不想父进程阻塞，waitpid提供了一个选项：**WNOHANG**，它可以使调用者不阻塞。如果一个没有任何子进程的进程调用wait函数，会立即出错返回。

wait.c

```c
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

int main()
{
	pid_t pid;
    char *msg;
    int k;
    int exit_code;
    printf("get exit code\n");
    pid = fork();
    switch(pid){
        case 0:
            msg="Chlid process is running";
            k = 5;
            exit_code = 37;
            break;
        case -1:
            perror("Process creation failed\n");
            exit(1);
        default:
            exit_code = 0;
            break;
    }
    //父子进程都会执行以下代码，子进程pid值为0，父进程pid值为子进程的ID
    if(pid !=0){
        int stat_val;
        pid_t child_pid;
        
        child_pid = wait(&stat_val);
     	
        printf("Child process has exited ,pid = %d\n",child_pid);
        if(WIFEXITED(stat_val))
            printf("Child exited with coude %\n",WEXITSTATUS(stat_val))
        else
        	printf("Child exited abnormally\n");   
    }
    else{
        while(k-- >0)
        {
            puts(msg);
            sleep(1);
        }
    }
    exit(exit_code);
}
```

运行结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/wait.png)

​	父进程调用wait后被挂起等待(此时打开另一个终端，输入命令ps aux可以看到父进程的状态为S)，直到子进程结束为止。子进程正常结束后，wait函数返回刚刚结束运行的子进程的pid，宏WEXITSTATUS获取子进程的退出码。

## 进程的其他操作

### 获得进程ID

​	系统调用getpid用来获取当前进程的ID。输入命令man 2 getpid可获得该函数的声明：

```c
#include <sys/types.h>
#include <unistd.h>
pid_t getpid(void);
```

getpid.c

```c
#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
int main(void)
{
    pid_t pid;
    if((pid = fork()) == -1)
    {
        printf("fork error!\n");
        exit(1);
    }
    if(pid == 0)
        printf("getpid return %d\n",getpid());
    exit(0);
}
```

​	运行程序结果如下：

```bash
...
getpid return 5586
getpid返回了子进程的ID号5586
```

### setuid和setgid

​	可以用setuid设置实际用户ID和有效用户ID。与此相似，setgid用来设置实际组ID和有效组ID。

函数的声明在unistd.h头文件，声明如下：

```c
#include <sys/types.h>
#include <unistd.h>
int setuid(uid_t uid);
int setgid(gid_t gid);
```

​	设置用户ID的setuid函数遵守以下规则(设置组ID的setgid函数与此类似)

- 若进程具有root权限，则函数将实际用户ID、有效用户ID设置为参数uid
- 若进程不具有root权限，但uid等于实际用户ID，则setuid只将有效用户ID设置为uid，不改变实际用户ID。
- 若以上两个条件都不满足，则函数调用失败，返回-1，并设置errno为EPERM

​	只有超级用户才能更改实际用户ID。所以一个非特权用户进程是不能通过setuid或seteuid得到特权用户权限的。但是su命令却能使一个普通用户变成特权用户。这并不矛盾，因为su是一个"set_uid"程序。执行一个设置了set_uid位的程序时，内核将进程的有效用户ID设置为文件属性主的ID。而内核检查一个进程是否具有访问某文件的权限时，是使用进程的有效用户ID来进行检查的。su程序的文件属主是root，普通用户运行su命令时，su进程的权限是root权限。

从以下程序可以看到，内核是通过检查进程的有效用户ID来检查的，而不是实际用户ID。

```c
#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <stdlib.h>
#include <string.h> //一定要加上该头文件，否则导致段错误(吐核)
extern int errno;

int main()
{
    int fd;
    
    printf("Process 's uid = %d,euid = %d\n",getuid(),geteuid());
    //strerror函数获取指定错误码的提示信息
    if((fd = open("test.c",O_RDWR)) == -1)
    {
        printf("Open failure, errno is %d: %s\n",errno,strerror(errno));
        exit(1);
    }
    else
    {
        printf("Open successfully\n");
    }
    close(fd);
    exit(0);
}
```

​	以root用户使用命令在当前目录创建一个名为test.c的文件：touch test.c，在root用户下运行该程序，此时test.c的权限为-rw-r--r--，即root用户有读写权限，其他用户只读，运行结果为：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/setuid1.png)

​	转到另外一个普通用户下，运行的结果为：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/setuid2.png)

在root下，修改aa的set_uid位

```bash
chmod 4755 aa
```

然后再在普通用户下运行aa，结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/setuid3.png)

​	结果说明，内核对进程存取文件的许可权的检查，是通过考查进程的有效用户ID实现的。

**注：**对于调用了setuid函数的函数要小心，当进程的有效用户ID即euid是root时，如果调用setuid函数使euid为其他非root用户，则该进程就不再具有超级用户权限了。

​	可以这样使用setuid函数：开始时某个程序需要root权限完成一些工作，但后续的工作不需要root权限。可以将该可执行程序文件设置set_uid位，并使得该文件的属主是root。这样普通用户执行这个程序时，进程就具有了root权限，当不再需要root权限时，调用setuid(getuid())恢复进程的实际用户ID和有效用户ID为执行该程序的普通用户的ID。对于一些提供网络服务的程序，这样做可以防止被攻击者利用。

### 改变进程的优先级

​	可以通过设置进程的优先级来保证进程优先运行。在Linux下，通过系统调用nice可以改变进程的优先级。nice系统调用用来改变调用进程的优先级。命令行下输入命令man 2 nice可以获得该函数的声明：

```c
#include <unistd.h>
int nice(int increment);
```

#### getpriority和setpriority

```c
#include <sys/resource.h>
int getpriority(int which,int who);
int setpriority(int which,int who,int prio);
```

**getpriority**函数：该函数返回一组进程的优先级。参数which和who组合确定返回哪一组进程得优先级。which得可能取值以及who得意义如下：

- PRIO_PROCESS：一个特定得进程，此时who得取值为进程ID
- PRIO_PGRP：一个进程组的所有进程，此时who的取值为进程组ID
- PRIO_USER：一个用户拥有的所有进程，此时参数who取值为实际用户ID。

​	getpriority函数如果调用成功返回指定进程的优先级，如果出错将返回-1，并设置errno的值。errno的可能取值如下：

- ESRCH：which和who的组合 与现存的所有进程均不匹配
- EINVAL：which是个无效的值。

**注：**当指定的一组进程的优先级不同时getpriority将返回其中优先级最低的一个，此外，当getpriority返回-1时，可能是发生错误，也有可能是返回的是指定进程的优先级。区分它们的唯一方法是在调用getpriority前将errno清零，**如果函数返回-1且errno不为零**，说明有错误产生。

**setpriority**函数：该函数用来设置指定进程的优先级。进程指定的方法与getpriority函数相同。如果调用成功，函数返回指定进程的优先级，出错则返回-1，并设置相应的errno。除了产生与getpriority相同的两个错误外，很有可能产生以下错误：

- EPERM：要设置优先级的进程与当前进程不属于一个用户，且当前进程没有CAP_SYS_NICE特许。
- EACCES：该调用可能降低进程的优先级并且进程没有CAP_SYS_NICE特许。

​	通过以上两个函数，完全可以改变进程的优先级。nice系统调用是它们的一种组合形式，nice系统调用等价于：

```c
int nice(int increment)
{
    int oldpro = getpriority(PRIO_PROCESS,getpid());
    return setpriority(PRIO_PROCESS,getpid(),oldpro + increment);
}
```

mynice.c

```c
#include<stdio.h>
#include<sys/types.h>
#include <unistd.h>
#include<sys/resource.h>
#include<sys/wait.h>

int main(void)
{
    pid_t pid;
    int stat_val=0;
    int oldpri,newpri;
    
    printf("nice study\n");
    pid= fork();
    switch(pid){
        case 0:
            printf("Child is running,Curpid is %d,ParentPid is %d\n",pid,getppid());
            oldpri = getpriority(PRIO_PROCESS,0);
            printf("Old priority = %d\n",oldpri);
            newpri = nice(2);
            printf("New priority = %d\n",nwepri);
            
            exit(0);
        case -1:
            perror("Process creation failed\n");
            break;
        default:
            printf("Parent is running ,ChildPid is %d,ParentPid is %d\n",pid,getpid());
            break;
    }
    wait(&stat_val);
    exit(0);
}
```

运行结果如下：

```bash
./mynice
nice study
Child is running,Curpid is 0,ParentPid is 17360
Old priority = 0
Parent running,ChildPid is 17361,ParentPid is 17360
New priority = 2
```

可以看到子进程的优先级有原来的0变为2.

## 代码实现myshell

​	实现一个自己的shell，具有：解释执行命令，支持输入输出重定向，支持管道，后台运行程序的功能

1.运行本程序后，它支持以下命令格式

- 单个命令，如：ls
- 带1到多个参数的命令，如ls -l /tmp
- 带一个输出重定向的命令，如ls -l / > a
- 带一个输入重定向的命令，如wc -c < a
- 带一个管道的命令，如ls -l / | wc -c
- 后台运行符&可以加在以上各个命令的最后面

如：ls &	ls -l /tmp &	ls-l / > a &	wc -c < a &	ls-l | wc -c &

-  输入exit或logout退出myshell。

2，错误处理

- 输入错的命令格式报错
- 输入不存在的命令报错

3.程序主函数的流程图：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/shell.png)



4.关键函数的功能及说明

1. void print_prompt()

   该函数只是简单地打印myshell的提示符，即"myshell$$"

2. void get_input(char *buf)

​		获得一条用户输入的待执行的命令，参数buf用于存放输入的命令。如果输入的命令过长(大于256个字符)，则终止程序。输入的命令以换行符"\n"作为结束标志，

 3. ```c
    void explain_input(char *buf,int *argcount,char arglist[100][256])
    ```

    解析buf中存放的命令，把每个选项存放在arglist中，如输入命令"ls -l /tmp"，则arglist[0]、arglist[1]、arglist[2]指向的字符串分别为"ls"、"l"、"/tmp"。

 4. ```c
    do_cmd(int argcount, cahr arglist[100][256])
    ```

    zhi行arglist中存放的命令，argcount为待执行命令的参数个数

 5. int find_command(char *command)

​		功能是分别在当前目录下、/bin、/usr/bin目录下查找命令的可执行程序

5.程序实现源代码

myshell.c

```c
#include <stdio.h>
#include<stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <dirent.h>
#define normal 0 //一般的命令
#define out_redirect 1//输出重定向
#define in_redirect 2 //输入重定向
#define have_pipr 3 //命令中有管道

void print_prompt(); //打印提示符
void get_input(char *buf); //得到输入的命令
void explain_input(char *buf, int *argcount, char a[][]);//对输入命令进行解析
void do_cmd(int argcount,char a[][]);//执行命令
int find_command(char *); //查找命令中的可执行程序

int main(int argc, char **argv)
{
    int i;
    int argcount=0;
    char arglist[100][256];
    char **arg = NULL;
    char *buf = NULL;
    
    buf = (char *)malloc(256);
    if( buf == NULL){
        perror("malloc failed");
        exit(-1);
    }
    
    while(1){
        //将buf所指向的空间清零
        memset(buf,0,256);
        print_prompt();
        get_input(buf);
        //若输入的命令为exit或logout则退出程序
        if( strcmp(buf,"exit\n") == 0 || strcmp(buf,"logout\n") == 0)
            break;
       	for(i=0;i<100;i++)
        {
            arglist[i][0]='\0';
        }
        argcount = 0;
        explain_input(buf, &argcount, arglist);
        do_cmd(atgcount, arglist);
    }
    
    if(buf != NULL)
    {
        free(buf);
        buf = NULL;
    }
    
    exit(0);
}

void print_prompt()
{
    printf("myshell$$");
}

//获取用户输入
void get_input(char *buf)
{
    int len = 0;
    int ch;
    
    ch = getchar();
    while(len <256 && ch != '\n')
    {
        buf[len++] =ch;
        ch = getchar();
    }
    
    if(len == 256)
    {
        printf("command is too long \n");
        exit(-1);//输入的命令过长则退出程序
    }
    	
    buf[len] = '\n';
    len++;
    buf[len] = '\0';
}

//解析buf中的命令，将结果存入arglist中，命令以回车符号\n结束
//如输入命令为"ls -l /tmp"，则arglist[0]、arglist[1]、atglist[2]分别为ls、-l和/tmp
void explain_input(char *buf, int * argcount,char arglist[100][256])
{
    char *p = buf;
    char *q = buf;
    int number = 0;
    while (1){
        if(p[0] == '\n')
            break;
        if(p[0] == ' ')
            p++;
        else{
            q = p;
            number = 0;
            while((q[0]!= ' ') && (q[0] != '\n')){
                number++;
                q++;
            }
            strncpy(arglist[*argcount],p,number+1);
            arglist[*argcount][number] = '\0';
            *argcount = *argcount + 1;
            p = q;
        }
    }
}

void  do_cmd(int argcount, char arglist[100][256])
{
    int flag = 0;
    int how = 0; //用于指示命令中是否含有>、<、|
    int background = 0;//标识命令中是否有后台运行标识符
    int status;
    int i;
    int fd;
    char * atg[argcount+1];
    char * argnext[argccount+1];
    char * file;
    pid_t pid;
    //将命令取出
    for(i=0;i < argcount; i++)
    {
        arg[i] = (char *) arglist[i];
    }
    arg[argcount] = NULL;
    
    //查看命令行是否有后台运行符
    for(i=0; i < argcount; i++)
    {
        if(strncmp(atg[1],"&",1) == 0)
        {
            if(i == argcount -1)
            {
                background =1;
                arg[argcount -1] = NULL;
                break;
            }
            else
            {
                printf("wrong command");
                return ;
            }
        }
    }
    
    for(i=0;arg[i]!=NULL;i++)
    {
        if(strcmp(arg[i],">") == 0)
        {
            flag++;
            how = out_redirect;
            if(arg[i+1] == NULL)
                	flag++;
        }
        if(strcmp(arg[i],"<") == 0)
        {
            flag++;
            how = int_redirect;
            if(i == 0)
                flag++;
        }
        if(strcmp(arg[i],"|") == 0)
        {
            flag++;
            how = have_pipe;
            if(arg[i+1] == NULL)
                flag++;
            if(i == 0)
                flag++;
        }
    }
    //flag大于1，说明命令中含有多个>,<,|符号，本程序不支持这样的命令，或者命令格式不对，如"ls -l /tmp >"
    if(flag>1)
    {
        printf("wrong command\n");
        return;
    }
    
    if(how == out_redirect)//命令只含有一个输出重定向符号
    {
        for(i=0;arg[i] != NULL;i++)
        {
            if(strcmp(arg[i],">") ==0)
            {
              file = arg[i+1];
          	  arg[i] = NULL;
            }
        }
    }
    
    if(how == in_redirect)//命令只有一个输入重定向符号
    {
        for(i=0;arg[i] != NULL;i++)
        {
            if(strcmp(arg[i],"<") ==0)
            {
                file =arg[i+1];
                arg[i] = NULL;
            }
        }
    }
    if(how == have_pipe)//命令只含有一个管道符号
    {
        //把管道符号后门的部分存入argnext中，管道后面的部分是一个可执行的Shell命令
        for(i=0; arg[i] != NULL;i++)
        {
            if(strcmp(arg[i],"|") == 0)
            {
                arg[i] =NULL;
                int j;
                for(j=i+1;arg[j] != NULL;j++)
                    argnext[j-i-1] = arg[j];
          	 	argnext[j-i-1] = arg[j];
          	 	break;
        	}
      	}
    }
    if( (pid = fork()) <0)
    {
        printf("fork error\n");
        return;
    }
    
    switch(how)
    {
        case 0://输入的命令中不含>、<和|
            //pid为0说明是子进程，在子进程中执行输入的命令
            if(pid == 0)
            {
                if( !(find_command(arg[0])) )
                {
                    printf("%s : command not found\n",arg[0]);
                    exit(0);
                }
                execvp(arg[0],arg);
                exit(0);
            }
            break;
        case 1:
            //输入的命令中含有输出 重定向符>
            if(pid == 0)
            {
                if(!(find_command(arg[0])) )
                {
                    printf("%s : command not found \n",arg[0]);
                    exit(0);
                }
                fd = open(file,O_RDWR|O_CREAT|O_TRUNC,0644);
                dup2(fd,1);
                execvp(arg[0],arg);
                exit(0);
            }
            break;
        case 2:
            //输入的命令中含有输入重定向符
            if(pid == 0)
            {
                if( !(find_command (arg[0])) )
                {
                    printf("%s : command not found\n",arg[0]);
                    exit(0);
                }
                fd = open(file,O_RDONLY);
                dup2(fd,0);
                execvp(arg[0],arg);
                exit(0);
            }
            break;
        case 3:
            //输入的命令中含有管道符
            if(pid == 0)
            {
                int pid2;
                int status2;
                int fd2;
                
                if( (pid2 = fork()) < 0 )
                {
                    printf("fork2 error\n");
                    return;
                }
                else if(pid2 == 0)
                {
                    if(! (find_command(arg[0])))
                    {
                        printf("%s : command not found\n",arg[0]);
                        exit(0);
                    }
                    fd2 = open("/tmp/youdonotknowfile",O_WRONLY|O_CREAT|O_TRUNC,0644);
                    dup2(fd2,1);
                    execvp(arg[0],arg);
                    exit(0);
            	}
                if(waitpid(pid2, &status2, 0) == -1)
                    printf("wait for child process error\n");

                if(!(find_command(argnext[0])) )
                {
                    printf("%s : command not found\n",argnext[0]);
                    exit(0);
                }
                fd2 = open("/tmp/youdonotknowfile",O_RDONLY);
                dup2(fd2,0);
                execvp(argnext[0],argnext);

                if(remove("/tmp/youdonotknowfile"))
                    printf("remove error\n");
                exit(0);
    		}
            break;
        default:
            break;
    }
    
    //若命令中有&，表示后台执行，父进程直接返回，不等待子进程结束
    if( background == 1)
    {
        printf("[process is %d]\n",pid);
        return;
    }
    
    //父进程等待子进程结束
    if(waitpid (pid, &status,0) == -1)
        printf("wait for child process error\n");
}

//查找命令中的可执行程序
int find_command(char *command)
{
    DIR* dp;
    struct dirent* dirp;
    char *path[] = {"./","/bin","/usr/bin",NULL};
    //使当前目录下的程序可以运行，如命令"./fork"可以被正确解释和执行
    if(strncmp(command,"./",2) == 0)
        command =command +2;
    
    //分别在当前目录、/bin和/usr/bin目录查找要执行的程序
    int i=0;
    while(path[i] != NULL)
    {
        if((dp = opendir(path[i])) == NULL)
            printf("can not open /bin \n");
        while((dirp = readdir(dp)) != NULL)
        {
            if (strcmp(dirp->d_name,command) == 0)
            {
                closedir(dp);
                return 1;
            }
        }
        closedir(dp);
        i++;
    }
    return 0;
}
```

# 线程控制

##### 原子操作

​	原子操作是指不会被线程调度机制打断的操作；这种操作一旦开始，就一直运行到结束，中间不会有任何context switch(切换到另一个线程)。

​	在单核环境中，一般的意义下原子操作中线程不会被切换，线程切换要么在原子操作之前，要么在原子操作完成之后。更广泛的意义下原子操作是指一系列必须整体完成的操作步骤，如果任何一步操作没有完成，那么所有完成的步骤都必须回滚，这样就可以保证要么所有操作步骤都未完成，要么所有操作步骤都被完成。

## 线程和进程关系

​	线程是计算机中独立运行的最小单位，运行时占用很少的系统资源。由于每个线程占用的CPU时间是由系统分配的，因此可以把线程看成操作系统分配CPU时间的基本单位。在用户看来，多个线程是同时执行的，但从操作系统调度上看，**各个线程是交替执行的**。系统不停地在各个线程之间切换，每个线程只有在系统分配给它的时间片内才能取得CPU的控制权，执行线程中的代码。

​	**注：**这里只是针对单CPU单核的情况，在多CPU多核的主机上，多个线程是可以同时运行的

​	Linux系统是支持多线程的，它在一个进程内生成了许多个线程。一个进程可以拥有一至多个线程。多线程相对于多进程有以下优点：

- 在多进程情况下，每个进程都有自己独立的地址空间，而在多线程情况下，同一进程内的线程共享进程的地址空间。因此，创建一个新的进程时就要耗费时间来为其分配系统资源，而创建一个新的线程花费的时间则要少得多。
- 在系统调度方面，由于进程地址空间独立而线程共享地址空间，线程间的切换速度要远远快过进程间的切换速度。
- 在通信机制方面，进程间的数据空间相互独立，彼此通信要以专门的通信方式进行，通信时必须经过操作系统。而同一进程内的多个线程共享数据空间，一个线程的数据可以直接提供给其他线程使用，而不必经过操作系统。因此，线程间的通信更加方便和省时。
- 可以提高应用程序的响应速度。在图像界面程序中，如果有一个非常耗时的操作，它会导致其他操作不能进行而等待这个操作，此时界面响应用户操作的速度会变得很慢。多线程环境下可以将这个非常耗时的操作由一个单独的线程来完成。这个线程在用完操作系统分配给它的时间片后，让出CPU，这样其他操作便有机会执行了。
- 可以提高多处理器效率。现在许多计算机都是采用多核技术，在这种情况下，可以让多个线程在不同的处理器上同时处理，从而大大提高程序执行速度。因此，多线程更能发挥硬件的潜力。
- 可以改善程序的结构。对于要处理多个命令的应用程序，可以将对每个命令的处理设计成一个线程，从而避免设计成大程序时造成的程序结构复杂。

​	虽然线程在进程内部共享地址空间、打开的文件描述符等资源。但是线程也有其私有的数据信息，包括：

- 线程号(thread ID)：每个线程都有一个唯一的线程号一一对应
- 寄存器(包括程序计数器和堆栈指针)
- 堆栈
- 信号掩码
- 优先级
- 线程私有的存储空间

​	Linux系统支持POSIX多线程接口，成为pthread(Posix Thread的简称)。编写Linux下的多线程应用程序，**需要使用头文件pthread.h**，**链接时需要使用库libpthread.h**

## 创建线程

### 线程创建函数pthread_create

​	单线程的程序都是按照一定的顺序执行的，如果在主线程里创建线程，程序就会在创建线程的地方产生分支，变成老哥程序执行。这似乎和多进程一样，其实不然。子进程是通过拷贝父进程的地址空间来实现的；而线程与进程内的线程共享程序代码，一段代码可以同时被多个线程执行。

​	线程的创建通过函数pthread_create来完成，该函数的声明如下：

```c
#include <pthread.h>
int pthread_create (pthread_t *thread,pthread_attr_t * attr,void* (*start_routine)(void*),void*arg);
```

函数各参数含义如下：

- **thread**：该参数是一个指针，当线程创建成功时，用来返回创建的线程ID
- **attr**：该参数用于指定线程的属性，NULL表示使用默认属性。
- **start_routine**：该参数为一个函数指针，指向线程创建后要调用的函数。这个被线程调用的函数也称为线程函数。
- **arg**：该参数指向传递给线程函数的参数。

**注：**线程创建成功时，pthread_create函数返回0，若不为0则说明创建线程失败。常见的错误码为EAGAIN和EINVAL。前者表示系统限制创建新的线程，例如，线程数目过多；后者表示第2个参数代表的线程属性值非法。线程创建成功后，新创建的线程开始运行第3个参数所指向的函数，原来的线程继续运行。

​	pthread_create函数的第2个参数attr是一个指向pthread_attr_t结构体的指针，该结构体指明待创建线程的属性。

​	在头文件pthread.h中还声明了其他一些有用的系统调用。

|                          函数                           |              说明              |
| :-----------------------------------------------------: | :----------------------------: |
|               pthread_t pthread_sel(void)               |       获取本线程的线程ID       |
| int pthread_equal(pthread_t thread l,pthread_t thread2) | 判断两个线程ID是否指向同一线程 |

```c
int pthread_one(pthread_once_t *once_control,void(*init_routine)(void) //用来保证init_routine线程函数在进程中仅执行一次
```

线程的创建过程，createthread.c：

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

int *thread(void *arg)
{
	pthread_t newthid;
    
    newthid = pthread_self();
    printf("this is a new thread,thread ID = %u\n",newthid);
    return NULL;
}

int main(void)
{
    pthread_t thid;
    printf("main thread, ID is %u\n",pthread_self()); //打印主线程的ID
    if(pthread_create(&thid,NULL,(void*)thread,NULL) !=0)
    {
        printf("thread creation failed\n");
        exit(1);
    }
    sleep(1);
    exit(0);
}
```

编译并运行程序：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/thread.png)

**一定要链接lpthread库**

​	程序首先打印出主线程的ID，然后打印新创建的线程的ID。

​	在某些情况下，函数执行次数要被限制为一次，这种情况下就要使用pthread_once函数。

这个程序创建两个线程，分别通过pthread_once调用同一个函数，结果被调用的函数只被执行了一次。

```c
#include <stdio.h>
#include <pthread.h>

pthread_once_t once = PTHREAD_ONCE_INIT;
void run(void)
{
    printf("Fuction run is running in thread %u\n",pthread_self());
}

void *thread1(void*arg)
{
    pthread_t thid=pthread_self();
    printf("Current thread's Id is %u\n",thid);
    pthread_once(&once,run);
    printf("thread1 ends\n");
}

void * thread2(void*arg)
{
    pthread_t thid = pthread_self();
    printf("Current thread's ID is %u\n",thid);
    pthread_once(&once,run);
    printf("thread2 ends\n");
}

int main()
{
    pthread_t thid1,thid2;
    
    pthread_create(&thid1,NULL,thread1,NULL);
    pthread_create(&thid2,NULL,thread2,NULL);
    sleep(3);
    printf("main thread exit!\n");
    exit(0);
}
```

​	编译并运行，结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/threadcreate.png)

​	从结果可以看出函数run在线程thread1中运行了一次，线程thread2虽然也调用了run函数，但是并未执行

### 线程属性

​	线程创建函数pthread_create有一个参数的类型为pthread_attr_t，该结构体的定义如下：

```c
typedef struct{
    int					detachstate;
    int					schedpolicy;
    struct sched_param	schedparam;
    int					inheritsched;
    int					scope;
    size_t				guardsize;
    int					stackaddr_set;
    void *				stackaddr;
    size_t				stacksize;
}	pthread_attr_t;
```

各个字段的含义如下：

- **detachstate**:表示新创建的线程是否与进程中其他的线程脱离同步。detachstate的缺省值为PTHREAD_CREATE_JOINABLE状态，这个属性也可以用函数pthread_detach()来设置。如果将detachstate设置为PTHREAD_CREATE_DETACH状态，则detachstate不能再恢复到PTHREAD_CREATE_JOINABLE状态
- **schedpolicy**：表示新线程的调度策略，主要包括SCHED_OTHER(正常、非实时)、SCHED_RR(实时、轮转法)和SCHED_FIFO(实时、先进先出)3种，缺省为SCHED_OTHER，后两种调度策略仅对超级用户有效。
- **shedparam**：一个struct sched_param结构，其中有一个sched_priority整形变量表示线程的运行优先级。这个参数仅当调度政策为实时(即SCHED_RR或SCHED_FIFO)时才有效，缺省为0.
- **inheritsched**：有两种值可供选择，PTHREAD_EXPLICIT和PTHREAD_INHERIT_SCHED，前者表示新线程显式指定调度策略和调度参数(即attr中的值)，而后者表示继承调用者线程的值。缺省为PTHREAD_EXPLICIT_SCHED。
- **scope**：表示线程间竞争CPU的范围，也就是说，线程优先级的有效范围。POSIX的标准中定义了两个值，PTHREAD_SCOPE_SYSTEM和PTHREAD_SCOPE_PROCESS，前者表示与系统中所有线程一起竞争CPU，后者表示仅与同进程中的线程竞争CPU。
- **guardsize**：警戒堆栈的大小。
- **stackaddr_set**：堆栈地址集
- **stackaddr**：堆栈的地址
- **stacksize**：堆栈的大小

Linux下提供了这些状态的获取和设置函数。这些函数声明如下：

```c
#include <pthread.h>
//返回detachstate属性
int pthread_attr_getdetachstate(pthread_attr_t * attr,int * detachstate);
//设置detachstate属性，将attr中的detachstate设置为提供的detachstate
int pthread_attr_setdetachstate(pthread_attr_t * attr,int detachstate);
...
```

## 线程终止

​	Linux下有两种方式可以使线程终止。第一种通过return从线程函数返回，第二种使通过调用函数pthread_exit()使线程退出。pthread_exit在头文pthread.h中声明，该函数原型如下：

```c
#include<pthread.h>
void pthread_exit(void * retval);
```

​	有两种特殊情况要注意：一种情况是，在主线程中，如果从main函数返回或是调用了exit函数退出主线程，那整个进程将终止，此时进程中所有线程也将终止，因此在主线程中不能过早地从main函数返回；另种情况是如果主线程调用pthread_exit函数，则仅仅是主线程消亡，进程不会结束，进程内的其他线程也不会终止，直到所有线程结束，进程才会结束。

​	线程终止最重要地问题是资源释放的问题，特别是一些临界资源。临界资源在一段时间内只能被一个线程所持有，当线程要使用临界资源时提出请求，如果该资源未被使用则申请成功，否则等待。临界资源使用完毕后要释放以便其他线程可以使用。例如，某线程要写一个文件，在写文件时一般不允许其他线程也对该文件执行写操作的，否则会导致文件数据混乱。这里的文件就是一种临界资源。

​	临界资源为一个线程所独占，当一个线程终止时，如果不是放其占有的临界资源，则该资源会被认为还被已经退出的线程所使用，因而永远不会得到释放。如果一个线程在等待使用这个临界资源，它就可能无限的等待下去，这就形成了死锁，而这往往是灾难性的。

#### pthread_cleanup_push

​	为此，Linux系统提供了一对函数：pthread_cleanup_push()、pthread_cleanup_pop()用于自动释放资源。从pthread_cleanup_push()的调用点到pthread_cleanup_pop()之间的程序段中的终止动作(如调用pthread_exit)都将执行pthread_cleanup_push()所指定的清理函数。这两个函数是以宏形式提供的：

```c
#include<pthread.h>
#define pthread_cleanup_push(routine,arg)\
{	struct _pthread_cleanup_buffer buffer;\
	_pthread_cleanup_push(&buffer,(routine),(srg));
#define pthread_cleanup_pop \
	_pthread_cleanup_pop(&buffer,(exeute));}
```

**注：**pthread_cleanup_push()带有一个"{",而pthread_cleanup_pop带有一个"}"，因此这两个函数必须成对出现，且必须位于程序的同一代码段中才能通过编译。

​	线程终止时另外一个要注意的问题是线程间的同步问题。一般情况下，进程中各个线程的运行是相互独立的，线程的终止并不会相互通知，也不会影响其他线程，终止的线程所占用的资源不会随着线程的终止而归还系统，而是仍为线程所在的进程持有。正如进程之间可以使用wait()系统调用来等待其他进程结束一样，线程也有类似的函数：pthread_join()。该函数也在头文件pthread.h中声明：

```c
#include <pthread.h>
void pthread_exit(void* retval);
int pthread_join(pthread_t th,void * thread_return);
int pthread_detach(pthread_t th);
```

​	函数pthread_join用来等待一个线程的结束。pthread_join()的调用者将被挂起并等待th线程终止，如果thread_return不为NULL，则*thread_return=retval。需要注意的是一个进程仅允许一个线程使用pthread_join()等待它的终止，并且被等待的线程应该处于可join状态，即非DETACHED状态。DETACHED状态是指某个线程执行pthread_detach()后其所处的状态。处于DETACHED状态的线程无法由pthread_join()同步。

​	一个可"join"的线程所占用的内存仅当有线程对其执行了pthread_join()后才会释放，因此为了避免内存泄漏，所有线程在终止时，要么已经设为DETACHED，要么使用pthread_join来回收资源。

**注：**一个线程不能被多个线程等待，否则第一个接收到信号的线程成功返回。其余调用pthread_join()的线程返回错误代码ESRCH。

主线程通过pthread_join等待辅助线程结束：jointhread.c

```c
#include <stdio.h>
#include <pthread.h>

void assisthread(void *arg)
{
    printf("help to da some jobs\n");
    sleep(3);
    pthread_exit(0);
}

int main(void)
{
    pthread_t		assistthid;
    int				status;
    
    pthread_create(&assistthid,NULL,(void *) assisthread,NULL);
    pthread_join(assistthid,(void *)&status);
    printf("assistthread's exit is caused %d\n",status);
    
    return 0;
}
```

​	运行结果：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/threadjoin.png)

​	从运行结果可以看出pthread_join会阻塞主线程，等待线程assisthread结束。pthread_exit结束时的退出码是0，pthread_join得出status也为0，两者是一致的。

## 私有数据

​	在多线程环境下，进程内的所有线程共享进程的数据空间，因此全局变量为所有线程共有。在程序设计中有时需要保存线程自己的全局变量，这种特殊的变量仅在某个线程内部有效。如常见的变量errno，它返回标准的出错代码。errno不应该是一个局部变量，几乎每个函数都应该可以访问它；但它又不能作为一个全局变量，否则在一个线程里输出的很可能是另一个线程的出错信息，这个问题可以通过创建线程的私有数据(THread-specific Data,或TSD)来解决。在线程内部，线程私有数据可以被各个函数访问，但它对其他线程是屏蔽的。

​	线程私有数据采用了一种被称为一键多值的技术，即一个键对应多个数值。访问数据时都是通过键值来访问，好像是对一个变量进行访问，其实是在访问不同的数据。使用线程私有数据时，首先要为每个线程数据创建一个相关联的键。在各个线程内部，都使用这个公用的键来指代线程数据，但是在不同的线程中，这个键代表的数据是不同的。操作线程私有数据的函数主要有4个：pthread_key_create(创建一个键)，pthread_setspecific(为一个键设置线程私有数据)，pthread_getspecific(从一个键读取线程私有数据)，pthread_key_delete(删除一个键)、这几个函数的声明如下：

```c
#include<pthread.h>
int pthread_key_create(pthread_key_t *key,void (*destr_funciton) (void*));
int pthread_setspecific(pthread_key_t key,const void *pointer);
void *pthread_getspecific(pthread_key_t key);
int pthread_key_delete(pthread_key_t key);
```

- **pthread_key_create**：从Linux的TSD池中分配一项，将其赋值给key供以后访问使用，它的第一个参数key为指向键值的指针，第二个参数为一个函数指针，如果指针不为空，则在线程退出时将以key所关联的数据为参数引用destr_function()，释放分配的缓冲区。

  ​	key一旦被创建，所有线程都可访问它，但各线程可根据自己的需要往key中填入不同的值，这就相当于提供了一个同名而不同值的全局变量，一键多值。一键多值靠的是一个关键数据结构数组，即TSD池，其结构如下：

  ```c
  static struct pthread_key_struct pthread_key [PTHREAD_KEYS_MAX] = {{0,NULL} };
  ```

  ​	创建一个TSD就相当于将结构数组中的某一项设置为"in_use"，并将其索引返回给*key，然后设置destructor函数为destr_function。

- **pthread_setspecific**：该函数将pointer的值(不是内容)与key相关联。用pthread_setspecific为一个键指定新的线程数据时，线程必须先释放原有的线程数据用以回收空间

- **pthread_getspecific**：通过该函数得到与key相关联的数据

- **pthread_key_delete**：该函数用来删除一个键，删除后，键所占用的内存将被释放。需要注意的是，**键占用的内存被释放**，与改键关联的线程数据所占用的数据并不被释放。因此，线程数据的释放必须在释放键之前完成。

创建和使用线程的私有数据：

```c
#include <stdio.h>
#include <string.h>
#include <pthread.h>
#include <stdlib.h>

pthread_key_t key;

void * thread2(void *arg)
{
    int tsd = 5;
    printf("thread %d is running\n",pthread_self());
    pthread_setspecific(key,(void *) tsd);
    printf("thread %d returns %d\n",pthread_self(),pthread_getspecific(key));
}

void * thread1(void *arg)
{
    int tsd = 0;
    pthread_t thid2;
    
    printf("thread %d is running\n",pthread_self());
    pthread_setspecific(key,(void *) tsd);
    pthread_create(&thid2,NULL,thread2,NULL);
    sleepp(5);
    printf("thread %d returns %d\n",pthread_self(),pthread_getspecific(key));
}

int main(void)
{
    pthread_t thid1;
    printf("main thread begins running\n");
    pthread_key_create(&key,NULL);
    pthread_create(&thid1,NULL,thread1,NULL);
    sleep(3);
    pthread_key_delete(key);
    printf("main thread exit\n");
    return 0;
}
```

编译并运行，结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/tsd.png)

其中在打印线程id时出现了负数，原因是：

```c
typedef unsigned long int pthread_t; 
```

修改成%lu后正常。

​	程序中，主线程创建了线程thread1，线程thread1创建了线程thread2。两个线程分别将tsd作为线程私有数据。从程序运行结果可以看出，两个线程tsd的修改互不干扰，可以看出thread2先于thread1结束，线程在创建thread2后，睡眠5s等待thread2执行完毕。可以看出thread2对tsd的修改没有影响刀thread1的tsd的取值。

## 线程同步

​	线程最大的特点就是资源的共享性，然后资源共享中的同步问题时多线程编程的难点。Linux系统提供了多种方式处理线程间的同步问题，其中最常用的有互斥锁、条件变量和异步信号。

### 互斥锁

​	互斥锁通过锁机制来实现线程间的同步。在同一时刻它通常只允许一个线程执行一个关键部分的代码。

​	**互斥锁是一种独占锁，同一时间只有一个线程可以访问共享的数据资源、**

互斥锁函数：

|         函数          |                     功能                      |
| :-------------------: | :-------------------------------------------: |
|  pthread_mutex_init   |               初始化一个互斥锁                |
| pthread_mutex_destroy |                注销一个互斥锁                 |
|  pthread_mutex_lock   |          加锁，如果不成功，阻塞等待           |
| pthread_mutex_unlock  |                     解锁                      |
| pthread_mutex_trylock | 测试加锁，如果不成功则立即返回，错误码为EBUSY |

​	这些函数均声明在头文件pthread.h中

​	使用互斥锁前必须先进行初始化操作。初始化有两种方式，一种是静态赋值法，将宏结构变量PTHREAD_MUTEX_INITIALIZER赋给互斥锁，操作语句：

```c
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
```

​	另外一种方式是通过pthread_mutex_init函数初始化互斥锁，该函数的原型如下：

```c
int pthread_mutex_init (pthread_mutex_t *mutex, const pthread_mutexattr_t *mutexattr);
```

​	函数中的mutexattr表示互斥锁的属性，如果为NULL则使用默认属性。互斥锁的属性以意义如下：

|           属性值            |                             意义                             |
| :-------------------------: | :----------------------------------------------------------: |
|   PTHREAD_MUTEX_TIMED_NP    | 普通锁：当一个线程加锁后，其余请求锁的线程形成等待队列，解锁后按优先级获得锁 |
| PTHREAD_MUTEX_RECURSIVE_NP  | 嵌套锁：允许一个线程对同一个锁多次加锁，并通过多次unlock解锁，如果不是同线程请求，则在解锁时重新竞争 |
| PTHREAD_MUTEX_ERRORCHECK_NP | 检错锁：在同一个线程请求同一个锁的情况下，返回EDEADLK，否则执行的动作与类型PTHREAD_MUTEX_TIMED_NP相同 |
|  PTHREAD_MUTEX_ADAPTIVE_NP  |                    适应锁：解锁后重新竞争                    |

​	初始化以后，就可以给互斥锁加锁了。加锁有两个函数:

```c
int pthread_mutex_lock(pthread_mutex_t *mutex);
int pthread_mutex_trylock(pthread_mutex_t *mutex);
```

​	用pthread_mutex_lock加锁时，如果mutex已经被锁住，**当前尝试加锁的线程就会阻塞**，直到互斥锁被其他线程释放。当pthread_mutex_lock函数返回时，说明互斥锁已经被当前线程成功加锁。pthread_mutex_trylock函数则不同，如果mutex已经被加锁，**它将立即返回**，返回的错误码为EBUSY，而不是阻塞等待

​	**注：**加锁时，不论哪种类型的锁，都不可能被两个不同的线程同时得到，其中一个必须等待解锁。在同一进程中的线程，如果加锁后没有解锁，则其他线程将无法再获得该锁。

​	函数pthread_mutex_unlock用来解锁，函数原型如下：

```c
int pthread_mutex_unlock (pthread_mutex_t * mutex);
```

​	用pthread_mutex_unlock函数解锁时，要满足两个条件：一是互斥锁必须处于加锁状态，而是调用本函数的线程必须是给互斥锁加锁的线程。解锁后如果有其他线程在等待互斥锁，等待队列中的第一个线程将获得互斥锁。

​	当一个互斥锁使用完毕后，必须进行清除。清除互斥锁使用函数pthread_mutex_destroy，该函数原型如下：

```c
int pthread_mutex_destroy(pthread_mutex_t *mutex);
```

​	清除一个互斥锁意味着释放它所占用的资源。清除锁时要求当前处于开放状态，若锁处于锁定状态，函数返回EBUSY，该函数成功执行时返回0.由于在Linux中，互斥锁并不占用内存，因此pthread_mutex_destroy()除了接触互斥锁的状态以为没有其他操作。

​	通过互斥锁保护全局变量：

```c
pthread_mutex_t	number_mutex;
int				globalnumber;
void write_globalnumber()
{
	pthread_mutex_lock (&number_mutex);
    globalnumber++;
    pthread_mutex_unlock(&number_mutex);
}

int read_globalnumber()
{
    int temp;
    pthread_mutex_lock(&number_mutex);
    temp= globalnumber;
    pthread_mutex_unlock(&number_mutex);
    return (temp);
}
```

​	上述代码中，两个函数对共享全局变量globalnumber进行读写操作。write_ globalnumber函数使用互斥锁保证在修改变量的时候操作一次执行完毕，不会中断。而read_ globalnumber函数使用互斥锁保证在读数据的时候，全局变量globalnumber不会被修改，确保读到正确的数据。

### 条件变量

​	条件变量是利用线程间共享的全局变量进行同步的一种机制。条件变量宏观上类似if语句，符合条件就能执行某段程序，否则只能等待条件成立。

​	使用条件变量主要包括两个动作：一个等待使用资源的线程等待"条件变量被设置为真"：另一个线程在使用完资源后"设置条件为真"，这样就可以保证线程间的同步了。这样就存在一个关键问题，就是要保证条件变量能被正确的修改，条件变量要受到特殊的保护，实际使用中互斥锁扮演着这样一个保护者的角色。

操作条件变量的函数：

|          函数          |                             功能                             |
| :--------------------: | :----------------------------------------------------------: |
|   pthread_cond_init    |                        初始化条件变量                        |
|   pthread_cond_wait    |                 基于条件变量阻塞，无条件等待                 |
| pthread_cond_timedwait |                阻塞直到指定事件发生，计时等待                |
|  pthread_cond_signal   | 解除特定线程的阻塞，存在多个等待线程时按入队顺序激活其中一个 |
| pthread_cond_broadcast |                      解除所有线程的阻塞                      |
|  pthread_cond_destroy  |                         清除条件变量                         |

​	与互斥锁一样，条件变量的初始化也有两种方式，一种是静态赋值法，将宏结构常量PTHREAD_COND_INITIALIZER赋予互斥锁：

```c
pthread_cond_t cond = PTHREAD_COND_INITIALIZER
```

​	另一种方式是使用函数pthrad_cond_init，它的原型如下：

```c
int pthread_cond_init(pthread_cond_t *cond,pthread_condattr_t *cond_attr);
```

​	其中，cond_attr参数是条件变量的属性，由于其没有得到实现，所以它的值通常是NULL。

​	等待条件成立有两个函数：pthread_cond_wait和pthread_cond_timedwait。它们的原型如下：

```c
int pthread_cond_wait(pthread_cond_t *cond,pthread_mutex_t *mutex);
int pthread_cond_timedwait(pthread_cond_t *cond,pthread_mutex_t *mutex,const struct timespec *abstime);
```

​	pthread_cond_wait函数释放由mutex指向的互斥锁，同时使当前线程关于cond指向的条件变量阻塞，直到条件被信号唤醒。通常条件表达式在互斥锁的保护下求值，如果条件表达式为假，那么线程基本条件变量阻塞。当一个线程改变条件变量的值时，条件变量获得一个信号，使得等待条件变量的线程退出阻塞状态。

​	pthread_cond_timedwait函数和pthread_cond_wait函数用法类似，差别在于pthread_cond_timedwait函数将阻塞直到条件变量获得信号或者经过由abstime指定的时间，也就是说，如果**在给定时刻前条件没有满足**，则返回ETIMEOUT，结束等待。

​	线程被条件变量阻塞后，可通过函数pthread_cond_signal和pthread_cond_broadcast激活，它们的原型如下：

```c
int pthread_cond_signal(pthread_cond_t *cond);
int pthread_cond_broadcast(pthread_cond_t *cond);
```

​	pthread_cond_signal()激活一个等待条件成立的线程，存在多个等待线程时，按入队顺序激活其中一个；而pthread_cond_broadcast()则激活所有等待线程。

​	当一个条件变量不再使用时，需要将其清除。清除一个条件变量通过调用pthread_cond_destroy()实现，函数原型如下：

```c
int pthread_cond_destroy(pthread_cond_t *cond)
```

​	pthread_cond_destroy函数清除由cond指向的条件变量。注意：只有在没有线程等待该条件变量的时候才能清楚这个条件变量，否则返回EBUSY。

条件变量使用方法：

```c
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>

pthread_mutex_t	mutex;
pthread_cond_t cond;

void * thread1(void *arg)
{
    pthread_cleanup_push(pthread_mutex_unlock,&mutex);
    
    while(1)
    {
        printf("thread1 is running\n");
        pthread_mutex_lock(&mutex);
        pthread_cond_wait(&cond,&mutex);
        printf("thread1 applied the condition\n");
        pthread_mutex_unlock(&mutex);
        sleep(4);
    }
    pthread_cleanup_pop(0);
}

void *thread2(void *arg)
{
    while(1)
    {
        printf("thread2 is running\n");
        pthread_mutex_lock (&mutex);
        pthread_cond_wait(&cond,&mutex);
        printf("thread2 applied the condition\n");
        pthread_mutex_unlock(&mutex);
        sllep(1);
    }
}

int main(void)
{
    pthread_t tid1,tid2;
    
    printf("condition variable stydy\n");
    pthread_mutex_init (&mutex,NULL);
    pthread_cond_init(&cond,NULL);
    pthread_create(&tid1,NULL,(void*) thread1,NULL);
    pthread_create(&tid2,NULL,(void*) tjread2,NULL);
    
    do
    {
        pthread_con_signal(&cond);
    } while(1);
    sleep(50);
    pthread_exit(0);
}
```

结果如下

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/cond.png)

​	thread1和thread2通过条件变量同步运行。在线程函数thread1中可以看到**条件变量使用时要配合互斥锁使用**，这样可以防止多个线程同时请求pthread_cond_wait()。调用pthread_cond_wait()前必须由本线程加锁(pthread_mutex_lock())，而在更新条件等待队列以前，mutex保持锁定状态，并在线程挂起前解锁。在条件满足离开pthread_cond_wait()之前，mutex将被重新加锁，与进入pthread_cond_wait()前的加锁动作对应。

​	函数pthread_cleanup_push和pthread_cleanup_pop提供回调函数保护。pthread_cond_wait()和pthread_cond_timedwait()都被实现为取消点，因此，在该处等待的线程将立即重新运行，在锁定mutex后退出pthread_cond_wai()，然后执行取消操作。也就是说，如果pthread_cond_wait()被取消，mutex将依然保持锁定状态，那么thread1就需要定义推出回调函数来为其解锁。

### 异步信号

​	在Linux操作系统中，线程实在内核外实现的，进程是在内核中实现的。Linux线程本质上是轻量级的进程。信号可以被进程用来进行相互通信，一个线程通过信号通知另一个进程发生了某事件，比如该进程所需要的输入数据已经就绪。线程同进程一样也可以接收和处理信号，信号也是一种线程间同步的手段。

​	信号(如SIGINT和SIGIO)与任何线程都是异步的，也就是说信号到达线程的时间是不定的。如果有多个线程可以接收异步信号，则只有一个被选中。如果并发的多个同意的信号被送到一个进程，每一个将被不同的线程处理。如果所有的线程都屏蔽该信号，则这些信号将被挂起，直到有信号接触屏蔽来处理它们。

​	Linux多线程扩展函数中有三个函数用于处理异步信号：

```c
int pthread_kill(pthread_t threadid,int signo);
int pthread_sigmask (int how,const sigset_t *newmask,sigset_t *oldmask);
int sigwait(const sigset_t *set,int *sig);
```

​	其中，函数pthread_kill用来向特定的线程发送信号signo。函数pthread_sigmask用来设置线程的信号屏蔽码，但对不允许屏蔽的Cancel信号和不允许相应的Restart信号进行了保护。函数sigwait用来阻塞线程，等待set中指定的信号之一到达，并将到达的信号存入*sig中。

## 出错处理

### 错误检查

​	函数执行失败时，一般都会返回一个特定的值，比如-1、空指针。这些值只能说明有错误发生，但错误的原因并没有表明。头文件errno.h定义了变量errno(含义是error number)，它存储了错误发生时的错误码，通过错误码可以得到错误的描述信息。

errno.h的一部分：

```c
#include<errno.h>
#ifndef errno
extern int errnol
#endif
```

​		程序开始执行时，变量errno被初始化为0.很多库函数在执行过程中遇到错误时就会将errno设置为相应的错误码。函数被成功调用时，它们不修改errno的值。因此，当一个函数成功调用，errno的值可能不为零，它的非零值由前面的函数设置。所以不能根据errno的值来判断一个函数执行是否成功。当函数调用失败时(函数返回-1或NULL)，errno值才有意义。

​	打开文件失败时的errno值：

```c
#include<stdio.h>
#include<stdlib.h>
#include<errno.h>
int main()
{
    FILE *stream;
    char	*filename = "test";
    
    errno = 0;
    stream = fopen(filename,"r");
    if(stream == NULL)
        printf("open file %s failed,errno is %d\n",filename,errno);
    else
        printf("open file %s successfully\n",filename);
}
```

​	程序中使用C语言的库函数fopen打开名为"test"的文件，如果打开失败就打印出对应的errno值。如果该文件不存在将输出：

```bash
open file test failed, errno is 2
```

​	由errno的值可以查出2对应的错误码ENOENT，得出出错原因是：文件或目录不存在。

​	如果是文件不具有读写权限，则errno的值将为13，对应错误码为EPERM。

### 错误码

​	错误码是一些定义在erron.h中的宏，通常以字母E(代表error)开头，后面由一串大写字母或数字组成。

​	**注**：在自定义宏时，要避免与这些保留宏名冲突。除了EWOULDBLOCK和EAGAIN具有相同的值，其余所有的错误码的值都是非负且惟一的，，因此在switch语句中可以使用它们。

​	部分常见错误码解释如下：

- **ENOMEM**：表示内存不足，系统不能再提供更多的虚拟内存
- **EIO**：输入输出错误，在读写磁盘时经常会遇到
- **ENXIO**：指定的设备或地址不存在
- **EPERM**：禁止操作，只有具备相应权限的进程才能执行该操作
- **ESRCH**：没有进程与给定的进程ID相匹配
- **ENOENT**：文件或目录不存在
- **EINTR**：函数调用被中断，如果发生这种错误，要重新调用函数
- **E2BIG**：参数过长
- **ENOEXEC**：可执行文件格式无效
- **EBADF**：文件描述符错误
- **ECHILD**：子进程 不存在
- **EBUSY**：资源正在使用，不能共享
- **EINVAL**：无效的参数
- **EMFILE**：当前进程打开的文件已达上限，不能再打开其他文件
- **ENFILE**：系统打开的文件已达上限
- **EFBIG**：文件太大
- **ENOTDIR**：当需要目录的时候指定了一个非目录的文件
- **EISDIR**：文件是一个目录
- **ENOTTY**：不适当的I/O控制操作
- **ETXTBSY**：尝试执行一个正在进行写操作的文件或者尝试写一个正在执行的文件
- **ENOSPC**：设备上无剩余空间

### 错误的提示信息

​	当程序出现错误时，可以打印出相应的错误提示信息，以便用户修改该错误。函数strerror和perror可以通过错误码获取标准的错误提示信息。

#### strerror

​	该函数在头文件<string.h>中声明：

```c
#include <string.h>
char * strerror(int errnum);
```

​	strerror函数根据参数errnum提供的错误码获取一个描述错误信息的字符串，函数的返回值为指向该字符串的指针。errnum的值通常就是errno。

#### perror

​	该函数在头文件<stdio.h>中

```c
#include <stdio.h>
void perror(const char *message);
```

​	perror()打印错误信息到stderr，stderr在Linux中通常就是指屏幕或命令行终端。调用perror()时，如果参数message是一个空指针，perror仅仅根据errno打印出对应的错误提示信息。如果提供要给非空的值，perror会把此message加在其输出信息的前面。perror会添加一个冒号和空格将message和错误信息分开，以便区分。

errshow.c：

```c
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
FILE* open_file(char * filename)
{
    FILE* stream;
    errno = 0;
    stream = fopen(filename,"r");
    if(stream == NULL)
    {
        printf("can not open the file %s.reason: %s\n",filename,strerror(errno));
        exit(-1);
    }
    else
        return stream;
}

int main(void)
{
    char *filename ="test";
    open_file (filename);
    return 0;
}
```

​	调用open_file打开文件test，当打开失败时，将打印出相应的错误提示信息，错误提示信息由函数strerror根据errno获得。

​	当文件不存在时：

```c
couldn't open file test. reason:No such file  or directory
```

​	在建立一个test文件后并屏蔽其读权限后：

```bash
could't open file test. reason:Permission denied
```

# 信号

## Linux信号介绍

​	信号(signal)是一种软件中断，它提供了一种处理异步事件的方法，也是进程间唯一的异步通信方式。在Linux系统中，根据POSIX标准扩展以后的信号机制，不仅可以用来通知某进程发生了什么事，还可以给进程传递数据。

### 信号的来源

​	信号的来源按产生条件不同可以分为硬件和软件两种：

#### 硬件方式

- 当用户在终端上按下某些键时，将产生信号。如按下<Ctrl+C>组合键后将产生一个SIGINT信号
- 硬件异常产生信号：除数为0、无效的存储访问等，这些事件通常由硬件(如CPU)检测到，并将其通知给Linux操作系统内核，然后内核生成相应的信号，并把信号发送给该事件发生时正在运行的程序。

#### 软件方式

- 用户在终端下调用kill命令向进程发送任意信号
- 进程调用kill或sigqueue函数发送信号
- 当检测到某种软件条件已经具备时发出信号，如由alarm或settimer设置的定时器超时时将生成SIGALRM信号。

### 信号的种类

​	在Shell下输入kill -l可显示Linux系统支持的全部信号

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/kill-l.png)



信号的值在signal.h中定义，上面这些信号的含义如下：

1. **SIGHUP**：当用户退出Shell时，由该Shell启动的所有进程将收到这个信号，默认动作为终止进程。
2. **SIGINT**：用户按下了<Ctrl+C>组合键时，用户终端向正在运行中的由该终端启动的程序发出此信号。默认动作为终止进程。
3. **SIGQUIT**：当用户按下<Ctrl + \\>组合键时产生该信号，用户终端向正在运行中的由该终端启动的程序发出此信号。默认动作为终止进程并产生core文件
4. **SIGILL**：CPU检测到某进程执行了非法指令。默认动作为终止进程并产生core文件
5. **SIGTRAP**：该信号由断点指令或其他trap指令产生。默认动作为终止进程并产生core文件
6. **SIGABRT**：调用abort函数时产生该符号。默认动作为终止进程并产生core文件
7. **SIGBUS**：非法访问内存地址，包括内存地址对齐(alignment)出错，默认动作为终止进程并产生core文件。
8. **SIGFPE**：在发生致命的算术运算错误时发出。不仅包括浮点运算错误，还包括溢出及除数为0等所有的算术错误。默认动作为终止进程并产生core文件
9. **SIGKILL**：无条件终止进程。本信号不能被忽略、处理和阻塞。默认动作为终止进程。它向系统管理员提供了一种可以杀死任何进程的办法。
10. **SIGUSR1**：用户定义的信号，即程序员可以在程序中定义并使用该信号。默认动作为终止进程。
11. **SIGSEGV**：指示进程进行了无效的内存访问。默认动作为终止进程并产生core文件
12. **SIGUSR2**：这是另外一个用户自定义信号，程序员可以在程序中定义并使用该信号。默认动作为终止进程。
13. **SIGPIPE**：Broken pipe：向一个没有读端的管道写数据。默认动作为终止进程
14. **SIGALRM**：定时器超时，超时的时间由系统调用alarm设置。默认动作为终止进程。
15. **SIGTERM**：程序结束(terminate)信号，与SIGKILL不同的是，该信号可以被阻塞和处理。通常用来要求程序正常退出。执行Shell命令kill时，缺省产生这个信号。默认动作为终止进程。
16. **SIGCHLD**：子进程结束时，父进程会收到这个信号。默认动作为忽略该信号。
17. **SIGCONT**：让一个暂停的进程继续执行
18. **SIGSTOP**：停止(stopped)进程的执行。注意它和SIGTERM以及SIGINT的区别：该进程还未结束，只是暂停执行。本信号不能被忽略、处理或阻塞。默认动作为暂停进程。
19. **SIGTSTP**：停止进程的运行，但该信号可以被处理和忽略。按下<Ctrl+Z>组合键时发出这个信号。默认动作为暂停进程
20. **SIGTTIN**：当后台进程要从用户终端读数据时，该终端中的所有进程会收到SIGTTIN信号。默认动作为暂停进程。
21. **SIGTTOU**：该信号类似于SIGTTIN，在后台进程要向终端输出数据时产生。默认动作为暂停进程。
22. **SIGURG**：套接字(socket)上有紧急数据时，向当前正在运行的进程发出此信号，报告有紧急数据到达。默认动作为忽略该信号
23. **SIGXCPU**：进程执行时间超过了分配给该进程的CPU时间，系统产生该信号并发送给该进程。默认动作为终止进程。
24. **SIGXFSZ**：超过文件最大长度的限制。默认动作为终止程序并产生core文件。
25. **SIGVTALRM**：虚拟始终超时时产生该信号。类似于SIGALRM，但是它只计算该进程占用的CPU时间。默认动作为终止进程。
26. **SIGPROF**：类似于SIGVTALRM，它不仅包括该进程占用的CPU时间还包括执行系统调用的时间。默认动作为终止进程。
27. **SIGWINCH**：窗口大小改变时发出。默认动作为忽略该信号。
28. **SIGIO**：此信号向进程指示发生了一个异步IO时事件。默认动作为忽略。
29. **SIGPWR**：关机。默认动作为终止进程
30. **SIGSYS**：无效的系统调用。默认动作为终止进程并产生core文件
31. **SIGRTMIN** ~64 **SIGRTMAX**：Linux的实时信号，它们没有固定的含义(或者可以说由用户自由使用)。注意，Linux线程机制使用了前3个实时信号。所有的实时信号的默认动作都是终止进程。

#### 可靠信号与不可靠信号

​	SIGHUP(1号)至SIGSYS(31号)之间的信号都是继承自UNIX系统，时不可靠信号。Linux系统根据POSIX.4标准定义了SIGTMIN(33号)与SIGRTMAX(64号)之间的信号，它们都是可靠信号，也成为实时信号，**Linux下没有16和32号信号**。

​	在Linux系统中，**信号的可靠性是指信号是否会丢失，或者说该信号是否支持排队。**当导致产生信号的事件发生时，内核就产生一个信号。信号产生后，内核通常会在进程表中设置某种形式的标志。当内核设置了这个标志，就是内核向一个进程递送了一个信号。信号产生(generate)和递送(delivery)之间的时间间隔，称为信号未决(pending)。

​	进程可以调用sigpending将信号设置为阻塞，如果为进程产生了一个阻塞的信号，而对该信号的动作是捕捉该信号(即不忽略信号)，则内核将为该进程的此信号保持为未决状态，直到该进程对此信号解除阻塞或将对此信号的响应更改为忽略。如果在进程接触对某个信号的阻塞之前，这种信号发生了多次，那么如果信号被递送多次(即信号在未决信号队列里面排队)，则称之为可靠信号；只被递送一次的信号称为不可靠信号。

#### 信号的优先级

​	信号实质上是软中断，中断有优先级，信号也有优先级。如果一个进程有多个未决信号，则对于同一个未决的实时信号，内核将按照发送的顺序来递送信号。如果存在多个未决的实时信号，则值(或者说编号)越小的越先被递送。如果既存在不可靠信号，又存在可靠信号(实时信号)，虽然POSIX对这一情况没有明确规定，但Linux系统和大多数遵循POSIX标准的操作系统一样，将优先递送不可靠信号。

### 进程对信号的相应

​	当信号发生时，用户可以要求进程以下列3种方式之一对信号做出相应。

- 捕捉信号。对于要捕捉的信号，可以为其指定信号处理函数，信号发生时该函数自动被调用，在该函数内部实现对该信号的处理
- 忽略信号。大多数信号都可使用这种方式进行处理，但是SIGKILL和SIGSTOP这两个信号不能被忽略，同时这两个信号也不能被捕获和阻塞。此外，如果忽略某些由硬件异常产生的信号(如非法存储访问或除以0)，则进程的行为时不可预测的。
- 按照系统默认方式处理。大部分信号的默认操作是终止进程，且所有的实时信号的默认动作都是终止进程。

## 信号处理

### 信号的捕捉和处理

​	Linux系统中对信号的处理主要由signal和sigaction函数来完成。

#### signal

signal函数用来设置进程在接收到信号时的动作，在Shell下输入man signal可获得该函数原型：

```c
#include <signal.h>
typedef void (*sighandler_t) (int);
sighandler_t signal(int signum,sighandler_t handler);
```

​	signal会根据参数signum指定的信号编号来设置该信号的处理函数。当指定的信号到达时就会跳转到参数handler指定的函数执行。如果参数handler不是函数指针，则必须是常数SIG_IGN(忽略该信号)或SIG_DFL(对该信号执行默认操作)。handler是一个函数指针，它所指向的函数的类型是sighandler_t，即它所指向的函数有一个int型参数，且返回值的类型为void。

​	signal函数执行成功时返回以前的信号处理函数指针，当有错误发生时则返回SIG_ERR(即-1).

**注：**SIGKILL和SIGSTOP这两个信号不能被捕捉或忽略

​	my_signal.c

```c
#include <stdio.h>
#include <signal.h>
//信号处理函数
void handler_sigint(int signo)
{
    printf("recv SIGINT\n");
}

int main()
{
    //安装信号处理函数
    signal(SIGINT, handler_sigint);
    
    while(1)
        ;
    return 0;
}
```

​	程序首先使用signal()安装信号SIGINT的处理函数handler_sigint，然后进入死循环。当接收到SIGINT信号时，程序自动跳转到信号处理函数处执行，打印出提示信息，然后返回主函数继续死循环。

执行结果：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/sigint.png)

​		执行程序时，用户在终端按下<Ctrl+C>组合键后将向进程发送SIGINT信号，最后按下<Ctrl+\\>组合键向进程发送SIGQUIT信号。由于程序本身并没有处理SIGQUIT信号，按照默认处理方式，进程退出(也可以使用kill命令结束程序)

#### sigaction

​	sigaction函数可以用来检查或设置进程在接收到信号时的动作，在Shell下输入man sigaction可获得该函数的原型：

```c
#include<signal.h>
int sigaction(int signum, const struct sigaction *act,struct sigaction *oldact);
```

​	sigaction会根据参数signum指定的信号编号来设置该信号的处理函数。参数signum可以是SIGKILL和SIGSTOP以外的任何信号。如果参数act不是空指针，则为signum设置新的信号处理函数；如果oldact不是空指针，则旧的信号处理函数将被存储在oldact中。struct sigaction定义如下：

```c
struct sigaction{
	void (*sa_handler)(int);
	void (*sa_sigaction)(int,siginfo_t *,void *);
	sigset_t sa_mask;
	int sa_flags;
 	void (*sa_restorer) (void);
}
```

​	sa_handler和sa_sigaction在某些体系结构上被定义为共用体，即这两个值在某一时刻只有一个有效。

​	数据成员sa_restorer已经作废，不再使用，POSIX标准也不支持该数据成员。 

​	sa_handler可以是常数SIG_DFL或SIG_IGN，或者是一个信号处理函数的函数名。信号处理函数只有一个参数即信号编号。该参数和参数sa_sigaction实际上都是函数指针。

​	sa_sigaction也是用来指定信号signum的处理函数，但是它有3个参数，第一个参数是信号编号；第二个参数是一个指向siginfo_t结构的指针；第三个参数是一个指向任何类型的指针，一般不使用。

​	sa_mask成员声明了一个信号集，在调用信号捕捉函数之前，该信号集会增加到进程的信号屏蔽码中，新的信号屏蔽码会自动包括正在处理的信号(sa_flags未指定 SA_NODEFER或SA_NOMASK)、当从信号捕捉函数返回时，进程的信号屏蔽码会恢复为原来的值。因此，当处理一个给定的信号时，如果这种信号再次发生，那么它会被阻塞直到本次信号处理结束为止。若这种信号发生了多次，则对于不可靠信号，它只会被阻塞一次，即本次信号处理结束以后只会再处理一次(相当于丢失了信号)；对于可靠信号(实时信号)，则会被阻塞多次，即信号不会丢失，信号发生了多少次就会调用信号处理函数多少次。

​	sa_flags成员用来说明信号处理的一些其他相关操作，它可以取以下值或它们的组合。

- SA_NOCLDSTOP：如果参数signum为SIGCHLD，则当子进程暂停时，并不会通知父进程
- SA_ONESHOT或SA_RESETHAND：在调用新的信号处理函数前，将此信号的处理方式改为系统默认的方式。
- SA_ONSTACK：以预先定义好的堆栈调用信号处理函数
- SA_RESTART：被信号中断的系统调用，在信号处理函数执行完毕后会自动重新开始执行(BSD操作系统默认使用该值)、
- SA_NOMASK或SA_NODEFER：在处理此信号 结束前允许此信号再次递送，相当于中断嵌套。
- SA_SIGINFO：如果设置了该标志，则信号处理函数由三参数的sa_sigaction指定而不是sa_handler指定。

​	当使用三参数的sa_sigaction来指定信号处理函数时，它的第二个参数可以用来传递数据，定义如下：

```c
siginfo_t{
    int	 		si_signo;	//信号编号
    int 		si_errno;	//错误码
    int 		si_code;	//信号产生的原因
    pid_t		si_pid;		//接收信号的过程ID
    uid_t		si-uid;		//接收信号的进程的用户ID
    int 		si_status;	//状态编号
    clock_t		si_utime;	//耗费的用户空间的时间
    clock_t		si_stime;	//耗费的系统内核的时间
    sigval_t	si_value;	//信号值
    int			si_int;		//用于传递数据
    void*		si_ptr;		//用户传递数据
    void*		si_addr;	//产生内存访问错误的内存地址
    int			si_band;	//band事件
    int			si_fd;		//文件描述符
}
```

​	其中，所有的信号都有si_signo、si_errno和si_code这三个数据成员，分别表示信号编号，errno值和信号产生的原因。其他成员则根据信号的不同有不同的意义，接收信号的进程只能读这些成员的值，而不能设置。si_int和si_ptr可以用来传递数据。其余的数据成员则根据不同的信号存在不同的组合。

​	sigaction函数执行成功时返回0，当有错误发生时则返回-1，错误代码存入errno中。

**注：**Linux下signal函数是由sigaction函数实现的

my_sigaction.c

```c
#include <stdio.h>
#include <unistd.h>
#include <signal.h>

int temp = 0;

//信号处理函数
void handler_sigint(int signo)
{
    printf("recv SIGINT\n");
    sleep(5);
    temp += 1;
    printf("the value of temp is %d\n",temp);
    printf("int handler_sigint,after sleep\n");
}

int main()
{
    struct sigaction act;
    
    //赋值act结构
    act.sa_handler = handler_sigint;
    act.sa_flags = SA_NOMASK;
    //安装信号处理函数
    sigaction(SIGINT,&act,NULL);
    while(1)
        ;
    return 0;
}
```

​	程序定义了一个act结构，并设置act的sa_handler为信号处理函数handler_sigint，且将sa_flags赋值为SA_NOMASK，即支持信号的嵌套处理。

​	快速按下<Ctrl+c>组合键两次，执行结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/nomask.png)

​	执行程序时，当第一次按下<Ctrl+c>组合键发送出SIGINT信号时，程序打印出"recv SIGINT"表明信号处理程序处理了信号SIGINT，然后sleep(5)睡眠5秒。在5秒内再次按下<Ctrl+c>时，由于我们设定了sa_flags的值为SA_NOMASK，因此程序又响应一次SIGINT，程序从sleep()处嵌套调用信号处理函数handler_sigint，再一次打印出"recv SIGINT"。睡眠5秒之后，将temp的值打印出来并返回到本次信号处理程序的跳入点sleep()处，然后再打印出temp的值并返回到主函数。

​	该程序中temp的值随着信号处理函数被调用的次数的增加而递增，而由于实际应用中信号总是随即发生的，这样temp的值也会随机变化。如果main函数或其他地方还用到了这个全局变量，则程序将产生不可预料的后果。这种数据会被破坏的函数被称为不可重入函数。编写信号处理程序时要注意不要使用不可重入函数。一般来说，满足下列条件之一的函数是不可重入的：

- 使用了静态的数据结构，如getgrgid()，全局变量等
- 函数实现时，调用了malloc()或者free()函数
- 函数实现时，使用了标准I/O函数

​	POSIX.1说明了保证可重入的函数。带*号的函数并没有按POSIX.1说明是可重入的，但SVR4 SVID(AT&T 1989)包括Linux则将它们 列为是可重入的。

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/func.png)

​	将程序中的"act.sa_flags = SA_NOMASK;"这一行注释去掉，然后重新编译程序并执行，同时多次按下<Ctrl+c>，会发生程序响应次数与之不符的情况。

​	因为在此时，sigaction安装默认方式阻塞当前正在处理的信号，因为SIGINT是不可靠信号，不可靠信号不支持排队，从而有可能丢失信号。

#### pause

​	pause函数使调用进程挂起直至捕捉到一个信号。在Shell下输入man pause可获取该函数的原型：

```c
#include <unistd.h>
	int pause(void);
```

​	pause函数会令目前的进程暂停(进入睡眠状态)，直到被信号(signal)所中断、该函数只返回-1并将errno设置为EINTR。

## 信号处理函数的返回

​	信号处理函数可以正常返回，也可以调用其他函数返回到程序的主函数中，而不是从该处理程序返回。正如ANSI C标准所说明的，一个信号处理程序可以返回或者调用abort、exit或longjmp(goto不支持跳出它所在的函数，因此不能用来从信号处理程序返回到主函数中)

##### setjmp/longjmp

​	使用longjmp可以跳转到setjmp设置的位置，它们的原型如下：

```c
#include <setjmp.h>
int setjmp(jmp_buf env);
void longjmp(jmp_buf env,int val);
```

​	参数env是一个特殊类型jmp_buf的变量。这一数据类型是某种形式的数组，其中存放的是在调用longjmp时能用来恢复栈状态的所有信息。一般来说env是个全局变量，因为需从另一个函数中引用它。我们希望可以在返回的位置使用setjmp，直接调用setjmp时返回0；当从longjmp返回时，setjmp的返回值是longjmp的第2个参数的值，可以利用这一点使多个longjmp返回到一个setjmp处。

wrong_return.c

```c
#include<stdio.h>
#include<setjmp.h>
#include<signal.h>

jmp_buf env;//保存待跳转位置的栈信息

//信号SIGRTMIN+15的处理函数
void handler_sigrtmin15(int signo)
{
    printf("recv SIGRTMIN+15\n");
    longjmp(env,1);//返回到env处，注意第二个参数的值
}

//信号SIGRTMAX-13的处理函数
void handler_sigrtmanx15(int signo)
{
    printf("recv SIGRTMAX-13\n");
    longjmp(env,2);//返回到env处，注意第二个参数的值
}

int main()
{
    //设置返回点
    switch(setjmp(env))//该函数默认为0，第一次运行时直接break
    {
        case 0:
            break;
        case 1:
            printf("return from SIGRTMIN+15\n");
            break;
        case 2:
            printf("return from SIGRTMAX-13\n");
            break;
        default:
            break;
    }
    
    //捕捉信号，安装信号处理函数
    signal(SIGRTMIN+15,handler_sigrtmin15);
    signal(SIGRTMAX-13,handler_sigrtmax13);
    while(1)
        ;
    return 0;
}
//根本就没有SIGRTMAX-15，这书我流汗了
```

​	程序捕捉了两个实时信号(可靠信号)，SIGRTMIN+15和SIGRTMAX-13，并安装了它们的信号处理函数。在main函数内首先调用setjmp设置了返回点，并根据返回值打印出不同的提示信息。信号处理函数内部打印出提示信息以后没有正常返回，而是调用longjmp直接跨函数跳转，返回到setjmp处。

​	执行程序时，在一个终端执行本程序，在另一个终端先使用命令ps -a查看进程的PID，再使用kill命令发送信号，结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/jmp1.png)



![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/jmp2.png)



第一个终端结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/jmp3.png)

​	如果在第二个终端继续使用kill发送信号：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/jmp4.png)

​	从结果看，第一个终端的结果没有任何变化，然而信号又确实发送给目标进程了，这是因为信号处理时会自动阻塞正在被处理的信号，在信号处理函数返回时把进程的信号屏蔽字修复，即解除对当前信号的阻塞。但上面的程序没有让信号处理函数正常返回，而是用longjmp直接跳转，所以进程的信号屏蔽字在第一次收到信号后，就把信号设置为阻塞并且再也没有恢复，因而再也触发不了信号处理函数了，除非手动将进程对信号的屏蔽去除。

##### sigsetjmp/siglongjmp

​	如果既想使用跨函数跳转直接返回，又想避免每次都手动清楚信号屏蔽的麻烦，就要使用这两个函数。

它们的函数原型如下：

```c
#include <setjmp.h>
int sigsetjmp(sigjmp_buf env,int savesigs);
void siglongjmp(sigjmp_buf env,int val);
```

​	这两个函数与setjmp/longjmp的唯一区别是sigsetjmp多了一个参数savesigs，如果savesigs非0，则sigsetjmp在env中保存进程的当前信号屏蔽字，在调用siglongjmp时会从其中恢复保存的信号屏蔽字。

right_return.c

```c
#incldue <stdio.h>
#include <setjmp.h>
#include <signal.h>
#include <unistd.h>

#define ENV_UNSAVE
#define ENV_SAVED

int flag_saveenv = ENV_UNSAVE;
jmp_buf env;//保存待跳转位置的栈信息

//信号SIGRTMIN+15的处理函数
void handler_sigrtmin15(int signo)
{
    if(flag_saveenv == ENV_UNSAVE)
        return;
    printf("recv SIGRTMIN+15\n");
    sleep(10);
    printf("in handler_sigrtmin15, after sleep\n");
    siglongjmp(env,1);//返回到env处，注意第二个参数
}

int main()
{
    //设置返回点
    switch(sigsetjmp(env,1)){//该函数默认为0
        case 0:
            printf("return 0\n");
            flag_saveenv = ENV_SAVED;
            break;
        case 1:
            printf("return from SIGRTMIN+15\n");
            break;
        default:
            printf("return else\n");
            break;
    }
    
    //捕捉信号，安装信号处理函数
    signal(SIGRTMIN+15, handler_sigrtmin15);
    while(1)
        ;
    return 0;
}
```

在一个终端执行

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/sig1.png)

另一终端用kill命令连续发送信号4次：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/sig2.png)

第一个终端结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/sig3.png)

​	本程序的信号处理函数先检查flag_saveenv的值是否为ENV_UNSAVE，如果是，则直接返回，因为此时程序还没来得及保存返回点的栈状态信息。在sigsetjm之后才将flag_saveenv设置为ENV_SAVED。不过不这样处理，那么当信号发生在调用sigsetjmp之前时，信号处理函数将返回到位置地点或程序崩溃。使用setlongjmp从信号程序返回时都应该这样处理。

​	4次信号都被响应了，可见实时信号是可靠的，支持排队的。

​	执行结果只打印出一条"return from SIGRTMIN+15"，整个程序的流程为：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/sig4.png)



## 信号的发送

​	信号的发送主要有函数kill、raise、sigqueue、alarm、setitimer以及abort来完成

##### kill

​	kill函数用来发送信号给指定的进程，在Shell下输入man 2 kill可获得其原型：

```c
#include<sys/types.h>
#include <signal.h>
int kill(pid_t pid,int sig);
```

该函数的行为与第一个参数pid的取值有关，第二个参数sig表示信号编号。

- 如果pid是正数，则发送信号sig给进程号为pid的进程
- 如果pid为0，则发送信号sig给当前进程所属进程组里的所有进程
- 如果pid为-1，则把信号sig广播至系统内除1号进程(init进程)和自身以外的所有进程
- 如果pid是比-1还小的负数，则发送信号sig给属于进程组-pid的所有进程
- 如果参数sig是0，则kill()仍执行正常的错误检查，但不发送信号。可以利用这一点来确定某进程是否有权向另外一个进程发送信号。如果向一个并不存在的进程发送空信号，则kill()返回-1，errno则被设置为ESRCH。

​	函数执行成功返回0，当有错误发生时则返回-1，错误代码存入errno中。

**注：**只有具有root权限的进程才能向其他任一进程发送信号，非root权限的进程只能向属于同一个组或同一个用户的进程发送信号。

mykill.c(不支持-l(显示信号编号))

```c
#include<stdio.h>
#include <signal.h>
#include <stdlib.h>
#include <sys.types.h>

int main(int argc,char **argv)
{
    int i,j;
    int signum = SIGTERM;//默认发送SIGTERM
    pid_t pid;
    
    //检查参数个数
    if(argc != 2&& argc != 4)
    {
        printf("Usage: ./a <-s signum> [PId]\n");
        exit(0)
    }
    
    //从命令行参数解析出信号编号
    for(i=1;i<argc;i++)
    {
        if(!strcmp(argv[i],"-s"))
        {
            signum = atoi(argv[i+1]);
            break;
        }
    }
    
    //解析出进程号
    if(argc == 2)
        pid = atoi(argv[1]);
    else
        for(j=1;i<argc;j++)
            if(j!=1&&j!=i+1)
            {
                pid = atoi(argv[j]);
                break;
            }
    
    if(kill(pid,signum)<0)
    {
        perror("kill");
        exit(1);
    }
    return 0;
}
```

​	程序首先解析出命令行参数，再调用kill函数发送指定的信号给目标进程。结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/kill1.png)



![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/kill2.png)



信号SIGINT的编号在所有Linux系统中都为2.从执行结果可以看出，a成功发出信号并被signal接收到。

##### raise

​	raise函数是ANSI C而非POSIX标准定义的，用来给调用它的进程发送信号，在Shell下输入man raise可获得其函数原型如下：

```c
#include <signal.h>
int raise(int sig);
```

​	参数sig表示要发送的信号编号，成功返回0，失败返回非0值。

##### sigqueue

​	sigqueue函数是一个比较新的发送信号的函数，它支持信号带有参数，从而可以与函数sigaction配合使用。在Shell下输入man sigqueue可获取函数原型：

```c
#include <signal.h>
intt sigqueue(pid_t pid,int sig,const union sigval value);
```

​	sigqueue用来发送信号sig给进程pid。与kill系统调用不同的是，sigqueue在发送信号的同时还支持信号携带参数；另一个不同点是不能给一组进程发送信号。参数value是一个共用体，其定义如下：

```c
union sigval{
    int sival_int;
    void *sigval_ptr;
};
```

​	也就是说，信号携带的参数要么是一个整型值，要么是一个void型指针。当接收进程的信号处理函数是由sigaction函数设置的并且设置了SA_SIGINFO标志(表明使用3参数的sa_sigaction设置信号处理函数)时，接收进程可以从siginfo_t结构的si_value域取得信号发送时携带的数据。

​	函数成功执行时返货0，表明信号被成功发送到目标进程，当有错误发生时则返回-1，错误代码存入errno中。

##### alarm

​	alarm函数可以用来设置定时器，定时器超时将产生SIGALRM信号给调用进程。在Shell下输入man alarm可获取函数原型：

```c
#include <unistd.h>
unsigned int alarm(unsigned int seconds);
```

​	参数seconds表示设定的秒数，经过seconds后，内核将给调用该函数的进程发送SIGALRM信号。如果seconds为0，则不再发送SIGALRM信号。最新一次调用alarm函数将取消之前一次的设定。

**注：**alarm只设定为发送一次信号，如果要多次发送，就要对alarm进行多次调用。

​	如果之前已经调用过alarm，则返回之前设置的定时器**剩余时间**；否则返回0。

模拟网络命令ping功能：

```c
#include <stdio.h>
#include <signal.h>
#include <unistd.h>

void send_ip()
{
    //发送回请求报文，这里只是打消息
    printf("send a icmp echo request packet\n");
}

void recv_ip()
{
    //挂在套接字上等待数据并解析报文，这里是使用死循环
    while(1)
        ;
}

void handler_sigalarm(int signo)
{
    send_ip();
    alarm(2);
}

int main()
{
    //安装信号处理程序
    signal(SIGALRM, handler_sigalarm);
    //触发一个SIGALRM信号给本进程
    raise(SIGALRM);
    recv_ip();
    
    return 0;
}
```

​	本程序利用alarm定时触发SIGALRM信号模拟ping程序的定时发包功能。结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/alarm.png)

##### getitimer/setitimer

​	与alarm函数意义，setitimer函数也是用来设置定时器的，且alarm和setitimer使用同一个定时器，因此会互相影响。setitimer要比alarm具有更多的功能，在Shell下输入man setitimer可获得函数原型：

```c
#include <sys/time.h>
int  getitimer(int which, struct itimerval *value);
int  setitimer(int which, const struct itimerval *value,struct itimerval *ovalue);
```

第一个参数which用来指定使用哪一个定时器，根据参数which可单独设定每个定时器，定时器的种类如下：

- **ITIMER_REAL**：按实际时间计时，计时到达时将给进程发送SIGALRM信号，相当于高精度的alarm函数。
- **ITIMER_VIRTUAL**：仅当进程执行时才进行计时。计时到达时将给进程发送SIGVTALRM信号。
- **ITIMER_PROF**：进程执行的时间以及内核因本进程而消耗的时间都计时。与ITIMER_VIRTUAL搭配使用，通常用来统计进程在用户态与核心态花费的时间总和。计时到达时发送SIGPROF信号。

参数value用来指定定时器的时间，结构struct itimerval的定义如下：

```c
struct itimerval{
	struct timeval it_interval; //next value
    struct timeval it_value;	//current value
};
```

其中结构struct timeval的定义如下：

```c
struct timeval{
    long tv_sec;		//秒数
    long tv_usec;		//微妙
};
```

​	对于函数getitimer，如果存在由which指定的定时器，则将剩余时间保存在it_value中，该定时器的初始值保存在it_interval中；如果不存在指定类型的定时器，则将value置为0返回。执行成功返回0，当有错误发生时则返回-1，错误代码存入errno中。

​	对于函数setitimer，参数ovalue如果不是空指针，则将在其中保存上次设定的定时器的值。定时器从value递减为0时，产生一个信号，并将it_value的值设为it_interval，然后重新开始计时，如此周而复始。仅当it_value的值为0或者计时到达而it_interval的值为0时，停止计时。执行成功返回0，当有错误发生时则返回-1，错误代码存入errno中。

代码示例：

```c
#include <stdio.h>
#incldue <signal.h>
#include <sys/time.h>
#include <unistd.h>

//信号处理程序
void handler_sigtime(int signo)
{
    switch(signo){
        case SIGALRM:
            printf("recv SIGALRM\n");
            break;
        case SIGPROF:
            printf("recv SIGPROF\n");
            break;
        default:
            break;
    }
}

int main()
{
    struct itimerval value;
    
    //安装信号处理函数
    signal(SIGALRM, handler_sigtime);
    signal(SIGPROF, handler_sigtime);
    
    //初始化value结构
    value.it_value.tv_sec = 1;//第一次1秒触发信号
    value.it_value.tv_usec = 0;
    value.it_interval.tv_sec = 5; //第二次开始每5秒触发信号
    value.it_interval.tv_usec = 0;
    
    //设置定时器
    setitimer(ITIMER_REAL, &value, NULL);
    setitimer(ITIMER_PROF, &value, NULL);
    
    while(1)
        ;
    
    return 0;
}
```

​	程序设置了两个定时器**ITIMER_REAL**和**ITIMER_PROF**。系统时间经过1秒后，将触发一个SIGALRM信号，以后每5秒触发一个SIGALRM信号。按照程序执行时消耗的时间以及内核因本程序消耗的时间来计时，第一次经过1秒后将触发一个SIGALRM信号，以后每5秒触发一个SIGPROF信号。

执行结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/timer.png)

​	开始SIGALRM先于SIGPROF出现，理论上SIGALRM的次数应该要多余SIGPROF的次数，因为整个系统并不是只允许这一个进程。(可能因为进程太少或者电脑太好没出现这个结果)。

##### abort

abort函数用来向进程发送SIGABRT信号，在Shell下输入man abort可获取原型：

```c
#include <stdlib.h>
void abort(void)l
```

​	如果进程设置信号处理函数以捕捉SIGABRT信号，且信号处理函数不返回(如使用longjmp)，则abort()不能终止进程。abort()终止进程时，所有打开的流(如I/O流、文件流)均会被刷新和关闭。如果进程设置了SIGABRT被阻塞或忽略，abort()将覆盖这种设置。

​	abort函数没有返回值

## 信号的屏蔽

### sigset_t(信号集)

信号的种类中列出了64个信号，超过了一个整型数能代表的位数(一个整型变量通常为32位)，因此不能用整型数中的以为代表一种信号。POSIX标准定义了数据类型sigset_t来表示信号集，并且定义了一系列函数来操作信号集。在Shell下输入man sigsetops可查看它们的原型如下：

#### sigxxxset

```c
#include <signal.h>
int sigemptyset(sigset_t *set);
int sigfillset(sigset_t *set);
int sigaddset(sigset_t *set, int signum);
int sigdelset(sigset_t *set,int signum);
int sigismember(const sigset_t *set,int signum);
```

这些函数的具体含义为：

- 函数sigemptyset用来初始化一个信号集，使其不包括任何信号
- 函数sigfillset用来初始化一个信号集，使其包括所有信号
- 函数sigaddset用来向set指定的信号集中添加由signum指定的信号
- 函数sigdelset用来从set指定的信号集中删除由signum指定的信号
- 函数sigismember用来测试信号signum是否包括在set指定的信号集中

​	函数sigemptyset、sigfillse、sigaddset以及sigdelset在执行成功时返回0，失败返回-1.函数sigismember返回1表示测试的信号在信号集中，返回0表示测试的信号不在信号集中，出错返回-1。

**注：**所有应用程序在使用信号集前，要对该信号集调用一次sigemptyset或sigfillset以初始化信号集。这是因为C语言编译器将不赋初值的外部和静态度量都初始化为0。

### 信号屏蔽

信号屏蔽又称为信号阻塞，在Shell下输入man sigprocmask可获取信号阻塞的一系列函数的说明：

```c
#include <signal.h>
int sigprocmask(int how,const sigset_t *set,sigset_t * oldset);
int sigpending(sigset_t *set);
int sigsuspend(const sigset_t *mask);
```

##### sigprocmask

​	每个进程都有一个信号屏蔽码，它规定了当前阻塞而不能传递给该进程的信号集。调用函数sigprocmask可以检测或更改进程的信号屏蔽码。如果参数oldset是非空指针，则该进程之前的信号屏蔽码通过oldset返回；如果参数set是非空指针，则该函数将根据参数how来修改信号当前屏蔽码，how的取值如下：

- **SIG_BLOCK**：将进程新的信号屏蔽码设置为当前信号屏蔽码和set指向信号集的并集。
- **SIG_UNBLOCK**：将进程新的信号屏蔽码设置为当前信号屏蔽码中，删除set所指向信号集，即set包含了希望解除阻塞的信号。**即使对当前信号屏蔽码中不存的信号使用SIG_UNBLOCK也是合法操作**。
- **SIG_SETMASK**：将进程新的信号屏蔽码设置为set指向的值。

函数执行成功返回0，当有错发生时返回-1，错误代码存入errno中。

##### sigpending

​	函数sigpending用来获取调用线程因阻塞而不能递送和当前未决的信号集。该信号集通过参数set返回。

​	函数执行成功返回0，有错误发生则返回-1，错误代码存入errno。

sig_mask：

```c
#include <stdio.h>
#include <unistd.h>
#include <signal.h>

//自定义错误处理函数
void my_err(const char * err_string,int line)
{
    fprintf(stderr,"line:%d ",line);
    perror(err_string);
    exit(1);
}

//SIGINT的处理函数
void handler_sigint(int signo)
{
    printf("recv SIGINT\n");
}

int main()
{
    sigset_t newmask,oldmask,pendmask;	//定义信号集
    
    //安装信号处理函数
    if(signal(SIGINT, hander_sigint) == SIG_ERR)
        my_err("signal",__LINE__);
    //睡眠10秒
    sleep(10);
    
    //初始化信号集newmask并将SIGINT添加进去
    sigemptyset(&newmask);
    sigaddset(&newmask,SIGINT);
    
    //屏蔽信号SIGINT
    if(sigprocmask(SIG_BLOCK,&newmask,&oldmask) <0 )
        my_err("sigprocmask",__LINE__);
    else
        printf("SIGINT blocked\n");
    
    sleep(10);
    
    //获取未决信号队列
    if(sigpending(&pendmask) < 0)
        my_err("sigpending",__LINE__);
    
    //检查未决信号队列里面是否有SIGINT
    switch(sigismember(&pendmask,SIGINT)){
        case 0:
            printf("SIGINT is not in pending queue\n");
            break;
        case 1:
            printf("SIGINT is in pending queue\n");
            break;
        case -1:
            my_err("sigismember",__LINE__);
            break;
        default:
            break;
    }
    //解除对SIGINT的屏蔽
    if(sigprocmask(SIG_SETMASK,&oldmask,NULL) < 0 )
        my_errr("sigprocmask",__LINE__);
    else
        printf("SIGINT unblocked\n");
    while(1)
        ;
    return 0;
}
```

​	程序首先安装SIGINT的信号处理函数，然后睡眠10秒(此时按下<Ctrl+c>组合键)。之后将SIGINT阻塞，然后再睡眠10秒(此时可以多次按下Ctrl+c键)，并检测SIGINT是否在未决信号队列中，最后解除对SIGINT信号的阻塞。

结果如下

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/sigmask.png)

​	从结果可以看出，第二个"recv SIGINT"先于"SIGINT unblocked"打印。这是因为第二次调用sigprocmask解除对SIGINT信号的阻塞以后，进程未决信号队列非空，**首先执行信号处理函数**，然后才执行后面的"printf("SIGINT unblocked\n");"。

​	程序中设置SIGINT为阻塞时，先保存了进程的信号屏蔽字在oldmask中，以方便后面解除对SIGINT的阻塞。在解除SIGINT的阻塞时，重新设置进程的信号屏蔽字(SIG_SETMASK)为oldmask。或者也可以使用SIG_UNBLOCK使信号不被阻塞，但这样可能会产生一个问题，当一个大的程序中其他地方可能也阻塞了此信号时，使用SIG_UNBLOCK就会把其他地方的设置修改掉，因此，**建议使用SIG_SETMASK恢复进程的信号屏蔽字而不是使用SIG_UNBLOCK解除特定信号的阻塞**。

​	程序的结果也再次证明了**不可靠信号不支持排队**，有可能丢失信号。因为第二次按下Ctrl+c多次，在解除对SIGINT信号的阻塞后，只打印了一个"recv SIGINT"。

##### sigsuspend

​	函数sigsuspend将进程的信号屏蔽码设置为mask，然后与pause函数一样等待信号的发生并执行完信号处理函数。信号处理函数执行完后再把进程的信号屏蔽码设置为原来的屏蔽字，然后sigsuspend函数才返回。sigsuspend函数保证改变进程的屏蔽码和将进程挂起等待信号是原子操作。

​	sigsuspend函数总是返回-1，并将errno置为EINTR。

sig_suspend.c：

```c
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>

//自定义的错误处理函数
void my_err(const char * err_string, int line)
{
    fprintf(stderr,"line:%d ",line);
    perror(err_string);
    exit(1);
}

//SIGINT的处理函数
void handler_sigint(int signo)
{
    printf("recv SIGINT\n");
}

int main()
{
    sigset_t newmask,oldmask,zeromask;//定义信号集

    //安装信号处理函数
    if(signal(SIGINT, handler_sigint) == SIG_ERR)
        my_err("signal",__LINE__);
    
    sigemptyset(&newmask);
    sigemptyset(&zeromask);
    sigaddset(&newmask, SIGINT);

    //屏蔽信号SIGINT
    if(sigprocmask(SIG_BLOCK, &newmaks, &oldmask) < 0)
        my_err("sigprocmask",__LINE__);
    else
        printf("SIGINT blocked\n");

    //临界区

    //使用sigsuspend取消所有信号得到屏蔽并等待信号的触发
    if(sigsuspend(&zeromask) != -1)
        my_err("sigsuspend",__LINE__);
    else
        printf("recv a signo,return from a sigsuspend\n");

    /*如果使用sigprocmask加上pause可能会出现错误
    if(sigprocmask(SIG_SETMASK,&oldmask,NULL) < 0)
        my_err("sigprocmask",__LINE__);

    pause();
    */
    //将信号屏蔽字恢复
    if(sigprocmask(SIG_SETMASK,&oldmask,NULL) < 0 )
        my_err("sigprocmask",__LINE__);

    while(1)
        ;
    return 0;
}
```

​	程序使用sigprocmask()屏蔽掉信号SIGINT，然后使用sigsuspend()取消对所有信号的屏蔽，并挂起等待信号的触发

执行结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/sus2.png)

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/sus1.png)

​	由于所有实时信号的默认相应都是终止进程，因此程序在收到信号SIGRTMIN之后，解除sigsuspend()的挂起状态，并将信号屏蔽字修改为第一次调用sigprocmask()前的状态。

​	如果为了达到相同的效果，不适用sigsuspend()而是使用注释掉的部分代码，虽然表面上看没有问题，且执行的时候也不会发现问题，但潜在的bug将一直存在。如果信号发生在sigprocmask()之后pause()之前，则这个信号就会丢失掉了，且如果信号只发生一次，程序将永远挂起在pause()上。

# 进程间通信

## 概述

​	进程的地址空间是各自独立的，因此进程之间交互数据必须采用专门的通信机制。特别是在大型的应用系统中，往往需要多个进程互相协作共同完成一个任务，这就需要使用进程间通信(Internet Process Connection，IPC）编程技术。

​	Linux下进程间通信的方法基本是从UNIX平台继承来的。Linux操作系统不但继承了system V IPC通信机制，还继承了基于套接字(socket)的进程间通信机制。前者是贝尔实验室队UNIX早期的进程间通信手段的改进和扩充，**其通信的进程局限于单台计算机内**；后者则突破了这一局限，通信的进程可以运行在不同主机上，也就是进行网络通信。

​	进程间通信的主要手段：

- **管道(pipe)**：管道是一种**半双工**的通信方式，数据只能单方向流动，而且**只能在具有亲缘关系的进程间**使用。进程的亲缘关系通常是指父子进程关系
- **有名管道(named pipe)**：有名管道也是半双工的通信方式，但它允许无亲缘关系进程间的通信
- **信号量(semophore)**：信号量是一个计数器，可以用来控制多个进程对共享资源的访问。它**常作为一种锁机制**，防止某进程正在访问共享资源时，其他进程也访问该资源。因此主**要作为进程间以及同一进程内不同线程之间的同步手段**。
- **消息队列(message queue)**：消息队列是由消息的链表，存放在内核中并由消息队列标识符标识。信息队列克服了信息传递信息量少、管道只能承载无格式字节流以及缓冲区大小受限等缺点。
- **信号(signal)**：信号是一种比较复杂的通信方式，用于通知接收进程某个事件已经发生。
- **共享内存(shared memory)**：共享内存就是映射一段能被其他进程所访问的内存，这段共享内存由一个进程创建但是多个进程都可以访问。共享内存是最快的IPC方式，它是针对其他进程间通信方式运行效率低而专门设计的。它往往与其他通信机制，如信号量，配合使用，来实现进程间的同步和通信。
- **套接字(socket)**：套接字也是一种进程间通信机制，与其他通信机制不同的是，它可以用于不同机器间的进程通信。

## 管道

### 概念

​	管道是一种两个进程间进行单向通信的机制。因为管道传递数据的单向性，管道又称为半双工管道。管道的这一特点决定了其使用恶的局限性：

- 数据只能由一个进程流向另一个进程(其中一个写管道，另外一个读管道)；如果要进行全双工通信，需要建立两个管道。
- 管道只能用于父子进程或者兄弟进程间的通信，也就是说管道只能用于具有亲缘关系的进程间的通信，无亲缘关系的进程不能使用管道

​	除了以上局限性，管道还有其他的不足，如 **管道没有名字**，**管道的缓冲区大小是受限制的**，管道所传送的是无格式的字节流。这就要求管道的输入方和输出方事先约定好数据的格式。

​	使用管道进行通信时，两端的进程向管道读写数据是通过创建管道时，系统设置的文件描述符进行的。因此对管道两端的进程来说，管道就是一个特殊的文件，这个文件只存在于内存中。在创建管道时，系统为管道分配一个界面作为数据缓冲区，进行管道通信的两个进程通过读写这个缓冲区进行通信。

​	通过管道通信的两个通信，一个进程向管道写数据，另一个进程从管道的另一端读数据。**写入的数据每次都添加在管道缓冲区的末尾，读数据的时候都是从缓冲区的头部读出数据**。

### 创建与读写

#### 管道的创建

##### pipe

​	Linux下创建管道可以通过函数pipe来完成。该函数如果调用成功返回0，并且数据中将包含两个新的文件描述符；如果又错误发生，返回-1。该函数原型如下：

```c
#include <unistd.h>
int pipe(int fd[2]);
```

​	管道两端可分别用描述符fd[0]以及fd[1]来描述。管道两端的任务是固定的，一段只能用于读，由描述符fd[0]表示，称其为管道读端；另一端只能用于写，由描述符fd[1]来表示，称其为管道写端。如果试图从管道写端读数据，或者向管道读端写数据都将导致出错。

​	管道是一种文件，因此对文件操作的I/O函数都可以用于管道，如read(),write等。

**注：**管道一旦创建成功，就可以作为一般的文件来使用，对一般文件进行操作的I/O函数也适用于管道。

​	管道的一般用法是，进程在使用fork函数创建子进程前先创建一个管道，该管道用于在父子进程间通信，然后创建子进程，之后父进程关闭管道的读端，子进程关闭管道的写段。父进程负责向管道写数据而子进程负责读数据。当然父进程也可以关闭管道的写段而子进程关闭管道的读端。这样管道就可以用于父子进程间的通信，可以用于兄弟进程间的通信。

#### 从管道中读数据

​	如果某进程要读取管道中的数据，那么该进程应当关闭fd1，同时向管道写数据的进程应当关闭fd0。因为管道只能用于具有亲缘关系的进程间的通信，在各进程进行通信时，它们共享文件描述符。在使用前，应及时地关闭不需要的管道的另一端，以避免意外错误的发生。

​	进程在管道地读端读数据时，如果管道的写段不存在，则进程认为已经读到了数据的末尾，该函数返回读出的字节数为0；管道的写端如果存在，且请求读取的字节数大于PIPE_BUF，则返回管道中现有的所有数据；如果请求的字节数不大于PIPE_BUF，则返回管道中现有的所有数据(此时，管道中数据量小于请求的数据量)，或者返回请求的字节数(此时，管道中的数据量大于请求的数据量)。

**注：**PIPE_BUF在include/linux/limits.h中定义，不同的内核版本可能会有所不同

####  向管道中写数据

​	如果某进程希望向管道中写入数据，那么该进程应该关闭fd0文件描述符，同时管道另一端的进程关闭fd1。向管道中写入数据时，Linux不保证写入的原子性(原子性是指操作在任何时候不能被任何原因打断，操作要么不做要么就一定完成)。管道缓冲区一有空闲区域，写进程就会试图向管道写入数据。如果读进程不读走管道缓冲区中的数据，那么写操作将一直被阻塞等待。

​	在写管道时，如果要求写的字节数小于等于PIPE_BUF，则多个进程对同一管道的写操作不会交错进行。但是，如果有多个进程同时写一个管道，而且某些进程要求写的字节数超过PIPE_BUF所能容纳时，则多个写操作的数据可能会交错。

**注：**只有在管道的读端存在时，向管道中写入数据才有意义。否则，向管道中写入的数据将收到内核传来的SIGPIPE信号。应用程序可以处理也可以忽略该信号，如果忽略该信号或捕捉该信号并从其处理程序返回，则write出错，错误码为EPIPE。

管道的创建、读写

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <unistd.h>

//读管道
void read_from_pipe(int fd)
{
    char message[100];
    read (fs,message,100);
    printf("read from pipe:%s",message);
}
//写管道
void write_to_pipe(int fd)
{
    char *message = "Hello,pipe!\n";
    write(fd,message,strlen(message)+1);
}

int main(void)
{
    int fd[2];
    pid_t pid;
    int stat_val;
    
    if(pipe(fd))
    {
        printf("create pipe failed!\n");
        exit(1);
    }
    
    pid = fork();
    //fork返回两个值，0作为子进程的返回值，子进程pid作为父进程的返回值
    switch(pid)
    {
        case -1:
            printf("fork error!\n");
            exit(1);
        case 0:
            //子进程关闭fd1
            close(fd[1]);
            write_to_pipe(fd[0]);
            exit(0);
        default:
            //父进程关闭
            close(fd[0]);
            write_to_pipe(fd[1]);
            wait(&stat_val);
            exit(0);
    }
    return 0;
}
```

​	父进程对管道中写数据，子进程从管道中读取数据。可以看到，对管道的读写和对一般文件的读写没有什么区别

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/spipe.png)

**注：**必须在系统调用fork()之前调用pipe()，否则子进程将不会继承管道的文件描述符。

​	管道是半双工的(一端只写不能读，另一端只读不能写)，但是可以通过创建两个管道来实现一个全双工(两端都可以读和写)通信。

dual_pipe

```c
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
#include<string.h>


//子进程读写管道的函数
void child_rw_pipe(int readfd,int writefd)
{
    char *message1 = "from child process!\n";
    write(writefd, message1,strlen(message1)+1);
    
    char message2[100];
    read(readfd,message2,100);
    printf("child process read from pipe:%s",message2);
}

//父进程读写管道的函数
void parent_rw_pipe(int readfd,int writefd)
{
    char *message1 = "from parent process!\n";
    write(writefd,message1,strlen(message1)+1);
    char message2[200];
    read(readfd,message2,100);
    printf("parent process read from pipe:%s",message2);
}

int main(void)
{
    int pipe1[2],pipe2[2];
    pid_t pid;
    int stat_val;
    
    printf("realize full-duplex communication:\n\n");
    if(pipe(pipe1))
    {
        printf("pipe1 failed\n");
        exit(1);
    }
    if(pipe(pipe2))
    {
        printf("pipe2 failed\n");
        exit(1);
    }
    
    pid = fork();
    switch(pid)
    {
        case -1:
            printf("fork error!\n");
            exit(1);
        case 0:
            //子进程关闭pipe1的读端，关闭pipe2的锁端
            close(pipe1[1]);
            close(pipe2[0]);
            child_rw_pipe(pipe1[0],pipe2[1]);
            exit(0);
        default:
            //父进程关闭pipe1的写段，关闭pipe2的读端
            close(pipe1[0]);
            close(pipe2[1]);
            parent_rw_pipe(pipe2[0],pipe1[1]);
            wait(&stat_val);
            exit(0);
    }
}
//又没包含string.h，童哥我流汗了
```

该程序为父子进程之间互相发送信息。程序的运行结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/dpipe.png)

##### dup()和dup2()

​	前面的代码实现均基于pipe()在fork()前调用，子进程可以直接继承父进程的文件描述符。但是如果子进程调用exec函数执行另一个应用程序时，就不能再共享了。这种情况下可以将子进程中的文件描述符重定向到标准输入，当新执行的程序从标准输入获取数据时实际上是从父进程中获取输入数据。dup和dup2函数提供了复制文件描述符的功能。两个函数的声明均在头文件unistd.h中：

```c
#include <unistd.h>
int dup(int oldfd);
int dup2(int oldfd,int newfd);
```

​	dup和dup2函数调用成功均返回一个oldfd文件描述符的副本，失败则返回-1。不同的是，由dup函数返回的文件描述符是当前可用文件描述符中的最小数值，而dup2函数则可以利用参数newfd指定欲返回的文件描述符。如果参数newfd指定的文件描述符已经打开，系统先将其关闭，然后将oldfd指定的文件描述符赋值到该参数。如果newfd等于oldfd，则dup2返回newfd，而不关闭它。

dup和dup2使用的对比

```c
//dup
pid =fork();
if(pid == 0)
{
	//关闭子进程的标准输出
	close(1);
	//赋值管道输入端到标准输出
	dup(fd[1]);
	execve("exam",argv,environ);
}

//dup2
pid = fork();
if(pid == 0)
{
	dup2(1,fd[1]);
	execve("exam",argv,environ);
}
```

​	可见dup2系统调用将close操作和文件描述符拷贝操作集成在同一个函数里，而且它保证操作具有原子性。

### 管道的应用案例

​	管道的一种常用用法是：在父进程创建子进程后向子进程传递参数。例如，一个应用软件有一个主进程和很多个不同的子进程。主进程创建子进程后，在子进程调用exec函数执行一个新程序前，通过管道给即将执行的程序传递命令行参数，子进程根据传来的参数进行初始化或其他操作。

monitor.c：

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>

int main(int arg,char **argv,char **environ)
{
    int fd[2];
    pid_t pid;
    int stat_val;
    
    if(arg < 2)
    {
        printf("wrong parameters\n");
        exit(0);
    }
    if(pipe(fd))
    {
        perror("pipe failed");
        exit(1);
    }
    
    pid = fork();
    switch(pid)
    {
        case -1:
            perror("fork failed!\n");
            exit(1);
        case 0:
            close(0);
            dup(fd[0]);
            execve("ex",(void *)argv,environ);
            exit(0);
        default:
            close(fd[0]);
            //将命令行第一个参数写进管道
            write(fd[1],argv[1],strlen(argv[1]));
            break;
    }
    wait(&stat_val);
    exit(0);
}
```

ex.c

```c
#include<stdio.h>
#include<unistd.h>
#include<stdlib.h>

int main(int arg,char **argv)
{
    int n;
    char buf[1024];
    while(1)
    {
     //   if((n = read(stdin,buf,1024)) > 0 )
        if((n = read(STDIN_FILENO,buf,1024)) > 0 )//STDIN_FILENO为0所对应的宏定义，stdin是FILE*类型
        {
            buf[n]='\0';
            printf("ex receivce: %s\n",buf);
            
            if(!strcmp(buf,"exit"))
                exit(0);
            if(!strcmp(buf,"getpid"))
            {
                printf("My pid: %d\n",getpid());
                sleep(3);
                exit(0);
            }
        }
    }
}
```

​	主进程向管道中写入一个命令行参数，子进程从标准输入里面读出该参数，进行相应的操作。

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/forkpipe.png)

​	可见，被监控子进程接受监控主进程的命令，执行不同的操作。在实际项目中，监控进程启动加载多个具有不同功能的子进程，并通过管道的形式向子进程传递参数，这是很常见的。

## 有名管道(FIFO)

### 概念

​	管道的一个不足之处是没有名字，因此，只能用于具有亲缘关系耳朵进程间通信，在有名管道(named pipe或FIFO)提出后，该限制得到了客服。FIFO不同于管道之处在于它提供一个路径名与之关联，以FIFO的文件形式存储于文件系统中。**有名管道是一个设备文件**，因此，即使进程与创建FIFO的进程不存在亲缘关系，只要可以访问该路径，就能够通过FIFO相互通信。值得注意的是，FIFO(First In First Out)总是安装先进先出的原则工作，第一个被写入的数据将首先从管道中读出。

### 创建和读写

​	Linux下有两种方式创建有名管道。一是在Shell下交互地建立一个有名管道，二是在程序中使用系统函数建立有名管道。Shell方式下可使用mknod或mkfifo命令，例：

```bash
mknod namedpipe
```

##### mknod/mkfifo

​	创建有名管道的系统函数有两个：mknod和mkfifo。两个函数均定义在头文件 sys/stat.h，函数原型如下：

```c
#include <sys/stat.h>
#include <sys/types,h>
int mknod(const char * path,mode_t mod,dev_t dev);
int mkfifo(const char * path,mode_t mode);
```

​	函数mknod参数中path为创建的有名管道的全路径名；mode为创建的有名管道的模式，指明其存取权限；dev为设备值，该值取决于文件创建的种类，它只在创建设备文件时才会用到。这两个函数调用成功都返回0，失败都返回-1.

mknod实例：

```c
umask(0);
if(mknod("/tmp/fifo",S_IFIFO | 0666,0) == -1)
{
    perror("mknod error!");
    exit(1);
}
```

函数mkfifo前两个参数的含义和mknod相同。

mkfifo实例：

```c
umask(0);
if(mkfifo("/tmp/fifo",S__IFIFO | 0666) == -1)
{
    perror("mkfifo error!");
    exit(1);
}
```

​	"S_IFIFO|0666"指明创建一个有名管道且存取权限为0666，即创建者、与创建者同组的用户、其他用户对该有名管道的访问权限都是可读可写。

​	有名管道使用方法的管道基本相同。只是使用有名管道时，必须先调用open()将其打开。因为有名管道是一个存在于硬盘上的文件，而管道是存在于内存中的特殊文件。

​	需要注意的是，调用open()打开有名管道的进程可能会被阻塞。但如果同时用读写方式(O_RDWR)打开，则一定不会导致阻塞；如果以只读方式(O_RDONLY)打开，则调用open()函数的进程将会被阻塞知道有写方式打开管道；同样以写方式(O_WRONLY)打开也会阻塞直到有读方式打开管道。

无亲缘关系的进程间通信：

procread.c

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/types.h>

#define FIFO_NAME    "myfifo"
#define BUF_SIZE		1024

int main(void)
{
    int fd;
    char buf[BUF_SIZE];
    
    umask(0);
    fd = open(FIFO_NAME, O_RDONLY);
    read(fd,buf,BUF_SIZE);
    printf("Read content: %s\n",buf);
    close(fd);
    exit(0);
}
```

procwrite.c

```c
#include <stdio.h>
#include <stdlib.h>
#include <string,h>
#incldue <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>

#define FIFO_NAME  "myfifo"
#define  BUF_SIZE	1024

int main(void)
{
    int fd;
    char buf[BUF_SIZE] = "Hello procwrite, i come from process named proceread";
    
    umask(0);
    
    if(mkfifo(FIFO_NAME, S_IFIFO|0666) == -1)
    {
        perror("mkfifo error!");
        exit(1);
    }
    
    if((fd = open(FIFO_NAME, O_WRONLY)) == -1)//以写方式打开FIFO
    {
        perror("fopen error!");
        exit(1);
    }
    write(fd,buf,strlen(buf)+1);//向FIFO写数据
    close(fd);
    exit(0);
}
```

​	编译后首先运行procwrite(运行后处于阻塞状态)，打开另一个终端运行程序procread。

结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/fifo1.png)

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/fifo2.png)

### 有名管道的应用实例

​	通过创建两个管道可以实现进程间的全双工通信，同样也可以通过创建两个FIFO来实现不同进程间的全双工通信。

一个程序中两个进程间的聊天程序(一个为server端，另一个为client端)

```c
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
#include <stdlib.h>

#define  FIFO_READ		"readfifo"
#define  FIFO_WRITE   "writefifo"
#define  BUF_SIZE 1024

int main(void)
{
   int wfd,rfd;
   char buf[BUF_SIZE];
   int len;

   umask(0);
   if(mkfifo(FIFO_WRITE,S_IFIFO|0666))
   {
      printf("Cant't create FIFO %s because %s",FIFO_WRITE,strerror(errno));
      exit(1);
   }
   umask(0);
   wfd=open(FIFO_WRITE,O_WRONLY);
   if(wfd == -1)
   {
      printf("oepn FIFO %s error :%s",FIFO_WRITE,strerror(errno));
      exit(1);
   }
   while((rfd = open(FIFO_READ,O_RDONLY)) == -1)
   {
      sleep(1);
   }
   while(1)
   {
      printf("Server: ");
      fgets(buf,BUF_SIZE,stdin);
      buf[strlen(buf)-1] = '\0';
      if(strncmp(buf,"quit",4) == 0)
      {
         close(wfd);
         unlink(FIFO_WRITE);//删除FIFO文件
         close(rfd);
         exit(0);
      }
      write(wfd,buf,strlen(buf));

      len = read(rfd,buf,BUF_SIZE);
      if(len>0)
      {
         buf[len] = '\0';
         printf("Client: %s\n",buf);
      }
   }
}
```

server.c

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <errno.h>
#include <unistd.h>

#define FIFO_READ   "writefifo"
#define FIFO_WRITE	"readfifo"
#define BUF_SIZE  1024
int main(void)
{
    int wfd,rfd;
	char buf[BUF_SIZE];
	int len;

	umask(0);
	if(mkfifo(FIFO_WRITE,S_IFIFO|0666))
	{
		printf("Can't create FIFO %s because %s\n",FIFO_WRITE,strerror(errno));
		exit(1);
	}
	while((rfd = open(FIFO_READ,O_RDONLY)) == -1)
		sleep(1);

	wfd = open(FIFO_WRITE,O_WRONLY);
	if(wfd == -1)
	{
		printf("Fail to open FIFO %s: %s",FIFO_WRITE,strerror(errno));
		exit(-1);
	}

	while(1)
	{
		len = read(rfd,buf,BUF_SIZE);
		if(len >0);
		{
			buf[len] = '\0';
			printf("Server:%s\n",buf);
		}

		printf("Client:");
		fgets(buf,BUF_SIZE,stdin);
		buf[strlen(buf)-1] = '\0';
		if(strncmp(buf,"quit",4) == 0)
		{
			close(wfd);
			unlink(FIFO_WRITE);//删除FIFO文件
			close(rfd);
			exit(0);
		}
		write(wfd,buf,strlen(buf));
	}
	
}
```

​	server和client两个程序，两者的实现基本是一样的，只不过对FIFO文件的读写顺序颠倒了一下，，两个程序只要将定义FIFO文件名的宏的值对换一下，分别在两个终端上运行这两个程序，并在server端和client端输入数据

运行结果：

server端：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/dfifo1.png)

client端：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/dfifo2.png)

从结果看出，通过两个有名管道也可以实现进程间的双向通信。

## 消息队列

### 概念

​	消息队列是一个存放在内核中的消息链表，每个消息队列由消息队列标识符标识。与管道不同的是消息队列存放在内核中，只有在内核重启(即操作系统重启)或者显式地删除一个消息队列时，该消息队列才会被真正删除。

​	消息队列的重要数据结构：

#### 消息缓冲结构

​	向消息队列发送消息时，必须组成合理的数据结构。Linux系统定义了一个模板数据结构msgbuf：

```c
#include <linux/msg.h>
struct msgbuf{
    long mtype;
    char mtext[1];
};
```

​	结构体中的mtype字段代表消息类型。给消息指定类型，可以使得消息在一个队列中重复使用。mtext字段指消息内容

**注：**mtext虽然定义为char类型，并不表示消息只能是一个字符，消息内容可以为任意类型，由用户根据需要定义。

用户定义的一个消息结构：

```c
struct myMsgbuf{
    long mtype;
    struct student stu;
};
```

​	消息队列中的消息的大小是受限制的，由<linux/msg.h>中的宏MSGMAX给出消息的最大长度，在实际应用中要注意这个限制。

##### msqid_ds内核数据结构

​	Linux内核中，每个消息队列都维护一个结构体msiqd_ds，此结构体保存着消息队列当前的状态信息。该结构定义在头文件linux/msg.h中，具体定义如下：

```c
struct mqid_ds{
    struct_ipc_perm		msg_perm;
    struct_msg			*msg_first;
    struct_msg			*msg_last;
    __kernel_t			msg_stime;
    __kernel_t			msg_rtime;
    __kernel_t			msg_ctime;
    unsigned long		msg_lcbytes;
    unsigned long		msg_lqbytes;
    unsigned short		msg_cbytes;
    unsigned short		msg_qnum;
    unsigned short		msg_qbytes;
    __kernel_ipc_pid_t	msg_lspid;
    __kernel_ipc_pid_t	msg_lrpid;
};
```

各字段的含义如下：

- **msg_perm**：是一个ipc_perm(定义在头文件linux/ipc.h)的结构，保存了消息队列的存取权限，以及队列的用户ID、组ID等信息。
- **msg_first**：指向队列中的第一条消息。
- **msg_last**：指向队列中的最后一条消息
- **msg_stime**：向消息队列发送最后一条信息的时间
- **msg_rtime**：从消息队列取最后一条信息的时间
- **msg_ctime**：最后一次编程消息队列的时间
- **msg_cbytes**：消息队列中所有消息占的字节数
- **msg_qunm**：消息队列中消息的数目
- **msg_qbytes**：消息队列的最大字节数
- **msg_lspid**：向消息队列发送最后一条消息的进程ID
- **msg_lrpid**：从消息队列读取最后一条消息的进程ID

##### ipc_perm内核数据结构

​	结构体ipc_perm保存着消息队列的一些重要的信息，比如消息队列关联的键值，消息队列的用户ID、组ID等，它定义在头文件linux/ipc.h中：

```c
struct ipc_perm{
    __kernel_key_t  key;
    __kernel_uid_t  uid;
    __kernel_gid_t  gid;
    __kernel_uid_t  cuid;
    __kernel_gid_t  cgid;
    __kernel_mode_t    mode;
    unsigned_short   seq;
};
```

几个主要字段的含义如下。

- **key**：创建消息队列用到的键值key
- **uid**：消息队列的用户ID
- **gid**：消息队列的组ID
- **cuid**：创建消息队列的进程用户ID
- **cgid**：创建消息队列的进程组ID

### 消息队列的创建和读写

##### ftok--创建消息队列

​	消息队列是随着内核的存在而存在的，每个消息队列在系统范围内对应唯一的键值。要获得一个消息队列的描述符，只需提供该消息队列的键值即可，该键值通常由函数ftok返回。该函数定义在头文件sys/ipc.h中：

```c
#include <sys/types.h>
#include <sys/ipc.h>
key_t  ftok(const char * pathname,int proj_id);
```

​	ftok函数根据pathname和proj_id这两个参数生成惟一的键值。该函数执行成功会返回一个键值，失败返回-1.

获取键值：

```c
#include <stdio.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <stdlib.h>

int main(void)
{
    int i;
    for(i=1;i<=5;i++)
        printf("key[%d] = %ul\n",i,ftok(".",i));
    exit(0);
}
```

运行结果：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/tok.png)

**注：**参数pathname在系统中一定要存在且进程有权访问，参数proj_id的取值范围为1~255.

##### msgget

​	ftok()返回的键值可以提供给函数msgget。msgget()根据这个键值创建一个新的消息队列或者访问一个已存在的消息队列。msgget定义在头文件sys/msg.h中

```c
int msgget(ket_t key,int msgflg);
```

​	msgget的参数key即为ftok函数的返回值。msgflg是一个标志参数。

msgflg的可能取值：

- **IPC_CREATE**：如果内核中不存在键值与key相等的消息队列，则新建一个消息队列；如果存在这样的消息队列，返回该消息队列的描述符
- **IPC_EXCL**：和IPC_CREATE一起使用，如果对应键值的消息队列已经存在，则出错，返回-1.

**注：**IPC_EXCL单独使用没有任何意义

该函数如果调用成功返回一个信息队列的描述符，否则返回-1.

##### msgsnd--写消息队列

​	创建了一个消息队列后，就可以对消息队列进行读写了。函数msgsnd用于向消息队列发送(写)数据。该函数定义在头文件sys/msg.h中

```c
int msgsnd(int msqid,struct msgbuf *msgp,size_t msgsz,int magflg);
```

msgsnd各参数含义：

- **msqid**：函数向msgid表示的消息队列发送一个信息
- **msgp**：msgp指向发送的消息
- **msgsz**：要发送的消息的大小，不包含消息类型占用的4个字节
- **magflg**：标志操作位。可以 设置为0或IPC_NOWAIT。如果magflg为0，则当消息队列已满的时候，msgsnd将会阻塞，直到消息可以写进消息队列；如果magflg为IPC_NOWAIT，当消息队列已满的时候，msgsnd函数将不等待立即返回。

​	msgsnd函数成功返回0，失败返回-1，常见的错误码有：**EAGAIN**，说明消息队列已满；**EIDRM**，说明消息队列已被删除；**EACCESS**，说明无权访问消息队列。

向消息队列发送信息：

```c
#include <stdio.h>
#include <stdlib.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>
#define BUF_SIZE     256
#define PROJ_ID      32
#define PATH_NAME      "."

int main(void)
{
   //用户自定义消息缓冲
   struct mymsgbuf{
      long msgtype;
      char ctrlstring[BUF_SIZE];
   } msgbuffer;

   int qid;//消息队列标识符
   int msglen;
   key_t msgkey;

   //获取键值
   if((msgkey = ftok(PATH_NAME, PROJ_ID))  == -1)
   {
      perror("ftok error!\n");
      exit(1);
   }

   //创建消息队列
   if((qid = msgget(msgkey,IPC_CREAT|0660)) == -1)
   {
      perror("msgget error!\n");
      exit(1);
   }

   //填充消息结构，发送到消息队列
   msgbuffer.msgtype = 3;
   strcpy(msgbuffer.ctrlstring , "Hello,message queue");
   msglen= sizeof(struct mymsgbuf) -4;
   if(msgsnd(qid,&msgbuffer,msglen,0) == -1)
   {
      perror("msgget error!\n");
      exit(1);
   }
   exit(0);
}
```

​	编译并运行后，就向消息队列放入了一条信息，可以通过命令ipcs查看，执行结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/mqueue.png)

​	从输出的结果可以看出，系统内部生成一个消息队列，其中含有一条信息。

##### msgrcv--读消息队列

​	消息队列中放入数据后，其他进程就可以读取其中的信息了。读取消息的系统调用为msgrcv()，该函数定义在头文件sys/msg.h中，其原型如下：

```c
int msgrcv(int msqid,struct msgbuf *msgp,size_t msgsz,long int msgtyp, int msgflg);
```

该函数有5个参数：

- **msqid**：消息队列描述符
- **msgp**：读取的信息存储到msgp指向的消息结构中
- **msgsz**：消息缓冲区的大小
- **msgtyp**：为请求读取的消息类型
- **msgflg**：操作标志位。msgflg可以为IPC_NOWAIT，IPC_EXCEPT，IPC_NOERROR

​	3个常量。这些值的意义分别为：**IPC_NOWAIT**，如果没有满足条件的信息，调用立即返回，此时错误码为ENOMSG； **IPC_EXCEPT**，与msgtyp配合使用，返回队列中第一个类型不为msgtyp的消息；**IPC_NOERROR**，如果队列中满足条件的信息内容大于请求的msgsz字节，则把该消息截断，截断部分将被丢弃。

recmsg.c

```c
#include <stdio.h>
#include <stdlib.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>
#define BUF_SIZE     256
#define PROJ_ID      32
#define PATH_NAME      "."

int main(void)
{
   //用户自定义消息缓冲区
   struct mymsgbuf{
      long msgtype;
      char ctrlstring[BUF_SIZE];
   } msgbuffer;
   int qid; //消息队列标识符
   int msglen;
   key_t msgkey;

   //获取键值
   if((msgkey = ftok(PATH_NAME, PROJ_ID)) == -1)
   {
      perror("ftok error!\n");
      exit(1);
   }

   //获取消息队列标识符
   if((qid = msgget(msgkey,IPC_CREAT|0660)) == -1)
   {
      perror("msgget error!\n");
      exit(1);
   }

   msglen = sizeof(struct mymsgbuf) -4;
   if(msgrcv(qid,&msgbuffer,msglen,3,0) == -1)//读取数据
   {
      perror("msgrcv error!\n");
      exit(1);
   }
   printf("Get message %s\n",msgbuffer.ctrlstring);
   exit(0);
}
```

运行结果：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/rmqueue.png)

### 获取和设置消息队列的属性

##### msgctl--获取msqid_ds

​	消息队列的属性保存在系统维护的数据结构msqid_ds中，用户可以通过函数msgctl获取或设置消息队列的属性。msgctl定义在头文件sys/msg.h中，如下：

```c
int msgctl(int msqid,int cmd,stuct msqid_ds *buf);
```

​	msgctl系统调用对msqid表示的消息队列执行cmd操作，系统定义了3种cmd操作：**IPC_STAT**、**IPC_SET**、**IPC_RMID**，它们的意义如下：

- **IPC_STAT**：该命令用来获取队列对应的msqid_ds数据结构，并将其保存到buf指向的内存空间
- **IPC_SET**：该命令用来设置消息队列的属性，要设置的属性存储在buf中，可设置的属性包括：msg_perm.uid、msg_perm.gid、msg_perm.mode以及msg_qbytes。
- **IPC_RMID**：从内核中删除msqid标识的消息队列。

获取和设置消息队列属性：

```c
#include <stdio.h>
#include <stdlib.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>
#include <unistd.h>
#include <time.h>
#define BUF_SIZE     256
#define PROJ_ID      32
#define PATH_NAME      "."

void getmsgattr(int msgid,struct msqid_ds msg_info);

int main(void)
{
   //用户自定义消息缓冲区
   struct mymsgbuf{
      long msagtype;
      char ctrlstring[BUF_SIZE];
   }  msgbuffer;
   int qid;//消息队列标识符
   int msglen;
   key_t msgkey;
   struct msqid_ds  msg_attr;

   //获取键值
   if((msgkey = ftok(PATH_NAME, PROJ_ID)) == -1)
   {
      perror("ftok error!\n");
      exit(1);
   }

   //获取消息队列标识符
   if((qid = msgget(msgkey, IPC_CREAT|0660)) == -1)
   {
      perror("msgget error!\n");
      exit(1);
   }
   msgctl(qid,IPC_STAT,&msg_attr);//将属性存入msg_attr中
   getmsgattr(qid,msg_attr);//输出消息队列的属性

   //发送一条消息到消息队列
   msgbuffer.msagtype = 2;
   strcpy(msgbuffer.ctrlstring,"Another message");
   msglen= sizeof(struct mymsgbuf) -4;
   if(msgsnd(qid,&msgbuffer,msglen,0) == -1)
   {
      perror("msgget error!\n");
      exit(1);
   }
   getmsgattr(qid,msg_attr);//再次输出消息队列的属性

   //设置消息队列的属性
   msg_attr.msg_perm.uid = 3;
   msg_attr.msg_perm.gid = 2;
   if(msgctl(qid,IPC_SET,&msg_attr) == -1)
   {
      perror("msg set error!\n");
      exit(1);
   }
   getmsgattr(qid,msg_attr);//修改后再观察其属性
   if(msgctl(qid,IPC_RMID,NULL) == -1)
   {
      perror("delete msg error!\n");
      exit(1);
   }
   getmsgattr(qid,msg_attr);//删除后再观察其属性
   exit(0);
}

void getmsgattr(int msgid, struct msqid_ds msg_info)
{
   if(msgctl(msgid,IPC_STAT,&msg_info) == -1)
   {
      perror("msgctl error!\n");
      return ;
   }
   printf("information of message queue%d\n",msgid);
   printf("last msgsnd to msq time is %s\n",ctime(&(msg_info.msg_stime)));
   printf("last msgrcv time from msg is %s\n",ctime(&(msg_info.msg_rtime)));
   printf("last change msq time is %s\n",ctime(&(msg_info.msg_ctime)));
   printf("current number of bytes on queue is %d\n",msg_info.__msg_cbytes);
   printf("number of messages in queue is %d\n",msg_info.msg_qnum);
   printf("max number of bytes on queue is %d\n",msg_info.msg_qbytes);
   printf("pid of last msgsnd is %d\n",msg_info.msg_lspid);
   printf("pid of last msgrcv is %d\n",msg_info.msg_lrpid);

   printf("msg uid is %d\n",msg_info.msg_perm.uid);
   printf("msg gid is %d\n",msg_info.msg_perm.gid);
   printf("information end!\n");
}
//必须包含<time.h>，而且要先用msgctl给msqid_ds结构变量赋值
```

运行结果片段：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/mctl.png)

​	结果片段显示的是对消息队列进行操作前的属性。发送消息后和重新设置后的消息队列属性都会因为操作而改变。可以运行程序观察全部的输出结果，对比操作前后队列属性是如何改变。

### 应用实例

​	聊天程序：

server.c

```c
#include <stdio.h>
#include <stdlib.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>
#include <unistd.h>
#include <time.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>

#define BUF_SIZE     256
#define PROJ_ID      32
#define PATH_NAME      "/tmp"
#define  SERVER_MSG  1
#define  CLIENT_MSG  2

int main(void)
{
   //用户自定义消息缓冲区
   struct mymsgbuf{
      long msgtype;
      char ctrlstring[BUF_SIZE];
   } mymsgbuffer;

   int qid;//消息队列标识符
   int msglen;
   key_t msgkey;

   //获取键值
   if((msgkey = ftok (PATH_NAME,PROJ_ID)) == -1 )
   {
      perror("ftok error!\n");
      exit(1);
   }

   if((qid = msgget (msgkey,IPC_CREAT|0660)) == -1)
   {
      perror("msgget error!\n");
      exit(1);
   }

   while(1)
   {
      printf("server: ");
      fgets(mymsgbuffer.ctrlstring, BUF_SIZE,stdin);
      if(strncmp("exit",mymsgbuffer.ctrlstring,4) == 0)
      {
         msgctl(qid,IPC_RMID,NULL);
         break;
      }
      mymsgbuffer.ctrlstring[strlen(mymsgbuffer.ctrlstring) -1 ] = '\0';
      mymsgbuffer.msgtype = SERVER_MSG;
      if(msgsnd(qid,&mymsgbuffer,strlen(mymsgbuffer.ctrlstring) +1,0) == -1)
      {
         perror("Server msgsnd error!\n");
         exit(1);
      }

      if(msgrcv(qid,&mymsgbuffer,BUF_SIZE,CLIENT_MSG,0) == -1)
      {
         perror("Server msgrcv error!\n");
         exit(1);
      }
      printf("Client: %s\n",mymsgbuffer.ctrlstring);
   }
   exit(0);
}

```

client.c

```c
#include <stdio.h>
#include <stdlib.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>
#include <unistd.h>
#include <time.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>

#define BUF_SIZE     256
#define PROJ_ID      32
#define PATH_NAME      "/tmp"
#define  SERVER_MSG  1
#define  CLIENT_MSG  2

int main(void)
{
   //用户自定义消息缓冲区
   struct mymsgbuf{
      long msgtype;
      char ctrlstring[BUF_SIZE];
   } mymsgbuffer;

   int qid;//消息队列标识符
   int msglen;
   key_t msgkey;

   //获取键值
   if((msgkey = ftok (PATH_NAME,PROJ_ID)) == -1 )
   {
      perror("ftok error!\n");
      exit(1);
   }

   if((qid = msgget (msgkey,IPC_CREAT|0660)) == -1)
   {
      perror("msgget error!\n");
      exit(1);
   }

   while(1)
   {
      if(msgrcv(qid,&mymsgbuffer,BUF_SIZE,SERVER_MSG,0) == -1)
      {
         perror("Server msgrcv error!\n");
         exit(1);
      }
      printf("Server: %s\n",mymsgbuffer.ctrlstring);
	  printf("client: ");
	  fgets(mymsgbuffer.ctrlstring,BUF_SIZE,stdin);
	  if(strncmp("exit",mymsgbuffer.ctrlstring,4) == 0)
	  {
		break;
	  }
	  mymsgbuffer.ctrlstring[strlen(mymsgbuffer.ctrlstring) -1 ] = '\0';
	  mymsgbuffer.msgtype = CLIENT_MSG;
	  if(msgsnd(qid,&mymsgbuffer,strlen(mymsgbuffer.ctrlstring) +1,0) == -1)
	  {
		perror("Client msgsnd error!\n");
		exit(1);
	  }
   }
   exit(0);
}

```

在两个终端分别执行，结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/msgchat1.png)

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/msgchat2.png)

## 信号量

### 概念

​	信号量是一个计数器，常用于处理进程或线程的同步问题，特别是对临界资源访问的同步。临界资源可以简单理解为在某一时刻只能由一个进程或线程进行操作的资源，该资源可以是一段代码、一个变量或某种硬件资源。信号量的值大于或等于0时表示可供并发进程使用的资源实体数；小于0时代表正在等待使用临界资源的进程数。

**注：**信号量和信号没有关系。

​	与信息队列类似，Linux内核也为每个信号集维护了一个semid_ds数据结构实例。该结构定义在头文件linux/sem.h中，其意义如下：

```c
struct semid_ds{
    struct ipc_perm		sem_perm;	//对信号进行操作的许可权
    __kernel_time_t		sem_otime;	//对信号进行操作的最后时间
    __kernel_time_t		em_ctime;	//对信号进行修改的最后时间
    struct sem			*sembase;	//指向第一个信号
    struct sem_queue	sem_pending;//等待处理的挂起操作
    struct sem_queque	**sem_pending_last;//最后一个正在挂起的操作
    struct sem_undo		*undo;		//撤销的请求
    ushort				sem_nsems;	//数组中的信号数
}
```

### 信号量的创建与使用

##### semget--信号集的创建或打开

​	Linux下使用系统函数semget创建或打开信号集。这个函数定义在头文件sys/sem.h中，函数原型如下：

```c
int semget(key_t,int nsems,int semflg);
```

​	该函数执行成功则返回一个信号集的标识符，失败返回-1.函数的第一个参数是由ftok()得到的键值；第二个参数nsems指明要创建的信号集包含的信号个数，**如果只是打开信号集，粑nsems设置为0即可**；第三个参数semflg为操作标志，可以取以下值：

- **IPC_CREATE**：调用semget()时，它会将此值与系统中其他信号集的key进行对比，如果存在相同的key，说明信号集已存在，此时返回该信号集的标识符，否则新建一个信号集并返回其标识符。
- **IPC_EXCL**：该宏须和IPC_CREATE一起使用，否则没有意义。当semflg取IPC_CREATE|IPC_EXCL时，如果发现该信号集已经存在，则返回错误，错误码为EEXIST。

创建信号集并对信号集中所有信号进行初始化的函数：

```c
int createsem(const char *pathname,int proj_id,int members,int init_val）
{
	key_t       msgkey;
    int 		index,sid;
    union semun		semopts;
    
    //获取键值
    if((msgkey = ftok(pathname,proj_id)) == -1)
    {
        perror("ftok error!\n");
        return -1;
    }
    
    if((sid = semget(msgkey,members,IPC_CREATE|0666)) == -1)
    {
        perror("semget call failed.\n");
        return -1;
    }
    
    //初始化操作
    semopts.val = init_val;
    for(index = 0;index<members;index++)
    {
        semctl(sid,index,SETVAL,semopts);
    }
    return (sid);
}
```

##### semop--信号量的操作

​	信号量的值与相应资源的使用情况有关，当它的值大于0时，表示当前可用资源的数量，当它的值小于0时，其绝对值表示等待使用该资源的进程个数。信号量的值仅能由PV操作来改变。在Linux下，PV操作通过调用函数semop实现。该函数定义在sys/sem.h，原型如下：

```c
int semop(int semid,struct sembuf* sops,size_t nsops);
```

​	函数的参数semid为信号集的标识符；参数sops指向进行操作的结构体数组首地址；参数nsops指出将要进行操作的信号的个数。semop函数调用成功返回0，否则返回-1。

​	semop的第二个参数sops指向的结构体数组中，每个sembuf结构体对应一个特定信号的操作。因此对信号进行操作必须熟悉该数据结构，该结构体定义在linux/sem.h。

```c
struct sembuf{
    ushort sem_num;		//信号在信号集中的索引
    short  sem_op;		//操作类型
    short  sem_flg;		//操作标志
};
```

sem_op的取值及意义

|  取值范围  |                           操作意义                           |
| :--------: | :----------------------------------------------------------: |
|  sem_op>0  |          信号加上sem_op的值，表示进程释放控制的资源          |
| sem_op = 0 | 如果没有设置IPC_NOWAIT，则调用进程进入睡眠状态，直到信号值为0；否则进程不会睡眠，直接返回EAGAIN |
|  sem_op<0  | 信号加上sem_op的值。若没有设置IPC_NOWAIT，则调用进程阻塞，知道资源可用；否则进程直接返回EAGAIN |

对一个信号集中的某个信号进行操作的P、V函数

```c
//P操作函数
int sem_p(int semid,int index)
{
    struct sembuf buf = {0,-1,IPC_NOWAIT};
    if(index < 0)
    {
        perror("index of array cannot eqauls a minus value!");
        return -1;
    }
    
    buf.sem_num = index;
    if(semop(semid,&buf,1) == -1)
    {
        perror("a wrong operation to semaphore occurred!");
        return -1;
    }
    return 0;
}

//V操作函数
int sem_v(int semid,int index)
{
    struct sembuf buf = {0,1,IPC_NOWAIT};
    
    if(index < 0)
    {
        perror("index of array cannot equala a minus value!");
        return -1;
    }
    buf.sem_num = index;
    if(semop(semid,&buf,1) == -1)
    {
        perror("a wrong operation to semaphore occurred");
        return -1;
    }
    return 0;
}
```

##### semctl--信号集的控制

​	使用信号量时，往往需要对信号集进行一些进程控制，比如删除信号集、对内核维护的信号集的数据结构semid_ds进行设置、获取信号集中信号值等。通过semctl控制函数可以完成这些操作，该函数定义在sys/sem.h，如下：

```c
int semctl(int semid,int semnum,int cmd,...);
```

​	函数中，参数semid为信号集的标识符；参数semnum标识一个特定的信号；cmd指明控制操作的类型；**最后的"..."说明函数的参数是可选的**，它依赖于第三个参数cmd，它通过共用体变量semum选择要操作的参数。

semun定义在include/sem.h，如下

```c
union semun{
	int				 val;
    struct semid_ds *buf;
    unsigned short *array;
    struct seminfo  *buf;
    void 			*pad;
};
```

各字段含义如下：

- **val**：仅用于SETVAL操作类型，设置某个信号的值等于val。
- **buf**：用于IPC_STAT和IPC_SET操作，存取semid_ds结构。
- **array**：用于SETALL和GETALL操作
- **buf**：为控制IPC_INFO提供的缓存



第三个参数cmd，通过宏来指示操作类型，可取的各个宏的含义如下：

- **IPC_STAT**：通过semun结构体的buf参数返回当前的semid_ds结构体

​		**注：**调用者必须首先分配一个semid_ds结构，并把buf设置为指向这个结构体

- **IPC_SET**：对信号集的属性进行设置
- **IPC_RMID**：把semid指定的信号集从系统中删除
- **GETPID**：返回最后一个执行semop操作的进程ID
- **GETVAL**：返回信号集中semnum指定信号的值
- **GETALL**：返回信号集中所有信号的值
- **GETNCNT**：返回正在等待资源的进程的数量
- **GETZCNT**：返回正在等待完全空闲资源的进程数量
- **SETVAL**：设置信号集中semnum指定的信号的值
- **SETALL**：设置信号集中所有信号的值

获取和设置的单个信号的函数：

```c
int semval_op(int semid,int index,int cmd)
{
    if(index <0)
    {
        printf("index cannot be minus!\n");
        return -1;
    }
    if(cmd == GETVAL||cmd== SETVAL)
    {
        return semctl(semid,index,cmd,0);
    }
    printf("function cannot surport cmd:%d\n",cmd);
    return -1;
}
```

### 应用实例

​	信号量一般用于处理访问临界资源的同步问题。通过一个server.c和client.c来演示信号量如何控制对资源的访问。server创建一个信号集，并对信号量循环-1，相当于分配资源。client执行时检查信号量，如果其值大于0代表有资源可用，继续执行，如果小于等于0代表资源已经分配完毕，进程client退出。

server.c

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>
#include <sys/types.h>
#include <linux/sem.h>

#define MAX_RESOURCE    5

int main(void)
{
   key_t    key;
   int      semid;
   struct sembuf sbuf = {0,-1,IPC_NOWAIT};
   union semun  semopts;

   if((key = ftok(".",'s')) == -1)
   {
      perror("ftok error!\n");
      exit(1);
   }

   if((semid = semget(key,1,IPC_CREAT|0666)) == -1)
   {
      perror("semget error!\n");
      eixt(1);
   }

   semopts.val = MAX_RESOURCE;
   if(semctl(semid,0,SETVAL,semopts) == -1)//将标识符semid指向的信号集中下标为0的信号设为semopts.val
   {
      perror("semctl error!\n");
      exit(1);
   }
   
   while(1)
   {
      if(semop(semid,&sbuf,1) == -1)
      {
         perror("semop error!\n");
         exit(1);
      }
      sleep(3);
   }
   exit(0);
}
```

client.c

```c
#include <stdio.h>
#include <stdlib.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>
#include <unistd.h>
#include <time.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>

int main(void)
{
	key_t  key;
	int   semid,semval;
	union semun semopts;
	if((key = ftok(".","s")) == -1)
	{
		perror("ftok error!\n");
		exit(1);
	}

	if((semid = semget(key,1,IPC_CREAT|0666)) == -1)
	{
		perror("semget error!\n");
		exit(1);
	}

	while(1)
	{
		if((semval = semctl(semid,0,GETVAL,0)) == -1)
		{
			perror("semctl error!\n");
			exit(1);
		}
		if(semval > 0)
		{
			printf("Still %d resources can be used\n",semval);
		}
		else
		{
			printf("No more resources can be used!\n");
			break;
		}
		sleep(3);
	}
	exit(0);
}
```

分别在两个终端上，先运行server.c，再运行client.c，结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/sem.png)

## 共享内存

### 共享内存的数据结构

​	共享内存就是分配一块能被其他进程访问的内存。每个共享内存段在内核中维护着一个内部结构shmid_ds(和消息队列、信号量一样)，该结构定义在头文件linux/shm.n，代码如下：

```c
struct shmid_ds{
    struct  ipc_perm			shm_perm;
    int							shm_segsz;
    __kernel_time_t				shm_atime;
    __kernel_time_t				shm_dtime;
    __kernel_time_t				shm_ctime;
    __kernel_ipc_pid_t			shm_cpid;
    __kernel_ipc_pid_t			shm_lpid;
    ushort						shm_nattch;
    ushort						shm_unused;
    void						*shm_unused2;
    void						*shm_unused3;
};
```

代码中主要字段含义如下：

- **shm_perm**：操作许可，里面包含共享内存的用户ID、组ID等信息。
- **shm_segsz**：共享内存段的大小，以单位为字节
- **shm_atime**：最后一个进程访问共享内存的时间
- **shm_dtime**：最后一个进程离开共享内存的时间
- **shm_ctime**：最后一次修改共享内存的时间
- **shm_cpid**：创建共享内存的进程ID
- **shm_lpid**：最后操作共享内存的进程ID
- **shm_nattch**：当前使用该内存共享段的进程数量

### 共享内存的创建与操作

##### shmget--共享内存区的创建

​	Linux下使用函数shmget来创建一个共享内存区，或者访问一个已存在的内存区。该函数定义在头文件linux/shm.h中，原型如下：

```c
int shmget(key_t key,size_t size,int shmflg);
```

​	参数key是ftok()**得到的键值**；参数size以字节为单位指定内存的大小；shmflg为操作标志位，它的值为一些宏：

- **IPC_CREATE**：调用shmget时，系统将此值与其他所有共享内存区的key进行比较，如果存在相同的key，说明共享内存区已存在，此时返回该共享内存区的标识符，否则新建一个共享内存区并返回其标识符。
- **IPC_EXCL**：该宏必须和**IPC_CREATE**一起使用，否则没有意义。当shmflg取**IPC_CREATE**|**IPC_EXCL**时，表示如果发现信号集已存在，则返回-1，错误码为EEXIST。

**注：**当创建一个新的共享内存区时，size值必须大于0；如果是访问一个已存在的共享内存区，置size为0.

##### shmat--共享内存区的操作

​	在使用共享内存区前，必须通过shmat函数将其附加到进程的地址空间。进程与共享内存就建立了连接。shmat调用成功后就会返回一个指向共享内存区的指针，使用该指针可以访问共享内存区，如果失败返回-1。该函数声明在linux/shm.h中，具体结构代码如下：

```c
void* shmat(int shmid,const void * shmaddr,int shmflg);
```

​	参数shmid为shmget的返回值；参数shmflg为存取权限标志；参数shmaddr为共享内存的附加点。参数shmaddr不同取值情况的含义说明如下：

- 如果为空，则由内核选择一个空闲的内存区；如果非空，返回地址取决于调用者是否给shmflg参数指定了SHM_RND值，如果没有指定，则共享内存区附加到由shmaddr指定的地址；否则附加地址为shmaddr向下舍入一个共享内存低端边界地址后的地址(SHMLBA，一个常址)。
- 通常将参数shmaddr设置为NULL

​	当进程结束使用共享内存区时，要通过函数shmdt断开与共享内存区的连接。该函数声明在sys/shm.h文件中，具体结构代码原型如下：

```c
int shmdt (const void* shmaddr);
```

​	参数shmaddr为shmat函数的返回值。该函数调用成功后，返回0，否则返回-1。进程脱离共享内存区后，数据结构shmid_ds中shm_nattch就会减1。但是共享内存段依然存在，只有shm_nattch为0后，即没有任何进程再使用该共享内存区，共享内存区才在内核中被删除。一般来说，当一个进程终止时，它所附加的共享内存区都会自动脱离。

##### shmctl--共享内存区的控制

​	Linux对共享内存区的控制是通过调用函数shmctl来完成的，该函数定义在头文件sys/shm.h中，原型代码：

```c
int shmctl (int shmid,int cmd,struct shmid_ds *buf);
```

​	函数中：参数shmid为共享内存区的标识符；buf为指向shmid_ds结构体的指针；cmd为操作标志位，支持以下3种操作控制：

- **IPC_RMID**：从系统中删除由shmid标识的共享内存区
- **IPC_SET**：设置共享内存区的shmid_ds结构
- **IPC_STAT**：读取共享内存区的shmid_ds结构，并将其存储到buf指向的地址中

### 应用实例

通过读写者问题(不考虑优先级)来演示共享内存和信号量如何配合使用。读者写者问题要求一个进程读共享内存的时候，其他进程不能写内存；当一个进程写内存的时候，其他进程不能读内存。

程序首先定义了一个包含公用函数的头文件sharemem.h

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <sys/shm.h>

#define SHM_SIZE  1024

union semun
{
   int      val;
   struct semid_ds   *buf;
   unsigned short    *array;
};

//创建信号量函数
int createsem(const char * pathname,int proj_id,int members,int init_val)
{
   key_t       msgkey;
   int         index,sid;
   union semun semopts;

   if((msgkey = ftok(pathname, proj_id)) == -1)
   {
      perror("ftok error!\n");
      return -1;
   }

   if((sid = semget(msgkey,members,IPC_CREAT|0666)) == -1)
   {
      perror("semget call failed\n");
      return -1;
   }

   //初始化操作
   semopts.val = init_val;
   for(index = 0;index < members; index++)
   {
      semctl(sid,index,SETVAL,semopts);
   }
   return(sid);
}

//打开信号量函数
int opensem(const char * pathname,int proj_id)
{
   key_t  msgkey;
   int    sid;

   if((msgkey = ftok(pathname,proj_id)) == -1)
   {
      perror("ftok error!\n");
      return -1;
   }

   if((sid = semget(msgkey,0,IPC_CREAT|0666)) == -1)
   {
      perror("semget call failed\n");
      return -1;
   }
   return(sid);
}

//P操作函数
int sem_p(int semid,int index)
{
   struct sembuf buf= {0,-1,IPC_NOWAIT};
   if(index < 0)
   {
      perror("index of array cannot equals a minus value");
      return -1;
   }

   buf.sem_num=index;
   if(semop(semid,&buf,1) == -1)
   {
      perror("a wrong operation to semaphore occurred");
      return -1;
   }
   return 0;
}

//V操作函数
int sem_v(int semid,int index)
{
   struct sembuf buf = {0,+1,IPC_NOWAIT};
   if(index < 0)
   {
      perror("index of array cannot equals a minus value");
      return -1;
   }

   buf.sem_num = index;
   if(semop(semid,&buf,1) == -1)
   {
      perror("a wrong operation to semaphore occurred");
      return -1;
   }
   return 0;
}

//删除信号集函数
int sem_delete(int semid)
{
   return(semctl(semid,0,IPC_RMID));
}

//等待信号为1
int wait_sem(int semid,int index)
{
   while(semctl(semid,index,GETVAL,0) == 0)
   {
      sleep(1);
   }
   return 1;
}

//创建共享内存函数
int createshm(char *pathname,int proj_id,size_t size)
{
   key_t shmkey;
   int   sid;

   //获取键值
   if((shmkey = ftok(pathname,proj_id)) == -1)
   {
      perror("ftok error!\n");
      return -1;
   }
   if((sid = shmget(shmkey,size,IPC_CREAT|0666)) == -1)
   {
      perror("shmget call failed\n");
      return -1;
   }
   return (sid);
}
```

下面是writer和reader程序，两程序在进入共享内存区之前，首先都检查信号集中信号的值是否为1(相当于是否能进入共享内存区)，如果不为1，调用sleep()进入睡眠状态直到信号的值变为1.进入共享内存区之后，将信号的值减1(相当于加锁)，这样就实现了互斥的访问共享资源。在退出共享内存时，将信号值加1(相当于解锁)。

writer.c

```c
#include "sharemem.h"

int main()
{
   int      semid,shmid;
   char     *shmaddr;
   char     write_str[SHM_SIZE];

   if((shmid = createshm(".",'m',SHM_SIZE)) == -1)
   {
      exit(1);
   }

   if((shmaddr = shmat(shmid,(char *)0,0)) == (char *)-1)
   {
      perror("attach shared memory error!\n");
      exit(1);
   }

   if((semid = createsem(".",'s',1,1)) == -1)
   {
      exit(1);
   }

   while(1)
   {
      wait_sem(semid,0);
      sem_p(semid,0);      //P操作

      printf("writer: ");
      fgets(write_str,1024,stdin);
      int len =strlen(write_str) -1;
      write_str[len] = '\0';
      strcpy(shmaddr,write_str);
      sleep(10);     //使reader处于阻塞状态

      sem_v(semid,0);      //V操作
      sleep(10);           //等待reader进行读操作
   }
}
```

reader.c

```c
#include "sharemem.h"

int main()
{
	int 		semid,shmid;
	char	 	*shmaddr;

	if((shmid = createshm(".",'m',SHM_SIZE)) == -1)
	{
		exit(1);
	}

	if((shmaddr = shmat(shmid,(char *)0,0)) == (char *) -1)
	{
		perror("attach shared memory error!\n");
		exit(1);
	}

	if((semid = opensem(".",'s')) == -1)
	{
		exit(1);
	}

	while (1)
	{
		printf("reader:");
		wait_sem(semid,0);//等待信号值为1
		sem_p(semid,0);//P操作

		printf("%s\n",shmaddr);
		sleep(10);		//使writer处于阻塞状态

		sem_v(semid,0);			//V操作
		sleep(10);			//等待writer进行写操作
	}
	
}
```

​	如果头文件和要编译文件不在同一目录，需要加上在gcc时加上 **-I** 头文件所在目录

同时在两个终端运行writer和reader，在writer段输入字符串，等待一会观察reader端。

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/sharedm.png)

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/sharedm1.png)

​	从结果可以看出，writer和reader进程是同步的，writer写入的信息均被reader完整的读出。

**共享内存没有提供同步的机制，所以往往使用共享内存进行进程间通信时，需要借助信号量来进行进程间的同步工作。**

## 库的创建和使用

### Linux库的概念

​	库是一种软件组件技术，库里面封装了数据和函数，提供给用户程序调用。库的使用可以使程序模块化，提高程序的编译速度，实现代码重用，使程序易于升级。因此，对于软件开发人员，掌握这项技术是很必要的。

​	Windows系统本身提供并使用了大量的库，包括静态链接库(.lib文件)和动态链接库(.dll文件)。类似的，Linux操作系统也是用库。Linux系统中 ，通常把库文件存放在/usr或/lib目录下。Linux库文件名**由后缀lib、库名以及后缀3部分组成，其中动态库以.so作为后缀，，而静态库通常以.a作为后缀**。

​	在程序中使用静态库和动态库时，它们的载入顺序是不一样的。静态库的代码在编译时就拷贝到应用程序中，因此当多个应用程序同时引用一个静态库函数时，内存中将会有调用函数的多个副本。这样的优点是节省编译时间。而动态库是在程序开始运行后调用库函数时才被载入，被调函数在内存中只有一个副本，并且动态库可以在程序运行期间释放动态库所占用的内存，腾出空间供其他程序使用。

#### 静态库的创建和使用

创建静态库的步骤：

1. 在一个头文件中声明静态库所导出的函数
2. 在一个源文件中实现静态库所导出的函数
3. 编译源文件，生成可执行代码
4. 将可执行代码所在的目标文件加入到某个静态库中，并将静态库拷贝到系统默认的存放库文件的目录下

​	示例说明如何创建和使用静态库。mylib.h中存放的是静态库提供给用户使用的函数声明，mylib.c实现了mylib.h中声明的函数。

mylib.h

```c
#ifndef _mylib_h_
#define _mylib_h_
void welcome();
void outstring(const char * str);
#endif

```

mylib.c

```c
#include "mylb.h"
#include <stdio.h>
void welcome()
{
    printf("Welcome to myliblib\n");
}

void outstring(const char * str)
{
    if(str!=NULL)
        printf("%s",str);
}
```

​	编译mylib.c生成目标文件

```bash
gcc mylib.c -c -o mylib.o
```

将目标文件加入到静态库中，静态库为libmylib.a

```bash
ar rcs libmylib.a mylib.o
```

将静态库拷贝到Linux的库目录(/usr/lib)下：

```bash
cp libmylib.a /usr/lib/libmylib.a
```

调用库函数的测试程序test.c

```c
#include "mylib.h"
#include <stdio.h>

int main()
{
    printf("create and use library:\n");
    welcome();
    outstring("It's successful\n");
}
```

编译使用了库函数的程序：

```bash
gcc test.c -o test -lmylib
```

**注：**-lmylb中-l为选项，mylib为库名。mylib是"libmylib.a"的中间部分，Linux下约定所有库都以前缀lib开始，静态库以.a结尾，动态库以.so结尾，在编译程序时，无需带上前缀和后缀

运行结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/mylib.png)

##### ar

​	Linux下，可以使用ar命令来创建和修改静态库。示例命令如下：

```bash
ar rcs libmylib.a file1.p file2.p
```

​	该命令会将目标代码file1.o和file2.o加入到静态库libmylib.a中，加入时如果静态库libmylib.a不存在，则会自动创建一个静态库。rcs是命令ar的选项。以下是ar命令的适用格式：

```bash
ar  [-]{dmpqrtx}		[abcfilNoPsSuvV]	库名		库中的成员文件名
```

​	其中"-"不是必须的，"dmpqrtx"这些字母在每个命令中只能有一个且必须有一个。"abcfilNoPsSuv"中的字母可以在命令中有0到多个

常用选项含义：

- d：从库中删除成员文件
- m：从库中移动成员文件。当库中的若干成员文件有相同的符号时(如相同的函数名)，成员的位置顺序很重要。如果没有指定其他选项，任何指定的成员将被移动到库的最后。也可以使用'a'，'b'或'I'选项移动到指定的位置

- p：在终端上打印库中指定的成员
- q：快速追加。增加新成员文件到库的结尾处而不检查库中是否存在同名的成员文件
- r：在库中加入成员文件，如果要加入的成员文件在库中已存在，则替换之。默认的情况下，新的成员增加在库的结尾处，可以使用其他选项来改变加入的位置
- t：显示库的成员文件清单
- x：从库中提取一个成员文件。不过不指定要提取的成员文件则提取库中所有的文件



- a：在库的一个已经存在的成员后面增加一个新的成员文件
- b：在库的一个已经存在的成员前面增加一个新的文件
- c：创建一个库
- i：在库中的一个已经存在的成员前面增加一个新的成员文件，类似选项b
- l：暂未使用
- s：无论ar命令是否修改了库内容，都强制重新生成库符号表
- u：插入并列出文件中那些比库中同名文件新的文件，该选项必须和r选项一起使用
- v：用来显示操作的附加信息
- V：显示ar的版本信息

#### 动态库的创建和使用

​	在Linux环境下，只要在编译函数库源程序时加上-shared选项即可，这样所生成的可执行程序就为动态链接库。从某种意义上来说，动态链接库也是一种可执行程序。按一般规则，动态链接库以.so后缀。下面命令把myLib.c程序创建成了一个动态库：

```bash
gcc -fPIC -o mylib.o -c mylib.c
gcc -shared -o libttt.so mylib.o
```

也可以直接使用一条命令：

```bash
gcc -fPIC -shared -o libttt.so mylib.c  #实测不可用
```

​	动态链接库创建后就可以使用了。Linux下有两种方式调用动态链接库中的函数，一种是像使用静态库一样，通过gcc命令调用，命令格式如下：

```bash
gcc -o main main.c ./libmylib.so
```

或者：

```bash
cp libttt.so /usr/lib/libttt.so
gcc -o test test.c /usr/lib/libttt.so
```

**注：**引用动态链接库时，必须含有路径，如果只是使用libmylib.so，则必须确保这个库所在目录也含在PATH环境变量中

运行程序，结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/libso.png)

另一种方法是通过调用系统函数来使用动态链接库，相关函数如表：

|                    函数                     |                             说明                             |
| :-----------------------------------------: | :----------------------------------------------------------: |
| void* dlopen(const char* filename,int flag) |         用于打开指定名字的动态链接库，并返回一个句柄         |
|   void* dlsym(void* handle,char* symbol)    |   根据动态链接库的句柄与函数名，返回函数名对应的函数的地址   |
|         int dlclose (void *handle)          |       关闭动态链接库，handle是调用dlopen函数返回的句柄       |
|          const char* dlerror(void)          | 动态链接库中的函数执行失败时，dlerror返回出错信息；若执行成功，则返回NULL |

​	dlopen函数的参数flag可取的值有：RTLD_LAZY、RTLD_NOW、RTLD_GLOBAL。其含义为：

- RTLD_LAZY：在dlopen()返回前，对于动态中存在的未定义变量(如外部变量extern，也可以是函数)不执行解析，也就是不解析这个变量的地址
-  RTLD_NOW：与RTLD_LAZY不同，在dlopen返回前，解析出每个未定义变量的地址，如果解析不出来，dlopen会返回NULL，错误为"undefined symbol:xxxx"。
- RTLD_GLOBAL：使库中被解析出来的变量在随后的其他链接库中也可以使用，即全局有效。

test.c

```c
#include <stdio.h>
#include <dlfcn.h>
#include <stdlib.h>
int main(void)
{
    void *handle;
    char *error;
    void(*welcom)();

    // if((handle = dlopen("/tmp/lib/libttt.so",RTLD_NOW)) == NULL);
    // {
    //     printf("dlopen error:%s\n",dlerror());
    //     exit(1);
    // }
    handle = dlopen("/tmp/lib/libttt.so",RTLD_NOW);
    welcom = dlsym(handle, "welcome");//返回函数给指针
    if((error = dlerror()) != NULL)
    {
        printf("dlsym error\n");
        exit(1);
    }

    welcom();
    dlclose(handle);

    exit(0);

}
//dlopen会返回NULL，但神奇的是dlsym能用NULL返回正确的函数的地址
```

执行结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/dlopen.png)

​	-ldl指明dlopen等函数所在的库。程序成功调用了动态链接库libttt.so中的welcome()函数。通过在程序中调用系统函数来使用动态链接库时，动态链接库所在的目录须包含在程序中，如使用"dlopen("./libttt.so",RTLD_LAZY)"，如果动态库libttt.so不在当前目录下，程序运行将出错。

# 网络编程

## 基本原理

​	TCP/IP协议是目前世界上使用最广泛的网络通信协议，日常生活中的大部分网络应用都是基于该系列协议。

### 网络模型与协议

​	大多数网络模型都是按层(layer)的方式来组织的。在分层网络模型中，**每一层都为上一层提供一定的服务，而把如何实现本层服务的细节对上一层加以屏蔽**。上层只需知道下层提供了什么功能以及对应于这些功能的接口，而不必关心下一层如何实现这些功能。

​	为确保使用不同硬件和底层协议构建的网络能互相通信，国际标准化组织(ISO)制定了一套被称为开放系统互联(OSI)的规范。但当前普遍使用的是TCP/IP模型。几乎所有互联网设备都支持TCP/IP协议。

TCP/IP各层功能如下：

1**.网络接口层**

​	网络接口层是TCP/IP模型最下一层，它包括多种逻辑链路控制和媒体访问协议。网络接口层负责将Internet层发送来的数据分成帧，并通过物理链路进行传送，或从网络上接收物理帧，抽取其数据并转交给其上的Internet层

2.**Internet层(网络层)**

​	网络层负责在发送端和接收端之间建立一条虚拟路径。这一层的主要协议是IP协议。IP协议并不保证数据能完整正确地到达目的地，这个任务由它上面的传输层来完成。这一层的ABR协议(地址解析协议)和RARP协议(反向地址解析协议)用于IP地址和物理地址(通常就是网卡地址)的相互转换。如果数据在传输过程中出现问题，该层的ICMP协议将产生错误报文.

3.**传输层**

​	传输层通过位于该层的TCP协议(传输控制协议)**或**UDP协议(用户数据报协议)在两台主机间传输数据。其中TCP协议提供可靠的面向连接的服务，它保证数据能完整地按顺序地传送到目标计算机。它在传输数据前首先需要和目的计算机建立连接，并且在数据传输过程中维持此连接，因此在速度上会有些损失。UDP提供简单的无连接服务，它不保证数据能按顺序、正确地传送到目的地(**但可由它的上层来保证**)，它不用建立连接，通常速度上要比TCP快些。TCP协议和IP协议都需要网络层提供通往目的地的路由。传输层提供端到端，即应用程序之间的通信。该层的主要功能有差错控制、传输确认和丢失重传等。

4.**应用层**

​	应用层面向用户提供一系列访问网络的协议，如用于传输文件的FTP协议、用于远程登录的Telnet协议、用于发送电子邮件的SMTP协议(简单邮件传输协议)，以及最常用的用于浏览网页的HTTP协议(超文本传输协议)。还有点对点共享文件协议，BitTorrent协议，该协议基于HTTP协议。使用该协议构建的就有BT下载工具。

​	协议简单地讲就是通信实体为实现正确的通信而制定的规约。协议就是两台计算机要进行通信所使用的双方都能理解的语言。TCP/IP是由许多协议构成的协议簇，如TCP、UDP、IP、FTP和HTTP等

### 地址

​	为了使网络上的计算机能进行互相通信，必须有一个唯一的标识来区分网络上每台计算机。有两种标识可以使用：物理地址(如网卡地址)和IP地址。

#### 物理地址

​	对于以太网来说，物理地址就是一个48位的位串，此地址在网卡的生产过程中就已经固定了，是不可更改的，并且是全球唯一的。在Shell下输入命令ifconfig可以查看到本机的物理地址：

```
虚拟机没这玩意捏
```

​		在正常机器的HWaddr后面即是以16进制表示的48位(6字节)网卡地址，每个字节用冒号隔开。有些计算机可能会有多块网卡，每块网卡代表计算机的一个网络接口，这种计算机称为多宿主计算机(Multihomed Computer)

#### IP地址

​	TCP/IP协议能够使计算机之间进行与底层物理网络无关的通信，底层网络可以是以太网也可以是令牌环网或者是其他类型的网，两台计算机可以位于不同的局域网内，但通过TCP/IP协议它们也能够进行通信。物理地址(如网卡地址)虽然也能唯一的标识网络上的每台计算机，但是由于物理地址依赖于底层网络(不同的底层网络应用不同的物理地址)，因此必须**使用一个与底层硬件技术无关的通用地址来表示网络上的每台计算机，这就是IP地址**。IP地址由32个比特位构成，它分为两部分：计算机所在的网络号和该网络给计算机分配的主机号，分别成为网络ID和主机ID。

​	32位(4字节)的IP地址为了表示方便也是每字节隔开，不过不是使用冒号而是使用点号，如，腾讯网(www.qq.com)的IP地址是202.205.3.195.一个字节可以表示的数的范围是00000000~11111111(即0~255)。因此在理论上IP地址的范围是”0.0.0.0~255.255.255.255，但实际上有些地址是专用的，不能用来标识计算机。

​	IP地址按一定的格式分为5类：A类、B类、C类、D类和E类。

​	![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/IP.png)

​	A类地址使用8位作为网络地址，其余24位为主机地址，网络地址的第一位固定为0。B类地址是用16位作为网络地址，其余16位为主机地址，网络地址的第一、二位固定为10。C类地址使用24位作为网络地址，其余8位位主机地址，网络地址的第一、二、三位固定为110.D类地址和E类地址比较少用。

​	127.0.0.1是一个特殊的地址，它指代本机，用于测试本机上的TCP/IP协议是否正常工作。输入命令"ping 127.0.0.1"，如有回应说明本机上的TCP/ID协议工作正常。

​	TCP/IP上的每台主机还有一个32位的子网掩码，它用来区分IP地址的网络号和主机号。将IP地址与子网掩码进行按位"与"运算就可以得到IP地址的网络号，网络号是一台主机所处的网络的编号。例如，有一台主机Ip地址为222.197.168.244，对应的子网掩码为255.255.255.0，两者做"与"运算得到的结果为222.197.168.0，那么这台主机所处的网络的编号为222.197.168.0

​	随着越来越多计算机接入互联网中，IP地址已经快耗尽了。于是提出了IPv6，IPv6使用一个128位的IP地址来标识一台计算机，原来的版本称为IPv4。使用IPv6，IP地址很难被耗尽，因为它可以表示的计算技术是2的128次方。

#### 端口

​	用网络地址可以惟一的标识网络上的每台计算机。通常一台计算机上会同时运行多个程序，而它们可能同时要访问网络。对于一台计算机上的多个应用程序，TCP和UDP协议采用16位的端口号来识别它们。一台主机上的不同进程可以绑定到不同的端口上，这些进程都可以访问网络而互不干扰

​	TCP/IP将端口号(端口号是一个16位的无符号整数，因此端口号的范围是0~2的16次方，即0~65535)分为两部分：一部分是保留端口即知名端口，范围为0~1023，这些端口由权威机构规定其用途，如编号为21的TCP端口由FTP协议专用，80号TCP端口由HTTP协议专用；其余的我自由端口，用户进程可以自由申请和使用。

#### IP协议

​	IP协议(Internet Protocal)是网络层最重要的协议。无论传输层使用何种协议，都要依靠IP协议来确定到达目的计算机的路由。IP协议主要负责指定路由，当到达同一目的地有多条路由时，IP协议会选择一条最短路由来将数据分组传送到目的计算机。同时，IP协议还定义了一组规则，如，有时目的地不可达或不存在，IP协议规定了在这种情况下如何丢弃传送中的数据分组。IP协议定义了数据单元格式，称为IP数据报。它由IP首部和数据量部分组成，IP数据报如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/IPHEAD.png)

主要字段的含义如下：

1.**版本**

​	表示该数据报采用的是哪个版本的IP协议。该字段长度为4位。对于IPv4，这个字段的值位4，IPv6对应的值为6

2.**总长度**

​	IP首部和数据的总长度

3.**生存期**

​	该字段表示数据报在网络上的最大生存时间(Time to Live,TTL)。数据报每经过一个路由器，路由器将TTL值减一，当TTL减为0时，数据报将被丢弃，并且该路由器会向发送者返回一个ICMP超时报文，告知数据报被丢弃。TTL的默认值是64，最大值是255。

4.**协议**

​	该字段用于说明发送数据所使用的协议，如果0x06表示使用TCP协议传输数据，0x11表示使用UDP协议

5.**报头校验和**

​	该字段用于检查IP首部的完整性，该字段只校验IP首部，不校验数据

6.**源IP地址**

​	该字段为发送数据报的源计算机IP地址

7.**目的IP地址**

​	该字段为接收数据报的目的计算机IP地址

8.**IP选项**

​	该字段是一个可选的字段，主要用于网络调试

#### 用户数据报协议UDP

​	在TCP/IP模型中，UDP协议位于传输层，在网络层之上而在应用层之下。UDP协议向应用程序提供一种面向无连接的服务，通常UDP协议被用于不需要可靠数据传输的网络环境中。UDP不需要建立连接，应用程序采用UDP协议无需建立和维持连接。UDP协议不保证数据报按顺序、正确的到达目的地，这项任务由应用程序来完成

​	UDP数据报首部格式

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/UDP.png)

UDP首部的各部分含义：

1.源端口

​	发送UDP数据的源端口号

2.目标端口

​	接收UDP数据的目的端口号

3.长度

​	该字段表示包括UDP首部和数据在内的整个数据报的长度，以字节为长度

4.UDP校验和

​	该字段是根据IP首部。UDP首部和数据计算出来的值，当该字段被设置为0x0000时，表明发送端计算机没有计算校验和。

#### 传输控制协议TCP

##### TCP数据包的格式

​	TCP提供一种面向连接的、可靠的数据传输服务。

​	TCP数据包的首部格式：

![](D:\学习存档\笔记\tcphead.png)

主要字段的含义如下：

1.源端口

​	发送TCP数据的源端口号

2.目标端口

​	接收TCP数据的目的端口号

##### 使用TCP进行通信的过程

1.建立连接

第一步：连接的发起段(通常称为客户端)向目标计算机(通常称为服务器)发送一个请求建立连接的数据包。

第二步：服务器收到请求后，对客户端的同步信号做出相应，并发送自己的同步信号给客户端

第三步：客户端对服务器端发来的同步信号进行相应。连接建立完成，就可以进行数据传输了

这三个步骤顺利完成后，连接建立，这个过程也被称为"三次握手"

2.关闭

第一步：请求主机发送一个关闭连接的请求给另一方

第二部：另一方收到关闭连接的请求后，发送一个接受请求的确认数据包，并关闭它的socket连接。

第三步：请求主机收到确认数据包后，发送一个确认数据包，告知另一方其发送的确认已收到，请求主机关闭它的socket连接

#### 客户机/服务器模型

​	网络中的实际应用大多都可以归纳为客户机/服务器模型**(Client/Server模型、C/S模型**)，其中客户机是指请求服务的一方，服务器是指提供某种服务的一方。有些应用程序，请求服务的同时也提供一定的服务，拆开来看，这种服务也是基于客户机/服务器模型。

​	客户机/服务器模型既可以使用TCP协议也可以使用UDP协议，或者两者混合使用，可根据具体需要而定。在客户机/服务器模型中，通常服务器端的IP地址和端口号是固定的，客户端程序连接到服务器IP和端口。通常客户端的程序设计相对要简单一些。而服务器端由于要考虑多个客户端同时请求服务的问题，设计上相对复杂一些。

## 套接字编程

​	不论是Windows还是Linux都使用socket来开发网络应用程序。通常Linux下的网络编程就是指套接字编程。

### 套接字地址结构

##### struct sockaddr

​	结构struct sockaddr定义了一种通用的套接字地址，它在linux/sockt.h中的定义代码为：

```c
struct sockaddr{
    unsigned short			sa_family;	//地址类型，AF_xxx
    char					sa_data[14];	//14字节的协议地址
};
```

​	其中，成员sa_family表示套接字的协议族类型，对应于TCP/IP协议该值为AF_INET；成员sa_data存储具体的协议地址。sa_data之所以被定义成14个字节，因为有的协议族使用较长的地址格式。一般在编程中并不对该结构体进行操作，而是使用另一个与它等价的数据结构：sockaddr_in

##### struct sockaddr_in

​	每种协议族都有自己的协议地址格式，TCP/IP协议族的地址格式为结构体struct sockadd_in，它在netinet/in.h头文件中定义，格式为：

```c
struct sockaddr_in{
    unsigned short		sin_family;		//地址类型
    unsigned shrot int	sin_port;		//端口号
    struct in_addr		sin_addr;		//IP地址
    unsigned char 		sin_zero[8];	//填充字节，一般赋值为0
};
```

​	其中，成员sin_family表示地址类型，对于使用TCP/IP协议进行的网络编程，该值只能是AF_INET。sin_port是端口号，sin_addr用来存储32位的IP地址，数组sin_zero为填充字段，一般赋值为0。

##### struct in_addr

struct in_addr的定义如下：

```c
struct in_addr{
	unsigned long		s_addr;
};
```

​	结构体sockaddr的长度为16字节，结构体sockaddr_in的长度也为16字节。通常在编写基于TCP/IP协议的网络程序时，使用结构体sockaddr_in来设置地址，然后通过强制类型转换成sockaddr类型。

设置地址信息的示例代码：

```c
struct sockaddr_in sock;
sock.sin_family = AF_INET;
sock.sin_prot = htons(80);		//设置端口号为80
sock.sin_addr.s_addr = inet_addr("202.205.3.195");		//设置地址
memset(sock.sin_zero,0,sizeof(sock,sin_zero));		//将数组sin_zero清零
```

​	memset函数的原型为：

```c
memset(void *s,int c,size_t n);
```

​	它将s指向的内存区域的前n个字节赋值成由参数c指定的指。

### 创建套接字

##### socket

​	socket函数用来创建一个套接字，在Shell下输入"man socket"可获得该函数的原型：

```c
#include <sys/types.h>
#include <sys/socket.h>
int socket(int domain,int type,int protocol);
```

参数domain用于指定创建套接字所使用的协议族，它们在头文件linux/socket.h中定义。常用的协议族如下：

- **AF_UNIX**：创建只在本机内进行通信的套接字。
- **AF_INET**：使用IPv4 TCP/IP协议。
- **AF_INET6**：使用IPv6 TCP/IP协议。

参数type指定套接字的类型，可以取如下值。

- **SOCK_STREAM**：创建TCP流套接字
- **SOCK_DGRAM**：创建UDP数据报套接字
- **SOCK_RAW**：创建原始套接字

​	参数protocol通常设置为0，表示通过参数domain指定的协议族和参数type指定的套接字类型来确定使用的协议。当创建原始套接字时，系统无法唯一的确定协议，此时就需要使用该参数指定的类型所使用的协议

​	执行成功返回一个新创建的套接字；若有错误发生则返回-1，错误代码存入errno中。

创建TCP套接字：

```c
int sock_fd;
sock_fd = socket(AF_INET, SOCK_STREAM,0);
if(sock_fd < 0)
{
    perror("socket");
    exit(1);
}
```

创建UDP协议的套接字为：

```c
sock_fd = socket(AF_INET,SOCK_DGRAM,0);
```

### 建立连接

##### connect

​	函数connect用来在一个指定的套接字上创建一个连接，在Shell下输入"man connect"可获得该函数的原型：

```c
#include <sys/types.h>
#include <sys/socket.h>
int connect(int sockfd,const strucf sockaddr *serv_addr,socklen_t addrlen);
```

​	参数sockfd是一个由函数socket创建的套接字。如果该套接字的类型是SOCK_STREAM，则connect函数用于向服务器发出连接请求，服务器的IP地址和端口号由参数serv_addr指定。如果套接字的类型是SOCK_DGRAM，则connect函数**并不建立真正的连接，它只是告诉内核与套接字进行通信的目的地址**(由第二个参数指定)，只有该目的地址发来的数据才会被该socket接收。对于SOCK_DGRAM类型的套接字，调用connect函数的好处是不必在每次发送和接收数据时都指定目的地址。

​	通常一个面向连接的套接字(如TCP套接字)只能调用一次connect函数。而对于无连接的套接字(如UDP套接字)则可用多次调用connect函数以改变与目的地址的绑定。将参数serv_addr中的sa_family设置为AF_UNSPEC可以取消绑定。

​	addrlen为参数serv_addr的长度。

​	执行成功返回0，有错误发生则返回-1，错误代码存入errno中。

​	该函数的常见用法：

```c
struct sockaddr_in				serv_addr;
memset(&serv_addr,0,sizeof(struct sockaddr_in));		//将serv_addr的各个字段清零
serv_addr.sin_family = AF_INET;
serv_addr.sin_port = htons(80);	//htons是字节顺序转换函数
//inet_aton函数将一个字符串转换成一个网络地址，并把网络地址赋给第二个参数
if(inet_aton("172.17.242.131",&serv_addr.sin_addr) <0 )
{
    perror("inet_aton");
    exit(1);
}
//使用sock_fd套接字连接到由serv_addr指定的目的地址上，假定sock_fd已定义
if(connect(sock_fd,(struct sockaddr*)&serv_addr,sizeof(struct sockaddr_in)) < 0)
{
    perror("connect");
    exit(1);
}
```

​	**注：**serv_addr强制类型转换为struct sockaddr类型(原来是spcladdr_in)。

### 绑定套接字

##### bind

​	函数bind用来将一个套接字和某个端口绑定在一起，在Shell下输入"man 2 bind"可获取该函数的原型：

```c
#include <sys/types.h>
#include <sys/socket.h>
int bind(int sockfd,struct sockaddr *my_addr, socklen_t addrlen);
```

​	socket函数只是创建了一个套接字，这个套接字将工作在哪个端口上，程序并没有指定。在客户机/服务器模型中，服务器端的IP地址和端口号一般是固定的，因此在服务器端的程序中，使用bind函数将一个套接字和某个端口绑定在一起。该函数一般只有服务器端的程序调用。

​	参数my_addr指定了sockfd将绑定到的本地地址，可以将参数my_addr和sin_addr设置为INADDR_ANY而不是某个确定的IP地址就可以绑定到任何网络接口。对于只有一个IP地址的计算机，INADDR_ANY对应的就是它的IP地址；对于多宿主主机(拥有多块网卡)，INADDR_ANY表示本服务器程序将处理来自所有网络接口上相应端口的连接请求。

​	函数执行成功返回0，错误返回-1，错误代码存入errno

该函数的常见用法如下：

```c
struct sockaddr_in			serv_addr;
memset(&serv_addr,0 ,sizeof(struct sockaddr_in));
serv_addr.sin_family = AF_INET;
serv_addr.sin_port = htons(80);
serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);

if(bind(sock_fd,(struct sockaddr *)&serv_addr,sizeof(struct sockaddr_in))<0)
{
    perror("bind");
    exit(1);
}
```

### 在套接字上监听

##### listen

​	函数listen把套接字转化为被动监听，在Shell下输入"man listen"可获得该函数原型：

```c
#include <sys/socket.h>
int listen(int s,int backlog);
```

​	由函数socket创建的套接字是主动套接字。这种套接字可以用来主动请求连接到某个服务器(通过函数connect)。但**作为服务器端的程序，通常在某个端口上监听等待来自客户端的连接请求**。在服务器端，一般是先调用函数socket创建一个主动套接字，然后调用函数bind将该套接字绑定到某个端口上，接着再调用函数listen将该套接字转化为监听套接字，等待来自于其他客户端的连接请求。

​	一般多个客户端连接到一个服务器，服务器向这些客户端提供某种服务。服务器端设置一个连接队列，记录已经建立的连接，参数backlog指定了该连接队列的最大长度。如果连接队列已经达到最大，之后的连接请求将被服务器拒绝。

​	执行成功返回0，错误发生返回-1，错误代码存入errno中。

**注：**函数listen只是将套接字设置为倾听模式以等待连接请求，它并不能接收连接请求，真正接受客户端连接请求的是函数accept()。

该函数的常见用法如下：

```c
#define LISTEN_NUM     12 //定义连接请求队列长度
...
    if(listen(sock_fd,LISTEN_NUM) <0)
    {
        perror("listen");
        exit(1);
    }
```

### 接受连接

##### accept

​	函数accept用来接受一个连接请求，在Shell下输入"man 2 accept"可获得该函数的原型：

```c
#include <sys/type.h>
#include <sys/socket.h>
int accept(int s,struct sockaddr *addr ,socklen_t *addrlen);
```

​	参数s是由函数socket创建，经函数bind绑定到本地某一端口上，然后通过函数listen转化而来的**监听套接字**。

- 参数addr用来保存发起连接请求的主机的地址和端口
- 参数addrlen是addr所指向的结构体的大小

​	执行成功返回一个新的代表客户端的套接字，出错则返回-1，错误代码存入errno中。

​	只能对面向连接的套接字使用accept函数。accept执行成功时，将创建一个新的套接字，并且为这个新的套接字分配一个套接字描述符，并返回这个新的套接字描述符。这个新的套接字描述符与打开文件时返回的文件描述符类似，进程可以利用这个新的套接字描述符与客户端交换数据，参数s所指定的套接字继续等待客户端的连接请求。

​	如果参数s所指定的套接字被设置为阻塞方式(Linux下的默认方式)，且连接请求队列为空，则accept()将被阻塞直到有连接请求到达为止；如果参数s所指定的套接字被设置为非阻塞方式，则如果队列为空，accept立即返回-1，errno被设置为EAGAIN。

套接字为阻塞方式下该函数的常见方法：

```c
int client_fd;
int client_len;
struct sockaddr_in		client_addr;
...
client_len = sizeof(struct sockaddr_in);
client_fd = accept(sock_fd, (struct sockaddr *)&client_addr, &client_len);
if(conn_fd < 0)
{
    perror("accept");
    exit(1);
}
```

### TCP套接字的数据传输

#### 发送数据

##### send

​	函数send用来在TCP套接字上发送数据，在Shell下输入"man 2 send"可获取函数原型：

```c
#include <sys/types.h>
#include <sys/socket.h>
ssize_t send(int s,const void *msg,size_t len,int flags);
```

​	函数send只能对处于连接状态的套接字使用。参数s为已建立好连接的套接字描述符，即accept函数的返回值。参数msg指向存放待发送数据的缓冲区，参数len为待发送数据的长度。

​	参数flags为控制选项，一般设置为0或取以下值：

- **MSG_OOB**：在指定的套接字上发送带外数据(out-of-band data)，该类型的套接字必须支持带外数据(如**SOCK_STREAM**)。
- **MSG_DONTROUTE**：通过最直接的路径发送数据，而忽略下层协议的路由设置。

​	如果要发送的数据太长而不能发送时，将出现错误，errno设置为EMSGSIZE；如果要发送的数据长度大于该套接字的缓冲区剩余空间大小时，send()一般会被阻塞，如果该套接字被设置为非阻塞方式，则此时立即返回-1并将errno设为EAGAIN。

​	执行成功返回实际发送数据的字节数，出错返回1，错误代码存入errno中。

**注：**执行成功只是说明数据写入套接字的缓冲区中，并不表示数据已经成功地通过网络发送到目的地。

套接字为阻塞方式下，该函数的常见用法：

```c
#define BUFFERSIZE 			1500
char		send_buf[BUFFERSIZE];
...
if(send(conn_fd,send_buf,len,0) <0)
{//len为待发送数据的长度
    perror("send");
    exit(1);
}
```

#### 接收数据

##### recv

​	函数recv用来在TCP套接字上接收数据，在Shell下输入"man recv"可获得该函数的原型：

```c
#include <sys/types.h>
#include <sys/socket.h>
ssize_t recv(int s,void *buf,size_t len,int flags);
```

​	函数recv从参数s所指定的套接字描述符(必须是面向连接的套接字)上接收数据并保存到参数buf所指定的缓冲区，参数len为缓冲区长度。

参数flags为控制选项，一般设置为0或取以下数值：

- **MSG_OOB**：请求接收带外数据
- **MSG_PEEK**：只查看数据而不读出
- **MSG_WAITALL**：只在接收缓冲区满时才返回

​	如果一个数据包太长以至于缓冲不能完全放下时，剩余部分的数据可能被丢弃(根据接收数据的套接字类型而定)。如果在指定的套接字上无数据到达时，recv()将被阻塞，如果该套接字被设置为非阻塞方式，则立即返回-1并将errno设置为EAGAIN。函数recv接收到数据就返回，并不会等待接收到参数len指定长度的数据才返回。

​	执行成功后接收到的数据字节数，出错则返回-1，错误代码存入errno中。

​	套接字为阻塞方式下该函数的常见用法：

```c
char		recv_buf[BUFFERSIZE];
...
if(recv(conn_fd,recv_buf,sizeof(recv_buf),0) <0 )
{
    perror("recv");
    exit(1);
}
```

### UDP套接字的数据传输

#### 发送数据

##### sendto

​	函数sendto用来在UDP套接字上发送数据，在Shell下输入"man sendto"可获取其函数原型：

```c
#include <sys/types.h>
#include <sys/socket.h>
ssize_t sendto(int s,const void* msg,size_t len,int flags,const struct sockaddr *to,socklen_t tolen);
```

​	函数sendto功能与函数send类似，但函数sendto不需要套接字处于连接状态，所以该函数通常用来发送UDP数据。同时因为是无连接的套接字，在使用sendto时需要指定数据的目的地址。

​	参数msg指向待发送数据的缓存区，参数len指定了待发送数据的长度，参数flags是控制选项，含义与send()一致，参数to用于指定目的地址，目的地址的长度由tolen指定。

​	执行成功返回实际发送数据的字节数，错误则返回-1，错误代码写入errno中。

该函数的常见用法：

```c
char			send_buf[BUFFERSIZE];
struct	sockaddr_in		dest_addr;

//设置目的地址
memset(&dest_addr ,0,sizeof(struct sockaddr_in));
dest_addr.sin_family = AF_INET;
dest_addr.sin_port = htons(DEST_PORT);
if(inet_aton("172.17.242.131",&dest_addr.sin_addr) < 0)
{
    perror("inet_aton");
    exit(1);
}

if(sendto(sock_fd,send_buf,len,0,(struct sockadddr *)&dest_addr,sizeof(struct sockaddr_in)) < 0)
{
    perror("sendto");
    exit(1);
}
```

#### 接收数据

##### recvfrom

​	函数recvfrom用来在UDP套接字上接收数据，在Shell下输入"man recvfrom"可获得函数原型：

```c
#include <sys/types.h>
#include <sys/socket.h>
ssize_t recvfrom(int s,void *buf,size_t len,int flags,struct sockaddr *from,socklen_t *fromlen);
```

​	函数recvfrom与函数recv功能类似，只是函数recv只能用于面向连接的套接字，而函数recvfrom没有此限制，可以用于从无连接的套接字(如UDP套接字)上接收数据。

​	参数buf指向接收缓冲区，参数len指定了缓冲区的大小，参数flags是控制选项，含义与recv一致。如果参数from非空，且该套接字不是面向连接的，则函数recvfrom返回时，参数from中将保存数据的源地址，参数fromlen在调用recvfrom前为参数from的长度，调用recvfrom后将保存from的实际大小。

​	执行成功返回实际接收到数据的字节数，出错则返回-1，错误代码存入errno中。

套接字为阻塞方式下该函数的常用方法：

```c
char		recv_buf[BUFFERSIZE];
struct  sockaddr_in	src_adddr;
int src_len;

src_len = sizeof(struct sockaddr_in);
if(recvfrom(sock_fd,recv_buf,sizeof(recv_buf),0,(struct sockaddr*)&src_addr,&src_len) < 0)
{
    perror("again_recvfrom");
    exit(1);
}
```

### 关闭套接字

##### close

​	函数close用来关闭一个套接字描述符，它与关闭文件描述符是类似的，在Shell下输入"man close"可获得该函数的原型：

```c
#include <unistd.h>
int close(int  fd);
```

​	函数fd为一个套接字描述符。该函数关闭一个套接字。

​	执行成功返回0，出错则返回-1，错误代码存入errno中

##### shutdown

​	函数shutdown也用于关闭一个套接字描述符，在Shell下输入"man 2 shutdown"可获得该函数的原型：

```c
#include <sys/socket.h>
int shutdown(int s,int how);
```

​	函数shutdown的功能与函数close类似，但是shutdown()功能更强大，可以对套接字的关闭进行一些更细致的控制，它允许对套接字进行单向关闭或全部禁止。参数s为待关闭的套接字描述符，参数howto指定了关闭的方式，具体取值如下：

- **SHUT_RD**：将连接上的读通道关闭，此后进程将不能再接受任何数据，接收缓冲区中还未被读取的数据也将被丢弃，但仍然可以在该套接字上发送数据。
- **SHUT_WR**：将连接上的写通道关闭，此后进程将不能再发送任何数据，发送缓冲区中还未被发送的数据也将被丢弃，但仍然可以在该套接字上接收数据。
- **SHUT_RDWR**：读、写通道都将被关闭

​	执行成功返回0，出错返回-1，错误代码存入 errno中。

### 主要系统调用函数

#### htonl--字节顺序和转换函数

​	不同机器内部对变量的字节存储顺序不同，有的采用大端模式(big-endian)，有的采用小端模式(little-endian)，**大端模式是指高字节数据存放在低地址处，低字节数据存放在高地址处**；**小端模式是指低字节数据存放在内存低地址处，高字节数据数据存放在内存高地址处**。

0x 04030201分别在大小端模式下的存储格式

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/DEAN.png)

​	在网络上传输数据时，由于数据传输的两端可能对应不同的硬件平台，采用的存储字节顺序也可能不一样，因此TCP/IP协议规定了在网络上必须采用网络字节顺序(也就是大端模式)。通过对大小端模式存储原理的分析也可以发现，对于**char型数据，由于其只占一个字节，所以不存在这个问题，这也是一般将数据缓冲区定义为char型的原因之一**。而对于IP地址、端口号等非char型数据，必须在数据发送到网络上之前将其转换为大端模式，在接收到数据之后再将其转换成符合接收接收端主机的存储模式。

​	Linux系统为大小端模式的转换提供了4个函数，在Shell下输入"man byteorder"可获取它们的函数原型：

```c
#include <netinet/in.h>
uint32_t  htonl(uint32_t hostlong);
uint16_t  hotns(uint16_t hostshort);
uint32_t  ntohl(uint32_t netlong);
uint16_t  ntohs(uint16_t netshort);
```

​	htonl表示host to network long，用于将主机unsigned int型数据转换成网络字节顺序；htons表示host to network short，用于将主机unsigned shrot型数据转换成网络字节顺序；ntohl、ntohs的功能分别与htonl、htons相反。

#### inet系列函数

​	通常我们习惯于使用字符串形式的网络地址(如"172.17.242.131")，然而在网络上进行数据通信时，需要使用的是二进制形式且为网络字节顺序的IP地址，如"172.17.242.131"对应的二进制形式为：0x83f211ac。Linux系统为网络地址的格式转换提供了一系列函数，在Shell下输入"man inet"可获取它们的函数原型：

```c
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
int inet_aton(const char *cp,struct in_addr *inp);
in_addr_t inet_addr(const char *cp);
in_addr_t inet_network(const char *cp);
char* inet_ntoa(struct in_addr in);
struct in_addr inet_makeaddr(int net,int host);
in_addr_t inet_lnaof(struct in_addr in);
in_addr_t inet_netof(struct in_addr in);
```

##### inet_aton

​	inet_aton()将参数cp所指向的字符串形式的IP地址转换为二进制的网络字节顺序的IP地址，转换后的结果存于inp所指向的空间中。执行成功返回非0值，参数无效返回0。

##### inet_addr

​	该函数的功能与inet_aton()类似，它将参数cp所指向的字符串形式的网络地址转换为网络字节顺序形式的二进制IP地址，执行成功后将转换后的结果返回，参数无效返回INADDR_NONE(一般该值为-1)

​	该函数已经过时，推荐使用函数inet_aton()。因为对有效地址"255.255.255/255"它也返回-1(因为-1的补码形式为0xFFFFFFFF)，使得用户可能将"255.255.255.255"当成无效的非法地址，而是用inet_aton()则不存在这种问题

##### inet_network

​	inet_network()将参数cp所指向的字符串形式的网络地址转换为主机字节顺序形式的二进制IP地址，执行成功返回转换后的结果，参数无效返回-1。

##### inet_ntoa

​	该函数将值为in的网络字节顺序形式的二进制IP地址转换成以"."分隔的字符串形式，执行成功返回字符串的指针，参数无效返回NULL。

##### inet_makeaddr

​	该函数将把网络号为参数net，主机号为参数host的两个地址组合成一个网络地址，如net取0xac11(172.17.0.0，主机字节顺序形式)，host取0xf283(0.0.242.131，主机字节顺序形式)，则组合后的地址为172.17.242.131，并表示为网络字节顺序形式0x83f211ac。

##### inet_lnaof

​	该函数从参数in中提取出主机地址，执行成功返回主机字节顺序形式的主机地址。如172.17.242.131，属于B类地址，则主机号为低16位，主机地址为0.0.242.131，按主机字节顺序输出则为0xf283。

##### inet_netof

​	该函数从参数in中提取出网络地址，执行成功返回主机字节顺序形式的网络地址。如172.17.242.131，属于B类地址，则高16位表示网络号，网络地址位172.17.0.0，按主机字节顺序输出则为0xac11.

inet函数族的使用：

```c
//示例inet函数族的使用
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

int main()
{
    char            buffer[32];
    int             ret = 0;
    int             host = 0;
    int             network = 0;
    unsigned int    address = 0;
    struct in_addr in;

    in.s_addr = 0;

    //输入一个以"."分割的字符串形式的IP地址
    printf("please input your ip address:");
    fgets(buffer,31,stdin);
    buffer[31] = '\0';

    //示例使用inet_aton()函数
    if((ret = inet_aton(buffer, &in)) == 0)
    {
        printf("inet_aton : \t invalid address\n");
    }
    else
    {
        printf("inet_aton:\t0x%x\n",in.s_addr);
    }

    //示例使用inet_addr()函数 
    if((address = inet_addr(buffer)) == INADDR_NONE)
    {
        printf("inet_addr :\t invalid address\n");
    }
    else
    {
        printf("inet_addr:\t0x%lx\n",address);
    }

    //示例使用inet_network()函数
    if((address = inet_network(buffer)) == -1)
    {
        printf("inet_network: \t invalid address\n");
    }
    else
    {
        printf("inet_network: \t0x%lx\n",address);
    }

    //实例使用inet_ntoa()函数
    if(inet_ntoa(in)  == NULL)
    {
        printf("inet_ntoa: \t invalid address\n");
    }
    else
    {
        printf("inet_ntoa:\t%s\n",inet_ntoa(in));
    }
    
    //示例使用inet_lnaof()与inet_netof()函数
    host = inet_lnaof(in);
    network = inet_netof(in);
    printf("inet_lnaof:\t0x%x\n",host);
    printf("inet_netof:\t0x%x\n",network);

    in = inet_makeaddr(network,host);
    printf("inet_makeaddr:0x%x\n",in.s_addr);

    return 0;
}
```

运行程序，并输入172.17.242.131：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/inet1.png)

再次运行程序，输入255.255.255.255，结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/inet2.png)

​	从运行结果可以看出，函数inet_addr()和inet_network()把地址"255.255.255.255"当成了无效地址

#### getsockopt()和setsockopt()

​	套接字创建之后，就可以利用它来传输数据，但有时可能对套接字的工作方式有特殊的要求，此时就需要修改套接字的属性。系统提供了套接字选项来控制套接字的属性，使用函数getsockopt可以获取套接字的属性，使用setsockopt()可以设置套接字的属性。

​	在Shell下输入"man getsockopt"可以获取他们的原型：

```c
#include <sys/types.h>
#include <sys/socket.h>
int getsockopt(int s,int level,int optname,void *optval,socklen_t *optlen);
int setsockopt(int s,int level,int optname,const void* optval,socklen_t optlen);
```

​	参数s为一个套接字，参数level是进行套接字选项操作的层次，可以取SOL_SOCKET(通用套接字)、IPPROTO_IP(IP层套接字)、IPPROTO_TCP(TCP层套接字)等值，一般取SOL_SOCKET来进行与特定协议不相关的操作。参数optname是套接字选项的名称。

​	对于函数getsockopt，参数optval用来存放获得的套接字选项，参数optlen在调用函数前其值为optval指向的空间的大小，调用完成后则其值为参数optval所保存的结果的实际大小。对于参数setsockopt，参数optval是待设置的套接字选项的值，参数optlen是该选项的长度。

​	这两个函数执行成功时都返回0，出错都返回-1，错误代码存入errno中。

可使用命令"man 87socket"获取通用套接字SOL_SOCKET的选项：

- **SO_KEEPALIVE**

​	如果没有设置**SO_KEEPALIVE**选项，那么即使TCP连接已经很长时间没有数据传输时，系统也不会检测这个连接是否还有效。对于服务器进程，如果某一客户端非正常断开连接，则服务器进程将一直被阻塞等待。因此服务器端程序需要使用这个选项，如果某个客户端一段时间内没有反应则关闭连接。

- **SO_RCVLOWAT**和**SO_SNDLOWAT**

​	**SO_RCVLOWAT**表示接收缓冲区的下限，只有当接收缓冲区中的数据超过了**SO_RCVLOWAT**才会将数据传送到上层应用程序。**SO_SNDLOWAT**表示发送缓冲区的下限，只有当发送缓冲区中数据超过了**SO_SNDLOWAT**才会将数据发送出去。Linux下这两个值都为1且不能更改，也就是说只要有数据就接收或发送。这两个选项只能使用getsockopt函数获取，不能用setsockopt函数更改。

- **SO_RCVTIMEO**和**SO_SNDTIMEO**

​	这两个选项可以设置对套接字读或写的超时时间，具体时间由下面这个结构指定：

```c
struct timeval{
    long tv_sec;		//秒数
    long tv_usec		//微秒数
};
```

​	成员tv_sec指描述，tv_usec指定微秒数。超时时间为这两时间的和。在某个套接字连接上，若读或写超时，则认为接受或发送数据失败。

- **SO_BINDTODEVVICE**

​	将套接字绑定到特定的网络接口如"eth0"，此后只有该网络接口上的数据才会被套接字处理。如果将选项值设置为空字符串或选项长度设为0将取消绑定。

- **SO_DEBUG**

​	该选项只能对TCP套接字使用，设置了该选项 后系统将保存TCP发送和接收的所有数据的相关信息，以方便调试程序。

- **SO_REUSEADDR**

​	Linux系统中，如果一个socket绑定了一个端口，该socket正常关闭或程序异常退出后的一段时间内，该端口依然维持原来的绑定状态，其他程序无法绑定到该端口，如果设置了该选项则可以避免这个问题，示例代码：

```c
int option_value = 1;
int length = sizeof(option_value);
setsockopt(sock_fd,SOL_SOCKET,SO_REUSEADDR,&option_value,length);
```

- **SO_TYPE**

​	用于获取套接字的类型，如SOCK_DGRAM、SOCK_STREAM、SOCK_SEQPACKET、SOCK_RDM等。该选项只能被函数getsockopt用来获取陶杰类型，而不能使用函数setsockopt修改套接字的类型

- **SO_ACCEPTCONN**

​	该选项用来检测套接字是否处于监听状态，如果为0表示处于非监听状态，如果为1表示正在监听，该选项只能被函数getsockopt用来获取监听状态信息。

- **SO_DONTROUTE**

​	设置该选项表示在发送IP数据包时不使用路由表来寻找路由

- **SO_BROADCAST**

​	该选项用来决定套接字是否能够在网络上广播数据。实际应用中要在网络上广播数据必须硬件支持广播(如以太网支持广播)，并且使用的是SOCK_DGRAM套接字。系统默认不支持广播，如果希望该SOCK_DGRAM套接字支持广播，则可用这样来修改设置：

```c
int option_value = 1;
setsockopt(sock_fd,SOL_SOCKET,SO_BROADCAST,&option_value,sizeof(int));
```

- **SO_SNDBUF**和**SO_RCVBUF**

​	这两个选项用于设置套接字的发送和接收缓冲区的大小。对于TCP类型的套接字，缓冲区太小会影响TCP的流量控制；对于UDP类型的套接字，如果套接字的数据缓冲区满则后续数据将被丢弃，实际应用中应根据需要设置一个合适的大小。

- **SO_ERROR**

​	获取套接字内部的错误变量so_error，当套接字上发生了异步错误时，系统将设置套接字的so_error。异步错误是指错误的发生和错误被发现的时间不一致，通常在目的主机非正常关闭时发生这种错误。该选项只能被函数getsockopt用来获取so_error。

**注：**调用完函数getsockopt只会so_error的值将自动被重新初始化。

#### 多路复用select()

​	在客户端/服务器模型中，服务器端需要同时处理多个客户端的连接请求，此时就需要使用多路复用。实现多路服用最简单的方法是采用非阻塞方式套接字，服务器端不断地查询各个套接字的状态，如果有数据到达则读出数据，如果没有数据则查看下一个套接字。这种方法虽然简单，但在轮询过程中浪费了大量的CPU时间，效率非常低。

​	另一种方法是服务器进程并不主动地询问套接字状态，而是向系统登记希望监视的套接字，然后阻塞。当套接字上有事件发生时(如有数据到达)，系统通知服务器进程告知哪个套接字上发生了什么事件，服务器进程查询对应套接字并进行处理。在这种工作方式下，套接字上没有事件发生时，服务器进程不会去查询套接字的状态，从而不会浪费CPU时间，提高了效率。

​	使用函数select可以实现第二种多路复用，在Shell下输入"man select"可获得该函数的原型：

```c
#include <sys/select.h>
#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>
int select(int n,fd_set *readfds,fd_set *writefds,fd_set *exceptfds,struct timeval *timeout);
```

​	参数n是需要监视的文件描述符数，要监视的文件描述符值为0~n-1。**参数readfds指定需要监视的可读文件描述符集合**，当这个集合中的一个描述符上有数据到达时，系统将通知调用select函数的程序。**参数writefds指定需要监视的科协文件描述符集合**，当这个集合中的某个描述符可以发送数据时，程序将收到通知。**参数exceptfds指定需要监视的异常文件描述符集合**，当该集合中的一个描述符发生异常时，程序将收到通知。参数timeout指定了阻塞的时间，如果在这段时间内监视的文件描述符上都没有事件发生，则函数select()将返回0。

​	struct timeval的定义如下：

```c
struct timeval{
    long tv_sec;		//seconds
    long tv_usec;		//microseconds
};
```

​	成员tv_sec指定秒数，tv_usec指定微秒数。如果将timeout设为NULL，则函数select()将一直阻塞，直到某个文件描述符上发生了事件。如果将timeout设为0，则此时相当于非阻塞方式，函数select()查询完文件描述符集合的状态后立即返回。如果将timeout设成某一时间值，在这个时间内如果没有事件发生，函数select()将返回；如果在这段时间内有事件发生，程序将收到通知。

**注：**这里的文件描述符既可以是普通文件的描述符，也可以是套接字描述符

系统为文件描述符集合提供了一系列的宏以方便操作：

```c
FD_CLR(int fd, fd_set *set);		//将文件描述符fd从文件描述符集合中删除
FD_ISSET(int fd,fd_set *set);		//测试fd是否在set中
FD_SET(int fd,fd_set *set);			//在文件描述符集合set中添加文件描述符fd
FD_ZERO(fd_set *set);				//将文件描述符集合set清空
```

​	如果select()设定的要监视的文件描述符集合中有描述符发生了事件，则select将返回发生事件的文件描述符的个数。

test.c

```c
//示例函数select()的使用
#include <stdio.h>
#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>
#include <time.h>
#include <stdlib.h>

void display_time(const char *string)
{
    int         seconds;

    seconds = time((time_t *)NULL);
    printf("%s,%d\n",string,seconds);
}

int main(void)
{
    fd_set          readfds;
    struct timeval  timeout;
    int             ret;

    //监视文件描述符0是否有数据输入，文件描述符0表示标准输入，即键盘输入
    FD_ZERO(&readfds);  //开始使用一个描述符集合前一般要将其清空
    FD_SET(0,&readfds);

    //设置阻塞时间为10秒
    timeout.tv_sec = 10;
    timeout.tv_usec = 0;

    while(1)
    {
        display_time("before select");
        ret = select(1,&readfds,NULL,NULL,&timeout);
        display_time("after select");

        switch (ret)
        {
        case 0:
            printf("No data in ten seconds.\n");
            exit(0);
            break;
        case -1:
            perror("select");
            exit(1);
            break;
        default:
            getchar();//    将数据读入，否则标准输入将一直为读准备就绪
            printf("Data is available now.\n");
        }
    }
}
```

​	程序先初始化一个文件描述符集合readfds，然后将文件描述符0增加到这个文件描述符集合中，在调用select函数前将阻塞时间设置为10秒。函数time()用来获取从公元1970年1月1日0时0分0秒算起到现在所经过的秒数。执行结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/Select.png)

​	执行程序时，等待几秒钟后按下键盘任意键和Enter键，或者只按下Enter键。从执行过程及结果可以看出，在2秒后输入a，select()立即返回并打印出提示信息。而后再次进入循环，在同一秒重新设置select()监视键盘动作，这次没有键被按下，但只过了8秒select()就返回了，而不是预想中的10s。这是因为Linux系统对select()的实现中会修改参数timeout为剩余时间，第一次2s按下键盘，第二次就只剩下了8s。如果case 0分支不调用exit(0)，则从第三次开始将不阻塞(因为timeout为0)而出现打印信息的刷屏。

​	**如果在循环中使用select()，则参数timeout的初始化必须放在循环内部**。

## 面向连接的Client/Server实例

基于Client/Server模型的面向连接的网络程序，基本流程图如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/ServerClient.png)

### 服务器程序的设计

#### 服务器端的并发性

​	本程序采用多进程的方式来实现服务器对多个客户端连接请求的相应。主程序创建套接字后将套接字绑定在4507端口，也可以绑定到其他端口。然后使套接字处于监听状态，调用accept函数等待来自客户端的连接请求。每接受一个新的客户端连接请求，服务器端进程就创建一个子进程，在子进程中处理该连接请求，服务器端进程继续等待来自其他客户端的连接请求。

#### 数据格式

​	由于TCP是一种基于流的数据传输方式，数据没有固定的格式。因此需要在应用程序中定义一定的数据格式，本程序以回车符(字符'\n')作为一次数据的结束标志

#### 用户信息

​	本程序将用户信息保存在一个全局数组种。服务器端接收到来自客户端的登陆用户名中，在该全局数组种查询是否存在该用户名。若不存在，则回应字符'n'+结束标志(回车符'\n')；若存在，则回应字符'y'+结束标志，然后等待客户端的密码。若密码不匹配，则回应字符'n'+结束标志；若密码匹配，则回应字符'y'+结束标志，然后发送一个欢迎登陆的字符串给客户端

### 客户端程序的设计

​	客户端的应用程序相对于服务器端要简单，客户端主程序创建套接字后调用connect()连接到服务器端的4507端口，使用从connect()返回的套接字与服务器端进行通信，交换数据。



**服务器端程序**

```c
//Client/Server模型的服务器端
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <string.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <errno.h>
#include <stdlib.h>
#include "my_recv.h"    //自定义的头文件

#define SERV_PORT           4507//服务器的端口
#define LISTENQ             12  //连接请求队列的最大长度

#define INVALID_USERINFO    'n' //用户信息无效
#define VALID_USERINFO      'y' //用户信息有效

#define USERNAME            0   //接收到的是用户名
#define PASSWORD            1   //接收到的是密码
struct userinfo
{   //保存用户名和密码的结构体
    char username[32];
    char password[32];
};
struct userinfo users[ ] ={
    {"linux","unix"},
    {"4507","4508"},
    {"clh","clh"},
    {"xl","xl"},
    {" "," "}   //以只含一个空格的字符串作为数组的结束标志
};

//查找用户名是否存在，存在返回该用户名的下标，不存在则返回-1，出错返回-2
int find_name(const char *name)
{
    int i;

    if(name == NULL)
    {
        printf("in find_name, NULL pointer");
        return -2;
    }
    for(i=0;users[i].username[0] != ' ';i++)
    {
        if(strcmp(users[i].username,name) == 0 )\
            return 0;
    }

    return -1;
}

//发送数据
void send_data(int conn_fd,const char *string)
{
    if(send(conn_fd,string,strlen(string),0) < 0)
    {
        my_err("send",__LINE__);    //my_err函数在my_recv.h中声明
    }
}

int main()
{
    int                         sock_fd,conn_fd;
    int                         optval;
    int                         flag_recv = USERNAME;//表示接收到的是用户名还是密码
    int                         ret;
    int                         nmae_num;
    pid_t                       pid;
    socklen_t                   cli_len;
    struct sockaddr_in          cli_addr,serv_addr;
    char                        recv_buf[128];

    //创建一个TCP套接字
    sock_fd = socket(AF_INET, SOCK_STREAM,0);
    if(sock_fd <0)
        my_err("socket",__LINE__);

    //设置该套接字使之可以重新绑定端口
    optval = 1;
    if(setsockopt(sock_fd,SOL_SOCKET,SO_REUSEADDR,(void *)&optval,sizeof(int))<0)
        my_err("setsockopt",__LINE__);

    //初始化服务器端地址结构
    memset(&serv_addr,0,sizeof(struct sockaddr_in));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(SERV_PORT);
    serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);

    //将套接字绑定到本地端口
    if (bind(sock_fd,(struct sockaddr*)&serv_addr,sizeof(struct sockaddr_in)) <0 )
    {
        my_err("bind",__LINE__);
    }

    //将套接字转化为监听套接字
    if(listen(sock_fd,LISTENQ) < 0)
    {
        my_err("listen",__LINE__);
    }

    cli_len = sizeof(struct sockaddr_in);
    while(1)
    {
        //通过accept接收客户端的连接请求，并返回连接套接字用于收发数据
        conn_fd = accept(sock_fd,(struct sockaddr*)&cli_addr,&cli_len);
        if(conn_fd < 0)
            my_err("accept",__LINE__);

        printf("accept a new client , ip:%s\n",inet_ntoa(cli_addr.sin_addr));
        //创建一个子进程处理刚刚接收到的连接请求
        if((pid = fork()) == 0)
        {
            //子进程
            while(1)
            {
                if((ret = recv(conn_fd,recv_buf,sizeof(recv_buf),0)) < 0)
                {
                    perror("recv");
                    exit(1);
                }
                recv_buf[ret-1] = '\0';//将数据结束标志'\n'替换成字符串结束标志
                if(flag_recv == USERNAME)
                {
                    //接收到的是用户名
                    nmae_num = find_name(recv_buf);
                    switch(nmae_num)
                    {
                        case -1:
                            send_data(conn_fd,"n\n");
                            break;
                        case -2:
                            exit(1);
                            break;
                        default:
                            send_data(conn_fd,"y\n");
                            flag_recv=PASSWORD;
                            break;
                    }
                }
                else if(flag_recv == PASSWORD)
                {
                    //接收到的是密码
                    if(strcmp(users[nmae_num].password,recv_buf) ==0 )
                    {
                        send_data(conn_fd,"y\n");
                        send_data(conn_fd,"Welcome login my tcp server\n");
                        printf("%s login\n",users[nmae_num].username);
                        break;//跳出while循环
                    }
                    else
                        send_data(conn_fd,"n\n");
                }
            }
            close(sock_fd);
            close(conn_fd);
            eixt(0);    //结束子进程
        }
        else    //父进程关闭刚刚接收的连接请求，执行accept等待其他连接请求
            close(conn_fd);
    }
    return 0;
}
```

​	本程序首先创建一个TCP套接字并将其绑定到本地端口上，然后将其转化为监听套接字，再调用函数accept接收来自客户端的连接请求，收到请求后创建一个子进程来单独处理该连接请求。在子进程中，验证客户端的登陆用户名和密码，若正确则发送欢迎登录信息。

my_recv.c

```c
#define MY_RECV_C

#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include "my_recv.h"

//自定义的错误处理函数
void my_err(const char * err_string,int line)
{
    fprintf(stderr,"line:%d ",line);
    perror(err_string);
    exit(1);
}

/*
*函数名：my_recv
*秒数：从套接字上读取一次数据(以'\n'为结束标志)
*参数：conn_fd————从该连接套接字上接收数据
*      data_buf————读取到的数据保存在此缓冲中
*      len————data_buf所指向的空间长度
*返回值：出错返回-1，服务器端已关闭连接则返回0，成功返回读取的字节数
*/
int my_recv(int conn_fd,char *data_buf,int len)
{
    static char recv_buf[BUFSIZE];  //自定义缓冲区，BUFSIZE定义在my_recv.h中
    static char *pread;             //指向下一次读取数据的位置
    static int  len_remain=0;       //自定义缓冲区中剩余字节数
    int         i;
    //如果自定义缓冲区中没有数据，则从套接字读取数据
    if(len_remain <= 0)
    {
        if((len_remain = recv(conn_fd,recv_buf,sizeof(recv_buf),0)) < 0)
            my_err("recv",__LINE__);
        else if(len_remain == 0)
            return 0;
        pread = recv_buf;   //重新初始化pread指针
    }

    //从自定义缓冲区中读取一次数据
    for(i=0;*pread!= '\n';i++)
    {
        if(i>len)//防止指针越界
            return -1;
        data_buf[i] =*pread++;
        len_remain--;
    }
    //去除结束标志
    len_remain--;
    pread++;

    return i;       //读取成功
}
```

​	这是一个封装了函数recv的自定义读取数据的函数，实际上是将套接字缓冲区中的数据拷贝到自定义缓冲区，然后再按格式(以'\n'为结束标志)读取出数据。

my_recv.h

```c
#ifndef  __MY_RECV_H
#define  __MY_RECV_H
#define  BUFSIZE    1024
void my_err(const char * err_string,int line);
int my_recv(int conn_fd,char *data_buf,int len);
#endif
```

​	my_client.c

```c
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include "my_recv.h"

#define   INVALID_USERINFO          'n'//用户信息无效
#define   VAILD_USERINFO            'y'//用户信息有效

//获取用户输入存入到buf，buf的长度为len，用户输入数据以'\n'为结束标志
int get_userinfo(char *buf,int len)
{
    int i;
    int c;

    if(buf == NULL)
        return -1;
    
    i = 0;
    while(((c = getchar()) != '\n') &&(c != EOF) && (i < len-2) )
        buf[i++] = c;
    buf[i++] = '\n';
    buf[i++] = '\0';

    return 0;
}

//输入用户名，然后通过fd发送出去
void input_userinfo(int conn_fd, const char *string)
{
    char        input_buf[32];
    char        recv_buf[BUFSIZE];
    int         flag_userinfo;

    //输入用户信息知道正确为止
    do{
        printf("%s:",string);
        if(get_userinfo(input_buf,32) < 0)
        {
            printf("error return from get_userinfo\n");
            exit(1);
        }

        if(send(conn_fd, input_buf,strlen(input_buf),0) < 0 )
            my_err("send",__LINE__);
        
        //从连接套接字上读取一次数据
        if(my_recv(conn_fd,recv_buf,sizeof(recv_buf)) < 0 )
        {
            printF("data is too long\n");
            exit(1);
        }

        if(recv_buf[0] == VAILD_USERINFO)
            flag_userinfo = VAILD_USERINFO;
        else
        {
            printf("%s error,input again,",string);
            flag_userinfo = INVALID_USERINFO;
        }
    }while(flag_userinfo == INVALID_USERINFO);
}

int main(int argc,char **argv)
{
    int                 i;
    int                 ret;
    int                 conn_fd;
    int                 serv_port;
    struct sockaddr_in  serv_addr;
    char                recv_buf[BUFSIZE];

    //检查参数个数
    if(argc != 5)
    {
        printf("Usage: [-p] [serv_port] [-a] [serv_address]\n");
        exit(1);
    }

    //初始化服务器端地址结构
    memset(&serv_addr,0 ,sizeof(struct sockaddr_in));
    serv_addr.sin_family = AF_INET;
    //从命令行获取服务器端的端口与地址
    for(i=1;i<argc;i++)
    {
        if(strcmp("-p",argv[1]) == 0)
        {
            serv_port = atoi(argv[i+1]);
            if(serv_port < 0||serv_port > 65535)
            {
                printf("invalid serv_addr.sin_prot\n");
                exit(1);
            }
            else
                serv_addr.sin_port = htons(serv_port);
            continue;
        }
        if(strcmp("-a",argv[i]) == 0)
        {
            if(inet_aton(argv[i+1],&serv_addr.sin_addr) == 0 )
            {
                printf("invalid server ip address\n");
                exit(1);
            }
            continue;
        }
    }

    //检测是否少输入了某项参数
    if(serv_addr.sin_port == 0|| serv_addr.sin_addr.s_addr == 0)
    {
        printf("Usage:[-p] [serv_addr.sin_port] [-a] [serv_address]\n");
        exit(1);
    }

    //创建一个TCP套接字
    conn_fd = socket(AF_INET, SOCK_STREAM,0);
    if(conn_fd < 0)
        my_err("socket",__LINE__);

    //向服务器端发送连接请求
    if(connect(conn_fd,(struct sockaddr *)&serv_addr,sizeof(struct sockaddr)) <0)
        my_err("connect",__LINE__);

    //输入用户名和密码
    input_userinfo(conn_fd,"username");
    input_userinfo(conn_fd,"password");

    //读取欢迎信息并打印
    if((ret = my_recv(conn_fd,recv_buf,sizeof(recv_buf))) <0 )
    {
        printf("data is too long \n");
        exit(1);
    }
    for(i=0;i<ret;i++)
        printf("%c",recv_buf[i]);
    
    printf("\n");

    close(conn_fd);
    return 0;
}
```

​	程序首先创建一个TCP套接字，然后调用函数connect请求与服务器端连接。建立连接后，通过连接套接字首先发送用户名，然后等待服务器的确认，若用户名存在，则发送密码，若密码正确，则等待欢迎信息。

​	分辨编译服务器端和客户端程序，先在一个终端上运行服务器端程序，然后在几个终端运行客户端，以验证多进程方式的服务器对来自多个客户端的请求连接的处理：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/Connect1.png)

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/Connect2.png)

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/Connect3.png)

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/Connect4.png)

ip的选取需要ifconfig查看可用的ip，不然就是连接不上，超时。(127.0.0.1是本机ip，所有系统都通用，所以可以连接)

## 编写安全的代码

​	TCP/IP协议族在设计时并没有考虑到网络安全问题，所以使得基于TCP/IP协议族构建的Internet在安全性方面非常脆弱。

​	在所有安全漏洞中，威胁最大的时缓冲区溢出漏洞。所以在程序开发时应避免这些错误

### 缓冲区溢出

​	缓冲区溢出时指向缓冲区内填充的数据超过了缓冲区的容量，溢出的数据覆盖在原来合法数据上。产生缓冲区溢出的根本原因时：C及C++语言本质上是不安全的，没有机制用来检查引用数组和指针时的边界，导致越界访问。对边界的检查由程序员来完成，而这一任务往往会被程序员忽视或遗忘。

​	在Linux系统中，一个进程在内存中的数据主要分为3个部分：文本段、数据段和堆栈段，其中文本段存放可执行代码和制度数据，通常该区域的属性为只读；数据段主要用于存放全局变量、静态变量；临时变量、函数参数等存放在栈上；而由malloc函数动态分配的内存称为堆。

​	通常在程序要从外部接收输入数据时，系统会分配一块内存用于存放输入数据，这块内存通常称为缓冲区。如果输入的数据作为函数肚饿参数，那么它将被保存到栈上。当输入数据超过缓冲区所能容纳的最大容量时，而程序恰好没有对输入数据的长度做出检查，则缓冲区不能容纳的数据会存储到缓冲区之后的区域中。

​	一般情况下，覆盖其他数据区的数据是没有意义的，最多造成应用程序产生异常。但如果输入的多余数据是可执行的恶意代码。黑客在缓冲区溢出后让程序跳转到恶意代码处执行，黑客就获取了程序的控制权。如果该程序是以root身份运行，黑客就具有了root权限。黑客可以通过执行恶意代码创建一个管理员权限的账号或者植入一个后门以方便黑客对该系统的访问。

#### 如何防止缓冲区溢出

​	防止缓冲区溢出的一个重要方法是对程序中定义的缓冲区(如一个数组char s[32])作严格的边界检查。如果有超过缓冲区大小的内容被写入，程序应该报错，不允许将多余的数据写入缓冲区中。

​	应避免使用strcpy等存在溢出漏洞的函数，而是用srtncpy()或memcpy()等作为代替。

### 输入检查

​	对输入的参数进行检查可以有效地避免可能存在的溢出漏洞。编程中应该注意对输入数据的类型、输入数据的长度进行合法性检查。特别是指针参数，必须检查是否为空指针，它所指向的空间是否大于缓冲区的空间。

my_strcpy.c

```c
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

char *my_strcpy(char *strDest,const char *strSrc)
{
   char *p_return = strDest;

   //检查参数指针是否为空
   if(strDest == NULL || strSrc == NULL)
   {
      fprintf(stderr,"NULL POINT!");
      return NULL;
   }
   while( (*strDest++ = *strSrc++) != '\0')
      ;
   return p_return;
}

int main()
{
   char string1[32];
   char string2[32];
   int   c;
   int  i=0;

   printf("please input your string:");
   //对输入字符串的长度进行检查
   while( ( (c = getchar()) != '\n') && (c != EOF) && (i < 31) )
   {
      string2[i] =c;
      i++;
   }
   string2[i]='\0';
   //对返回值也进行合法性检查
   if(my_strcpy(string1,string2) == NULL)
   {
      fprintf(stderr,"return from my_strcpy");
      exit(1);
   }

   printf("string1:%s\n",string1);
   printf("string2:%s\n",string2);
   return 0;
}
```

​	在函数内部，首先对指针型参数进行合法性检查。在main函数中，对于输入的字符，判断其是否为文件结束标识符，以及是否遇到了回车键，同时还对输入字符的个数进行了检查。主函数中对函数my_strcpy的返回值也进行了检查。

### 端口扫描器

​	常用的端口扫描技术有很多种，如TCP connect扫描、TCP SYN扫描、TCP FIN扫描等，接下来用TCP connect扫描来实现自己的端口扫描程序my_scaner。

​	在扫描端口时使用多线程技术，把要扫描的所有端口平均分配给一些线程，每一个线程负责扫描一部分端口。主线程负责任务分配、启动各个子线程和等待各子线程结束。

my_scaner.c

```c
//端口扫描程序，只支持扫描TCP端口
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

//定义一个端口区间信息
typedef struct _port_segment{
   struct in_addr       dest_ip; //目标ip
   unsigned short int   min_port;//起始端口
   unsigned short int   max_port;//最大端口
}_port_segment ;

//自定义的错误处理函数
void my_err(const char* err_string,int line)
{
   fprintf(stderr,"line:%d ",line);
   perror(err_string);
   exit(1);
}

/*
*描述：扫描某一IP地址上的某一个端口的函数
*返回值：-1    出错
*        0     目标端口未打开
*        1     目标端口已打开
*/

int do_scan(struct sockaddr_in serv_addr)
{
   int      conn_fd;
   int      ret;

   //创建一个TCP套接字
   conn_fd = socket(AF_INET, SOCK_STREAM,0);
   if(conn_fd < 0)
      my_err("socket",__LINE__);

   //向服务器端发送连接请求
   if( (ret = connect(conn_fd, (struct sockaddr*)&serv_addr,sizeof(struct sockaddr))) <0)
   {
      if(errno == ECONNREFUSED)//目标端口未打开
      {
         close(conn_fd);
         return 0;
      }
      else//其他错误
      {
         close(conn_fd);
         return -1;
      }
   }
   else if(ret == 0)
   {
      printf("port %d found in %s\n",ntohs(serv_addr.sin_port),inet_ntoa(serv_addr.sin_addr));
      close(conn_fd);
      return -1;
   }
   return -1;  //实际执行不到这里，只是为了消除编译程序时的警告
}

//执行扫描的线程，扫描某一区间的端口
void * scaner(void *arg)
{
   unsigned short int      i;
   struct sockaddr_in      serv_addr;
   _port_segment            portinfo;//端口信息

   //读取端口区间信息
   memcpy(&portinfo, arg, sizeof(_port_segment));

   //初始化服务器端地址结构
   memset(&serv_addr,0,sizeof(struct sockaddr_in));
   serv_addr.sin_family = AF_INET;
   serv_addr.sin_addr.s_addr = portinfo.dest_ip.s_addr;

   for(i=portinfo.min_port;i<=portinfo.max_port;i++)
   {
      serv_addr.sin_port = htons(i);
      if(do_scan(serv_addr) < 0)
         continue;//出错则退出进程
   }
   return NULL;
}

//命令行参数：-m最大端口，-a目标主机的IP地址，-n最大线程数
int main(int argc,char **argv)
{
   pthread_t*        thread;     //指向所有的线程ID
   int               max_port;   //最大端口号
   int               thread_num; //最大线程数
   int               seg_len;    //端口区间长度
   struct in_addr    dest_ip;    //目标主机IP
   int               i;

   //检查参数个数
   if (argc != 7)
   {
      printf("Usage: [-m] [max_port] [-a] [serv_address] [-n] -threaqd_number]\n");
      exit(1);
   }

   //解析命令行参数
   for(i=1;i<argc;i++)
   {
      if(strcmp("-m",argv[i]) == 0)
      {
         max_port = atoi(argv[i+1]);//将字符串转化为对应的整数
         if(max_port < 0||max_port >65535)
         {
            printf("Usage:invalid max dest port\n");
            exit(1);
         }
         continue;
      }

      if(strcmp("-a",argv[i]) == 0)
      {
         if(inet_aton(argv[i+1],&dest_ip) == 0)
         {
            printf("Usage:invalid dest ip address\n");
            exit(1);
         }
         continue;
      }

      if(strcmp("-n",argv[i]) == 0)
      {
         thread_num = atoi(argv[i+1]);
         if(thread_num <= 0)
         {
            printf("Usage:invalid thread_number\n");
            exit(1);
         }
         continue;
      }
   }

   //如果输入的最大端口号小于进程数，则将进程数设为最大端口号
   if(max_port < thread_num)
      thread_num = max_port;

   seg_len = max_port / thread_num;
   if( (max_port%thread_num) !=0 )
      thread_num +=1;

   //分配存储所有线程ID的内存空间
   thread = (pthread_t *)malloc(thread_num*sizeof(pthread_t));

   //创建线程，根据最大端口号和线程数分配每个线程扫描的端口区间
   for(i=0;i<thread_num;i++)
   {
      _port_segment  portinfo;
      portinfo.dest_ip = dest_ip;
      portinfo.min_port = i*seg_len + 1;
      if( i == thread_num -1)
         portinfo.max_port = max_port;
      else
         portinfo.max_port = portinfo.min_port + seg_len -1;

      //创建线程
      if(pthread_create(&thread[i],NULL,scaner,(void*)&portinfo) != 0)
         my_err("pthread_create",__LINE__);

      //主线程等待子线程结束
      pthread_join(thread[i],NULL);
   }
   return 0;
}
```

​	程序首先从命令行中解析参数：目标IP、最大端口号(程序将扫描1到最大端口号的所有端口)、最大线程数，然后将端口号根据线程数均分并创建多个线程。每个线程首先根据结构portinfo中的信息填充地址结构serv_addr，然后多次调用实际执行扫描的函数do_scan，在该函数内先创建一个TCP套接字，然后调用connect函数测试目标端口，若目标端口打开，则打印出提示信息。

​	编译并执行程序(需要连接pthread库)，结果如下：

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/scan.png)

​	作为验证，可以使用netstat -a命令显示本机打开的TCP端口。

**注：**在运行本端口扫描程序前，最好先关闭防火墙。因为有的防火墙禁止以这种方式对主机进行探测。

​	当向目的主机未打开的UDP端口发送UDP数据时，目标设备会返回一个"ICMP端口不可到达"的错误报文，且函数sendto将出错返回并将错误代码设置为ECONNREFUSED。利用这一点可以进行UDP端口扫描。























































## 实例练习

### 

### 信号

#### 用alarm()和pause()实现sleep：

```c
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <signal.h>

void isr(int n)
{

}

unsigned mysleep(int nSeconds)
{
    unsigned a;
    signal(SIGALRM,isr);
    alarm(nSeconds); //nSeconds秒之后发送SIGALRM信号
    pause(); //等待isr函数结束
    return a;
}


int main()
{
    int nCount ;
    scanf("%d",&nCount);

    
    printf("\n%u\n",mysleep(nCount)) ;   
    

    return 0;
}
```

​	pause函数在收到信号后唤醒，但不一定是SIGALRM信号，也有可能是其他信号，而且如果在pause执行时，进程被切出去，SIGALRM抵达后才切回来就会造成pause一直等待信号

#### 用alarm()、sigprocmask()和sigsuspend()可靠的实现sleep函数

```c
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <signal.h>

void myhandler(int sig)
{

}

int mysleep(size_t w_time)
{
    struct sigaction act,oldact;
    sigset_t newsig,oldsig,susig;//创建block
    act.sa_handler = myhandler;
    sigemptyset(&act.sa_mask);
    act.sa_flags = 0;
    sigemptyset(&newsig);//清空
    sigaddset(&newsig,SIGALRM);//屏蔽字添加SIGALRM
    sigprocmask(SIG_BLOCK,&newsig,&oldsig);//设置屏蔽字
    sigaction(SIGALRM,&act,&oldact);//定义捕捉信号
    alarm(w_time);
    susig = oldsig;
    sigdelset(&susig,SIGALRM);//解除对SIGALRM的屏蔽
    sigsuspend(&susig);//挂起等待
    int ret = alarm(0);
    sigprocmask(SIG_BLOCK,NULL,&oldsig);
    return 0;
}

int main()
{
    unsigned long time;
    scanf("%lu",&time);
    printf("wake up");
    mysleep(time);
    return 0;
}
```

#### 实现raise函数

```c
//单线程
kill(getpid(),sig);
//多线程
pthread_kill(pthread_self(),sig);
```

#### sigsetjmp()/siglongjmp()和setjmp()/longjmp使用场合区别

​	信号处理时会自动阻塞正在被处理的信号，在信号处理函数返回时把进程的信号屏蔽字修复，即解除对当前信号的阻塞。但用longjmp直接跳转就不会使信号屏蔽字修复，该操作会忽略接下来的相同信号，而在env中保存进程的当前信号屏蔽字，在调用siglongjmp时会从其中恢复保存的信号屏蔽字。

​	sigsetjmp()/siglongjmp()更适合在信号处理程序中进行非局部转移时使用

#### 有没有可能在进程间传递指针

指针只在一个进程内有效，**进程内的地址是虚拟地址，被不同进程映射到的物理地址不同**。

```c
//发送信号
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char ** argv)
{
 union sigval value;
 int  signum = SIGTERM; // 默认发送SIGTERM
 pid_t  pid;
 int  i;

 /*初始化*/
 value.sival_int = 0; 

 /*检查参数的个数*/
 if (argc != 3 && argc != 5 && argc != 7) 
 {
    printf("./a <-d data> <-s signum> [-p][data]\n");
    exit(1);
 }

 /*从命令行参数解析出信号编号、PID以及待传递的数据*/
 for (i=1; i<argc; i++) 
 {
    if (!strcmp(argv[i], "-d"))
    {
        value.sival_ptr = argv[i+1];  //指针指向参数中的字符串
        continue;
    }
    if (!strcmp(argv[i], "-s")) 
    {
        signum = atoi(argv[i+1]);
        continue;
     }
  if (!strcmp(argv[i], "-p"))  
    {
        pid = atoi(argv[i+1]);
        continue;
    }
}

 /*利用sigqueue给pid发送信号signum，并携带数据value*/
 if (sigqueue(pid, signum, value) < 0) 
 {
    perror("sigqueue");
    exit(1);
 }

 return 0;
}
```

```c
//处理信号，测试传递的指针有没有用
#include <signal.h>
#include <stdio.h>

/*三参数的信号处理程序*/
void handler_sigint(int signo, siginfo_t *siginfo, void * pvoid)
{
	printf("recv SIGINT, the data value is:%s\n",(char *) siginfo->si_ptr);
}

int main()
{
	struct sigaction act;
 
 /*赋值act结构*/
	act.sa_sigaction = handler_sigint;
	act.sa_flags = SA_SIGINFO;  // 指定使用三参数的信号处理函数
 /*安装信号处理函数*/
	sigaction(SIGINT, &act, NULL);
 
	while(1)
  	;

	return 0;
}
```

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/pointer1.png)

![](https://isotope.oss-cn-hangzhou.aliyuncs.com/note_pic/%E7%AC%94%E8%AE%B0/Linux%20C/potiner 2.png)

可以看见，出现了段错误，该指针访问的内存超出了系统所给的这个程序的内存空间。



