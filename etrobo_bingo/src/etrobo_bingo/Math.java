package etrobo_bingo;

public class Math {
	public static int nCr(final int n,final int r) {
		int ans = n;
		int tr = r;
		for(int i=1; i<r; i++) {
			ans = ans*(n-i);
		}
		for(int i=1; i<r; i++) {
			tr = tr*(r-i);
		}
		
		return ans = ans / tr;
	}
}
