package com.wolf.test.netty;

//netty5因为一些更为复杂的特性和没有显著的提高性能已经被放弃了

//nio 在linux上使用的是epoll ，epoll支持在一个进程中打开的FD是操作系统最大文件句柄数，而select模型单进程打开的FD是受限的 select模型默认FD是1024 。
// 操作系统最大文件句柄数跟内存有关，1GB内存的机器上，大概是10万个句柄左右。可以通过cat /proc/sys/fs/file-max 查看