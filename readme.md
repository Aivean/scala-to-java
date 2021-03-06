Scala To Java
---------

Simple tool written in Scala that reveals the mystery of scala compiler. 
Reads scala code from the StdIn and writes it's decompiled Java version
 to StdOut.
 
 
Usage
---

* Make sure you have Java 1.8 and Maven installed
* Checkout the project
* In project directory invoke `mvn clean package`.
 In target directory `scala-to-java.jar` will be created
* Run application with `java -jar target/scala-to-java.jar --slim`.
Optional `--slim` flag removes some boilerplate from the output
* Type any scala code, for example `println("hello, world")`
and finish with `END` character (`Ctrl-D`)

Download
---

Just in case you don't want to bother yourself with building
the project, I already [have built it for you](https://github.com/Aivean/scala-to-java/releases/download/v1.0.2/scala-to-java.jar).
Please note that *Java 8* is required to run this tool.


Source highlighting and more
---

To improve usage experience, you may want to use external source code 
highlighter (like `pygmentize`) and `pv` as the indicator of transpiling 
process. 

My setup:
```sh
alias scala-to-java='java -jar ~/.scala-to-java.jar --slim | pv -W | pygmentize -f 256 -l java -O style=monokai'
```
<img src="https://cloud.githubusercontent.com/assets/2865203/8760097/b4fe881a-2cbe-11e5-9321-305e16d8ee52.png" 
width="400"/>


Credits
---

Thanks to [Stanley Shyiko](https://github.com/shyiko), who
actually implemented all the magic.
