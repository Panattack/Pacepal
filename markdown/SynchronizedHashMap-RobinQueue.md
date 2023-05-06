# SynchronizedHashMap and RobinQueue

## *SynchronizedHashMap*
> This class is mainly being used by the Master, as a HashMap collection. It's only difference is that it's methods are `synchronized`, meaning that one thread at a time has the ability to use the object.
Master only has one static instance of SynchronizedHashMap and the threads must control this object carefully.
Also it is `generic`, and that is available for all types, classes and combinations.

- The variables are : 
    - Map

- It also contains all the following methods
    - Contructor
    - put
    - get
    - containsKey
    - size
    - isEmpty
    - clear
    - entrySet
---
## *RobinQueue*
> This class is mainly being used by the Master, as a circular FIFO. Also it is `generic` and has `synchronized` methods.It main use is the round robin task distribution in Master.

- The variables are :
    - maxSize -> It is used for modulo (%) to implement the circular order.
    - index -> It shows the next available node according to circular order.
    - queue -> A linkedList to store the nodes.

- It also contains the following methods :
    - Contructor -> initializes all variables.
    - getIndex
    - add -> adds one new element and increases maxSize.
    - is_Empty
    - get -> gets the node that is pointed from index (index % maxSize) and increases index by one.
    - size