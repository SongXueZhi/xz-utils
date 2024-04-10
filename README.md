# xz-utils
This is ongoing utils for program analysis and evolution analysis. Feel free to suggest more requirements.

## Dependencies
I have chosen [byte-buddy](https://github.com/raphw/byte-buddy) & [ASM](https://asm.ow2.io/) as the technological foundation for **dynamic analysis**, and [Spoon](https://github.com/INRIA/spoon.git) the technological foundation for **static analysis**.

While alternatives such as [BCEL](https://github.com/apache/commons-bcel.git) exist for dynamic analysis, I maintain my current choice due to its ease of use and community support.

[JavaParser](https://github.com/javaparser/javaparser) is equally popular for static analysis, and **xz-utils** currently reserves a slot for it, possibly intending to enhance JavaParser's interface in the future. However, I prefer Spoon for its retrieval and templating mechanisms, which I believe are better suited for complex application scenarios.


## Features
### Tracing Program Execution
The feature is implemented in ``intrumentator`` module. You can use ``mvn clean package`` get jar file, 
and then use following command to trace the program execution.
```
java -javaagent:instrumentator-1.0-SNAPSHOT.jar=packageName=core -jar your-jar-file.jar
```

``methodName`` is also a parameter, you can specify the method you want to trace. It is optional, if you don't specify it, all methods in
 package will be traced. 
```
output example:
```bash
java -javaagent:instrumentator-1.0-SNAPSHOT.jar=packageName=core -jar regs4j.jar -checkout 1 -v bic
Entering method: public static boolean core.git.GitUtils.checkout(java.lang.String,java.io.File)
Arguments: [84835dd3cfb46939eb595742ea8d7d74918034bd, /Users/sxz/reg4j/cache_code/1_rfc]
Exiting method: public static boolean core.git.GitUtils.checkout(java.lang.String,java.io.File)
Method execution time: 1473 milliseconds
Return value: true
Entering method: public static boolean core.git.GitUtils.checkout(java.lang.String,java.io.File)
Arguments: [0867549b20c6a70fde8a11b41116034c2e94083b, /Users/sxz/reg4j/cache_code/1_ric]
Exiting method: public static boolean core.git.GitUtils.checkout(java.lang.String,java.io.File)
Method execution time: 276 milliseconds
Return value: true
Entering method: public static java.util.List core.git.GitUtils.getDiffEntriesBetweenCommits(java.io.File,java.lang.String,java.lang.String)
Arguments: [/Users/sxz/reg4j/cache_code/1_rfc, 84835dd3cfb46939eb595742ea8d7d74918034bd, 84835dd3cfb46939eb595742ea8d7d74918034bd~1]
Entering method: public static org.eclipse.jgit.lib.Repository core.git.RepositoryProvider.getRepoFromLocal(java.io.File) throws java.lang.Exception
Arguments: [/Users/sxz/reg4j/cache_code/1_rfc]
Exiting method: public static org.eclipse.jgit.lib.Repository core.git.RepositoryProvider.getRepoFromLocal(java.io.File) throws java.lang.Exception
Method execution time: 1 milliseconds
Return value: Repository[/Users/sxz/reg4j/cache_code/1_rfc/.git]
Entering method: private static org.eclipse.jgit.treewalk.AbstractTreeIterator core.git.GitUtils.prepareTreeParser(org.eclipse.jgit.lib.Repository,java.lang.String) throws java.lang.Exception
Arguments: [Repository[/Users/sxz/reg4j/cache_code/1_rfc/.git], 84835dd3cfb46939eb595742ea8d7d74918034bd~1]
Exiting method: private static org.eclipse.jgit.treewalk.AbstractTreeIterator core.git.GitUtils.prepareTreeParser(org.eclipse.jgit.lib.Repository,java.lang.String) throws java.lang.Exception
Method execution time: 2 milliseconds
Return value: CanonicalTreeParser[.github]
Entering method: private static org.eclipse.jgit.treewalk.AbstractTreeIterator core.git.GitUtils.prepareTreeParser(org.eclipse.jgit.lib.Repository,java.lang.String) throws java.lang.Exception
Arguments: [Repository[/Users/sxz/reg4j/cache_code/1_rfc/.git], 84835dd3cfb46939eb595742ea8d7d74918034bd]
Exiting method: private static org.eclipse.jgit.treewalk.AbstractTreeIterator core.git.GitUtils.prepareTreeParser(org.eclipse.jgit.lib.Repository,java.lang.String) throws java.lang.Exception
Method execution time: 1 milliseconds
Return value: CanonicalTreeParser[.github]
Exiting method: public static java.util.List core.git.GitUtils.getDiffEntriesBetweenCommits(java.io.File,java.lang.String,java.lang.String)
Method execution time: 27 milliseconds
Return value: [DiffEntry[MODIFY src/main/java/com/alibaba/fastjson/parser/deserializer/FieldDeserializer.java], DiffEntry[ADD src/test/java/com/alibaba/json/bvt/parser/deser/list/ListFieldTest.java]]
checkout successful:/Users/sxz/reg4j/cache_code/1_ric
test command:com.alibaba.json.bvt.parser.deser.list.ListFieldTest#test_for_list
```
### Call Graph & Reachability Analysis
#### Call Graph Generation
The feature is implemented in ``static-analysis`` module. Code for using example is as follows:
```java
   StaticAnalyst staticAnalysis = new StaticAnalyst(StaticAnalyst.Core.SPOON);
        staticAnalysis.setLanguageLevel(11);
        Graph<String, DefaultEdge> graph =  staticAnalysis.createCallGraphFrom(
               staticAnalysis.createModel("examples/src/test/java/TestCase1.java"),
               "TestCase1",
               "main"
        );
        printGraph(graph);
```
The output is as follows:
```bash
Node: TestCase1#main(java.lang.String[])
    |
    v
    TestCase1#complexMethod(int)

Node: TestCase1#complexMethod(int)
    |
    v
    TestCase1#fibonacci(int)
    |
    v
    TestCase1#factorial(int)
    |
    v
    TestCase1#multiplyAndIncrement(int,int,int)

Node: TestCase1#fibonacci(int)
    No outgoing edges

Node: TestCase1#factorial(int)
    No outgoing edges

Node: TestCase1#multiplyAndIncrement(int,int,int)
    |
    v
    TestCase1#add(int,int)

Node: TestCase1#add(int,int)
    No outgoing edges
```
#### Reachability Analysis
Code for using example is as follows:
```java
        int distance = staticAnalysis.checkReachabilityAndDistance(
                graph,
                "TestCase1#complexMethod(int)",
                "TestCase1#add(int,int)"
        );
        System.out.println(distance);
```
You can get more exmples in ``examples`` module.