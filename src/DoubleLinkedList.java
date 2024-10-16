public class DoubleLinkedList {
    private NodeForDouble head,tail;

    public DoubleLinkedList() {
        head=null;
        tail=null;
    }

    public void add(Object dataToAdd, String name){
        if(head==null){
            NodeForDouble newNode=new NodeForDouble(dataToAdd, name);
            head=newNode;
            tail=newNode;
        }
        else if ((int)dataToAdd>=(int)head.getData()) {
            NodeForDouble newNode=new NodeForDouble(dataToAdd, name);
            newNode.setNext(head);
            head.setPrev(newNode);
            head=newNode;
        }
        else {
            NodeForDouble newNode=new NodeForDouble(dataToAdd, name);
            NodeForDouble temp=head;
            while (temp.getNext()!=null && (int) dataToAdd<=(int)temp.getNext().getData()){
                temp=temp.getNext();
            }
            newNode.setPrev(temp);
            newNode.setNext(temp.getNext());
            if(temp.getNext()!=null){
                temp.getNext().setPrev(newNode);
            }
            else {
                tail=newNode;
            }
            temp.setNext(newNode);
        }
    }

    public void delete(Object dataToDelete){
        if(head==null){
            System.out.println("the list is empty...");
        }
        else {
            while ((int)dataToDelete==(int)head.getData()){
                head=head.getNext();
                head.setPrev(null);
            }
            while ((int)dataToDelete==(int)tail.getData()){
                tail=tail.getPrev();
                tail.setNext(null);
            }
            NodeForDouble temp=head;
            while (temp!=tail){
                if ((int)dataToDelete==(int)temp.getData()){
                    temp.getPrev().setNext(temp.getNext());
                    temp.getNext().setPrev(temp.getPrev());
                }
                temp=temp.getNext();
            }

        }
    }
    
    public int size(){
        int count = 0;
        NodeForDouble temp = head;
        while(temp != null){
            count++;
            temp = temp.getNext();
        }
        return count;
    }

    
    public void Display(){
        if (head==null){
            System.out.println("The list is empty");
        }
        else {
            NodeForDouble temp=head;
            while (temp!=null){
                System.out.println(temp.getName() + " " + temp.getData());
                temp=temp.getNext();
            }
        }
    }
   
}
