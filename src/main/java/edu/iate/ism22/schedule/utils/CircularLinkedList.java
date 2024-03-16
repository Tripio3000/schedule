package edu.iate.ism22.schedule.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CircularLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;
    private final Random rand;
    
    public CircularLinkedList(Random rand) {
        this.rand = rand;
        this.size = 0;
    }
    
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        
        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        tail.next = head;
        size++;
    }
    
    public List<T> getList() {
        if (head == null) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>();
        int randomInt = rand.nextInt(size);
        Node<T> start = head;
        for (int i = 0; i < randomInt; i++) {
            start = start.next;
        }
        Node<T> current = start;
        do {
            result.add(current.data);
            current = current.next;
        } while (current != start);
        
        return result;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    private static class Node<T> {
        T data;
        Node<T> next;
        
        public Node(T data) {
            this.data = data;
        }
    }
}
