# Description
This tool converts received VMG format SMSs to sent VMG format SMSs.

# Compilation
```mvn clean compile assembly:single```

# Runtime
Run with:
```
java -jar convert-received-to-sent-1.0-SNAPSHOT-jar-with-dependencies.jar
    <source dir of received VMG messages>
    <target dir for sent VMG messages>
```

For example,
```
java -jar convert-received-to-sent-1.0-SNAPSHOT-jar-with-dependencies.jar
    /home/chadi/received/
    /home/chadi/sent/
```

# License
Code released under the MIT license.