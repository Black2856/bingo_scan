package etrobo_bingo;

import java.io.*;

/*--メモ
 * scan_color_backのエラー発生バグあり;
 * 
 * 
----*/
public class Main {
	static int block_color[] = {1,2,2,3,5,4,4,5,0};
	static int block_max = 2;
	
	static int power_spot[] = {5,4,2,3,1,5,4,2,3};
	
	public static void main(String[] args) throws IOException{
		BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));
		
		Block block = new Block();
		block.set(block_color,block_max,power_spot);
		
		//Integer aaa[] = {3,3,2,2,1};
		//Integer bbb[] = {0,0,0,0,2,0,1,1,1};
		//block.point_calculation(aaa,bbb);
		
		while(true == block.main());
		block.result_list_show();
		block.histogram_show();
	}
}
