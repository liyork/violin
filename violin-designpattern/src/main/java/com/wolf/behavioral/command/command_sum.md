command解耦了invoker和receiver，invoker针对command接口操作。
Invoker class doesn't implement a request directly. 
Invoker refers to the Command interface to perform a request (command.execute()), which makes the Invoker 
independent of how the request is performed. 
The Command1 class implements the Command interface by performing an action on a receiver (receiver1.action1()).
The UML sequence diagram shows the run-time interactions: The Invoker object calls execute() on a Command1 object. 
Command1 calls action1() on a Receiver1 object, which performs the request.

关键是command内部自己可以完成好自己的功能，对于外界仅仅是一个触发机制

users
GUI buttons and menu items 
In Swing and Borland Delphi programming, an Action is a command object. In addition to the ability to 
perform the desired command, an Action may have an associated icon, keyboard shortcut, tooltip text, and so on.
 A toolbar button or menu item component may be completely initialized using only the Action object.
Networking 
It is possible to send whole command objects across the network to be executed on the other machines, for 
example player actions in computer games.
Parallel Processing 
Where the commands are written as tasks to a shared resource and executed by many threads in parallel 
(possibly on remote machines -this variant is often referred to as the Master/Worker pattern)

Thread pools 
A typical, general-purpose thread pool class might have a public addTask() method that adds a work item to 
an internal queue of tasks waiting to be done. It maintains a pool of threads that execute commands from the 
queue. The items in the queue are command objects. Typically these objects implement a common interface such
 as java.lang.Runnable that allows the thread pool to execute the command even though the thread pool class 
 itself was written without any knowledge of the specific tasks for which it would be used.

Transactional behavior 
Similar to undo, a database engine or software installer may keep a list of operations that have been or will 
be performed. Should one of them fail, all others can be reversed or discarded (usually called rollback). For
 example, if two database tables that refer to each other must be updated, and the second update fails, the 
 transaction can be rolled back, so that the first table does not now contain an invalid reference.