package cn.oasistech.tracer;


import java.util.TreeMap;

public class Node<K, V> {
	private K key;
	private V value;
	private TreeMap<K, Node<K, V>> nodes = new TreeMap<K, Node<K, V>>();
	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}
	public TreeMap<K, Node<K, V>> getNodes() {
		return nodes;
	}
	public void setNodes(TreeMap<K, Node<K, V>> nodes) {
		this.nodes = nodes;
	}
}
