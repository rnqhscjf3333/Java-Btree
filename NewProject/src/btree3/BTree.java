package btree3;

import java.util.*;

public class BTree {

	private int T;
	private int T2;
	private Node root;
	private boolean down;

	// Node creation
	public class Node implements Comparable<Node> {
		int d;
		ArrayList<Integer> key = new ArrayList<Integer>();
		ArrayList<Node> child = new ArrayList<Node>();
		Node Parent;// 부모

		@Override
		public int compareTo(Node n) {
			if ((this.key.get(0) < n.key.get(0))) {
				return -1;
			} else if (this.key.get(0) > n.key.get(0)) {
				return 1;
			}
			return 0;
		};
	}

	public BTree(int t) {
		T = t;
		T2 = (t + 1) / 2 - 1;
		root = new Node();
		root.d = 0;
	}

	public void Insert(final int key, Node r) {// 삽인되는곳 조사
		if (down == true) {
			if (!r.child.isEmpty()) {// 자식이 있으면
				int i = 0;
				Boolean p = false;
				for (i = 0; i < r.key.size(); i++) {
					if (key < r.key.get(i)) {
						p = true;
						Insert(key, r.child.get(i));
						break;
					}
				}
				if (p == false) {
					Insert(key, r.child.get(i));// 알맞은 곳으로 가서 또 조사함
				}
			} else {
				down = false;
				if (r.key.size() < T - 1) {// 빈공간 있으면
					System.out.println("빈공간있음");
					r.key.add(key);// 넣음
				}
				else {// 빈공간 없으면
					System.out.println("빈공간없음");
					Insert(key, r);
				}
			}
		} else {// 올라가는 중이면
			if(r.Parent != null)//부모가 있으면
				if (r.Parent.key.size() < T - 1) {// 부모에 자리가 있으면
					System.out.println("부모에 자리있음");
					ArrayList<Integer> list = r.key;
					list.add(key);
					Split(list, r);//부모랑 나눔
				} else {// 자리없으면
					System.out.println("부모에 자리없음");
					ArrayList<Integer> list = r.key;
					list.add(key);
					Split(list, r);//부모랑 나눔
				}
			else {//부모가 없으면
				ArrayList<Integer> list1 = r.key;
				list1.add(key);
				Split(list1, r);// 중간값에 맞춰서 나눠주기
			}
		}
	}
	private void degree(Node r) {
		r.d+=1;
		if(!r.child.isEmpty())
			for(Node i : r.child)
				degree(i);
	}
	private void degree2(Node r) {
		r.d-=1;
		if(!r.child.isEmpty())
			for(Node i : r.child)
				degree2(i);
	}

	private void Split(ArrayList<Integer> list1, Node y) {
		Node s = new Node();
		if(y.Parent != null) {
			s=y.Parent;
		}else {
			System.out.println("부모노드 새로만듦");
			root = s;
			s.d = y.d;
			degree(y);
			
			s.child.add(y);
			y.Parent = s;
		}
		Collections.sort(list1);
		Node z = new Node();//친구노드
		s.key.add(list1.get(T2));
		z.d = y.d;
		z.Parent = s;
		s.child.add(z);

		y.key = new ArrayList<Integer>();
		
		if(!y.child.isEmpty()) {
			y.child.get(y.child.size()-1).Parent=z;
			z.child.add(y.child.get(y.child.size()-1));
			y.child.remove(y.child.get(y.child.size()-1));
		}
		
		for (int i : list1) {
			if (i < list1.get(T2)) {
				y.key.add(i);
			} else if (i > list1.get(T2)) {
				z.key.add(i);
				if(!y.child.isEmpty()) {
					y.child.get(y.child.size()-1).Parent=z;
					z.child.add(0,y.child.get(y.child.size()-1));
					y.child.remove(y.child.get(y.child.size()-1));
					
				}
			}
		}
		Collections.sort(y.Parent.child);
		
		if(y.Parent != null && y.Parent.key.size()>T-1) {//부모의 사이즈도 큼
			
			ArrayList list2 = new ArrayList<Integer>();
			for(int i : y.Parent.key)
				list2.add(i);
			y.Parent.key.remove(y.Parent.key.get(T2));
			Split(list2, y.Parent);// 중간값에 맞춰서 나눠주기
		}
	}

	public void Insert(final int key) {
		down = true;
		Insert(key, root);
	}
	
	public void Del(final int key) {
		Del(key, root);
	}
	
	public void Del(final int key, Node r) {//삭제할거 찾기
		if(r.key.contains(key)) {
			System.out.println("동일한 값 찾음");
			r.key.remove(Integer.valueOf(key));//삭제
			Delete(key,r);
		}else {
			int i=0;
			for(i=0; i<r.key.size(); i++) {
				if(r.key.get(i)>key)
					break;
			}
			Del(key, r.child.get(i));
		}
	}
	public void Delete(final int key, Node r) {//삭제하기
		if(r.key.size()>T2) {//수가 충준할경우
			System.out.println("삭제");
		}else if(r.Parent != null) {//수가 부족한데 형제들에게 빌려오는 경우
			int t = r.Parent.child.indexOf(r);
			if(t != 0 && r.Parent.child.get(t-1) != null && r.Parent.child.get(t-1).key.size()>T2) {
				System.out.println("왼쪽 형제");
				int a = r.Parent.key.get(r.Parent.child.indexOf(r));
				int b = r.Parent.child.get(t-1).key.get(r.Parent.child.size()-1);
				r.key.add(a);
				r.Parent.key.remove(Integer.valueOf(a));
				r.Parent.key.add(b);
				r.Parent.child.get(t-1).key.remove(Integer.valueOf(b));
			}
			else if(t != r.Parent.child.size() && r.Parent.child.get(t+1) != null && r.Parent.child.get(t+1).key.size()>T2) {
				System.out.println("오른쪽 형제");
				int a = r.Parent.key.get(r.Parent.child.indexOf(r));
				int b = r.Parent.child.get(t+1).key.get(0);
				r.key.add(a);
				r.Parent.key.remove(Integer.valueOf(a));
				r.Parent.key.add(b);
				r.Parent.child.get(t+1).key.remove(Integer.valueOf(b));
			}else {
				System.out.println("형제도 부족함");
				Node pnode = r.Parent;
				for(Node i : r.Parent.child) {
					if(i.key.size()>0) {
						r.key.add(i.key.get(0));
						if(!i.child.isEmpty())//지울거에 자식이 있으면 
							for(Node q : i.child)
								r.child.add(q);//추가해줌
					}
					if(i != r)
						i.Parent = null;
				}
				r.key.add(pnode.key.get(0));
				pnode.key.clear();
				pnode.child.clear();
				pnode.child.add(r);
				Collections.sort(r.key);
				Delete(key, pnode);
				if(root.key.isEmpty()) {
					root = root.child.get(0);
					root.Parent = null;
					degree2(root);
				}
			}
		}
	}
	
	public void Search(final int key, Node r) {
		if(r.key.contains(key)) {
			System.out.println(key+"를 찾았습니다.");
		}else {
			int i=0;
			for(i=0; i<r.key.size(); i++) {
				if(r.key.get(i)>key)
					break;
			}
			if(!r.child.isEmpty())
				Search(key, r.child.get(i));
			else
				System.out.println(key+" 를(을) 찾을 수 없습니다.");
		}
	}
	public void Search(final int Key) {
		Search(Key, root);
	}
	

	public void Show() {
		Show(root);
	}

	// Display
	private void Show(Node x) {
		assert (x == null);
		System.out.print("깊이 : " + x.d + " - ");
		for (int i : x.key) {
			if(x.Parent != null)
				if(x.Parent.key.size()>0)
					System.out.print("부모노드 " + x.Parent.key.get(0)+" 의 ");
				else
					System.out.print("비어있는 부모노드의 ");
			System.out.print(i + " ");
		}
		System.out.println();
		if (!x.child.isEmpty()) {
			for (Node i : x.child) {
				Show(i);
			}
		}
	}

	public static void main(String[] args) {
		BTree b = new BTree(3);
		b.Insert(1);
		b.Insert(2);
		b.Insert(3);
		b.Insert(4);
		b.Insert(5);
		b.Insert(6);
		b.Insert(7);
		//b.Del(1);

		b.Show();
		
		b.Search(2);
	}
}