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
		Node Parent;// �θ�

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

	public void Insert(final int key, Node r) {// ���εǴ°� ����
		if (down == true) {
			if (!r.child.isEmpty()) {// �ڽ��� ������
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
					Insert(key, r.child.get(i));// �˸��� ������ ���� �� ������
				}
			} else {
				down = false;
				if (r.key.size() < T - 1) {// ����� ������
					System.out.println("���������");
					r.key.add(key);// ����
				}
				else {// ����� ������
					System.out.println("���������");
					Insert(key, r);
				}
			}
		} else {// �ö󰡴� ���̸�
			if(r.Parent != null)//�θ� ������
				if (r.Parent.key.size() < T - 1) {// �θ� �ڸ��� ������
					System.out.println("�θ� �ڸ�����");
					ArrayList<Integer> list = r.key;
					list.add(key);
					Split(list, r);//�θ�� ����
				} else {// �ڸ�������
					System.out.println("�θ� �ڸ�����");
					ArrayList<Integer> list = r.key;
					list.add(key);
					Split(list, r);//�θ�� ����
				}
			else {//�θ� ������
				ArrayList<Integer> list1 = r.key;
				list1.add(key);
				Split(list1, r);// �߰����� ���缭 �����ֱ�
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
			System.out.println("�θ��� ���θ���");
			root = s;
			s.d = y.d;
			degree(y);
			
			s.child.add(y);
			y.Parent = s;
		}
		Collections.sort(list1);
		Node z = new Node();//ģ�����
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
		
		if(y.Parent != null && y.Parent.key.size()>T-1) {//�θ��� ����� ŭ
			
			ArrayList list2 = new ArrayList<Integer>();
			for(int i : y.Parent.key)
				list2.add(i);
			y.Parent.key.remove(y.Parent.key.get(T2));
			Split(list2, y.Parent);// �߰����� ���缭 �����ֱ�
		}
	}

	public void Insert(final int key) {
		down = true;
		Insert(key, root);
	}
	
	public void Del(final int key) {
		Del(key, root);
	}
	
	public void Del(final int key, Node r) {//�����Ұ� ã��
		if(r.key.contains(key)) {
			System.out.println("������ �� ã��");
			r.key.remove(Integer.valueOf(key));//����
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
	public void Delete(final int key, Node r) {//�����ϱ�
		if(r.key.size()>T2) {//���� �����Ұ��
			System.out.println("����");
		}else if(r.Parent != null) {//���� �����ѵ� �����鿡�� �������� ���
			int t = r.Parent.child.indexOf(r);
			if(t != 0 && r.Parent.child.get(t-1) != null && r.Parent.child.get(t-1).key.size()>T2) {
				System.out.println("���� ����");
				int a = r.Parent.key.get(r.Parent.child.indexOf(r));
				int b = r.Parent.child.get(t-1).key.get(r.Parent.child.size()-1);
				r.key.add(a);
				r.Parent.key.remove(Integer.valueOf(a));
				r.Parent.key.add(b);
				r.Parent.child.get(t-1).key.remove(Integer.valueOf(b));
			}
			else if(t != r.Parent.child.size() && r.Parent.child.get(t+1) != null && r.Parent.child.get(t+1).key.size()>T2) {
				System.out.println("������ ����");
				int a = r.Parent.key.get(r.Parent.child.indexOf(r));
				int b = r.Parent.child.get(t+1).key.get(0);
				r.key.add(a);
				r.Parent.key.remove(Integer.valueOf(a));
				r.Parent.key.add(b);
				r.Parent.child.get(t+1).key.remove(Integer.valueOf(b));
			}else {
				System.out.println("������ ������");
				Node pnode = r.Parent;
				for(Node i : r.Parent.child) {
					if(i.key.size()>0) {
						r.key.add(i.key.get(0));
						if(!i.child.isEmpty())//����ſ� �ڽ��� ������ 
							for(Node q : i.child)
								r.child.add(q);//�߰�����
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
			System.out.println(key+"�� ã�ҽ��ϴ�.");
		}else {
			int i=0;
			for(i=0; i<r.key.size(); i++) {
				if(r.key.get(i)>key)
					break;
			}
			if(!r.child.isEmpty())
				Search(key, r.child.get(i));
			else
				System.out.println(key+" ��(��) ã�� �� �����ϴ�.");
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
		System.out.print("���� : " + x.d + " - ");
		for (int i : x.key) {
			if(x.Parent != null)
				if(x.Parent.key.size()>0)
					System.out.print("�θ��� " + x.Parent.key.get(0)+" �� ");
				else
					System.out.print("����ִ� �θ����� ");
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