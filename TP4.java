package tp4;

import java.util.*;

public class TP4
{
	public static void main(String[] args)
	{
		long[][] resultats = new long[2][10];
		long startTime;
		
		for (int i = 100 ; i <= 1000 ; i+=100)
		{
			int[] array = generate(i);
	
			startTime = System.nanoTime();
				triFusion(array, 0, array.length);
			resultats[0][i/100 -1] = System.nanoTime() - startTime;
	
			startTime = System.nanoTime();
				triRapide(array, 0, array.length-1);
			resultats[1][i/100 -1] = System.nanoTime() - startTime;
		}
		
		// Pour afficher avec MatLab
		System.out.print("\nf = [");
		for (int i = 0 ; i < resultats[0].length ; i++)
		{
			if (i < resultats[0].length-1)
				System.out.print(resultats[0][i] + ", ");
			else
				System.out.print(resultats[0][i] + "];");
		}
		
		System.out.print("\nr = [");
		for (int i = 0 ; i < resultats[1].length ; i++)
		{
			if (i < resultats[1].length-1)
				System.out.print(resultats[1][i] + ", ");
			else
				System.out.print(resultats[1][i] + "];");
		}
	}
	
	/// 2.0.1.3
	public static int[] triFusion(int[] tableau, int debut, int fin)
	{
		int taille = fin-debut;
		
		if (taille == 0)
			System.out.println("ca va pas");
		
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
	
	
//	public static int[] quicksort(int[] array)
//	{
//		
//	}

	public static int[] triRapide(int[] array, int debut, int fin)
	{
		int[] sorted = new int[array.length];
		System.arraycopy(array, 0, sorted, 0, array.length);
		
		quickSort(sorted, debut, fin);
		
		return sorted;
	}

	public static void quickSort(int[] array, int debut, int fin)
	{
		int pivot;
		
		if (debut < fin)
		{
			pivot = (int) Math.floor((debut+fin) / 2);
			pivot = partitionnement(array, debut, fin, pivot);
			quickSort(array, debut, pivot-1);
			quickSort(array, pivot+1, fin);
		}
	}

	public static int partitionnement(int[] array, int debut, int fin, int pivot)
	{
		swap(array, pivot, fin);
		
		int j = debut;
		for (int i = debut ; i  < fin ; ++i)
		{
			if (array[i] <= array[fin])
				swap(array, i, j++);
		}
		
		swap(array, fin, j);
		return j;
	}
	
	
	
	/// 4.2.1
	public static double maxDistance(double[][] polygone)
	{
		int n = polygone.length;
		double diagMax = Double.MIN_VALUE;

		for (int i = 0 ; i < n-1 ; ++i)
		{
			for (int j = i+2 ; j < Math.min(i+n-1, n) ; ++j)
			{
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
		Random rand = new Random();
		
		int [] tab = new int[size];
		
		for(int i =0; i< tab.length; i++)
			tab[i] = rand.nextInt();
		
		return tab;
	}
	
	private static void swap(int[] array, int a, int b)
	{
		int temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
}
