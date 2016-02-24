Java RMI Demo-- Java RMI 示例
本文给出了一个Java RMI的示例,其中注册过程是使用Registry

本例子包含了共5个文件，分别是HelloClient，Hello，HelloServer，
Configur，config.propertits

首先使用rmic test.rmi.HelloServer命令生成需要的stub及skel两个类文件
然后运行HelloServer服务器 java test.rmi.HelloServer
最后运行HelloClient客户端 java test.rmi.HelloClient
屏幕输出HelloWorld，这样，一个最简单的rmi远程调用成功了