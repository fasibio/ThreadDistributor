# ThreadDistributor
Java Thread Distributor ﻿If you have a list of tasks you can use this framework to distribut it to different Threads. ﻿ ﻿ ﻿ Please tell in which Software you are use this Lib


## How to use: 

You find the Source at bintray.com (javacenter)

Build it to get the Sources... 






##Download


```java
dependencies {
    ⋯
    compile 'de.fasibio.threaddistributor:threaddistributor:1.0'
    ⋯
}
```

##How to Start (simple)


You need to declare the threadspecific class. 
The methode execute will run in an own thread. 
```java
public class MyThreadspecificClass implements ExecuteAble{
    
    public void execute(Worker sender) {
		//What´s to do in the new Thread 
	}
}
```
At next you need an Distributor
```java
EasyDistributor distributor = new EasyDistributor(-1,0); //parameter: maxWorkerNumber, startWorkerNumber
```

You can Add Task to this distributor... 
```java
EasyDistributor distributor = new EasyDistributor(-1,0); //parameter: maxWorkerNumber, startWorkerNumber
Task oneTask = new Task();
oneTask.setObject(new MyThreadspecificClass() );

distributor.addTask(oneTask);
```

If you call distributor.addTask(oneTask) the distributor will look for a free Thread and execute this task in a new threads. 
If maxWorkerNumber not -1 and all worker are in prozess, the task will come in the waitlist and will be execute if an other task is finished. 



Now it´s a good idea to declare a TaskListener (optional)
```java
public class MyTaskListener implements TaskListener{
    public void updateStatus(Status status, Task task) {
    //get information about the task 
    }
}

```

You have to add the Listener to the Task before you add the task to the distributor. 
```java
onesheep.addTaskListener(new MyTaskListener());
```
At https://github.com/fasibio/ThreadDistributor/tree/master/src/test/java/de/fasibio/threaddistributor/example you find an example. 

IMPORTENT: 
By closing the Programm you have to call Distributor#killAllThreads

```java
distributor.killAllThreads();
```
