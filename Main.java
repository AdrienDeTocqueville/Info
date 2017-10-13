package tp4;

import java.util.*;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println(maxDistance( new double[][]{{0,0},{0,1},{1,1},{1,0}} ));
		
//		int[] array = generate(10);
//
//		long startTime = System.nanoTime();
//		
//			int[] res = triFusion(array, 0, array.length);
//			
//		long estimatedTime = System.nanoTime() - startTime;
//
//		System.out.println(Arrays.toString(res));
//		System.out.println("Time: " + estimatedTime + " ns");
	}
	
	/// 2.0.1.3
	public static int[] triFusion(int[] tableau, int debut, int fin)
	{
		int taille = fin-debut;
		
		if (taille == 1)
			return new int[]{tableau[debut]};
		
		int moitie = debut + (int) Math.floor(taille / 2);

		return fusion(triFusion(tableau, debut, moitie), triFusion(tableau, moitie, fin));
	}
	
	public static int[] fusion(int[] array1, int[] array2)
	{
		int[] array = new int[array1.length + array2.length];
		int i1 = 0, i2 = 0;
		
		for (int i = 0 ; i < array.length ; ++i)
		{
			if (i1 == array1.length)
				array[i] = array2[i2++];
			else if (i2 == array2.length)
				array[i] = array1[i1++];
			
			else if (array1[i1] <= array2[i2])
				array[i] = array1[i1++];
			else
				array[i] = array2[i2++];
		}
		
		return array;
	}
	/// 2.0.1.3
	
	/// 4.2.1
	public static double maxDistance(double[][] polygone)
	{
		int n = polygone.length;
		double diagMax = Double.MIN_VALUE;

		for (int i = 0 ; i < n-1 ; ++i)
		{
			for (int j = i+2 ; j < Math.min(i+n-1, n) ; ++j)
			{				
				System.out.println(i + " : " + j);
				
				double a1 = polygone[i][0], b1 = polygone[i][1];
				double a2 = polygone[j][0], b2 = polygone[j][1];
				
				double diag = Math.sqrt((a1-a2)*(a1-a2) + (b1-b2)*(b1-b2));
				
				if (diag > diagMax)
					diagMax = diag;
			}
		}
		
		return diagMax;
	}
	/// 4.2.1
	
	

	
	private static int [] generate(int size)
	{
		int [] tab = new int[size];
		Random rand = new Random();
		
		for(int i =0; i< tab.length; i++)
			tab[i] = rand.nextInt();
		
		return tab;
	}
}
