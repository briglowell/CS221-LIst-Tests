import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author Brig Lowell
 * 
 * @param <T> type to store
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
	private LinearNode<T> head, tail;
	private int size;
	private int modCount;
	
	/** Creates an empty list */
	public IUSingleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		LinearNode<T> newNode = new LinearNode<T>(element);
		if(isEmpty()) {
			head = newNode;
			tail = newNode;
			tail.setNext(null);
		}else {
			newNode.setNext(head);
			head = newNode;
		}
		size ++;
		modCount++;
	}

	@Override
	public void addToRear(T element) {
		LinearNode<T> newNode = new LinearNode<T>(element); 
		if (!isEmpty()) {
			tail.setNext(newNode);
		}else {
			head = newNode;
		}
		tail = newNode;
		tail.setNext(null);
		size++;
		modCount++;
	}

	@Override
	public void add(T element) {
		addToRear(element);
	}

	@Override
	public void addAfter(T element, T target) {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		boolean found = false;
		LinearNode<T> newNode = new LinearNode<T>(element);
		LinearNode<T> cur = head;
		
		while (cur != null && !found) {
			if (target.equals(cur.getElement())) {
				found = true;
				newNode.setNext(cur.getNext());
				cur.setNext(newNode);
			} else {
				cur = cur.getNext();
			}
		}
		if (!found) {
			throw new NoSuchElementException();
		}
		if(size == 1) {
			tail = newNode;
		}
		size++;
		modCount++;
	}

	@Override
	public void add(int index, T element) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		LinearNode<T> cur = head;
		LinearNode<T> newNode = new LinearNode<T>(element);
		if(!isEmpty()) {
			for (int i = 0; i < index-1; i++) {
				cur = cur.getNext();
			}
			if(index == 0) {
				newNode.setNext(head);
				head = newNode;
			}else {
				newNode.setNext(cur.getNext());
				cur.setNext(newNode);
				if(index == size) {
					tail = newNode;
				}
			}
		}else {
			head = newNode;
			tail = newNode;
			tail.setNext(null);
		}
		size++;
		modCount++;
	}

	@Override
	public T removeFirst() {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		T retVal = head.getElement();
		head = head.getNext();
		if(size==1) {
			tail=null;
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		Iterator<T> it = iterator();
		T retVal = tail.getElement();
		while(it.hasNext()) {
			it.next();
			}
		it.remove();
		return retVal;
	}

	@Override
	public T remove(T element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		boolean found = false;
		LinearNode<T> previous = null;
		LinearNode<T> current = head;
		
		while (current != null && !found) {
			if (element.equals(current.getElement())) {
				found = true;
			} else {
				previous = current;
				current = current.getNext();
			}
		}
		
		if (!found) {
			throw new NoSuchElementException();
		}
		
		if (size() == 1) { //only node
			head = tail = null;
		} else if (current == head) { //first node
			head = current.getNext();
		} else if (current == tail) { //last node
			tail = previous;
			tail.setNext(null);
		} else { //somewhere in the middle
			previous.setNext(current.getNext());
		}
		
		size--;
		modCount++;
		
		return current.getElement();
	}

	@Override
	public T remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		Iterator<T> it = iterator();
		int candidateIndex = 0;
		while(it.hasNext() && candidateIndex < index) {
			it.next();
			candidateIndex++;
		}
		T retVal = it.next();
		it.remove();
		return retVal;
	}

	@Override
	public void set(int index, T element) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		if (!isEmpty()) {
			LinearNode<T> cur = head;
			for(int i = 0; i < index; i++) {
				cur = cur.getNext();
			}
			cur.setElement(element);
			modCount++;
		}
		
	}

	@Override
	public T get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		LinearNode<T> cur = head;
		for (int i = 0; i < index; i++) {
			cur = cur.getNext();
		}
		return cur.getElement();
	}

	@Override
	public int indexOf(T element) {
		Iterator<T> it = iterator();
		int candidateIndex = 0;
		int index = -1;
		while(it.hasNext() && index<0) {
			if(it.next().equals(element)) {
				index = candidateIndex;
			}
			candidateIndex++;
		}
		return index;
	}

	@Override
	public T first() {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		return head.getElement();
	}

	@Override
	public T last() {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		return tail.getElement();
	}

	@Override
	public boolean contains(T target) {
		return (indexOf(target) != -1);
	}

	@Override
	public boolean isEmpty() {
		return (size==0);
	}

	@Override
	public int size() {
		return size;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("[");
		LinearNode<T> newNode = head;
		while(newNode != null) {
			str.append(newNode.getElement().toString());
			newNode = newNode.getNext();
			str.append(", ");
		}
		if(!isEmpty()) {
			str.delete(str.length()-2, str.length());
		}
		str.append("]");
		return str.toString();
	}

	@Override
	public Iterator<T> iterator() {
		return new SLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<T> {
		private LinearNode<T> nextNode;
		private int iterModCount;
		private boolean canRemove;
		
		/** Creates a new iterator for the list */
		public SLLIterator() {
			nextNode = head;
			iterModCount = modCount;
			canRemove = false;
		}

		@Override
		public boolean hasNext() {
			if(iterModCount!= modCount) {
				throw new ConcurrentModificationException();
			}
			return(nextNode != null);
		}

		@Override
		public T next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			T retVal = nextNode.getElement();
			nextNode = nextNode.getNext();
			canRemove = true;
			return retVal;
		}
		
		@Override
		public void remove() {
			if(iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if(!canRemove) {
				throw new IllegalStateException();
			}
			canRemove = false;
			if(size() == 1) {
				head = tail = null;
			}else {
				if(nextNode == head.getNext()){
					head = head.getNext();
				}else {
					LinearNode<T> cur = head;
					while (cur.getNext().getNext() != nextNode) {
						cur = cur.getNext();
					}
					if(nextNode == null) {
						tail = cur;
					}
					cur.setNext(nextNode);
				}
			}
			modCount++;
			iterModCount++;
			size--;
		}
	}
}