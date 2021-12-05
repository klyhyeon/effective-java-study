## item09_try-finally보다는_try-with-resources를_사용하라

자바 라이브러리에선 리소스 유출을 막기위해 close 메서드를 호출해 직접 닫아줘야 하는 자원이 많습니다. `InputStream`, `OutputStream`, `java.sql.Connection`(자원닫기)

전통적으로 try-finally를 사용하고 있습니다. 

![image](https://user-images.githubusercontent.com/61368705/144549992-ebf522db-104d-46cd-830f-b8f51ffb873e.png)


```java

static String firstLineOfFile(String path) throws IOException{
    BufferedReader br=new BufferedReader(new FileReader(path));
    try{
        return br.readLine();
    }finally{
        br.close();
    }
}
        
static String firstLineOfFile(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        return br.readLine();
    }
}
```

` Any object that implements java.lang.AutoCloseable, which includes all objects which implement java.io.Closeable, can be used as a resource.`
AutoCloseable을 상속하는 객체와 Closeable을 상속하는 객체는 try-with-resources 자원으로 쓸 수 있습니다. (e.g BufferedReader)

```java
//try, finally에서 exception이 발생하면 try(readLine)에서 던진 exception은 숨겨집니다(suppressed).
static String readFirstLineFromFileWithFinallyBlock(String path)
                                                     throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(path));
    try {
        return br.readLine();
    } finally {
        br.close();
    }
}

//try-resources를 사용하면 try의 exception이 실행됩니다. 
static String readFirstLineFromFile(String path) throws IOException {
        try (BufferedReader br =
           new BufferedReader(new FileReader(path))) {
                return br.readLine();
        }
}

```


```java
//둘 이상의 리소스가 try-with-resources에 선언되어 있다면 순서대로 close 됩니다.
public static void writeToFileZipFileContents(String zipFileName,
                                           String outputFileName)
                                           throws java.io.IOException {

    java.nio.charset.Charset charset =
         java.nio.charset.StandardCharsets.US_ASCII;
    java.nio.file.Path outputFilePath =
         java.nio.file.Paths.get(outputFileName);

    // Open zip file and create output file with 
    // try-with-resources statement

    try (
        java.util.zip.ZipFile zf =
             new java.util.zip.ZipFile(zipFileName);
        java.io.BufferedWriter writer = 
            java.nio.file.Files.newBufferedWriter(outputFilePath, charset)
    ) {
        // Enumerate each entry
        for (java.util.Enumeration entries =
                                zf.entries(); entries.hasMoreElements();) {
            // Get the entry name and write it to the output file
            String newLine = System.getProperty("line.separator");
            String zipEntryName =
                 ((java.util.zip.ZipEntry)entries.nextElement()).getName() +
                 newLine;
            writer.write(zipEntryName, 0, zipEntryName.length());
        }
    }
}
```


---
참고자료
- https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html



