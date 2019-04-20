package BinaryTree;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.Stack;

public class BinaryTreeIndex {

	RandomAccessFile indexFile;
	public BinaryTreeIndex(){}
	//Create record file
	public void CreateRecordsFile (String filename) throws IOException {
		indexFile=new RandomAccessFile(filename,"rw");
		if(indexFile.length()==0) {
			int numberOfRecords=0;
			System.out.println("Enter Number of records in the file");
			numberOfRecords=new Scanner(System.in).nextInt();
			
			for(int i=1; i<=numberOfRecords;i++) {
				if(i==numberOfRecords) {
					indexFile.writeInt(-1);
					indexFile.writeInt(0);
					indexFile.writeInt(0);
					indexFile.writeInt(0);
					break;
				}
				indexFile.writeInt(i);
				indexFile.writeInt(0);
				indexFile.writeInt(0);
				indexFile.writeInt(0);
			}	
		}
		indexFile.close();
	}
	
	//Displaying method
	public void DisplayIndexFileContent(String filename) throws IOException {
		indexFile=new RandomAccessFile(filename,"rw");
		while(indexFile.getFilePointer()!=indexFile.length()) {
			System.out.println(indexFile.readInt()+" "+indexFile.readInt()+" "+
			indexFile.readInt()+" "+indexFile.readInt());
		}
		System.out.println();
		indexFile.close();
	}
	
	//Insertion method
	public void InsertNewRecordAtIndex (String filename, int Key, int ByteOffset) throws IOException {
		indexFile=new RandomAccessFile(filename,"rw");
		int currentEmptyNode=indexFile.readInt();
		int nextEmptyNode=0;
		if(currentEmptyNode!=-1) {
			//root node 
			if(currentEmptyNode==1) {
				indexFile.seek(currentEmptyNode*16);
				nextEmptyNode=indexFile.readInt();
				indexFile.seek(currentEmptyNode*16);
				indexFile.writeInt(Key);
				indexFile.writeInt(ByteOffset);
				indexFile.writeInt(-1);
				indexFile.writeInt(-1);
				
				//update next empty node
				indexFile.seek(0);
				indexFile.writeInt(nextEmptyNode);
				indexFile.close();
				System.out.println("Inserted successfully for root");
				return;
			}
			//write the new record to the index file
			indexFile.seek(currentEmptyNode*16);
			nextEmptyNode=indexFile.readInt();
			indexFile.seek(currentEmptyNode*16);
			indexFile.writeInt(Key);
			indexFile.writeInt(ByteOffset);
			indexFile.writeInt(-1);
			indexFile.writeInt(-1);
			
			//update next empty node
			indexFile.seek(0);
			indexFile.writeInt(nextEmptyNode);
			indexFile.seek(16);//begins with root
			while(true) {
				int existkey=indexFile.readInt();
				if(Key>existkey) { //go right
					indexFile.skipBytes(8);
					int right=indexFile.readInt();
					if(right==-1) {
						indexFile.seek(indexFile.getFilePointer()-4);
						indexFile.writeInt(currentEmptyNode);
						indexFile.close();
						System.out.println("Inserted successfully");
						break;
					}
					else
					{
						indexFile.seek(right*16);
					}
				}
				else if(Key<existkey)
				{
					indexFile.skipBytes(4);
					int left=indexFile.readInt();
					if(left==-1) {
						indexFile.seek(indexFile.getFilePointer()-4);
						indexFile.writeInt(currentEmptyNode);
						indexFile.close();
						System.out.println("Inserted successfully");
						break;
					}
					else
					{
						indexFile.seek(left*16);
					}
				}
				//this id is already exists
				else {
					System.out.println("Sorry, This Id is already exists");
					break;
				}
			}
		}
		//no place to insert a new record 
		else
		{
			System.out.println("Sorry, no place to insert this new record");
			indexFile.close();
		}
	}
	
	//search method
	public void SearchRecordInIndex (String filename, int Key) throws IOException {
		indexFile=new RandomAccessFile(filename,"rw");
		if(indexFile.readInt()==1) {
			System.out.println("\nThere are no records to search for\n");
			indexFile.close();
			return;
		}
		indexFile.seek(16);
		while(true) {
			int existkey=indexFile.readInt();
			if(Key>existkey) { //go right
				indexFile.skipBytes(8);
				int right=indexFile.readInt();
				if(right==-1) {
					indexFile.close();
					System.out.println("\nThis id is not Exist");
					break;
				}
				else
					indexFile.seek(right*16);
			}
			else if(Key<existkey)
			{
				indexFile.skipBytes(4);
				int left=indexFile.readInt();
				if(left==-1) {
					indexFile.close();
					System.out.println("\nThis id is not Exist");
					break;
				}
				else
					indexFile.seek(left*16);
			}
			//this id is  existed
			else {
				System.out.println("\nthis id is found");
				System.out.println(existkey+" "+indexFile.readInt()+" "+
						indexFile.readInt()+" "+indexFile.readInt()+"\n");
				indexFile.close();
				break;
			}
		}
	}
	
	//InOrder Method
	public void TraverseBinarySearchTreeInOrder(String filename,  int pointer,boolean isOpened) throws IOException {
		
		if(!isOpened) {
			this.indexFile=new RandomAccessFile(filename,"rw");
			isOpened=true;
		}
		if(pointer ==-1) 
			return;
		
		if(pointer!=-1) {
			
			indexFile.seek(pointer*16);
			int key=indexFile.readInt();
			indexFile.skipBytes(4);
			int left=indexFile.readInt();
			int right=indexFile.readInt();
			TraverseBinarySearchTreeInOrder(filename,left,isOpened);
			System.out.println(key);
			TraverseBinarySearchTreeInOrder(filename,right,isOpened);	
		}
	}	
}
