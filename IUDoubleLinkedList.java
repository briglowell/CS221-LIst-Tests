import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Double-linked node implementation of IndexedUnsortedList.
 * A ListIterator with working remove(), set(), and add() methods are implemented
 * 
 * @author Brig Lowell
 * 
 * @param <T> type to store
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	
	private LinearNode<T> head;
	private LinearNode<T> tail;
	private int size;
	private int modCount;
	
	
	public IUDoubleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
//		ListIterator<T> lit = listIterator();
//		lit.add(element);
		
		LinearNode<T> newNode = new LinearNode<T>(element);
		if(isEmpty()) {
			head = newNode;
			tail = newNode;
			newNode.setNext(null);
//			newNode.setPrev(null);
		}else {
			newNode.setNext(head);
			head.setPrev(newNode);
//			newNode.setPrev(null);
			head = newNode;
		}
		newNode.setPrev(null);
		size ++;
		modCount++;

	}

	@Override
	public void addToRear(T element) {
		LinearNode<T> newNode = new LinearNode<T>(element); 
		if (!isEmpty()) {
			tail.setNext(newNode);
			newNode.setPrev(tail);
		}else {
			head = newNode;;
			newNode.setPrev(null);
		}
		tail = newNode;
		newNode.setNext(null);
		size++;
		modCount++;
	}

	@Override
	public void add(T element) {
		addToRear(element);

	}

	@Override
	public void addAfter(T element, T target) {
//		int candidateIndex = indexOf(target);
//		if(candidateIndex == -1) {
//			throw new NoSuchElementException();
//		}
//		ListIterator<T> lit = listIterator(candidateIndex);
//		lit.next();
//		lit.add(element);
		
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
				newNode.setPrev(cur);
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
		if(index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
//		ListIterator<T> lit = listIterator(index);
//		lit.add(element);
		
		LinearNode<T> cur = head;
		LinearNode<T> newNode = new LinearNode<T>(element);
		if(!isEmpty()) {
			for (int i = 0; i < index-1; i++) {
				cur = cur.getNext();
			}
			if(index == 0) {
				newNode.setNext(head);
				head.setPrev(newNode);
				newNode.setPrev(null);
				head = newNode;
			}else {
				newNode.setNext(cur.getNext());
				newNode.setPrev(cur);
				cur.setNext(newNode);
				if(index == size) {
					tail = newNode;
				}
			}
		}else {
			head = newNode;
			tail = newNode;
			newNode.setNext(null);
			newNode.setPrev(null);
		}
		size++;
		modCount++;

	}

	@Override
	public T removeFirst() {
		ListIterator<T> lit = listIterator();
		lit.next();
		T retVal = head.getElement();
		lit.remove();
		return retVal;
	}

	@Override
	public T removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		LinearNode<T> cur = tail.getPrev();
		T retVal = tail.getElement();
		if(size == 1) {
			head = null;
			tail = null;
		}else {
			cur.setNext(null);
			tail.setPrev(null);
			tail = cur;
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(T element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
//		LinearNode<T> cur = head;
//		while (cur != null && !cur.getElement().equals(element)) {
//			cur = cur.getNext();
//		}
//		if (cur == null) {
//			throw new NoSuchElementException();
//		}
//		if (cur != head) {
//			cur.getPrev().setNext(cur.getNext());
//		} else {
//			head = head.getNext();
//		}
//		if (cur != tail) {
//			cur.getNext().setPrev(cur.getPrev());
//		} else {
//			tail = tail.getPrev();
//		}
//		modCount++;
//		size--;
//		return cur.getElement();
		
		ListIterator<T> lit = listIterator();
		T retVal = null;
		boolean found = false;
		while (lit.hasNext() && !found) {
			retVal = lit.next();
			if(retVal.equals(element)) {
				found = true;
			}
		}
		if (!found) {
			throw new NoSuchElementException();
		}
		lit.remove();
		return retVal;
	}

	@Override
	public T remove(int index) {
		if(index < 0 || index >=size) {
			throw new IndexOutOfBoundsException();
		}
		ListIterator<T> lit = listIterator(index);
		T retVal = lit.next();
		lit.remove();
		return retVal;
	}

	@Override
	public void set(int index, T element) {
		if (index < 0 || index >=size) {
			throw new IndexOutOfBoundsException();
		}
		ListIterator<T> lit = listIterator(index);
			lit.next();
			lit.set(element);
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
		ListIterator<T> lit = listIterator();
		int candidateIndex = 0;
		int index = -1;
		while(lit.hasNext() && index<0) {
			if(lit.next().equals(element)) {
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
		
		return (size == 0);
	}

	@Override
	public int size() {
		return size;
	}
	
	@Override
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
		return new DLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		return new DLLIterator();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new DLLIterator(startingIndex);
	}
	
	/** Iterator for IUDoubleLinkedList */
	private class DLLIterator implements ListIterator<T> {
		private LinearNode<T> nextNode;
		private LinearNode<T> lastReturned;
		private int iterModCount;
		private int nextIndex;
		
		/**
		 * Creates a new ListIterator for the list
		 */
		public DLLIterator() {
			nextNode = head;
			iterModCount = modCount;
			nextIndex = 0;
			lastReturned = null;
		}
		
		/**
		 * Creates a new ListIterator with starting index for the list
		 * @param startingIndex
		 */
		public DLLIterator(int startingIndex) {
			this();
			if (startingIndex < 0 || startingIndex > size) {
				throw new IndexOutOfBoundsException();
			}
			for (int i = 0; i < startingIndex; i ++) {
				next();
			}
			lastReturned = null;
		}
		
		@Override
		public boolean hasNext() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextNode != null);
		}
		
		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			T retVal = nextNode.getElement();
			lastReturned = nextNode;
			nextNode = nextNode.getNext();
			nextIndex ++;
			return retVal;
		}
		
		@Override
		public boolean hasPrevious() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextNode != head);
		}
		
		@Override
		public T previous() {
			
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}
			if (nextNode == null) {
				nextNode = tail;
			}else {
				nextNode = nextNode.getPrev();
			}
			nextIndex --;
			lastReturned = nextNode;
			return lastReturned.getElement();
		}
		
		@Override
		public int nextIndex() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			int candidateIndex = 0;
			if(!hasNext()) {
				candidateIndex = size;
			}else {
				candidateIndex = nextIndex;
			}
			return candidateIndex;
		}
		
		@Override
		public int previousIndex() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			int candidateIndex = -1;
			if(!hasPrevious()) {
				candidateIndex = -1;
			}else {
				candidateIndex = nextIndex -1;
			}
			return candidateIndex;
		}
		
		@Override
		public void remove() {
			
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (lastReturned == null) {
				throw new IllegalStateException();
			}
			if (lastReturned != head) {
				lastReturned.getPrev().setNext(lastReturned.getNext());
			}else {
				head = head.getNext();
			}
			if (lastReturned != tail) {
				lastReturned.getNext().setPrev(lastReturned.getPrev());
			}else {
				tail = tail.getPrev();
			}
			if (nextNode == lastReturned) {
				nextNode = nextNode.getNext();
			}else {
				nextIndex--;
			}
			lastReturned = null;
			modCount++;
			iterModCount++;
			size--;
		}
		
		@Override
		public void set(T element) {
			
			if(iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (lastReturned == null) {
				throw new IllegalStateException();
			}
			lastReturned.setElement(element);
			lastReturned = null;
			modCount++;
			iterModCount++;
			
		}
		
		@Override
		public void add(T element) {
			
			if(iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			LinearNode<T> newNode = new LinearNode<T>(element);
			if(isEmpty()) {
				head = newNode;
				tail = newNode;
				newNode.setNext(null);
				newNode.setPrev(null);
				nextNode = newNode.getNext();
			}else {
				if(nextNode == head) {
					newNode.setNext(head);
					head.setPrev(newNode);
					newNode.setPrev(null);
				}
				else if (nextNode == null) {
					tail.setNext(newNode);
					newNode.setPrev(tail);
					tail = newNode;
					newNode.setNext(null);
				}else {
					nextNode.getPrev().setNext(newNode);
					newNode.setPrev(nextNode.getPrev());
					newNode.setNext(nextNode);
					nextNode.setPrev(newNode);
				}
			}
			lastReturned = null;
			nextIndex++;
			modCount++;
			iterModCount++;
			size++;
		}
	}

}
