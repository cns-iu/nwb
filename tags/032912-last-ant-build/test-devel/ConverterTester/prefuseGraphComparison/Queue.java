package prefuseGraphComparison;

import java.util.*; 

public class Queue {

    private Vector queue;


    public Queue ()  {
        queue = new Vector();
    }

    Queue (int initialSize) {
         if (initialSize >= 1) {
             queue = new Vector(initialSize);
         } else {
             queue = new Vector();
         }
     }

    public void enQueue (Object item) {
        queue.addElement(item);
    }

    public Object front () {
        return queue.firstElement();
    }

    public Object deQueue () {
        Object obj = null;

       if (!queue.isEmpty()) {
            obj = front();
           queue.removeElement(obj);
       }

        return obj;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
         return queue.size();
     }

     public int availableRoom() {
         return (queue.capacity() - queue.size());
     }

    // perform a deQueue and convert the result to a string
     public String deQueueString() {
         Object obj = deQueue();
         if (obj != null) {
             return obj.toString();
         } else {
             return  " ";
         }
     }

}