package com.practice.designs;

import java.util.LinkedList;

class Entry<K,V>{
	K key;
	V value;
	
	Entry<K, V> next;

	public Entry(K key, V value) {
		super();
		this.key = key;
		this.value = value;
		this.next=null;
	}
	
	@Override
	public int hashCode() {
		return key.hashCode();
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		return key.equals((K)obj);
	}
}
public class HashMapImpl<K, V> {
	LinkedList<Entry<K,V>> buckets[];
	int size=10;
	@SuppressWarnings("unchecked")
	public HashMapImpl() {
		super();
		LinkedList<Entry<K, V>>[] bucketList=new LinkedList[size];
		for(int i=0;i<size;i++) {
			LinkedList<Entry<K,V>> list=new LinkedList<Entry<K,V>>();
			bucketList[i]=list;
		}
		this.buckets = bucketList;
	}
	public void put(K key,V value) {
		Entry<K,V> entry=new Entry<K,V>(key,value);
		if(key==null) {
			buckets[0].clear();
			buckets[0].add(entry);
			return;
		}
		int hashcode=entry.hashCode();
		int ind=Math.abs(hashcode%(size-1))+1;
		LinkedList<Entry<K,V>> list=buckets[ind];
		boolean isPresent=false;
		int index=0;
		for(Entry<K,V> listEntry:list) {
			if(listEntry.key==key) {
				isPresent=true;
				break;
			}
			index++;
		}
		if(isPresent) {
			list.set(index, entry);
		}else {
			list.add(entry);
		}
	}
	public V get(K key) {
		if(key==null) {
			return buckets[0].get(0).value;
		}
		Entry<K,V> entry=new Entry<K,V>(key, null);
		int hashcode=entry.hashCode();
		int ind=Math.abs(hashcode%(size-1))+1;
		LinkedList<Entry<K,V>> list=buckets[ind];
		for(Entry<K,V> listEntry:list) {
			if(listEntry.key==key) {
				return listEntry.value;
			}
		}
		return null;
	}
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder("{");
		int index=0;
		for(LinkedList<Entry<K,V>> list: buckets) {
			if(!list.isEmpty()) {
				sb.append("[ "+index+": ");
				for(Entry<K,V> entry:list) {
					sb.append("{ "+entry.key+" : "+entry.value+"},");
				}
				sb.deleteCharAt(sb.length()-1);
				sb.append("],");
			}
			index++;
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("}");
		return sb.toString();
	}
	public static void main(String[] args) {
		HashMapImpl<String, String> myMap=new HashMapImpl<String, String>();
		myMap.put("Ankush", "1");
		myMap.put("Sunandan", "2");
		myMap.put("Ankush", "3");
		myMap.put("Sunandan", "4");
		myMap.put("Test", "5");
		myMap.put(null, "6");
		System.out.println(myMap.toString());
	}
}
