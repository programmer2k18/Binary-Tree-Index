package BinaryTree;

import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int key=0,offset=0,choice=0;
		BinaryTreeIndex Tree=new BinaryTreeIndex();
		Tree.CreateRecordsFile("index.txt");
		
		while(true) {
			System.out.println("\nPlease Enter Your Choice ?\n"
					+ "1- Insert a new record\n"+"2- Search for a record\n"
					+"3- Display Keys InOrder Method\n"+
					"4- Display data normally\n"+
					"5- Exit");
			choice=new Scanner(System.in).nextInt();
			if(choice==1) {
				System.out.println("Enter key!!");
				key=new Scanner(System.in).nextInt();
				System.out.println("Enter offset!!");
				offset=new Scanner(System.in).nextInt();
				Tree.InsertNewRecordAtIndex("index.txt", key, offset);
			}
			else if(choice==2) {
				System.out.println("Enter key!!");
				key=new Scanner(System.in).nextInt();
				Tree.SearchRecordInIndex("index.txt", key);
			}
			else if(choice==3) {
				Tree.TraverseBinarySearchTreeInOrder("index.txt", 1, false);
				Tree.indexFile.close();
			}
			else if(choice==4) {
				Tree.DisplayIndexFileContent("index.txt");
			}
			else if(choice==5)
				break;
			else
				System.out.println("Please enter a valid choice number!!");
		}
		
	}
}
