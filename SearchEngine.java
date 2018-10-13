import java.util.*;
import java.io.*;
class MySet<T>
{
	MyLinkedList<T> l1;
	void addElement(T element)
	{
		MyLinkedList<T> ptr;
		for(ptr=l1;ptr!=null && ptr.data!=element ;ptr=ptr.next){}
		try
		{
			if(ptr==null)
			{ 
				l1=new MyLinkedList(element,l1);
			}
			else 
			{
				throw new Exception();
			}
		}
		catch (Exception e)
		{
			System.out.println("already present");
		}
	}
	public Boolean mem(T o)
	{
		MyLinkedList<T> ptr;
		for(ptr=l1;ptr!=null && !ptr.data.equals(o);ptr=ptr.next){}
		if (ptr!=null) 
		{
			return true;	
		}
		else
			return false;
	}
	MySet<T> union(MySet<T> otherSet)
	{
		MySet<T> s=new MySet<T>();
		s.l1=null;
		MyLinkedList<T> ptr1,ptr2;
		for(ptr1=l1;ptr1!=null;ptr1=ptr1.next)
		{
			s.addElement(ptr1.data);
		}
		for(ptr1=otherSet.l1;ptr1!=null;ptr1=ptr1.next)
		{
			int flag=0;
			for(ptr2=l1;ptr2!=null;ptr2=ptr2.next)
			{
				if(ptr1.data==ptr2.data)
					flag=1;
			}
			if(flag==0)
			{
				s.addElement(ptr1.data);
			}
		}
		return s;
	}
	MySet<T> intersection(MySet<T> otherSet)
	{
		MySet<T> s=new MySet<T>();
		s.l1=null;
		MyLinkedList<T> ptr1, ptr2;
		for(ptr1=l1;ptr1!=null;ptr1=ptr1.next)
		{
			for(ptr2=otherSet.l1;ptr2!=null;ptr2=ptr2.next)
			{
				if(ptr1.data==ptr2.data)
				{
					s.addElement(ptr1.data);
					break;
				}
			}
		}
		return s;
	}
}
class MyLinkedList<T>
{
	T data;
	MyLinkedList<T> next;
	MyLinkedList ()
	{
		next=null;
	} 
	MyLinkedList(T element,MyLinkedList<T> n)
	{
		data=element;
		next=n;
	}
}
class Position
{
	public int wordposition;
	public PageEntry page;
	public int connector;
	Position(PageEntry p, int wordIndex,int c)
	{
		wordposition=wordIndex;
		page=p;
		connector=c;
	}
	PageEntry getPageEntry()
	{
		return page;
	}
	int getWordIndex()
	{
		return wordposition;
	}
	int getConnectorIndex()
	{
		return connector;
	}
}
class WordEntry
{
	String st;
	AVLTree tree=new AVLTree();
	MyLinkedList<Position> list=new MyLinkedList<Position>();
	WordEntry(String word)
	{
		st=word;
		list=null;
	}
	void addPosition(Position position)
	{
		MyLinkedList<Position> ptr;
		MyLinkedList<Position> last=null;
		MyLinkedList<Position> x=new MyLinkedList<Position>();
		for(ptr=list;ptr!=null;ptr=ptr.next)
		{last=ptr;}
		if(last==null)
		{
			x.data=position;
			x.next=null;
			list=x;
			ptr=list;
			tree.root.key=position;
		}
		else
		{
			x.data=position;
			x.next=null;
			last.next=x;
			last=x;
			tree.root=tree.insert(tree.root,position);
		}
	}
	void addPositions(MyLinkedList<Position> positions)
	{
		MyLinkedList<Position> ptr;
		MyLinkedList<Position> last=null;
		for(ptr=list;ptr!=null;ptr=ptr.next)
		{
			last=ptr;
		}
		last.next=positions;
		
	}
	MyLinkedList<Position> getAllPositionsForThisWord()
	{
		return list;
	}
}
class PageIndex
{
	MySet<WordEntry> wordenter=new MySet<WordEntry>();
	void addPositionForWord(String str,Position p)
	{
		WordEntry word=new WordEntry(str);
		MyLinkedList<WordEntry> ptr;
		for(ptr=wordenter.l1;ptr!=null;ptr=ptr.next)
		{
			if(ptr.data.st.equals(str))
			{
				break;
			}
		}
		if(ptr==null)
		{
			word.addPosition(p);
			wordenter.addElement(word);
		}
		else
		{
			ptr.data.addPosition(p);
		}
	}
	MyLinkedList<WordEntry> getwordentries()
	{
		return wordenter.l1;
	}
}
class PageEntry
{
	PageIndex result=new PageIndex();
	String str;
	PageEntry(String pageName)
	{	
		str=pageName;
		try
		{
			FileInputStream fstream=new FileInputStream(str);
			Scanner a=new Scanner(fstream);
			String line=null;
			int total=1;
			int connector=1;
			while(a.hasNextLine())
			{
				String ch=a.nextLine();
				ch=ch.toLowerCase();
				if(ch.length()>0)
				{
					int j;
					String[] pieces =ch.split("[\\-\\[\\]\\{\\}\\<\\>\\=\\(\\)\\.\\,\\:\\'\"\\?\\#\\!\\:\\s]+");
					for( j=0;j<pieces.length;j++)
					{
						if(pieces[j].equals("stacks"))
							pieces[j]="stack";
						else if(pieces[j].equals("structures"))
							pieces[j]="structure";
						else if(pieces[j].equals("applications"))
							pieces[j]="application";
						if(pieces[j].equals("a")||pieces[j].equals("an")||pieces[j].equals("the")||pieces[j].equals("they")||pieces[j].equals("these")||pieces[j].equals("this")||pieces[j].equals("for")||pieces[j].equals("is")||pieces[j].equals("are")||pieces[j].equals("was")||pieces[j].equals("of")||pieces[j].equals("or")||pieces[j].equals("and")||pieces[j].equals("does")||pieces[j].equals("will")||pieces[j].equals("whose"))
						{
							total++;
						}
						else
						{
							Position pos=new Position(this,total,connector);
							result.addPositionForWord(pieces[j],pos);
							total++;
							connector++;
						}
					}
				}
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println ("File not found ");
		}	
	}
	PageIndex getPageIndex()
	{
		return result;
	}
	float relev(String word)
	{
		float relevance=0;
		MyLinkedList<WordEntry> ret=result.getwordentries();
		MyLinkedList<WordEntry> pt=ret;
		for(pt=ret;pt!=null;pt=pt.next)
		{
			if(pt.data.st.equals(word))
			{
				MyLinkedList<Position> output=pt.data.getAllPositionsForThisWord();
				MyLinkedList<Position> last=output;
				for(last=output;last!=null;last=last.next)
				{
					if(last.data.getPageEntry().str.equals(str))
					{
						//System.out.print(last.data.getWordIndex()+", ");
						relevance+=1.0/(last.data.getWordIndex()*last.data.getWordIndex());
					}
				}
				break;
			}
		}		
		return relevance;			
	}
	float getRelevanceofPage(String stri[], Boolean doTheseWordsRepresentAPhrase)
	{
		float relevance=0;
		if(doTheseWordsRepresentAPhrase)
		{
			MyLinkedList<WordEntry> newone=new MyLinkedList<WordEntry>();
			MyLinkedList<WordEntry> alt= result.getwordentries();
			MyLinkedList<WordEntry> last=null;
			MyLinkedList<WordEntry> ptr2;
			for(int i=0;i<stri.length;i++)
			{
				for(ptr2=alt;ptr2!=null;ptr2=ptr2.next)
				{
					if(stri[i].equals(ptr2.data.st))
					{
						MyLinkedList<WordEntry> x=new MyLinkedList<WordEntry>();
						if(last==null)
						{
							x.data=ptr2.data;
							//System.out.println(x.data.st);
							x.next=null;
							newone=x;
							last=newone;
							//System.out.println(newone.data.st);
						}
						else
						{
							x.data=ptr2.data;
							//System.out.println(ptr2.data.st);
							x.next=null;
							last.next=x;
							last=x;
						}
					}
				}
			}
			last.next=null;
			MyLinkedList<Position> pos=newone.data.getAllPositionsForThisWord();
			last=newone.next;
			MyLinkedList<Position> pt;
			Boolean var=true;
			for(pt=pos;pt!=null ;pt=pt.next)
			{
				if(pt.data.getPageEntry().str.equals(str))
				{
					int index=pt.data.getConnectorIndex();
					int temp=pt.data.getWordIndex();
					//System.out.println(temp);
					var=true;
					last=newone.next;
					while (var==true && last!=null)
					{
						AVLTree alt_tree=last.data.tree;
						index=index+1;
						var=alt_tree.preOrderfind(alt_tree.root,index);
						last=last.next;
					}
					if(var==true)
					{
						//System.out.println("dgsdg");
						relevance+=1.0/(temp*temp);
					}
				}
			}//System.out.println(relevance);
		}
		else
		{
			for(int i=0;i<stri.length;i++)
			{
				relevance+=relev(stri[i]);
			}

		}
		return relevance;
	}
}
class MyHashTable
{
	MyLinkedList<WordEntry>[] arr = (MyLinkedList<WordEntry>[])new MyLinkedList[100];
	MyHashTable()
	{
		for(int i=0;i<100;i++)
		{
			arr[i]=new MyLinkedList<WordEntry>();
			arr[i].next=null;
		}
	}
	int getHashIndex(String str)
	{
		int hash = str.charAt(0);
		for (int i = 1; i < str.length(); i++) 
		{
		    hash = (str.charAt(i)+hash*31)%100;
		}
		return hash%100;
	}
	void addPositionsForWord(WordEntry w)
	{
		int index=getHashIndex(w.st);
		MyLinkedList<WordEntry> x=new MyLinkedList<WordEntry>();
		//System.out.println(index);
		if(arr[index].next==null)
		{
			x.data=w;
			x.next=null;
			arr[index].next=x;
		}
		else
		{   
			MyLinkedList<WordEntry> ptr=arr[index].next;
			MyLinkedList<WordEntry> last=ptr;
			for(;ptr!=null && !ptr.data.st.equals(w.st);ptr=ptr.next)
			{
				last=ptr;
			}
			if(ptr==null)
			{
				x.data=w;
				x.next=null;
				last.next=x;
			}
			else
			{
				ptr.data.addPositions(w.list);
			}
		}
	}
}
class InvertedPageIndex
{
	MyHashTable a=new MyHashTable();
	void addPage(PageEntry p)
	{
		MyLinkedList<WordEntry> invlist= p.getPageIndex().getwordentries();
		MyLinkedList<WordEntry> ptr;
		
		for(ptr=invlist;ptr!=null;ptr=ptr.next)
		{
			a.addPositionsForWord(ptr.data);
		}
	}
	MySet<PageEntry> getPagesWhichContainWord(String str)
	{
		int index=a.getHashIndex(str);

		MySet<PageEntry> set=new MySet<PageEntry>();
		MyLinkedList<WordEntry> ptr=a.arr[index].next;
		for(;ptr!=null && !ptr.data.st.equals(str) ;ptr=ptr.next)
		{}
		if(ptr!=null)
		{
			MyLinkedList<Position> pos=ptr.data.list;
			set.addElement(pos.data.getPageEntry());
			MyLinkedList<Position> last=pos;
			for(pos=pos.next;pos!=null;pos=pos.next)
			{
				if(!last.data.getPageEntry().str.equals(pos.data.getPageEntry().str))
				set.addElement(pos.data.getPageEntry());
				last=pos;
			}
			return set;
		}
		else
			return  null;	
	}
	MySet<PageEntry> getPagesWhichContainPhrase(String str[])
	{
		MySet<PageEntry> set=new MySet<PageEntry>();
		MySet<PageEntry> answer=new MySet<PageEntry>();
		for(int i=0;i<str.length;i++)
		{
			MySet<PageEntry> result=getPagesWhichContainWord(str[i]);
			if(result!=null)
			{
				if(answer.l1==null)
					answer.l1=result.l1;
				else
				answer=answer.intersection(result);
			}
		}
		if(answer.l1==null)
		{
			set.l1=null;
		}
		else
		{
			MyLinkedList<PageEntry> ptr;
			for(ptr=answer.l1;ptr!=null;ptr=ptr.next)
			{
				MyLinkedList<WordEntry> newone=new MyLinkedList<WordEntry>();
				MyLinkedList<WordEntry> alt= ptr.data.result.getwordentries();
				MyLinkedList<WordEntry> last=null;
				MyLinkedList<WordEntry> ptr2;
				for(int i=0;i<str.length;i++)
				{
					for(ptr2=alt;ptr2!=null;ptr2=ptr2.next)
					{
						if(str[i].equals(ptr2.data.st))
						{
							MyLinkedList<WordEntry> x=new MyLinkedList<WordEntry>();
							if(last==null)
							{
								x.data=ptr2.data;
								x.next=null;
								newone=x;
								last=newone;
							}
							else
							{
								x.data=ptr2.data;
								x.next=null;
								last.next=x;
								last=x;
							}
						}
					}
				}
				last.next=null;
				MyLinkedList<Position> pos=newone.data.getAllPositionsForThisWord();
				last=newone.next;
				MyLinkedList<Position> pt;
				Boolean var=true;
				for(pt=pos;pt!=null ;pt=pt.next)
				{
					if(pt.data.getPageEntry().str.equals(ptr.data.str))
					{
						int index=pt.data.getConnectorIndex();
						var=true;
						last=newone.next;
						while (last!=null && var==true)
						{	
							AVLTree alt_tree=last.data.tree;
							index=index+1;
							var=alt_tree.preOrderfind(alt_tree.root,index);
							last=last.next;
						}
						if(var==true)
						{
							//System.out.println(pt.data.getPageEntry().str);
							set.addElement(pt.data.getPageEntry());
							break;
						}
					}
				}
				
			}
		}
		return set;
	}
}
class SearchResult implements Comparable<SearchResult>
{
	PageEntry page;
	float relevance;
	public SearchResult(PageEntry p,float r)
	{
		page=p;
		relevance=r;
	}
	public PageEntry getPageEntry()
	{
		return page;
	}
	public float getRelevance()
	{
		return relevance;
	}
	public int compareTo(SearchResult otherObject)
	{
		if(relevance>otherObject.relevance)
		{
			return -1;
		}
		else if(relevance<otherObject.relevance)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
}
class MySort
{
	ArrayList<SearchResult> sortThisList(MySet<SearchResult> listOfSortableEntries)
	{
		ArrayList<SearchResult> answer=new ArrayList<SearchResult>();
		MySet<SearchResult> set=listOfSortableEntries;
		MyLinkedList<SearchResult> list=set.l1;
		while(list!=null)
		{
			answer.add(list.data);
			list=list.next;
		}
		for (int i = 0; i < answer.size(); i++)
		{
	    	for (int j = answer.size() - 1; j > i; j--) 
         	{
	            if (answer.get(i).compareTo(answer.get(j))==1)
    	        {
    		        SearchResult tmp = answer.get(i);
	                answer.set(i,answer.get(j)) ;
            	    answer.set(j,tmp);
              	}
	        }
      	}
		//Collections.sort(answer);
		return answer;
	}
}
class Node 
{
    int height;
    Position key;
    Node left, right;
    Node(Position d)
    {
        key = d;
        height = 1;
    }
    Node()
    {
    	left=null;
    	right=null;
    }
}
class AVLTree 
{
    Node root=new Node();
     // function to get height of the tree
    int height(Node N)
    {
        if (N == null) 
        {
            return 0;
        }
        return N.height;
    }
    //maximum of two integers
    int max(int a, int b) 
    {
        return (a > b) ? a : b;
    }
    // right rotate subtree rooted with y
    Node rightRotate(Node y) 
    {
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;
        return x;
    }
     // left rotate subtree rooted with x
    Node leftRotate(Node x) 
    {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;
        return y;
    }
    //Balance factor of node N
    int getBalance(Node N) 
    {
        if (N == null) 
        {
            return 0;
        }
        return height(N.left) - height(N.right);
    }
    Node insert(Node node, Position key) 
    {
        if (node == null) 
        {
            return (new Node(key));
        }
        if (key.getWordIndex() < node.key.getWordIndex()) 
        {
            node.left = insert(node.left, key);
        } else 
        {
            node.right = insert(node.right, key);
        }
        node.height = max(height(node.left), height(node.right)) + 1;
        int balance = getBalance(node);
        if (balance > 1 && key.getWordIndex() < node.left.key.getWordIndex()) 
        {
            return rightRotate(node);
        }
        if (balance < -1 && key.getWordIndex() > node.right.key.getWordIndex()) 
        {
            return leftRotate(node);
        }
        if (balance > 1 && key.getWordIndex() > node.left.key.getWordIndex()) 
        {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && key.getWordIndex() < node.right.key.getWordIndex()) 
        {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }
    Boolean preOrderfind(Node node,int index) 
    {
    	Boolean var=false;
        if (node != null) 
        {
        	if(node.key.getConnectorIndex()==index)
        		var= true;
        	else if(node.key.getConnectorIndex()<index)
        	{
        		var=preOrderfind(node.right,index);
        	}
        	else if(node.key.getConnectorIndex()>index)
            {
            	var=preOrderfind(node.left,index);
            }            
        }
        else
       	{
       		var=false;
       	}
        return var;
    }
}
public class SearchEngine
{
	InvertedPageIndex ans;
	MySet<String> page=new MySet<String>();
	public SearchEngine() 
	{
		ans=new InvertedPageIndex();;
	}
	public void performAction(String actionMessage)
	{
		System.out.println(" ");
		System.out.println(actionMessage);
		System.out.println(" ");
		String[] str = actionMessage.split("\\s+");	
		if(str[0].equals("addPage"))
		{
			if(page.mem(str[1]))
			{
				System.out.println("page already added");
			}
			else
			{
				//System.out.println("page added");
				PageEntry web=new PageEntry(str[1]);
				ans.addPage(web);
				page.addElement(str[1]);
			}
		}
		else if(str[0].equals("queryFindPagesWhichContainWord"))
		{
			MySort answ=new MySort();
			String[] extra=new String[str.length-1];
			str[1]=str[1].toLowerCase();
			if(str[1].equals("stacks"))
				str[1]="stack";
			else if(str[1].equals("structures"))
				str[1]="structure";
			else if(str[1].equals("applications"))
				str[1]="application";
			extra[0]=str[1];
			MySet<PageEntry> result=ans.getPagesWhichContainWord(str[1]);
			MyLinkedList<PageEntry> ptr;
			if(result==null)
			{
				System.out.println("no webpage contain word "+ str[1]);
			}
			else
			{
				MySet<SearchResult> alt=new MySet<SearchResult>();
				MyLinkedList<PageEntry> pages;
				int i=0;
				for(pages=result.l1;pages!=null;pages=pages.next)
				{
					float relev=pages.data.getRelevanceofPage(extra,false);
					SearchResult x=new SearchResult(pages.data,relev);
					alt.addElement(x);
					i++;
				}
				ArrayList<SearchResult> output=answ.sortThisList(alt);
				int l=0;
				while(l<i)
				{
					System.out.print(output.get(l).getPageEntry().str + " ,  ");
					l++;
				}
				System.out.println(" ");
					
			}
			System.out.println("");		 
		}
		else if(str[0].equals("queryFindPositionsOfWordInAPage"))
		{
			str[1]=str[1].toLowerCase();
			if(str[1].equals("stacks"))
				str[1]="stack";
			else if(str[1].equals("structures"))
				str[1]="structure";
			else if(str[1].equals("applications"))
				str[1]="application";
			if(page.mem(str[2]))
			{
				MySet<PageEntry> result=ans.getPagesWhichContainWord(str[1]);
				MyLinkedList<PageEntry> ptr=null;
				if(result!=null)
				{
					for(ptr=result.l1;ptr!=null;ptr=ptr.next)
					{
						if(ptr.data.str.equals(str[2]))
							break;
					}
				}
				if(ptr==null)
				{
					System.out.println("Webpage "+str[2]+" does not contain "+str[1]);
				}
				else
				{
					MyLinkedList<WordEntry> ret=ptr.data.getPageIndex().getwordentries();
					MyLinkedList<WordEntry> pt=ret;
					for(pt=ret;pt!=null;pt=pt.next)
					{
						if(pt.data.st.equals(str[1]))
						{
							MyLinkedList<Position> output=pt.data.getAllPositionsForThisWord();
							MyLinkedList<Position> last=output;
							for(last=output;last!=null;last=last.next)
							{
								if(last.data.getPageEntry().str.equals(str[2]))
								System.out.print(last.data.getWordIndex()+", ");
							}
							System.out.println(" ");
						}
					}
				}
				System.out.println("");	
			}
			else
			{
				System.out.println("webpage "+str[2]+" is not added");
				System.out.println("");	
			}
		}
		else if(str[0].equals("queryFindPagesWhichContainAllWords"))
		{
			MySort answ=new MySort();
			MySet<PageEntry> answer=new MySet<PageEntry>();
			String[] extra=new String[str.length-1];
			for(int i=1;i<str.length;i++)
			{
				str[i]=str[i].toLowerCase();
				if(str[i].equals("stacks"))
					str[i]="stack";
				else if(str[i].equals("structures"))
					str[i]="structure";
				else if(str[i].equals("applications"))
					str[i]="application";
				extra[i-1]=str[i];
				MySet<PageEntry> result=ans.getPagesWhichContainWord(str[i]);
				if(result!=null && result.l1!=null)
				{
					if(answer.l1==null)
						answer.l1=result.l1;
					else
					answer=answer.intersection(result);
				}
				else
					break;
			}
			if(answer.l1==null)
			{
				System.out.println("No webpage contains all the given words");
			}
			else
			{
				MySet<SearchResult> alt=new MySet<SearchResult>();
				MyLinkedList<PageEntry> pages;
				int i=0;
				for(pages=answer.l1;pages!=null;pages=pages.next)
				{
					float relev=pages.data.getRelevanceofPage(extra,false);
					SearchResult x=new SearchResult(pages.data,relev);
					alt.addElement(x);
					i++;
				}
				int l=0;
				ArrayList<SearchResult> output=answ.sortThisList(alt);
				while(l<i)
				{
					System.out.print(output.get(l).getPageEntry().str + " ,  ");
					//System.out.println(output.get(l).relevance);
					l++;
				}
			}
			System.out.println(" ");
		}
		else if(str[0].equals("queryFindPagesWhichContainAnyOfTheseWords"))
		{
			MySort answ=new MySort();
			MySet<PageEntry> answer=new MySet<PageEntry>();
			String[] extra=new String[str.length-1];
			for(int i=1;i<str.length;i++)
			{

				str[i]=str[i].toLowerCase();
				if(str[i].equals("stacks"))
					str[i]="stack";
				else if(str[i].equals("structures"))
					str[i]="structure";
				else if(str[i].equals("applications"))
					str[i]="application";
				extra[i-1]=str[i];
				MySet<PageEntry> result=ans.getPagesWhichContainWord(str[i]);
				MyLinkedList<PageEntry> top;
				if(result!=null && result.l1!=null )
				{
					if(answer.l1==null)
					{
						answer=result;
					}	
					else
					{
						answer=answer.union(result);
					}	
				}	
			}
			if(answer.l1==null)
			{
				System.out.println("None of the webpages contain any of the given words");
			}
			else
			{
				MySet<SearchResult> alt=new MySet<SearchResult>();
				MyLinkedList<PageEntry> pages;
				int i=0;
				for(pages=answer.l1;pages!=null;pages=pages.next)
				{
					float relev=pages.data.getRelevanceofPage(extra,false);
					SearchResult x=new SearchResult(pages.data,relev);
					alt.addElement(x);
					i++;
				}
				ArrayList<SearchResult> output=answ.sortThisList(alt);
				int l=0;
				while(l<i)
				{
					System.out.print(output.get(l).getPageEntry().str + " ,  ");
					//System.out.println(output.get(l).relevance);
					l++;
				}
			}
			System.out.println(" ");
		}
		else if(str[0].equals("queryFindPagesWhichContainPhrase"))
		{
			MySort answ=new MySort();
			MySet<PageEntry> answer=new MySet<PageEntry>();
			String[] extra=new String[str.length-1];
			for(int i=1;i<str.length;i++)
			{
				str[i]=str[i].toLowerCase();
				if(str[i].equals("stacks"))
					str[i]="stack";
				else if(str[i].equals("structures"))
					str[i]="structure";
				else if(str[i].equals("applications"))
					str[i]="application";
				extra[i-1]=str[i];
			}
			MySet<PageEntry> result=ans.getPagesWhichContainPhrase(extra);
			if(result==null || result.l1==null)
			{
				System.out.println("no webpage contains the given phrase");
			}
			else
			{
				MyLinkedList<PageEntry> ptr;
				MySet<SearchResult> alt=new MySet<SearchResult>();
				MyLinkedList<PageEntry> pages;
				int i=0;
				for(pages=result.l1;pages!=null;pages=pages.next)
				{
					float relev=pages.data.getRelevanceofPage(extra,true);
					SearchResult x=new SearchResult(pages.data,relev);
					alt.addElement(x);
					i++;
				}
				int l=0;
				ArrayList<SearchResult> output=answ.sortThisList(alt);
				while(l<i)
				{
					System.out.print(output.get(l).getPageEntry().str + " ,  ");
					//System.out.println(output.get(l).relevance);
					l++;
				}
			}
			System.out.println(" ");
		}
	}
}
