# Basic Design Principle #

This is a Batch Framework which is based on Java Concurrent Framework. All the tasks can be divided into three kinds of Task. The Quantifiable Task, the Predictable Task, and the Non-Quantifiable Task. Each Task can be executed by the Java Concurrent Framework Executor. The central controller provides the Executor to execute the Task and all the task can be persisted into a Database via a simple persistence API which can be implemented by JDBC and other persistence methods. Users can also restore the tasks from database after they make any task stop.

Also, a monitor will be provided to observe the status of each task.


# Details #

Add your content here.  Format your content with:
  * Text in **bold** or _italic_
  * Headings, paragraphs, and lists
  * Automatic links to other wiki pages