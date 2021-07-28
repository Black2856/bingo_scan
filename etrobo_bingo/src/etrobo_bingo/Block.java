package etrobo_bingo;

import java.util.*;

public class Block {
	Result_str result_str = new Result_str();
	ArrayList <Result_str> result_list = new ArrayList<Result_str>();
	Pos pos = new Pos();
	Color color = new Color();
	
	int histogram[] = new int[100];//0.5秒刻み
	int power_spot[];
	int quantity;
	int count=0;
	
	public void set(final int color[],final int max,final int power_spot[]) {//初期値をセットする
		result_list = new ArrayList<Result_str>();
		
		quantity = 0;
		for(int i=0; i<color.length; i++) {
			if(color[i] != 0) {
				quantity++;
			}
		}
		this.power_spot = power_spot;
		
		this.pos.set(quantity,max,power_spot.length);
		this.color.set(quantity,color);
		
		this.color.scan_color_reset();
		this.pos.scan_pos_reset();
	}
	
	public Integer[] integration(final Integer data[][],final int max) {//分割された二次元配列を一次元配列に統合します
		int skip = 0;
		Integer result[] = new Integer[max];
		Arrays.fill(result, 0);
		
		for(int i=0; i<data.length; i++) {
			for(int j=0; j<data[i].length; j++) {
				if(data[i][j] > 0) {
					int I = 0,zero = 0;
					while(result[I] != 0) {
						I++;
					}
					while(zero < skip) {
						if(result[I+1] == 0) {
							zero++;
						}
						I++;
					}
					result[I] = data[i][j];
				}else {
					skip++;
				}
			}
			skip = 0;
		}
		
		return result;
	}
	
	public boolean main() {//メイン走査//
		result_str = new Result_str();
		result_str.time = 0;
		
		result_str.color_ary = integration(color.pos, color.quantity);
		result_str.pos_ary = integration(pos.pos, pos.circle);
		result_str.enable_count = enable_count(result_str.color_ary,result_str.pos_ary);
		result_str.time = time_calculation(result_str);
		
		result_str_ins(1000,result_str);
		///*
		System.out.print("col :");
		for(int i=0;i<result_str.color_ary.length;i++) {
			System.out.print(result_str.color_ary[i]+",");
		}
		System.out.print(" /pos :");
		for(int i=0;i<result_str.pos_ary.length;i++) {
			System.out.print(result_str.pos_ary[i]+",");
		}

		System.out.print(" /stk :");
		for(int i=0;i<pos.stack.length;i++) {
			System.out.print(pos.stack[i]+",");
		}
		System.out.print(" /"+result_str.time);
		//*/
		count++;
		System.out.println(" /"+count);
		
		if(false == color.scan_color(0)) {
			if(false == pos.scan_pos(0)) {
				if(false == pos.scan_stack()) {
					return false;
				}
				pos.scan_pos_reset();
			}
			color.scan_color_reset();
		}
		
		return true;
	}
	
	public Integer[] enable_count(final Integer color_ary[],final Integer pos_ary[]){//得点計算
		Integer enable_count[] = new Integer[5];//有効数
		/*
		 *	0:ブロックサークル有効移動 0.5
		 *	1:パワースポット有効移動 1
		 *	2:センターブロック有効移動 2
		 *	3:ビンゴ 1
		 *	4:パワースポットビンゴ 2
		 */
		boolean enable_power[] = new boolean[power_spot.length];
		Arrays.fill(enable_count, 0);
		Arrays.fill(enable_power, false);
		int idx = 0;
		
		for(int i=0; i<pos_ary.length; i++) {
			for(int j=0; j<pos_ary[i]; j++) {
				if(power_spot[i] == color_ary[idx]) {//パワースポット有効移動
					enable_power[i] = true;
					if(power_spot[i] == 1) {//センターブロック有効移動
						enable_count[2] += 1;
					}
				}
				idx++;
			}
		}
		
		for(int i=0; i < pos_ary.length; i++) {//ブロック有効移動処理；
			if(pos_ary[i] > 0) {
				if(power_spot[i] != 1) {
					enable_count[0] += pos_ary[i];
				}
				if(enable_power[i] == true) {
					enable_count[1] += pos_ary[i];
				}
			}
		}
		
		for(int i=0; i<3; i++) {//横ビンゴ
			if(pos_ary[i*3] > 0 && pos_ary[i*3 +1] > 0 && pos_ary[i*3 +2] > 0) {
				if(true == enable_power[i*3] && true == enable_power[i*3 +1] && true == enable_power[i*3 +2]) {
					enable_count[3] += 1;
					enable_count[4] += 1;
				}else {
					enable_count[3] += 1;
				}
			}
		}
		for(int i=0; i<3; i++) {//縦ビンゴ
			if(pos_ary[i] > 0 && pos_ary[i +3] > 0 && pos_ary[i +6] > 0) {
				if(true == enable_power[i] && true == enable_power[i +1] && true == enable_power[i +2]) {
					enable_count[3] += 1;
					enable_count[4] += 1;
				}else {
					enable_count[3] += 1;
				}
			}
		}
		for(int i=0; i<2; i++) {//斜めビンゴ
			if(pos_ary[0+i*2] > 0 && pos_ary[4] > 0 && pos_ary[8-i*2] > 0) {
				if(true == enable_power[0+i*2] && true == enable_power[4] && true == enable_power[8-i*2]) {
					enable_count[3] += 1;
					enable_count[4] += 1;
				}else {
					enable_count[3] += 1;
				}
			}
		}		
		return enable_count;
	}
	
	public float time_calculation(final Result_str result_str) {
		float time = 0;
			time += result_str.enable_count[0] * 0.5;
			time += result_str.enable_count[1] * 1;
			time += result_str.enable_count[2] * 2;
			time += result_str.enable_count[3] * 1;
			time += result_str.enable_count[4] * 2;
			
			histogram[(int)(time*2)]++;
			
		return time;
	}
	
	public void result_str_ins(final int list_max , final Result_str result_str) {//Result_strリスト最後がtime比較し大きい場合リストに追加
		int I = 0;
		if(result_list.size() > 0) {
			while(result_str.time < result_list.get(I).time) {
				I++;
				if(result_list.size() == I) {
					result_list.add(I,result_str);
					if(result_list.size() > list_max) {
						result_list.remove(result_list.size()-1);
					}
					return;
				}
			}
			result_list.add(I,result_str);
		}else {
			result_list.add(0,result_str);

		}
		if(result_list.size() > list_max) {
			result_list.remove(result_list.size()-1);
		}
	}
	
	public void result_list_show() {
		for(int i=0; i<result_list.size(); i++) {
			System.out.print(result_list.get(i).time+"s /");
			for(int j=0; j<result_list.get(i).color_ary.length; j++) {
				System.out.print(result_list.get(i).color_ary[j]+",");
			}
			System.out.print(" /");
			for(int j=0; j<result_list.get(i).pos_ary.length; j++) {
				System.out.print(result_list.get(i).pos_ary[j]+",");
			}
			System.out.print(" /");
			for(int j=0; j<result_list.get(i).enable_count.length; j++) {
				System.out.print(result_list.get(i).enable_count[j]+",");
			}
			System.out.println("");
		}
	}
	
	public void histogram_show() {
		for(int i=0; i<histogram.length; i++) {
			System.out.print(histogram[i]+",");
		}
		System.out.println("");
		System.out.print(count+"通りを走査");
	}
}