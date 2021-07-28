package etrobo_bingo;

import java.util.*;

public class Pos {
	int circle;
	int quantity; //色を区別しない置くブロックの数
	int max;//ブロックサークルに入れる最大数
	Integer[] stack;//1つのブロックサークル入っている状態の個数

	ArrayList<Integer[]> stack_list = new ArrayList<Integer[]>();//scan_stackメソッド用:走査時のstats記録用
	ArrayList<Integer> scan_max_list = new ArrayList<Integer>();//scan_stackメソッド用:走査時のscam_max記録用
	int scan_max;//scan_stackメソッド用:統合最大値
	int a=1,b=1;//scan_stackメソッド用:

	Integer[][] pos;//scan_posメソッド用:ブロックサークルに置くブロックの状態

	public void set(final int quantity,final int max,final int circle) {//初期値をセットする
		stack = new Integer[max];
		for(int i=0; i<stack.length; i++) {
			stack[i] = 0;
		}

		this.max = max;
		this.stack[0] = quantity;
		this.quantity = quantity;
		this.circle = circle;
	}


	public boolean scan_stack() {//スタックを走査(1回のみ),全通り走査するとfalseを返す
		boolean result = true;

		while(true) {
			if(stack_list.size()==0) {
				for(int i=0; i<stack.length; i++) {
					if(stack[i]>0) {
						a = i+1;
					}
				}
				scan_max = max;
			}

			result = scan_stack_integration();

			if(result == false && stack_list.size()==0) {//走査終点
				return false;
			}

			if(true == result) {
				stack_list.add(stack.clone());
				scan_max_list.add(scan_max);
				scan_max = a+b;
				a=1;
				//System.out.println(stack[0]+","+stack[1]+","+stack[2]+","+stack[3]+","+stack[4]+","+stack[5]+","+stack[6]);
				return true;
			}else {
				a = scan_max;
				b=1;
				stack = stack_list.get(stack_list.size()-1);
				scan_max = scan_max_list.get(scan_max_list.size()-1);
				stack_list.remove(stack_list.size()-1);
				scan_max_list.remove(scan_max_list.size()-1);
			}
		}
	}

	private boolean scan_stack_integration() {//走査用統合,できなければfalseを返す
		while(true){
			if(a+b <= scan_max) {
				if(true == integration(a,b)){
					return true;
				}
			}
			b++;
			if(a+b > scan_max || b > max ) {//上限に足したらカウントアップ
				a++;
				b=a;
				if(a+b > scan_max) {//それでも上限なら終了
					a=1;
					b=1;
					return false;
				}
			}
		}
	}

	private boolean integration(final int a,final int b) {//2つの指定された数を統合する
		int amount = 1;
		if(a == b) {
			amount++;
		}
		if(stack[a-1] >= amount && stack[b-1] >= amount) {
			stack[a+b-1]++;
			stack[a-1]--;
			stack[b-1]--;
			return true;
		}
		return false;
	}

	public void disassembly() {//ブロックを全て分解する
		for(int i=0; i<stack.length; i++) {
			stack[i] = 0;
		}
		stack[0] = quantity;
	}


	public boolean scan_pos(final int e) {//位置を走査(1回のみ),全通り走査するとfalseを返す
		int p = 1,t;
		t = scan_pos_find(e,0);
		if(true == scan_pos_forward(e,t)) {
			return true;
		}else {//繰り上げ処理
			
			while(true) {
				t = scan_pos_find(e,p);
				if(t == -1) {//1段目の走査が終わったら次の段を処理
					while(true==scan_pos_back(e,1));
					if(pos.length-1 == e){//走査の終点?
						return false;
					}else if(false == scan_pos(e+1)){
						return false;
					}
					return true;
				}
				
				while(true==scan_pos_back(e,t+1));
				if(true == scan_pos_forward(e,t)) {
					return true;
				}else {
					p++;
				}
			}
		}
	}

	private boolean scan_pos_forward(int e1,int e2) {//[e1][e2]~[e1][end]を後ろへ1つずらす、成功でtrue
		int idx = 0;
		Integer copyary[] = Arrays.copyOfRange(pos[e1], e2, pos[e1].length);//退避
		
		if(copyary[copyary.length-1] == 0) {//最後の要素が0ならずらす
			pos[e1][e2] = 0;
			for(int i=e2+1; i<pos[e1].length; i++) {
				pos[e1][i] = copyary[idx];
				idx++;
			}
			return true;
		}else {
			return false;
		}
	}

	private boolean scan_pos_back(int e1,int e2){//[e1][e2]~[e1][end]を前へ1つずらす、成功でtrue
		int idx1 = 0;
		Integer copyary[] = Arrays.copyOfRange(pos[e1], e2, pos[e1].length);//退避
		while(copyary[idx1] == 0) {
			idx1++;
			e2++;
			if(copyary.length == idx1) {
				return false;
			}
		}
		
		if(pos[e1][e2-1] == 0) {//1つ前の要素が0ならずらす
			for(int i=e2-1; i<pos[e1].length; i++) {
				if(idx1 < copyary.length) {
					pos[e1][i] = copyary[idx1];
				}else {
					pos[e1][i] = 0;
				}
				idx1++;
			}
			return true;
		}else {
			return false;
		}
	}

	private int scan_pos_find(int e1,int num) {//最後からnum番目の要素番号を返す
		int point = -1;

		for(int i=0; i<pos[e1].length; i++) {
			if(pos[e1][i] > 0) {
				point = i;
			}
		}

		while(true) {
			if(num > 0) {
				point--;
				if(point < 0) {
					return -1;
				}
				if(pos[e1][point] > 0) {
					num--;
				}
			}else {
				return point;
			}
		}
	}

	public void scan_pos_reset() {//位置を初期状態にする
		int count = 0;
		int element = circle;

		for(int i=0; i<stack.length; i++) {
			if(stack[i]>0) {
				count++;
			}
		}
		pos = new Integer[count][];
		count = 0;

		for(int i=0; i<stack.length; i++) {
			if(stack[i]>0) {
				pos[count] = new Integer[element];
				for(int j=0; j<pos[count].length; j++) {
					if(j<stack[i]) {
						pos[count][j] = i+1;
					}else {
						pos[count][j] = 0;
					}
				}
				element = element - stack[i];
				count++;
			}
		}
	}

	public void scan_pos_show() {
		for(int i=0; i<pos.length; i++) {
			for(int j=0; j<pos[i].length; j++) {
				System.out.print(pos[i][j]+",");
			}
		System.out.println();
		}
	}
}
