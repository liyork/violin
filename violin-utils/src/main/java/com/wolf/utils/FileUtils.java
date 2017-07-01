package com.wolf.utils;

import org.apache.commons.lang.ArrayUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/8/14
 * Time: 18:39
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class FileUtils {

    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     */
    public static void readFilePerByte(String fileName) {
        InputStream in = null;
        try {
            // 一次读一个字节
            in = new FileInputStream(fileName);
            int tempByte;
            while((tempByte = in.read()) != -1) {
                System.out.print(tempByte);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFilePerBytes(String fileName) {

        InputStream in = null;
        try {
            // 一次读多个字节
            byte[] tempbytes = new byte[100];
            int byteread = 0;
            in = new FileInputStream(fileName);
            System.out.println("可用字节" + in.available());
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while((byteread = in.read(tempbytes)) != -1) {
                //使用utf-8的原因是，a.txt文件就是utf-8
                System.out.println(new String(tempbytes, 0, byteread, "utf-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     */
    public static void readFilePerChar(String fileName) {
        Reader reader = null;
        try {
            // 一次读一个字符
            reader = new InputStreamReader(new FileInputStream(fileName), "utf-8");
            int tempchar;
            while((tempchar = reader.read()) != -1) {
                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
                // 但如果这两个字符分开显示时，会换两次行。
                // 因此，屏蔽掉\r(或者屏蔽\n)。否则，将会多出很多空行。
                if(((char) tempchar) != '\r') {
                    System.out.print((char) tempchar);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readFilePerChars(String fileName) {
        Reader reader = null;
        try {
            // 一次读多个字符
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(fileName), "utf-8");
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while((charread = reader.read(tempchars)) != -1) {
                // 同样屏蔽掉\r不显示
                if((charread == tempchars.length) && (tempchars[tempchars.length - 1] != '\r')) {
                    System.out.print(tempchars);
                } else {
                    for(int i = 0; i < charread; i++) {
                        if(tempchars[i] != '\r') {
                            System.out.print(tempchars[i]);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static void readFilePerLine(String fileName, int appointLineNum) {
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
            String tempString;
            // 一次读入一行，直到读入null为文件结束
            while((tempString = reader.readLine()) != null) {
                int lineNumber = reader.getLineNumber();
                if(lineNumber >= appointLineNum) {
                    System.out.println("line " + lineNumber + ": " + tempString);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 指定位置读取文件内容
     */
    public static void readFileRandom(String fileName) {
        RandomAccessFile randomFile = null;
        try {
            // 打开一个随机访问文件流，按只读方式
            randomFile = new RandomAccessFile(fileName, "r");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 读文件的起始位置,这里由于a.txt中内容为“中文\r\n123”，
            // 编码为utf-8所以从4个字节开始，0-3是“中文”
            int beginIndex = (fileLength > 4) ? 4 : 0;
            // 将读文件的开始位置移到beginIndex位置，从这个index开始读
            randomFile.seek(beginIndex);
            byte[] bytes = new byte[10];
            int readByteLength = 0;
            // 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
            // 将一次读取的字节数赋给byteread
            while((readByteLength = randomFile.read(bytes)) != -1) {
                System.out.print(new String(bytes, 0, readByteLength, "utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 追加内容：使用RandomAccessFile
     * 内部是：先读文件然后再append，所以读时使用的系统默认编码，
     */
    public static void appendContent2File(String fileName, String content, String targetFileCharSet) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            //目标a.txt文件是utf-8编码
            randomFile.write(content.getBytes(targetFileCharSet));
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 以行为单位写入文件(追加或覆盖)，常用于写入面向行的格式化文件
     */
    public static void writeContent2File(String fileName, String content, boolean isAppend, String targetFileCharSet) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, isAppend), targetFileCharSet));
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void writeFile(String fileName, InputStream inputStream) {

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(fileName);
            // 一次读多个字节
            byte[] tempBytes = new byte[1024];
            int readNum;
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while((readNum = inputStream.read(tempBytes)) != -1) {
                os.write(tempBytes, 0, readNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createTempFile(String prefix, String suffix) {
        File file = null;
        try {
            file = File.createTempFile(prefix, suffix);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(null != file) {
            FileWriter fout = null;
            try {
                fout = new FileWriter(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            PrintWriter out = new PrintWriter(fout);
            out.println("some info!");
            out.close(); //注意：如无此关闭语句，文件将不能删除


            // deletes file when the virtual machine terminate
            file.deleteOnExit();
        }
    }

    /**
     * 拷贝文件
     *
     * @param sourceFile 全路径
     * @param directFile 全路径
     */
    public static void copyFile(File sourceFile, File directFile) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(sourceFile);
            fileOutputStream = new FileOutputStream(directFile);

            byte[] tempArray = new byte[1024];
            int byteCount;
            while((byteCount = fileInputStream.read(tempArray)) != -1) {
                fileOutputStream.write(tempArray, 0, byteCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != fileInputStream) {
                    fileInputStream.close();
                }

                if(null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拷贝指定目录的过滤后的文件，之拷贝一层
     *
     * @param sourceDir 全路径
     * @param directDir 全路径
     */
    public static void copyFile(File sourceDir, File directDir, FileFilter fileFilter) {
        File[] files = sourceDir.listFiles(fileFilter);
        if(ArrayUtils.isEmpty(files)) {
            return;
        }

        for(File file : files) {
            copyFile(file, new File(directDir, file.getName()));
        }
    }



    public static void findReplication() {
        HashMap<String, Integer> map = new HashMap<>();

        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream("D:\\test.txt"), "utf-8"));
            String tempString;
            // 一次读入一行，直到读入null为文件结束
            while((tempString = reader.readLine()) != null) {
                Integer integer = map.get(tempString);
                if(null == integer) {
                    integer = 1;
                } else {
                    integer++;
                }
                map.put(tempString, integer);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for(Map.Entry<String, Integer> entry : map.entrySet()) {
            Integer value = entry.getValue();
            if(value > 2) {
                System.out.println(entry.getKey() + " " + value);
            }
        }
    }

    public static void removeReplication() {
        TreeSet<String> set = new TreeSet<>();

        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream("D:\\test.txt"), "utf-8"));
            String tempString;
            // 一次读入一行，直到读入null为文件结束
            while((tempString = reader.readLine()) != null) {
                set.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for(String s : set) {
            System.out.println(s);
        }
    }


    public static void main(String[] args) throws IOException {
        String fileName = "D:\\a.txt";
//		readFilePerByte(fileName);
//		readFilePerBytes(fileName);
//		readFilePerChar(fileName);
//		readFilePerChars(fileName);
        readFilePerLine(fileName, 1);
//		readFileRandom(fileName);

//		appendContent2File(fileName,"四五","utf-8");
//		writeContent2File(fileName, "123哈123哈123", true, "utf-8");

        createTempFile("tmp", ".txt");


    }

}
