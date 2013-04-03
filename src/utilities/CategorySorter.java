package utilities;

import edu.drake.questionapp.R;

public class CategorySorter 
{
	private static int[] position = {
			Answerer.Legit.ordinal(),
			Answerer.Anyone.ordinal(),
			Answerer.Ke$ha.ordinal(),
			Answerer.Confucius.ordinal(),
			Answerer.Lincoln.ordinal(),
			Answerer.Barack.ordinal(),
			Answerer.Gaga.ordinal(),
			Answerer.Armstrong.ordinal(),
			Answerer.Norris.ordinal(),
			Answerer.Jackson.ordinal(),
			Answerer.Ghandi.ordinal(),
			Answerer.Shakespeare.ordinal(),
			Answerer.TSwift.ordinal(),
			Answerer.HClinton.ordinal(),
			Answerer.Palin.ordinal(),
			Answerer.Wonderwoman.ordinal(),
			Answerer.Batman.ordinal(),
			Answerer.Jesus.ordinal(),
			Answerer.Pope.ordinal(),
			Answerer.Bragnelina.ordinal(),
			Answerer.Arnold.ordinal(),
			Answerer.Bond.ordinal(),
			Answerer.Snape.ordinal(),
			Answerer.Dumbledore.ordinal()
	};
	
	private static int[] drawableID = {
		R.drawable.legit,
		R.drawable.anyone,
		R.drawable.kesha,
		R.drawable.confucius,
		R.drawable.lincoln,
		R.drawable.obama,
		R.drawable.gaga,
		R.drawable.armstrong,
		R.drawable.norris,
		R.drawable.jackson,
		R.drawable.ghandi,
		R.drawable.shakespeare,
		R.drawable.tswift,
		R.drawable.clinton,
		R.drawable.palin,
		R.drawable.wonderwoman,
		R.drawable.batman,
		R.drawable.jesus,
		R.drawable.pope,
		R.drawable.brangelina,
		R.drawable.arnold,
		R.drawable.bond,
		R.drawable.snape,
		R.drawable.dumbledore			
	};
	
	// returns the answerer in the category page at position i
	public static int getPosition(int i)
	{
		return position[i];
	}
	
	// returns the r.drawable id of the answerer i
	public static int getDrawable(int i)
	{
		return drawableID[i];
	}
	
	public static int getLength()
	{
		return position.length;
	}
}
