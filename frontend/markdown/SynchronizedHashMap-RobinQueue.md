# SynchronizedHashMap and RobinQueue

## *SynchronizedHashMap*
> This class is mainly being used by the Master, as a HashMap collection. It's only difference is that it's methods are `synchronized`, meaning that one thread at a time has the ability to use the object.
Master only has one static instance of SynchronizedHashMap and the threads must control this object carefully.
Also it is `generic`, and that is available for all types, classes and combinations.

- It contains the following attributes : 
    1. Map

- It also contains all the following methods
    - <a>Contructor</a>
    - <a>put</a>
    - <a>get</a>
    - <a>containsKey</a>
    - <a>size</a>
    - <a>isEmpty</a>
    - <a>clear</a>
    - <a>entrySet</a>
---
## *RobinQueue*
> This class is mainly being used by the Master, as a circular FIFO. Also it is `generic` and has `synchronized` methods.It main use is the round robin task distribution in Master.

- It contains the following attributes :
    1. maxSize -> It is used for modulo (%) to implement the circular order.
    2. index -> It shows the next available node according to circular order.
    3. queue -> A linkedList to store the nodes.

- It also contains the following methods :
    - <a>Contructor</a> -> initializes all variables.
    - <a>getIndex</a>
    - <a>add</a> -> adds one new element and increases maxSize.
    - <a>is_Empty</a>
    - <a>get</a> -> gets the node that is pointed from index (index % maxSize) and increases index by one.
    - <a>size</a>