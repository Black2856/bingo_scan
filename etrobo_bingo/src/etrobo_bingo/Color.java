package etrobo_bingo;

import java.util.*;

public class Color {
	int color[];
	int color_type[] = new int[5];
	int quantity; //色を区別しない置くブロックの数
	
	Integer[][] pos;//scan_posメソッド用:ブロックサークルに置くブロックの状態

	public void set(final int quantity,final int color[]) {//初期値をセットする
		for(int i=0; i<color.length; i++) {//1:黒 2:赤 3:青 4:緑 5:黄色
			if(color[i] == 0) {
				break;
			}
			color_type[color[i]-1]++;
		}
		
		this.color = color;
		this.quantity = quantity;
	}
	
	public boolean scan_color(final int e) {//位置を走査(1回のみ),全通り走査するとfalseを返す
		int p = 1,t;
		t = scan_color_find(e,0);
		if(true == scan_color_forward(e,t)) {
			return true;
		}else {//繰り上げ処理
			
			while(true) {
				t = scan_color_find(e,p);
				if(t == -1) {//1段目の走査が終わったら次の段を処理
					while(true==scan_color_back(e,1));
					if(pos.length-1 == e){//走査の終点?
						return false;
					}else if(false == scan_color(e+1)){
						return false;
					}
					return true;
				}
				
				while(true==scan_color_back(e,t+1));
				if(true == scan_color_forward(e,t)) {
					return true;
				}else {
					p++;
				}
			}
		}
	}

	private boolean scan_color_forward(int e1,int e2) {//[e1][e2]~[e1][end]を後ろへ1つずらす、成功でtrue
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

	private boolean scan_color_back(int e1,int e2){//[e1][e2]~[e1][end]を前へ1つずらす、成功でtrue
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

	private int scan_color_find(int e1,int num) {//最後からnum番目の要素番号を返す
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

	public void scan_color_reset() {//位置を初期状態にする
		int q = quantity, count = 0;
		for(int i=0; i<color_type.length; i++) {
			if(color_type[i]>0) {
				count++;
			}
		}
		pos = new Integer[count][];
		count = 0;
		
		for(int i=0; i<color_type.length; i++) {
			if(color_type[i] > 0) {
				pos[count] = new Integer[q];
				
				for(int j=0; j<q; j++) {
					if(color_type[i] > j) {
						pos[count][j] = i+1;
					}else {
						pos[count][j] = 0;
					}
				}
				
				q = q - color_type[i];
				count++;
			}
		}
	}

	public void scan_color_show() {
		for(int i=0; i<pos.length; i++) {
			for(int j=0; j<pos[i].length; j++) {
				System.out.print(pos[i][j]+",");
			}
		System.out.println();
		}
	}
}
